/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

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
 * Data Access Object for Supplier entities.
 * Handles database operations for suppliers.
 */
public class SupplierDAO {
    private static final Logger LOGGER = Logger.getLogger(SupplierDAO.class.getName());
    private final DatabaseConnection dbConnection;

    /**
     * Constructor.
     */
    public SupplierDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Saves a supplier to the database (handles INSERT operation).
     * This method is an alias for insert() to maintain interface compatibility.
     *
     * @param supplier The supplier to save
     * @return The saved supplier with its generated ID, or null if the save failed
     * @throws SQLException If a database access error occurs
     */
    public Supplier save(Supplier supplier) throws SQLException {
        LOGGER.log(Level.INFO, "Saving supplier: {0}", supplier.getName());
        return insert(supplier);
    }

    /**
     * Inserts a new supplier into the database.
     *
     * @param supplier The supplier to insert
     * @return The inserted supplier with its generated ID, or null if the insertion failed
     * @throws SQLException If a database access error occurs
     */
    public Supplier insert(Supplier supplier) throws SQLException {
        LOGGER.log(Level.INFO, "Inserting supplier: {0}", supplier.getName());

        String sql = "INSERT INTO suppliers (name, contact_name, phone, email, address, " +
                "tax_id, notes, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set current timestamps if not provided
            if (supplier.getCreatedAt() == null) {
                supplier.setCreatedAt(new Date());
            }
            if (supplier.getUpdatedAt() == null) {
                supplier.setUpdatedAt(new Date());
            }

            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getContactName());
            statement.setString(3, supplier.getPhone());
            statement.setString(4, supplier.getEmail());
            statement.setString(5, supplier.getAddress());
            statement.setString(6, supplier.getTaxId());
            statement.setString(7, supplier.getNotes());
            statement.setString(8, supplier.getStatus());
            statement.setTimestamp(9, new Timestamp(supplier.getCreatedAt().getTime()));
            statement.setTimestamp(10, new Timestamp(supplier.getUpdatedAt().getTime()));

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "Failed to insert supplier: {0}", supplier.getName());
                throw new SQLException("Creating supplier failed, no rows affected.");
            }

            generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                supplier.setId(generatedKeys.getInt(1));
                LOGGER.log(Level.INFO, "Supplier inserted successfully with ID: {0}", supplier.getId());
                return supplier;
            } else {
                LOGGER.log(Level.WARNING, "Failed to get generated ID for supplier: {0}", supplier.getName());
                throw new SQLException("Creating supplier failed, no ID obtained.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting supplier", e);
            throw e;
        } finally {
            closeResources(connection, statement, generatedKeys);
        }
    }

    /**
     * Updates an existing supplier in the database.
     *
     * @param supplier The supplier to update
     * @return True if the update was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public boolean update(Supplier supplier) throws SQLException {
        LOGGER.log(Level.INFO, "Updating supplier with ID: {0}", supplier.getId());

        String sql = "UPDATE suppliers SET name = ?, contact_name = ?, phone = ?, email = ?, " +
                "address = ?, tax_id = ?, notes = ?, status = ?, updated_at = ? " +
                "WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            // Set current timestamp if not provided
            if (supplier.getUpdatedAt() == null) {
                supplier.setUpdatedAt(new Date());
            }

            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getContactName());
            statement.setString(3, supplier.getPhone());
            statement.setString(4, supplier.getEmail());
            statement.setString(5, supplier.getAddress());
            statement.setString(6, supplier.getTaxId());
            statement.setString(7, supplier.getNotes());
            statement.setString(8, supplier.getStatus());
            statement.setTimestamp(9, new Timestamp(supplier.getUpdatedAt().getTime()));
            statement.setInt(10, supplier.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.log(Level.INFO, "Supplier updated successfully with ID: {0}", supplier.getId());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No supplier found with ID: {0}", supplier.getId());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating supplier", e);
            throw e;
        } finally {
            closeResources(connection, statement, null);
        }
    }

    /**
     * Deletes a supplier from the database.
     *
     * @param id The ID of the supplier to delete
     * @return True if the deletion was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public boolean delete(int id) throws SQLException {
        LOGGER.log(Level.INFO, "Deleting supplier with ID: {0}", id);

        String sql = "DELETE FROM suppliers WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.log(Level.INFO, "Supplier deleted successfully with ID: {0}", id);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No supplier found with ID: {0}", id);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting supplier", e);
            throw e;
        } finally {
            closeResources(connection, statement, null);
        }
    }

    /**
     * Finds a supplier by its ID.
     *
     * @param id The ID of the supplier to find
     * @return The found supplier, or null if no supplier was found
     * @throws SQLException If a database access error occurs
     */
    public Supplier findById(int id) throws SQLException {
        LOGGER.log(Level.INFO, "Finding supplier with ID: {0}", id);

        String sql = "SELECT * FROM suppliers WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Supplier supplier = mapResultSetToSupplier(resultSet);
                LOGGER.log(Level.INFO, "Supplier found with ID: {0}", id);
                return supplier;
            } else {
                LOGGER.log(Level.WARNING, "No supplier found with ID: {0}", id);
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding supplier by ID", e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds a supplier by its tax ID.
     *
     * @param taxId The tax ID of the supplier to find
     * @return The found supplier, or null if no supplier was found
     * @throws SQLException If a database access error occurs
     */
    public Supplier findByTaxId(String taxId) throws SQLException {
        LOGGER.log(Level.INFO, "Finding supplier with tax ID: {0}", taxId);

        String sql = "SELECT * FROM suppliers WHERE tax_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, taxId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Supplier supplier = mapResultSetToSupplier(resultSet);
                LOGGER.log(Level.INFO, "Supplier found with tax ID: {0}", taxId);
                return supplier;
            } else {
                LOGGER.log(Level.WARNING, "No supplier found with tax ID: {0}", taxId);
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding supplier by tax ID", e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Gets the average rating for a supplier from the supplier_evaluation table.
     *
     * @param supplierId The ID of the supplier
     * @return The average rating, or 0 if no evaluations exist
     * @throws SQLException If a database access error occurs
     */
    public double getAverageRating(int supplierId) throws SQLException {
        LOGGER.log(Level.INFO, "Getting average rating for supplier ID: {0}", supplierId);

        String sql = "SELECT AVG((delivery_rating + quality_rating + price_rating + communication_rating) / 4.0) AS avg_rating " +
                "FROM supplier_evaluation WHERE supplier_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, supplierId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double avgRating = resultSet.getDouble("avg_rating");
                if (resultSet.wasNull()) {
                    LOGGER.log(Level.INFO, "No evaluations found for supplier ID: {0}, returning 0.0", supplierId);
                    return 0.0;
                }
                LOGGER.log(Level.INFO, "Average rating for supplier ID {0}: {1}", new Object[]{supplierId, avgRating});
                return avgRating;
            } else {
                LOGGER.log(Level.INFO, "No evaluations found for supplier ID: {0}, returning 0.0", supplierId);
                return 0.0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting average rating for supplier", e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds all suppliers.
     *
     * @return A list of all suppliers
     * @throws SQLException If a database access error occurs
     */
    public List<Supplier> findAll() throws SQLException {
        LOGGER.info("Finding all suppliers");

        String sql = "SELECT * FROM suppliers";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            List<Supplier> suppliers = new ArrayList<>();

            while (resultSet.next()) {
                Supplier supplier = mapResultSetToSupplier(resultSet);
                suppliers.add(supplier);
            }

            LOGGER.log(Level.INFO, "Found {0} suppliers", suppliers.size());
            return suppliers;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all suppliers", e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds suppliers by name.
     *
     * @param name The name to search for
     * @return A list of suppliers with names containing the search term
     * @throws SQLException If a database access error occurs
     */
    public List<Supplier> findByName(String name) throws SQLException {
        LOGGER.log(Level.INFO, "Finding suppliers by name: {0}", name);

        String sql = "SELECT * FROM suppliers WHERE name LIKE ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, "%" + name + "%");

            resultSet = statement.executeQuery();

            List<Supplier> suppliers = new ArrayList<>();

            while (resultSet.next()) {
                Supplier supplier = mapResultSetToSupplier(resultSet);
                suppliers.add(supplier);
            }

            LOGGER.log(Level.INFO, "Found {0} suppliers with name containing: {1}", new Object[]{suppliers.size(), name});
            return suppliers;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding suppliers by name", e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds active suppliers.
     *
     * @return A list of active suppliers
     * @throws SQLException If a database access error occurs
     */
    public List<Supplier> findActive() throws SQLException {
        LOGGER.info("Finding active suppliers");

        String sql = "SELECT * FROM suppliers WHERE status = 'ACTIVE'";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            List<Supplier> suppliers = new ArrayList<>();

            while (resultSet.next()) {
                Supplier supplier = mapResultSetToSupplier(resultSet);
                suppliers.add(supplier);
            }

            LOGGER.log(Level.INFO, "Found {0} active suppliers", suppliers.size());
            return suppliers;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding active suppliers", e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Maps a ResultSet to a Supplier object.
     *
     * @param resultSet The ResultSet to map
     * @return The mapped Supplier object
     * @throws SQLException If an error occurs while accessing the ResultSet
     */
    private Supplier mapResultSetToSupplier(ResultSet resultSet) throws SQLException {
        Supplier supplier = new Supplier();

        supplier.setId(resultSet.getInt("id"));
        supplier.setName(resultSet.getString("name"));
        supplier.setContactName(resultSet.getString("contact_name"));
        supplier.setPhone(resultSet.getString("phone"));
        supplier.setEmail(resultSet.getString("email"));
        supplier.setAddress(resultSet.getString("address"));
        supplier.setTaxId(resultSet.getString("tax_id"));
        supplier.setNotes(resultSet.getString("notes"));
        supplier.setStatus(resultSet.getString("status"));
        supplier.setCreatedAt(resultSet.getTimestamp("created_at"));
        supplier.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        return supplier;
    }

    /**
     * Closes database resources.
     *
     * @param connection The connection to close
     * @param statement The statement to close
     * @param resultSet The result set to close
     */
    private void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                dbConnection.closeConnection(connection);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error closing database resources", e);
        }
    }
}