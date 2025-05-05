/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import com.carmotorsproject.services.model.Vehicle;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface for vehicle data access operations.
 * Defines methods for CRUD operations on vehicles.
 */
public interface VehicleDAOInterface {

    /**
     * Saves a new vehicle to the database.
     *
     * @param vehicle The vehicle to save
     * @return The saved vehicle with its generated ID
     * @throws SQLException If a database access error occurs
     */
    Vehicle save(Vehicle vehicle) throws SQLException;

    /**
     * Updates an existing vehicle in the database.
     *
     * @param vehicle The vehicle to update
     * @return The updated vehicle
     * @throws SQLException If a database access error occurs
     */
    Vehicle update(Vehicle vehicle) throws SQLException;

    /**
     * Deletes a vehicle from the database.
     *
     * @param id The ID of the vehicle to delete
     * @return true if the vehicle was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds a vehicle by its ID.
     *
     * @param id The ID of the vehicle to find
     * @return The vehicle, or null if not found
     * @throws SQLException If a database access error occurs
     */
    Vehicle findById(int id) throws SQLException;

    /**
     * Finds all vehicles in the database.
     *
     * @return A list of all vehicles
     * @throws SQLException If a database access error occurs
     */
    List<Vehicle> findAll() throws SQLException;

    /**
     * Finds vehicles for a specific customer.
     *
     * @param customerId The ID of the customer
     * @return A list of vehicles for the customer
     * @throws SQLException If a database access error occurs
     */
    List<Vehicle> findByCustomer(int customerId) throws SQLException;

    /**
     * Finds a vehicle by its license plate.
     *
     * @param licensePlate The license plate to search for
     * @return The vehicle, or null if not found
     * @throws SQLException If a database access error occurs
     */
    Vehicle findByLicensePlate(String licensePlate) throws SQLException;

    /**
     * Finds vehicles by make and model.
     *
     * @param make The vehicle make
     * @param model The vehicle model
     * @return A list of vehicles matching the make and model
     * @throws SQLException If a database access error occurs
     */
    List<Vehicle> findByMakeAndModel(String make, String model) throws SQLException;

    /**
     * Checks if a license plate is already in use (excluding a specific vehicle).
     *
     * @param licensePlate The license plate to check
     * @param excludeVehicleId The ID of the vehicle to exclude (0 for new vehicles)
     * @return true if the license plate is unique, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean isUniqueLicensePlate(String licensePlate, int excludeVehicleId) throws SQLException;

    /**
     * Updates the last service date for a vehicle.
     *
     * @param vehicleId The ID of the vehicle
     * @param mileage The current mileage
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean updateLastServiceInfo(int vehicleId, int mileage) throws SQLException;
}