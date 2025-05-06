/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

public class PartsInService {
    private int partsInServiceId;
    private int serviceId;
    private int partId;
    private int quantityUsed;
    private Double unitPrice;

    public PartsInService(int partsInServiceId, int serviceId, int partId, int quantityUsed, Double unitPrice) {
        this.partsInServiceId = partsInServiceId;
        this.serviceId = serviceId;
        this.partId = partId;
        this.quantityUsed = quantityUsed;
        this.unitPrice = unitPrice;
    }

    // Getters y setters
    public int getPartsInServiceId() { return partsInServiceId; }
    public void setPartsInServiceId(int partsInServiceId) { this.partsInServiceId = partsInServiceId; }
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    public int getPartId() { return partId; }
    public void setPartId(int partId) { this.partId = partId; }
    public int getQuantityUsed() { return quantityUsed; }
    public void setQuantityUsed(int quantityUsed) { this.quantityUsed = quantityUsed; }
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
}