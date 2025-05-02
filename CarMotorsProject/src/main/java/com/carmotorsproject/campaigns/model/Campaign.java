package com.carmotorsproject.campaigns.model;

import java.util.Date;
import java.util.UUID;

/**
 * Model class representing a marketing campaign
 */
public class Campaign implements Identifiable {
    
    private String id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private double discount;
    private String type;
    private boolean active;
    private Date creationDate;
    private Date lastModifiedDate;
    
    /**
     * Default constructor
     */
    public Campaign() {
        this.id = UUID.randomUUID().toString();
        this.creationDate = new Date();
        this.active = true;
    }
    
    /**
     * Constructor with parameters
     * 
     * @param name Campaign name
     * @param description Campaign description
     * @param startDate Start date
     * @param endDate End date
     * @param discount Discount percentage
     * @param type Campaign type
     */
    public Campaign(String name, String description, Date startDate, Date endDate, double discount, String type) {
        this();
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = discount;
        this.type = type;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public double getDiscount() {
        return discount;
    }
    
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
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
     * Check if the campaign is currently active based on dates
     * 
     * @return true if the campaign is currently active
     */
    public boolean isCurrentlyActive() {
        if (!active) {
            return false;
        }
        
        Date now = new Date();
        return startDate.before(now) && endDate.after(now);
    }
    
    /**
     * Calculate remaining days for the campaign
     * 
     * @return number of days remaining, or 0 if campaign has ended
     */
    public int getRemainingDays() {
        Date now = new Date();
        if (now.after(endDate)) {
            return 0;
        }
        
        long diff = endDate.getTime() - now.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
    
    @Override
    public String toString() {
        return name + " (" + discount + "% descuento)";
    }
}