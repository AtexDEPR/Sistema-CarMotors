/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import com.carmotorsproject.parts.model.Identifiable;

import java.util.Date;

/**
 * Represents a technician.
 * Maps to the 'technicians' table in the database.
 */
public class Technician implements Identifiable {

    private int technicianId;
    private String name;
    private String lastName;
    private TechnicianSpecialty specialty;
    private String status; // ACTIVE, INACTIVE, ON_LEAVE
    private String phone;
    private String email;
    private Date hireDate;
    private double hourlyRate;
    private String certifications;
    private String notes;

    /**
     * Default constructor.
     */
    public Technician() {
    }

    /**
     * Constructor with essential fields.
     *
     * @param name The technician's first name
     * @param lastName The technician's last name
     * @param specialty The technician's specialty
     * @param hourlyRate The technician's hourly rate
     */
    public Technician(String name, String lastName, TechnicianSpecialty specialty, double hourlyRate) {
        this.name = name;
        this.lastName = lastName;
        this.specialty = specialty;
        this.hourlyRate = hourlyRate;
        this.status = "ACTIVE"; // Default status
        this.hireDate = new Date(); // Default to current date
    }

    /**
     * Gets the unique identifier of the technician.
     *
     * @return The technician ID
     */
    @Override
    public int getId() {
        return technicianId;
    }

    /**
     * Gets the technician ID.
     *
     * @return The technician ID
     */
    public int getTechnicianId() {
        return technicianId;
    }

    /**
     * Sets the technician ID.
     *
     * @param technicianId The technician ID to set
     */
    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    /**
     * Gets the technician's first name.
     *
     * @return The technician's first name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the technician's first name.
     *
     * @param name The technician's first name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the technician's last name.
     *
     * @return The technician's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the technician's last name.
     *
     * @param lastName The technician's last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the technician's full name (first name + last name).
     *
     * @return The technician's full name
     */
    public String getFullName() {
        return name + " " + lastName;
    }

    /**
     * Gets the technician's specialty.
     *
     * @return The technician's specialty
     */
    public TechnicianSpecialty getSpecialty() {
        return specialty;
    }

    /**
     * Sets the technician's specialty.
     *
     * @param specialty The technician's specialty to set
     */
    public void setSpecialty(TechnicianSpecialty specialty) {
        this.specialty = specialty;
    }

    /**
     * Gets the technician's status.
     *
     * @return The technician's status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the technician's status.
     *
     * @param status The technician's status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the technician's phone number.
     *
     * @return The technician's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the technician's phone number.
     *
     * @param phone The technician's phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the technician's email address.
     *
     * @return The technician's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the technician's email address.
     *
     * @param email The technician's email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the technician's hire date.
     *
     * @return The technician's hire date
     */
    public Date getHireDate() {
        return hireDate;
    }

    /**
     * Sets the technician's hire date.
     *
     * @param hireDate The technician's hire date to set
     */
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    /**
     * Gets the technician's hourly rate.
     *
     * @return The technician's hourly rate
     */
    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
     * Sets the technician's hourly rate.
     *
     * @param hourlyRate The technician's hourly rate to set
     */
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    /**
     * Gets the technician's certifications.
     *
     * @return The technician's certifications
     */
    public String getCertifications() {
        return certifications;
    }

    /**
     * Sets the technician's certifications.
     *
     * @param certifications The technician's certifications to set
     */
    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    /**
     * Gets the notes.
     *
     * @return The notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes.
     *
     * @param notes The notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return getFullName() + " - " + specialty;
    }
}