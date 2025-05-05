/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.model;


import com.carmotorsproject.customers.model.LoyaltyProgram;
import com.carmotorsproject.customers.model.CustomerLevel;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interface for loyalty program data access operations.
 * Defines methods for CRUD operations on loyalty programs.
 */
public interface LoyaltyProgramDAOInterface {

    /**
     * Saves a new loyalty program to the database.
     *
     * @param program The loyalty program to save
     * @return The saved loyalty program with its generated ID
     * @throws SQLException If a database access error occurs
     */
    LoyaltyProgram save(LoyaltyProgram program) throws SQLException;

    /**
     * Updates an existing loyalty program in the database.
     *
     * @param program The loyalty program to update
     * @return The updated loyalty program
     * @throws SQLException If a database access error occurs
     */
    LoyaltyProgram update(LoyaltyProgram program) throws SQLException;

    /**
     * Deletes a loyalty program from the database.
     *
     * @param id The ID of the loyalty program to delete
     * @return true if the loyalty program was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds a loyalty program by its ID.
     *
     * @param id The ID of the loyalty program to find
     * @return The loyalty program, or null if not found
     * @throws SQLException If a database access error occurs
     */
    LoyaltyProgram findById(int id) throws SQLException;

    /**
     * Finds all loyalty programs in the database.
     *
     * @return A list of all loyalty programs
     * @throws SQLException If a database access error occurs
     */
    List<LoyaltyProgram> findAll() throws SQLException;

    /**
     * Finds a loyalty program by its customer ID.
     *
     * @param customerId The ID of the customer
     * @return The loyalty program, or null if not found
     * @throws SQLException If a database access error occurs
     */
    LoyaltyProgram findByCustomer(int customerId) throws SQLException;

    /**
     * Finds loyalty programs by their level.
     *
     * @param level The level to search for
     * @return A list of loyalty programs with the specified level
     * @throws SQLException If a database access error occurs
     */
    List<LoyaltyProgram> findByLevel(CustomerLevel level) throws SQLException;

    /**
     * Finds loyalty programs with points greater than or equal to the specified amount.
     *
     * @param points The minimum points
     * @return A list of loyalty programs with points >= the specified amount
     * @throws SQLException If a database access error occurs
     */
    List<LoyaltyProgram> findByMinimumPoints(int points) throws SQLException;

    /**
     * Finds loyalty programs by their enrollment date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of loyalty programs enrolled within the date range
     * @throws SQLException If a database access error occurs
     */
    List<LoyaltyProgram> findByEnrollmentDateRange(Date startDate, Date endDate) throws SQLException;

    /**
     * Finds active loyalty programs.
     *
     * @return A list of active loyalty programs
     * @throws SQLException If a database access error occurs
     */
    List<LoyaltyProgram> findActive() throws SQLException;

    /**
     * Finds inactive loyalty programs.
     *
     * @return A list of inactive loyalty programs
     * @throws SQLException If a database access error occurs
     */
    List<LoyaltyProgram> findInactive() throws SQLException;

    /**
     * Updates the active status of a loyalty program.
     *
     * @param programId The ID of the loyalty program
     * @param active The new active status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean updateActiveStatus(int programId, boolean active) throws SQLException;

    /**
     * Adds points to a loyalty program.
     *
     * @param programId The ID of the loyalty program
     * @param points The points to add
     * @return The updated loyalty program
     * @throws SQLException If a database access error occurs
     */
    LoyaltyProgram addPoints(int programId, int points) throws SQLException;

    /**
     * Redeems points from a loyalty program.
     *
     * @param programId The ID of the loyalty program
     * @param points The points to redeem
     * @return The updated loyalty program, or null if there are not enough points
     * @throws SQLException If a database access error occurs
     */
    LoyaltyProgram redeemPoints(int programId, int points) throws SQLException;
}