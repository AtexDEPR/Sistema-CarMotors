/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import com.carmotorsproject.parts.model.Identifiable;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a maintenance service.
 * Maps to the 'services' table in the database.
 */
public class Service implements Identifiable {

    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    private int serviceId;
    private int vehicleId;
    private int technicianId;
    private MaintenanceType maintenanceType;
    private ServiceStatus status;
    private Date startDate;
    private Date endDate;
    private String description;
    private String diagnosis;
    private double laborCost;
    private double partsCost;
    private double totalCost;
    private int mileage;
    private String notes;
    private Vehicle vehicle;
    private Date serviceDate;
    private Technician technician;
    private BigDecimal total;

    /**
     * Default constructor.
     * Logs the creation of a new Service object.
     */
    public Service() {
        LOGGER.log(Level.INFO, "New Service object created");
    }

    /**
     * Constructor with essential fields.
     *
     * @param vehicleId The ID of the vehicle
     * @param technicianId The ID of the technician
     * @param maintenanceType The type of maintenance
     * @param description The service description
     */
    public Service(int vehicleId, int technicianId, MaintenanceType maintenanceType, String description) {
        this.vehicleId = vehicleId;
        this.technicianId = technicianId;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.status = ServiceStatus.PENDING; // Default status
        this.startDate = new Date(); // Default to current date
        LOGGER.log(Level.INFO, "New Service object created for vehicle ID: {0}", vehicleId);
    }

    private List<PartInService> partsInService;
    // --- AÑADIR ESTE GETTER ---
    public List<PartInService> getPartsInService() {
        // Devuelve una lista vacía si es null para evitar NullPointerException en el bucle for
        return (partsInService != null) ? partsInService : new ArrayList<>();
    }

    // --- AÑADIR ESTE SETTER ---
    public void setPartsInService(List<PartInService> partsInService) {
        this.partsInService = partsInService;
    }
    /**
     * Calculates the total cost of the service (labor + parts).
     * Updates the totalCost field.
     */
    public void calculateTotalCost() {
        this.totalCost = this.laborCost + this.partsCost;
    }

    /**
     * Gets the unique identifier of the service.
     *
     * @return The service ID
     */
    @Override
    public int getId() {
        return serviceId;
    }

    /**
     * Gets the service ID.
     *
     * @return The service ID
     */
    public int getServiceId() {
        return serviceId;
    }

    /**
     * Sets the service ID.
     *
     * @param serviceId The service ID to set
     */
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
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
     * Gets the technician ID.
     *
     * @return The technician ID
     */
    public int getTechnicianId() {
        return technicianId;
    }

    /**
     * Sets the technician ID.
     *
     * @param technicianId The technician ID to set
     */
    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    /**
     * Gets the maintenance type.
     *
     * @return The maintenance type
     */
    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    /**
     * Sets the maintenance type.
     *
     * @param maintenanceType The maintenance type to set
     */
    public void setMaintenanceType(MaintenanceType maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    /**
     * Gets the service status.
     *
     * @return The service status
     */
    public ServiceStatus getStatus() {
        return status;
    }

    /**
     * Sets the service status.
     *
     * @param status The service status to set
     */
    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    /**
     * Gets the start date.
     *
     * @return The start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     *
     * @param startDate The start date to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date.
     *
     * @return The end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date.
     *
     * @param endDate The end date to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the service description.
     *
     * @return The service description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the service description.
     *
     * @param description The service description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the diagnosis.
     *
     * @return The diagnosis
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Sets the diagnosis.
     *
     * @param diagnosis The diagnosis to set
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * Gets the labor cost.
     *
     * @return The labor cost
     */
    public double getLaborCost() {
        return laborCost;
    }

    /**
     * Sets the labor cost and recalculates the total cost.
     *
     * @param laborCost The labor cost to set
     */
    public void setLaborCost(double laborCost) {
        this.laborCost = laborCost;
        calculateTotalCost();
    }

    /**
     * Gets the parts cost.
     *
     * @return The parts cost
     */
    public double getPartsCost() {
        return partsCost;
    }

    /**
     * Sets the parts cost and recalculates the total cost.
     *
     * @param partsCost The parts cost to set
     */
    public void setPartsCost(double partsCost) {
        this.partsCost = partsCost;
        calculateTotalCost();
    }

    /**
     * Gets the total cost.
     *
     * @return The total cost
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Gets the vehicle mileage.
     *
     * @return The vehicle mileage
     */
    public int getMileage() {
        return mileage;
    }

    /**
     * Sets the vehicle mileage.
     *
     * @param mileage The vehicle mileage to set
     */
    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    /**
     * Gets the notes.
     *
     * @return The notes
     */
    public String getNotes() {
        return notes;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public Technician getTechnician() {
        return technician;
    }

    public BigDecimal getTotal() { // o public double getTotal()
        return total;
    }

    /**
     * Sets the notes.
     *
     * @param notes The notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId=" + serviceId +
                ", vehicleId=" + vehicleId +
                ", maintenanceType=" + maintenanceType +
                ", status=" + status +
                ", startDate=" + startDate +
                ", totalCost=" + totalCost +
                '}';
    }

    public void setTotalCost(double totalCost) {

    }
}