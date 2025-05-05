/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

/**
 * Enum representing the different levels of customer satisfaction.
 * Corresponds to the 'customer_satisfaction' column in the 'delivery_orders' table.
 */
public enum CustomerSatisfaction {
    /**
     * Customer is very satisfied with the service
     */
    VERY_SATISFIED(5),

    /**
     * Customer is satisfied with the service
     */
    SATISFIED(4),

    /**
     * Customer is neither satisfied nor dissatisfied with the service
     */
    NEUTRAL(3),

    /**
     * Customer is dissatisfied with the service
     */
    DISSATISFIED(2),

    /**
     * Customer is very dissatisfied with the service
     */
    VERY_DISSATISFIED(1);

    private final int rating;

    /**
     * Constructor that assigns a numeric rating to each satisfaction level.
     *
     * @param rating The numeric rating (1-5)
     */
    CustomerSatisfaction(int rating) {
        this.rating = rating;
    }

    /**
     * Gets the numeric rating associated with this satisfaction level.
     *
     * @return The numeric rating (1-5)
     */
    public int getRating() {
        return rating;
    }

    /**
     * Converts a string to the corresponding CustomerSatisfaction enum value.
     * Case-insensitive.
     *
     * @param satisfactionStr The string representation of the customer satisfaction
     * @return The corresponding CustomerSatisfaction enum value
     * @throws IllegalArgumentException If the string does not match any CustomerSatisfaction
     */
    public static CustomerSatisfaction fromString(String satisfactionStr) {
        for (CustomerSatisfaction satisfaction : CustomerSatisfaction.values()) {
            if (satisfaction.name().equalsIgnoreCase(satisfactionStr)) {
                return satisfaction;
            }
        }
        throw new IllegalArgumentException("Unknown customer satisfaction level: " + satisfactionStr);
    }

    /**
     * Gets the CustomerSatisfaction enum value corresponding to a numeric rating.
     *
     * @param rating The numeric rating (1-5)
     * @return The corresponding CustomerSatisfaction enum value
     * @throws IllegalArgumentException If the rating is not between 1 and 5
     */
    public static CustomerSatisfaction fromRating(int rating) {
        for (CustomerSatisfaction satisfaction : CustomerSatisfaction.values()) {
            if (satisfaction.getRating() == rating) {
                return satisfaction;
            }
        }
        throw new IllegalArgumentException("Invalid rating: " + rating + ". Must be between 1 and 5.");
    }
}