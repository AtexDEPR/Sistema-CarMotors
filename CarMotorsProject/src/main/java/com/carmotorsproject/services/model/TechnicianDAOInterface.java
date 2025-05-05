/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import com.carmotorsproject.services.model.Technician;
import com.carmotorsproject.services.model.TechnicianSpecialty;
import com.carmotorsproject.services.model.TechnicianStatus;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface for technician data access operations.
 * Defines methods for CRUD operations on technicians.
 */
public interface TechnicianDAOInterface {

    /**
     * Saves a new technician to the database.
     *
     * @param technician The technician to save
     * @return The saved technician with its generated ID
     * @throws SQLException If a database access error occurs
     */
    Technician save(Technician technician) throws SQLException;

    /**
     * Updates an existing technician in the database.
     *
     * @param technician The technician to update
     * @return The updated technician
     * @throws SQLException If a database access error occurs
     */
    Technician update(Technician technician) throws SQLException;

    /**
     * Deletes a technician from the database.
     *
     * @param id The ID of the technician to delete
     * @return true if the technician was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds a technician by their ID.
     *
     * @param id The ID of the technician to find
     * @return The technician, or null if not found
     * @throws SQLException If a database access error occurs
     */
    Technician findById(int id) throws SQLException;

    /**
     * Finds all technicians in the database.
     *
     * @return A list of all technicians
     * @throws SQLException If a database access error occurs
     */
    List<Technician> findAll() throws SQLException;

    /**
     * Finds technicians by their specialty.
     *
     * @param specialty The specialty to search for
     * @return A list of technicians with the specified specialty
     * @throws SQLException If a database access error occurs
     */
    List<Technician> findBySpecialty(TechnicianSpecialty specialty) throws SQLException;

    /**
     * Finds technicians by their status.
     *
     * @param status The status to search for
     * @return A list of technicians with the specified status
     * @throws SQLException If a database access error occurs
     */
    List<Technician> findByStatus(TechnicianStatus status) throws SQLException;

    /**
     * Finds technicians by name (first name or last name).
     *
     * @param name The name to search for
     * @return A list of technicians matching the name
     * @throws SQLException If a database access error occurs
     */
    List<Technician> findByName(String name) throws SQLException;

    /**
     * Updates the status of a technician.
     *
     * @param technicianId The ID of the technician
     * @param status The new status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean updateStatus(int technicianId, TechnicianStatus status) throws SQLException;

    /**
     * Gets the average hourly rate of all active technicians.
     *
     * @return The average hourly rate
     * @throws SQLException If a database access error occurs
     */
    double getAverageHourlyRate() throws SQLException;
}