package com.carmotorsproject.campaigns.controller;

import com.carmotorsproject.campaigns.model.Campaign;
import com.carmotorsproject.campaigns.model.CampaignAppointment;
import com.carmotorsproject.campaigns.service.CampaignAppointmentService;
import com.carmotorsproject.campaigns.service.CampaignService;
import com.carmotorsproject.campaigns.views.CampaignAppointmentView;
import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.customers.service.CustomerService;
import com.carmotorsproject.utils.ValidationUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing appointments related to marketing campaigns
 */
public class CampaignAppointmentController {
    
    private CampaignAppointmentService appointmentService;
    private CampaignService campaignService;
    private CustomerService customerService;
    private CampaignAppointmentView appointmentView;
    
    public CampaignAppointmentController(CampaignAppointmentService appointmentService,
                                        CampaignService campaignService,
                                        CustomerService customerService,
                                        CampaignAppointmentView appointmentView) {
        this.appointmentService = appointmentService;
        this.campaignService = campaignService;
        this.customerService = customerService;
        this.appointmentView = appointmentView;
        
        // Initialize view with controller reference
        this.appointmentView.setController(this);
        
        // Load initial data
        loadAllAppointments();
    }
    
    /**
     * Load all appointments and update the view
     */
    public void loadAllAppointments() {
        List<CampaignAppointment> appointments = appointmentService.getAllAppointments();
        appointmentView.updateAppointmentTable(appointments);
    }
    
    /**
     * Load appointments for a specific campaign
     * 
     * @param campaignId Campaign ID
     */
    public void loadAppointmentsByCampaign(String campaignId) {
        List<CampaignAppointment> appointments = appointmentService.getAppointmentsByCampaign(campaignId);
        appointmentView.updateAppointmentTable(appointments);
    }
    
    /**
     * Load appointments for a specific customer
     * 
     * @param customerId Customer ID
     */
    public void loadAppointmentsByCustomer(String customerId) {
        List<CampaignAppointment> appointments = appointmentService.getAppointmentsByCustomer(customerId);
        appointmentView.updateAppointmentTable(appointments);
    }
    
    /**
     * Create a new appointment
     * 
     * @param campaignId Campaign ID
     * @param customerId Customer ID
     * @param appointmentDate Appointment date and time
     * @param notes Additional notes
     * @param vehicleId Vehicle ID (optional)
     * @return True if appointment was created successfully
     */
    public boolean createAppointment(String campaignId, String customerId, 
                                    Date appointmentDate, String notes, String vehicleId) {
        
        // Validate input
        Map<String, String> validationErrors = validateAppointmentInput(campaignId, customerId, appointmentDate);
        
        if (!validationErrors.isEmpty()) {
            appointmentView.showValidationErrors(validationErrors);
            return false;
        }
        
        // Check if campaign exists and is active
        Campaign campaign = campaignService.getCampaignById(campaignId);
        if (campaign == null) {
            appointmentView.showErrorMessage("Campaña no encontrada");
            return false;
        }
        
        if (!campaign.isActive()) {
            appointmentView.showErrorMessage("La campaña seleccionada no está activa");
            return false;
        }
        
        // Check if customer exists
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            appointmentView.showErrorMessage("Cliente no encontrado");
            return false;
        }
        
        // Create appointment object
        CampaignAppointment appointment = new CampaignAppointment();
        appointment.setCampaignId(campaignId);
        appointment.setCustomerId(customerId);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setNotes(notes);
        appointment.setVehicleId(vehicleId);
        appointment.setStatus("Programada");
        appointment.setCreationDate(new Date());
        
        // Save appointment
        boolean success = appointmentService.saveAppointment(appointment);
        
        if (success) {
            // Refresh appointment list
            loadAllAppointments();
            appointmentView.showSuccessMessage("Cita programada exitosamente");
        } else {
            appointmentView.showErrorMessage("Error al programar la cita");
        }
        
