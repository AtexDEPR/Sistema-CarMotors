package com.carmotorsproject.campaigns.model;

import java.util.Date;
import java.util.UUID;

/**
 * Model class representing an inspection item result
 */
public class InspectionResult implements Identifiable {
    
    private String id;
    private String inspectionId;
    private String itemName;
    private String category;
    private int conditionScore; // 1-5 scale
    private String conditionDescription;
    private String notes;
    private boolean requiresAttention;
    private Date creationDate;
    
    /**
     * Default constructor
     */
    public InspectionResult() {
        this.id = UUID.randomUUID().toString();
        this.creationDate = new Date();
    }
    
    /**
     * Constructor with parameters
     * 
     * @param inspectionId Inspection ID
     * @param itemName Item name
     * @param category Category
     * @param conditionScore Condition score (1-5)
     * @param conditionDescription Condition description
     */
    public InspectionResult(String inspectionId, String itemName, String category, 
                           int conditionScore, String conditionDescription) {
        this();
        this.inspectionId = inspectionId;
        this.itemName = itemName;
        this.category = category;
        this.conditionScore = conditionScore;
        this.conditionDescription = conditionDescription;
        this.requiresAttention = conditionScore <= 2;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getInspectionId() {
        return inspectionId;
    }
    
    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public int getConditionScore() {
        return conditionScore;
    }
    
    public void setConditionScore(int conditionScore) {
        this.conditionScore = conditionScore;
        this.requiresAttention = conditionScore <= 2;
    }
    
    public String getConditionDescription() {
        return conditionDescription;
    }
    
    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = conditionDescription;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public boolean isRequiresAttention() {
        return requiresAttention;
    }
    
    public void setRequiresAttention(boolean requiresAttention) {
        this.requiresAttention = requiresAttention;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    /**
     * Get condition description based on score
     * 
     * @return Standardized condition description
     */
    public String getStandardConditionDescription() {
        switch (conditionScore) {
            case 1:
                return "Deficiente";
            case 2:
                return "Regular";
            case 3:
                return "Aceptable";
            case 4:
                return "Bueno";
            case 5:
                return "Excelente";
            default:
                return "No evaluado";
        }
    }
    
    @Override
    public String toString() {
        return itemName + ": " + getStandardConditionDescription();
    }
}