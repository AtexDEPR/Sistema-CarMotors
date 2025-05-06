package com.carmotorsproject.campaigns.controller;

import com.carmotorsproject.campaigns.model.Inspection;
import com.carmotorsproject.campaigns.model.InspectionResult;
import com.carmotorsproject.campaigns.model.InspectionDAO;
import com.carmotorsproject.campaigns.views.InspectionView;
import com.carmotorsproject.campaigns.model.DAOFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for managing vehicle inspections
 */
public class InspectionController {

    private static final Logger LOGGER = Logger.getLogger(InspectionController.class.getName());

    private InspectionDAO inspectionDAO;
    private InspectionView inspectionView;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The inspection view
     */
    public InspectionController(InspectionView view) {
        this.inspectionView = view;
        this.inspectionDAO = DAOFactory.getInspectionDAO();

        // Initialize view with controller reference
        this.inspectionView.setController(this);

        // Load initial data
        loadAllInspections();

        LOGGER.log(Level.INFO, "InspectionController initialized");
    }

    /**
     * Load all inspections and update the view
     */
    public void loadAllInspections() {
        try {
            List<Inspection> inspections = inspectionDAO.findAll();
            inspectionView.updateInspectionTable(inspections);
            LOGGER.log(Level.INFO, "Loaded {0} inspections", inspections.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading inspections", ex);
            inspectionView.showError("Error loading inspections: " + ex.getMessage());
        }
    }

    /**
     * Load inspections for a specific vehicle
     *
     * @param vehicleId Vehicle ID
     */
    public void loadInspectionsByVehicle(int vehicleId) {
        try {
            List<Inspection> inspections = inspectionDAO.findByVehicle(vehicleId);
            inspectionView.updateInspectionTable(inspections);
            LOGGER.log(Level.INFO, "Loaded {0} inspections for vehicle ID: {1}",
                    new Object[]{inspections.size(), vehicleId});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading inspections by vehicle", ex);
            inspectionView.showError("Error loading inspections: " + ex.getMessage());
        }
    }

    /**
     * Create a new inspection
     *
     * @param inspection The inspection to create
     * @return True if inspection was created successfully
     */
    public boolean createInspection(Inspection inspection) {
        try {
            // Validate input
            Map<String, String> validationErrors = validateInspection(inspection);

            if (!validationErrors.isEmpty()) {
                inspectionView.showValidationErrors(validationErrors);
                return false;
            }

            // Set current date if not provided
            if (inspection.getInspectionDate() == null) {
                inspection.setInspectionDate(new Date());
            }

            // Save inspection
            Inspection savedInspection = inspectionDAO.save(inspection);

            // Refresh inspection list
            loadAllInspections();

            inspectionView.showSuccess("Inspection saved successfully with ID: " + savedInspection.getInspectionId());
            LOGGER.log(Level.INFO, "Created inspection with ID: {0}", savedInspection.getInspectionId());

            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error creating inspection", ex);
            inspectionView.showError("Error creating inspection: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Update an existing inspection
     *
     * @param inspection The inspection to update
     * @return True if inspection was updated successfully
     */
    public boolean updateInspection(Inspection inspection) {
        try {
            // Validate input
            Map<String, String> validationErrors = validateInspection(inspection);

            if (!validationErrors.isEmpty()) {
                inspectionView.showValidationErrors(validationErrors);
                return false;
            }

            // Update inspection
            Inspection updatedInspection = inspectionDAO.update(inspection);

            // Refresh inspection list
            loadAllInspections();

            inspectionView.showSuccess("Inspection updated successfully with ID: " + updatedInspection.getInspectionId());
            LOGGER.log(Level.INFO, "Updated inspection with ID: {0}", updatedInspection.getInspectionId());

            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating inspection", ex);
            inspectionView.showError("Error updating inspection: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Delete an inspection
     *
     * @param inspectionId The ID of the inspection to delete
     * @return True if inspection was deleted successfully
     */
    public boolean deleteInspection(int inspectionId) {
        try {
            boolean deleted = inspectionDAO.delete(inspectionId);

            if (deleted) {
                // Refresh inspection list
                loadAllInspections();

                inspectionView.showSuccess("Inspection deleted successfully with ID: " + inspectionId);
                LOGGER.log(Level.INFO, "Deleted inspection with ID: {0}", inspectionId);
            } else {
                inspectionView.showError("Inspection not found with ID: " + inspectionId);
                LOGGER.log(Level.WARNING, "Inspection not found with ID: {0}", inspectionId);
            }

            return deleted;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting inspection", ex);
            inspectionView.showError("Error deleting inspection: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Get inspection details
     *
     * @param inspectionId Inspection ID
     */
    public void getInspectionDetails(int inspectionId) {
        try {
            Inspection inspection = inspectionDAO.findById(inspectionId);

            if (inspection == null) {
                inspectionView.showError("Inspection not found with ID: " + inspectionId);
                LOGGER.log(Level.WARNING, "Inspection not found with ID: {0}", inspectionId);
                return;
            }

            inspectionView.populateInspectionForm(inspection);
            LOGGER.log(Level.INFO, "Loaded details for inspection ID: {0}", inspectionId);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading inspection details", ex);
            inspectionView.showError("Error loading inspection details: " + ex.getMessage());
        }
    }

    /**
     * Filter inspections by date range
     *
     * @param startDate Start date
     * @param endDate End date
     */
    public void filterInspectionsByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            inspectionView.showError("Both dates are required to filter by range");
            return;
        }

        try {
            List<Inspection> inspections = inspectionDAO.findByDateRange(startDate, endDate);
            inspectionView.updateInspectionTable(inspections);
            LOGGER.log(Level.INFO, "Found {0} inspections in date range: {1} to {2}",
                    new Object[]{inspections.size(), startDate, endDate});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error filtering inspections by date range", ex);
            inspectionView.showError("Error filtering inspections: " + ex.getMessage());
        }
    }

    /**
     * Filter inspections by result
     *
     * @param result Inspection result
     */
    public void filterInspectionsByResult(InspectionResult result) {
        if (result == null) {
            loadAllInspections();
            return;
        }

        try {
            List<Inspection> inspections = inspectionDAO.findByResult(result);
            inspectionView.updateInspectionTable(inspections);
            LOGGER.log(Level.INFO, "Found {0} inspections with result: {1}",
                    new Object[]{inspections.size(), result});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error filtering inspections by result", ex);
            inspectionView.showError("Error filtering inspections: " + ex.getMessage());
        }
    }

    /**
     * Update inspection result
     *
     * @param inspectionId Inspection ID
     * @param result New result
     * @param notes Additional notes
     * @return True if result was updated successfully
     */
    public boolean updateInspectionResult(int inspectionId, InspectionResult result, String notes) {
        try {
            boolean updated = inspectionDAO.updateResult(inspectionId, result, notes);

            if (updated) {
                loadAllInspections();
                inspectionView.showSuccess("Inspection result updated successfully");
                LOGGER.log(Level.INFO, "Updated result of inspection ID: {0} to {1}",
                        new Object[]{inspectionId, result});
            } else {
                inspectionView.showError("Inspection not found with ID: " + inspectionId);
                LOGGER.log(Level.WARNING, "Inspection not found with ID: {0}", inspectionId);
            }

            return updated;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating inspection result", ex);
            inspectionView.showError("Error updating inspection result: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Get all possible inspection results
     *
     * @return Array of inspection results
     */
    public InspectionResult[] getAllInspectionResults() {
        return InspectionResult.values();
    }

    /**
     * Validate inspection input
     *
     * @param inspection The inspection to validate
     * @return Map of validation errors
     */
    private Map<String, String> validateInspection(Inspection inspection) {
        Map<String, String> errors = new HashMap<>();

        // Validate vehicle ID
        if (inspection.getVehicleId() <= 0) {
            errors.put("vehicleId", "Vehicle is required");
        }

        // Validate technician ID
        if (inspection.getTechnicianId() <= 0) {
            errors.put("technicianId", "Technician is required");
        }

        return errors;
    }

    /**
     * Get inspection statistics
     *
     * @return Map with inspection statistics
     */
    public Map<String, Object> getInspectionStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        try {
            // Get total inspections
            List<Inspection> allInspections = inspectionDAO.findAll();
            statistics.put("totalInspections", allInspections.size());

            // Get inspections by result
            Map<InspectionResult, Integer> resultCounts = new HashMap<>();
            for (InspectionResult result : InspectionResult.values()) {
                List<Inspection> inspections = inspectionDAO.findByResult(result);
                resultCounts.put(result, inspections.size());
            }
            statistics.put("inspectionsByResult", resultCounts);

            // Get recent inspections (last 30 days)
            Date endDate = new Date();
            Date startDate = new Date(endDate.getTime() - 30L * 24 * 60 * 60 * 1000);
            List<Inspection> recentInspections = inspectionDAO.findByDateRange(startDate, endDate);
            statistics.put("recentInspections", recentInspections.size());

            LOGGER.log(Level.INFO, "Generated inspection statistics");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error generating inspection statistics", ex);
            statistics.put("error", "Error generating statistics: " + ex.getMessage());
        }

        return statistics;
    }
}