package com.carmotorsproject.campaigns.model;

/**
 * Enum representing the possible results of a vehicle inspection.
 */
public enum InspectionResult {
    /**
     * The vehicle passed the inspection.
     */
    APPROVED,

    /**
     * The vehicle needs repairs before it can be approved.
     */
    REPAIRS_NEEDED,

    /**
     * The vehicle failed the inspection.
     */
    REJECTED;

    /**
     * Converts a string to an InspectionResult enum value.
     *
     * @param result The result string
     * @return The corresponding InspectionResult enum value
     */
    public static InspectionResult fromString(String result) {
        try {
            return InspectionResult.valueOf(result.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return REPAIRS_NEEDED; // Default value
        }
    }
}