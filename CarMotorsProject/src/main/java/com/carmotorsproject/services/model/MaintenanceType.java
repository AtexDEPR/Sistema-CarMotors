/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

/**
 * Enum representing the different types of maintenance services.
 * Corresponds to the 'maintenance_type' column in the 'services' table.
 */
public enum MaintenanceType {
    /**
     * Preventive maintenance (e.g., regular service, oil change)
     */
    PREVENTIVE,

    /**
     * Corrective maintenance (e.g., repairs, part replacements)
     */
    CORRECTIVE;

    /**
     * Converts a string to the corresponding MaintenanceType enum value.
     * Case-insensitive.
     *
     * @param typeStr The string representation of the maintenance type
     * @return The corresponding MaintenanceType enum value
     * @throws IllegalArgumentException If the string does not match any MaintenanceType
     */
    public static MaintenanceType fromString(String typeStr) {
        for (MaintenanceType type : MaintenanceType.values()) {
            if (type.name().equalsIgnoreCase(typeStr)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown maintenance type: " + typeStr);
    }
}