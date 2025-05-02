package com.carmotorsproject.campaigns.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Model class representing a vehicle inspection
 */
public class Inspection implements Identifiable {
    
    private String id;
    private String vehicleId;
    private String customerId;
    private String technicianName;
    private Date inspectionDate;
    private String overallCondition;
    private String notes;
    private String status;
    private Date creationDate;
    private Date lastModifiedDate;
    
    /**
     * Default constructor
     */
    public Inspection() {
        this.id = UUID.randomUUID().toString();
        this.creationDate = new Date();
        this.inspectionDate = new Date();
    }
    
    /**
     * Constructor with parameters
     * 
     * @param vehicleId Vehicle ID
     * @param customerId Customer ID
     * @param technicianName Technician name
     * @param overallCondition Overall condition
     */
    public Inspection(String vehicleId, String customerId, String technicianName, String overallCondition) {
        this();
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.technicianName = technicianName;
        this.overallCondition = overallCondition;
        this.status = "Completada";
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getTechnicianName() {
        return technicianName;
    }
    
    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }
    
    public Date getInspectionDate() {
        return inspectionDate;
    }
    
    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }
    
    public String getOverallCondition() {
        return overallCondition;
    }
    
    public void setOverallCondition(String overallCondition) {
        this.overallCondition = overallCondition;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    /**
     * Calculate the age of the inspection in days
     * 
     * @return age in days
     */
    public int getAgeInDays() {
        Date now = new Date();
        long diff = now.getTime() - inspectionDate.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
    
    /**
     * Check if the inspection is recent (less than 30 days old)
     * 
     * @return true if the inspection is recent
     */
    public boolean isRecent() {
        return getAgeInDays() < 30;
    }
    
    @Override
    public String toString() {
        return "Inspección: " + inspectionDate + " - Condición: " + overallCondition;
    }
}