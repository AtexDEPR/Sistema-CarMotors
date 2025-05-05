/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.model;

import com.carmotorsproject.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of LoyaltyProgramDAOInterface that provides data access operations for loyalty programs.
 */
public class LoyaltyProgramDAO implements LoyaltyProgramDAOInterface {

    private static final Logger LOGGER = Logger.getLogger(LoyaltyProgramDAO.class.getName());
    private final Connection connection;

    /**
     * Constructor that initializes the database connection.
     */
    public LoyaltyProgramDAO() {
        // Fix: Use getInstance().getConnection() instead of static getConnection()
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        LOGGER.log(Level.INFO, "LoyaltyProgramDAO initialized with database connection");
    }

    /**
     * Saves a new loyalty program to the database.
     *
     * @param program The loyalty program to save
     * @return The saved loyalty program with its generated ID
     * @throws SQLException If a database access error occurs
     */
    @Override
    public LoyaltyProgram save(LoyaltyProgram program) throws SQLException {
        // Check if a loyalty program already exists for this customer
        LoyaltyProgram existingProgram = findByCustomer(program.getCustomerId());
        if (existingProgram != null) {
            throw new SQLException("A loyalty program already exists for customer ID: " + program.getCustomerId());
        }

        String sql = "INSERT INTO loyalty_program (customer_id, accumulated_points, level, " +
                "enrollment_date, last_update_date, active, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            stmt.setInt(1, program.getCustomerId());
            stmt.setInt(2, program.getAccumulatedPoints());
            stmt.setString(3, program.getLevel().name());

            if (program.getEnrollmentDate() != null) {
                stmt.setTimestamp(4, new Timestamp(program.getEnrollmentDate().getTime()));
            } else {
                stmt.setTimestamp(4, new Timestamp(new Date().getTime())); // Default to current date
            }

            if (program.getLastUpdateDate() != null) {
                stmt.setTimestamp(5, new Timestamp(program.getLastUpdateDate().getTime()));
            } else {
                stmt.setTimestamp(5, new Timestamp(new Date().getTime())); // Default to current date
            }

            stmt.setBoolean(6, program.isActive());
            stmt.setString(7, program.getNotes());

            // Execute insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating loyalty program failed, no rows affected.");
            }

