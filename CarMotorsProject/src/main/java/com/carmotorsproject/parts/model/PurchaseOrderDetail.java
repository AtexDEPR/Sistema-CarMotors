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
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.purchaseOrder = purchaseOrder;
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
        return part.getName() + " x " + quantity;
    }
}