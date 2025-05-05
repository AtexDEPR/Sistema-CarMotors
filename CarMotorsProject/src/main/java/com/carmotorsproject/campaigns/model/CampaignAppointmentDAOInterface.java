package com.carmotorsproject.campaigns.model;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interface for campaign appointment data access operations.
 */
public interface CampaignAppointmentDAOInterface {

    /**
     * Saves a new campaign appointment.
     *
     * @param appointment The campaign appointment to save
     * @return The saved campaign appointment with its ID
     * @throws SQLException If a database error occurs
     */
    CampaignAppointment save(CampaignAppointment appointment) throws SQLException;

    /**
     * Updates an existing campaign appointment.
     *
     * @param appointment The campaign appointment to update
     * @return The updated campaign appointment
     * @throws SQLException If a database error occurs
     */
    CampaignAppointment update(CampaignAppointment appointment) throws SQLException;

    /**
     * Deletes a campaign appointment by ID.
     *
     * @param id The ID of the campaign appointment to delete
     * @return true if the campaign appointment was deleted, false otherwise
     * @throws SQLException If a database error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds a campaign appointment by ID.
     *
     * @param id The ID of the campaign appointment to find
     * @return The campaign appointment, or null if not found
     * @throws SQLException If a database error occurs
     */
    CampaignAppointment findById(int id) throws SQLException;

    /**
     * Finds all campaign appointments.
     *
     * @return A list of all campaign appointments
     * @throws SQLException If a database error occurs
     */
    List<CampaignAppointment> findAll() throws SQLException;

    /**
     * Finds campaign appointments by campaign ID.
     *
     * @param campaignId The campaign ID to search for
     * @return A list of campaign appointments for the campaign
     * @throws SQLException If a database error occurs
     */
    List<CampaignAppointment> findByCampaign(int campaignId) throws SQLException;

    /**
     * Finds campaign appointments by vehicle ID.
     *
     * @param vehicleId The vehicle ID to search for
     * @return A list of campaign appointments for the vehicle
     * @throws SQLException If a database error occurs
     */
    List<CampaignAppointment> findByVehicle(int vehicleId) throws SQLException;

    /**
     * Finds campaign appointments by customer ID.
     *
     * @param customerId The customer ID to search for
     * @return A list of campaign appointments for the customer
     * @throws SQLException If a database error occurs
     */
    List<CampaignAppointment> findByCustomer(int customerId) throws SQLException;

    /**
     * Finds campaign appointments by date range.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return A list of campaign appointments within the date range
     * @throws SQLException If a database error occurs
     */
    List<CampaignAppointment> findByDateRange(Date startDate, Date endDate) throws SQLException;

    /**
     * Finds campaign appointments by status.
     *
     * @param status The status to search for
     * @return A list of campaign appointments with the status
     * @throws SQLException If a database error occurs
     */
    List<CampaignAppointment> findByStatus(CampaignStatus status) throws SQLException;

    /**
     * Updates the status of a campaign appointment.
     *
     * @param id The ID of the campaign appointment
     * @param status The new status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database error occurs
     */
    boolean updateStatus(int id, CampaignStatus status) throws SQLException;
}