/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.parts.model.Identifiable;

import java.util.Date;

/**
 * Represents a vehicle.
 * Maps to the 'vehicles' table in the database.
 */
public class Vehicle implements Identifiable {

    private int vehicleId;
    private int customerId;
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private String vin;
    private String color;
    private String engineType;
    private String transmission;
    private int currentMileage;
    private Date lastServiceDate;
    private String notes;
    private Customer customer;

    /**
     * Default constructor.
     */
    public Vehicle() {
    }

    /**
     * Constructor with essential fields.
     *
     * @param customerId The ID of the customer
     * @param licensePlate The license plate number
     * @param make The vehicle make
     * @param model The vehicle model
     * @param year The vehicle year
     */
    public Vehicle(int customerId, String licensePlate, String make, String model, int year) {
        this.customerId = customerId;
        this.licensePlate = licensePlate;
        this.make = make;
        this.model = model;
        this.year = year;
    }

    /**
     * Gets the unique identifier of the vehicle.
     *
     * @return The vehicle ID
     */
    @Override
    public int getId() {
        return vehicleId;
    }

    /**
     * Gets the vehicle ID.
     *
     * @return The vehicle ID
     */
    public int getVehicleId() {
        return vehicleId;
    }

    /**
     * Sets the vehicle ID.
     *
     * @param vehicleId The vehicle ID to set
     */
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    /**
     * Gets the customer ID.
     *
     * @return The customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId The customer ID to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the license plate number.
     *
     * @return The license plate number
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Sets the license plate number.
     *
     * @param licensePlate The license plate number to set
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * Gets the vehicle make.
     *
     * @return The vehicle make
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the vehicle make.
     *
     * @param make The vehicle make to set
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Gets the vehicle model.
     *
     * @return The vehicle model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the vehicle model.
     *
     * @param model The vehicle model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    public Customer getCustomer() { // Añadir este getter
        return customer;
    }

    public void setCustomer(Customer customer) { // Añadir este setter
        this.customer = customer;
    }

    /**
     * Gets the vehicle year.
     *
     * @return The vehicle year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the vehicle year.
     *
     * @param year The vehicle year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the vehicle identification number (VIN).
     *
     * @return The VIN
     */
    public String getVin() {
        return vin;
    }

    /**
     * Sets the vehicle identification number (VIN).
     *
     * @param vin The VIN to set
     */
    public void setVin(String vin) {
        this.vin = vin;
    }

    /**
     * Gets the vehicle color.
     *
     * @return The vehicle color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the vehicle color.
     *
     * @param color The vehicle color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets the engine type.
     *
     * @return The engine type
     */
    public String getEngineType() {
        return engineType;
    }

    /**
     * Sets the engine type.
     *
     * @param engineType The engine type to set
     */
    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    /**
     * Gets the transmission type.
     *
     * @return The transmission type
     */
    public String getTransmission() {
        return transmission;
    }

    /**
     * Sets the transmission type.
     *
     * @param transmission The transmission type to set
     */
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    /**
     * Gets the current mileage.
     *
     * @return The current mileage
     */
    public int getCurrentMileage() {
        return currentMileage;
    }

    /**
     * Sets the current mileage.
     *
     * @param currentMileage The current mileage to set
     */
    public void setCurrentMileage(int currentMileage) {
        this.currentMileage = currentMileage;
    }

    /**
     * Gets the last service date.
     *
     * @return The last service date
     */
    public Date getLastServiceDate() {
        return lastServiceDate;
    }

    /**
     * Sets the last service date.
     *
     * @param lastServiceDate The last service date to set
     */
    public void setLastServiceDate(Date lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
    }

    /**
     * Gets the notes.
     *
     * @return The notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes.
     *
     * @param notes The notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Returns a string representation of the vehicle.
     *
     * @return A string representation of the vehicle
     */
    @Override
    public String toString() {
        return year + " " + make + " " + model + " (" + licensePlate + ")";
    }
}