/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import java.util.Date;

public class Technician {
    private int technicianId;
    private String name;
    private String specialty;
    private String status;
    private Date creationDate;
    private Date lastUpdateDate;

    public Technician() {}

    public Technician(int technicianId, String name, String specialty, String status, Date creationDate, Date lastUpdateDate) {
        this.technicianId = technicianId;
        this.name = name;
        this.specialty = specialty;
        this.status = status;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    // Getters and setters
    public int getTechnicianId() { return technicianId; }
    public void setTechnicianId(int technicianId) { this.technicianId = technicianId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public Date getLastUpdateDate() { return lastUpdateDate; }
    public void setLastUpdateDate(Date lastUpdateDate) { this.lastUpdateDate = lastUpdateDate; }
}