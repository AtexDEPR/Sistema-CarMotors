/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;


import java.util.Date;
import java.util.List;

public class PurchaseOrder {
    private int orderId;
    private Date orderDate;
    private String status; // "Pending", "Completed", "Cancelled"
    private Integer supplierId; // Puede ser null si no se asigna proveedor inicialmente
    private double totalAmount;
    private Date creationDate;
    private Date lastUpdateDate;
    private List<PurchaseOrderDetail> details;

    public PurchaseOrder(int orderId, Date orderDate, String status, Integer supplierId, double totalAmount,
                        Date creationDate, Date lastUpdateDate, List<PurchaseOrderDetail> details) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = status;
        this.supplierId = supplierId;
        this.totalAmount = totalAmount;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.details = details;
    }

    // Getters y setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<PurchaseOrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PurchaseOrderDetail> details) {
        this.details = details;
    }
}