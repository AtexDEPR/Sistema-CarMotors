package com.carmotorsproject.campaigns.model;

import com.carmotorsproject.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the InspectionDAOInterface.
 * Provides data access operations for inspections.
 */
public class InspectionDAO implements InspectionDAOInterface {
    private static final Logger LOGGER = Logger.getLogger(InspectionDAO.class.getName());
    private DatabaseConnection dbConnection; // Manten la variable de instancia

    /**
     * Constructor that initializes the database connection using the Singleton instance.
     */
    public InspectionDAO() {
        // Obtén la instancia Singleton
        this.dbConnection = DatabaseConnection.getInstance(); // <-- Correcto
    }

    /**
     * Saves a new inspection.
     *
     * @param inspection The inspection to save
     * @return The saved inspection with its ID
     * @throws SQLException If a database error occurs
     */
    @Override
    public Inspection save(Inspection inspection) throws SQLException {
        String sql = "INSERT INTO inspections (vehicle_id, technician_id, inspection_date, result, notes, " +
                "created_by, created_date, last_modified_by, last_modified_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters
            stmt.setInt(1, inspection.getVehicleId());
            stmt.setInt(2, inspection.getTechnicianId());
            stmt.setTimestamp(3, inspection.getInspectionDate() != null ?
                    new Timestamp(inspection.getInspectionDate().getTime()) : null);
            stmt.setString(4, inspection.getResult().name());
            stmt.setString(5, inspection.getNotes());
            stmt.setString(6, inspection.getCreatedBy());

            // Set current timestamp if not provided
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(7, inspection.getCreatedDate() != null ?
                    new Timestamp(inspection.getCreatedDate().getTime()) : now);
            stmt.setString(8, inspection.getLastModifiedBy());
            stmt.setTimestamp(9, inspection.getLastModifiedDate() != null ?
                    new Timestamp(inspection.getLastModifiedDate().getTime()) : now);

            // Execute the insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating inspection failed, no rows affected.");
            }

