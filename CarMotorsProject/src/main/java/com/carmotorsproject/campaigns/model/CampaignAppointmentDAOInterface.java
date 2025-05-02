package com.carmotorsproject.campaigns.model;

import com.carmotorsproject.customers.model.Customer;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface for CampaignAppointment Data Access Object
 */
public interface CampaignAppointmentDAOInterface {
    
    /**
     * Save a new appointment
     * 
     * @param appointment Appointment to save
     * @return true if successful
     */
    boolean save(CampaignAppointment appointment);
    
    /**
     * Update an existing appointment
     * 
     * @param appointment Appointment to update
     * @return true if successful
     */
    boolean update(CampaignAppointment appointment);
    
    /**
     * Delete an appointment
     * 
     * @param appointmentId Appointment ID
     * @return true if successful
     */
    boolean delete(String appointmentId);
    
    /**
     * Get an appointment by ID
     * 
     * @param appointmentId Appointment ID
     * @return CampaignAppointment object or null if not found
     */
    CampaignAppointment getById(String appointmentId);
    
    /**
     * Get all appointments
     * 
     * @return List of all appointments
     */
    List<CampaignAppointment> getAll();
    
    /**
     * Get appointments by campaign
     * 
     * @param campaignId Campaign ID
     * @return List of appointments for the specified campaign
     */
    List<CampaignAppointment> getByCampaign(String campaignId);
    
    /**
     * Get appointments by customer
     * 
     * @param customerId Customer ID
     * @return List of appointments for the specified customer
     */
    List<CampaignAppointment> getByCustomer(String customerId);
    
    /**
     * Get appointments by status
     * 
     * @param status Appointment status
     * @return List of appointments with the specified status
     */
    List<CampaignAppointment> getByStatus(String status);
    
    /**
     * Get appointments by date range
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return List of appointments within the date range
     */
    List<CampaignAppointment> getByDateRange(Date startDate, Date endDate);
    
    /**
     * Get all appointment statuses
     * 
     * @return List of appointment statuses
     */
    List<String> getAllStatuses();
    
    /**
     * Get appointment statistics
     * 
     * @return Map with appointment statistics
     */
    Map<String, Object> getStatistics();
    
    /**
     * Get customers for a campaign
     * 
     * @param campaignId Campaign ID
     * @return List of customers
     */
    List<Customer> getCustomersForCampaign(String campaignId);
    
    /**
     * Get appointment usage statistics
     * 
     * @return List of statistics data
     */
    List<Object[]> getUsageStatistics();
}