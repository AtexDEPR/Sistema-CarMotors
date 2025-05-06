/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.model;

import java.util.Date;

public class Customer {
    private int customerId;
    private String name;
    private String identificationNumber;
    private String phone;
    private String email;
    private String address;
    private Date creationDate;
    private Date lastUpdateDate;

    public Customer(int customerId, String name, String identificationNumber, String phone, String email, 
                    String address, Date creationDate, Date lastUpdateDate) {
        this.customerId = customerId;
        this.name = name;
        this.identificationNumber = identificationNumber;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    // Getters y setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIdentificationNumber() { return identificationNumber; }
    public void setIdentificationNumber(String identificationNumber) { this.identificationNumber = identificationNumber; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public Date getLastUpdateDate() { return lastUpdateDate; }
    public void setLastUpdateDate(Date lastUpdateDate) { this.lastUpdateDate = lastUpdateDate; }
}