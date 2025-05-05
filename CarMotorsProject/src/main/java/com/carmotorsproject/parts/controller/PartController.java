/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;

import com.carmotorsproject.parts.model.PartDAO;
import com.carmotorsproject.parts.model.DAOFactory;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PartType;
import com.carmotorsproject.parts.views.PartView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class that mediates between PartView and PartDAO.
 * Handles user actions from the view and updates the model and view accordingly.
 */
public class PartController {

    private static final Logger LOGGER = Logger.getLogger(PartController.class.getName());

    private final PartView view;
    private final PartDAO dao;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The part view
     */
    public PartController(PartView view) {
        this.view = view;
        this.dao = DAOFactory.getPartDAO();
        LOGGER.log(Level.INFO, "PartController initialized");
    }

    /**
     * Loads all parts from the database and updates the view.
     */
    public void loadAllParts() {
        List<Part> parts = dao.findAll();
        view.updateTable(parts);
        LOGGER.log(Level.INFO, "Loaded {0} parts", parts.size());
    }

    /**
     * Loads a part's description by ID.
     *
     * @param partId The ID of the part
     */
    public void loadPartDescription(int partId) {
        Part part = dao.findById(partId);
        if (part != null) {
            view.setDescription(part.getDescription());
        }
    }

    /**
     * Adds a new part to the database.
     */
    public void addPart() {
        Part part = view.getPartFromForm();
        if (part == null) {
            return; // Form validation failed
        }

        try {
            // Validate part data
            if (!validatePart(part)) {
                return;
            }

            // Save part to database
            Part savedPart = dao.save(part);

            // Update view
            view.clearForm();
            loadAllParts();
            view.showInfo("Part added successfully: " + savedPart.getName());
            LOGGER.log(Level.INFO, "Part added: {0}", savedPart.getName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding part", e);
            view.showError("Error adding part: " + e.getMessage());
        }
    }

    /**
     * Updates an existing part in the database.
     */
    public void updatePart() {
        Part part = view.getPartFromForm();
        if (part == null) {
            return; // Form validation failed
        }

        // Check if a part is selected
        if (part.getPartId() == 0) {
            view.showError("Please select a part to update.");
            return;
        }

        try {
            // Validate part data
            if (!validatePart(part)) {
                return;
            }

            // Update part in database
            Part updatedPart = dao.update(part);

            // Update view
            view.clearForm();
            loadAllParts();
            view.showInfo("Part updated successfully: " + updatedPart.getName());
            LOGGER.log(Level.INFO, "Part updated: {0}", updatedPart.getName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating part", e);
            view.showError("Error updating part: " + e.getMessage());
        }
    }

    /**
     * Deletes a part from the database.
     */
    public void deletePart() {
        Part part = view.getPartFromForm();
        if (part == null || part.getPartId() == 0) {
            view.showError("Please select a part to delete.");
            return;
        }

        // Confirm deletion
        if (!view.showConfirm("Are you sure you want to delete this part: " + part.getName() + "?")) {
            return;
        }

        // Delete part from database
        boolean deleted = dao.delete(part.getPartId());

        if (deleted) {
            // Update view
            view.clearForm();
            loadAllParts();
            view.showInfo("Part deleted successfully.");
            LOGGER.log(Level.INFO, "Part deleted: ID {0}", part.getPartId());
        } else {
            view.showError("Part could not be deleted. It may be referenced by other records.");
        }
    }

    /**
     * Searches for parts based on search criteria.
     *
     * @param searchText The search text
     * @param searchType The type of search (Name, Part Number, Type, Supplier ID)
     */
    public void searchParts(String searchText, String searchType) {
        if (searchText.isEmpty()) {
            view.showError("Please enter search text.");
            return;
        }

        try {
            List<Part> results = new ArrayList<>();

            switch (searchType) {
                case "Name":
                    results = dao.findByName(searchText);
                    break;
                case "Part Number":
                    // Find by exact part number
                    Part part = dao.findById(0); // Placeholder to avoid null
                    for (Part p : dao.findAll()) {
                        if (p.getPartNumber().equalsIgnoreCase(searchText)) {
                            part = p;
                            results.add(p);
                            break;
                        }
                    }
                    break;
                case "Type":
                    try {
                        PartType type = PartType.valueOf(searchText.toUpperCase());
                        results = dao.findByType(type);
                    } catch (IllegalArgumentException e) {
                        view.showError("Invalid part type. Valid types are: " +
                                String.join(", ", getPartTypeNames()));
                        return;
                    }
                    break;
                case "Supplier ID":
                    try {
                        int supplierId = Integer.parseInt(searchText);
                        results = dao.findBySupplier(supplierId);
                    } catch (NumberFormatException e) {
                        view.showError("Please enter a valid supplier ID (number).");
                        return;
                    }
                    break;
                default:
                    view.showError("Invalid search type.");
                    return;
            }

            // Update view with search results
            view.updateTable(results);
            LOGGER.log(Level.INFO, "Search completed. Found {0} results.", results.size());

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching parts", e);
            view.showError("Error searching parts: " + e.getMessage());
        }
    }

    /**
     * Finds parts with stock levels below their minimum stock level.
     */
    public void findLowStock() {
        List<Part> lowStockParts = new ArrayList<>();

        // Get all parts and filter those with stock below minimum
        for (Part part : dao.findAll()) {
            if (part.getStockQuantity() < part.getMinStockLevel()) {
                lowStockParts.add(part);
            }
        }

        // Update view with low stock parts
        view.updateTable(lowStockParts);
        LOGGER.log(Level.INFO, "Found {0} parts with low stock", lowStockParts.size());

    }

    /**
     * Validates part data.
     *
     * @param part The part to validate
     * @return true if the part is valid, false otherwise
     */
    private boolean validatePart(Part part) {
        // Validate name
        if (part.getName() == null || part.getName().trim().isEmpty()) {
            view.showError("Part name is required.");
            return false;
        }

        // Validate part number
        if (part.getPartNumber() == null || part.getPartNumber().trim().isEmpty()) {
            view.showError("Part number is required.");
            return false;
        }

        // Validate prices
        if (part.getPurchasePrice() < 0) {
            view.showError("Purchase price cannot be negative.");
            return false;
        }

        if (part.getSellingPrice() < 0) {
            view.showError("Selling price cannot be negative.");
            return false;
        }

        // Validate stock quantity
        if (part.getStockQuantity() < 0) {
            view.showError("Stock quantity cannot be negative.");
            return false;
        }

        // Validate min stock level
        if (part.getMinStockLevel() < 0) {
            view.showError("Minimum stock level cannot be negative.");
            return false;
        }

        return true;
    }

    /**
     * Gets the names of all part types.
     *
     * @return An array of part type names
     */
    private String[] getPartTypeNames() {
        PartType[] types = PartType.values();
        String[] names = new String[types.length];

        for (int i = 0; i < types.length; i++) {
            names[i] = types[i].name();
        }

        return names;
    }
}