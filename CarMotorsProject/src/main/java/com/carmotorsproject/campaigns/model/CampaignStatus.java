package com.carmotorsproject.campaigns.model;

/**
 * Enumeration of possible campaign statuses
 */
public enum CampaignStatus {
    
    DRAFT("Borrador"),
    ACTIVE("Activa"),
    PAUSED("Pausada"),
    COMPLETED("Completada"),
    CANCELLED("Cancelada");
    
    private final String displayName;
    
    /**
     * Constructor
     * 
     * @param displayName Display name in Spanish
     */
    CampaignStatus(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Get the display name
     * 
     * @return Display name in Spanish
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get CampaignStatus from display name
     * 
     * @param displayName Display name in Spanish
     * @return CampaignStatus enum value
     */
    public static CampaignStatus fromDisplayName(String displayName) {
        for (CampaignStatus status : CampaignStatus.values()) {
            if (status.getDisplayName().equals(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No CampaignStatus with display name: " + displayName);
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}