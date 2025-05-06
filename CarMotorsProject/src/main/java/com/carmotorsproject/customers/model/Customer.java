/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.model;

import java.util.Date;

/**
 * Represents a customer in the system.
 * Implements the Identifiable interface.
 */
public class Customer implements Identifiable {

    private int customerId;
    private String firstName;
    private String lastName;
    private String identification;
    private String identificationType;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Date registrationDate;
    private Date lastVisitDate;
    private boolean active;
    private String notes;
    private LoyaltyProgram loyaltyProgram;

    /**
     * Default constructor.
     */
    public Customer() {
        this.registrationDate = new Date();
        this.active = true;
    }

    /**
     * Constructor with essential fields.
     *
     * @param firstName The first name of the customer
     * @param lastName The last name of the customer
     * @param identification The identification number of the customer
     * @param identificationType The type of identification (e.g., ID card, passport)
     * @param email The email address of the customer
     * @param phone The phone number of the customer
     */
    public Customer(String firstName, String lastName, String identification,
                    String identificationType, String email, String phone) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.identification = identification;
        this.identificationType = identificationType;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Gets the ID of the customer.
     *
     * @return The customer ID
     */
    @Override
    public int getId() {
        return customerId;
    }

    /**
     * Gets the customer ID.
     *
     * @return The customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId The customer ID to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the first name of the customer.
     *
     * @return The first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the customer.
     *
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the customer.
     *
     * @return The last name
     */
    public String getLastName() {
        return lastName;
    }

    public LoyaltyProgram getLoyaltyProgram() { // Añadir getter
        return loyaltyProgram;
    }

    public void setLoyaltyProgram(LoyaltyProgram loyaltyProgram) { // Añadir setter
        this.loyaltyProgram = loyaltyProgram;
    }

    /**
     * Sets the last name of the customer.
     *
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the full name of the customer.
     *
     * @return The full name (first name + last name)
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Gets the identification number of the customer.
     *
     * @return The identification number
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * Sets the identification number of the customer.
     *
     * @param identification The identification number to set
     */
    public void setIdentification(String identification) {
        this.identification = identification;
    }

    /**
     * Gets the type of identification.
     *
     * @return The identification type
     */
    public String getIdentificationType() {
        return identificationType;
    }

    /**
     * Sets the type of identification.
     *
     * @param identificationType The identification type to set
     */
    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    /**
     * Gets the email address of the customer.
     *
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the customer.
     *
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number of the customer.
     *
     * @return The phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the customer.
     *
     * @param phone The phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the address of the customer.
     *
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address The address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the city of the customer.
     *
     * @return The city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the customer.
     *
     * @param city The city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the state of the customer.
     *
     * @return The state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state of the customer.
     *
     * @param state The state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets the ZIP code of the customer.
     *
     * @return The ZIP code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the ZIP code of the customer.
     *
     * @param zipCode The ZIP code to set
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Gets the country of the customer.
     *
     * @return The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the customer.
     *
     * @param country The country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the registration date of the customer.
     *
     * @return The registration date
     */
    public Date getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets the registration date of the customer.
     *
     * @param registrationDate The registration date to set
     */
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Gets the last visit date of the customer.
     *
     * @return The last visit date
     */
    public Date getLastVisitDate() {
        return lastVisitDate;
    }

    /**
     * Sets the last visit date of the customer.
     *
     * @param lastVisitDate The last visit date to set
     */
    public void setLastVisitDate(Date lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    /**
     * Checks if the customer is active.
     *
     * @return true if the customer is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the customer.
     *
     * @param active The active status to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the notes about the customer.
     *
     * @return The notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes about the customer.
     *
     * @param notes The notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }


    /**
     * Returns a string representation of the customer.
     *
     * @return A string representation of the customer
     */
    @Override
    public String toString() {
        return getFullName() + " (" + identification + ")";
    }
}