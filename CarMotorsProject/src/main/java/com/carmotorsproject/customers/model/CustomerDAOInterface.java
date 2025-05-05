/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.model;

import com.carmotorsproject.customers.model.Customer;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interface for customer data access operations.
 * Defines methods for CRUD operations on customers.
 */
public interface CustomerDAOInterface {

    /**
     * Saves a new customer to the database.
     *
     * @param customer The customer to save
     * @return The saved customer with its generated ID
     * @throws SQLException If a database access error occurs
     */
    Customer save(Customer customer) throws SQLException;

    /**
     * Updates an existing customer in the database.
     *
     * @param customer The customer to update
     * @return The updated customer
     * @throws SQLException If a database access error occurs
     */
    Customer update(Customer customer) throws SQLException;

    /**
     * Deletes a customer from the database.
     *
     * @param id The ID of the customer to delete
     * @return true if the customer was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds a customer by their ID.
     *
     * @param id The ID of the customer to find
     * @return The customer, or null if not found
     * @throws SQLException If a database access error occurs
     */
    Customer findById(int id) throws SQLException;

    /**
     * Finds all customers in the database.
     *
     * @return A list of all customers
     * @throws SQLException If a database access error occurs
     */
    List<Customer> findAll() throws SQLException;

    /**
     * Finds customers by their identification.
     *
     * @param identification The identification to search for
     * @return A list of customers with the specified identification
     * @throws SQLException If a database access error occurs
     */
    List<Customer> findByIdentification(String identification) throws SQLException;

    /**
     * Finds customers by their name (first name or last name).
     *
     * @param name The name to search for
     * @return A list of customers matching the name
     * @throws SQLException If a database access error occurs
     */
    List<Customer> findByName(String name) throws SQLException;

    /**
     * Finds customers by their email.
     *
     * @param email The email to search for
     * @return A list of customers with the specified email
     * @throws SQLException If a database access error occurs
     */
    List<Customer> findByEmail(String email) throws SQLException;

    /**
     * Finds customers by their phone number.
     *
     * @param phone The phone number to search for
     * @return A list of customers with the specified phone number
     * @throws SQLException If a database access error occurs
     */
    List<Customer> findByPhone(String phone) throws SQLException;

    /**
     * Finds customers by their registration date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of customers registered within the date range
     * @throws SQLException If a database access error occurs
     */
    List<Customer> findByRegistrationDateRange(Date startDate, Date endDate) throws SQLException;

    /**
     * Finds active customers.
     *
     * @return A list of active customers
     * @throws SQLException If a database access error occurs
     */
    List<Customer> findActive() throws SQLException;

    /**
     * Finds inactive customers.
     *
     * @return A list of inactive customers
     * @throws SQLException If a database access error occurs
     */
    List<Customer> findInactive() throws SQLException;

    /**
     * Updates the active status of a customer.
     *
     * @param customerId The ID of the customer
     * @param active The new active status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean updateActiveStatus(int customerId, boolean active) throws SQLException;

    /**
     * Updates the last visit date of a customer.
     *
     * @param customerId The ID of the customer
     * @param lastVisitDate The new last visit date
     * @return true if the date was updated, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean updateLastVisitDate(int customerId, Date lastVisitDate) throws SQLException;

    /**
     * Checks if an identification is unique.
     *
     * @param identification The identification to check
     * @param excludeCustomerId The ID of the customer to exclude from the check (for updates)
     * @return true if the identification is unique, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean isUniqueIdentification(String identification, int excludeCustomerId) throws SQLException;

    /**
     * Checks if an email is unique.
     *
     * @param email The email to check
     * @param excludeCustomerId The ID of the customer to exclude from the check (for updates)
     * @return true if the email is unique, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean isUniqueEmail(String email, int excludeCustomerId) throws SQLException;
}