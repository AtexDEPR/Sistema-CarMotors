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
 * Implementation of the CampaignAppointmentDAOInterface.
 * Provides data access operations for campaign appointments.
 */
public class CampaignAppointmentDAO implements CampaignAppointmentDAOInterface {
    private static final Logger LOGGER = Logger.getLogger(CampaignAppointmentDAO.class.getName());
    private DatabaseConnection dbConnection; // Manten la variable de instancia

    /**
     * Constructor that initializes the database connection using the Singleton instance.
     */
    public CampaignAppointmentDAO() {
        // Obtén la instancia Singleton
        this.dbConnection = DatabaseConnection.getInstance(); // <-- Correcto
    }

    /**
     * Saves a new campaign appointment.
     *
     * @param appointment The campaign appointment to save
     * @return The saved campaign appointment with its ID
     * @throws SQLException If a database error occurs
     */
    @Override
    public CampaignAppointment save(CampaignAppointment appointment) throws SQLException {
        String sql = "INSERT INTO campaign_appointments (campaign_id, vehicle_id, customer_id, appointment_date, " +
                "status, notes, created_by, created_date, last_modified_by, last_modified_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters
            stmt.setInt(1, appointment.getCampaignId());
            stmt.setInt(2, appointment.getVehicleId());
            stmt.setInt(3, appointment.getCustomerId());
            stmt.setTimestamp(4, appointment.getAppointmentDate() != null ?
                    new Timestamp(appointment.getAppointmentDate().getTime()) : null);
            stmt.setString(5, appointment.getStatus().name());
            stmt.setString(6, appointment.getNotes());
            stmt.setString(7, appointment.getCreatedBy());

            // Set current timestamp if not provided
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(8, appointment.getCreatedDate() != null ?
                    new Timestamp(appointment.getCreatedDate().getTime()) : now);
            stmt.setString(9, appointment.getLastModifiedBy());
            stmt.setTimestamp(10, appointment.getLastModifiedDate() != null ?
                    new Timestamp(appointment.getLastModifiedDate().getTime()) : now);

            // Execute the insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating campaign appointment failed, no rows affected.");
            }

