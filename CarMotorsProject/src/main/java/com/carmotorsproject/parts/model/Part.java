/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import java.util.Date;

public class Part {
    private int partId;
    private String name;
    private String type;
    private String compatibleMakeModel;
    private Integer supplierId; // Integer para permitir null
    private int quantityInStock;
    private int minimumStock;
    private Date entryDate;
    private Date estimatedLifespan;
    private String status;
    private String batchId;
    private Date creationDate;
    private Date lastUpdateDate;

    public Part(int partId, String name, String type, String compatibleMakeModel, Integer supplierId,
                int quantityInStock, int minimumStock, Date entryDate, Date estimatedLifespan,
                String status, String batchId, Date creationDate, Date lastUpdateDate) {
        this.partId = partId;
        this.name = name;
        this.type = type;
        this.compatibleMakeModel = compatibleMakeModel;
        this.supplierId = supplierId;
        this.quantityInStock = quantityInStock;
        this.minimumStock = minimumStock;
        this.entryDate = entryDate;
        this.estimatedLifespan = estimatedLifespan;
        this.status = status;
        this.batchId = batchId;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public int getPartId() { return partId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getCompatibleMakeModel() { return compatibleMakeModel; }
    public Integer getSupplierId() { return supplierId; }
    public int getQuantityInStock() { return quantityInStock; }
    public int getMinimumStock() { return minimumStock; }
    public Date getEntryDate() { return entryDate; }
    public Date getEstimatedLifespan() { return estimatedLifespan; }
    public String getStatus() { return status; }
    public String getBatchId() { return batchId; }
    public Date getCreationDate() { return creationDate; }
    public Date getLastUpdateDate() { return lastUpdateDate; }

    public void setPartId(int partId) { this.partId = partId; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setCompatibleMakeModel(String compatibleMakeModel) { this.compatibleMakeModel = compatibleMakeModel; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }
    public void setQuantityInStock(int quantityInStock) { this.quantityInStock = quantityInStock; }
    public void setMinimumStock(int minimumStock) { this.minimumStock = minimumStock; }
    public void setEntryDate(Date entryDate) { this.entryDate = entryDate; }
    public void setEstimatedLifespan(Date estimatedLifespan) { this.estimatedLifespan = estimatedLifespan; }
    public void setStatus(String status) { this.status = status; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public void setLastUpdateDate(Date lastUpdateDate) { this.lastUpdateDate = lastUpdateDate; }
}