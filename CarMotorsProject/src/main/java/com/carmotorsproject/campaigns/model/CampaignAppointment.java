package com.carmotorsproject.campaigns.model;

import java.util.Date;
import java.util.UUID;

/**
 * Model class representing an appointment related to a marketing campaign
 */
public class CampaignAppointment implements Identifiable {
    
    private String id;
    private String campaignId;
    private String customerId;
    private String vehicleId;
    private Date appointmentDate;
    private String status;
    private String notes;
    private Date creationDate;
    private Date completionDate;
    private Date lastModifiedDate;
    
    /**
     * Default constructor
     */
    public CampaignAppointment() {
        this.id = UUID.randomUUID().toString();
        this.creationDate = new Date();
    }
    
    /**
     * Constructor with parameters
     * 
     * @param campaignId Campaign ID
     * @param customerId Customer ID
     * @param vehicleId Vehicle ID
     * @param appointmentDate Appointment date
     * @param status Appointment status
     */
    public CampaignAppointment(String campaignId, String customerId, String vehicleId, 
                              Date appointmentDate, String status) {
        this();
        this.campaignId = campaignId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCampaignId() {
        return campaignId;
    }
    
    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public Date getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public Date getCompletionDate() {
        return completionDate;
    }
    
    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }
    
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    /**
     * Check if the appointment is upcoming
     * 
     * @return true if the appointment is in the future
     */
    public boolean isUpcoming() {
        return appointmentDate.after(new Date());
    }
    
    /**
     * Check if the appointment is overdue
     * 
     * @return true if the appointment date has passed and status is not "Completada" or "Cancelada"
     */
    public boolean isOverdue() {
        return appointmentDate.before(new Date()) && 
               !status.equals("Completada") && 
               !status.equals("Cancelada");
    }
    
    /**
     * Calculate days until appointment
     * 
     * @return number of days until appointment, negative if appointment has passed
     */
    public int getDaysUntilAppointment() {
        Date now = new Date();
        long diff = appointmentDate.getTime() - now.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
    
    @Override
    public String toString() {
        return "Cita: " + appointmentDate + " - Estado: " + status;
    }
}