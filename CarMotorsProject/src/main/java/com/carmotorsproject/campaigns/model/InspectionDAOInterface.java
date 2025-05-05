package com.carmotorsproject.campaigns.model;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interface for inspection data access operations.
 */
public interface InspectionDAOInterface {

    /**
     * Saves a new inspection.
     *
     * @param inspection The inspection to save
     * @return The saved inspection with its ID
     * @throws SQLException If a database error occurs
     */
    Inspection save(Inspection inspection) throws SQLException;

    /**
     * Updates an existing inspection.
     *
     * @param inspection The inspection to update
     * @return The updated inspection
     * @throws SQLException If a database error occurs
     */
    Inspection update(Inspection inspection) throws SQLException;

    /**
     * Deletes an inspection by ID.
     *
     * @param id The ID of the inspection to delete
     * @return true if the inspection was deleted, false otherwise
     * @throws SQLException If a database error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds an inspection by ID.
     *
     * @param id The ID of the inspection to find
     * @return The inspection, or null if not found
     * @throws SQLException If a database error occurs
     */
    Inspection findById(int id) throws SQLException;

    /**
     * Finds all inspections.
     *
     * @return A list of all inspections
     * @throws SQLException If a database error occurs
     */
    List<Inspection> findAll() throws SQLException;

    /**
     * Finds inspections by vehicle ID.
     *
     * @param vehicleId The vehicle ID to search for
     * @return A list of inspections for the vehicle
     * @throws SQLException If a database error occurs
     */
    List<Inspection> findByVehicle(int vehicleId) throws SQLException;

    /**
     * Finds inspections by technician ID.
     *
     * @param technicianId The technician ID to search for
     * @return A list of inspections performed by the technician
     * @throws SQLException If a database error occurs
     */
    List<Inspection> findByTechnician(int technicianId) throws SQLException;

    /**
     * Finds inspections by date range.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return A list of inspections within the date range
     * @throws SQLException If a database error occurs
     */
    List<Inspection> findByDateRange(Date startDate, Date endDate) throws SQLException;

    /**
     * Finds inspections by result.
     *
     * @param result The result to search for
     * @return A list of inspections with the result
     * @throws SQLException If a database error occurs
     */
    List<Inspection> findByResult(InspectionResult result) throws SQLException;

    /**
     * Updates the result of an inspection.
     *
     * @param id The ID of the inspection
     * @param result The new result
     * @param notes Additional notes about the result
     * @return true if the result was updated, false otherwise
     * @throws SQLException If a database error occurs
     */
    boolean updateResult(int id, InspectionResult result, String notes) throws SQLException;
}