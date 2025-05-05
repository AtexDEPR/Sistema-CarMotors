package com.carmotorsproject.campaigns.controller;

import com.carmotorsproject.campaigns.model.CampaignDAO;
import com.carmotorsproject.campaigns.model.CampaignAppointmentDAO;
import com.carmotorsproject.campaigns.model.Campaign;
import com.carmotorsproject.campaigns.model.CampaignAppointment;
import com.carmotorsproject.campaigns.model.DAOFactory;
import com.carmotorsproject.campaigns.views.CampaignView;
import com.carmotorsproject.campaigns.views.CampaignReportView;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Controller for campaign-related operations.
 * Mediates between the CampaignView and the CampaignDAO.
 */
public class CampaignController {

    private static final Logger LOGGER = Logger.getLogger(CampaignController.class.getName());

    // Views
    private final CampaignView view;
    private CampaignReportView reportView;

    // DAOs
    private final CampaignDAO campaignDAO;
    private final CampaignAppointmentDAO appointmentDAO;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The campaign view
     */
    public CampaignController(CampaignView view) {
        this.view = view;
        this.campaignDAO = DAOFactory.getCampaignDAO();
        this.appointmentDAO = DAOFactory.getCampaignAppointmentDAO();
        LOGGER.log(Level.INFO, "CampaignController initialized");
    }

    /**
     * Sets the report view.
     *
     * @param reportView The campaign report view
     */
    public void setReportView(CampaignReportView reportView) {
        this.reportView = reportView;
    }

