package com.carmotorsproject.campaigns.controller;

import com.carmotorsproject.campaigns.model.Campaign;
import com.carmotorsproject.campaigns.service.CampaignService;
import com.carmotorsproject.campaigns.views.CampaignView;
import com.carmotorsproject.utils.ValidationUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing marketing campaigns
 */
public class CampaignController {
    
    private CampaignService campaignService;
    private CampaignView campaignView;
    
    public CampaignController(CampaignService campaignService, CampaignView campaignView) {
        this.campaignService = campaignService;
        this.campaignView = campaignView;
        
        // Initialize view with controller reference
        this.campaignView.setController(this);
        
        // Load initial data
        loadAllCampaigns();
    }
    
    /**
     * Load all campaigns and update the view
     */
    public void loadAllCampaigns() {
        List<Campaign> campaigns = campaignService.getAllCampaigns();
        campaignView.updateCampaignTable(campaigns);
    }
    
    /**
     * Create a new campaign
     * 
     * @param name Campaign name
     * @param description Campaign description
     * @param startDate Start date
     * @param endDate End date
     * @param discount Discount percentage
     * @param type Campaign type
     * @return True if campaign was created successfully
     */
    public boolean createCampaign(String name, String description, Date startDate, 
                                 Date endDate, double discount, String type) {
        
        // Validate input
        Map<String, String> validationErrors = validateCampaignInput(name, startDate, endDate, discount);
        
        if (!validationErrors.isEmpty()) {
            campaignView.showValidationErrors(validationErrors);
            return false;
        }
        
        // Create campaign object
        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setDescription(description);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        campaign.setDiscount(discount);
        campaign.setType(type);
        campaign.setActive(true);
        campaign.setCreationDate(new Date());
        
        // Save campaign
        boolean success = campaignService.saveCampaign(campaign);
        
        if (success) {
            // Refresh campaign list
            loadAllCampaigns();
            campaignView.showSuccessMessage("Campaña creada exitosamente");
        } else {
            campaignView.showErrorMessage("Error al crear la campaña");
        }
        
        return success;
    }
    
    /**
     * Update an existing campaign
     * 
     * @param campaignId Campaign ID
     * @param name Campaign name
     * @param description Campaign description
     * @param startDate Start date
     * @param endDate End date
     * @param discount Discount percentage
     * @param type Campaign type
     * @param active Active status
     * @return True if campaign was updated successfully
     */
    public boolean updateCampaign(String campaignId, String name, String description, 
                                 Date startDate, Date endDate, double discount, 
                                 String type, boolean active) {
        
        // Validate input
        Map<String, String> validationErrors = validateCampaignInput(name, startDate, endDate, discount);
        
        if (!validationErrors.isEmpty()) {
            campaignView.showValidationErrors(validationErrors);
            return false;
        }
        
        // Get existing campaign
        Campaign campaign = campaignService.getCampaignById(campaignId);
        
        if (campaign == null) {
            campaignView.showErrorMessage("Campaña no encontrada");
            return false;
        }
        
        // Update campaign object
        campaign.setName(name);
        campaign.setDescription(description);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        campaign.setDiscount(discount);
        campaign.setType(type);
        campaign.setActive(active);
        campaign.setLastModifiedDate(new Date());
        
        // Save updated campaign
        boolean success = campaignService.updateCampaign(campaign);
        
        if (success) {
            // Refresh campaign list
            loadAllCampaigns();
            campaignView.showSuccessMessage("Campaña actualizada exitosamente");
        } else {
            campaignView.showErrorMessage("Error al actualizar la campaña");
        }
        
        return success;
    }
    
    /**
     * Delete a campaign
     * 
     * @param campaignId Campaign ID
     * @return True if campaign was deleted successfully
     */
    public boolean deleteCampaign(String campaignId) {
        boolean success = campaignService.deleteCampaign(campaignId);
        
        if (success) {
            // Refresh campaign list
            loadAllCampaigns();
            campaignView.showSuccessMessage("Campaña eliminada exitosamente");
        } else {
            campaignView.showErrorMessage("Error al eliminar la campaña");
        }
        
        return success;
    }
    
    /**
     * Search campaigns by name or description
     * 
     * @param searchTerm Search term
     */
    public void searchCampaigns(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadAllCampaigns();
            return;
        }
        
        List<Campaign> campaigns = campaignService.searchCampaigns(searchTerm);
        campaignView.updateCampaignTable(campaigns);
    }
    
    /**
     * Filter campaigns by type
     * 
     * @param type Campaign type
     */
    public void filterCampaignsByType(String type) {
        if (type == null || type.equals("Todos")) {
            loadAllCampaigns();
            return;
        }
        
        List<Campaign> campaigns = campaignService.getCampaignsByType(type);
        campaignView.updateCampaignTable(campaigns);
    }
    
    /**
     * Filter campaigns by status (active/inactive)
     * 
     * @param active Active status
     */
    public void filterCampaignsByStatus(boolean active) {
        List<Campaign> campaigns = campaignService.getCampaignsByStatus(active);
        campaignView.updateCampaignTable(campaigns);
    }
    
    /**
     * Get all campaign types
     * 
     * @return List of campaign types
     */
    public List<String> getAllCampaignTypes() {
        return campaignService.getAllCampaignTypes();
    }
    
    /**
     * Validate campaign input
     * 
     * @param name Campaign name
     * @param startDate Start date
     * @param endDate End date
     * @param discount Discount percentage
     * @return Map of validation errors
     */
    private Map<String, String> validateCampaignInput(String name, Date startDate, 
                                                     Date endDate, double discount) {
        Map<String, String> errors = ValidationUtil.createErrorMap();
        
        // Validate name
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "El nombre de la campaña es obligatorio");
        }
        
        // Validate dates
        if (startDate == null) {
            errors.put("startDate", "La fecha de inicio es obligatoria");
        }
        
        if (endDate == null) {
            errors.put("endDate", "La fecha de fin es obligatoria");
        }
        
        if (startDate != null && endDate != null && startDate.after(endDate)) {
            errors.put("dateRange", "La fecha de inicio debe ser anterior a la fecha de fin");
        }
        
        // Validate discount
        if (discount < 0 || discount > 100) {
            errors.put("discount", "El descuento debe estar entre 0 y 100");
        }
        
        return errors;
    }
    
    /**
     * Get campaign statistics
     * 
     * @return Map with campaign statistics
     */
    public Map<String, Object> getCampaignStatistics() {
        return campaignService.getCampaignStatistics();
    }
}