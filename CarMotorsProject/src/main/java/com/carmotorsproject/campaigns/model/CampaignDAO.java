package com.carmotorsproject.campaigns.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of CampaignDAOInterface
 */
public class CampaignDAO implements CampaignDAOInterface {
    
    // In-memory storage for campaigns (would be replaced with database in production)
    private static final Map<String, Campaign> campaigns = new HashMap<>();
    
    @Override
    public boolean save(Campaign campaign) {
        try {
            campaigns.put(campaign.getId(), campaign);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(Campaign campaign) {
        try {
            if (!campaigns.containsKey(campaign.getId())) {
                return false;
            }
            
            campaign.setLastModifiedDate(new Date());
            campaigns.put(campaign.getId(), campaign);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(String campaignId) {
        try {
            if (!campaigns.containsKey(campaignId)) {
                return false;
            }
            
            campaigns.remove(campaignId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Campaign getById(String campaignId) {
        return campaigns.get(campaignId);
    }
    
    @Override
    public List<Campaign> getAll() {
        return new ArrayList<>(campaigns.values());
    }
    
    @Override
    public List<Campaign> search(String searchTerm) {
        String term = searchTerm.toLowerCase();
        
        return campaigns.values().stream()
                .filter(c -> c.getName().toLowerCase().contains(term) || 
                       (c.getDescription() != null && c.getDescription().toLowerCase().contains(term)))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Campaign> getByType(String type) {
        return campaigns.values().stream()
                .filter(c -> c.getType().equals(type))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Campaign> getByStatus(boolean active) {
        return campaigns.values().stream()
                .filter(c -> c.isActive() == active)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Campaign> getByDateRange(Date startDate, Date endDate) {
        return campaigns.values().stream()
                .filter(c -> (c.getStartDate().after(startDate) || c.getStartDate().equals(startDate)) && 
                       (c.getEndDate().before(endDate) || c.getEndDate().equals(endDate)))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getAllTypes() {
        return campaigns.values().stream()
                .map(Campaign::getType)
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // Total campaigns
        statistics.put("totalCampaigns", campaigns.size());
        
        // Active campaigns
        long activeCampaigns = campaigns.values().stream()
                .filter(Campaign::isActive)
                .count();
        statistics.put("activeCampaigns", activeCampaigns);
        
        // Current campaigns (based on date)
        Date now = new Date();
        long currentCampaigns = campaigns.values().stream()
                .filter(c -> c.isActive() && c.getStartDate().before(now) && c.getEndDate().after(now))
                .count();
        statistics.put("currentCampaigns", currentCampaigns);
        
        // Campaigns by type
        Map<String, Long> campaignsByType = campaigns.values().stream()
                .collect(Collectors.groupingBy(Campaign::getType, Collectors.counting()));
        statistics.put("campaignsByType", campaignsByType);
        
        // Average discount
        double avgDiscount = campaigns.values().stream()
                .mapToDouble(Campaign::getDiscount)
                .average()
                .orElse(0);
        statistics.put("averageDiscount", avgDiscount);
        
        return statistics;
    }
    
    @Override
    public List<Campaign> getActiveCampaigns() {
        Date now = new Date();
        
        return campaigns.values().stream()
                .filter(c -> c.isActive() && c.getStartDate().before(now) && c.getEndDate().after(now))
                .collect(Collectors.toList());
    }
}