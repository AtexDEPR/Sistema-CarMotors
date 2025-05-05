/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import com.carmotorsproject.parts.model.Supplier;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface for supplier data access operations.
 * Defines methods for CRUD operations on suppliers.
 */
public interface SupplierDAOInterface {

    /**
     * Saves a new supplier to the database.
     *
     * @param supplier The supplier to save
     * @return The saved supplier with generated ID
     * @throws SQLException If a database access error occurs
     */
    Supplier save(Supplier supplier) throws SQLException;

    /**
     * Updates an existing supplier in the database.
     *
     * @param supplier The supplier to update
     * @return The updated supplier
     * @throws SQLException If a database access error occurs
     */
    Supplier update(Supplier supplier) throws SQLException;

    /**
     * Deletes a supplier from the database.
     *
     * @param id The ID of the supplier to delete
     * @return true if deletion was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds a supplier by its ID.
     *
     * @param id The ID of the supplier to find
     * @return The found supplier, or null if not found
     * @throws SQLException If a database access error occurs
     */
    Supplier findById(int id) throws SQLException;

    /**
     * Finds all suppliers in the database.
     *
     * @return A list of all suppliers
     * @throws SQLException If a database access error occurs
     */
    List<Supplier> findAll() throws SQLException;

    /**
     * Finds suppliers by name (partial match).
     *
     * @param name The name or part of the name to search for
     * @return A list of suppliers matching the name search
     * @throws SQLException If a database access error occurs
     */
    List<Supplier> findByName(String name) throws SQLException;

    /**
     * Finds suppliers by category.
     *
     * @param category The category of suppliers to find
     * @return A list of suppliers in the specified category
     * @throws SQLException If a database access error occurs
     */
    List<Supplier> findByCategory(String category) throws SQLException;

    /**
     * Finds suppliers by contact email (exact match).
     *
     * @param email The email to search for
     * @return The supplier with the specified email, or null if not found
     * @throws SQLException If a database access error occurs
     */
    Supplier findByEmail(String email) throws SQLException;

    /**
     * Finds suppliers by average rating range.
     *
     * @param minRating The minimum average rating
     * @param maxRating The maximum average rating
     * @return A list of suppliers within the specified rating range
     * @throws SQLException If a database access error occurs
     */
    List<Supplier> findByRatingRange(double minRating, double maxRating) throws SQLException;

    /**
     * Gets the average rating for a supplier based on all evaluations.
     *
     * @param supplierId The ID of the supplier
     * @return The average rating, or 0 if no evaluations exist
     * @throws SQLException If a database access error occurs
     */
    double getAverageRating(int supplierId) throws SQLException;
}