/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import com.carmotorsproject.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of TechnicianDAOInterface that provides data access operations for technicians.
 */
public class TechnicianDAO implements TechnicianDAOInterface {

    private static final Logger LOGGER = Logger.getLogger(TechnicianDAO.class.getName());
    private final Connection connection;

    /**
     * Constructor that initializes the database connection.
     */
    public TechnicianDAO() {
        // Fix: Use getInstance().getConnection() instead of static getConnection()
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        LOGGER.log(Level.INFO, "TechnicianDAO initialized with database connection");
    }

    /**
     * Saves a new technician to the database.
     *
     * @param technician The technician to save
     * @return The saved technician with its generated ID
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Technician save(Technician technician) throws SQLException {
        String sql = "INSERT INTO technicians (name, last_name, specialty, status, phone, email, " +
                "hire_date, hourly_rate, certifications, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            stmt.setString(1, technician.getName());
            stmt.setString(2, technician.getLastName());
            stmt.setString(3, technician.getSpecialty().name());
            stmt.setString(4, technician.getStatus());
            stmt.setString(5, technician.getPhone());
            stmt.setString(6, technician.getEmail());

            if (technician.getHireDate() != null) {
                stmt.setTimestamp(7, new Timestamp(technician.getHireDate().getTime()));
            } else {
                stmt.setTimestamp(7, new Timestamp(new java.util.Date().getTime())); // Default to current date
            }

            stmt.setDouble(8, technician.getHourlyRate());
            stmt.setString(9, technician.getCertifications());
            stmt.setString(10, technician.getNotes());

            // Execute insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating technician failed, no rows affected.");
            }

            // Get generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    technician.setTechnicianId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating technician failed, no ID obtained.");
                }
            }

            LOGGER.log(Level.INFO, "Technician created with ID: {0}", technician.getTechnicianId());
            return technician;
        }
    }

    /**
     * Updates an existing technician in the database.
     *
     * @param technician The technician to update
     * @return The updated technician
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Technician update(Technician technician) throws SQLException {
        String sql = "UPDATE technicians SET name = ?, last_name = ?, specialty = ?, status = ?, " +
                "phone = ?, email = ?, hire_date = ?, hourly_rate = ?, certifications = ?, notes = ? " +
                "WHERE technician_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set parameters
            stmt.setString(1, technician.getName());
            stmt.setString(2, technician.getLastName());
            stmt.setString(3, technician.getSpecialty().name());
            stmt.setString(4, technician.getStatus());
            stmt.setString(5, technician.getPhone());
            stmt.setString(6, technician.getEmail());

            if (technician.getHireDate() != null) {
                stmt.setTimestamp(7, new Timestamp(technician.getHireDate().getTime()));
            } else {
                stmt.setTimestamp(7, new Timestamp(new java.util.Date().getTime())); // Default to current date
            }

            stmt.setDouble(8, technician.getHourlyRate());
            stmt.setString(9, technician.getCertifications());
            stmt.setString(10, technician.getNotes());
            stmt.setInt(11, technician.getTechnicianId());

            // Execute update
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating technician failed, no rows affected.");
            }

            LOGGER.log(Level.INFO, "Technician updated with ID: {0}", technician.getTechnicianId());
            return technician;
        }
    }

    /**
     * Deletes a technician from the database.
     *
     * @param id The ID of the technician to delete
     * @return true if the technician was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        // Check if technician has associated services
        String checkServicesSql = "SELECT COUNT(*) FROM services WHERE technician_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(checkServicesSql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Cannot delete technician with associated services.");
                }
            }
        }

        // Delete the technician
        String deleteSql = "DELETE FROM technicians WHERE technician_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Technician deleted with ID: {0}, Rows affected: {1}",
                    new Object[]{id, affectedRows});

            return affectedRows > 0;
        }
    }

    /**
     * Finds a technician by their ID.
     *
     * @param id The ID of the technician to find
     * @return The technician, or null if not found
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Technician findById(int id) throws SQLException {
        String sql = "SELECT * FROM technicians WHERE technician_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTechnician(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Finds all technicians in the database.
     *
     * @return A list of all technicians
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Technician> findAll() throws SQLException {
        List<Technician> technicians = new ArrayList<>();
        String sql = "SELECT * FROM technicians ORDER BY last_name, name";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                technicians.add(mapResultSetToTechnician(rs));
            }
        }

        return technicians;
    }

    /**
     * Finds technicians by their specialty.
     *
     * @param specialty The specialty to search for
     * @return A list of technicians with the specified specialty
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Technician> findBySpecialty(TechnicianSpecialty specialty) throws SQLException {
        List<Technician> technicians = new ArrayList<>();
        String sql = "SELECT * FROM technicians WHERE specialty = ? ORDER BY last_name, name";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, specialty.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    technicians.add(mapResultSetToTechnician(rs));
                }
            }
        }

        return technicians;
    }

    /**
     * Finds technicians by their status.
     *
     * @param status The status to search for
     * @return A list of technicians with the specified status
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Technician> findByStatus(TechnicianStatus status) throws SQLException {
        List<Technician> technicians = new ArrayList<>();
        String sql = "SELECT * FROM technicians WHERE status = ? ORDER BY last_name, name";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    technicians.add(mapResultSetToTechnician(rs));
                }
            }
        }

        return technicians;
    }

    /**
     * Finds technicians by name (first name or last name).
     *
     * @param name The name to search for
     * @return A list of technicians matching the name
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Technician> findByName(String name) throws SQLException {
        List<Technician> technicians = new ArrayList<>();
        String sql = "SELECT * FROM technicians WHERE name LIKE ? OR last_name LIKE ? ORDER BY last_name, name";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            stmt.setString(2, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    technicians.add(mapResultSetToTechnician(rs));
                }
            }
        }

        return technicians;
    }

    /**
     * Updates the status of a technician.
     *
     * @param technicianId The ID of the technician
     * @param status The new status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean updateStatus(int technicianId, TechnicianStatus status) throws SQLException {
        String sql = "UPDATE technicians SET status = ? WHERE technician_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, technicianId);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Technician status updated: ID {0}, New Status {1}",
                    new Object[]{technicianId, status});

            return affectedRows > 0;
        }
    }

    /**
     * Gets the average hourly rate of all active technicians.
     *
     * @return The average hourly rate
     * @throws SQLException If a database access error occurs
     */
    @Override
    public double getAverageHourlyRate() throws SQLException {
        String sql = "SELECT AVG(hourly_rate) FROM technicians WHERE status = 'ACTIVE'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }
        }
    }

    /**
     * Maps a ResultSet row to a Technician object.
     *
     * @param rs The ResultSet containing technician data
     * @return A Technician object populated with data from the ResultSet
     * @throws SQLException If a database access error occurs
     */
    private Technician mapResultSetToTechnician(ResultSet rs) throws SQLException {
        Technician technician = new Technician();

        technician.setTechnicianId(rs.getInt("technician_id"));
        technician.setName(rs.getString("name"));
        technician.setLastName(rs.getString("last_name"));
        technician.setSpecialty(TechnicianSpecialty.valueOf(rs.getString("specialty")));
        technician.setStatus(rs.getString("status"));
        technician.setPhone(rs.getString("phone"));
        technician.setEmail(rs.getString("email"));

        Timestamp hireDate = rs.getTimestamp("hire_date");
        if (hireDate != null) {
            technician.setHireDate(hireDate);
        }

        technician.setHourlyRate(rs.getDouble("hourly_rate"));
        technician.setCertifications(rs.getString("certifications"));
        technician.setNotes(rs.getString("notes"));

        return technician;
    }
}