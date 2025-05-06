/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;
/**
 * Interface for service-related models with unique identifiers.
 * Provides a standard way to access the ID of an entity.
 */
public interface Identifiable {

    /**
     * Gets the unique identifier of the entity.
     *
     * @return The entity's unique identifier
     */
    int getId();
}