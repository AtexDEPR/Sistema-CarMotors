/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import com.carmotorsproject.config.DatabaseConnection;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Part entities.
 * Handles database operations for parts.
 */
public class PartDAO {
    private static final Logger LOGGER = Logger.getLogger(PartDAO.class.getName());
    private final DatabaseConnection dbConnection;
    private final SupplierDAO supplierDAO;

    /**
     * Constructor.
     */
    public PartDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.supplierDAO = new SupplierDAO();
    }

    /**
     * Inserts a new part into the database.
     *
     * @param part The part to insert
     * @return The inserted part with its generated ID, or null if the insertion failed
     */
    public Part insert(Part part) {
        LOGGER.log(Level.INFO, "Inserting part: {0}", part.getName());

        String sql = "INSERT INTO parts (name, description, reference, type, price, quantity_in_stock, " +
                "minimum_stock, location, status, supplier_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, part.getName());
            statement.setString(2, part.getDescription());
            statement.setString(3, part.getReference());
            statement.setString(4, part.getType());
            statement.setDouble(5, part.getPrice());
            statement.setInt(6, part.getQuantityInStock());
            statement.setInt(7, part.getMinimumStock());
            statement.setString(8, part.getLocation());
            statement.setString(9, part.getStatus());

            if (part.getSupplier() != null) {
                statement.setInt(10, part.getSupplier().getId());
            } else {
                statement.setNull(10, java.sql.Types.INTEGER);
            }

            statement.setTimestamp(11, new Timestamp(part.getCreatedAt().getTime()));
            statement.setTimestamp(12, new Timestamp(part.getUpdatedAt().getTime()));

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "Failed to insert part: {0}", part.getName());
                return null;
            }

            generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                part.setId(generatedKeys.getInt(1));
                LOGGER.log(Level.INFO, "Part inserted successfully with ID: {0}", part.getId());
                return part;
            } else {
                LOGGER.log(Level.WARNING, "Failed to get generated ID for part: {0}", part.getName());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting part", e);
            return null;
        } finally {
            closeResources(connection, statement, generatedKeys);
        }
    }

    /**
     * Updates an existing part in the database.
     *
     * @param part The part to update
     * @return True if the update was successful, false otherwise
     */
    public boolean update(Part part) {
        LOGGER.log(Level.INFO, "Updating part with ID: {0}", part.getId());

        String sql = "UPDATE parts SET name = ?, description = ?, reference = ?, type = ?, " +
                "price = ?, quantity_in_stock = ?, minimum_stock = ?, location = ?, " +
                "status = ?, supplier_id = ?, updated_at = ? WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, part.getName());
            statement.setString(2, part.getDescription());
            statement.setString(3, part.getReference());
            statement.setString(4, part.getType());
            statement.setDouble(5, part.getPrice());
            statement.setInt(6, part.getQuantityInStock());
            statement.setInt(7, part.getMinimumStock());
            statement.setString(8, part.getLocation());
            statement.setString(9, part.getStatus());

            if (part.getSupplier() != null) {
                statement.setInt(10, part.getSupplier().getId());
            } else {
                statement.setNull(10, java.sql.Types.INTEGER);
            }

            statement.setTimestamp(11, new Timestamp(part.getUpdatedAt().getTime()));
            statement.setInt(12, part.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.log(Level.INFO, "Part updated successfully with ID: {0}", part.getId());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No part found with ID: {0}", part.getId());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating part", e);
            return false;
        } finally {
            closeResources(connection, statement, null);
        }
    }

    /**
     * Deletes a part from the database.
     *
     * @param id The ID of the part to delete
     * @return True if the deletion was successful, false otherwise
     */
    public boolean delete(int id) {
        LOGGER.log(Level.INFO, "Deleting part with ID: {0}", id);

        String sql = "DELETE FROM parts WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.log(Level.INFO, "Part deleted successfully with ID: {0}", id);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No part found with ID: {0}", id);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting part", e);
            return false;
        } finally {
            closeResources(connection, statement, null);
        }
    }

    /**
     * Finds a part by its ID.
     *
     * @param id The ID of the part to find
     * @return The found part, or null if no part was found
     */
    public Part findById(int id) {
        LOGGER.log(Level.INFO, "Finding part with ID: {0}", id);

        String sql = "SELECT * FROM parts WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Part part = mapResultSetToPart(resultSet);
                LOGGER.log(Level.INFO, "Part found with ID: {0}", id);
                return part;
            } else {
                LOGGER.log(Level.WARNING, "No part found with ID: {0}", id);
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding part by ID", e);
            return null;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds a part by its reference code.
     *
     * @param reference The reference code of the part to find
     * @return The found part, or null if no part was found
     */
    public Part findByReference(String reference) {
        LOGGER.log(Level.INFO, "Finding part with reference: {0}", reference);

        String sql = "SELECT * FROM parts WHERE reference = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, reference);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Part part = mapResultSetToPart(resultSet);
                LOGGER.log(Level.INFO, "Part found with reference: {0}", reference);
                return part;
            } else {
                LOGGER.log(Level.WARNING, "No part found with reference: {0}", reference);
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding part by reference", e);
            return null;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds all parts.
     *
     * @return A list of all parts
     */
    public List<Part> findAll() {
        LOGGER.info("Finding all parts");

        String sql = "SELECT * FROM parts";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            List<Part> parts = new ArrayList<>();

            while (resultSet.next()) {
                Part part = mapResultSetToPart(resultSet);
                parts.add(part);
            }

            LOGGER.log(Level.INFO, "Found {0} parts", parts.size());
            return parts;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all parts", e);
            return new ArrayList<>();
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds parts by supplier ID.
     *
     * @param supplierId The ID of the supplier
     * @return A list of parts supplied by the specified supplier
     */
    public List<Part> findBySupplier(int supplierId) {
        LOGGER.log(Level.INFO, "Finding parts by supplier ID: {0}", supplierId);

        String sql = "SELECT * FROM parts WHERE supplier_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, supplierId);

            resultSet = statement.executeQuery();

            List<Part> parts = new ArrayList<>();

            while (resultSet.next()) {
                Part part = mapResultSetToPart(resultSet);
                parts.add(part);
            }

            LOGGER.log(Level.INFO, "Found {0} parts for supplier ID: {1}", new Object[]{parts.size(), supplierId});
            return parts;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding parts by supplier", e);
            return new ArrayList<>();
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds parts by type.
     *
     * @param type The type of parts to find
     * @return A list of parts of the specified type
     */
    public List<Part> findByType(String type) {
        LOGGER.log(Level.INFO, "Finding parts by type: {0}", type);

        String sql = "SELECT * FROM parts WHERE type = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, type);

            resultSet = statement.executeQuery();

            List<Part> parts = new ArrayList<>();

            while (resultSet.next()) {
                Part part = mapResultSetToPart(resultSet);
                parts.add(part);
            }

            LOGGER.log(Level.INFO, "Found {0} parts of type: {1}", new Object[]{parts.size(), type});
            return parts;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding parts by type", e);
            return new ArrayList<>();
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds parts that are below their minimum stock level.
     *
     * @return A list of parts that are below their minimum stock level
     */
    public List<Part> findBelowMinimumStock() {
        LOGGER.info("Finding parts below minimum stock");

        String sql = "SELECT * FROM parts WHERE quantity_in_stock < minimum_stock";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            List<Part> parts = new ArrayList<>();

            while (resultSet.next()) {
                Part part = mapResultSetToPart(resultSet);
                parts.add(part);
            }

            LOGGER.log(Level.INFO, "Found {0} parts below minimum stock", parts.size());
            return parts;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding parts below minimum stock", e);
            return new ArrayList<>();
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Maps a ResultSet to a Part object.
     *
     * @param resultSet The ResultSet to map
     * @return The mapped Part object
     * @throws SQLException If an error occurs while accessing the ResultSet
     */
    private Part mapResultSetToPart(ResultSet resultSet) throws SQLException {
        Part part = new Part();

        part.setId(resultSet.getInt("id"));
        part.setName(resultSet.getString("name"));
        part.setDescription(resultSet.getString("description"));
        part.setReference(resultSet.getString("reference"));
        part.setType(resultSet.getString("type"));
        part.setPrice(resultSet.getDouble("price"));
        part.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
        part.setMinimumStock(resultSet.getInt("minimum_stock"));
        part.setLocation(resultSet.getString("location"));
        part.setStatus(resultSet.getString("status"));
        part.setCreatedAt(resultSet.getTimestamp("created_at"));
        part.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        int supplierId = resultSet.getInt("supplier_id");
        if (!resultSet.wasNull()) {
            Supplier supplier = supplierDAO.findById(supplierId);
            part.setSupplier(supplier);
        }

        return part;
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