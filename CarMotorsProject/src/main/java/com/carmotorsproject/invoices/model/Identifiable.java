/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.invoices.model;

/**
 * Interface for invoice-related models that have an ID.
 * Provides a standard way to access the ID of an entity.
 */
public interface Identifiable {

    /**
     * Gets the ID of the entity.
     *
     * @return The ID of the entity
     */
    int getId();
}