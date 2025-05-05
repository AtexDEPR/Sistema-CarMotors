/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.controller;

import com.carmotorsproject.services.model.DAOFactory;
import com.carmotorsproject.services.model.Vehicle;
import com.carmotorsproject.services.model.VehicleDAO;
import com.carmotorsproject.services.views.VehicleView;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for vehicle-related operations.
 * Mediates between the VehicleView and the VehicleDAO.
 */
public class VehicleController {

    private static final Logger LOGGER = Logger.getLogger(VehicleController.class.getName());

    // View
    private final VehicleView view;

    // DAO
    private final VehicleDAO vehicleDAO;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The vehicle view
     */
    public VehicleController(VehicleView view) {
        this.view = view;
        this.vehicleDAO = DAOFactory.getVehicleDAO();
    }

    /**
     * Loads all vehicles and updates the view.
     */
    public void loadVehicles() {
        try {
            List<Vehicle> vehicles = vehicleDAO.findAll();
            view.updateVehicleTable(vehicles);
            LOGGER.log(Level.INFO, "Loaded {0} vehicles", vehicles.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading vehicles", ex);
            showError("Error loading vehicles: " + ex.getMessage());
        }
    }

    /**
     * Loads the details of a specific vehicle and updates the view.
     *
     * @param vehicleId The ID of the vehicle to load
     */
    public void loadVehicleDetails(int vehicleId) {
        try {
            Vehicle vehicle = vehicleDAO.findById(vehicleId);
            if (vehicle != null) {
                view.populateVehicleForm(vehicle);
                LOGGER.log(Level.INFO, "Loaded details for vehicle ID: {0}", vehicleId);
            } else {
                LOGGER.log(Level.WARNING, "Vehicle not found with ID: {0}", vehicleId);
                showError("Vehicle not found with ID: " + vehicleId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading vehicle details", ex);
            showError("Error loading vehicle details: " + ex.getMessage());
        }
    }

    /**
     * Adds a new vehicle.
     *
     * @param vehicle The vehicle to add
     */
    public void addVehicle(Vehicle vehicle) {
        try {
            // Check if the license plate is unique
            if (!vehicleDAO.isUniqueLicensePlate(vehicle.getLicensePlate(), 0)) {
                showError("License plate already exists: " + vehicle.getLicensePlate());
                return;
            }

            Vehicle savedVehicle = vehicleDAO.save(vehicle);
            loadVehicles();
            LOGGER.log(Level.INFO, "Added vehicle with ID: {0}", savedVehicle.getVehicleId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding vehicle", ex);
            showError("Error adding vehicle: " + ex.getMessage());
        }
    }

    /**
     * Updates an existing vehicle.
     *
     * @param vehicle The vehicle to update
     */
    public void updateVehicle(Vehicle vehicle) {
        try {
            // Check if the license plate is unique (excluding this vehicle)
            if (!vehicleDAO.isUniqueLicensePlate(vehicle.getLicensePlate(), vehicle.getVehicleId())) {
                showError("License plate already exists: " + vehicle.getLicensePlate());
                return;
            }

            Vehicle updatedVehicle = vehicleDAO.update(vehicle);
            loadVehicles();
            LOGGER.log(Level.INFO, "Updated vehicle with ID: {0}", updatedVehicle.getVehicleId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating vehicle", ex);
            showError("Error updating vehicle: " + ex.getMessage());
        }
    }

    /**
     * Deletes a vehicle.
     *
     * @param vehicleId The ID of the vehicle to delete
     */
    public void deleteVehicle(int vehicleId) {
        try {
            boolean deleted = vehicleDAO.delete(vehicleId);
            if (deleted) {
                loadVehicles();
                LOGGER.log(Level.INFO, "Deleted vehicle with ID: {0}", vehicleId);
            } else {
                LOGGER.log(Level.WARNING, "Vehicle not found with ID: {0}", vehicleId);
                showError("Vehicle not found with ID: " + vehicleId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting vehicle", ex);
            showError("Error deleting vehicle: " + ex.getMessage());
        }
    }

    /**
     * Searches for vehicles by make and model.
     *
     * @param make The make to search for
     * @param model The model to search for
     */
    public void searchVehiclesByMakeAndModel(String make, String model) {
        try {
            List<Vehicle> vehicles = vehicleDAO.findByMakeAndModel(make, model);
            view.updateVehicleTable(vehicles);
            LOGGER.log(Level.INFO, "Found {0} vehicles matching make '{1}' and model '{2}'",
                    new Object[]{vehicles.size(), make, model});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching vehicles", ex);
            showError("Error searching vehicles: " + ex.getMessage());
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