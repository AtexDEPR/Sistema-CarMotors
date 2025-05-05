/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

/**
 * Enum representing the different statuses a part can have.
 * Corresponds to the 'status' column in the 'parts' table.
 */
public enum PartStatus {
    /**
     * Part is available for use or sale
     */
    AVAILABLE,

    /**
     * Part is reserved for a specific order or service
     */
    RESERVED,

    /**
     * Part is out of service (damaged, expired, etc.)
     */
    OUT_OF_SERVICE;

    /**
     * Converts a string to the corresponding PartStatus enum value.
     * Case-insensitive.
     *
     * @param statusStr The string representation of the part status
     * @return The corresponding PartStatus enum value
     * @throws IllegalArgumentException If the string does not match any PartStatus
     */
    public static PartStatus fromString(String statusStr) {
        for (PartStatus status : PartStatus.values()) {
            if (status.name().equalsIgnoreCase(statusStr)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown part status: " + statusStr);
    }
}