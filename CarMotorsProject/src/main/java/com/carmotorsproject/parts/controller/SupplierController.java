/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;

import com.carmotorsproject.parts.model.SupplierDAO;
import com.carmotorsproject.parts.model.Supplier;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Supplier entities.
 * Handles business logic for suppliers.
 */
public class SupplierController {
    private static final Logger LOGGER = Logger.getLogger(SupplierController.class.getName());
    private final SupplierDAO supplierDAO;

    /**
     * Constructor.
     */
    public SupplierController() {
        this.supplierDAO = new SupplierDAO();
    }

    /**
     * Creates a new supplier.
     *
     * @param supplier The supplier to create
     * @return The created supplier with its generated ID, or null if the creation failed
     */
    public Supplier createSupplier(Supplier supplier) {
        LOGGER.log(Level.INFO, "Creating supplier: {0}", supplier.getName());
        return supplierDAO.insert(supplier);
    }

    /**
     * Updates an existing supplier.
     *
     * @param supplier The supplier to update
     * @return True if the update was successful, false otherwise
     */
    public boolean updateSupplier(Supplier supplier) {
        LOGGER.log(Level.INFO, "Updating supplier with ID: {0}", supplier.getId());
        return supplierDAO.update(supplier);
    }

    /**
     * Deletes a supplier.
     *
     * @param id The ID of the supplier to delete
     * @return True if the deletion was successful, false otherwise
     */
    public boolean deleteSupplier(int id) {
        LOGGER.log(Level.INFO, "Deleting supplier with ID: {0}", id);
        return supplierDAO.delete(id);
    }

    /**
     * Gets a supplier by its ID.
     *
     * @param id The ID of the supplier to get
     * @return The found supplier, or null if no supplier was found
     */
    public Supplier getSupplierById(int id) {
        LOGGER.log(Level.INFO, "Getting supplier with ID: {0}", id);
        return supplierDAO.findById(id);
    }

    /**
     * Gets all suppliers.
     *
     * @return A list of all suppliers
     */
    public List<Supplier> getAllSuppliers() {
        LOGGER.info("Getting all suppliers");
        return supplierDAO.findAll();
    }

    /**
     * Gets suppliers by name.
     *
     * @param name The name to search for
     * @return A list of suppliers with names containing the search term
     */
    public List<Supplier> getSuppliersByName(String name) {
        LOGGER.log(Level.INFO, "Getting suppliers by name: {0}", name);
        return supplierDAO.findByName(name);
    }

    /**
     * Gets active suppliers.
     *
     * @return A list of active suppliers
     */
    public List<Supplier> getActiveSuppliers() {
        LOGGER.info("Getting active suppliers");
        return supplierDAO.findActive();
    }

    /**
     * Activates a supplier.
     *
     * @param id The ID of the supplier to activate
     * @return True if the supplier was successfully activated, false otherwise
     */
    public boolean activateSupplier(int id) {
        LOGGER.log(Level.INFO, "Activating supplier with ID: {0}", id);

        Supplier supplier = supplierDAO.findById(id);

        if (supplier == null) {
            LOGGER.log(Level.WARNING, "Supplier not found with ID: {0}", id);
            return false;
        }

        supplier.setStatus("ACTIVE");
        return supplierDAO.update(supplier);
    }

    /**
     * Deactivates a supplier.
     *
     * @param id The ID of the supplier to deactivate
     * @return True if the supplier was successfully deactivated, false otherwise
     */
    public boolean deactivateSupplier(int id) {
        LOGGER.log(Level.INFO, "Deactivating supplier with ID: {0}", id);

        Supplier supplier = supplierDAO.findById(id);

        if (supplier == null) {
            LOGGER.log(Level.WARNING, "Supplier not found with ID: {0}", id);
            return false;
        }

        supplier.setStatus("INACTIVE");
        return supplierDAO.update(supplier);
    }

    /**
     * Updates the contact information of a supplier.
     *
     * @param id The ID of the supplier
     * @param contactName The new contact name
     * @param phone The new phone number
     * @param email The new email address
     * @return True if the contact information was successfully updated, false otherwise
     */
    public boolean updateContactInfo(int id, String contactName, String phone, String email) {
        LOGGER.log(Level.INFO, "Updating contact info for supplier with ID: {0}", id);

        Supplier supplier = supplierDAO.findById(id);

        if (supplier == null) {
            LOGGER.log(Level.WARNING, "Supplier not found with ID: {0}", id);
            return false;
        }

        supplier.setContactName(contactName);
        supplier.setPhone(phone);
        supplier.setEmail(email);

        return supplierDAO.update(supplier);
    }

    /**
     * Updates the address of a supplier.
     *
     * @param id The ID of the supplier
     * @param address The new address
     * @return True if the address was successfully updated, false otherwise
     */
    public boolean updateAddress(int id, String address) {
        LOGGER.log(Level.INFO, "Updating address for supplier with ID: {0}", id);

        Supplier supplier = supplierDAO.findById(id);

        if (supplier == null) {
            LOGGER.log(Level.WARNING, "Supplier not found with ID: {0}", id);
            return false;
        }

        supplier.setAddress(address);

        return supplierDAO.update(supplier);
    }
}