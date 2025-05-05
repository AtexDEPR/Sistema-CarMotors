package com.carmotorsproject.campaigns.model;

/**
 * Interface for objects that have a unique identifier.
 * Used by campaign-related models.
 */
public interface Identifiable {

    /**
     * Gets the unique identifier of the object.
     *
     * @return The unique identifier
     */
    int getId();
}