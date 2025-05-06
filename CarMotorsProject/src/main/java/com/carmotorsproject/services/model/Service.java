/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import jdk.jfr.Timespan;


public class Service {
    private int serviceId;
    private String maintenanceType;
    private int vehicleId;
    private Integer mileage;
    private String description;
    private String initialDiagnosis;
    private String finalObservations;
    private Double estimatedTime;
    private Double laborCost;
    private String status;
    private Date startDate;
    private Date endDate;
    private Date warrantyUntil;
    private List<Integer> technicianIds;
    private List<PartsInService> partsInService;

    public Service(int serviceId, String maintenanceType, int vehicleId, Integer mileage, String description, 
                   String initialDiagnosis, String finalObservations, Double estimatedTime, Double laborCost, 
                   String status, Date startDate, Date endDate, Date warrantyUntil, List<Integer> technicianIds, 
                   List<PartsInService> partsInService) {
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
        this.technicianIds = technicianIds;
        this.partsInService = partsInService;
    }

    // Getters y setters
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; }
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public Integer getMileage() { return mileage; }
    public void setMileage(Integer mileage) { this.mileage = mileage; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getInitialDiagnosis() { return initialDiagnosis; }
    public void setInitialDiagnosis(String initialDiagnosis) { this.initialDiagnosis = initialDiagnosis; }
    public String getFinalObservations() { return finalObservations; }
    public void setFinalObservations(String finalObservations) { this.finalObservations = finalObservations; }
    public Double getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(Double estimatedTime) { this.estimatedTime = estimatedTime; }
    public Double getLaborCost() { return laborCost; }
    public void setLaborCost(Double laborCost) { this.laborCost = laborCost; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Date getWarrantyUntil() { return warrantyUntil; }
    public void setWarrantyUntil(Date warrantyUntil) { this.warrantyUntil = warrantyUntil; }
    public List<Integer> getTechnicianIds() { return technicianIds; }
    public void setTechnicianIds(List<Integer> technicianIds) { this.technicianIds = technicianIds; }
    public List<PartsInService> getPartsInService() { return partsInService; }
    public void setPartsInService(List<PartsInService> partsInService) { this.partsInService = partsInService; }
}