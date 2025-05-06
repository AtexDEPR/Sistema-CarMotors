/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import java.util.Date;

/**
 * Represents a car part in the inventory.
 * Implements Identifiable interface to ensure it has a unique ID.
 */
public class Part implements Identifiable {
    private int id;
    private String name;
    private String description;
    private String reference;
    private String type;
    private double price;
    private double purchasePrice;
    private double sellingPrice;
    private int quantityInStock;
    private int minimumStock;
    private String location;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private Supplier supplier;
    private int supplierId;

    /**
     * Default constructor.
     */
    public Part() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.status = "ACTIVE";
    }

    /**
     * Constructor with essential fields.
     *
     * @param id The part ID
     * @param name The part name
     * @param reference The part reference code
     * @param type The part type
     * @param price The part price
     * @param quantityInStock The quantity in stock
     */
    public Part(int id, String name, String reference, String type, double price, int quantityInStock) {
        this.id = id;
        this.name = name;
        this.reference = reference;
        this.type = type;
        this.price = price;
        this.purchasePrice = price; // Default to same as price
        this.sellingPrice = price;  // Default to same as price
        this.quantityInStock = quantityInStock;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.status = "ACTIVE";
    }

    /**
     * Full constructor with all fields.
     *
     * @param id The part ID
     * @param name The part name
     * @param description The part description
     * @param reference The part reference code
     * @param type The part type
     * @param price The part price
     * @param quantityInStock The quantity in stock
     * @param minimumStock The minimum stock level
     * @param location The storage location
     * @param status The part status
     * @param supplier The part supplier
     */
    public Part(int id, String name, String description, String reference, String type,
                double price, int quantityInStock, int minimumStock, String location,
                String status, int supplier) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.reference = reference;
        this.type = type;
        this.price = price;
        this.purchasePrice = price; // Default to same as price
        this.sellingPrice = price;  // Default to same as price
        this.quantityInStock = quantityInStock;
        this.minimumStock = minimumStock;
        this.location = location;
        this.status = status;


        this.supplierId = supplier;

        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /**
     * Gets the part ID.
     *
     * @return The part ID
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Gets the part ID (alias for getId).
     *
     * @return The part ID
     */
    public int getPartId() {
        return id;
    }

    /**
     * Sets the part ID.
     *
     * @param id The part ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the part ID (alias for setId).
     *
     * @param partId The part ID
     */
    public void setPartId(int partId) {
        this.id = partId;
    }

    /**
     * Gets the part name.
     *
     * @return The part name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the part name.
     *
     * @param name The part name
     */
    public void setName(String name) {
        this.name = name;
        this.updatedAt = new Date();
    }

    /**
     * Gets the part description.
     *
     * @return The part description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the part description.
     *
     * @param description The part description
     */
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = new Date();
    }

    /**
     * Gets the part reference code.
     *
     * @return The part reference code
     */
    public String getReference() {
        return reference;
    }

    /**
     * Gets the part number (alias for getReference).
     *
     * @return The part number
     */
    public String getPartNumber() {
        return reference;
    }

    /**
     * Sets the part reference code.
     *
     * @param reference The part reference code
     */
    public void setReference(String reference) {
        this.reference = reference;
        this.updatedAt = new Date();
    }

    /**
     * Sets the part number (alias for setReference).
     *
     * @param partNumber The part number
     */
    public void setPartNumber(String partNumber) {
        this.reference = partNumber;
        this.updatedAt = new Date();
    }

    /**
     * Gets the part type.
     *
     * @return The part type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the part type as enum.
     *
     * @return The part type as enum
     */


    /**
     * Sets the part type.
     *
     * @param type The part type
     */
    public void setType(String type) {
        this.type = type;
        this.updatedAt = new Date();
    }

    /**
     * Sets the part type from enum.
     *
     * @param type The part type enum
     */
    public void setType(PartType type) {
        this.type = type.toString();
        this.updatedAt = new Date();
    }

    /**
     * Gets the part status.
     *
     * @return The part status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the part status as enum.
     *
     * @return The part status as enum
     */


    /**
     * Sets the part status.
     *
     * @param status The part status
     */
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = new Date();
    }

    /**
     * Sets the part status from enum.
     *
     * @param status The part status enum
     */
    public void setStatus(PartStatus status) {
        this.status = status.toString();
        this.updatedAt = new Date();
    }

    /**
     * Gets the part price.
     *
     * @return The part price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the part price.
     *
     * @param price The part price
     */
    public void setPrice(double price) {
        this.price = price;
        this.updatedAt = new Date();
    }

    /**
     * Gets the purchase price.
     *
     * @return The purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Sets the purchase price.
     *
     * @param purchasePrice The purchase price
     */
    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
        this.updatedAt = new Date();
    }

    /**
     * Gets the selling price.
     *
     * @return The selling price
     */
    public double getSellingPrice() {
        return sellingPrice;
    }

    /**
     * Sets the selling price.
     *
     * @param sellingPrice The selling price
     */
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
        this.updatedAt = new Date();
    }

    /**
     * Gets the quantity in stock.
     *
     * @return The quantity in stock
     */
    public int getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Gets the stock quantity (alias for getQuantityInStock).
     *
     * @return The stock quantity
     */
    public int getStockQuantity() {
        return quantityInStock;
    }

    /**
     * Sets the quantity in stock.
     *
     * @param quantityInStock The quantity in stock
     */
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
        this.updatedAt = new Date();
    }

    /**
     * Sets the stock quantity (alias for setQuantityInStock).
     *
     * @param stockQuantity The stock quantity
     */
    public void setStockQuantity(int stockQuantity) {
        this.quantityInStock = stockQuantity;
        this.updatedAt = new Date();
    }

    /**
     * Gets the minimum stock level.
     *
     * @return The minimum stock level
     */
    public int getMinimumStock() {
        return minimumStock;
    }

    /**
     * Gets the minimum stock level (alias for getMinimumStock).
     *
     * @return The minimum stock level
     */
    public int getMinStockLevel() {
        return minimumStock;
    }

    /**
     * Sets the minimum stock level.
     *
     * @param minimumStock The minimum stock level
     */
    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
        this.updatedAt = new Date();
    }

    /**
     * Sets the minimum stock level (alias for setMinimumStock).
     *
     * @param minStockLevel The minimum stock level
     */
    public void setMinStockLevel(int minStockLevel) {
        this.minimumStock = minStockLevel;
        this.updatedAt = new Date();
    }

    /**
     * Gets the storage location.
     *
     * @return The storage location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the storage location.
     *
     * @param location The storage location
     */
    public void setLocation(String location) {
        this.location = location;
        this.updatedAt = new Date();
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
     * Gets the part supplier.
     *
     * @return The part supplier
     */
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * Gets the supplier ID.
     *
     * @return The supplier ID
     */
    public int getSupplierId() {
        if (supplier != null) {
            return supplier.getId();
        }
        return supplierId;
    }

    /**
     * Sets the part supplier.
     *
     * @param supplier The part supplier
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        if (supplier != null) {
            this.supplierId = supplier.getId();
        }
        this.updatedAt = new Date();
    }

    /**
     * Sets the supplier ID.
     *
     * @param supplierId The supplier ID
     */
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
        this.updatedAt = new Date();
    }

    /**
     * Checks if the part is below the minimum stock level.
     *
     * @return True if the part is below the minimum stock level, false otherwise
     */
    public boolean isBelowMinimumStock() {
        return quantityInStock < minimumStock;
    }

    /**
     * Adds the specified quantity to the stock.
     *
     * @param quantity The quantity to add
     */
    public void addStock(int quantity) {
        if (quantity > 0) {
            this.quantityInStock += quantity;
            this.updatedAt = new Date();
        }
    }

    /**
     * Removes the specified quantity from the stock.
     *
     * @param quantity The quantity to remove
     * @return True if the quantity was successfully removed, false if there is not enough stock
     */
    public boolean removeStock(int quantity) {
        if (quantity > 0 && quantity <= this.quantityInStock) {
            this.quantityInStock -= quantity;
            this.updatedAt = new Date();
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the part.
     *
     * @return A string representation of the part
     */
    @Override
    public String toString() {
        return name + " (" + reference + ")";
    }
}