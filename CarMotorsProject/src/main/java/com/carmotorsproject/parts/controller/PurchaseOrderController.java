/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;

import com.carmotorsproject.parts.model.PurchaseOrderDAO;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PurchaseOrder;
import com.carmotorsproject.parts.model.PurchaseOrderDetail;
import com.carmotorsproject.parts.model.Supplier;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for PurchaseOrder entities.
 * Handles business logic for purchase orders.
 */
public class PurchaseOrderController {
    private static final Logger LOGGER = Logger.getLogger(PurchaseOrderController.class.getName());
    private final PurchaseOrderDAO purchaseOrderDAO;
    private final PartController partController;

    /**
     * Constructor.
     */
    public PurchaseOrderController() {
        this.purchaseOrderDAO = new PurchaseOrderDAO();
        this.partController = new PartController();
    }

    /**
     * Creates a new purchase order.
     *
     * @param purchaseOrder The purchase order to create
     * @return The created purchase order with its generated ID, or null if the creation failed
     */
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        LOGGER.log(Level.INFO, "Creating purchase order: {0}", purchaseOrder.getOrderNumber());
        return purchaseOrderDAO.insert(purchaseOrder);
    }

    /**
     * Updates an existing purchase order.
     *
     * @param purchaseOrder The purchase order to update
     * @return True if the update was successful, false otherwise
     */
    public boolean updatePurchaseOrder(PurchaseOrder purchaseOrder) {
        LOGGER.log(Level.INFO, "Updating purchase order with ID: {0}", purchaseOrder.getId());
        return purchaseOrderDAO.update(purchaseOrder);
    }

    /**
     * Deletes a purchase order.
     *
     * @param id The ID of the purchase order to delete
     * @return True if the deletion was successful, false otherwise
     */
    public boolean deletePurchaseOrder(int id) {
        LOGGER.log(Level.INFO, "Deleting purchase order with ID: {0}", id);
        return purchaseOrderDAO.delete(id);
    }

    /**
     * Gets a purchase order by its ID.
     *
     * @param id The ID of the purchase order to get
     * @return The found purchase order, or null if no purchase order was found
     */
    public PurchaseOrder getPurchaseOrderById(int id) {
        LOGGER.log(Level.INFO, "Getting purchase order with ID: {0}", id);
        return purchaseOrderDAO.findById(id);
    }

    /**
     * Gets a purchase order by its order number.
     *
     * @param orderNumber The order number of the purchase order to get
     * @return The found purchase order, or null if no purchase order was found
     */
    public PurchaseOrder getPurchaseOrderByOrderNumber(String orderNumber) {
        LOGGER.log(Level.INFO, "Getting purchase order with order number: {0}", orderNumber);
        return purchaseOrderDAO.findByOrderNumber(orderNumber);
    }

    /**
     * Gets all purchase orders.
     *
     * @return A list of all purchase orders
     */
    public List<PurchaseOrder> getAllPurchaseOrders() {
        LOGGER.info("Getting all purchase orders");
        return purchaseOrderDAO.findAll();
    }

    /**
     * Gets purchase orders by supplier.
     *
     * @param supplier The supplier
     * @return A list of purchase orders for the specified supplier
     */
    public List<PurchaseOrder> getPurchaseOrdersBySupplier(Supplier supplier) {
        LOGGER.log(Level.INFO, "Getting purchase orders by supplier ID: {0}", supplier.getId());
        return purchaseOrderDAO.findBySupplier(supplier.getId());
    }

    /**
     * Gets purchase orders by status.
     *
     * @param status The status to search for
     * @return A list of purchase orders with the specified status
     */
    public List<PurchaseOrder> getPurchaseOrdersByStatus(String status) {
        LOGGER.log(Level.INFO, "Getting purchase orders by status: {0}", status);
        return purchaseOrderDAO.findByStatus(status);
    }

    /**
     * Gets purchase orders by date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of purchase orders within the specified date range
     */
    public List<PurchaseOrder> getPurchaseOrdersByDateRange(Date startDate, Date endDate) {
        LOGGER.log(Level.INFO, "Getting purchase orders by date range: {0} to {1}", new Object[]{startDate, endDate});
        return purchaseOrderDAO.findByDateRange(startDate, endDate);
    }

    /**
     * Creates a new purchase order item.
     *
     * @param purchaseOrder The purchase order
     * @param part The part
     * @param quantity The quantity
     * @param unitPrice The unit price
     * @return The created purchase order item
     */
    public PurchaseOrderDetail createPurchaseOrderItem(PurchaseOrder purchaseOrder, Part part, int quantity, double unitPrice) {
        LOGGER.log(Level.INFO, "Creating purchase order item for part: {0}", part.getName());

        PurchaseOrderDetail item = new PurchaseOrderDetail();
        item.setPurchaseOrder(purchaseOrder);
        item.setPart(part);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);

        purchaseOrder.addItem(item);

        return item;
    }

    /**
     * Marks a purchase order as received.
     * Updates the delivery date and status, and adds the received parts to inventory.
     *
     * @param id The ID of the purchase order
     * @param deliveryDate The delivery date
     * @return True if the purchase order was successfully marked as received, false otherwise
     */
    public boolean markPurchaseOrderAsReceived(int id, Date deliveryDate) {
        LOGGER.log(Level.INFO, "Marking purchase order with ID {0} as received on {1}", new Object[]{id, deliveryDate});

        PurchaseOrder purchaseOrder = purchaseOrderDAO.findById(id);

        if (purchaseOrder == null) {
            LOGGER.log(Level.WARNING, "Purchase order not found with ID: {0}", id);
            return false;
        }

        if (!"PENDING".equals(purchaseOrder.getStatus())) {
            LOGGER.log(Level.WARNING, "Purchase order with ID {0} is not in PENDING status", id);
            return false;
        }

        // Update purchase order status and delivery date
        purchaseOrder.setDeliveryDate(deliveryDate);
        purchaseOrder.setStatus("RECEIVED");

        // Add received parts to inventory
        for (PurchaseOrderDetail item : purchaseOrder.getItems()) {
            Part part = item.getPart();
            if (part != null) {
                partController.addStock(part.getId(), item.getQuantity());
            }
        }

        return purchaseOrderDAO.update(purchaseOrder);
    }

    /**
     * Cancels a purchase order.
     *
     * @param id The ID of the purchase order to cancel
     * @return True if the purchase order was successfully cancelled, false otherwise
     */
    public boolean cancelPurchaseOrder(int id) {
        LOGGER.log(Level.INFO, "Cancelling purchase order with ID: {0}", id);

        PurchaseOrder purchaseOrder = purchaseOrderDAO.findById(id);

        if (purchaseOrder == null) {
            LOGGER.log(Level.WARNING, "Purchase order not found with ID: {0}", id);
            return false;
        }

        if (!"PENDING".equals(purchaseOrder.getStatus())) {
            LOGGER.log(Level.WARNING, "Purchase order with ID {0} is not in PENDING status", id);
            return false;
        }

        purchaseOrder.setStatus("CANCELLED");

        return purchaseOrderDAO.update(purchaseOrder);
    }

    /**
     * Generates a new order number.
     *
     * @return A new order number
     */
    public String generateOrderNumber() {
        // Generate a unique order number based on the current timestamp
        String prefix = "PO";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp.substring(timestamp.length() - 8);
    }

    /**
     * Creates a purchase order for parts that are below their minimum stock level.
     *
     * @param supplier The supplier
     * @return The created purchase order, or null if no parts are below their minimum stock level
     */
    public PurchaseOrder createPurchaseOrderForLowStock(Supplier supplier) {
        LOGGER.log(Level.INFO, "Creating purchase order for low stock parts from supplier: {0}", supplier.getName());

        // Get parts below minimum stock level for the specified supplier
        List<Part> lowStockParts = partController.getPartsBelowMinimumStock();

        if (lowStockParts.isEmpty()) {
            LOGGER.info("No parts below minimum stock level");
            return null;
        }

        // Create a new purchase order
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderNumber(generateOrderNumber());
        purchaseOrder.setOrderDate(new Date());
        purchaseOrder.setStatus("PENDING");
        purchaseOrder.setSupplier(supplier);

        // Add items for each low stock part
        for (Part part : lowStockParts) {
            if (part.getSupplier() != null && part.getSupplier().getId() == supplier.getId()) {
                int quantityToOrder = part.getMinimumStock() - part.getQuantityInStock();
                if (quantityToOrder > 0) {
                    createPurchaseOrderItem(purchaseOrder, part, quantityToOrder, part.getPrice());
                }
            }
        }

        // If no items were added, return null
        if (purchaseOrder.getItems().isEmpty()) {
            LOGGER.info("No low stock parts found for the specified supplier");
            return null;
        }

        // Save the purchase order
        return createPurchaseOrder(purchaseOrder);
    }
}