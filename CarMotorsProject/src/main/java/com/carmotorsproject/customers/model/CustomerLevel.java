/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.model;

/**
 * Enum for customer loyalty levels.
 * Defines the different levels of customer loyalty.
 */
public enum CustomerLevel {
    /**
     * Bronze level - Entry level for new customers.
     */
    BRONZE,

    /**
     * Silver level - Intermediate level for regular customers.
     */
    SILVER,

    /**
     * Gold level - Advanced level for loyal customers.
     */
    GOLD,

    /**
     * Platinum level - Highest level for premium customers.
     */
    PLATINUM;

    /**
     * Gets the minimum points required for this level.
     *
     * @return The minimum points required
     */
    public int getMinPoints() {
        switch (this) {
            case BRONZE:
                return 0;
            case SILVER:
                return 1000;
            case GOLD:
                return 5000;
            case PLATINUM:
                return 10000;
            default:
                return 0;
        }
    }

    /**
     * Gets the discount percentage for this level.
     *
     * @return The discount percentage
     */
    public double getDiscountPercentage() {
        switch (this) {
            case BRONZE:
                return 0.0;
            case SILVER:
                return 0.05;
            case GOLD:
                return 0.10;
            case PLATINUM:
                return 0.15;
            default:
                return 0.0;
        }
    }

    /**
     * Gets the points multiplier for this level.
     *
     * @return The points multiplier
     */
    public double getPointsMultiplier() {
        switch (this) {
            case BRONZE:
                return 1.0;
            case SILVER:
                return 1.2;
            case GOLD:
                return 1.5;
            case PLATINUM:
                return 2.0;
            default:
                return 1.0;
        }
    }

    /**
     * Gets the customer level for the given points.
     *
     * @param points The accumulated points
     * @return The corresponding customer level
     */
    public static CustomerLevel getLevelForPoints(int points) {
        if (points >= PLATINUM.getMinPoints()) {
            return PLATINUM;
        } else if (points >= GOLD.getMinPoints()) {
            return GOLD;
        } else if (points >= SILVER.getMinPoints()) {
            return SILVER;
        } else {
            return BRONZE;
        }
    }
}