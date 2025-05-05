/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.model;

/**
 * Interface for customer-related models that have an ID.
 * Defines a method to get the ID of the model.
 */
public interface Identifiable {

    /**
     * Gets the ID of the model.
     *
     * @return The ID of the model
     */
    int getId();
}