        return success;
    }
    
    /**
     * Update an existing appointment
     * 
     * @param appointmentId Appointment ID
     * @param appointmentDate Appointment date and time
     * @param notes Additional notes
     * @param status Appointment status
     * @param vehicleId Vehicle ID (optional)
     * @return True if appointment was updated successfully
     */
    public boolean updateAppointment(String appointmentId, Date appointmentDate, 
                                    String notes, String status, String vehicleId) {
        
        // Validate input
        if (appointmentDate == null) {
            appointmentView.showErrorMessage("La fecha de la cita es obligatoria");
            return false;
        }
        
        // Get existing appointment
        CampaignAppointment appointment = appointmentService.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            appointmentView.showErrorMessage("Cita no encontrada");
            return false;
        }
        
        // Update appointment object
        appointment.setAppointmentDate(appointmentDate);
        appointment.setNotes(notes);
        appointment.setStatus(status);
        appointment.setVehicleId(vehicleId);
        appointment.setLastModifiedDate(new Date());
        
        // Save updated appointment
        boolean success = appointmentService.updateAppointment(appointment);
        
        if (success) {
            // Refresh appointment list
            loadAllAppointments();
            appointmentView.showSuccessMessage("Cita actualizada exitosamente");
        } else {
            appointmentView.showErrorMessage("Error al actualizar la cita");
        }
        
        return success;
    }
    
    /**
     * Cancel an appointment
     * 
     * @param appointmentId Appointment ID
     * @param cancellationReason Reason for cancellation
     * @return True if appointment was cancelled successfully
     */
    public boolean cancelAppointment(String appointmentId, String cancellationReason) {
        // Get existing appointment
        CampaignAppointment appointment = appointmentService.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            appointmentView.showErrorMessage("Cita no encontrada");
            return false;
        }
        
        // Update appointment status
        appointment.setStatus("Cancelada");
        appointment.setNotes(appointment.getNotes() + "\nMotivo de cancelación: " + cancellationReason);
        appointment.setLastModifiedDate(new Date());
        
        // Save updated appointment
        boolean success = appointmentService.updateAppointment(appointment);
        
        if (success) {
            // Refresh appointment list
            loadAllAppointments();
            appointmentView.showSuccessMessage("Cita cancelada exitosamente");
        } else {
            appointmentView.showErrorMessage("Error al cancelar la cita");
        }
        
        return success;
    }
    
    /**
     * Complete an appointment
     * 
     * @param appointmentId Appointment ID
     * @param serviceNotes Service notes
     * @return True if appointment was marked as completed successfully
     */
    public boolean completeAppointment(String appointmentId, String serviceNotes) {
        // Get existing appointment
        CampaignAppointment appointment = appointmentService.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            appointmentView.showErrorMessage("Cita no encontrada");
            return false;
        }
        
        // Update appointment status
        appointment.setStatus("Completada");
        appointment.setNotes(appointment.getNotes() + "\nNotas de servicio: " + serviceNotes);
        appointment.setCompletionDate(new Date());
        appointment.setLastModifiedDate(new Date());
        
        // Save updated appointment
        boolean success = appointmentService.updateAppointment(appointment);
        
        if (success) {
            // Refresh appointment list
            loadAllAppointments();
            appointmentView.showSuccessMessage("Cita marcada como completada exitosamente");
        } else {
            appointmentView.showErrorMessage("Error al marcar la cita como completada");
        }
        
        return success;
    }
    
    /**
     * Filter appointments by status
     * 
     * @param status Appointment status
     */
    public void filterAppointmentsByStatus(String status) {
        if (status == null || status.equals("Todos")) {
            loadAllAppointments();
            return;
        }
        
        List<CampaignAppointment> appointments = appointmentService.getAppointmentsByStatus(status);
        appointmentView.updateAppointmentTable(appointments);
    }
    
    /**
     * Filter appointments by date range
     * 
     * @param startDate Start date
     * @param endDate End date
     */
    public void filterAppointmentsByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            appointmentView.showErrorMessage("Ambas fechas son requeridas para filtrar por rango");
            return;
        }
        
        List<CampaignAppointment> appointments = appointmentService.getAppointmentsByDateRange(startDate, endDate);
        appointmentView.updateAppointmentTable(appointments);
    }
    
    /**
     * Get all available campaigns for appointments
     * 
     * @return List of active campaigns
     */
    public List<Campaign> getAvailableCampaigns() {
        return campaignService.getActiveCampaigns();
    }
    
    /**
     * Get all appointment statuses
     * 
     * @return List of appointment statuses
     */
    public List<String> getAllAppointmentStatuses() {
        return appointmentService.getAllAppointmentStatuses();
    }
    
    /**
     * Validate appointment input
     * 
     * @param campaignId Campaign ID
     * @param customerId Customer ID
     * @param appointmentDate Appointment date
     * @return Map of validation errors
     */
    private Map<String, String> validateAppointmentInput(String campaignId, String customerId, Date appointmentDate) {
        Map<String, String> errors = ValidationUtil.createErrorMap();
        
        // Validate campaign
        if (campaignId == null || campaignId.trim().isEmpty()) {
            errors.put("campaignId", "La campaña es obligatoria");
        }
        
        // Validate customer
        if (customerId == null || customerId.trim().isEmpty()) {
            errors.put("customerId", "El cliente es obligatorio");
        }
        
        // Validate appointment date
        if (appointmentDate == null) {
            errors.put("appointmentDate", "La fecha de la cita es obligatoria");
        } else {
            Date now = new Date();
            if (appointmentDate.before(now)) {
                errors.put("appointmentDate", "La fecha de la cita debe ser futura");
            }
        }
        
        return errors;
    }
    
    /**
     * Get appointment statistics
     * 
     * @return Map with appointment statistics
     */
    public Map<String, Object> getAppointmentStatistics() {
        return appointmentService.getAppointmentStatistics();
    }
}