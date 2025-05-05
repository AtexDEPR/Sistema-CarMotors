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
    private int supplierId;
    private Supplier supplier;
    private Date orderDate;
    private Date expectedDate;
    private Date actualDeliveryDate;
    private String status;
    private String notes;
    private double total;
    private List<PurchaseOrderDetail> details;

    /**
     * Default constructor.
     */
    public PurchaseOrder() {
        this.orderDate = new Date();
        this.status = "PENDIENTE";
        this.details = new ArrayList<>();
    }

    /**
     * Constructor with essential fields.
     *
     * @param id The order ID
     * @param supplier The supplier
     * @param orderDate The order date
     * @param status The order status
     */
    public PurchaseOrder(int id, Supplier supplier, Date orderDate, String status) {
        this.id = id;
        this.supplier = supplier;
        if (supplier != null) {
            this.supplierId = supplier.getId();
        }
        this.orderDate = orderDate;
        this.status = status;
        this.details = new ArrayList<>();
    }

    /**
     * Full constructor with all fields.
     *
     * @param id The order ID
     * @param supplier The supplier
     * @param orderDate The order date
     * @param expectedDate The expected delivery date
     * @param actualDeliveryDate The actual delivery date
     * @param status The order status
     * @param notes Order notes
     * @param details Order details
     */
    public PurchaseOrder(int id, Supplier supplier, Date orderDate, Date expectedDate,
                         Date actualDeliveryDate, String status, String notes,
                         List<PurchaseOrderDetail> details) {
        this.id = id;
        this.supplier = supplier;
        if (supplier != null) {
            this.supplierId = supplier.getId();
        }
        this.orderDate = orderDate;
        this.expectedDate = expectedDate;
        this.actualDeliveryDate = actualDeliveryDate;
        this.status = status;
        this.notes = notes;
        this.details = details != null ? details : new ArrayList<>();
        calculateTotal();
    }

    /**
     * Gets the order ID.
     *
     * @return The order ID
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Sets the order ID.
     *
     * @param id The order ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the supplier ID.
     *
     * @return The supplier ID
     */
    public int getSupplierId() {
        return supplierId;
    }

    /**
     * Sets the supplier ID.
     *
     * @param supplierId The supplier ID
     */
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
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
        if (supplier != null) {
            this.supplierId = supplier.getId();
        }
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
    }

    /**
     * Gets the expected delivery date.
     *
     * @return The expected delivery date
     */
    public Date getExpectedDate() {
        return expectedDate;
    }

    /**
     * Sets the expected delivery date.
     *
     * @param expectedDate The expected delivery date
     */
    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    /**
     * Gets the actual delivery date.
     *
     * @return The actual delivery date
     */
    public Date getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    /**
     * Sets the actual delivery date.
     *
     * @param actualDeliveryDate The actual delivery date
     */
    public void setActualDeliveryDate(Date actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    /**
     * Gets the order status.
     *
     * @return The order status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the order status.
     *
     * @param status The order status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the order notes.
     *
     * @return The order notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the order notes.
     *
     * @param notes The order notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the order total.
     *
     * @return The order total
     */
    public double getTotal() {
        return total;
    }

    /**
     * Sets the order total.
     *
     * @param total The order total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Gets the order details.
     *
     * @return The order details
     */
    public List<PurchaseOrderDetail> getDetails() {
        return details;
    }

    /**
     * Sets the order details.
     *
     * @param details The order details
     */
    public void setDetails(List<PurchaseOrderDetail> details) {
        this.details = details != null ? details : new ArrayList<>();
        calculateTotal();
    }

    /**
     * Adds a detail to the order.
     *
     * @param detail The detail to add
     */
    public void addDetail(PurchaseOrderDetail detail) {
        if (details == null) {
            details = new ArrayList<>();
        }
        details.add(detail);
        detail.setPurchaseOrder(this);
        calculateTotal();
    }

    /**
     * Removes a detail from the order.
     *
     * @param detail The detail to remove
     * @return True if the detail was removed, false otherwise
     */
    public boolean removeDetail(PurchaseOrderDetail detail) {
        if (details != null && details.remove(detail)) {
            calculateTotal();
            return true;
        }
        return false;
    }

    /**
     * Calculates the order total.
     */
    private void calculateTotal() {
        total = 0.0;
        if (details != null) {
            for (PurchaseOrderDetail detail : details) {
                total += detail.getQuantity() * detail.getUnitPrice();
            }
        }
    }

    /**
     * Returns a string representation of the purchase order.
     *
     * @return A string representation of the purchase order
     */
    @Override
    public String toString() {
        return "Order #" + id + " - " + (supplier != null ? supplier.getName() : "Unknown Supplier");
    }
}