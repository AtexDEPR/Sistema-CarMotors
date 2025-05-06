/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

/**
 * Enum representing the different statuses a technician can have.
 * Corresponds to the 'status' column in the 'technicians' table.
 */
public enum TechnicianStatus {
    /**
     * Technician is currently active and available for work
     */
    ACTIVE,

    /**
     * Technician is inactive (e.g., no longer employed)
     */
    INACTIVE,

    /**
     * Technician is on vacation or leave
     */
    VACATION;

    /**
     * Converts a string to the corresponding TechnicianStatus enum value.
     * Case-insensitive.
     *
     * @param statusStr The string representation of the technician status
     * @return The corresponding TechnicianStatus enum value
     * @throws IllegalArgumentException If the string does not match any TechnicianStatus
     */
    public static TechnicianStatus fromString(String statusStr) {
        for (TechnicianStatus status : TechnicianStatus.values()) {
            if (status.name().equalsIgnoreCase(statusStr)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown technician status: " + statusStr);
    }
}