            // Get the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appointment.setCampaignAppointmentId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating campaign appointment failed, no ID obtained.");
                }
            }

            LOGGER.log(Level.INFO, "Campaign appointment saved with ID: {0}", appointment.getCampaignAppointmentId());
            return appointment;
        }
    }

    /**
     * Updates an existing campaign appointment.
     *
     * @param appointment The campaign appointment to update
     * @return The updated campaign appointment
     * @throws SQLException If a database error occurs
     */
    @Override
    public CampaignAppointment update(CampaignAppointment appointment) throws SQLException {
        String sql = "UPDATE campaign_appointments SET campaign_id = ?, vehicle_id = ?, customer_id = ?, " +
                "appointment_date = ?, status = ?, notes = ?, last_modified_by = ?, last_modified_date = ? " +
                "WHERE campaign_appointment_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters
            stmt.setInt(1, appointment.getCampaignId());
            stmt.setInt(2, appointment.getVehicleId());
            stmt.setInt(3, appointment.getCustomerId());
            stmt.setTimestamp(4, appointment.getAppointmentDate() != null ?
                    new Timestamp(appointment.getAppointmentDate().getTime()) : null);
            stmt.setString(5, appointment.getStatus().name());
            stmt.setString(6, appointment.getNotes());
            stmt.setString(7, appointment.getLastModifiedBy());

            // Set current timestamp if not provided
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(8, appointment.getLastModifiedDate() != null ?
                    new Timestamp(appointment.getLastModifiedDate().getTime()) : now);
            stmt.setInt(9, appointment.getCampaignAppointmentId());

            // Execute the update
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating campaign appointment failed, no rows affected.");
            }

            LOGGER.log(Level.INFO, "Campaign appointment updated with ID: {0}", appointment.getCampaignAppointmentId());
            return appointment;
        }
    }

    /**
     * Deletes a campaign appointment by ID.
     *
     * @param id The ID of the campaign appointment to delete
     * @return true if the campaign appointment was deleted, false otherwise
     * @throws SQLException If a database error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM campaign_appointments WHERE campaign_appointment_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Campaign appointment deleted with ID: {0}, Rows affected: {1}",
                    new Object[]{id, affectedRows});

            return affectedRows > 0;
        }
    }

    /**
     * Finds a campaign appointment by ID.
     *
     * @param id The ID of the campaign appointment to find
     * @return The campaign appointment, or null if not found
     * @throws SQLException If a database error occurs
     */
    @Override
    public CampaignAppointment findById(int id) throws SQLException {
        String sql = "SELECT * FROM campaign_appointments WHERE campaign_appointment_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CampaignAppointment appointment = mapResultSetToCampaignAppointment(rs);
                    LOGGER.log(Level.INFO, "Found campaign appointment with ID: {0}", id);
                    return appointment;
                } else {
                    LOGGER.log(Level.INFO, "No campaign appointment found with ID: {0}", id);
                    return null;
                }
            }
        }
    }

    /**
     * Finds all campaign appointments.
     *
     * @return A list of all campaign appointments
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<CampaignAppointment> findAll() throws SQLException {
        String sql = "SELECT * FROM campaign_appointments ORDER BY appointment_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<CampaignAppointment> appointments = new ArrayList<>();

            while (rs.next()) {
                appointments.add(mapResultSetToCampaignAppointment(rs));
            }

            LOGGER.log(Level.INFO, "Found {0} campaign appointments", appointments.size());
            return appointments;
        }
    }

    /**
     * Finds campaign appointments by campaign ID.
     *
     * @param campaignId The campaign ID to search for
     * @return A list of campaign appointments for the campaign
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<CampaignAppointment> findByCampaign(int campaignId) throws SQLException {
        String sql = "SELECT * FROM campaign_appointments WHERE campaign_id = ? ORDER BY appointment_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, campaignId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<CampaignAppointment> appointments = new ArrayList<>();

                while (rs.next()) {
                    appointments.add(mapResultSetToCampaignAppointment(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} campaign appointments for campaign ID: {1}",
                        new Object[]{appointments.size(), campaignId});
                return appointments;
            }
        }
    }

    /**
     * Finds campaign appointments by vehicle ID.
     *
     * @param vehicleId The vehicle ID to search for
     * @return A list of campaign appointments for the vehicle
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<CampaignAppointment> findByVehicle(int vehicleId) throws SQLException {
        String sql = "SELECT * FROM campaign_appointments WHERE vehicle_id = ? ORDER BY appointment_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<CampaignAppointment> appointments = new ArrayList<>();

                while (rs.next()) {
                    appointments.add(mapResultSetToCampaignAppointment(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} campaign appointments for vehicle ID: {1}",
                        new Object[]{appointments.size(), vehicleId});
                return appointments;
            }
        }
    }

    /**
     * Finds campaign appointments by customer ID.
     *
     * @param customerId The customer ID to search for
     * @return A list of campaign appointments for the customer
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<CampaignAppointment> findByCustomer(int customerId) throws SQLException {
        String sql = "SELECT * FROM campaign_appointments WHERE customer_id = ? ORDER BY appointment_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<CampaignAppointment> appointments = new ArrayList<>();

                while (rs.next()) {
                    appointments.add(mapResultSetToCampaignAppointment(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} campaign appointments for customer ID: {1}",
                        new Object[]{appointments.size(), customerId});
                return appointments;
            }
        }
    }

    /**
     * Finds campaign appointments by date range.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return A list of campaign appointments within the date range
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<CampaignAppointment> findByDateRange(Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT * FROM campaign_appointments WHERE appointment_date BETWEEN ? AND ? " +
                "ORDER BY appointment_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                List<CampaignAppointment> appointments = new ArrayList<>();

                while (rs.next()) {
                    appointments.add(mapResultSetToCampaignAppointment(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} campaign appointments in date range: {1} to {2}",
                        new Object[]{appointments.size(), startDate, endDate});
                return appointments;
            }
        }
    }

    /**
     * Finds campaign appointments by status.
     *
     * @param status The status to search for
     * @return A list of campaign appointments with the status
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<CampaignAppointment> findByStatus(CampaignStatus status) throws SQLException {
        String sql = "SELECT * FROM campaign_appointments WHERE status = ? ORDER BY appointment_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {
                List<CampaignAppointment> appointments = new ArrayList<>();

                while (rs.next()) {
                    appointments.add(mapResultSetToCampaignAppointment(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} campaign appointments with status: {1}",
                        new Object[]{appointments.size(), status});
                return appointments;
            }
        }
    }

    /**
     * Updates the status of a campaign appointment.
     *
     * @param id The ID of the campaign appointment
     * @param status The new status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database error occurs
     */
    @Override
    public boolean updateStatus(int id, CampaignStatus status) throws SQLException {
        String sql = "UPDATE campaign_appointments SET status = ?, last_modified_date = ? " +
                "WHERE campaign_appointment_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(3, id);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Updated status of campaign appointment ID: {0} to {1}, Rows affected: {2}",
                    new Object[]{id, status, affectedRows});

            return affectedRows > 0;
        }
    }

    /**
     * Maps a ResultSet to a CampaignAppointment object.
     *
     * @param rs The ResultSet
     * @return A CampaignAppointment object
     * @throws SQLException If a database error occurs
     */
    private CampaignAppointment mapResultSetToCampaignAppointment(ResultSet rs) throws SQLException {
        CampaignAppointment appointment = new CampaignAppointment();

        appointment.setCampaignAppointmentId(rs.getInt("campaign_appointment_id"));
        appointment.setCampaignId(rs.getInt("campaign_id"));
        appointment.setVehicleId(rs.getInt("vehicle_id"));
        appointment.setCustomerId(rs.getInt("customer_id"));

        Timestamp appointmentDate = rs.getTimestamp("appointment_date");
        if (appointmentDate != null) {
            appointment.setAppointmentDate(new Date(appointmentDate.getTime()));
        }

        appointment.setStatus(CampaignStatus.fromString(rs.getString("status")));
        appointment.setNotes(rs.getString("notes"));
        appointment.setCreatedBy(rs.getString("created_by"));

        Timestamp createdDate = rs.getTimestamp("created_date");
        if (createdDate != null) {
            appointment.setCreatedDate(new Date(createdDate.getTime()));
        }

        appointment.setLastModifiedBy(rs.getString("last_modified_by"));

        Timestamp lastModifiedDate = rs.getTimestamp("last_modified_date");
        if (lastModifiedDate != null) {
            appointment.setLastModifiedDate(new Date(lastModifiedDate.getTime()));
        }

        return appointment;
    }
}