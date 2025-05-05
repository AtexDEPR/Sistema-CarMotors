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
     * Saves a new purchase order to the database.
     *
     * @param order The purchase order to save
     * @return The saved purchase order with its generated ID
     * @throws SQLException If a database access error occurs
     */
    public PurchaseOrder save(PurchaseOrder order) throws SQLException {
        LOGGER.log(Level.INFO, "Saving purchase order for supplier ID: {0}", order.getSupplierId());

        String sql = "INSERT INTO purchase_orders (supplier_id, order_date, expected_date, " +
                "actual_delivery_date, status, notes, total) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, order.getSupplierId());
            statement.setTimestamp(2, order.getOrderDate() != null ?
                    new Timestamp(order.getOrderDate().getTime()) : null);
            statement.setTimestamp(3, order.getExpectedDate() != null ?
                    new Timestamp(order.getExpectedDate().getTime()) : null);
            statement.setTimestamp(4, order.getActualDeliveryDate() != null ?
                    new Timestamp(order.getActualDeliveryDate().getTime()) : null);
            statement.setString(5, order.getStatus());
            statement.setString(6, order.getNotes());
            statement.setDouble(7, order.getTotal());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "Failed to save purchase order");
                throw new SQLException("Creating purchase order failed, no rows affected.");
            }

            generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                order.setId(generatedKeys.getInt(1));

                // Save order details if any
                if (order.getDetails() != null && !order.getDetails().isEmpty()) {
                    for (PurchaseOrderDetail detail : order.getDetails()) {
                        detail.setOrderId(order.getId());
                        addDetail(detail);
                    }
                }

                LOGGER.log(Level.INFO, "Purchase order saved successfully with ID: {0}", order.getId());
                return order;
            } else {
                LOGGER.log(Level.WARNING, "Failed to get generated ID for purchase order");
                throw new SQLException("Creating purchase order failed, no ID obtained.");
            }
        } finally {
            closeResources(connection, statement, generatedKeys);
        }
    }

    /**
     * Updates an existing purchase order in the database.
     *
     * @param order The purchase order to update
     * @return The updated purchase order
     * @throws SQLException If a database access error occurs
     */
    public PurchaseOrder update(PurchaseOrder order) throws SQLException {
        LOGGER.log(Level.INFO, "Updating purchase order with ID: {0}", order.getId());

        String sql = "UPDATE purchase_orders SET supplier_id = ?, order_date = ?, expected_date = ?, " +
                "actual_delivery_date = ?, status = ?, notes = ?, total = ? WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, order.getSupplierId());
            statement.setTimestamp(2, order.getOrderDate() != null ?
                    new Timestamp(order.getOrderDate().getTime()) : null);
            statement.setTimestamp(3, order.getExpectedDate() != null ?
                    new Timestamp(order.getExpectedDate().getTime()) : null);
            statement.setTimestamp(4, order.getActualDeliveryDate() != null ?
                    new Timestamp(order.getActualDeliveryDate().getTime()) : null);
            statement.setString(5, order.getStatus());
            statement.setString(6, order.getNotes());
            statement.setDouble(7, order.getTotal());
            statement.setInt(8, order.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.log(Level.INFO, "Purchase order updated successfully with ID: {0}", order.getId());
                return order;
            } else {
                LOGGER.log(Level.WARNING, "No purchase order found with ID: {0}", order.getId());
                throw new SQLException("Updating purchase order failed, no rows affected.");
            }
        } finally {
            closeResources(connection, statement, null);
        }
    }

    /**
     * Deletes a purchase order from the database.
     *
     * @param orderId The ID of the purchase order to delete
     * @return True if the deletion was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public boolean delete(int orderId) throws SQLException {
        LOGGER.log(Level.INFO, "Deleting purchase order with ID: {0}", orderId);

        // First delete all details
        deleteAllDetails(orderId);

        String sql = "DELETE FROM purchase_orders WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, orderId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.log(Level.INFO, "Purchase order deleted successfully with ID: {0}", orderId);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No purchase order found with ID: {0}", orderId);
                return false;
            }
        } finally {
            closeResources(connection, statement, null);
        }
    }

    /**
     * Finds a purchase order by its ID.
     *
     * @param orderId The ID of the purchase order to find
     * @return The found purchase order, or null if no purchase order was found
     * @throws SQLException If a database access error occurs
     */
    public PurchaseOrder findById(int orderId) throws SQLException {
        LOGGER.log(Level.INFO, "Finding purchase order with ID: {0}", orderId);

        String sql = "SELECT * FROM purchase_orders WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, orderId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                PurchaseOrder order = mapResultSetToOrder(resultSet);
                LOGGER.log(Level.INFO, "Purchase order found with ID: {0}", orderId);
                return order;
            } else {
                LOGGER.log(Level.WARNING, "No purchase order found with ID: {0}", orderId);
                return null;
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds all purchase orders.
     *
     * @return A list of all purchase orders
     * @throws SQLException If a database access error occurs
     */
    public List<PurchaseOrder> findAll() throws SQLException {
        LOGGER.info("Finding all purchase orders");

        String sql = "SELECT * FROM purchase_orders ORDER BY order_date DESC";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            List<PurchaseOrder> orders = new ArrayList<>();

            while (resultSet.next()) {
                PurchaseOrder order = mapResultSetToOrder(resultSet);
                orders.add(order);
            }

            LOGGER.log(Level.INFO, "Found {0} purchase orders", orders.size());
            return orders;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds purchase orders by status.
     *
     * @param status The status to search for
     * @return A list of purchase orders with the specified status
     * @throws SQLException If a database access error occurs
     */
    public List<PurchaseOrder> findByStatus(String status) throws SQLException {
        LOGGER.log(Level.INFO, "Finding purchase orders with status: {0}", status);

        String sql = "SELECT * FROM purchase_orders WHERE status = ? ORDER BY order_date DESC";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, status);

            resultSet = statement.executeQuery();

            List<PurchaseOrder> orders = new ArrayList<>();

            while (resultSet.next()) {
                PurchaseOrder order = mapResultSetToOrder(resultSet);
                orders.add(order);
            }

            LOGGER.log(Level.INFO, "Found {0} purchase orders with status: {1}",
                    new Object[]{orders.size(), status});
            return orders;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds purchase orders by supplier.
     *
     * @param supplierId The ID of the supplier
     * @return A list of purchase orders from the specified supplier
     * @throws SQLException If a database access error occurs
     */
    public List<PurchaseOrder> findBySupplier(int supplierId) throws SQLException {
        LOGGER.log(Level.INFO, "Finding purchase orders for supplier ID: {0}", supplierId);

        String sql = "SELECT * FROM purchase_orders WHERE supplier_id = ? ORDER BY order_date DESC";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, supplierId);

            resultSet = statement.executeQuery();

            List<PurchaseOrder> orders = new ArrayList<>();

            while (resultSet.next()) {
                PurchaseOrder order = mapResultSetToOrder(resultSet);
                orders.add(order);
            }

            LOGGER.log(Level.INFO, "Found {0} purchase orders for supplier ID: {1}",
                    new Object[]{orders.size(), supplierId});
            return orders;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds purchase orders by status and supplier.
     *
     * @param status The status to search for
     * @param supplierId The ID of the supplier
     * @return A list of purchase orders with the specified status and supplier
     * @throws SQLException If a database access error occurs
     */
    public List<PurchaseOrder> findByStatusAndSupplier(String status, int supplierId) throws SQLException {
        LOGGER.log(Level.INFO, "Finding purchase orders with status: {0} and supplier ID: {1}",
                new Object[]{status, supplierId});

        String sql = "SELECT * FROM purchase_orders WHERE status = ? AND supplier_id = ? ORDER BY order_date DESC";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, status);
            statement.setInt(2, supplierId);

            resultSet = statement.executeQuery();

            List<PurchaseOrder> orders = new ArrayList<>();

            while (resultSet.next()) {
                PurchaseOrder order = mapResultSetToOrder(resultSet);
                orders.add(order);
            }

            LOGGER.log(Level.INFO, "Found {0} purchase orders with status: {1} and supplier ID: {2}",
                    new Object[]{orders.size(), status, supplierId});
            return orders;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Adds a detail item to a purchase order.
     *
     * @param detail The detail to add
     * @return The added detail with its generated ID
     * @throws SQLException If a database access error occurs
     */
    public PurchaseOrderDetail addDetail(PurchaseOrderDetail detail) throws SQLException {
        LOGGER.log(Level.INFO, "Adding detail to purchase order ID: {0}", detail.getOrderId());

        String sql = "INSERT INTO purchase_order_details (order_id, part_id, quantity, unit_price) " +
                "VALUES (?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, detail.getOrderId());
            statement.setInt(2, detail.getPartId());
            statement.setInt(3, detail.getQuantity());
            statement.setDouble(4, detail.getUnitPrice());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "Failed to add detail to purchase order");
                throw new SQLException("Adding detail failed, no rows affected.");
            }

            generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                detail.setDetailId(generatedKeys.getInt(1));

                // Update order total
                updateOrderTotal(detail.getOrderId());

                LOGGER.log(Level.INFO, "Detail added successfully with ID: {0}", detail.getDetailId());
                return detail;
            } else {
                LOGGER.log(Level.WARNING, "Failed to get generated ID for detail");
                throw new SQLException("Adding detail failed, no ID obtained.");
            }
        } finally {
            closeResources(connection, statement, generatedKeys);
        }
    }

    /**
     * Updates an existing detail item in a purchase order.
     *
     * @param detail The detail to update
     * @return The updated detail
     * @throws SQLException If a database access error occurs
     */
    public PurchaseOrderDetail updateDetail(PurchaseOrderDetail detail) throws SQLException {
        LOGGER.log(Level.INFO, "Updating detail with ID: {0}", detail.getDetailId());

        String sql = "UPDATE purchase_order_details SET order_id = ?, part_id = ?, quantity = ?, " +
                "unit_price = ? WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, detail.getOrderId());
            statement.setInt(2, detail.getPartId());
            statement.setInt(3, detail.getQuantity());
            statement.setDouble(4, detail.getUnitPrice());
            statement.setInt(5, detail.getDetailId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                // Update order total
                updateOrderTotal(detail.getOrderId());

                LOGGER.log(Level.INFO, "Detail updated successfully with ID: {0}", detail.getDetailId());
                return detail;
            } else {
                LOGGER.log(Level.WARNING, "No detail found with ID: {0}", detail.getDetailId());
                throw new SQLException("Updating detail failed, no rows affected.");
            }
        } finally {
            closeResources(connection, statement, null);
        }
    }

    /**
     * Deletes a detail item from a purchase order.
     *
     * @param detailId The ID of the detail to delete
     * @return True if the deletion was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public boolean deleteDetail(int detailId) throws SQLException {
        LOGGER.log(Level.INFO, "Deleting detail with ID: {0}", detailId);

        // First get the order ID to update total later
        PurchaseOrderDetail detail = findDetailById(detailId);
        if (detail == null) {
            return false;
        }

        int orderId = detail.getOrderId();

        String sql = "DELETE FROM purchase_order_details WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, detailId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                // Update order total
                updateOrderTotal(orderId);

                LOGGER.log(Level.INFO, "Detail deleted successfully with ID: {0}", detailId);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No detail found with ID: {0}", detailId);
                return false;
            }
        } finally {
            closeResources(connection, statement, null);
        }
    }

    /**
     * Deletes all details for a purchase order.
     *
     * @param orderId The ID of the purchase order
     * @throws SQLException If a database access error occurs
     */
    private void deleteAllDetails(int orderId) throws SQLException {
        LOGGER.log(Level.INFO, "Deleting all details for purchase order ID: {0}", orderId);

        String sql = "DELETE FROM purchase_order_details WHERE order_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, orderId);

            int affectedRows = statement.executeUpdate();

            LOGGER.log(Level.INFO, "Deleted {0} details for purchase order ID: {1}",
                    new Object[]{affectedRows, orderId});
        } finally {
            closeResources(connection, statement, null);
        }
    }

    /**
     * Finds a detail item by its ID.
     *
     * @param detailId The ID of the detail to find
     * @return The found detail, or null if no detail was found
     * @throws SQLException If a database access error occurs
     */
    public PurchaseOrderDetail findDetailById(int detailId) throws SQLException {
        LOGGER.log(Level.INFO, "Finding detail with ID: {0}", detailId);

        String sql = "SELECT * FROM purchase_order_details WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, detailId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                PurchaseOrderDetail detail = mapResultSetToDetail(resultSet);
                LOGGER.log(Level.INFO, "Detail found with ID: {0}", detailId);
                return detail;
            } else {
                LOGGER.log(Level.WARNING, "No detail found with ID: {0}", detailId);
                return null;
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds all details for a purchase order.
     *
     * @param orderId The ID of the purchase order
     * @return A list of details for the specified purchase order
     * @throws SQLException If a database access error occurs
     */
    public List<PurchaseOrderDetail> findDetailsByOrderId(int orderId) throws SQLException {
        LOGGER.log(Level.INFO, "Finding details for purchase order ID: {0}", orderId);

        String sql = "SELECT d.*, p.name as part_name FROM purchase_order_details d " +
                "JOIN parts p ON d.part_id = p.id WHERE d.order_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, orderId);

            resultSet = statement.executeQuery();

            List<PurchaseOrderDetail> details = new ArrayList<>();

            while (resultSet.next()) {
                PurchaseOrderDetail detail = mapResultSetToDetail(resultSet);
                detail.setPartName(resultSet.getString("part_name"));
                details.add(detail);
            }

            LOGGER.log(Level.INFO, "Found {0} details for purchase order ID: {1}",
                    new Object[]{details.size(), orderId});
            return details;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Updates the total for a purchase order based on its details.
     *
     * @param orderId The ID of the purchase order
     * @throws SQLException If a database access error occurs
     */
    private void updateOrderTotal(int orderId) throws SQLException {
        LOGGER.log(Level.INFO, "Updating total for purchase order ID: {0}", orderId);

        String sqlSelect = "SELECT SUM(quantity * unit_price) as total FROM purchase_order_details " +
                "WHERE order_id = ?";
        String sqlUpdate = "UPDATE purchase_orders SET total = ? WHERE id = ?";

        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            selectStatement = connection.prepareStatement(sqlSelect);

            selectStatement.setInt(1, orderId);

            resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                double total = resultSet.getDouble("total");

                updateStatement = connection.prepareStatement(sqlUpdate);
                updateStatement.setDouble(1, total);
                updateStatement.setInt(2, orderId);

                updateStatement.executeUpdate();

                LOGGER.log(Level.INFO, "Updated total for purchase order ID: {0} to {1}",
                        new Object[]{orderId, total});
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (selectStatement != null) {
                selectStatement.close();
            }
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (connection != null) {
                dbConnection.closeConnection(connection);
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
    private PurchaseOrder mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        PurchaseOrder order = new PurchaseOrder();

        order.setId(resultSet.getInt("id"));
        order.setSupplierId(resultSet.getInt("supplier_id"));

        // Load supplier
        Supplier supplier = supplierDAO.findById(order.getSupplierId());
        order.setSupplier(supplier);

        Timestamp orderDate = resultSet.getTimestamp("order_date");
        if (orderDate != null) {
            order.setOrderDate(new java.util.Date(orderDate.getTime()));
        }

        Timestamp expectedDate = resultSet.getTimestamp("expected_date");
        if (expectedDate != null) {
            order.setExpectedDate(new java.util.Date(expectedDate.getTime()));
        }

        Timestamp actualDeliveryDate = resultSet.getTimestamp("actual_delivery_date");
        if (actualDeliveryDate != null) {
            order.setActualDeliveryDate(new java.util.Date(actualDeliveryDate.getTime()));
        }

        order.setStatus(resultSet.getString("status"));
        order.setNotes(resultSet.getString("notes"));
        order.setTotal(resultSet.getDouble("total"));

        return order;
    }

    /**
     * Maps a ResultSet to a PurchaseOrderDetail object.
     *
     * @param resultSet The ResultSet to map
     * @return The mapped PurchaseOrderDetail object
     * @throws SQLException If an error occurs while accessing the ResultSet
     */
    private PurchaseOrderDetail mapResultSetToDetail(ResultSet resultSet) throws SQLException {
        PurchaseOrderDetail detail = new PurchaseOrderDetail();

        detail.setDetailId(resultSet.getInt("id"));
        detail.setOrderId(resultSet.getInt("order_id"));
        detail.setPartId(resultSet.getInt("part_id"));
        detail.setQuantity(resultSet.getInt("quantity"));
        detail.setUnitPrice(resultSet.getDouble("unit_price"));

        // Load part if needed
        try {
            Part part = partDAO.findById(detail.getPartId());
            if (part != null) {
                detail.setPart(part);
                detail.setPartName(part.getName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error loading part for detail", e);
        }

        return detail;
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