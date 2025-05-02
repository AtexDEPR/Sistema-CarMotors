package com.carmotorsproject.campaigns.controller;

import com.carmotorsproject.campaigns.model.Inspection;
import com.carmotorsproject.campaigns.model.InspectionItem;
import com.carmotorsproject.campaigns.service.InspectionService;
import com.carmotorsproject.campaigns.views.InspectionView;
import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.customers.service.CustomerService;
import com.carmotorsproject.utils.ValidationUtil;
import com.carmotorsproject.vehicles.model.Vehicle;
import com.carmotorsproject.vehicles.service.VehicleService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing vehicle inspections
 */
public class InspectionController {
    
    private InspectionService inspectionService;
    private CustomerService customerService;
    private VehicleService vehicleService;
    private InspectionView inspectionView;
    
    public InspectionController(InspectionService inspectionService,
                               CustomerService customerService,
                               VehicleService vehicleService,
                               InspectionView inspectionView) {
        this.inspectionService = inspectionService;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
        this.inspectionView = inspectionView;
        
        // Initialize view with controller reference
        this.inspectionView.setController(this);
        
        // Load initial data
        loadAllInspections();
    }
    
    /**
     * Load all inspections and update the view
     */
    public void loadAllInspections() {
        List<Inspection> inspections = inspectionService.getAllInspections();
        inspectionView.updateInspectionTable(inspections);
    }
    
    /**
     * Load inspections for a specific vehicle
     * 
     * @param vehicleId Vehicle ID
     */
    public void loadInspectionsByVehicle(String vehicleId) {
        List<Inspection> inspections = inspectionService.getInspectionsByVehicle(vehicleId);
        inspectionView.updateInspectionTable(inspections);
    }
    
    /**
     * Load inspections for a specific customer
     * 
     * @param customerId Customer ID
     */
    public void loadInspectionsByCustomer(String customerId) {
        List<Inspection> inspections = inspectionService.getInspectionsByCustomer(customerId);
        inspectionView.updateInspectionTable(inspections);
    }
    
    /**
     * Create a new inspection
     * 
     * @param vehicleId Vehicle ID
     * @param customerId Customer ID
     * @param technicianName Technician name
     * @param notes General notes
     * @param inspectionItems List of inspection items
     * @return True if inspection was created successfully
     */
    public boolean createInspection(String vehicleId, String customerId, 
                                   String technicianName, String notes,
                                   List<InspectionItem> inspectionItems) {
        
        // Validate input
        Map<String, String> validationErrors = validateInspectionInput(vehicleId, customerId, technicianName);
        
        if (!validationErrors.isEmpty()) {
            inspectionView.showValidationErrors(validationErrors);
            return false;
        }
        
        // Check if vehicle exists
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (vehicle == null) {
            inspectionView.showErrorMessage("Vehículo no encontrado");
            return false;
        }
        
        // Check if customer exists
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            inspectionView.showErrorMessage("Cliente no encontrado");
            return false;
        }
        
        // Create inspection object
        Inspection inspection = new Inspection();
        inspection.setVehicleId(vehicleId);
        inspection.setCustomerId(customerId);
        inspection.setTechnicianName(technicianName);
        inspection.setNotes(notes);
        inspection.setInspectionDate(new Date());
        inspection.setStatus("Completada");
        
        // Calculate overall condition based on inspection items
        int totalScore = 0;
        for (InspectionItem item : inspectionItems) {
            totalScore += item.getConditionScore();
        }
        double averageScore = (double) totalScore / inspectionItems.size();
        
        if (averageScore >= 4.0) {
            inspection.setOverallCondition("Excelente");
        } else if (averageScore >= 3.0) {
            inspection.setOverallCondition("Bueno");
        } else if (averageScore >= 2.0) {
            inspection.setOverallCondition("Regular");
        } else {
            inspection.setOverallCondition("Deficiente");
        }
        
        // Save inspection and its items
        boolean success = inspectionService.saveInspection(inspection, inspectionItems);
        
        if (success) {
            // Refresh inspection list
            loadAllInspections();
            inspectionView.showSuccessMessage("Inspección guardada exitosamente");
        } else {
            inspectionView.showErrorMessage("Error al guardar la inspección");
        }
        
