/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;

import com.carmotorsproject.parts.model.SupplierEvaluationDAO;
import com.carmotorsproject.parts.model.SupplierDAO;
import com.carmotorsproject.parts.model.DAOFactory;
import com.carmotorsproject.parts.model.SupplierEvaluation;
import com.carmotorsproject.parts.model.Supplier;
import com.carmotorsproject.parts.views.SupplierEvaluationView;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class that mediates between SupplierEvaluationView and SupplierEvaluationDAO.
 * Handles user actions from the view and updates the model and view accordingly.
 */
public class SupplierEvaluationController {

    private static final Logger LOGGER = Logger.getLogger(SupplierEvaluationController.class.getName());

    private final SupplierEvaluationView view;
    private final SupplierEvaluationDAO evaluationDAO;
    private final SupplierDAO supplierDAO;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The supplier evaluation view
     */
    public SupplierEvaluationController(SupplierEvaluationView view) {
        this.view = view;
        this.evaluationDAO = DAOFactory.getSupplierEvaluationDAO();
        this.supplierDAO = DAOFactory.getSupplierDAO();
        LOGGER.log(Level.INFO, "SupplierEvaluationController initialized");
    }

    /**
     * Loads all supplier evaluations from the database and updates the view.
     */
    public void loadAllEvaluations() {
        try {
            List<SupplierEvaluation> evaluations = evaluationDAO.findAll();
            view.updateTable(evaluations);
            LOGGER.log(Level.INFO, "Loaded {0} supplier evaluations", evaluations.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading supplier evaluations", e);
            view.showError("Error loading supplier evaluations: " + e.getMessage());
        }
    }

    /**
     * Loads evaluations for a specific supplier.
     *
     * @param supplierId The ID of the supplier
     */
    public void loadEvaluationsBySupplier(int supplierId) {
        try {
            List<SupplierEvaluation> evaluations = evaluationDAO.findBySupplier(supplierId);
            view.updateTable(evaluations);
            LOGGER.log(Level.INFO, "Loaded {0} evaluations for supplier ID {1}",
                    new Object[]{evaluations.size(), supplierId});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading evaluations for supplier", e);
            view.showError("Error loading evaluations: " + e.getMessage());
        }
    }

    /**
     * Adds a new supplier evaluation to the database.
     *
     * @param supplierId The ID of the supplier
     * @param evaluationDate The date of the evaluation
     * @param deliveryRating Rating for delivery performance (1-5)
     * @param qualityRating Rating for product quality (1-5)
     * @param priceRating Rating for price competitiveness (1-5)
     * @param communicationRating Rating for communication effectiveness (1-5)
     * @param comments Additional comments about the evaluation
     */
    public void addEvaluation(int supplierId, Date evaluationDate, int deliveryRating,
                              int qualityRating, int priceRating, int communicationRating,
                              String comments) {
        try {
            // Validate supplier
            Supplier supplier = supplierDAO.findById(supplierId);
            if (supplier == null) {
                view.showError("Invalid supplier ID.");
                return;
            }

            // Validate date
            if (evaluationDate == null) {
                evaluationDate = new Date(); // Use current date if not provided
            }

            // Create evaluation
            SupplierEvaluation evaluation = new SupplierEvaluation(
                    supplierId, evaluationDate, deliveryRating, qualityRating,
                    priceRating, communicationRating, comments
            );

            // Save evaluation to database
            SupplierEvaluation savedEvaluation = evaluationDAO.save(evaluation);

            // Update view
            view.clearForm();
            loadEvaluationsBySupplier(supplierId);
            view.showInfo("Supplier evaluation added successfully.");
            LOGGER.log(Level.INFO, "Supplier evaluation added for supplier ID {0}", supplierId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding supplier evaluation", e);
            view.showError("Error adding supplier evaluation: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Invalid rating value", e);
            view.showError("Invalid rating value: " + e.getMessage());
        }
    }

    /**
     * Updates an existing supplier evaluation in the database.
     *
     * @param evaluationId The ID of the evaluation to update
     * @param supplierId The ID of the supplier
     * @param evaluationDate The date of the evaluation
     * @param deliveryRating Rating for delivery performance (1-5)
     * @param qualityRating Rating for product quality (1-5)
     * @param priceRating Rating for price competitiveness (1-5)
     * @param communicationRating Rating for communication effectiveness (1-5)
     * @param comments Additional comments about the evaluation
     */
    public void updateEvaluation(int evaluationId, int supplierId, Date evaluationDate,
                                 int deliveryRating, int qualityRating, int priceRating,
                                 int communicationRating, String comments) {
        try {
            // Validate evaluation ID
            SupplierEvaluation existingEvaluation = evaluationDAO.findById(evaluationId);
            if (existingEvaluation == null) {
                view.showError("Invalid evaluation ID.");
                return;
            }

            // Validate supplier
            Supplier supplier = supplierDAO.findById(supplierId);
            if (supplier == null) {
                view.showError("Invalid supplier ID.");
                return;
            }

            // Validate date
            if (evaluationDate == null) {
                evaluationDate = new Date(); // Use current date if not provided
            }

            // Create evaluation
            SupplierEvaluation evaluation = new SupplierEvaluation(
                    supplierId, evaluationDate, deliveryRating, qualityRating,
                    priceRating, communicationRating, comments
            );
            evaluation.setEvaluationId(evaluationId);

            // Update evaluation in database
            SupplierEvaluation updatedEvaluation = evaluationDAO.update(evaluation);

            // Update view
            view.clearForm();
            loadEvaluationsBySupplier(supplierId);
            view.showInfo("Supplier evaluation updated successfully.");
            LOGGER.log(Level.INFO, "Supplier evaluation updated: ID {0}", evaluationId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating supplier evaluation", e);
            view.showError("Error updating supplier evaluation: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Invalid rating value", e);
            view.showError("Invalid rating value: " + e.getMessage());
        }
    }

    /**
     * Deletes a supplier evaluation from the database.
     *
     * @param evaluationId The ID of the evaluation to delete
     */
    public void deleteEvaluation(int evaluationId) {
        try {
            // Validate evaluation ID
            SupplierEvaluation existingEvaluation = evaluationDAO.findById(evaluationId);
            if (existingEvaluation == null) {
                view.showError("Invalid evaluation ID.");
                return;
            }

            // Confirm deletion
            if (!view.showConfirm("Are you sure you want to delete this supplier evaluation?")) {
                return;
            }

            // Delete evaluation from database
            boolean deleted = evaluationDAO.delete(evaluationId);

            if (deleted) {
                // Update view
                view.clearForm();
                loadAllEvaluations();
                view.showInfo("Supplier evaluation deleted successfully.");
                LOGGER.log(Level.INFO, "Supplier evaluation deleted: ID {0}", evaluationId);
            } else {
                view.showError("Supplier evaluation could not be deleted.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting supplier evaluation", e);
            view.showError("Error deleting supplier evaluation: " + e.getMessage());
        }
    }

    /**
     * Loads all suppliers for the supplier combo box.
     *
     * @return A list of all suppliers
     */
    public List<Supplier> loadSuppliers() {
        try {
            return supplierDAO.findAll();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading suppliers", e);
            view.showError("Error loading suppliers: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Gets the average rating for a supplier.
     *
     * @param supplierId The ID of the supplier
     * @return The average rating, or 0 if no evaluations exist
     */
    public double getAverageRating(int supplierId) {
        try {
            // Call the getAverageRating method from SupplierDAO
            double averageRating = supplierDAO.getAverageRating(supplierId);
            LOGGER.log(Level.INFO, "Retrieved average rating for supplier ID {0}: {1}",
                    new Object[]{supplierId, averageRating});
            return averageRating;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting average rating for supplier ID " + supplierId, e);
            return 0.0;
        }
    }

    /**
     * Validates that a rating is between 1 and 5.
     *
     * @param rating The rating to validate
     * @param ratingName The name of the rating for error message
     * @throws IllegalArgumentException If the rating is not between 1 and 5
     */
    public void validateRating(int rating, String ratingName) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(ratingName + " must be between 1 and 5");
        }
    }
}