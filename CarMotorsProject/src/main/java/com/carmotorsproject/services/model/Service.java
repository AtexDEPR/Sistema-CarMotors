/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import java.sql.Timestamp;
import java.util.Date;
import jdk.jfr.Timespan;

/**
 *
 * @author camper
 */
public class Service {
    private int serviceId;
    private MaintenanceType maintenanceType;
    private int vehicleId;
    private Integer mileage;
    private String description;
    private String initialDiagnosis;
    private String finalObservations;
    private Double estimatedTime;
    private Double laborCost;
    private ServiceStatus status;
    private Timestamp startDate;
    private Timestamp endDate;
    private Date warrantyUntil;

    public Service(int serviceId, MaintenanceType maintenanceType, int vehicleId, Integer mileage, String description,
                   String initialDiagnosis, String finalObservations, Double estimatedTime, Double laborCost,
                   ServiceStatus status, Timestamp startDate, Timestamp endDate, Date warrantyUntil) {
        this.serviceId = serviceId;
        this.maintenanceType = maintenanceType;
        this.vehicleId = vehicleId;
        this.mileage = mileage;
        this.description = description;
        this.initialDiagnosis = initialDiagnosis;
        this.finalObservations = finalObservations;
        this.estimatedTime = estimatedTime;
        this.laborCost = laborCost;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.warrantyUntil = warrantyUntil;
    }

    // Getters
    public int getServiceId() { return serviceId; }
    public MaintenanceType getMaintenanceType() { return maintenanceType; }
    public int getVehicleId() { return vehicleId; }
    public Integer getMileage() { return mileage; }
    public String getDescription() { return description; }
    public String getInitialDiagnosis() { return initialDiagnosis; }
    public String getFinalObservations() { return finalObservations; }
    public Double getEstimatedTime() { return estimatedTime; }
    public Double getLaborCost() { return laborCost; }
    public ServiceStatus getStatus() { return status; }
    public Timestamp getStartDate() { return startDate; }
    public Timestamp getEndDate() { return endDate; }
    public Date getWarrantyUntil() { return warrantyUntil; }

    // Setter básico para status (puede expandirse)
    public void setStatus(ServiceStatus status) { this.status = status; }

    @Override
    public String toString() {
        return serviceId + "-" + description; // Formato para comboBox
    }
    
}
