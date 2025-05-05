/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.controller;

import com.carmotorsproject.customers.model.CustomerDAO;
import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.customers.model.DAOFactory;
import com.carmotorsproject.customers.views.CustomerView;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for customer-related operations.
 * Mediates between the CustomerView and the CustomerDAO.
 */
public class CustomerController {

    private static final Logger LOGGER = Logger.getLogger(CustomerController.class.getName());

    // View
    private final CustomerView view;

    // DAO
    private final CustomerDAO customerDAO;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The customer view
     */
    public CustomerController(CustomerView view) {
        this.view = view;
        this.customerDAO = DAOFactory.getCustomerDAO();
    }

    /**
     * Loads all customers and updates the view.
     */
    public void loadCustomers() {
        try {
            List<Customer> customers = customerDAO.findAll();
            view.updateCustomerTable(customers);
            LOGGER.log(Level.INFO, "Loaded {0} customers", customers.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading customers", ex);
            view.showError("Error loading customers: " + ex.getMessage());
        }
    }

    /**
     * Loads the details of a specific customer and updates the view.
     *
     * @param customerId The ID of the customer to load
     */
    public void loadCustomerDetails(int customerId) {
        try {
            Customer customer = customerDAO.findById(customerId);
            if (customer != null) {
                view.populateCustomerForm(customer);
                LOGGER.log(Level.INFO, "Loaded details for customer ID: {0}", customerId);
            } else {
                LOGGER.log(Level.WARNING, "Customer not found with ID: {0}", customerId);
                view.showError("Customer not found with ID: " + customerId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading customer details", ex);
            view.showError("Error loading customer details: " + ex.getMessage());
        }
    }

    /**
     * Adds a new customer.
     *
     * @param customer The customer to add
     */
    public void addCustomer(Customer customer) {
        try {
            // Validate the customer
            validateCustomer(customer);

            // Check if the identification is unique
            if (!customerDAO.isUniqueIdentification(customer.getIdentification(), 0)) {
                throw new IllegalArgumentException("Identification already exists: " + customer.getIdentification());
            }

            // Check if the email is unique (if provided)
            if (customer.getEmail() != null && !customer.getEmail().isEmpty() &&
                    !customerDAO.isUniqueEmail(customer.getEmail(), 0)) {
                throw new IllegalArgumentException("Email already exists: " + customer.getEmail());
            }

            // Save the customer
            Customer savedCustomer = customerDAO.save(customer);

            // Reload the customer list
            loadCustomers();

            LOGGER.log(Level.INFO, "Added customer with ID: {0}", savedCustomer.getCustomerId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding customer", ex);
            view.showError("Error adding customer: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, "Invalid customer data", ex);
            view.showError(ex.getMessage());
        }
    }

    /**
     * Updates an existing customer.
     *
     * @param customer The customer to update
     */
    public void updateCustomer(Customer customer) {
        try {
            // Validate the customer
            validateCustomer(customer);

            // Check if the identification is unique (excluding this customer)
            if (!customerDAO.isUniqueIdentification(customer.getIdentification(), customer.getCustomerId())) {
                throw new IllegalArgumentException("Identification already exists: " + customer.getIdentification());
            }

            // Check if the email is unique (if provided, excluding this customer)
            if (customer.getEmail() != null && !customer.getEmail().isEmpty() &&
                    !customerDAO.isUniqueEmail(customer.getEmail(), customer.getCustomerId())) {
                throw new IllegalArgumentException("Email already exists: " + customer.getEmail());
            }

            // Update the customer
            Customer updatedCustomer = customerDAO.update(customer);

            // Reload the customer list
            loadCustomers();

            LOGGER.log(Level.INFO, "Updated customer with ID: {0}", updatedCustomer.getCustomerId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating customer", ex);
            view.showError("Error updating customer: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, "Invalid customer data", ex);
            view.showError(ex.getMessage());
        }
    }

    /**
     * Deletes a customer.
     *
     * @param customerId The ID of the customer to delete
     */
    public void deleteCustomer(int customerId) {
        try {
            boolean deleted = customerDAO.delete(customerId);
            if (deleted) {
                // Reload the customer list
                loadCustomers();
                LOGGER.log(Level.INFO, "Deleted customer with ID: {0}", customerId);
            } else {
                LOGGER.log(Level.WARNING, "Customer not found with ID: {0}", customerId);
                view.showError("Customer not found with ID: " + customerId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting customer", ex);
            view.showError("Error deleting customer: " + ex.getMessage());
        }
    }

    /**
     * Searches for customers by name.
     *
     * @param name The name to search for
     */
    public void searchCustomersByName(String name) {
        try {
            List<Customer> customers = customerDAO.findByName(name);
            view.updateCustomerTable(customers);
            LOGGER.log(Level.INFO, "Found {0} customers matching name '{1}'",
                    new Object[]{customers.size(), name});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching customers by name", ex);
            view.showError("Error searching customers by name: " + ex.getMessage());
        }
    }

    /**
     * Searches for customers by identification.
     *
     * @param identification The identification to search for
     */
    public void searchCustomersByIdentification(String identification) {
        try {
            List<Customer> customers = customerDAO.findByIdentification(identification);
            view.updateCustomerTable(customers);
            LOGGER.log(Level.INFO, "Found {0} customers matching identification '{1}'",
                    new Object[]{customers.size(), identification});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching customers by identification", ex);
            view.showError("Error searching customers by identification: " + ex.getMessage());
        }
    }

    /**
     * Searches for customers by email.
     *
     * @param email The email to search for
     */
    public void searchCustomersByEmail(String email) {
        try {
            List<Customer> customers = customerDAO.findByEmail(email);
            view.updateCustomerTable(customers);
            LOGGER.log(Level.INFO, "Found {0} customers matching email '{1}'",
                    new Object[]{customers.size(), email});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching customers by email", ex);
            view.showError("Error searching customers by email: " + ex.getMessage());
        }
    }

    /**
     * Searches for customers by phone.
     *
     * @param phone The phone to search for
     */
    public void searchCustomersByPhone(String phone) {
        try {
            List<Customer> customers = customerDAO.findByPhone(phone);
            view.updateCustomerTable(customers);
            LOGGER.log(Level.INFO, "Found {0} customers matching phone '{1}'",
                    new Object[]{customers.size(), phone});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching customers by phone", ex);
            view.showError("Error searching customers by phone: " + ex.getMessage());
        }
    }

    /**
     * Updates the active status of a customer.
     *
     * @param customerId The ID of the customer
     * @param active The new active status
     * @return true if the status was updated, false otherwise
     */
    public boolean updateActiveStatus(int customerId, boolean active) {
        try {
            boolean updated = customerDAO.updateActiveStatus(customerId, active);
            if (updated) {
                // Reload the customer list
                loadCustomers();
                LOGGER.log(Level.INFO, "Updated active status of customer ID {0} to {1}",
                        new Object[]{customerId, active});
            } else {
                LOGGER.log(Level.WARNING, "Customer not found with ID: {0}", customerId);
                view.showError("Customer not found with ID: " + customerId);
            }
            return updated;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating customer active status", ex);
            view.showError("Error updating customer active status: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Updates the last visit date of a customer.
     *
     * @param customerId The ID of the customer
     * @param lastVisitDate The new last visit date
     * @return true if the date was updated, false otherwise
     */
    public boolean updateLastVisitDate(int customerId, Date lastVisitDate) {
        try {
            boolean updated = customerDAO.updateLastVisitDate(customerId, lastVisitDate);
            if (updated) {
                LOGGER.log(Level.INFO, "Updated last visit date of customer ID {0} to {1}",
                        new Object[]{customerId, lastVisitDate});
            } else {
                LOGGER.log(Level.WARNING, "Customer not found with ID: {0}", customerId);
                view.showError("Customer not found with ID: " + customerId);
            }
            return updated;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating customer last visit date", ex);
            view.showError("Error updating customer last visit date: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Validates a customer.
     *
     * @param customer The customer to validate
     * @throws IllegalArgumentException If the customer is invalid
     */
    private void validateCustomer(Customer customer) throws IllegalArgumentException {
        // Validate first name
        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required.");
        }

        // Validate last name
        if (customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required.");
        }

        // Validate identification
        if (customer.getIdentification() == null || customer.getIdentification().trim().isEmpty()) {
            throw new IllegalArgumentException("Identification is required.");
        }

        // Validate identification type
        if (customer.getIdentificationType() == null || customer.getIdentificationType().trim().isEmpty()) {
            throw new IllegalArgumentException("Identification type is required.");
        }

        // Validate email format (if provided)
        if (customer.getEmail() != null && !customer.getEmail().isEmpty() &&
                !customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Validate phone format (if provided)
        if (customer.getPhone() != null && !customer.getPhone().isEmpty() &&
                !customer.getPhone().matches("^[0-9\\-\\+\\s()]+$")) {
            throw new IllegalArgumentException("Invalid phone format.");
        }
    }
}