    /**
     * Loads all campaigns and updates the view.
     */
    public void loadCampaigns() {
        try {
            List<Campaign> campaigns = campaignDAO.findAll();
            view.updateCampaignTable(campaigns);
            LOGGER.log(Level.INFO, "Loaded {0} campaigns", campaigns.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading campaigns", ex);
            view.showError("Error loading campaigns: " + ex.getMessage());
        }
    }

    /**
     * Loads the details of a specific campaign and updates the view.
     *
     * @param campaignId The ID of the campaign to load
     */
    public void loadCampaignDetails(int campaignId) {
        try {
            Campaign campaign = campaignDAO.findById(campaignId);
            if (campaign != null) {
                view.populateCampaignForm(campaign);
                LOGGER.log(Level.INFO, "Loaded details for campaign ID: {0}", campaignId);
            } else {
                LOGGER.log(Level.WARNING, "Campaign not found with ID: {0}", campaignId);
                view.showError("Campaign not found with ID: " + campaignId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading campaign details", ex);
            view.showError("Error loading campaign details: " + ex.getMessage());
        }
    }

    /**
     * Adds a new campaign.
     *
     * @param campaign The campaign to add
     */
    public void addCampaign(Campaign campaign) {
        try {
            // Validate the campaign
            if (!validateCampaign(campaign)) {
                return;
            }

            // Save the campaign
            Campaign savedCampaign = campaignDAO.save(campaign);

            // Reload the campaign list
            loadCampaigns();

            // Show success message
            view.showSuccess("Campaign created successfully with ID: " + savedCampaign.getCampaignId());

            LOGGER.log(Level.INFO, "Added campaign with ID: {0}", savedCampaign.getCampaignId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding campaign", ex);
            view.showError("Error adding campaign: " + ex.getMessage());
        }
    }

    /**
     * Updates an existing campaign.
     *
     * @param campaign The campaign to update
     */
    public void updateCampaign(Campaign campaign) {
        try {
            // Validate the campaign
            if (!validateCampaign(campaign)) {
                return;
            }

            // Update the campaign
            Campaign updatedCampaign = campaignDAO.update(campaign);

            // Reload the campaign list
            loadCampaigns();

            // Show success message
            view.showSuccess("Campaign updated successfully with ID: " + updatedCampaign.getCampaignId());

            LOGGER.log(Level.INFO, "Updated campaign with ID: {0}", updatedCampaign.getCampaignId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating campaign", ex);
            view.showError("Error updating campaign: " + ex.getMessage());
        }
    }

    /**
     * Deletes a campaign.
     *
     * @param campaignId The ID of the campaign to delete
     */
    public void deleteCampaign(int campaignId) {
        try {
            // Check if there are appointments for this campaign
            List<CampaignAppointment> appointments = appointmentDAO.findByCampaign(campaignId);
            if (!appointments.isEmpty()) {
                view.showError("Cannot delete campaign with ID: " + campaignId +
                        ". There are " + appointments.size() + " appointments associated with it.");
                return;
            }

            boolean deleted = campaignDAO.delete(campaignId);
            if (deleted) {
                // Reload the campaign list
                loadCampaigns();

                // Show success message
                view.showSuccess("Campaign deleted successfully with ID: " + campaignId);

                LOGGER.log(Level.INFO, "Deleted campaign with ID: {0}", campaignId);
            } else {
                LOGGER.log(Level.WARNING, "Campaign not found with ID: {0}", campaignId);
                view.showError("Campaign not found with ID: " + campaignId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting campaign", ex);
            view.showError("Error deleting campaign: " + ex.getMessage());
        }
    }

    /**
     * Searches for campaigns by name.
     *
     * @param name The name to search for
     */
    public void searchByName(String name) {
        try {
            List<Campaign> campaigns = campaignDAO.findByName(name);
            view.updateCampaignTable(campaigns);
            LOGGER.log(Level.INFO, "Found {0} campaigns matching name: {1}",
                    new Object[]{campaigns.size(), name});

            if (campaigns.isEmpty()) {
                view.showInfo("No campaigns found matching name: " + name);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching campaigns by name", ex);
            view.showError("Error searching campaigns by name: " + ex.getMessage());
        }
    }

    /**
     * Searches for campaigns by date range.
     *
     * @param startDate The start date
     * @param endDate The end date
     */
    public void searchByDateRange(Date startDate, Date endDate) {
        try {
            List<Campaign> campaigns = campaignDAO.findByDateRange(startDate, endDate);
            view.updateCampaignTable(campaigns);
            LOGGER.log(Level.INFO, "Found {0} campaigns in date range: {1} to {2}",
                    new Object[]{campaigns.size(), startDate, endDate});

            if (campaigns.isEmpty()) {
                view.showInfo("No campaigns found in date range: " + startDate + " to " + endDate);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching campaigns by date range", ex);
            view.showError("Error searching campaigns by date range: " + ex.getMessage());
        }
    }

    /**
     * Searches for active campaigns.
     */
    public void searchActive() {
        try {
            List<Campaign> campaigns = campaignDAO.findActive();
            view.updateCampaignTable(campaigns);
            LOGGER.log(Level.INFO, "Found {0} active campaigns", campaigns.size());

            if (campaigns.isEmpty()) {
                view.showInfo("No active campaigns found.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching active campaigns", ex);
            view.showError("Error searching active campaigns: " + ex.getMessage());
        }
    }

    /**
     * Searches for campaigns by target vehicle type.
     *
     * @param vehicleType The target vehicle type
     */
    public void searchByTargetVehicleType(String vehicleType) {
        try {
            List<Campaign> campaigns = campaignDAO.findByTargetVehicleType(vehicleType);
            view.updateCampaignTable(campaigns);
            LOGGER.log(Level.INFO, "Found {0} campaigns for vehicle type: {1}",
                    new Object[]{campaigns.size(), vehicleType});

            if (campaigns.isEmpty()) {
                view.showInfo("No campaigns found for vehicle type: " + vehicleType);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching campaigns by vehicle type", ex);
            view.showError("Error searching campaigns by vehicle type: " + ex.getMessage());
        }
    }

    /**
     * Searches for campaigns by target customer segment.
     *
     * @param customerSegment The target customer segment
     */
    public void searchByTargetCustomerSegment(int customerSegment) {
        try {
            List<Campaign> campaigns = campaignDAO.findByTargetCustomerSegment(customerSegment);
            view.updateCampaignTable(campaigns);
            LOGGER.log(Level.INFO, "Found {0} campaigns for customer segment: {1}",
                    new Object[]{campaigns.size(), customerSegment});

            if (campaigns.isEmpty()) {
                view.showInfo("No campaigns found for customer segment: " + customerSegment);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching campaigns by customer segment", ex);
            view.showError("Error searching campaigns by customer segment: " + ex.getMessage());
        }
    }

    /**
     * Opens the campaign report view.
     */
    public void openReportView() {
        if (reportView != null) {
            reportView.setVisible(true);
            LOGGER.log(Level.INFO, "Opened campaign report view");
        } else {
            LOGGER.log(Level.WARNING, "Campaign report view is not set");
            view.showError("Campaign report view is not available.");
        }
    }

    /**
     * Validates a campaign.
     *
     * @param campaign The campaign to validate
     * @return true if the campaign is valid, false otherwise
     */
    private boolean validateCampaign(Campaign campaign) {
        // Check name
        if (campaign.getName() == null || campaign.getName().trim().isEmpty()) {
            view.showError("Campaign name cannot be empty.");
            return false;
        }

        // Check dates
        if (campaign.getStartDate() == null) {
            view.showError("Start date cannot be empty.");
            return false;
        }

        if (campaign.getEndDate() != null && campaign.getEndDate().before(campaign.getStartDate())) {
            view.showError("End date cannot be before start date.");
            return false;
        }

        // Check discount percentage
        if (campaign.getDiscountPercentage() < 0 || campaign.getDiscountPercentage() > 100) {
            view.showError("Discount percentage must be between 0 and 100.");
            return false;
        }

        return true;
    }
}