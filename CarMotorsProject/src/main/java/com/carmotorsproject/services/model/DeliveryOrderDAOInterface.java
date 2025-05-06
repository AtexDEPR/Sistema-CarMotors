package com.carmotorsproject.services.model;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interface for delivery order data access operations.
 */
public interface DeliveryOrderDAOInterface {

    /**
     * Saves a new delivery order to the database.
     *
     * @param deliveryOrder The delivery order to save
     * @return The saved delivery order with its generated ID
     * @throws SQLException If a database access error occurs
     */
    DeliveryOrder save(DeliveryOrder deliveryOrder) throws SQLException;

    /**
     * Updates an existing delivery order in the database.
     *
     * @param deliveryOrder The delivery order to update
     * @return The updated delivery order
     * @throws SQLException If a database access error occurs
     */
    DeliveryOrder update(DeliveryOrder deliveryOrder) throws SQLException;

    /**
     * Deletes a delivery order from the database.
     *
     * @param id The ID of the delivery order to delete
     * @return true if the delivery order was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds a delivery order by its ID.
     *
     * @param id The ID of the delivery order to find
     * @return The delivery order, or null if not found
     * @throws SQLException If a database access error occurs
     */
    DeliveryOrder findById(int id) throws SQLException;

    /**
     * Finds all delivery orders in the database.
     *
     * @return A list of all delivery orders
     * @throws SQLException If a database access error occurs
     */
    List<DeliveryOrder> findAll() throws SQLException;

    /**
     * Finds a delivery order by its service ID.
     *
     * @param serviceId The ID of the service
     * @return The delivery order, or null if not found
     * @throws SQLException If a database access error occurs
     */
    DeliveryOrder findByServiceId(int serviceId) throws SQLException;

    /**
     * Finds delivery orders that require follow-up.
     *
     * @return A list of delivery orders requiring follow-up
     * @throws SQLException If a database access error occurs
     */
    List<DeliveryOrder> findRequiringFollowUp() throws SQLException;

    /**
     * Finds delivery orders with low customer satisfaction.
     *
     * @return A list of delivery orders with low customer satisfaction
     * @throws SQLException If a database access error occurs
     */
    List<DeliveryOrder> findLowSatisfaction() throws SQLException;

    /**
     * Updates the customer feedback for a delivery order.
     *
     * @param deliveryOrderId The ID of the delivery order
     * @param satisfaction The customer satisfaction level
     * @param comments The customer feedback comments
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean updateCustomerFeedback(int deliveryOrderId, CustomerSatisfaction satisfaction, String comments) throws SQLException;

    /**
     * Updates the follow-up status of a delivery order.
     *
     * @param deliveryOrderId The ID of the delivery order
     * @param followUp The new follow-up status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean updateFollowUpStatus(int deliveryOrderId, boolean followUp) throws SQLException;

    /**
     * Finds delivery orders within a date range.
     *
     * @param start The start date of the range
     * @param end The end date of the range
     * @return A list of delivery orders within the date range
     * @throws SQLException If a database access error occurs
     */
    List<DeliveryOrder> findByDateRange(Date start, Date end) throws SQLException;
}