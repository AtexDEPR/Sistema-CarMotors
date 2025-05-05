/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.controller;

import com.carmotorsproject.services.model.DAOFactory;
import com.carmotorsproject.services.model.Technician;
import com.carmotorsproject.services.model.TechnicianDAO;
import com.carmotorsproject.services.model.TechnicianSpecialty;
import com.carmotorsproject.services.model.TechnicianStatus;
import com.carmotorsproject.services.views.TechnicianView;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for technician-related operations.
 * Mediates between the TechnicianView and the TechnicianDAO.
 */
public class TechnicianController {

    private static final Logger LOGGER = Logger.getLogger(TechnicianController.class.getName());

    // View
    private final TechnicianView view;

    // DAO
    private final TechnicianDAO technicianDAO;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The technician view
     */
    public TechnicianController(TechnicianView view) {
        this.view = view;
        this.technicianDAO = DAOFactory.getTechnicianDAO();
    }

    /**
     * Loads all technicians and updates the view.
     */
    public void loadTechnicians() {
        try {
            List<Technician> technicians = technicianDAO.findAll();
            view.updateTechnicianTable(technicians);
            LOGGER.log(Level.INFO, "Loaded {0} technicians", technicians.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading technicians", ex);
            showError("Error loading technicians: " + ex.getMessage());
        }
    }

    /**
     * Loads the details of a specific technician and updates the view.
     *
     * @param technicianId The ID of the technician to load
     */
    public void loadTechnicianDetails(int technicianId) {
        try {
            Technician technician = technicianDAO.findById(technicianId);
            if (technician != null) {
                view.populateTechnicianForm(technician);
                LOGGER.log(Level.INFO, "Loaded details for technician ID: {0}", technicianId);
            } else {
                LOGGER.log(Level.WARNING, "Technician not found with ID: {0}", technicianId);
                showError("Technician not found with ID: " + technicianId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading technician details", ex);
            showError("Error loading technician details: " + ex.getMessage());
        }
    }

    /**
     * Adds a new technician.
     *
     * @param technician The technician to add
     */
    public void addTechnician(Technician technician) {
        try {
            // Validate the technician
            validateTechnician(technician);

            Technician savedTechnician = technicianDAO.save(technician);
            loadTechnicians();
            LOGGER.log(Level.INFO, "Added technician with ID: {0}", savedTechnician.getTechnicianId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding technician", ex);
            showError("Error adding technician: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, "Invalid technician data", ex);
            showError(ex.getMessage());
        }
    }

    /**
     * Updates an existing technician.
     *
     * @param technician The technician to update
     */
    public void updateTechnician(Technician technician) {
        try {
            // Validate the technician
            validateTechnician(technician);

            Technician updatedTechnician = technicianDAO.update(technician);
            loadTechnicians();
            LOGGER.log(Level.INFO, "Updated technician with ID: {0}", updatedTechnician.getTechnicianId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating technician", ex);
            showError("Error updating technician: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, "Invalid technician data", ex);
            showError(ex.getMessage());
        }
    }

    /**
     * Deletes a technician.
     *
     * @param technicianId The ID of the technician to delete
     */
    public void deleteTechnician(int technicianId) {
        try {
            boolean deleted = technicianDAO.delete(technicianId);
            if (deleted) {
                loadTechnicians();
                LOGGER.log(Level.INFO, "Deleted technician with ID: {0}", technicianId);
            } else {
                LOGGER.log(Level.WARNING, "Technician not found with ID: {0}", technicianId);
                showError("Technician not found with ID: " + technicianId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting technician", ex);
            showError("Error deleting technician: " + ex.getMessage());
        }
    }

    /**
     * Searches for technicians by name.
     *
     * @param name The name to search for
     */
    public void searchTechniciansByName(String name) {
        try {
            List<Technician> technicians = technicianDAO.findByName(name);
            view.updateTechnicianTable(technicians);
            LOGGER.log(Level.INFO, "Found {0} technicians matching name '{1}'",
                    new Object[]{technicians.size(), name});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching technicians", ex);
            showError("Error searching technicians: " + ex.getMessage());
        }
    }

    /**
     * Searches for technicians by specialty.
     *
     * @param specialty The specialty to search for
     */
    public void searchTechniciansBySpecialty(TechnicianSpecialty specialty) {
        try {
            List<Technician> technicians = technicianDAO.findBySpecialty(specialty);
            view.updateTechnicianTable(technicians);
            LOGGER.log(Level.INFO, "Found {0} technicians with specialty '{1}'",
                    new Object[]{technicians.size(), specialty});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching technicians by specialty", ex);
            showError("Error searching technicians by specialty: " + ex.getMessage());
        }
    }

    /**
     * Searches for technicians by status.
     *
     * @param status The status to search for
     */
    public void searchTechniciansByStatus(TechnicianStatus status) {
        try {
            List<Technician> technicians = technicianDAO.findByStatus(status);
            view.updateTechnicianTable(technicians);
            LOGGER.log(Level.INFO, "Found {0} technicians with status '{1}'",
                    new Object[]{technicians.size(), status});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching technicians by status", ex);
            showError("Error searching technicians by status: " + ex.getMessage());
        }
    }

    /**
     * Updates the status of a technician.
     *
     * @param technicianId The ID of the technician
     * @param status The new status
     */
    public void updateTechnicianStatus(int technicianId, TechnicianStatus status) {
        try {
            boolean updated = technicianDAO.updateStatus(technicianId, status);
            if (updated) {
                loadTechnicians();
                LOGGER.log(Level.INFO, "Updated status of technician ID {0} to {1}",
                        new Object[]{technicianId, status});
            } else {
                LOGGER.log(Level.WARNING, "Technician not found with ID: {0}", technicianId);
                showError("Technician not found with ID: " + technicianId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating technician status", ex);
            showError("Error updating technician status: " + ex.getMessage());
        }
    }

    /**
     * Validates a technician.
     *
     * @param technician The technician to validate
     * @throws IllegalArgumentException If the technician is invalid
     */
    private void validateTechnician(Technician technician) throws IllegalArgumentException {
        // Validate name
        if (technician.getName() == null || technician.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }

        // Validate last name
        if (technician.getLastName() == null || technician.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required.");
        }

        // Validate specialty
        if (technician.getSpecialty() == null) {
            throw new IllegalArgumentException("Specialty is required.");
        }

        // Validate status
        if (technician.getStatus() == null || technician.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Status is required.");
        }

        // Validate hourly rate
        if (technician.getHourlyRate() <= 0) {
            throw new IllegalArgumentException("Hourly rate must be greater than zero.");
        }
    }

    /**
     * Shows an error message.
     *
     * @param message The error message to show
     */
    private void showError(String message) {
        javax.swing.JOptionPane.showMessageDialog(view, message, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}