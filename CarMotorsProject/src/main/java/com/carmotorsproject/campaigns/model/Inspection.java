package com.carmotorsproject.campaigns.model;

import java.util.Date;

/**
 * Represents a vehicle inspection.
 * Maps to the inspections table in the database.
 */
public class Inspection implements Identifiable {

    private int inspectionId;
    private int vehicleId;
    private int technicianId;
    private Date inspectionDate;
    private InspectionResult result;
    private String notes;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;

    /**
     * Default constructor.
     */
    public Inspection() {
    }

    /**
     * Constructor with essential fields.
     *
     * @param inspectionId The inspection ID
     * @param vehicleId The vehicle ID
     * @param technicianId The technician ID
     * @param inspectionDate The inspection date
     * @param result The inspection result
     */
    public Inspection(int inspectionId, int vehicleId, int technicianId, Date inspectionDate, InspectionResult result) {
        this.inspectionId = inspectionId;
        this.vehicleId = vehicleId;
        this.technicianId = technicianId;
        this.inspectionDate = inspectionDate;
        this.result = result;
    }

    /**
     * Full constructor with all fields.
     *
     * @param inspectionId The inspection ID
     * @param vehicleId The vehicle ID
     * @param technicianId The technician ID
     * @param inspectionDate The inspection date
     * @param result The inspection result
     * @param notes Additional notes
     * @param createdBy The user who created the inspection
     * @param createdDate The date the inspection was created
     * @param lastModifiedBy The user who last modified the inspection
     * @param lastModifiedDate The date the inspection was last modified
     */
    public Inspection(int inspectionId, int vehicleId, int technicianId, Date inspectionDate,
                      InspectionResult result, String notes, String createdBy, Date createdDate,
                      String lastModifiedBy, Date lastModifiedDate) {
        this.inspectionId = inspectionId;
        this.vehicleId = vehicleId;
        this.technicianId = technicianId;
        this.inspectionDate = inspectionDate;
        this.result = result;
        this.notes = notes;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Gets the inspection ID.
     *
     * @return The inspection ID
     */
    @Override
    public int getId() {
        return inspectionId;
    }

    /**
     * Gets the inspection ID.
     *
     * @return The inspection ID
     */
    public int getInspectionId() {
        return inspectionId;
    }

    /**
     * Sets the inspection ID.
     *
     * @param inspectionId The inspection ID
     */
    public void setInspectionId(int inspectionId) {
        this.inspectionId = inspectionId;
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
     * @param vehicleId The vehicle ID
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
     * @param technicianId The technician ID
     */
    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    /**
     * Gets the inspection date.
     *
     * @return The inspection date
     */
    public Date getInspectionDate() {
        return inspectionDate;
    }

    /**
     * Sets the inspection date.
     *
     * @param inspectionDate The inspection date
     */
    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    /**
     * Gets the inspection result.
     *
     * @return The inspection result
     */
    public InspectionResult getResult() {
        return result;
    }

    /**
     * Sets the inspection result.
     *
     * @param result The inspection result
     */
    public void setResult(InspectionResult result) {
        this.result = result;
    }

    /**
     * Gets the additional notes.
     *
     * @return The additional notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the additional notes.
     *
     * @param notes The additional notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the user who created the inspection.
     *
     * @return The creator
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created the inspection.
     *
     * @param createdBy The creator
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the date the inspection was created.
     *
     * @return The creation date
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the date the inspection was created.
     *
     * @param createdDate The creation date
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Gets the user who last modified the inspection.
     *
     * @return The last modifier
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Sets the user who last modified the inspection.
     *
     * @param lastModifiedBy The last modifier
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * Gets the date the inspection was last modified.
     *
     * @return The last modification date
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets the date the inspection was last modified.
     *
     * @param lastModifiedDate The last modification date
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Returns a string representation of the inspection.
     *
     * @return A string representation
     */
    @Override
    public String toString() {
        return "Inspection #" + inspectionId + " (" + result + ")";
    }
}