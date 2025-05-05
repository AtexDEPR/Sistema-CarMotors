/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PartType;
import com.carmotorsproject.parts.model.PartStatus;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface for part data access operations.
 * Defines methods for CRUD operations on parts.
 */
public interface PartDAOInterface {

    /**
     * Saves a new part to the database.
     *
     * @param part The part to save
     * @return The saved part with generated ID
     * @throws SQLException If a database access error occurs
     */
    Part save(Part part) throws SQLException;

    /**
     * Updates an existing part in the database.
     *
     * @param part The part to update
     * @return The updated part
     * @throws SQLException If a database access error occurs
     */
    Part update(Part part) throws SQLException;

    /**
     * Deletes a part from the database.
     *
     * @param id The ID of the part to delete
     * @return true if deletion was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds a part by its ID.
     *
     * @param id The ID of the part to find
     * @return The found part, or null if not found
     * @throws SQLException If a database access error occurs
     */
    Part findById(int id) throws SQLException;

    /**
     * Finds all parts in the database.
     *
     * @return A list of all parts
     * @throws SQLException If a database access error occurs
     */
    List<Part> findAll() throws SQLException;

    /**
     * Finds parts by their type.
     *
     * @param type The type of parts to find
     * @return A list of parts of the specified type
     * @throws SQLException If a database access error occurs
     */
    List<Part> findByType(PartType type) throws SQLException;

    /**
     * Finds parts by their status.
     *
     * @param status The status of parts to find
     * @return A list of parts with the specified status
     * @throws SQLException If a database access error occurs
     */
    List<Part> findByStatus(PartStatus status) throws SQLException;

    /**
     * Finds parts by supplier ID.
     *
     * @param supplierId The ID of the supplier
     * @return A list of parts from the specified supplier
     * @throws SQLException If a database access error occurs
     */
    List<Part> findBySupplier(int supplierId) throws SQLException;

    /**
     * Finds parts by name (partial match).
     *
     * @param name The name or part of the name to search for
     * @return A list of parts matching the name search
     * @throws SQLException If a database access error occurs
     */
    List<Part> findByName(String name) throws SQLException;

    /**
     * Finds parts with stock level below the specified threshold.
     *
     * @param threshold The stock threshold
     * @return A list of parts with stock level below the threshold
     * @throws SQLException If a database access error occurs
     */
    List<Part> findLowStock(int threshold) throws SQLException;

    /**
     * Updates the stock level of a part.
     *
     * @param partId The ID of the part
     * @param quantity The quantity to add (positive) or subtract (negative)
     * @return The updated stock level
     * @throws SQLException If a database access error occurs
     */
    int updateStock(int partId, int quantity) throws SQLException;
}