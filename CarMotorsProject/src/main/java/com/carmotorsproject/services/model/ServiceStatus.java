/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

/**
 * Enum representing the different statuses a service can have.
 * Corresponds to the 'status' column in the 'services' table.
 */
public enum ServiceStatus {
    /**
     * Service is pending (not yet started)
     */
    PENDING,

    /**
     * Service is in progress (work has started)
     */
    IN_PROGRESS,

    /**
     * Service is completed (work is finished)
     */
    COMPLETED,

    /**
     * Vehicle has been delivered to the customer
     */
    DELIVERED, CANCELLED;

    /**
     * Converts a string to the corresponding ServiceStatus enum value.
     * Case-insensitive.
     *
     * @param statusStr The string representation of the service status
     * @return The corresponding ServiceStatus enum value
     * @throws IllegalArgumentException If the string does not match any ServiceStatus
     */
    public static ServiceStatus fromString(String statusStr) {
        for (ServiceStatus status : ServiceStatus.values()) {
            if (status.name().equalsIgnoreCase(statusStr)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown service status: " + statusStr);
    }
}