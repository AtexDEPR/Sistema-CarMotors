/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

/**
 * Interface for models with a unique ID.
 * Ensures all model classes (e.g., Part, Customer) have a unique identifier.
 */
public interface Identifiable {

    /**
     * Gets the unique identifier of the model.
     *
     * @return The unique identifier
     */
    int getId();
}