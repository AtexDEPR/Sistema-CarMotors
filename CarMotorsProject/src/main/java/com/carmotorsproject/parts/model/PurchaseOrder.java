/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a purchase order for parts.
 * Implements Identifiable interface to ensure it has a unique ID.
 */
public class PurchaseOrder implements Identifiable {
    private int id;
    private String orderNumber;
    private Date orderDate;
    private Date expectedDeliveryDate;
    private Date deliveryDate;
    private String status;
    private double totalAmount;
    private String notes;
    private Supplier supplier;
    private List<PurchaseOrderItem> items;
    private Date createdAt;
    private Date updatedAt;

    /**
     * Default constructor.
     */
    public PurchaseOrder() {
        this.orderDate = new Date();
        this.status = "PENDING";
        this.items = new ArrayList<>();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /**
     * Constructor with essential fields.
     *
     * @param id The purchase order ID
     * @param orderNumber The purchase order number
     * @param orderDate The order date
     * @param supplier The supplier
     */
    public PurchaseOrder(int id, String orderNumber, Date orderDate, Supplier supplier) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.supplier = supplier;
        this.status = "PENDING";
        this.items = new ArrayList<>();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /**
     * Full constructor with all fields.
     *
     * @param id The purchase order ID
     * @param orderNumber The purchase order number
     * @param orderDate The order date
     * @param expectedDeliveryDate The expected delivery date
     * @param deliveryDate The actual delivery date
     * @param status The purchase order status
     * @param totalAmount The total amount
     * @param notes Additional notes
     * @param supplier The supplier
     */
    public PurchaseOrder(int id, String orderNumber, Date orderDate, Date expectedDeliveryDate,
                         Date deliveryDate, String status, double totalAmount, String notes,
                         Supplier supplier) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.notes = notes;
        this.supplier = supplier;
        this.items = new ArrayList<>();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /**
     * Gets the purchase order ID.
     *
     * @return The purchase order ID
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Sets the purchase order ID.
     *
     * @param id The purchase order ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the purchase order number.
     *
     * @return The purchase order number
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets the purchase order number.
     *
     * @param orderNumber The purchase order number
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        this.updatedAt = new Date();
    }

    /**
     * Gets the order date.
     *
     * @return The order date
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the order date.
     *
     * @param orderDate The order date
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
        this.updatedAt = new Date();
    }

    /**
     * Gets the expected delivery date.
     *
     * @return The expected delivery date
     */
    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    /**
     * Sets the expected delivery date.
     *
     * @param expectedDeliveryDate The expected delivery date
     */
    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.updatedAt = new Date();
    }

    /**
     * Gets the actual delivery date.
     *
     * @return The actual delivery date
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Sets the actual delivery date.
     *
     * @param deliveryDate The actual delivery date
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
        this.updatedAt = new Date();
    }

    /**
     * Gets the purchase order status.
     *
     * @return The purchase order status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the purchase order status.
     *
     * @param status The purchase order status
     */
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = new Date();
    }

    /**
     * Gets the total amount.
     *
     * @return The total amount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the total amount.
     *
     * @param totalAmount The total amount
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        this.updatedAt = new Date();
    }

    /**
     * Gets additional notes.
     *
     * @return Additional notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets additional notes.
     *
     * @param notes Additional notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
        this.updatedAt = new Date();
    }

    /**
     * Gets the supplier.
     *
     * @return The supplier
     */
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * Sets the supplier.
     *
     * @param supplier The supplier
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        this.updatedAt = new Date();
    }

    /**
     * Gets the list of purchase order items.
     *
     * @return The list of purchase order items
     */
    public List<PurchaseOrderItem> getItems() {
        return items;
    }

    /**
     * Sets the list of purchase order items.
     *
     * @param items The list of purchase order items
     */
    public void setItems(List<PurchaseOrderItem> items) {
        this.items = items;
        this.updatedAt = new Date();
        calculateTotalAmount();
    }

    /**
     * Gets the creation date.
     *
     * @return The creation date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation date.
     *
     * @param createdAt The creation date
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last update date.
     *
     * @return The last update date
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update date.
     *
     * @param updatedAt The last update date
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Adds an item to the purchase order.
     *
     * @param item The item to add
     */
    public void addItem(PurchaseOrderItem item) {
        if (item != null) {
            items.add(item);
            calculateTotalAmount();
            this.updatedAt = new Date();
        }
    }

    /**
     * Removes an item from the purchase order.
     *
     * @param item The item to remove
     * @return True if the item was successfully removed, false otherwise
     */
    public boolean removeItem(PurchaseOrderItem item) {
        if (item != null && items.contains(item)) {
            items.remove(item);
            calculateTotalAmount();
            this.updatedAt = new Date();
            return true;
        }
        return false;
    }

    /**
     * Calculates the total amount of the purchase order.
     */
    public void calculateTotalAmount() {
        totalAmount = 0;
        for (PurchaseOrderItem item : items) {
            totalAmount += item.getSubtotal();
        }
    }

    /**
     * Marks the purchase order as received.
     * Updates the delivery date and status, and adds the received parts to inventory.
     *
     * @param deliveryDate The delivery date
     * @return True if the purchase order was successfully marked as received, false otherwise
     */
    public boolean markAsReceived(Date deliveryDate) {
        if (!"RECEIVED".equals(status)) {
            this.deliveryDate = deliveryDate;
            this.status = "RECEIVED";
            this.updatedAt = new Date();

            // Add received parts to inventory
            for (PurchaseOrderItem item : items) {
                Part part = item.getPart();
                if (part != null) {
                    part.addStock(item.getQuantity());
                }
            }

            return true;
        }
        return false;
    }

    /**
     * Cancels the purchase order.
     *
     * @return True if the purchase order was successfully cancelled, false otherwise
     */
    public boolean cancel() {
        if ("PENDING".equals(status)) {
            this.status = "CANCELLED";
            this.updatedAt = new Date();
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the purchase order.
     *
     * @return A string representation of the purchase order
     */
    @Override
    public String toString() {
        return "Orden #" + orderNumber + " - " + supplier.getName();
    }
}