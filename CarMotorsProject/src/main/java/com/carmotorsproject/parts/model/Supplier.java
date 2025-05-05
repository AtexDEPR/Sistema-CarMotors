/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a supplier of car parts.
 * Implements Identifiable interface to ensure it has a unique ID.
 */
public class Supplier implements Identifiable {
    private int id;
    private String name;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String taxId;
    private String notes;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private List<Part> suppliedParts;

    /**
     * Default constructor.
     */
    public Supplier() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.status = "ACTIVE";
        this.suppliedParts = new ArrayList<>();
    }

    /**
     * Constructor with essential fields.
     *
     * @param id The supplier ID
     * @param name The supplier name
     * @param contactName The contact person name
     * @param phone The supplier phone number
     * @param email The supplier email
     */
    public Supplier(int id, String name, String contactName, String phone, String email) {
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.status = "ACTIVE";
        this.suppliedParts = new ArrayList<>();
    }

    /**
     * Full constructor with all fields.
     *
     * @param id The supplier ID
     * @param name The supplier name
     * @param contactName The contact person name
     * @param phone The supplier phone number
     * @param email The supplier email
     * @param address The supplier address
     * @param taxId The supplier tax ID
     * @param notes Additional notes about the supplier
     * @param status The supplier status
     */
    public Supplier(int id, String name, String contactName, String phone, String email,
                    String address, String taxId, String notes, String status) {
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.taxId = taxId;
        this.notes = notes;
        this.status = status;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.suppliedParts = new ArrayList<>();
    }

    /**
     * Gets the supplier ID.
     *
     * @return The supplier ID
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Sets the supplier ID.
     *
     * @param id The supplier ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the supplier name.
     *
     * @return The supplier name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the supplier name.
     *
     * @param name The supplier name
     */
    public void setName(String name) {
        this.name = name;
        this.updatedAt = new Date();
    }

    /**
     * Gets the contact person name.
     *
     * @return The contact person name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the contact person name.
     *
     * @param contactName The contact person name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
        this.updatedAt = new Date();
    }

    /**
     * Gets the supplier phone number.
     *
     * @return The supplier phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the supplier phone number.
     *
     * @param phone The supplier phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
        this.updatedAt = new Date();
    }

    /**
     * Gets the supplier email.
     *
     * @return The supplier email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the supplier email.
     *
     * @param email The supplier email
     */
    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = new Date();
    }

    /**
     * Gets the supplier address.
     *
     * @return The supplier address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the supplier address.
     *
     * @param address The supplier address
     */
    public void setAddress(String address) {
        this.address = address;
        this.updatedAt = new Date();
    }

    /**
     * Gets the supplier tax ID.
     *
     * @return The supplier tax ID
     */
    public String getTaxId() {
        return taxId;
    }

    /**
     * Sets the supplier tax ID.
     *
     * @param taxId The supplier tax ID
     */
    public void setTaxId(String taxId) {
        this.taxId = taxId;
        this.updatedAt = new Date();
    }

    /**
     * Gets additional notes about the supplier.
     *
     * @return Additional notes about the supplier
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets additional notes about the supplier.
     *
     * @param notes Additional notes about the supplier
     */
    public void setNotes(String notes) {
        this.notes = notes;
        this.updatedAt = new Date();
    }

    /**
     * Gets the supplier status.
     *
     * @return The supplier status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the supplier status.
     *
     * @param status The supplier status
     */
    public void setStatus(String status) {
        this.status = status;
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
     * Gets the list of parts supplied by this supplier.
     *
     * @return The list of parts supplied by this supplier
     */
    public List<Part> getSuppliedParts() {
        return suppliedParts;
    }

    /**
     * Sets the list of parts supplied by this supplier.
     *
     * @param suppliedParts The list of parts supplied by this supplier
     */
    public void setSuppliedParts(List<Part> suppliedParts) {
        this.suppliedParts = suppliedParts;
        this.updatedAt = new Date();
    }

    /**
     * Adds a part to the list of parts supplied by this supplier.
     *
     * @param part The part to add
     */
    public void addSuppliedPart(Part part) {
        if (part != null && !suppliedParts.contains(part)) {
            suppliedParts.add(part);
            part.setSupplier(this);
            this.updatedAt = new Date();
        }
    }

    /**
     * Removes a part from the list of parts supplied by this supplier.
     *
     * @param part The part to remove
     * @return True if the part was successfully removed, false otherwise
     */
    public boolean removeSuppliedPart(Part part) {
        if (part != null && suppliedParts.contains(part)) {
            suppliedParts.remove(part);
            part.setSupplier(null);
            this.updatedAt = new Date();
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the supplier.
     *
     * @return A string representation of the supplier
     */
    @Override
    public String toString() {
        return name + " (" + contactName + ")";
    }
}