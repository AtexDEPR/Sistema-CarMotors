/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.controller;

import com.carmotorsproject.services.model.Vehicle;
import com.carmotorsproject.services.model.VehicleDAO;
import java.util.List;

public class VehicleController {
    private VehicleDAO vehicleDAO;

    public VehicleController() {
        this.vehicleDAO = new VehicleDAO();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleDAO.save(vehicle);
    }

    public void updateVehicle(Vehicle vehicle) {
        vehicleDAO.update(vehicle);
    }

    public void deleteVehicle(int vehicleId) {
        vehicleDAO.delete(vehicleId);
    }

    public Vehicle findById(int vehicleId) {
        return vehicleDAO.findById(vehicleId);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.findAll();
    }
}