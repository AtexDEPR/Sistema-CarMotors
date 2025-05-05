/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;


import java.util.Date;
import java.util.List;

public class PurchaseOrder {
    private int purchaseOrderId;
    private int supplierId; // Cambiado a int porque es NOT NULL
    private Date creationDate;
    private Date expectedDeliveryDate;
    private String status;
    private String observations;
    private List<PurchaseOrderDetail> details;

    public PurchaseOrder(int purchaseOrderId, int supplierId, Date creationDate, Date expectedDeliveryDate,
                         String status, String observations, List<PurchaseOrderDetail> details) {
        this.purchaseOrderId = purchaseOrderId;
        this.supplierId = supplierId;
        this.creationDate = creationDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.status = status;
        this.observations = observations;
        this.details = details;
    }

    public int getPurchaseOrderId() { return purchaseOrderId; }
    public int getSupplierId() { return supplierId; }
    public Date getCreationDate() { return creationDate; }
    public Date getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public String getStatus() { return status; }
    public String getObservations() { return observations; }
    public List<PurchaseOrderDetail> getDetails() { return details; }

    public void setPurchaseOrderId(int purchaseOrderId) { this.purchaseOrderId = purchaseOrderId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public void setExpectedDeliveryDate(Date expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }
    public void setStatus(String status) { this.status = status; }
    public void setObservations(String observations) { this.observations = observations; }
    public void setDetails(List<PurchaseOrderDetail> details) { this.details = details; }
}