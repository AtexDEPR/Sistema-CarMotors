/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.model;

import java.util.Date;

/**
 * Represents a customer loyalty program.
 * Implements the Identifiable interface.
 */
public class LoyaltyProgram implements Identifiable {

    private int programId;
    private int customerId;
    private int accumulatedPoints;
    private CustomerLevel level;
    private Date enrollmentDate;
    private Date lastUpdateDate;
    private boolean active;
    private String notes;

    /**
     * Default constructor.
     */
    public LoyaltyProgram() {
        this.enrollmentDate = new Date();
        this.lastUpdateDate = new Date();
        this.accumulatedPoints = 0;
        this.level = CustomerLevel.BRONZE;
        this.active = true;
    }

    /**
     * Constructor with essential fields.
     *
     * @param customerId The ID of the customer
     */
    public LoyaltyProgram(int customerId) {
        this();
        this.customerId = customerId;
    }

    /**
     * Gets the ID of the loyalty program.
     *
     * @return The program ID
     */
    @Override
    public int getId() {
        return programId;
    }

    /**
     * Gets the program ID.
     *
     * @return The program ID
     */
    public int getProgramId() {
        return programId;
    }

    /**
     * Sets the program ID.
     *
     * @param programId The program ID to set
     */
    public void setProgramId(int programId) {
        this.programId = programId;
    }

    /**
     * Gets the customer ID.
     *
     * @return The customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId The customer ID to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


    /**
     * Gets the accumulated points.
     *
     * @return The accumulated points
     */
    public int getAccumulatedPoints() {
        return accumulatedPoints;
    }

    /**
     * Sets the accumulated points.
     *
     * @param accumulatedPoints The accumulated points to set
     */
    public void setAccumulatedPoints(int accumulatedPoints) {
        this.accumulatedPoints = accumulatedPoints;
        updateLevel();
    }

    /**
     * Gets the customer level.
     *
     * @return The customer level
     */
    public CustomerLevel getLevel() {
        return level;
    }

    /**
     * Sets the customer level.
     *
     * @param level The customer level to set
     */
    public void setLevel(CustomerLevel level) {
        this.level = level;
    }

    /**
     * Gets the enrollment date.
     *
     * @return The enrollment date
     */
    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    /**
     * Sets the enrollment date.
     *
     * @param enrollmentDate The enrollment date to set
     */
    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    /**
     * Gets the last update date.
     *
     * @return The last update date
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Sets the last update date.
     *
     * @param lastUpdateDate The last update date to set
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Checks if the loyalty program is active.
     *
     * @return true if the loyalty program is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the loyalty program.
     *
     * @param active The active status to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the notes about the loyalty program.
     *
     * @return The notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes about the loyalty program.
     *
     * @param notes The notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Adds points to the loyalty program.
     *
     * @param points The points to add
     */
    public void addPoints(int points) {
        if (points > 0) {
            // Apply the points multiplier based on the current level
            double multiplier = level.getPointsMultiplier();
            int adjustedPoints = (int) (points * multiplier);

            this.accumulatedPoints += adjustedPoints;
            this.lastUpdateDate = new Date();

            // Update the level based on the new points
            updateLevel();
        }
    }

    /**
     * Redeems points from the loyalty program.
     *
     * @param points The points to redeem
     * @return true if the points were redeemed, false if there are not enough points
     */
    public boolean redeemPoints(int points) {
        if (points > 0 && points <= accumulatedPoints) {
            this.accumulatedPoints -= points;
            this.lastUpdateDate = new Date();

            // Update the level based on the new points
            updateLevel();

            return true;
        }
        return false;
    }

    /**
     * Updates the customer level based on the accumulated points.
     */
    private void updateLevel() {
        this.level = CustomerLevel.getLevelForPoints(accumulatedPoints);
    }

    /**
     * Gets the discount percentage based on the current level.
     *
     * @return The discount percentage
     */
    public double getDiscountPercentage() {
        return level.getDiscountPercentage();
    }

    /**
     * Returns a string representation of the loyalty program.
     *
     * @return A string representation of the loyalty program
     */
    @Override
    public String toString() {
        return "Customer ID: " + customerId + ", Points: " + accumulatedPoints + ", Level: " + level;
    }
}