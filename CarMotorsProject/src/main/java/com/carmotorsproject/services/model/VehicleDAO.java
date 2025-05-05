/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import com.carmotorsproject.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of VehicleDAOInterface that provides data access operations for vehicles.
 */
public class VehicleDAO implements VehicleDAOInterface {

    private static final Logger LOGGER = Logger.getLogger(VehicleDAO.class.getName());
    private final Connection connection;

    /**
     * Constructor that initializes the database connection.
     */
    public VehicleDAO() {
        // Fix: Use getInstance().getConnection() instead of static getConnection()
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        LOGGER.log(Level.INFO, "VehicleDAO initialized with database connection");
    }

    /**
     * Saves a new vehicle to the database.
     *
     * @param vehicle The vehicle to save
     * @return The saved vehicle with its generated ID
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Vehicle save(Vehicle vehicle) throws SQLException {
        // Check for unique license plate
        if (!isUniqueLicensePlate(vehicle.getLicensePlate(), 0)) {
            throw new SQLException("License plate already exists: " + vehicle.getLicensePlate());
        }

        String sql = "INSERT INTO vehicles (customer_id, license_plate, make, model, year, vin, " +
                "color, engine_type, transmission, current_mileage, last_service_date, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            stmt.setInt(1, vehicle.getCustomerId());
            stmt.setString(2, vehicle.getLicensePlate());
            stmt.setString(3, vehicle.getMake());
            stmt.setString(4, vehicle.getModel());
            stmt.setInt(5, vehicle.getYear());
            stmt.setString(6, vehicle.getVin());
            stmt.setString(7, vehicle.getColor());
            stmt.setString(8, vehicle.getEngineType());
            stmt.setString(9, vehicle.getTransmission());
            stmt.setInt(10, vehicle.getCurrentMileage());

            if (vehicle.getLastServiceDate() != null) {
                stmt.setTimestamp(11, new Timestamp(vehicle.getLastServiceDate().getTime()));
            } else {
                stmt.setNull(11, Types.TIMESTAMP);
            }

            stmt.setString(12, vehicle.getNotes());

            // Execute insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating vehicle failed, no rows affected.");
            }

            // Get generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehicle.setVehicleId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating vehicle failed, no ID obtained.");
                }
            }

            LOGGER.log(Level.INFO, "Vehicle created with ID: {0}", vehicle.getVehicleId());
            return vehicle;
        }
    }

    /**
     * Updates an existing vehicle in the database.
     *
     * @param vehicle The vehicle to update
     * @return The updated vehicle
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Vehicle update(Vehicle vehicle) throws SQLException {
        // Check for unique license plate
        if (!isUniqueLicensePlate(vehicle.getLicensePlate(), vehicle.getVehicleId())) {
            throw new SQLException("License plate already exists: " + vehicle.getLicensePlate());
        }

        String sql = "UPDATE vehicles SET customer_id = ?, license_plate = ?, make = ?, model = ?, " +
                "year = ?, vin = ?, color = ?, engine_type = ?, transmission = ?, " +
                "current_mileage = ?, last_service_date = ?, notes = ? " +
                "WHERE vehicle_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set parameters
            stmt.setInt(1, vehicle.getCustomerId());
            stmt.setString(2, vehicle.getLicensePlate());
            stmt.setString(3, vehicle.getMake());
            stmt.setString(4, vehicle.getModel());
            stmt.setInt(5, vehicle.getYear());
            stmt.setString(6, vehicle.getVin());
            stmt.setString(7, vehicle.getColor());
            stmt.setString(8, vehicle.getEngineType());
            stmt.setString(9, vehicle.getTransmission());
            stmt.setInt(10, vehicle.getCurrentMileage());

            if (vehicle.getLastServiceDate() != null) {
                stmt.setTimestamp(11, new Timestamp(vehicle.getLastServiceDate().getTime()));
            } else {
                stmt.setNull(11, Types.TIMESTAMP);
            }

            stmt.setString(12, vehicle.getNotes());
            stmt.setInt(13, vehicle.getVehicleId());

            // Execute update
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating vehicle failed, no rows affected.");
            }

            LOGGER.log(Level.INFO, "Vehicle updated with ID: {0}", vehicle.getVehicleId());
            return vehicle;
        }
    }

    /**
     * Deletes a vehicle from the database.
     *
     * @param id The ID of the vehicle to delete
     * @return true if the vehicle was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        // Check if vehicle has associated services
        String checkServicesSql = "SELECT COUNT(*) FROM services WHERE vehicle_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(checkServicesSql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Cannot delete vehicle with associated services.");
                }
            }
        }

        // Delete the vehicle
        String deleteSql = "DELETE FROM vehicles WHERE vehicle_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Vehicle deleted with ID: {0}, Rows affected: {1}",
                    new Object[]{id, affectedRows});

            return affectedRows > 0;
        }
    }

    /**
     * Finds a vehicle by its ID.
     *
     * @param id The ID of the vehicle to find
     * @return The vehicle, or null if not found
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Vehicle findById(int id) throws SQLException {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVehicle(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Finds all vehicles in the database.
     *
     * @return A list of all vehicles
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Vehicle> findAll() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY make, model, year";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        }

        return vehicles;
    }

    /**
     * Finds vehicles for a specific customer.
     *
     * @param customerId The ID of the customer
     * @return A list of vehicles for the customer
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Vehicle> findByCustomer(int customerId) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE customer_id = ? ORDER BY make, model, year";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(mapResultSetToVehicle(rs));
                }
            }
        }

        return vehicles;
    }

    /**
     * Finds a vehicle by its license plate.
     *
     * @param licensePlate The license plate to search for
     * @return The vehicle, or null if not found
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Vehicle findByLicensePlate(String licensePlate) throws SQLException {
        String sql = "SELECT * FROM vehicles WHERE license_plate = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, licensePlate);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVehicle(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Finds vehicles by make and model.
     *
     * @param make The vehicle make
     * @param model The vehicle model
     * @return A list of vehicles matching the make and model
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Vehicle> findByMakeAndModel(String make, String model) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE make LIKE ? AND model LIKE ? ORDER BY year DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + make + "%");
            stmt.setString(2, "%" + model + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(mapResultSetToVehicle(rs));
                }
            }
        }

        return vehicles;
    }

    /**
     * Checks if a license plate is already in use (excluding a specific vehicle).
     *
     * @param licensePlate The license plate to check
     * @param excludeVehicleId The ID of the vehicle to exclude (0 for new vehicles)
     * @return true if the license plate is unique, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean isUniqueLicensePlate(String licensePlate, int excludeVehicleId) throws SQLException {
        String sql = "SELECT vehicle_id FROM vehicles WHERE license_plate = ? AND vehicle_id != ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, licensePlate);
            stmt.setInt(2, excludeVehicleId);

            try (ResultSet rs = stmt.executeQuery()) {
                return !rs.next(); // If no results, the license plate is unique
            }
        }
    }

    /**
     * Updates the last service date for a vehicle.
     *
     * @param vehicleId The ID of the vehicle
     * @param mileage The current mileage
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean updateLastServiceInfo(int vehicleId, int mileage) throws SQLException {
        String sql = "UPDATE vehicles SET last_service_date = ?, current_mileage = ? WHERE vehicle_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            stmt.setInt(2, mileage);
            stmt.setInt(3, vehicleId);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Vehicle service info updated: ID {0}, Mileage {1}",
                    new Object[]{vehicleId, mileage});

            return affectedRows > 0;
        }
    }

    /**
     * Maps a ResultSet row to a Vehicle object.
     *
     * @param rs The ResultSet containing vehicle data
     * @return A Vehicle object populated with data from the ResultSet
     * @throws SQLException If a database access error occurs
     */
    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleId(rs.getInt("vehicle_id"));
        vehicle.setCustomerId(rs.getInt("customer_id"));
        vehicle.setLicensePlate(rs.getString("license_plate"));
        vehicle.setMake(rs.getString("make"));
        vehicle.setModel(rs.getString("model"));
        vehicle.setYear(rs.getInt("year"));
        vehicle.setVin(rs.getString("vin"));
        vehicle.setColor(rs.getString("color"));
        vehicle.setEngineType(rs.getString("engine_type"));
        vehicle.setTransmission(rs.getString("transmission"));
        vehicle.setCurrentMileage(rs.getInt("current_mileage"));

        Timestamp lastServiceDate = rs.getTimestamp("last_service_date");
        if (lastServiceDate != null) {
            vehicle.setLastServiceDate(lastServiceDate);
        }

        vehicle.setNotes(rs.getString("notes"));

        return vehicle;
    }
}