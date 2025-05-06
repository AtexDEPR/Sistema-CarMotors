/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import com.carmotorsproject.parts.model.SupplierEvaluation;
import java.util.List;
import java.sql.SQLException;

/**
 * Interface for supplier evaluation data access operations.
 * Defines methods for CRUD operations on supplier evaluations.
 */
public interface SupplierEvaluationDAOInterface {

    /**
     * Saves a new supplier evaluation to the database.
     *
     * @param evaluation The supplier evaluation to save
     * @return The saved supplier evaluation with generated ID
     * @throws SQLException If a database access error occurs
     */
    SupplierEvaluation save(SupplierEvaluation evaluation) throws SQLException;

    /**
     * Updates an existing supplier evaluation in the database.
     *
     * @param evaluation The supplier evaluation to update
     * @return The updated supplier evaluation
     * @throws SQLException If a database access error occurs
     */
    SupplierEvaluation update(SupplierEvaluation evaluation) throws SQLException;

    /**
     * Deletes a supplier evaluation from the database.
     *
     * @param id The ID of the supplier evaluation to delete
     * @return true if deletion was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds a supplier evaluation by its ID.
     *
     * @param id The ID of the supplier evaluation to find
     * @return The found supplier evaluation, or null if not found
     * @throws SQLException If a database access error occurs
     */
    SupplierEvaluation findById(int id) throws SQLException;

    /**
     * Finds all supplier evaluations for a specific supplier.
     *
     * @param supplierId The ID of the supplier
     * @return A list of supplier evaluations for the specified supplier
     * @throws SQLException If a database access error occurs
     */
    List<SupplierEvaluation> findBySupplier(int supplierId) throws SQLException;

    /**
     * Finds all supplier evaluations in the database.
     *
     * @return A list of all supplier evaluations
     * @throws SQLException If a database access error occurs
     */
    List<SupplierEvaluation> findAll() throws SQLException;

    /**
     * Finds supplier evaluations by evaluation score range.
     *
     * @param minScore The minimum score
     * @param maxScore The maximum score
     * @return A list of supplier evaluations within the specified score range
     * @throws SQLException If a database access error occurs
     */
    List<SupplierEvaluation> findByScoreRange(double minScore, double maxScore) throws SQLException;

    /**
     * Finds supplier evaluations by evaluation date range.
     *
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return A list of supplier evaluations within the specified date range
     * @throws SQLException If a database access error occurs
     */
    List<SupplierEvaluation> findByDateRange(java.util.Date startDate, java.util.Date endDate) throws SQLException;
}