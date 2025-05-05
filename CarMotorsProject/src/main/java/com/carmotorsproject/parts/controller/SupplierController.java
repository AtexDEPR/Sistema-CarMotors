/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;

import com.carmotorsproject.parts.model.SupplierDAO;
import com.carmotorsproject.parts.model.DAOFactory;
import com.carmotorsproject.parts.model.Supplier;
import com.carmotorsproject.parts.views.SupplierView;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class that mediates between SupplierView and SupplierDAO.
 * Handles user actions from the view and updates the model and view accordingly.
 */
public class SupplierController {

    private static final Logger LOGGER = Logger.getLogger(SupplierController.class.getName());

    private final SupplierView view;
    private final SupplierDAO dao;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The supplier view
     */
    public SupplierController(SupplierView view) {
        this.view = view;
        this.dao = DAOFactory.getSupplierDAO();
        LOGGER.log(Level.INFO, "SupplierController initialized");
    }

    /**
     * Loads all suppliers from the database and updates the view.
     */
    public void loadAllSuppliers() {
        try {
            List<Supplier> suppliers = dao.findAll();
            view.updateTable(suppliers);
            LOGGER.log(Level.INFO, "Loaded {0} suppliers", suppliers.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading suppliers", e);
            view.showError("Error loading suppliers: " + e.getMessage());
        }
    }

    /**
     * Adds a new supplier to the database.
     *
     * @param supplier The supplier to add
     */
    public void addSupplier(Supplier supplier) {
        try {
            // Validate supplier data
            if (!validateSupplier(supplier)) {
                return;
            }

            // Check for unique taxId
            if (!isUniqueTaxId(supplier.getTaxId(), 0)) {
                view.showError("A supplier with this Tax ID already exists.");
                return;
            }

            // Save supplier to database
            Supplier savedSupplier = dao.insert(supplier);

            // Update view
            view.clearForm();
            loadAllSuppliers();
            view.showInfo("Supplier added successfully: " + savedSupplier.getName());
            LOGGER.log(Level.INFO, "Supplier added: {0}", savedSupplier.getName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding supplier", e);
            view.showError("Error adding supplier: " + e.getMessage());
        }
    }

    /**
     * Updates an existing supplier in the database.
     *
     * @param supplier The supplier to update
     */
    public void updateSupplier(Supplier supplier) {
        // Check if a supplier is selected
        if (supplier.getId() == 0) {
            view.showError("Please select a supplier to update.");
            return;
        }

        try {
            // Validate supplier data
            if (!validateSupplier(supplier)) {
                return;
            }

            // Check for unique taxId (excluding current supplier)
            if (!isUniqueTaxId(supplier.getTaxId(), supplier.getId())) {
                view.showError("A supplier with this Tax ID already exists.");
                return;
            }

            // Update supplier in database
            boolean updated = dao.update(supplier);

            if (!updated) {
                view.showError("Failed to update supplier. Supplier not found.");
                return;
            }

            // Update view
            view.clearForm();
            loadAllSuppliers();
            view.showInfo("Supplier updated successfully: " + supplier.getName());
            LOGGER.log(Level.INFO, "Supplier updated: {0}", supplier.getName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating supplier", e);
            view.showError("Error updating supplier: " + e.getMessage());
        }
    }

    /**
     * Deletes a supplier from the database.
     *
     * @param supplierId The ID of the supplier to delete
     */
    public void deleteSupplier(int supplierId) {
        try {
            // Check if supplier exists
            Supplier supplier = dao.findById(supplierId);
            if (supplier == null) {
                view.showError("Supplier not found.");
                return;
            }

            // Check if supplier has associated records
            if (hasAssociatedRecords(supplierId)) {
                view.showError("Cannot delete supplier. It has associated parts or purchase orders.");
                return;
            }

            // Delete supplier from database
            boolean deleted = dao.delete(supplierId);

            if (deleted) {
                // Update view
                view.clearForm();
                loadAllSuppliers();
                view.showInfo("Supplier deleted successfully.");
                LOGGER.log(Level.INFO, "Supplier deleted: ID {0}", supplierId);
            } else {
                view.showError("Supplier could not be deleted.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting supplier", e);
            view.showError("Error deleting supplier: " + e.getMessage());
        }
    }

    /**
     * Searches for suppliers based on search criteria.
     *
     * @param searchText The search text
     * @param searchType The type of search (Name, Tax ID, Category, Email)
     */
    public void searchSuppliers(String searchText, String searchType) {
        if (searchText.isEmpty()) {
            view.showError("Please enter search text.");
            return;
        }

        try {
            List<Supplier> results;

            // Default to searching by name if searchType is empty
            if (searchType == null || searchType.isEmpty()) {
                searchType = "Name";
            }

            switch (searchType) {
                case "Name":
                    results = dao.findByName(searchText);
                    break;
                case "Tax ID":
                    Supplier supplier = dao.findByTaxId(searchText);
                    results = supplier != null ? List.of(supplier) : List.of();
                    break;
                default:
                    view.showError("Invalid search type. Searching by name instead.");
                    results = dao.findByName(searchText);
                    break;
            }

            // Update view with search results
            view.updateTable(results);
            LOGGER.log(Level.INFO, "Search completed. Found {0} results.", results.size());

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching suppliers", e);
            view.showError("Error searching suppliers: " + e.getMessage());
        }
    }

    /**
     * Checks if a tax ID is unique (excluding a specific supplier).
     *
     * @param taxId The tax ID to check
     * @param excludeSupplierId The ID of the supplier to exclude (0 for new suppliers)
     * @return true if the tax ID is unique, false otherwise
     * @throws SQLException If a database access error occurs
     */
    private boolean isUniqueTaxId(String taxId, int excludeSupplierId) throws SQLException {
        Supplier existingSupplier = dao.findByTaxId(taxId);
        return existingSupplier == null || existingSupplier.getId() == excludeSupplierId;
    }

    /**
     * Checks if a supplier has associated records (parts or purchase orders).
     *
     * @param supplierId The ID of the supplier
     * @return true if the supplier has associated records, false otherwise
     * @throws SQLException If a database access error occurs
     */
    private boolean hasAssociatedRecords(int supplierId) throws SQLException {
        // Check for associated parts
        if (!DAOFactory.getPartDAO().findBySupplier(supplierId).isEmpty()) {
            return true;
        }

        // Check for associated purchase orders
        if (!DAOFactory.getPurchaseOrderDAO().findBySupplier(supplierId).isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * Validates supplier data.
     *
     * @param supplier The supplier to validate
     * @return true if the supplier is valid, false otherwise
     */
    private boolean validateSupplier(Supplier supplier) {
        // Validate name
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            view.showError("Supplier name is required.");
            return false;
        }

        // Validate tax ID
        if (supplier.getTaxId() == null || supplier.getTaxId().trim().isEmpty()) {
            view.showError("Tax ID is required.");
            return false;
        }

        // Validate email format
        if (supplier.getEmail() != null && !supplier.getEmail().isEmpty() &&
                !supplier.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            view.showError("Invalid email format.");
            return false;
        }

        // Validate phone format (optional)
        if (supplier.getPhone() != null && !supplier.getPhone().isEmpty() &&
                !supplier.getPhone().matches("^[0-9\\-\\+\\s()]+$")) {
            view.showError("Invalid phone format.");
            return false;
        }

        return true;
    }
}