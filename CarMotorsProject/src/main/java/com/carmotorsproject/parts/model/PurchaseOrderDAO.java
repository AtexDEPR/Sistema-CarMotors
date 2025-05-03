/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import com.carmotorsproject.config.DatabaseConnection;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PurchaseOrder;
import com.carmotorsproject.parts.model.PurchaseOrderDetail;
import com.carmotorsproject.parts.model.Supplier;

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
 * Data Access Object for PurchaseOrder entities.
 * Handles database operations for purchase orders.
 */
public class PurchaseOrderDAO {
    private static final Logger LOGGER = Logger.getLogger(PurchaseOrderDAO.class.getName());
    private final DatabaseConnection dbConnection;
    private final SupplierDAO supplierDAO;
    private final PartDAO partDAO;

    /**
     * Constructor.
     */
    public PurchaseOrderDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.supplierDAO = new SupplierDAO();
        this.partDAO = new PartDAO();
    }

    /**
     * Inserts a new purchase order into the database.
     *
     * @param purchaseOrder The purchase order to insert
     * @return The inserted purchase order with its generated ID, or null if the insertion failed
     */
    public PurchaseOrder insert(PurchaseOrder purchaseOrder) {
        LOGGER.log(Level.INFO, "Inserting purchase order: {0}", purchaseOrder.getOrderNumber());

        String sql = "INSERT INTO purchase_orders (order_number, order_date, expected_delivery_date, " +
                "delivery_date, status, total_amount, notes, supplier_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            connection = dbConnection.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, purchaseOrder.getOrderNumber());
            statement.setTimestamp(2, new Timestamp(purchaseOrder.getOrderDate().getTime()));

            if (purchaseOrder.getExpectedDeliveryDate() != null) {
                statement.setTimestamp(3, new Timestamp(purchaseOrder.getExpectedDeliveryDate().getTime()));
            } else {
                statement.setNull(3, java.sql.Types.TIMESTAMP);
            }

            if (purchaseOrder.getDeliveryDate() != null) {
                statement.setTimestamp(4, new Timestamp(purchaseOrder.getDeliveryDate().getTime()));
            } else {
                statement.setNull(4, java.sql.Types.TIMESTAMP);
            }

            statement.setString(5, purchaseOrder.getStatus());
            statement.setDouble(6, purchaseOrder.getTotalAmount());
            statement.setString(7, purchaseOrder.getNotes());
            statement.setInt(8, purchaseOrder.getSupplier().getId());
            statement.setTimestamp(9, new Timestamp(purchaseOrder.getCreatedAt().getTime()));
            statement.setTimestamp(10, new Timestamp(purchaseOrder.getUpdatedAt().getTime()));

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                connection.rollback();
                LOGGER.log(Level.WARNING, "Failed to insert purchase order: {0}", purchaseOrder.getOrderNumber());
                return null;
            }

            generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                purchaseOrder.setId(generatedKeys.getInt(1));

                // Insert purchase order items
                for (PurchaseOrderDetail item : purchaseOrder.getItems()) {
                    item.setPurchaseOrder(purchaseOrder);
                    if (!insertPurchaseOrderItem(connection, item)) {
                        connection.rollback();
                        LOGGER.log(Level.WARNING, "Failed to insert purchase order item for order: {0}", purchaseOrder.getOrderNumber());
                        return null;
                    }
                }

                connection.commit();
                LOGGER.log(Level.INFO, "Purchase order inserted successfully with ID: {0}", purchaseOrder.getId());
                return purchaseOrder;
            } else {
                connection.rollback();
                LOGGER.log(Level.WARNING, "Failed to get generated ID for purchase order: {0}", purchaseOrder.getOrderNumber());
                return null;
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error rolling back transaction", ex);
            }
            LOGGER.log(Level.SEVERE, "Error inserting purchase order", e);
            return null;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error resetting auto-commit", e);
            }
            closeResources(connection, statement, generatedKeys);
        }
    }

    /**
     * Inserts a purchase order item into the database.
     *
     * @param connection The database connection
     * @param item The purchase order item to insert
     * @return True if the insertion was successful, false otherwise
     * @throws SQLException If an error occurs while accessing the database
     */
    private boolean insertPurchaseOrderItem(Connection connection, PurchaseOrderDetail item) throws SQLException {
        String sql = "INSERT INTO purchase_order_items (purchase_order_id, part_id, quantity, unit_price, subtotal) " +
                "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, item.getPurchaseOrder().getId());
            statement.setInt(2, item.getPart().getId());
            statement.setInt(3, item.getQuantity());
            statement.setDouble(4, item.getUnitPrice());
            statement.setDouble(5, item.getSubtotal());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                item.setId(generatedKeys.getInt(1));
                return true;
            } else {
                return false;
            }
        } finally {
            if (generatedKeys != null) {
                generatedKeys.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Updates an existing purchase order in the database.
     *
     * @param purchaseOrder The purchase order to update
     * @return True if the update was successful, false otherwise
     */
    public boolean update(PurchaseOrder purchaseOrder) {
        LOGGER.log(Level.INFO, "Updating purchase order with ID: {0}", purchaseOrder.getId());

        String sql = "UPDATE purchase_orders SET order_number = ?, order_date = ?, " +
                "expected_delivery_date = ?, delivery_date = ?, status = ?, " +
                "total_amount = ?, notes = ?, supplier_id = ?, updated_at = ? " +
                "WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(sql);

            statement.setString(1, purchaseOrder.getOrderNumber());
            statement.setTimestamp(2, new Timestamp(purchaseOrder.getOrderDate().getTime()));

            if (purchaseOrder.getExpectedDeliveryDate() != null) {
                statement.setTimestamp(3, new Timestamp(purchaseOrder.getExpectedDeliveryDate().getTime()));
            } else {
                statement.setNull(3, java.sql.Types.TIMESTAMP);
            }

            if (purchaseOrder.getDeliveryDate() != null) {
                statement.setTimestamp(4, new Timestamp(purchaseOrder.getDeliveryDate().getTime()));
            } else {
                statement.setNull(4, java.sql.Types.TIMESTAMP);
            }

            statement.setString(5, purchaseOrder.getStatus());
            statement.setDouble(6, purchaseOrder.getTotalAmount());
            statement.setString(7, purchaseOrder.getNotes());
            statement.setInt(8, purchaseOrder.getSupplier().getId());
            statement.setTimestamp(9, new Timestamp(purchaseOrder.getUpdatedAt().getTime()));
            statement.setInt(10, purchaseOrder.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                connection.rollback();
                LOGGER.log(Level.WARNING, "No purchase order found with ID: {0}", purchaseOrder.getId());
                return false;
            }

            // Delete existing items
            if (!deletePurchaseOrderItems(connection, purchaseOrder.getId())) {
                connection.rollback();
                LOGGER.log(Level.WARNING, "Failed to delete existing items for purchase order ID: {0}", purchaseOrder.getId());
                return false;
            }

            // Insert updated items
            for (PurchaseOrderDetail item : purchaseOrder.getItems()) {
                item.setPurchaseOrder(purchaseOrder);
                if (!insertPurchaseOrderItem(connection, item)) {
                    connection.rollback();
                    LOGGER.log(Level.WARNING, "Failed to insert updated item for purchase order ID: {0}", purchaseOrder.getId());
                    return false;
                }
            }

            connection.commit();
            LOGGER.log(Level.INFO, "Purchase order updated successfully with ID: {0}", purchaseOrder.getId());
            return true;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error rolling back transaction", ex);
            }
            LOGGER.log(Level.SEVERE, "Error updating purchase order", e);
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error resetting auto-commit", e);
            }
            closeResources(connection, statement, null);
        }
    }

    /**
     * Deletes purchase order items for a purchase order.
     *
     * @param connection The database connection
     * @param purchaseOrderId The ID of the purchase order
     * @return True if the deletion was successful, false otherwise
     * @throws SQLException If an error occurs while accessing the database
     */
    private boolean deletePurchaseOrderItems(Connection connection, int purchaseOrderId) throws SQLException {
        String sql = "DELETE FROM purchase_order_items WHERE purchase_order_id = ?";

        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(sql);

            statement.setInt(1, purchaseOrderId);

            statement.executeUpdate();

            return true;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Deletes a purchase order from the database.
     *
     * @param id The ID of the purchase order to delete
     * @return True if the deletion was successful, false otherwise
     */
    public boolean delete(int id) {
        LOGGER.log(Level.INFO, "Deleting purchase order with ID: {0}", id);

        String sql = "DELETE FROM purchase_orders WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            connection.setAutoCommit(false);

            // Delete purchase order items first
            if (!deletePurchaseOrderItems(connection, id)) {
                connection.rollback();
                LOGGER.log(Level.WARNING, "Failed to delete items for purchase order ID: {0}", id);
                return false;
            }

            // Delete purchase order
            statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                connection.commit();
                LOGGER.log(Level.INFO, "Purchase order deleted successfully with ID: {0}", id);
                return true;
            } else {
                connection.rollback();
                LOGGER.log(Level.WARNING, "No purchase order found with ID: {0}", id);
                return false;
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error rolling back transaction", ex);
            }
            LOGGER.log(Level.SEVERE, "Error deleting purchase order", e);
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error resetting auto-commit", e);
            }
            closeResources(connection, statement, null);
        }
    }

    /**
     * Finds a purchase order by its ID.
     *
     * @param id The ID of the purchase order to find
     * @return The found purchase order, or null if no purchase order was found
     */
    public PurchaseOrder findById(int id) {
        LOGGER.log(Level.INFO, "Finding purchase order with ID: {0}", id);

        String sql = "SELECT * FROM purchase_orders WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                PurchaseOrder purchaseOrder = mapResultSetToPurchaseOrder(resultSet);

                // Load purchase order items
                loadPurchaseOrderItems(connection, purchaseOrder);

                LOGGER.log(Level.INFO, "Purchase order found with ID: {0}", id);
                return purchaseOrder;
            } else {
                LOGGER.log(Level.WARNING, "No purchase order found with ID: {0}", id);
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding purchase order by ID", e);
            return null;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds a purchase order by its order number.
     *
     * @param orderNumber The order number of the purchase order to find
     * @return The found purchase order, or null if no purchase order was found
     */
    public PurchaseOrder findByOrderNumber(String orderNumber) {
        LOGGER.log(Level.INFO, "Finding purchase order with order number: {0}", orderNumber);

        String sql = "SELECT * FROM purchase_orders WHERE order_number = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, orderNumber);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                PurchaseOrder purchaseOrder = mapResultSetToPurchaseOrder(resultSet);

                // Load purchase order items
                loadPurchaseOrderItems(connection, purchaseOrder);

                LOGGER.log(Level.INFO, "Purchase order found with order number: {0}", orderNumber);
                return purchaseOrder;
            } else {
                LOGGER.log(Level.WARNING, "No purchase order found with order number: {0}", orderNumber);
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding purchase order by order number", e);
            return null;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds all purchase orders.
     *
     * @return A list of all purchase orders
     */
    public List<PurchaseOrder> findAll() {
        LOGGER.info("Finding all purchase orders");

        String sql = "SELECT * FROM purchase_orders";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            List<PurchaseOrder> purchaseOrders = new ArrayList<>();

            while (resultSet.next()) {
                PurchaseOrder purchaseOrder = mapResultSetToPurchaseOrder(resultSet);

                // Load purchase order items
                loadPurchaseOrderItems(connection, purchaseOrder);

                purchaseOrders.add(purchaseOrder);
            }

            LOGGER.log(Level.INFO, "Found {0} purchase orders", purchaseOrders.size());
            return purchaseOrders;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all purchase orders", e);
            return new ArrayList<>();
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds purchase orders by supplier ID.
     *
     * @param supplierId The ID of the supplier
     * @return A list of purchase orders for the specified supplier
     */
    public List<PurchaseOrder> findBySupplier(int supplierId) {
        LOGGER.log(Level.INFO, "Finding purchase orders by supplier ID: {0}", supplierId);

        String sql = "SELECT * FROM purchase_orders WHERE supplier_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, supplierId);

            resultSet = statement.executeQuery();

            List<PurchaseOrder> purchaseOrders = new ArrayList<>();

            while (resultSet.next()) {
                PurchaseOrder purchaseOrder = mapResultSetToPurchaseOrder(resultSet);

                // Load purchase order items
                loadPurchaseOrderItems(connection, purchaseOrder);

                purchaseOrders.add(purchaseOrder);
            }

            LOGGER.log(Level.INFO, "Found {0} purchase orders for supplier ID: {1}", new Object[]{purchaseOrders.size(), supplierId});
            return purchaseOrders;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding purchase orders by supplier", e);
            return new ArrayList<>();
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds purchase orders by status.
     *
     * @param status The status to search for
     * @return A list of purchase orders with the specified status
     */
    public List<PurchaseOrder> findByStatus(String status) {
        LOGGER.log(Level.INFO, "Finding purchase orders by status: {0}", status);

        String sql = "SELECT * FROM purchase_orders WHERE status = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, status);

            resultSet = statement.executeQuery();

            List<PurchaseOrder> purchaseOrders = new ArrayList<>();

            while (resultSet.next()) {
                PurchaseOrder purchaseOrder = mapResultSetToPurchaseOrder(resultSet);

                // Load purchase order items
                loadPurchaseOrderItems(connection, purchaseOrder);

                purchaseOrders.add(purchaseOrder);
            }

            LOGGER.log(Level.INFO, "Found {0} purchase orders with status: {1}", new Object[]{purchaseOrders.size(), status});
            return purchaseOrders;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding purchase orders by status", e);
            return new ArrayList<>();
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds purchase orders by date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of purchase orders within the specified date range
     */
    public List<PurchaseOrder> findByDateRange(Date startDate, Date endDate) {
        LOGGER.log(Level.INFO, "Finding purchase orders by date range: {0} to {1}", new Object[]{startDate, endDate});

        String sql = "SELECT * FROM purchase_orders WHERE order_date BETWEEN ? AND ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setTimestamp(1, new Timestamp(startDate.getTime()));
            statement.setTimestamp(2, new Timestamp(endDate.getTime()));

            resultSet = statement.executeQuery();

            List<PurchaseOrder> purchaseOrders = new ArrayList<>();

            while (resultSet.next()) {
                PurchaseOrder purchaseOrder = mapResultSetToPurchaseOrder(resultSet);

                // Load purchase order items
                loadPurchaseOrderItems(connection, purchaseOrder);

                purchaseOrders.add(purchaseOrder);
            }

            LOGGER.log(Level.INFO, "Found {0} purchase orders in date range", purchaseOrders.size());
            return purchaseOrders;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding purchase orders by date range", e);
            return new ArrayList<>();
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Loads purchase order items for a purchase order.
     *
     * @param connection The database connection
     * @param purchaseOrder The purchase order to load items for
     * @throws SQLException If an error occurs while accessing the database
     */
    private void loadPurchaseOrderItems(Connection connection, PurchaseOrder purchaseOrder) throws SQLException {
        String sql = "SELECT * FROM purchase_order_items WHERE purchase_order_id = ?";

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(sql);

            statement.setInt(1, purchaseOrder.getId());

            resultSet = statement.executeQuery();

            List<PurchaseOrder> items = new ArrayList<>();

            while (resultSet.next()) {
                PurchaseOrder item = new PurchaseOrder();

                item.setId(resultSet.getInt("id"));
                item.setQuantity(resultSet.getInt("quantity"));
                item.setUnitPrice(resultSet.getDouble("unit_price"));
                item.setPurchaseOrder(purchaseOrder);

                // Load part
                int partId = resultSet.getInt("part_id");
                Part part = partDAO.findById(partId);
                item.setPart(part);

                items.add(item);
            }

            purchaseOrder.setItems(items);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Maps a ResultSet to a PurchaseOrder object.
     *
     * @param resultSet The ResultSet to map
     * @return The mapped PurchaseOrder object
     * @throws SQLException If an error occurs while accessing the ResultSet
     */
    private PurchaseOrder mapResultSetToPurchaseOrder(ResultSet resultSet) throws SQLException {
        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setId(resultSet.getInt("id"));
        purchaseOrder.setOrderNumber(resultSet.getString("order_number"));
        purchaseOrder.setOrderDate(resultSet.getTimestamp("order_date"));

        Timestamp expectedDeliveryDate = resultSet.getTimestamp("expected_delivery_date");
        if (expectedDeliveryDate != null) {
            purchaseOrder.setExpectedDeliveryDate(expectedDeliveryDate);
        }

        Timestamp deliveryDate = resultSet.getTimestamp("delivery_date");
        if (deliveryDate != null) {
            purchaseOrder.setDeliveryDate(deliveryDate);
        }

        purchaseOrder.setStatus(resultSet.getString("status"));
        purchaseOrder.setTotalAmount(resultSet.getDouble("total_amount"));
        purchaseOrder.setNotes(resultSet.getString("notes"));
        purchaseOrder.setCreatedAt(resultSet.getTimestamp("created_at"));
        purchaseOrder.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        // Load supplier
        int supplierId = resultSet.getInt("supplier_id");
        Supplier supplier = supplierDAO.findById(supplierId);
        purchaseOrder.setSupplier(supplier);

        return purchaseOrder;
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