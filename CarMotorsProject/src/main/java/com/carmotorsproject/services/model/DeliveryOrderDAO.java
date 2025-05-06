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
 * Implementation of data access operations for delivery orders.
 */
public class DeliveryOrderDAO {

    private static final Logger LOGGER = Logger.getLogger(DeliveryOrderDAO.class.getName());
    private final Connection connection;
    private final ServiceDAO serviceDAO;

    /**
     * Constructor that initializes the database connection.
     */
    public DeliveryOrderDAO() {
        // Fix: Use getInstance().getConnection() instead of static getConnection()
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.serviceDAO = new ServiceDAO();
        LOGGER.log(Level.INFO, "DeliveryOrderDAO initialized with database connection");
    }

    /**
     * Saves a new delivery order to the database.
     *
     * @param deliveryOrder The delivery order to save
     * @return The saved delivery order with its generated ID
     * @throws SQLException If a database access error occurs
     */
    public DeliveryOrder save(DeliveryOrder deliveryOrder) throws SQLException {
        // Update service status to DELIVERED
        serviceDAO.updateServiceStatus(deliveryOrder.getServiceId(), ServiceStatus.DELIVERED);

        String sql = "INSERT INTO delivery_orders (service_id, delivery_date, delivered_by, received_by, " +
                "customer_satisfaction, customer_feedback, follow_up_required, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            stmt.setInt(1, deliveryOrder.getServiceId());
            stmt.setTimestamp(2, new Timestamp(deliveryOrder.getDeliveryDate().getTime()));
            stmt.setString(3, deliveryOrder.getDeliveredBy());
            stmt.setString(4, deliveryOrder.getReceivedBy());

            if (deliveryOrder.getCustomerSatisfaction() != null) {
                stmt.setString(5, deliveryOrder.getCustomerSatisfaction().name());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }

            stmt.setString(6, deliveryOrder.getCustomerFeedback());
            stmt.setBoolean(7, deliveryOrder.isFollowUpRequired());
            stmt.setString(8, deliveryOrder.getNotes());

            // Execute insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating delivery order failed, no rows affected.");
            }

