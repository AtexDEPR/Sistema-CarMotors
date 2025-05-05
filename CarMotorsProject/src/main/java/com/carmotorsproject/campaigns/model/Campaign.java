package com.carmotorsproject.campaigns.model;

import java.util.Date;
import java.util.List;

/**
 * Represents a marketing campaign.
 * Maps to the campaigns table in the database.
 */
public class Campaign implements Identifiable {

    private int campaignId;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private double discountPercentage;
    private String targetVehicleType;
    private int targetCustomerSegment;
    private String status;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private List<CampaignAppointment> appointments;
    private long completedAppointmentsCount;

    /**
     * Default constructor.
     */
    public Campaign() {
    }

    /**
     * Constructor with essential fields.
     *
     * @param campaignId The campaign ID
     * @param name The campaign name
     * @param description The campaign description
     * @param startDate The start date of the campaign
     * @param endDate The end date of the campaign
     * @param discountPercentage The discount percentage offered
     */
    public Campaign(int campaignId, String name, String description, Date startDate, Date endDate, double discountPercentage) {
        this.campaignId = campaignId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPercentage = discountPercentage;
    }

    /**
     * Full constructor with all fields.
     *
     * @param campaignId The campaign ID
     * @param name The campaign name
     * @param description The campaign description
     * @param startDate The start date of the campaign
     * @param endDate The end date of the campaign
     * @param discountPercentage The discount percentage offered
     * @param targetVehicleType The target vehicle type
     * @param targetCustomerSegment The target customer segment
     * @param status The campaign status
     * @param createdBy The user who created the campaign
     * @param createdDate The date the campaign was created
     * @param lastModifiedBy The user who last modified the campaign
     * @param lastModifiedDate The date the campaign was last modified
     */
    public Campaign(int campaignId, String name, String description, Date startDate, Date endDate,
                    double discountPercentage, String targetVehicleType, int targetCustomerSegment,
                    String status, String createdBy, Date createdDate, String lastModifiedBy, Date lastModifiedDate) {
        this.campaignId = campaignId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPercentage = discountPercentage;
        this.targetVehicleType = targetVehicleType;
        this.targetCustomerSegment = targetCustomerSegment;
        this.status = status;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Gets the campaign ID.
     *
     * @return The campaign ID
     */
    @Override
    public int getId() {
        return campaignId;
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
     * Gets the campaign name.
     *
     * @return The campaign name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the campaign name.
     *
     * @param name The campaign name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the campaign description.
     *
     * @return The campaign description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the campaign description.
     *
     * @param description The campaign description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the start date of the campaign.
     *
     * @return The start date
     */
    public Date getStartDate() {
        return startDate;
    }

    public List<CampaignAppointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<CampaignAppointment> appointments) {
        this.appointments = appointments;
    }

    public long getCompletedAppointments() { // Cambiado el nombre para evitar conflicto con getter
        return completedAppointmentsCount;
    }

    public void setCompletedAppointments(long count) { // Cambiado el nombre
        this.completedAppointmentsCount = count;
    }

    /**
     * Sets the start date of the campaign.
     *
     * @param startDate The start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the campaign.
     *
     * @return The end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the campaign.
     *
     * @param endDate The end date
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the discount percentage offered.
     *
     * @return The discount percentage
     */
    public double getDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * Sets the discount percentage offered.
     *
     * @param discountPercentage The discount percentage
     */
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    /**
     * Gets the target vehicle type.
     *
     * @return The target vehicle type
     */
    public String getTargetVehicleType() {
        return targetVehicleType;
    }

    /**
     * Sets the target vehicle type.
     *
     * @param targetVehicleType The target vehicle type
     */
    public void setTargetVehicleType(String targetVehicleType) {
        this.targetVehicleType = targetVehicleType;
    }

    /**
     * Gets the target customer segment.
     *
     * @return The target customer segment
     */
    public int getTargetCustomerSegment() {
        return targetCustomerSegment;
    }

    /**
     * Sets the target customer segment.
     *
     * @param targetCustomerSegment The target customer segment
     */
    public void setTargetCustomerSegment(int targetCustomerSegment) {
        this.targetCustomerSegment = targetCustomerSegment;
    }

    /**
     * Gets the campaign status.
     *
     * @return The campaign status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the campaign status.
     *
     * @param status The campaign status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the user who created the campaign.
     *
     * @return The creator
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created the campaign.
     *
     * @param createdBy The creator
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the date the campaign was created.
     *
     * @return The creation date
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the date the campaign was created.
     *
     * @param createdDate The creation date
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Gets the user who last modified the campaign.
     *
     * @return The last modifier
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Sets the user who last modified the campaign.
     *
     * @param lastModifiedBy The last modifier
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * Gets the date the campaign was last modified.
     *
     * @return The last modification date
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets the date the campaign was last modified.
     *
     * @param lastModifiedDate The last modification date
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Returns a string representation of the campaign.
     *
     * @return A string representation
     */
    @Override
    public String toString() {
        return name + " (" + discountPercentage + "% discount)";
    }
}