            // Get the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inspection.setInspectionId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating inspection failed, no ID obtained.");
                }
            }

            LOGGER.log(Level.INFO, "Inspection saved with ID: {0}", inspection.getInspectionId());
            return inspection;
        }
    }

    /**
     * Updates an existing inspection.
     *
     * @param inspection The inspection to update
     * @return The updated inspection
     * @throws SQLException If a database error occurs
     */
    @Override
    public Inspection update(Inspection inspection) throws SQLException {
        String sql = "UPDATE inspections SET vehicle_id = ?, technician_id = ?, inspection_date = ?, " +
                "result = ?, notes = ?, last_modified_by = ?, last_modified_date = ? " +
                "WHERE inspection_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters
            stmt.setInt(1, inspection.getVehicleId());
            stmt.setInt(2, inspection.getTechnicianId());
            stmt.setTimestamp(3, inspection.getInspectionDate() != null ?
                    new Timestamp(inspection.getInspectionDate().getTime()) : null);
            stmt.setString(4, inspection.getResult().name());
            stmt.setString(5, inspection.getNotes());
            stmt.setString(6, inspection.getLastModifiedBy());

            // Set current timestamp if not provided
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(7, inspection.getLastModifiedDate() != null ?
                    new Timestamp(inspection.getLastModifiedDate().getTime()) : now);
            stmt.setInt(8, inspection.getInspectionId());

            // Execute the update
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating inspection failed, no rows affected.");
            }

            LOGGER.log(Level.INFO, "Inspection updated with ID: {0}", inspection.getInspectionId());
            return inspection;
        }
    }

    /**
     * Deletes an inspection by ID.
     *
     * @param id The ID of the inspection to delete
     * @return true if the inspection was deleted, false otherwise
     * @throws SQLException If a database error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM inspections WHERE inspection_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Inspection deleted with ID: {0}, Rows affected: {1}",
                    new Object[]{id, affectedRows});

            return affectedRows > 0;
        }
    }

    /**
     * Finds an inspection by ID.
     *
     * @param id The ID of the inspection to find
     * @return The inspection, or null if not found
     * @throws SQLException If a database error occurs
     */
    @Override
    public Inspection findById(int id) throws SQLException {
        String sql = "SELECT * FROM inspections WHERE inspection_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Inspection inspection = mapResultSetToInspection(rs);
                    LOGGER.log(Level.INFO, "Found inspection with ID: {0}", id);
                    return inspection;
                } else {
                    LOGGER.log(Level.INFO, "No inspection found with ID: {0}", id);
                    return null;
                }
            }
        }
    }

    /**
     * Finds all inspections.
     *
     * @return A list of all inspections
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<Inspection> findAll() throws SQLException {
        String sql = "SELECT * FROM inspections ORDER BY inspection_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Inspection> inspections = new ArrayList<>();

            while (rs.next()) {
                inspections.add(mapResultSetToInspection(rs));
            }

            LOGGER.log(Level.INFO, "Found {0} inspections", inspections.size());
            return inspections;
        }
    }

    /**
     * Finds inspections by vehicle ID.
     *
     * @param vehicleId The vehicle ID to search for
     * @return A list of inspections for the vehicle
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<Inspection> findByVehicle(int vehicleId) throws SQLException {
        String sql = "SELECT * FROM inspections WHERE vehicle_id = ? ORDER BY inspection_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Inspection> inspections = new ArrayList<>();

                while (rs.next()) {
                    inspections.add(mapResultSetToInspection(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} inspections for vehicle ID: {1}",
                        new Object[]{inspections.size(), vehicleId});
                return inspections;
            }
        }
    }

    /**
     * Finds inspections by technician ID.
     *
     * @param technicianId The technician ID to search for
     * @return A list of inspections performed by the technician
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<Inspection> findByTechnician(int technicianId) throws SQLException {
        String sql = "SELECT * FROM inspections WHERE technician_id = ? ORDER BY inspection_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, technicianId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Inspection> inspections = new ArrayList<>();

                while (rs.next()) {
                    inspections.add(mapResultSetToInspection(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} inspections for technician ID: {1}",
                        new Object[]{inspections.size(), technicianId});
                return inspections;
            }
        }
    }

    /**
     * Finds inspections by date range.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return A list of inspections within the date range
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<Inspection> findByDateRange(Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT * FROM inspections WHERE inspection_date BETWEEN ? AND ? " +
                "ORDER BY inspection_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                List<Inspection> inspections = new ArrayList<>();

                while (rs.next()) {
                    inspections.add(mapResultSetToInspection(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} inspections in date range: {1} to {2}",
                        new Object[]{inspections.size(), startDate, endDate});
                return inspections;
            }
        }
    }

    /**
     * Finds inspections by result.
     *
     * @param result The result to search for
     * @return A list of inspections with the result
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<Inspection> findByResult(InspectionResult result) throws SQLException {
        String sql = "SELECT * FROM inspections WHERE result = ? ORDER BY inspection_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, result.name());

            try (ResultSet rs = stmt.executeQuery()) {
                List<Inspection> inspections = new ArrayList<>();

                while (rs.next()) {
                    inspections.add(mapResultSetToInspection(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} inspections with result: {1}",
                        new Object[]{inspections.size(), result});
                return inspections;
            }
        }
    }

    /**
     * Updates the result of an inspection.
     *
     * @param id The ID of the inspection
     * @param result The new result
     * @param notes Additional notes about the result
     * @return true if the result was updated, false otherwise
     * @throws SQLException If a database error occurs
     */
    @Override
    public boolean updateResult(int id, InspectionResult result, String notes) throws SQLException {
        String sql = "UPDATE inspections SET result = ?, notes = ?, last_modified_date = ? " +
                "WHERE inspection_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, result.name());
            stmt.setString(2, notes);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(4, id);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Updated result of inspection ID: {0} to {1}, Rows affected: {2}",
                    new Object[]{id, result, affectedRows});

            return affectedRows > 0;
        }
    }

    /**
     * Maps a ResultSet to an Inspection object.
     *
     * @param rs The ResultSet
     * @return An Inspection object
     * @throws SQLException If a database error occurs
     */
    private Inspection mapResultSetToInspection(ResultSet rs) throws SQLException {
        Inspection inspection = new Inspection();

        inspection.setInspectionId(rs.getInt("inspection_id"));
        inspection.setVehicleId(rs.getInt("vehicle_id"));
        inspection.setTechnicianId(rs.getInt("technician_id"));

        Timestamp inspectionDate = rs.getTimestamp("inspection_date");
        if (inspectionDate != null) {
            inspection.setInspectionDate(new Date(inspectionDate.getTime()));
        }

        inspection.setResult(InspectionResult.fromString(rs.getString("result")));
        inspection.setNotes(rs.getString("notes"));
        inspection.setCreatedBy(rs.getString("created_by"));

        Timestamp createdDate = rs.getTimestamp("created_date");
        if (createdDate != null) {
            inspection.setCreatedDate(new Date(createdDate.getTime()));
        }

        inspection.setLastModifiedBy(rs.getString("last_modified_by"));

        Timestamp lastModifiedDate = rs.getTimestamp("last_modified_date");
        if (lastModifiedDate != null) {
            inspection.setLastModifiedDate(new Date(lastModifiedDate.getTime()));
        }

        return inspection;
    }
}