            // Get generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    program.setProgramId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating loyalty program failed, no ID obtained.");
                }
            }

            LOGGER.log(Level.INFO, "Loyalty program created with ID: {0}", program.getProgramId());
            return program;
        }
    }

    /**
     * Updates an existing loyalty program in the database.
     *
     * @param program The loyalty program to update
     * @return The updated loyalty program
     * @throws SQLException If a database access error occurs
     */
    @Override
    public LoyaltyProgram update(LoyaltyProgram program) throws SQLException {
        String sql = "UPDATE loyalty_program SET customer_id = ?, accumulated_points = ?, level = ?, " +
                "enrollment_date = ?, last_update_date = ?, active = ?, notes = ? " +
                "WHERE program_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set parameters
            stmt.setInt(1, program.getCustomerId());
            stmt.setInt(2, program.getAccumulatedPoints());
            stmt.setString(3, program.getLevel().name());

            if (program.getEnrollmentDate() != null) {
                stmt.setTimestamp(4, new Timestamp(program.getEnrollmentDate().getTime()));
            } else {
                stmt.setTimestamp(4, new Timestamp(new Date().getTime())); // Default to current date
            }

            if (program.getLastUpdateDate() != null) {
                stmt.setTimestamp(5, new Timestamp(program.getLastUpdateDate().getTime()));
            } else {
                stmt.setTimestamp(5, new Timestamp(new Date().getTime())); // Default to current date
            }

            stmt.setBoolean(6, program.isActive());
            stmt.setString(7, program.getNotes());
            stmt.setInt(8, program.getProgramId());

            // Execute update
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating loyalty program failed, no rows affected.");
            }

            LOGGER.log(Level.INFO, "Loyalty program updated with ID: {0}", program.getProgramId());
            return program;
        }
    }

    /**
     * Deletes a loyalty program from the database.
     *
     * @param id The ID of the loyalty program to delete
     * @return true if the loyalty program was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM loyalty_program WHERE program_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Loyalty program deleted with ID: {0}, Rows affected: {1}",
                    new Object[]{id, affectedRows});

            return affectedRows > 0;
        }
    }

    /**
     * Finds a loyalty program by its ID.
     *
     * @param id The ID of the loyalty program to find
     * @return The loyalty program, or null if not found
     * @throws SQLException If a database access error occurs
     */
    @Override
    public LoyaltyProgram findById(int id) throws SQLException {
        String sql = "SELECT * FROM loyalty_program WHERE program_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoyaltyProgram(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Finds all loyalty programs in the database.
     *
     * @return A list of all loyalty programs
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<LoyaltyProgram> findAll() throws SQLException {
        List<LoyaltyProgram> programs = new ArrayList<>();
        String sql = "SELECT * FROM loyalty_program ORDER BY customer_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                programs.add(mapResultSetToLoyaltyProgram(rs));
            }
        }

        return programs;
    }

    /**
     * Finds a loyalty program by its customer ID.
     *
     * @param customerId The ID of the customer
     * @return The loyalty program, or null if not found
     * @throws SQLException If a database access error occurs
     */
    @Override
    public LoyaltyProgram findByCustomer(int customerId) throws SQLException {
        String sql = "SELECT * FROM loyalty_program WHERE customer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoyaltyProgram(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Finds loyalty programs by their level.
     *
     * @param level The level to search for
     * @return A list of loyalty programs with the specified level
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<LoyaltyProgram> findByLevel(CustomerLevel level) throws SQLException {
        List<LoyaltyProgram> programs = new ArrayList<>();
        String sql = "SELECT * FROM loyalty_program WHERE level = ? ORDER BY accumulated_points DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, level.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    programs.add(mapResultSetToLoyaltyProgram(rs));
                }
            }
        }

        return programs;
    }

    /**
     * Finds loyalty programs with points greater than or equal to the specified amount.
     *
     * @param points The minimum points
     * @return A list of loyalty programs with points >= the specified amount
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<LoyaltyProgram> findByMinimumPoints(int points) throws SQLException {
        List<LoyaltyProgram> programs = new ArrayList<>();
        String sql = "SELECT * FROM loyalty_program WHERE accumulated_points >= ? ORDER BY accumulated_points DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, points);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    programs.add(mapResultSetToLoyaltyProgram(rs));
                }
            }
        }

        return programs;
    }

    /**
     * Finds loyalty programs by their enrollment date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of loyalty programs enrolled within the date range
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<LoyaltyProgram> findByEnrollmentDateRange(Date startDate, Date endDate) throws SQLException {
        List<LoyaltyProgram> programs = new ArrayList<>();
        String sql = "SELECT * FROM loyalty_program WHERE enrollment_date BETWEEN ? AND ? ORDER BY enrollment_date";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    programs.add(mapResultSetToLoyaltyProgram(rs));
                }
            }
        }

        return programs;
    }

    /**
     * Finds active loyalty programs.
     *
     * @return A list of active loyalty programs
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<LoyaltyProgram> findActive() throws SQLException {
        List<LoyaltyProgram> programs = new ArrayList<>();
        String sql = "SELECT * FROM loyalty_program WHERE active = TRUE ORDER BY customer_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                programs.add(mapResultSetToLoyaltyProgram(rs));
            }
        }

        return programs;
    }

    /**
     * Finds inactive loyalty programs.
     *
     * @return A list of inactive loyalty programs
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<LoyaltyProgram> findInactive() throws SQLException {
        List<LoyaltyProgram> programs = new ArrayList<>();
        String sql = "SELECT * FROM loyalty_program WHERE active = FALSE ORDER BY customer_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                programs.add(mapResultSetToLoyaltyProgram(rs));
            }
        }

        return programs;
    }

    /**
     * Updates the active status of a loyalty program.
     *
     * @param programId The ID of the loyalty program
     * @param active The new active status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean updateActiveStatus(int programId, boolean active) throws SQLException {
        String sql = "UPDATE loyalty_program SET active = ? WHERE program_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, active);
            stmt.setInt(2, programId);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Loyalty program active status updated: ID {0}, New Status {1}",
                    new Object[]{programId, active});

            return affectedRows > 0;
        }
    }

    /**
     * Adds points to a loyalty program.
     *
     * @param programId The ID of the loyalty program
     * @param points The points to add
     * @return The updated loyalty program
     * @throws SQLException If a database access error occurs
     */
    @Override
    public LoyaltyProgram addPoints(int programId, int points) throws SQLException {
        if (points <= 0) {
            throw new IllegalArgumentException("Points to add must be greater than zero.");
        }

        // Get the current loyalty program
        LoyaltyProgram program = findById(programId);
        if (program == null) {
            throw new SQLException("Loyalty program not found with ID: " + programId);
        }

        // Add the points
        program.addPoints(points);

        // Update the loyalty program
        return update(program);
    }

    /**
     * Redeems points from a loyalty program.
     *
     * @param programId The ID of the loyalty program
     * @param points The points to redeem
     * @return The updated loyalty program, or null if there are not enough points
     * @throws SQLException If a database access error occurs
     */
    @Override
    public LoyaltyProgram redeemPoints(int programId, int points) throws SQLException {
        if (points <= 0) {
            throw new IllegalArgumentException("Points to redeem must be greater than zero.");
        }

        // Get the current loyalty program
        LoyaltyProgram program = findById(programId);
        if (program == null) {
            throw new SQLException("Loyalty program not found with ID: " + programId);
        }

        // Redeem the points
        boolean redeemed = program.redeemPoints(points);
        if (!redeemed) {
            return null; // Not enough points
        }

        // Update the loyalty program
        return update(program);
    }

    /**
     * Maps a ResultSet row to a LoyaltyProgram object.
     *
     * @param rs The ResultSet containing loyalty program data
     * @return A LoyaltyProgram object populated with data from the ResultSet
     * @throws SQLException If a database access error occurs
     */
    private LoyaltyProgram mapResultSetToLoyaltyProgram(ResultSet rs) throws SQLException {
        LoyaltyProgram program = new LoyaltyProgram();

        program.setProgramId(rs.getInt("program_id"));
        program.setCustomerId(rs.getInt("customer_id"));
        program.setAccumulatedPoints(rs.getInt("accumulated_points"));
        program.setLevel(CustomerLevel.valueOf(rs.getString("level")));

        Timestamp enrollmentDate = rs.getTimestamp("enrollment_date");
        if (enrollmentDate != null) {
            program.setEnrollmentDate(new Date(enrollmentDate.getTime()));
        }

        Timestamp lastUpdateDate = rs.getTimestamp("last_update_date");
        if (lastUpdateDate != null) {
            program.setLastUpdateDate(new Date(lastUpdateDate.getTime()));
        }

        program.setActive(rs.getBoolean("active"));
        program.setNotes(rs.getString("notes"));

        return program;
    }
}