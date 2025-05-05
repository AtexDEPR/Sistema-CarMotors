/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.controller;

import com.carmotorsproject.customers.model.CustomerDAO;
import com.carmotorsproject.customers.model.LoyaltyProgramDAO;
import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.customers.model.CustomerLevel;
import com.carmotorsproject.customers.model.DAOFactory;
import com.carmotorsproject.customers.model.LoyaltyProgram;
import com.carmotorsproject.customers.views.LoyaltyProgramView;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for loyalty program-related operations.
 * Mediates between the LoyaltyProgramView and the LoyaltyProgramDAO.
 */
public class LoyaltyProgramController {

    private static final Logger LOGGER = Logger.getLogger(LoyaltyProgramController.class.getName());

    // View
    private final LoyaltyProgramView view;

    // DAOs
    private final LoyaltyProgramDAO loyaltyProgramDAO;
    private final CustomerDAO customerDAO;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The loyalty program view
     */
    public LoyaltyProgramController(LoyaltyProgramView view) {
        this.view = view;
        this.loyaltyProgramDAO = DAOFactory.getLoyaltyProgramDAO();
        this.customerDAO = DAOFactory.getCustomerDAO();
    }

    /**
     * Loads all loyalty programs and updates the view.
     */
    public void loadPrograms() {
        try {
            List<LoyaltyProgram> programs = loyaltyProgramDAO.findAll();
            view.updateProgramTable(programs);
            LOGGER.log(Level.INFO, "Loaded {0} loyalty programs", programs.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading loyalty programs", ex);
            view.showError("Error loading loyalty programs: " + ex.getMessage());
        }
    }

    /**
     * Loads all customers for the customer combo box.
     */
    public void loadCustomers() {
        try {
            List<Customer> customers = customerDAO.findActive();
            view.updateCustomerComboBox(customers);
            LOGGER.log(Level.INFO, "Loaded {0} active customers", customers.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading customers", ex);
            view.showError("Error loading customers: " + ex.getMessage());
        }
    }

    /**
     * Loads the details of a specific loyalty program and updates the view.
     *
     * @param programId The ID of the loyalty program to load
     */
    public void loadProgramDetails(int programId) {
        try {
            LoyaltyProgram program = loyaltyProgramDAO.findById(programId);
            if (program != null) {
                view.populateProgramForm(program);
                LOGGER.log(Level.INFO, "Loaded details for loyalty program ID: {0}", programId);
            } else {
                LOGGER.log(Level.WARNING, "Loyalty program not found with ID: {0}", programId);
                view.showError("Loyalty program not found with ID: " + programId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading loyalty program details", ex);
            view.showError("Error loading loyalty program details: " + ex.getMessage());
        }
    }

    /**
     * Enrolls a customer in the loyalty program.
     *
     * @param program The loyalty program to create
     */
    public void enrollCustomer(LoyaltyProgram program) {
        try {
            // Validate the program
            validateProgram(program);

            // Check if the customer already has a loyalty program
            LoyaltyProgram existingProgram = loyaltyProgramDAO.findByCustomer(program.getCustomerId());
            if (existingProgram != null) {
                throw new IllegalArgumentException("Customer already has a loyalty program.");
            }

            // Initialize the program with default values
            program.setAccumulatedPoints(0);
            program.setLevel(CustomerLevel.BRONZE);
            program.setEnrollmentDate(new Date());
            program.setLastUpdateDate(new Date());
            program.setActive(true);

            // Save the program
            LoyaltyProgram savedProgram = loyaltyProgramDAO.save(program);

            // Reload the program list
            loadPrograms();

            LOGGER.log(Level.INFO, "Enrolled customer ID {0} in loyalty program with ID: {1}",
                    new Object[]{program.getCustomerId(), savedProgram.getProgramId()});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error enrolling customer", ex);
            view.showError("Error enrolling customer: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, "Invalid loyalty program data", ex);
            view.showError(ex.getMessage());
        }
    }

    /**
     * Updates an existing loyalty program.
     *
     * @param program The loyalty program to update
     */
    public void updateProgram(LoyaltyProgram program) {
        try {
            // Validate the program
            validateProgram(program);

            // Get the existing program
            LoyaltyProgram existingProgram = loyaltyProgramDAO.findById(program.getProgramId());
            if (existingProgram == null) {
                throw new IllegalArgumentException("Loyalty program not found with ID: " + program.getProgramId());
            }

            // Preserve the original enrollment date and points
            program.setEnrollmentDate(existingProgram.getEnrollmentDate());
            program.setAccumulatedPoints(existingProgram.getAccumulatedPoints());
            program.setLastUpdateDate(new Date());

            // Update the program
            LoyaltyProgram updatedProgram = loyaltyProgramDAO.update(program);

            // Reload the program list
            loadPrograms();

            LOGGER.log(Level.INFO, "Updated loyalty program with ID: {0}", updatedProgram.getProgramId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating loyalty program", ex);
            view.showError("Error updating loyalty program: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, "Invalid loyalty program data", ex);
            view.showError(ex.getMessage());
        }
    }

    /**
     * Adds points to a loyalty program.
     *
     * @param programId The ID of the loyalty program
     * @param points The points to add
     */
    public void addPoints(int programId, int points) {
        try {
            // Validate points
            if (points <= 0) {
                throw new IllegalArgumentException("Points must be greater than zero.");
            }

            // Add points
            LoyaltyProgram updatedProgram = loyaltyProgramDAO.addPoints(programId, points);

            // Reload the program list
            loadPrograms();

            // Reload the program details
            loadProgramDetails(programId);

            LOGGER.log(Level.INFO, "Added {0} points to loyalty program ID {1}",
                    new Object[]{points, programId});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding points", ex);
            view.showError("Error adding points: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, "Invalid points", ex);
            view.showError(ex.getMessage());
        }
    }

    /**
     * Redeems points from a loyalty program.
     *
     * @param programId The ID of the loyalty program
     * @param points The points to redeem
     * @return true if the points were redeemed, false if there are not enough points
     */
    public boolean redeemPoints(int programId, int points) {
        try {
            // Validate points
            if (points <= 0) {
                throw new IllegalArgumentException("Points must be greater than zero.");
            }

            // Redeem points
            LoyaltyProgram updatedProgram = loyaltyProgramDAO.redeemPoints(programId, points);

            if (updatedProgram != null) {
                // Reload the program list
                loadPrograms();

                // Reload the program details
                loadProgramDetails(programId);

                LOGGER.log(Level.INFO, "Redeemed {0} points from loyalty program ID {1}",
                        new Object[]{points, programId});
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Not enough points to redeem {0} from loyalty program ID {1}",
                        new Object[]{points, programId});
                return false;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error redeeming points", ex);
            view.showError("Error redeeming points: " + ex.getMessage());
            return false;
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, "Invalid points", ex);
            view.showError(ex.getMessage());
            return false;
        }
    }

    /**
     * Updates the active status of a loyalty program.
     *
     * @param programId The ID of the loyalty program
     * @param active The new active status
     * @return true if the status was updated, false otherwise
     */
    public boolean updateActiveStatus(int programId, boolean active) {
        try {
            boolean updated = loyaltyProgramDAO.updateActiveStatus(programId, active);
            if (updated) {
                // Reload the program list
                loadPrograms();
                LOGGER.log(Level.INFO, "Updated active status of loyalty program ID {0} to {1}",
                        new Object[]{programId, active});
            } else {
                LOGGER.log(Level.WARNING, "Loyalty program not found with ID: {0}", programId);
                view.showError("Loyalty program not found with ID: " + programId);
            }
            return updated;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating loyalty program active status", ex);
            view.showError("Error updating loyalty program active status: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Gets the name of a customer by their ID.
     *
     * @param customerId The ID of the customer
     * @return The name of the customer, or "Unknown" if not found
     */
    public String getCustomerName(int customerId) {
        try {
            Customer customer = customerDAO.findById(customerId);
            if (customer != null) {
                return customer.getFullName();
            } else {
                return "Unknown";
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error getting customer name", ex);
            return "Unknown";
        }
    }

    /**
     * Validates a loyalty program.
     *
     * @param program The loyalty program to validate
     * @throws IllegalArgumentException If the loyalty program is invalid
     */
    private void validateProgram(LoyaltyProgram program) throws IllegalArgumentException {
        // Validate customer ID
        if (program.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Customer is required.");
        }

        // Validate accumulated points
        if (program.getAccumulatedPoints() < 0) {
            throw new IllegalArgumentException("Accumulated points cannot be negative.");
        }

        // Validate level
        if (program.getLevel() == null) {
            throw new IllegalArgumentException("Level is required.");
        }
    }
}