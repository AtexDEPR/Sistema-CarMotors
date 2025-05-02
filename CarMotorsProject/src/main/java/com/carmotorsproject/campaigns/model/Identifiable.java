package com.carmotorsproject.campaigns.model;

/**
 * Interface for objects that have an identifier
 */
public interface Identifiable {
    
    /**
     * Get the object's unique identifier
     * 
     * @return Unique identifier
     */
    String getId();
}