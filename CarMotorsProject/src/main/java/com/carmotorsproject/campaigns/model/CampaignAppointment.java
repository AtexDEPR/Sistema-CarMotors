package com.carmotorsproject.campaigns.model;

import java.util.Date;

/**
 * Represents an appointment for a marketing campaign.
 * Maps to the campaign_appointments table in the database.
 */
public class CampaignAppointment implements Identifiable {

    private int campaignAppointmentId;
    private int campaignId;
    private int vehicleId;
    private int customerId;
    private Date appointmentDate;
    private CampaignStatus status;
    private String notes;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;

    /**
     * Default constructor.
     */
    public CampaignAppointment() {
    }

    /**
     * Constructor with essential fields.
     *
     * @param campaignAppointmentId The campaign appointment ID
     * @param campaignId The campaign ID
     * @param vehicleId The vehicle ID
     * @param customerId The customer ID
     * @param appointmentDate The appointment date
     * @param status The appointment status
     */
    public CampaignAppointment(int campaignAppointmentId, int campaignId, int vehicleId, int customerId,
                               Date appointmentDate, CampaignStatus status) {
        this.campaignAppointmentId = campaignAppointmentId;
        this.campaignId = campaignId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    /**
     * Full constructor with all fields.
     *
     * @param campaignAppointmentId The campaign appointment ID
     * @param campaignId The campaign ID
     * @param vehicleId The vehicle ID
     * @param customerId The customer ID
     * @param appointmentDate The appointment date
     * @param status The appointment status
     * @param notes Additional notes
     * @param createdBy The user who created the appointment
     * @param createdDate The date the appointment was created
     * @param lastModifiedBy The user who last modified the appointment
     * @param lastModifiedDate The date the appointment was last modified
     */
    public CampaignAppointment(int campaignAppointmentId, int campaignId, int vehicleId, int customerId,
                               Date appointmentDate, CampaignStatus status, String notes,
                               String createdBy, Date createdDate, String lastModifiedBy, Date lastModifiedDate) {
        this.campaignAppointmentId = campaignAppointmentId;
        this.campaignId = campaignId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.notes = notes;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Gets the campaign appointment ID.
     *
     * @return The campaign appointment ID
     */
    @Override
    public int getId() {
        return campaignAppointmentId;
    }

    /**
     * Gets the campaign appointment ID.
     *
     * @return The campaign appointment ID
     */
    public int getCampaignAppointmentId() {
        return campaignAppointmentId;
    }

    /**
     * Sets the campaign appointment ID.
     *   {
     return campaignAppointmentId;
     }

     /**
     * Sets the campaign appointment ID.
     *
     * @param campaignAppointmentId The campaign appointment ID
     */
    public void setCampaignAppointmentId(int campaignAppointmentId) {
        this.campaignAppointmentId = campaignAppointmentId;
    }

    /**
     * Gets the campaign ID.
     *
     * @return The campaign ID
     */
    public int getCampaignId() {
        return campaignId;
    }

    /**
     * Sets the campaign ID.
     *
     * @param campaignId The campaign ID
     */
    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
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
     * @param customerId The customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the appointment date.
     *
     * @return The appointment date
     */
    public Date getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Sets the appointment date.
     *
     * @param appointmentDate The appointment date
     */
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    /**
     * Gets the appointment status.
     *
     * @return The appointment status
     */
    public CampaignStatus getStatus() {
        return status;
    }

    /**
     * Sets the appointment status.
     *
     * @param status The appointment status
     */
    public void setStatus(CampaignStatus status) {
        this.status = status;
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
     * Gets the user who created the appointment.
     *
     * @return The creator
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created the appointment.
     *
     * @param createdBy The creator
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the date the appointment was created.
     *
     * @return The creation date
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the date the appointment was created.
     *
     * @param createdDate The creation date
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Gets the user who last modified the appointment.
     *
     * @return The last modifier
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Sets the user who last modified the appointment.
     *
     * @param lastModifiedBy The last modifier
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * Gets the date the appointment was last modified.
     *
     * @return The last modification date
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets the date the appointment was last modified.
     *
     * @param lastModifiedDate The last modification date
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Returns a string representation of the campaign appointment.
     *
     * @return A string representation
     */
    @Override
    public String toString() {
        return "Appointment #" + campaignAppointmentId + " (" + status + ")";
    }
}