            // Get generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    deliveryOrder.setDeliveryOrderId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating delivery order failed, no ID obtained.");
                }
            }

            LOGGER.log(Level.INFO, "Delivery order created with ID: {0}", deliveryOrder.getDeliveryOrderId());
            return deliveryOrder;
        }
    }

    /**
     * Updates an existing delivery order in the database.
     *
     * @param deliveryOrder The delivery order to update
     * @return The updated delivery order
     * @throws SQLException If a database access error occurs
     */
    public DeliveryOrder update(DeliveryOrder deliveryOrder) throws SQLException {
        String sql = "UPDATE delivery_orders SET delivery_date = ?, delivered_by = ?, received_by = ?, " +
                "customer_satisfaction = ?, customer_feedback = ?, follow_up_required = ?, notes = ? " +
                "WHERE delivery_order_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set parameters
            stmt.setTimestamp(1, new Timestamp(deliveryOrder.getDeliveryDate().getTime()));
            stmt.setString(2, deliveryOrder.getDeliveredBy());
            stmt.setString(3, deliveryOrder.getReceivedBy());

            if (deliveryOrder.getCustomerSatisfaction() != null) {
                stmt.setString(4, deliveryOrder.getCustomerSatisfaction().name());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.setString(5, deliveryOrder.getCustomerFeedback());
            stmt.setBoolean(6, deliveryOrder.isFollowUpRequired());
            stmt.setString(7, deliveryOrder.getNotes());
            stmt.setInt(8, deliveryOrder.getDeliveryOrderId());

            // Execute update
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating delivery order failed, no rows affected.");
            }

            LOGGER.log(Level.INFO, "Delivery order updated with ID: {0}", deliveryOrder.getDeliveryOrderId());
            return deliveryOrder;
        }
    }

    /**
     * Deletes a delivery order from the database.
     *
     * @param id The ID of the delivery order to delete
     * @return true if the delivery order was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public boolean delete(int id) throws SQLException {
        // Get the service ID before deleting
        int serviceId = 0;
        String selectSql = "SELECT service_id FROM delivery_orders WHERE delivery_order_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    serviceId = rs.getInt("service_id");
                } else {
                    return false; // Delivery order not found
                }
            }
        }

        // Delete the delivery order
        String deleteSql = "DELETE FROM delivery_orders WHERE delivery_order_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Update service status back to COMPLETED
                serviceDAO.updateServiceStatus(serviceId, ServiceStatus.COMPLETED);

                LOGGER.log(Level.INFO, "Delivery order deleted with ID: {0}", id);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Finds a delivery order by its ID.
     *
     * @param id The ID of the delivery order to find
     * @return The delivery order, or null if not found
     * @throws SQLException If a database access error occurs
     */
    public DeliveryOrder findById(int id) throws SQLException {
        String sql = "SELECT * FROM delivery_orders WHERE delivery_order_id = ?";
        // Obtener conexión DENTRO del try-with-resources
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); // Asume que esto da una conexión fresca
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDeliveryOrder(rs);
                } else {
                    return null;
                }
            }
        } // conn, stmt, y rs se cierran automáticamente aquí
    }

    /**
     * Finds a delivery order by its service ID.
     *
     * @param serviceId The ID of the service
     * @return The delivery order, or null if not found
     * @throws SQLException If a database access error occurs
     */
    public DeliveryOrder findByServiceId(int serviceId) throws SQLException {
        String sql = "SELECT * FROM delivery_orders WHERE service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, serviceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDeliveryOrder(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Finds all delivery orders in the database.
     *
     * @return A list of all delivery orders
     * @throws SQLException If a database access error occurs
     */
    public List<DeliveryOrder> findAll() throws SQLException {
        List<DeliveryOrder> deliveryOrders = new ArrayList<>();
        String sql = "SELECT * FROM delivery_orders ORDER BY delivery_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                deliveryOrders.add(mapResultSetToDeliveryOrder(rs));
            }
        }

        return deliveryOrders;
    }

    /**
     * Finds delivery orders that require follow-up.
     *
     * @return A list of delivery orders requiring follow-up
     * @throws SQLException If a database access error occurs
     */
    public List<DeliveryOrder> findRequiringFollowUp() throws SQLException {
        List<DeliveryOrder> deliveryOrders = new ArrayList<>();
        String sql = "SELECT * FROM delivery_orders WHERE follow_up_required = TRUE ORDER BY delivery_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                deliveryOrders.add(mapResultSetToDeliveryOrder(rs));
            }
        }

        return deliveryOrders;
    }

    /**
     * Finds delivery orders with low customer satisfaction (DISSATISFIED or VERY_DISSATISFIED).
     *
     * @return A list of delivery orders with low customer satisfaction
     * @throws SQLException If a database access error occurs
     */
    public List<DeliveryOrder> findLowSatisfaction() throws SQLException {
        List<DeliveryOrder> deliveryOrders = new ArrayList<>();
        String sql = "SELECT * FROM delivery_orders WHERE customer_satisfaction IN ('DISSATISFIED', 'VERY_DISSATISFIED') " +
                "ORDER BY delivery_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                deliveryOrders.add(mapResultSetToDeliveryOrder(rs));
            }
        }

        return deliveryOrders;
    }

    /**
     * Maps a ResultSet row to a DeliveryOrder object.
     *
     * @param rs The ResultSet containing delivery order data
     * @return A DeliveryOrder object populated with data from the ResultSet
     * @throws SQLException If a database access error occurs
     */
    private DeliveryOrder mapResultSetToDeliveryOrder(ResultSet rs) throws SQLException {
        DeliveryOrder deliveryOrder = new DeliveryOrder();

        deliveryOrder.setDeliveryOrderId(rs.getInt("delivery_order_id"));
        deliveryOrder.setServiceId(rs.getInt("service_id"));
        deliveryOrder.setDeliveryDate(rs.getTimestamp("delivery_date"));
        deliveryOrder.setDeliveredBy(rs.getString("delivered_by"));
        deliveryOrder.setReceivedBy(rs.getString("received_by"));

        // Fix: Use the CustomerSatisfaction enum directly from the package
        String satisfactionStr = rs.getString("customer_satisfaction");
        if (satisfactionStr != null) {
            try {
                CustomerSatisfaction satisfaction = CustomerSatisfaction.valueOf(satisfactionStr);
                deliveryOrder.setCustomerSatisfaction(satisfaction);
                LOGGER.log(Level.FINE, "Set customer satisfaction to: {0}", satisfaction);
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Invalid customer satisfaction value: {0}", satisfactionStr);
            }
        }

        deliveryOrder.setCustomerFeedback(rs.getString("customer_feedback"));
        deliveryOrder.setFollowUpRequired(rs.getBoolean("follow_up_required"));
        deliveryOrder.setNotes(rs.getString("notes"));

        return deliveryOrder;
    }

    /**
     * Updates customer feedback and satisfaction for a delivery order.
     *
     * @param deliveryOrderId The ID of the delivery order
     * @param satisfaction The customer satisfaction rating
     * @param comments Customer feedback comments
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public boolean updateCustomerFeedback(int deliveryOrderId, CustomerSatisfaction satisfaction, String comments) throws SQLException {
        String sql = "UPDATE delivery_orders SET customer_satisfaction = ?, customer_feedback = ? WHERE delivery_order_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (satisfaction != null) {
                stmt.setString(1, satisfaction.name());
            } else {
                stmt.setNull(1, Types.VARCHAR);
            }

            stmt.setString(2, comments);
            stmt.setInt(3, deliveryOrderId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.log(Level.INFO, "Updated customer feedback for delivery order ID: {0}", deliveryOrderId);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No delivery order found with ID: {0}", deliveryOrderId);
                return false;
            }
        }
    }

    /**
     * Updates the follow-up status for a delivery order.
     *
     * @param deliveryOrderId The ID of the delivery order
     * @param followUp Whether follow-up is required
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public boolean updateFollowUpStatus(int deliveryOrderId, boolean followUp) throws SQLException {
        String sql = "UPDATE delivery_orders SET follow_up_required = ? WHERE delivery_order_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, followUp);
            stmt.setInt(2, deliveryOrderId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.log(Level.INFO, "Updated follow-up status to {0} for delivery order ID: {1}",
                        new Object[]{followUp, deliveryOrderId});
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No delivery order found with ID: {0}", deliveryOrderId);
                return false;
            }
        }
    }

    /**
     * Finds delivery orders within a date range.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return A list of delivery orders within the date range
     * @throws SQLException If a database access error occurs
     */
    public List<DeliveryOrder> findByDateRange(Date startDate, Date endDate) throws SQLException {
        List<DeliveryOrder> deliveryOrders = new ArrayList<>();
        String sql = "SELECT * FROM delivery_orders WHERE delivery_date BETWEEN ? AND ? ORDER BY delivery_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    deliveryOrders.add(mapResultSetToDeliveryOrder(rs));
                }
            }

            LOGGER.log(Level.INFO, "Found {0} delivery orders between {1} and {2}",
                    new Object[]{deliveryOrders.size(), startDate, endDate});
            return deliveryOrders;
        }
    }
}