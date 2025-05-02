package com.carmotorsproject.campaigns.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface for Campaign Data Access Object
 */
public interface CampaignDAOInterface {
    
    /**
     * Save a new campaign
     * 
     * @param campaign Campaign to save
     * @return true if successful
     */
    boolean save(Campaign campaign);
    
    /**
     * Update an existing campaign
     * 
     * @param campaign Campaign to update
     * @return true if successful
     */
    boolean update(Campaign campaign);
    
    /**
     * Delete a campaign
     * 
     * @param campaignId Campaign ID
     * @return true if successful
     */
    boolean delete(String campaignId);
    
    /**
     * Get a campaign by ID
     * 
     * @param campaignId Campaign ID
     * @return Campaign object or null if not found
     */
    Campaign getById(String campaignId);
    
    /**
     * Get all campaigns
     * 
     * @return List of all campaigns
     */
    List<Campaign> getAll();
    
    /**
     * Search campaigns by name or description
     * 
     * @param searchTerm Search term
     * @return List of matching campaigns
     */
    List<Campaign> search(String searchTerm);
    
    /**
     * Get campaigns by type
     * 
     * @param type Campaign type
     * @return List of campaigns of the specified type
     */
    List<Campaign> getByType(String type);
    
    /**
     * Get campaigns by status
     * 
     * @param active Active status
     * @return List of campaigns with the specified status
     */
    List<Campaign> getByStatus(boolean active);
    
    /**
     * Get campaigns by date range
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return List of campaigns within the date range
     */
    List<Campaign> getByDateRange(Date startDate, Date endDate);
    
    /**
     * Get all campaign types
     * 
     * @return List of campaign types
     */
    List<String> getAllTypes();
    
    /**
     * Get campaign statistics
     * 
     * @return Map with campaign statistics
     */
    Map<String, Object> getStatistics();
    
    /**
     * Get active campaigns
     * 
     * @return List of active campaigns
     */
    List<Campaign> getActiveCampaigns();
}