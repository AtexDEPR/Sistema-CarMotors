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
 * Implementation of the CampaignDAOInterface.
 * Provides data access operations for campaigns.
 */
public class CampaignDAO implements CampaignDAOInterface {
    private static final Logger LOGGER = Logger.getLogger(CampaignDAO.class.getName());
    private DatabaseConnection dbConnection; // Manten la variable de instancia

    public CampaignDAO() {
        // Obtén la instancia Singleton
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Saves a new campaign.
     *
     * @param campaign The campaign to save
     * @return The saved campaign with its ID
     * @throws SQLException If a database error occurs
     */
    @Override
    public Campaign save(Campaign campaign) throws SQLException {
        String sql = "INSERT INTO campaigns (name, description, start_date, end_date, discount_percentage, " +
                "target_vehicle_type, target_customer_segment, status, created_by, created_date, " +
                "last_modified_by, last_modified_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters
            stmt.setString(1, campaign.getName());
            stmt.setString(2, campaign.getDescription());
            stmt.setTimestamp(3, campaign.getStartDate() != null ? new Timestamp(campaign.getStartDate().getTime()) : null);
            stmt.setTimestamp(4, campaign.getEndDate() != null ? new Timestamp(campaign.getEndDate().getTime()) : null);
            stmt.setDouble(5, campaign.getDiscountPercentage());
            stmt.setString(6, campaign.getTargetVehicleType());
            stmt.setInt(7, campaign.getTargetCustomerSegment());
            stmt.setString(8, campaign.getStatus());
            stmt.setString(9, campaign.getCreatedBy());

            // Set current timestamp if not provided
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(10, campaign.getCreatedDate() != null ?
                    new Timestamp(campaign.getCreatedDate().getTime()) : now);
            stmt.setString(11, campaign.getLastModifiedBy());
            stmt.setTimestamp(12, campaign.getLastModifiedDate() != null ?
                    new Timestamp(campaign.getLastModifiedDate().getTime()) : now);

            // Execute the insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating campaign failed, no rows affected.");
            }

            // Get the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    campaign.setCampaignId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating campaign failed, no ID obtained.");
                }
            }

            LOGGER.log(Level.INFO, "Campaign saved with ID: {0}", campaign.getCampaignId());
            return campaign;
        }
    }

    /**
     * Updates an existing campaign.
     *
     * @param campaign The campaign to update
     * @return The updated campaign
     * @throws SQLException If a database error occurs
     */
    @Override
    public Campaign update(Campaign campaign) throws SQLException {
        String sql = "UPDATE campaigns SET name = ?, description = ?, start_date = ?, end_date = ?, " +
                "discount_percentage = ?, target_vehicle_type = ?, target_customer_segment = ?, " +
                "status = ?, last_modified_by = ?, last_modified_date = ? " +
                "WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters
            stmt.setString(1, campaign.getName());
            stmt.setString(2, campaign.getDescription());
            stmt.setTimestamp(3, campaign.getStartDate() != null ? new Timestamp(campaign.getStartDate().getTime()) : null);
            stmt.setTimestamp(4, campaign.getEndDate() != null ? new Timestamp(campaign.getEndDate().getTime()) : null);
            stmt.setDouble(5, campaign.getDiscountPercentage());
            stmt.setString(6, campaign.getTargetVehicleType());
            stmt.setInt(7, campaign.getTargetCustomerSegment());
            stmt.setString(8, campaign.getStatus());
            stmt.setString(9, campaign.getLastModifiedBy());

            // Set current timestamp if not provided
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(10, campaign.getLastModifiedDate() != null ?
                    new Timestamp(campaign.getLastModifiedDate().getTime()) : now);
            stmt.setInt(11, campaign.getCampaignId());

            // Execute the update
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating campaign failed, no rows affected.");
            }

            LOGGER.log(Level.INFO, "Campaign updated with ID: {0}", campaign.getCampaignId());
            return campaign;
        }
    }

    /**
     * Deletes a campaign by ID.
     *
     * @param id The ID of the campaign to delete
     * @return true if the campaign was deleted, false otherwise
     * @throws SQLException If a database error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM campaigns WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Campaign deleted with ID: {0}, Rows affected: {1}",
                    new Object[]{id, affectedRows});

            return affectedRows > 0;
        }
    }

    /**
     * Finds a campaign by ID.
     *
     * @param id The ID of the campaign to find
     * @return The campaign, or null if not found
     * @throws SQLException If a database error occurs
     */
    @Override
    public Campaign findById(int id) throws SQLException {
        String sql = "SELECT * FROM campaigns WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Campaign campaign = mapResultSetToCampaign(rs);
                    LOGGER.log(Level.INFO, "Found campaign with ID: {0}", id);
                    return campaign;
                } else {
                    LOGGER.log(Level.INFO, "No campaign found with ID: {0}", id);
                    return null;
                }
            }
        }
    }

    /**
     * Finds all campaigns.
     *
     * @return A list of all campaigns
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<Campaign> findAll() throws SQLException {
        String sql = "SELECT * FROM campaigns ORDER BY start_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Campaign> campaigns = new ArrayList<>();

            while (rs.next()) {
                campaigns.add(mapResultSetToCampaign(rs));
            }

            LOGGER.log(Level.INFO, "Found {0} campaigns", campaigns.size());
            return campaigns;
        }
    }

    /**
     * Finds campaigns by name.
     *
     * @param name The name to search for
     * @return A list of campaigns matching the name
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<Campaign> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM campaigns WHERE name LIKE ? ORDER BY start_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                List<Campaign> campaigns = new ArrayList<>();

                while (rs.next()) {
                    campaigns.add(mapResultSetToCampaign(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} campaigns matching name: {1}",
                        new Object[]{campaigns.size(), name});
                return campaigns;
            }
        }
    }

    /**
     * Finds campaigns by date range.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return A list of campaigns within the date range
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<Campaign> findByDateRange(Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT * FROM campaigns WHERE " +
                "((start_date BETWEEN ? AND ?) OR " +
                "(end_date BETWEEN ? AND ?) OR " +
                "(start_date <= ? AND end_date >= ?)) " +
                "ORDER BY start_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Timestamp startTimestamp = new Timestamp(startDate.getTime());
            Timestamp endTimestamp = new Timestamp(endDate.getTime());

            stmt.setTimestamp(1, startTimestamp);
            stmt.setTimestamp(2, endTimestamp);
            stmt.setTimestamp(3, startTimestamp);
            stmt.setTimestamp(4, endTimestamp);
            stmt.setTimestamp(5, startTimestamp);
            stmt.setTimestamp(6, endTimestamp);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Campaign> campaigns = new ArrayList<>();

                while (rs.next()) {
                    campaigns.add(mapResultSetToCampaign(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} campaigns in date range: {1} to {2}",
                        new Object[]{campaigns.size(), startDate, endDate});
                return campaigns;
            }
        }
    }

    /**
     * Finds active campaigns.
     *
     * @return A list of active campaigns
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<Campaign> findActive() throws SQLException {
        String sql = "SELECT * FROM campaigns WHERE " +
                "status = 'ACTIVE' AND " +
                "start_date <= ? AND " +
                "(end_date >= ? OR end_date IS NULL) " +
                "ORDER BY start_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(1, now);
            stmt.setTimestamp(2, now);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Campaign> campaigns = new ArrayList<>();

                while (rs.next()) {
                    campaigns.add(mapResultSetToCampaign(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} active campaigns", campaigns.size());
                return campaigns;
            }
        }
    }

    /**
     * Finds campaigns by target vehicle type.
     *
     * @param vehicleType The target vehicle type
     * @return A list of campaigns targeting the vehicle type
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<Campaign> findByTargetVehicleType(String vehicleType) throws SQLException {
        String sql = "SELECT * FROM campaigns WHERE target_vehicle_type = ? OR target_vehicle_type = 'ALL' " +
                "ORDER BY start_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicleType);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Campaign> campaigns = new ArrayList<>();

                while (rs.next()) {
                    campaigns.add(mapResultSetToCampaign(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} campaigns for vehicle type: {1}",
                        new Object[]{campaigns.size(), vehicleType});
                return campaigns;
            }
        }
    }

    /**
     * Finds campaigns by target customer segment.
     *
     * @param customerSegment The target customer segment
     * @return A list of campaigns targeting the customer segment
     * @throws SQLException If a database error occurs
     */
    @Override
    public List<Campaign> findByTargetCustomerSegment(int customerSegment) throws SQLException {
        String sql = "SELECT * FROM campaigns WHERE target_customer_segment = ? OR target_customer_segment = 0 " +
                "ORDER BY start_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerSegment);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Campaign> campaigns = new ArrayList<>();

                while (rs.next()) {
                    campaigns.add(mapResultSetToCampaign(rs));
                }

                LOGGER.log(Level.INFO, "Found {0} campaigns for customer segment: {1}",
                        new Object[]{campaigns.size(), customerSegment});
                return campaigns;
            }
        }
    }

    /**
     * Maps a ResultSet to a Campaign object.
     *
     * @param rs The ResultSet
     * @return A Campaign object
     * @throws SQLException If a database error occurs
     */
    private Campaign mapResultSetToCampaign(ResultSet rs) throws SQLException {
        Campaign campaign = new Campaign();

        campaign.setCampaignId(rs.getInt("id"));
        campaign.setName(rs.getString("name"));
        campaign.setDescription(rs.getString("description"));

        Timestamp startDate = rs.getTimestamp("start_date");
        if (startDate != null) {
            campaign.setStartDate(new Date(startDate.getTime()));
        }

        Timestamp endDate = rs.getTimestamp("end_date");
        if (endDate != null) {
            campaign.setEndDate(new Date(endDate.getTime()));
        }

        campaign.setDiscountPercentage(rs.getDouble("discount_percentage"));
        campaign.setTargetVehicleType(rs.getString("target_vehicle_type"));
        campaign.setTargetCustomerSegment(rs.getInt("target_customer_segment"));
        campaign.setStatus(rs.getString("status"));
        campaign.setCreatedBy(rs.getString("created_by"));

        Timestamp createdDate = rs.getTimestamp("created_date");
        if (createdDate != null) {
            campaign.setCreatedDate(new Date(createdDate.getTime()));
        }

        campaign.setLastModifiedBy(rs.getString("last_modified_by"));

        Timestamp lastModifiedDate = rs.getTimestamp("last_modified_date");
        if (lastModifiedDate != null) {
            campaign.setLastModifiedDate(new Date(lastModifiedDate.getTime()));
        }

        return campaign;
    }
}