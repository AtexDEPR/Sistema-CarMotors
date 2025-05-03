/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;

import com.carmotorsproject.parts.model.PartDAO;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.Supplier;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Part entities.
 * Handles business logic for parts.
 */
public class PartController {
    private static final Logger LOGGER = Logger.getLogger(PartController.class.getName());
    private final PartDAO partDAO;

    /**
     * Constructor.
     */
    public PartController() {
        this.partDAO = new PartDAO();
    }

    /**
     * Creates a new part.
     *
     * @param part The part to create
     * @return The created part with its generated ID, or null if the creation failed
     */
    public Part createPart(Part part) {
        LOGGER.log(Level.INFO, "Creating part: {0}", part.getName());
        return partDAO.insert(part);
    }

    /**
     * Updates an existing part.
     *
     * @param part The part to update
     * @return True if the update was successful, false otherwise
     */
    public boolean updatePart(Part part) {
        LOGGER.log(Level.INFO, "Updating part with ID: {0}", part.getId());
        return partDAO.update(part);
    }

    /**
     * Deletes a part.
     *
     * @param id The ID of the part to delete
     * @return True if the deletion was successful, false otherwise
     */
    public boolean deletePart(int id) {
        LOGGER.log(Level.INFO, "Deleting part with ID: {0}", id);
        return partDAO.delete(id);
    }

    /**
     * Gets a part by its ID.
     *
     * @param id The ID of the part to get
     * @return The found part, or null if no part was found
     */
    public Part getPartById(int id) {
        LOGGER.log(Level.INFO, "Getting part with ID: {0}", id);
        return partDAO.findById(id);
    }

    /**
     * Gets a part by its reference code.
     *
     * @param reference The reference code of the part to get
     * @return The found part, or null if no part was found
     */
    public Part getPartByReference(String reference) {
        LOGGER.log(Level.INFO, "Getting part with reference: {0}", reference);
        return partDAO.findByReference(reference);
    }

    /**
     * Gets all parts.
     *
     * @return A list of all parts
     */
    public List<Part> getAllParts() {
        LOGGER.info("Getting all parts");
        return partDAO.findAll();
    }

    /**
     * Gets parts by supplier.
     *
     * @param supplier The supplier
     * @return A list of parts supplied by the specified supplier
     */
    public List<Part> getPartsBySupplier(Supplier supplier) {
        LOGGER.log(Level.INFO, "Getting parts by supplier ID: {0}", supplier.getId());
        return partDAO.findBySupplier(supplier.getId());
    }

    /**
     * Gets parts by type.
     *
     * @param type The type of parts to get
     * @return A list of parts of the specified type
     */
    public List<Part> getPartsByType(String type) {
        LOGGER.log(Level.INFO, "Getting parts by type: {0}", type);
        return partDAO.findByType(type);
    }

    /**
     * Gets parts that are below their minimum stock level.
     *
     * @return A list of parts that are below their minimum stock level
     */
    public List<Part> getPartsBelowMinimumStock() {
        LOGGER.info("Getting parts below minimum stock");
        return partDAO.findBelowMinimumStock();
    }

    /**
     * Adds stock to a part.
     *
     * @param partId The ID of the part
     * @param quantity The quantity to add
     * @return True if the stock was successfully added, false otherwise
     */
    public boolean addStock(int partId, int quantity) {
        LOGGER.log(Level.INFO, "Adding {0} units to part with ID: {1}", new Object[]{quantity, partId});

        if (quantity <= 0) {
            LOGGER.log(Level.WARNING, "Cannot add non-positive quantity: {0}", quantity);
            return false;
        }

        Part part = partDAO.findById(partId);

        if (part == null) {
            LOGGER.log(Level.WARNING, "Part not found with ID: {0}", partId);
            return false;
        }

        part.addStock(quantity);
        return partDAO.update(part);
    }

    /**
     * Removes stock from a part.
     *
     * @param partId The ID of the part
     * @param quantity The quantity to remove
     * @return True if the stock was successfully removed, false otherwise
     */
    public boolean removeStock(int partId, int quantity) {
        LOGGER.log(Level.INFO, "Removing {0} units from part with ID: {1}", new Object[]{quantity, partId});

        if (quantity <= 0) {
            LOGGER.log(Level.WARNING, "Cannot remove non-positive quantity: {0}", quantity);
            return false;
        }

        Part part = partDAO.findById(partId);

        if (part == null) {
            LOGGER.log(Level.WARNING, "Part not found with ID: {0}", partId);
            return false;
        }

        if (part.getQuantityInStock() < quantity) {
            LOGGER.log(Level.WARNING, "Not enough stock for part with ID: {0}", partId);
            return false;
        }

        part.removeStock(quantity);
        return partDAO.update(part);
    }

    /**
     * Updates the price of a part.
     *
     * @param partId The ID of the part
     * @param newPrice The new price
     * @return True if the price was successfully updated, false otherwise
     */
    public boolean updatePrice(int partId, double newPrice) {
        LOGGER.log(Level.INFO, "Updating price to {0} for part with ID: {1}", new Object[]{newPrice, partId});

        if (newPrice < 0) {
            LOGGER.log(Level.WARNING, "Cannot set negative price: {0}", newPrice);
            return false;
        }

        Part part = partDAO.findById(partId);

        if (part == null) {
            LOGGER.log(Level.WARNING, "Part not found with ID: {0}", partId);
            return false;
        }

        part.setPrice(newPrice);
        return partDAO.update(part);
    }

    /**
     * Updates the minimum stock level of a part.
     *
     * @param partId The ID of the part
     * @param minimumStock The new minimum stock level
     * @return True if the minimum stock level was successfully updated, false otherwise
     */
    public boolean updateMinimumStock(int partId, int minimumStock) {
        LOGGER.log(Level.INFO, "Updating minimum stock to {0} for part with ID: {1}", new Object[]{minimumStock, partId});

        if (minimumStock < 0) {
            LOGGER.log(Level.WARNING, "Cannot set negative minimum stock: {0}", minimumStock);
            return false;
        }

        Part part = partDAO.findById(partId);

        if (part == null) {
            LOGGER.log(Level.WARNING, "Part not found with ID: {0}", partId);
            return false;
        }

        part.setMinimumStock(minimumStock);
        return partDAO.update(part);
    }
}