/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

public class Supplier {
    private int supplierId;
    private String name;
    private String taxId;
    private String contact;

    public Supplier(int supplierId, String name, String taxId, String contact) {
        this.supplierId = supplierId;
        this.name = name;
        this.taxId = taxId;
        this.contact = contact;
    }

    public int getSupplierId() { return supplierId; }
    public String getName() { return name; }
    public String getTaxId() { return taxId; }
    public String getContact() { return contact; }

    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public void setName(String name) { this.name = name; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public void setContact(String contact) { this.contact = contact; }
}