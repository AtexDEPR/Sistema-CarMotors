package com.carmotorsproject.campaigns.model;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interface for campaign data access operations.
 */
public interface CampaignDAOInterface {

    /**
     * Saves a new campaign.
     *
     * @param campaign The campaign to save
     * @return The saved campaign with its ID
     * @throws SQLException If a database error occurs
     */
    Campaign save(Campaign campaign) throws SQLException;

    /**
     * Updates an existing campaign.
     *
     * @param campaign The campaign to update
     * @return The updated campaign
     * @throws SQLException If a database error occurs
     */
    Campaign update(Campaign campaign) throws SQLException;

    /**
     * Deletes a campaign by ID.
     *
     * @param id The ID of the campaign to delete
     * @return true if the campaign was deleted, false otherwise
     * @throws SQLException If a database error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds a campaign by ID.
     *
     * @param id The ID of the campaign to find
     * @return The campaign, or null if not found
     * @throws SQLException If a database error occurs
     */
    Campaign findById(int id) throws SQLException;

    /**
     * Finds all campaigns.
     *
     * @return A list of all campaigns
     * @throws SQLException If a database error occurs
     */
    List<Campaign> findAll() throws SQLException;

    /**
     * Finds campaigns by name.
     *
     * @param name The name to search for
     * @return A list of campaigns matching the name
     * @throws SQLException If a database error occurs
     */
    List<Campaign> findByName(String name) throws SQLException;

    /**
     * Finds campaigns by date range.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return A list of campaigns within the date range
     * @throws SQLException If a database error occurs
     */
    List<Campaign> findByDateRange(Date startDate, Date endDate) throws SQLException;

    /**
     * Finds active campaigns.
     *
     * @return A list of active campaigns
     * @throws SQLException If a database error occurs
     */
    List<Campaign> findActive() throws SQLException;

    /**
     * Finds campaigns by target vehicle type.
     *
     * @param vehicleType The target vehicle type
     * @return A list of campaigns targeting the vehicle type
     * @throws SQLException If a database error occurs
     */
    List<Campaign> findByTargetVehicleType(String vehicleType) throws SQLException;

    /**
     * Finds campaigns by target customer segment.
     *
     * @param customerSegment The target customer segment
     * @return A list of campaigns targeting the customer segment
     * @throws SQLException If a database error occurs
     */
    List<Campaign> findByTargetCustomerSegment(int customerSegment) throws SQLException;
}