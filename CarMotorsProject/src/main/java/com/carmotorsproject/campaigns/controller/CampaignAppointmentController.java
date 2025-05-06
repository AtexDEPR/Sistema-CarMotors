package com.carmotorsproject.campaigns.controller;

import com.carmotorsproject.campaigns.model.Campaign;
import com.carmotorsproject.campaigns.model.CampaignAppointment;
import com.carmotorsproject.campaigns.model.CampaignStatus;
import com.carmotorsproject.campaigns.model.CampaignDAO;
import com.carmotorsproject.campaigns.model.CampaignAppointmentDAO;
import com.carmotorsproject.campaigns.views.CampaignAppointmentView;
import com.carmotorsproject.campaigns.model.DAOFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for managing appointments related to marketing campaigns
 */
public class CampaignAppointmentController {

    private static final Logger LOGGER = Logger.getLogger(CampaignAppointmentController.class.getName());

    private CampaignAppointmentDAO appointmentDAO;
    private CampaignDAO campaignDAO;
    private CampaignAppointmentView appointmentView;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The campaign appointment view
     */
    public CampaignAppointmentController(CampaignAppointmentView view) {
        this.appointmentView = view;
        this.appointmentDAO = DAOFactory.getCampaignAppointmentDAO();
        this.campaignDAO = DAOFactory.getCampaignDAO();

        // Initialize view with controller reference
        this.appointmentView.setController(this);

        // Load initial data
        loadAllAppointments();

        LOGGER.log(Level.INFO, "CampaignAppointmentController initialized");
    }

