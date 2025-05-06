/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.controller;

import com.carmotorsproject.services.model.DAOFactory;
import com.carmotorsproject.services.model.DeliveryOrder;
import com.carmotorsproject.services.model.CustomerSatisfaction;
import com.carmotorsproject.services.model.Service;
import com.carmotorsproject.services.model.ServiceStatus;
import com.carmotorsproject.services.model.DeliveryOrderDAO;
import com.carmotorsproject.services.model.ServiceDAO;
import com.carmotorsproject.services.views.DeliveryOrderView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for delivery order-related operations.
 * Mediates between the DeliveryOrderView and the DeliveryOrderDAO.
 */
public class DeliveryOrderController {

    private static final Logger LOGGER = Logger.getLogger(DeliveryOrderController.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // View
    private DeliveryOrderView view;

    // DAOs
    private final DeliveryOrderDAO deliveryOrderDAO;
    private final ServiceDAO serviceDAO;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The delivery order view
     */
    public DeliveryOrderController(DeliveryOrderView view) {
        this.view = view;
        this.deliveryOrderDAO = DAOFactory.getDeliveryOrderDAO();
        this.serviceDAO = DAOFactory.getServiceDAO();
    }

    /**
     * Constructor that initializes the controller without a view.
     * Used for operations that don't require a view.
     */
    public DeliveryOrderController() {
        this.view = null;
        this.deliveryOrderDAO = DAOFactory.getDeliveryOrderDAO();
        this.serviceDAO = DAOFactory.getServiceDAO();
    }

    /**
     * Loads all delivery orders and updates the view.
     */
    public void loadDeliveryOrders() {
        try {
            List<DeliveryOrder> deliveryOrders = deliveryOrderDAO.findAll();
            if (view != null) {
                view.updateDeliveryOrderTable(deliveryOrders);
            }
            LOGGER.log(Level.INFO, "Loaded {0} delivery orders", deliveryOrders.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading delivery orders", ex);
            showError("Error loading delivery orders: " + ex.getMessage());
        }
    }

    /**
     * Loads the details of a specific delivery order and updates the view.
     *
     * @param deliveryOrderId The ID of the delivery order to load
     */
    public void loadDeliveryOrderDetails(int deliveryOrderId) {
        try {
            DeliveryOrder deliveryOrder = deliveryOrderDAO.findById(deliveryOrderId);
            if (deliveryOrder != null) {
                if (view != null) {
                    view.populateDeliveryOrderForm(deliveryOrder);
                }
                LOGGER.log(Level.INFO, "Loaded details for delivery order ID: {0}", deliveryOrderId);
            } else {
                LOGGER.log(Level.WARNING, "Delivery order not found with ID: {0}", deliveryOrderId);
                showError("Delivery order not found with ID: " + deliveryOrderId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading delivery order details", ex);
            showError("Error loading delivery order details: " + ex.getMessage());
        }
    }

    /**
     * Creates a new delivery order.
     *
     * @param deliveryOrder The delivery order to create
     * @param signatureConfirmed Whether the signature has been confirmed
     * @return The created delivery order
     * @throws IllegalArgumentException If validation fails
     * @throws SQLException If a database error occurs
     */
    public DeliveryOrder createDeliveryOrder(DeliveryOrder deliveryOrder, boolean signatureConfirmed)
            throws IllegalArgumentException, SQLException {
        // Validate the delivery order
        validateDeliveryOrder(deliveryOrder, signatureConfirmed);

        // Check if the service exists and is in the COMPLETED status
        Service service = serviceDAO.findById(deliveryOrder.getServiceId());
        if (service == null) {
            throw new IllegalArgumentException("Service not found with ID: " + deliveryOrder.getServiceId());
        }

        if (service.getStatus() != ServiceStatus.COMPLETED) {
            throw new IllegalArgumentException("Service must be in COMPLETED status to create a delivery order.");
        }

        // Check if a delivery order already exists for this service
        DeliveryOrder existingOrder = deliveryOrderDAO.findByServiceId(deliveryOrder.getServiceId());
        if (existingOrder != null) {
            throw new IllegalArgumentException("A delivery order already exists for this service.");
        }

        // Save the delivery order
        DeliveryOrder savedDeliveryOrder = deliveryOrderDAO.save(deliveryOrder);

        // Update the service status to DELIVERED
        serviceDAO.updateServiceStatus(deliveryOrder.getServiceId(), ServiceStatus.DELIVERED);

        LOGGER.log(Level.INFO, "Created delivery order with ID: {0}", savedDeliveryOrder.getDeliveryOrderId());

        return savedDeliveryOrder;
    }

    /**
     * Updates an existing delivery order.
     *
     * @param deliveryOrder The delivery order to update
     * @param signatureConfirmed Whether the signature has been confirmed
     * @return The updated delivery order
     * @throws IllegalArgumentException If validation fails
     * @throws SQLException If a database error occurs
     */
    public DeliveryOrder updateDeliveryOrder(DeliveryOrder deliveryOrder, boolean signatureConfirmed)
            throws IllegalArgumentException, SQLException {
        // Validate the delivery order
        validateDeliveryOrder(deliveryOrder, signatureConfirmed);

        // Check if the delivery order exists
        DeliveryOrder existingOrder = deliveryOrderDAO.findById(deliveryOrder.getDeliveryOrderId());
        if (existingOrder == null) {
            throw new IllegalArgumentException("Delivery order not found with ID: " + deliveryOrder.getDeliveryOrderId());
        }

        // Update the delivery order
        DeliveryOrder updatedDeliveryOrder = deliveryOrderDAO.update(deliveryOrder);

        LOGGER.log(Level.INFO, "Updated delivery order with ID: {0}", updatedDeliveryOrder.getDeliveryOrderId());

        return updatedDeliveryOrder;
    }

    /**
     * Deletes a delivery order.
     *
     * @param deliveryOrderId The ID of the delivery order to delete
     * @return true if the delivery order was deleted, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean deleteDeliveryOrder(int deliveryOrderId) throws SQLException {
        // Check if the delivery order exists
        DeliveryOrder existingOrder = deliveryOrderDAO.findById(deliveryOrderId);
        if (existingOrder == null) {
            LOGGER.log(Level.WARNING, "Delivery order not found with ID: {0}", deliveryOrderId);
            return false;
        }

        // Delete the delivery order
        boolean deleted = deliveryOrderDAO.delete(deliveryOrderId);

        if (deleted) {
            LOGGER.log(Level.INFO, "Deleted delivery order with ID: {0}", deliveryOrderId);
        } else {
            LOGGER.log(Level.WARNING, "Failed to delete delivery order with ID: {0}", deliveryOrderId);
        }

        return deleted;
    }

    /**
     * Updates the customer satisfaction and feedback for a delivery order.
     *
     * @param deliveryOrderId The ID of the delivery order
     * @param satisfaction The customer satisfaction level
     * @param feedback The customer feedback
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateCustomerFeedback(int deliveryOrderId, CustomerSatisfaction satisfaction, String feedback)
            throws SQLException {
        // Validate the satisfaction level
        if (satisfaction == null) {
            throw new IllegalArgumentException("Customer satisfaction level is required.");
        }

        // Update the customer feedback
        boolean updated = deliveryOrderDAO.updateCustomerFeedback(deliveryOrderId, satisfaction, feedback);

        if (updated) {
            LOGGER.log(Level.INFO, "Updated customer feedback for delivery order ID: {0}", deliveryOrderId);
        } else {
            LOGGER.log(Level.WARNING, "Failed to update customer feedback for delivery order ID: {0}", deliveryOrderId);
        }

        return updated;
    }

    /**
     * Updates the follow-up status of a delivery order.
     *
     * @param deliveryOrderId The ID of the delivery order
     * @param followUpRequired The new follow-up status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateFollowUpStatus(int deliveryOrderId, boolean followUpRequired) throws SQLException {
        // Update the follow-up status
        boolean updated = deliveryOrderDAO.updateFollowUpStatus(deliveryOrderId, followUpRequired);

        if (updated) {
            LOGGER.log(Level.INFO, "Updated follow-up status for delivery order ID: {0}", deliveryOrderId);
        } else {
            LOGGER.log(Level.WARNING, "Failed to update follow-up status for delivery order ID: {0}", deliveryOrderId);
        }

        return updated;
    }

    /**
     * Finds delivery orders that require follow-up.
     *
     * @return A list of delivery orders requiring follow-up
     * @throws SQLException If a database error occurs
     */
    public List<DeliveryOrder> findRequiringFollowUp() throws SQLException {
        List<DeliveryOrder> deliveryOrders = deliveryOrderDAO.findRequiringFollowUp();
        LOGGER.log(Level.INFO, "Found {0} delivery orders requiring follow-up", deliveryOrders.size());
        return deliveryOrders;
    }

    /**
     * Finds delivery orders with low customer satisfaction.
     *
     * @return A list of delivery orders with low customer satisfaction
     * @throws SQLException If a database error occurs
     */
    public List<DeliveryOrder> findLowSatisfaction() throws SQLException {
        List<DeliveryOrder> deliveryOrders = deliveryOrderDAO.findLowSatisfaction();
        LOGGER.log(Level.INFO, "Found {0} delivery orders with low customer satisfaction", deliveryOrders.size());
        return deliveryOrders;
    }

    /**
     * Finds delivery orders within a date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of delivery orders within the date range
     * @throws SQLException If a database error occurs
     */
    public List<DeliveryOrder> findByDateRange(Date startDate, Date endDate) throws SQLException {
        List<DeliveryOrder> deliveryOrders = deliveryOrderDAO.findByDateRange((java.sql.Date) startDate, (java.sql.Date) endDate);
        LOGGER.log(Level.INFO, "Found {0} delivery orders within date range", deliveryOrders.size());
        return deliveryOrders;
    }

    /**
     * Validates a delivery order.
     *
     * @param deliveryOrder The delivery order to validate
     * @param signatureConfirmed Whether the signature has been confirmed
     * @throws IllegalArgumentException If validation fails
     */
    private void validateDeliveryOrder(DeliveryOrder deliveryOrder, boolean signatureConfirmed)
            throws IllegalArgumentException {
        // Check if the service ID is valid
        if (deliveryOrder.getServiceId() <= 0) {
            throw new IllegalArgumentException("Service ID is required.");
        }

        // Check if the delivery date is valid
        if (deliveryOrder.getDeliveryDate() == null) {
            throw new IllegalArgumentException("Delivery date is required.");
        }

        // Check if the delivered by field is valid
        if (deliveryOrder.getDeliveredBy() == null || deliveryOrder.getDeliveredBy().trim().isEmpty()) {
            throw new IllegalArgumentException("Delivered by is required.");
        }

        // Check if the received by field is valid
        if (deliveryOrder.getReceivedBy() == null || deliveryOrder.getReceivedBy().trim().isEmpty()) {
            throw new IllegalArgumentException("Received by is required.");
        }

        // Check if the signature has been confirmed
        if (!signatureConfirmed) {
            throw new IllegalArgumentException("Customer signature is required.");
        }
    }

    /**
     * Parses a date string in the format yyyy-MM-dd.
     *
     * @param dateStr The date string to parse
     * @return The parsed date
     * @throws IllegalArgumentException If the date string is invalid
     */
    public Date parseDate(String dateStr) throws IllegalArgumentException {
        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    /**
     * Shows an error message.
     *
     * @param message The error message to show
     */
    private void showError(String message) {
        if (view != null) {
            javax.swing.JOptionPane.showMessageDialog(view, message, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}