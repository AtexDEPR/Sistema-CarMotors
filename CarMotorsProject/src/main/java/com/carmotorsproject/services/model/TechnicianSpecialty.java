/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

/**
 * Enum representing the different specialties a technician can have.
 * Corresponds to the 'specialty' column in the 'technicians' table.
 */
public enum TechnicianSpecialty {
    /**
     * Mechanical specialist (e.g., engine, transmission)
     */
    MECHANICAL,

    /**
     * Electrical specialist (e.g., electronics, wiring)
     */
    ELECTRICAL,

    /**
     * Body specialist (e.g., panels, painting)
     */
    BODY,

    /**
     * General technician (multiple areas of expertise)
     */
    GENERAL;

    /**
     * Converts a string to the corresponding TechnicianSpecialty enum value.
     * Case-insensitive.
     *
     * @param specialtyStr The string representation of the technician specialty
     * @return The corresponding TechnicianSpecialty enum value
     * @throws IllegalArgumentException If the string does not match any TechnicianSpecialty
     */
    public static TechnicianSpecialty fromString(String specialtyStr) {
        for (TechnicianSpecialty specialty : TechnicianSpecialty.values()) {
            if (specialty.name().equalsIgnoreCase(specialtyStr)) {
                return specialty;
            }
        }
        throw new IllegalArgumentException("Unknown technician specialty: " + specialtyStr);
    }
}