    /**
     * Load all appointments and update the view
     */
    public void loadAllAppointments() {
        try {
            List<CampaignAppointment> appointments = appointmentDAO.findAll();
            appointmentView.updateAppointmentTable(appointments);
            LOGGER.log(Level.INFO, "Loaded {0} appointments", appointments.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading appointments", ex);
            appointmentView.showError("Error loading appointments: " + ex.getMessage());
        }
    }

    /**
     * Load appointments for a specific campaign
     *
     * @param campaignId Campaign ID
     */
    public void loadAppointmentsByCampaign(int campaignId) {
        try {
            List<CampaignAppointment> appointments = appointmentDAO.findByCampaign(campaignId);
            appointmentView.updateAppointmentTable(appointments);
            LOGGER.log(Level.INFO, "Loaded {0} appointments for campaign ID: {1}",
                    new Object[]{appointments.size(), campaignId});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading appointments by campaign", ex);
            appointmentView.showError("Error loading appointments: " + ex.getMessage());
        }
    }

    /**
     * Load appointments for a specific customer
     *
     * @param customerId Customer ID
     */
    public void loadAppointmentsByCustomer(int customerId) {
        try {
            List<CampaignAppointment> appointments = appointmentDAO.findByCustomer(customerId);
            appointmentView.updateAppointmentTable(appointments);
            LOGGER.log(Level.INFO, "Loaded {0} appointments for customer ID: {1}",
                    new Object[]{appointments.size(), customerId});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading appointments by customer", ex);
            appointmentView.showError("Error loading appointments: " + ex.getMessage());
        }
    }

    /**
     * Create a new appointment
     *
     * @param appointment The appointment to create
     * @return True if appointment was created successfully
     */
    public boolean createAppointment(CampaignAppointment appointment) {
        try {
            // Validate input
            Map<String, String> validationErrors = validateAppointment(appointment);

            if (!validationErrors.isEmpty()) {
                appointmentView.showValidationErrors(validationErrors);
                return false;
            }

            // Check if campaign exists and is active
            try {
                Campaign campaign = campaignDAO.findById(appointment.getCampaignId());
                if (campaign == null) {
                    appointmentView.showError("Campaign not found with ID: " + appointment.getCampaignId());
                    return false;
                }

                if (!"ACTIVE".equals(campaign.getStatus())) {
                    appointmentView.showError("The selected campaign is not active");
                    return false;
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error checking campaign", ex);
                appointmentView.showError("Error checking campaign: " + ex.getMessage());
                return false;
            }

            // Set current date if not provided
            if (appointment.getCreatedDate() == null) {
                appointment.setCreatedDate(new Date());
            }

            // Set default status if not provided
            if (appointment.getStatus() == null) {
                appointment.setStatus(CampaignStatus.SCHEDULED);
            }

            // Save appointment
            CampaignAppointment savedAppointment = appointmentDAO.save(appointment);

            // Refresh appointment list
            loadAllAppointments();

            appointmentView.showSuccess("Appointment saved successfully with ID: " + savedAppointment.getCampaignAppointmentId());
            LOGGER.log(Level.INFO, "Created appointment with ID: {0}", savedAppointment.getCampaignAppointmentId());

            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error creating appointment", ex);
            appointmentView.showError("Error creating appointment: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Update an existing appointment
     *
     * @param appointment The appointment to update
     * @return True if appointment was updated successfully
     */
    public boolean updateAppointment(CampaignAppointment appointment) {
        try {
            // Validate input
            Map<String, String> validationErrors = validateAppointment(appointment);

            if (!validationErrors.isEmpty()) {
                appointmentView.showValidationErrors(validationErrors);
                return false;
            }

            // Update last modified date
            appointment.setLastModifiedDate(new Date());

            // Update appointment
            CampaignAppointment updatedAppointment = appointmentDAO.update(appointment);

            // Refresh appointment list
            loadAllAppointments();

            appointmentView.showSuccess("Appointment updated successfully with ID: " + updatedAppointment.getCampaignAppointmentId());
            LOGGER.log(Level.INFO, "Updated appointment with ID: {0}", updatedAppointment.getCampaignAppointmentId());

            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating appointment", ex);
            appointmentView.showError("Error updating appointment: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Cancel an appointment
     *
     * @param appointmentId Appointment ID
     * @param cancellationReason Reason for cancellation
     * @return True if appointment was cancelled successfully
     */
    public boolean cancelAppointment(int appointmentId, String cancellationReason) {
        try {
            // Get existing appointment
            CampaignAppointment appointment = appointmentDAO.findById(appointmentId);

            if (appointment == null) {
                appointmentView.showError("Appointment not found with ID: " + appointmentId);
                return false;
            }

            // Update appointment status
            appointment.setStatus(CampaignStatus.CANCELLED);
            appointment.setNotes(appointment.getNotes() + "\nCancellation reason: " + cancellationReason);
            appointment.setLastModifiedDate(new Date());

            // Update appointment
            CampaignAppointment updatedAppointment = appointmentDAO.update(appointment);

            // Refresh appointment list
            loadAllAppointments();

            appointmentView.showSuccess("Appointment cancelled successfully with ID: " + updatedAppointment.getCampaignAppointmentId());
            LOGGER.log(Level.INFO, "Cancelled appointment with ID: {0}", updatedAppointment.getCampaignAppointmentId());

            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error cancelling appointment", ex);
            appointmentView.showError("Error cancelling appointment: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Complete an appointment
     *
     * @param appointmentId Appointment ID
     * @param serviceNotes Service notes
     * @return True if appointment was marked as completed successfully
     */
    public boolean completeAppointment(int appointmentId, String serviceNotes) {
        try {
            // Get existing appointment
            CampaignAppointment appointment = appointmentDAO.findById(appointmentId);

            if (appointment == null) {
                appointmentView.showError("Appointment not found with ID: " + appointmentId);
                return false;
            }

            // Update appointment status
            appointment.setStatus(CampaignStatus.COMPLETED);
            appointment.setNotes(appointment.getNotes() + "\nService notes: " + serviceNotes);
            appointment.setLastModifiedDate(new Date());

            // Update appointment
            CampaignAppointment updatedAppointment = appointmentDAO.update(appointment);

            // Refresh appointment list
            loadAllAppointments();

            appointmentView.showSuccess("Appointment marked as completed successfully with ID: " + updatedAppointment.getCampaignAppointmentId());
            LOGGER.log(Level.INFO, "Completed appointment with ID: {0}", updatedAppointment.getCampaignAppointmentId());

            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error completing appointment", ex);
            appointmentView.showError("Error completing appointment: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Filter appointments by status
     *
     * @param status Appointment status
     */
    public void filterAppointmentsByStatus(CampaignStatus status) {
        try {
            if (status == null) {
                loadAllAppointments();
                return;
            }

            List<CampaignAppointment> appointments = appointmentDAO.findByStatus(status);
            appointmentView.updateAppointmentTable(appointments);
            LOGGER.log(Level.INFO, "Found {0} appointments with status: {1}",
                    new Object[]{appointments.size(), status});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error filtering appointments by status", ex);
            appointmentView.showError("Error filtering appointments: " + ex.getMessage());
        }
    }

    /**
     * Filter appointments by date range
     *
     * @param startDate Start date
     * @param endDate End date
     */
    public void filterAppointmentsByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            appointmentView.showError("Both dates are required to filter by range");
            return;
        }

        try {
            List<CampaignAppointment> appointments = appointmentDAO.findByDateRange(startDate, endDate);
            appointmentView.updateAppointmentTable(appointments);
            LOGGER.log(Level.INFO, "Found {0} appointments in date range: {1} to {2}",
                    new Object[]{appointments.size(), startDate, endDate});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error filtering appointments by date range", ex);
            appointmentView.showError("Error filtering appointments: " + ex.getMessage());
        }
    }

    /**
     * Get all available campaigns for appointments
     *
     * @return List of active campaigns
     */
    public List<Campaign> getAvailableCampaigns() {
        try {
            return campaignDAO.findActive();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading active campaigns", ex);
            appointmentView.showError("Error loading active campaigns: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get all appointment statuses
     *
     * @return Array of appointment statuses
     */
    public CampaignStatus[] getAllAppointmentStatuses() {
        return CampaignStatus.values();
    }

    /**
     * Validate appointment input
     *
     * @param appointment The appointment to validate
     * @return Map of validation errors
     */
    private Map<String, String> validateAppointment(CampaignAppointment appointment) {
        Map<String, String> errors = new HashMap<>();

        // Validate campaign ID
        if (appointment.getCampaignId() <= 0) {
            errors.put("campaignId", "Campaign is required");
        }

        // Validate customer ID
        if (appointment.getCustomerId() <= 0) {
            errors.put("customerId", "Customer is required");
        }

        // Validate vehicle ID
        if (appointment.getVehicleId() <= 0) {
            errors.put("vehicleId", "Vehicle is required");
        }

        // Validate appointment date
        if (appointment.getAppointmentDate() == null) {
            errors.put("appointmentDate", "Appointment date is required");
        } else {
            Date now = new Date();
            if (appointment.getAppointmentDate().before(now)) {
                errors.put("appointmentDate", "Appointment date must be in the future");
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
        Map<String, Object> statistics = new HashMap<>();

        try {
            // Get total appointments
            List<CampaignAppointment> allAppointments = appointmentDAO.findAll();
            statistics.put("totalAppointments", allAppointments.size());

            // Get appointments by status
            Map<CampaignStatus, Integer> statusCounts = new HashMap<>();
            for (CampaignStatus status : CampaignStatus.values()) {
                List<CampaignAppointment> appointments = appointmentDAO.findByStatus(status);
                statusCounts.put(status, appointments.size());
            }
            statistics.put("appointmentsByStatus", statusCounts);

            // Get upcoming appointments (next 7 days)
            Date startDate = new Date();
            Date endDate = new Date(startDate.getTime() + 7L * 24 * 60 * 60 * 1000);
            List<CampaignAppointment> upcomingAppointments = appointmentDAO.findByDateRange(startDate, endDate);
            statistics.put("upcomingAppointments", upcomingAppointments.size());

            LOGGER.log(Level.INFO, "Generated appointment statistics");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error generating appointment statistics", ex);
            statistics.put("error", "Error generating statistics: " + ex.getMessage());
        }

        return statistics;
    }

    /**
     * Delete an appointment
     *
     * @param appointmentId The ID of the appointment to delete
     * @return True if appointment was deleted successfully
     */
    public boolean deleteAppointment(int appointmentId) {
        try {
            boolean deleted = appointmentDAO.delete(appointmentId);

            if (deleted) {
                // Refresh appointment list
                loadAllAppointments();

                appointmentView.showSuccess("Appointment deleted successfully with ID: " + appointmentId);
                LOGGER.log(Level.INFO, "Deleted appointment with ID: {0}", appointmentId);
            } else {
                appointmentView.showError("Appointment not found with ID: " + appointmentId);
                LOGGER.log(Level.WARNING, "Appointment not found with ID: {0}", appointmentId);
            }

            return deleted;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting appointment", ex);
            appointmentView.showError("Error deleting appointment: " + ex.getMessage());
            return false;
        }
    }
}