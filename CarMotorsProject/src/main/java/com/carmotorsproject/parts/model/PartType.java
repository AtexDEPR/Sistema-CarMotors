/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

/**
 * Enum representing the different types of parts.
 * Corresponds to the 'type' column in the 'parts' table.
 */
public enum PartType {
    /**
     * Mechanical parts (e.g., engine components, transmission parts)
     */
    MECHANICAL,

    /**
     * Electrical parts (e.g., sensors, wiring, electronic control units)
     */
    ELECTRICAL,

    /**
     * Body parts (e.g., panels, bumpers, mirrors)
     */
    BODY,

    /**
     * Consumable parts (e.g., filters, fluids, wiper blades)
     */
    CONSUMABLE;

    /**
     * Converts a string to the corresponding PartType enum value.
     * Case-insensitive.
     *
     * @param typeStr The string representation of the part type
     * @return The corresponding PartType enum value
     * @throws IllegalArgumentException If the string does not match any PartType
     */
    public static PartType fromString(String typeStr) {
        for (PartType type : PartType.values()) {
            if (type.name().equalsIgnoreCase(typeStr)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown part type: " + typeStr);
    }
}