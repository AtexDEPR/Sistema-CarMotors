/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

/**
 * Represents an item in a purchase order.
 * Implements Identifiable interface to ensure it has a unique ID.
 */
public class PurchaseOrderDetail implements Identifiable {
    private int id;
    private int orderId;
    private int partId;
    private String partName;
    private Part part;
    private int quantity;
    private double unitPrice;
    private double subtotal;
    private PurchaseOrder purchaseOrder;

    /**
     * Default constructor.
     */
    public PurchaseOrderDetail() {
    }

    /**
     * Constructor with essential fields.
     *
     * @param id The item ID
     * @param part The part
     * @param quantity The quantity
     * @param unitPrice The unit price
     */
    public PurchaseOrderDetail(int id, Part part, int quantity, double unitPrice) {
        this.id = id;
        this.part = part;
        if (part != null) {
            this.partId = part.getId();
            this.partName = part.getName();
        }
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    /**
     * Full constructor with all fields.
     *
     * @param id The item ID
     * @param part The part
     * @param quantity The quantity
     * @param unitPrice The unit price
     * @param purchaseOrder The purchase order
     */
    public PurchaseOrderDetail(int id, Part part, int quantity, double unitPrice, PurchaseOrder purchaseOrder) {
        this.id = id;
        this.part = part;
        if (part != null) {
            this.partId = part.getId();
            this.partName = part.getName();
        }
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.purchaseOrder = purchaseOrder;
        if (purchaseOrder != null) {
            this.orderId = purchaseOrder.getId();
        }
        calculateSubtotal();
    }

    /**
     * Gets the item ID.
     *
     * @return The item ID
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Sets the item ID.
     *
     * @param id The item ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the detail ID (alias for getId).
     *
     * @return The detail ID
     */
    public int getDetailId() {
        return id;
    }

    /**
     * Sets the detail ID (alias for setId).
     *
     * @param detailId The detail ID
     */
    public void setDetailId(int detailId) {
        this.id = detailId;
    }

    /**
     * Gets the order ID.
     *
     * @return The order ID
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the order ID.
     *
     * @param orderId The order ID
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the part ID.
     *
     * @return The part ID
     */
    public int getPartId() {
        return partId;
    }

    /**
     * Sets the part ID.
     *
     * @param partId The part ID
     */
    public void setPartId(int partId) {
        this.partId = partId;
    }

    /**
     * Gets the part name.
     *
     * @return The part name
     */
    public String getPartName() {
        return partName;
    }

    /**
     * Sets the part name.
     *
     * @param partName The part name
     */
    public void setPartName(String partName) {
        this.partName = partName;
    }

    /**
     * Gets the part.
     *
     * @return The part
     */
    public Part getPart() {
        return part;
    }

    /**
     * Sets the part.
     *
     * @param part The part
     */
    public void setPart(Part part) {
        this.part = part;
        if (part != null) {
            this.partId = part.getId();
            this.partName = part.getName();
        }
        calculateSubtotal();
    }

    /**
     * Gets the quantity.
     *
     * @return The quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity.
     *
     * @param quantity The quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    /**
     * Gets the unit price.
     *
     * @return The unit price
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price.
     *
     * @param unitPrice The unit price
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    /**
     * Gets the subtotal.
     *
     * @return The subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Gets the purchase order.
     *
     * @return The purchase order
     */
    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    /**
     * Sets the purchase order.
     *
     * @param purchaseOrder The purchase order
     */
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
        if (purchaseOrder != null) {
            this.orderId = purchaseOrder.getId();
        }
    }

    /**
     * Calculates the subtotal.
     */
    private void calculateSubtotal() {
        this.subtotal = this.quantity * this.unitPrice;
    }

    /**
     * Returns a string representation of the purchase order item.
     *
     * @return A string representation of the purchase order item
     */
    @Override
    public String toString() {
        return (partName != null ? partName : "Unknown Part") + " x " + quantity;
    }
}