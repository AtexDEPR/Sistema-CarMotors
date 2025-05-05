/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

public class PurchaseOrderDetail {
    private int orderDetailId;
    private int purchaseOrderId;
    private int partId;
    private int quantityOrdered;
    private double estimatedUnitPrice;

    public PurchaseOrderDetail(int orderDetailId, int purchaseOrderId, int partId, int quantityOrdered,
                               double estimatedUnitPrice) {
        this.orderDetailId = orderDetailId;
        this.purchaseOrderId = purchaseOrderId;
        this.partId = partId;
        this.quantityOrdered = quantityOrdered;
        this.estimatedUnitPrice = estimatedUnitPrice;
    }

    public int getOrderDetailId() { return orderDetailId; }
    public int getPurchaseOrderId() { return purchaseOrderId; }
    public int getPartId() { return partId; }
    public int getQuantityOrdered() { return quantityOrdered; }
    public double getEstimatedUnitPrice() { return estimatedUnitPrice; }

    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }
    public void setPurchaseOrderId(int purchaseOrderId) { this.purchaseOrderId = purchaseOrderId; }
    public void setPartId(int partId) { this.partId = partId; }
    public void setQuantityOrdered(int quantityOrdered) { this.quantityOrdered = quantityOrdered; }
    public void setEstimatedUnitPrice(double estimatedUnitPrice) { this.estimatedUnitPrice = estimatedUnitPrice; }
}