        return success;
    }
    
    /**
     * Get inspection details
     * 
     * @param inspectionId Inspection ID
     */
    public void getInspectionDetails(String inspectionId) {
        Inspection inspection = inspectionService.getInspectionById(inspectionId);
        
        if (inspection == null) {
            inspectionView.showErrorMessage("Inspección no encontrada");
            return;
        }
        
        List<InspectionItem> inspectionItems = inspectionService.getInspectionItems(inspectionId);
        
        // Get vehicle and customer details
        Vehicle vehicle = vehicleService.getVehicleById(inspection.getVehicleId());
        Customer customer = customerService.getCustomerById(inspection.getCustomerId());
        
        inspectionView.displayInspectionDetails(inspection, inspectionItems, vehicle, customer);
    }
    
    /**
     * Generate inspection report
     * 
     * @param inspectionId Inspection ID
     * @return True if report was generated successfully
     */
    public boolean generateInspectionReport(String inspectionId) {
        Inspection inspection = inspectionService.getInspectionById(inspectionId);
        
        if (inspection == null) {
            inspectionView.showErrorMessage("Inspección no encontrada");
            return false;
        }
        
        List<InspectionItem> inspectionItems = inspectionService.getInspectionItems(inspectionId);
        
        // Get vehicle and customer details
        Vehicle vehicle = vehicleService.getVehicleById(inspection.getVehicleId());
        Customer customer = customerService.getCustomerById(inspection.getCustomerId());
        
        boolean success = inspectionService.generateInspectionReport(inspection, inspectionItems, vehicle, customer);
        
        if (success) {
            inspectionView.showSuccessMessage("Reporte de inspección generado exitosamente");
        } else {
            inspectionView.showErrorMessage("Error al generar el reporte de inspección");
        }
        
        return success;
    }
    
    /**
     * Filter inspections by date range
     * 
     * @param startDate Start date
     * @param endDate End date
     */
    public void filterInspectionsByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            inspectionView.showErrorMessage("Ambas fechas son requeridas para filtrar por rango");
            return;
        }
        
        List<Inspection> inspections = inspectionService.getInspectionsByDateRange(startDate, endDate);
        inspectionView.updateInspectionTable(inspections);
    }
    
    /**
     * Filter inspections by overall condition
     * 
     * @param condition Overall condition
     */
    public void filterInspectionsByCondition(String condition) {
        if (condition == null || condition.equals("Todos")) {
            loadAllInspections();
            return;
        }
        
        List<Inspection> inspections = inspectionService.getInspectionsByCondition(condition);
        inspectionView.updateInspectionTable(inspections);
    }
    
    /**
     * Get all inspection conditions
     * 
     * @return List of inspection conditions
     */
    public List<String> getAllInspectionConditions() {
        return inspectionService.getAllInspectionConditions();
    }
    
    /**
     * Get all inspection item categories
     * 
     * @return List of inspection item categories
     */
    public List<String> getAllInspectionItemCategories() {
        return inspectionService.getAllInspectionItemCategories();
    }
    
    /**
     * Get default inspection items by vehicle type
     * 
     * @param vehicleType Vehicle type
     * @return List of default inspection items
     */
    public List<InspectionItem> getDefaultInspectionItems(String vehicleType) {
        return inspectionService.getDefaultInspectionItems(vehicleType);
    }
    
    /**
     * Validate inspection input
     * 
     * @param vehicleId Vehicle ID
     * @param customerId Customer ID
     * @param technicianName Technician name
     * @return Map of validation errors
     */
    private Map<String, String> validateInspectionInput(String vehicleId, String customerId, String technicianName) {
        Map<String, String> errors = ValidationUtil.createErrorMap();
        
        // Validate vehicle
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            errors.put("vehicleId", "El vehículo es obligatorio");
        }
        
        // Validate customer
        if (customerId == null || customerId.trim().isEmpty()) {
            errors.put("customerId", "El cliente es obligatorio");
        }
        
        // Validate technician
        if (technicianName == null || technicianName.trim().isEmpty()) {
            errors.put("technicianName", "El nombre del técnico es obligatorio");
        }
        
        return errors;
    }
    
    /**
     * Get inspection statistics
     * 
     * @return Map with inspection statistics
     */
    public Map<String, Object> getInspectionStatistics() {
        return inspectionService.getInspectionStatistics();
    }
    
    /**
     * Update inspection status
     * 
     * @param inspectionId Inspection ID
     * @param status New status
     * @return True if status was updated successfully
     */
    public boolean updateInspectionStatus(String inspectionId, String status) {
        Inspection inspection = inspectionService.getInspectionById(inspectionId);
        
        if (inspection == null) {
            inspectionView.showErrorMessage("Inspección no encontrada");
            return false;
        }
        
        inspection.setStatus(status);
        inspection.setLastModifiedDate(new Date());
        
        boolean success = inspectionService.updateInspection(inspection);
        
        if (success) {
            loadAllInspections();
            inspectionView.showSuccessMessage("Estado de inspección actualizado exitosamente");
        } else {
            inspectionView.showErrorMessage("Error al actualizar el estado de la inspección");
        }
        
        return success;
    }
    
    /**
     * Get vehicles for a specific customer
     * 
     * @param customerId Customer ID
     * @return List of vehicles
     */
    public List<Vehicle> getVehiclesByCustomer(String customerId) {
        return vehicleService.getVehiclesByCustomer(customerId);
    }
}