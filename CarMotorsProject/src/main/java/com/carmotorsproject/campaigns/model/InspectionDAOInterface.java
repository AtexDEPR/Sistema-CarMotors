package com.carmotorsproject.campaigns.model;

import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.services.model.Vehicle;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface for Inspection Data Access Object
 */
public interface InspectionDAOInterface {
    
    /**
     * Save a new inspection
     * 
     * @param inspection Inspection to save
     * @param inspectionItems List of inspection items
     * @return true if successful
     */
    boolean save(Inspection inspection, List<InspectionResult> inspectionItems);
    
    /**
     * Update an existing inspection
     * 
     * @param inspection Inspection to update
     * @return true if successful
     */
    boolean update(Inspection inspection);
    
    /**
     * Delete an inspection
     * 
     * @param inspectionId Inspection ID
     * @return true if successful
     */
    boolean delete(String inspectionId);
    
    /**
     * Get an inspection by ID
     * 
     * @param inspectionId Inspection ID
     * @return Inspection object or null if not found
     */
    Inspection getById(String inspectionId);
    
    /**
     * Get all inspections
     * 
     * @return List of all inspections
     */
    List<Inspection> getAll();
    
    /**
     * Get inspections by vehicle
     * 
     * @param vehicleId Vehicle ID
     * @return List of inspections for the specified vehicle
     */
    List<Inspection> getByVehicle(String vehicleId);
    
    /**
     * Get inspections by customer
     * 
     * @param customerId Customer ID
     * @return List of inspections for the specified customer
     */
    List<Inspection> getByCustomer(String customerId);
    
    /**
     * Get inspections by date range
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return List of inspections within the date range
     */
    List<Inspection> getByDateRange(Date startDate, Date endDate);
    
    /**
     * Get inspections by overall condition
     * 
     * @param condition Overall condition
     * @return List of inspections with the specified condition
     */
    List<Inspection> getByCondition(String condition);
    
    /**
     * Get inspection items for an inspection
     * 
     * @param inspectionId Inspection ID
     * @return List of inspection items
     */
    List<InspectionResult> getInspectionItems(String inspectionId);
    
    /**
     * Get all inspection conditions
     * 
     * @return List of inspection conditions
     */
    List<String> getAllConditions();
    
    /**
     * Get all inspection item categories
     * 
     * @return List of inspection item categories
     */
    List<String> getAllItemCategories();
    
    /**
     * Get default inspection items by vehicle type
     * 
     * @param vehicleType Vehicle type
     * @return List of default inspection items
     */
    List<InspectionResult> getDefaultItems(String vehicleType);
    
    /**
     * Generate inspection report
     * 
     * @param inspection Inspection
     * @param inspectionItems Inspection items
     * @param vehicle Vehicle
     * @param customer Customer
     * @return true if successful
     */
    boolean generateReport(Inspection inspection, List<InspectionResult> inspectionItems, 
                          Vehicle vehicle, Customer customer);
    
    /**
     * Get inspection statistics
     * 
     * @return Map with inspection statistics
     */
    Map<String, Object> getStatistics();
}