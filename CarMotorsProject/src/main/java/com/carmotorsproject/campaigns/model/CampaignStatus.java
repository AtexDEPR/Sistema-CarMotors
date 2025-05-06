package com.carmotorsproject.campaigns.model;

/**
 * Enum representing the possible statuses of a campaign appointment.
 */
public enum CampaignStatus {
    /**
     * The appointment is pending confirmation.
     */
    PENDING,

    /**
     * The appointment has been confirmed.
     */
    CONFIRMED,

    /**
     * The appointment has been completed.
     */
    COMPLETED,

    DRAFT,
    ACTIVE,
    SCHEDULED,
    EXPIRED,

    /**
     * The appointment has been cancelled.
     */
    CANCELLED;

    /**
     * Converts a string to a CampaignStatus enum value.
     *
     * @param status The status string
     * @return The corresponding CampaignStatus enum value
     */
    public static CampaignStatus fromString(String status) {
        try {
            return CampaignStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return PENDING; // Default value
        }
    }
}