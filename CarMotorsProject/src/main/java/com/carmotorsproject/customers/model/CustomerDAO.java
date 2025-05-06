/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.model;

import com.carmotorsproject.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of CustomerDAOInterface that provides data access operations for customers.
 */
public class CustomerDAO implements CustomerDAOInterface {

    private static final Logger LOGGER = Logger.getLogger(CustomerDAO.class.getName());
    private final Connection connection;

    /**
     * Constructor that initializes the database connection.
     */
    public CustomerDAO() {
        // Fix: Use getInstance().getConnection() instead of static getConnection()
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        LOGGER.log(Level.INFO, "CustomerDAO initialized with database connection");
    }

    /**
     * Saves a new customer to the database.
     *
     * @param customer The customer to save
     * @return The saved customer with its generated ID
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Customer save(Customer customer) throws SQLException {
        // Check if the identification is unique
        if (!isUniqueIdentification(customer.getIdentification(), 0)) {
            throw new SQLException("Identification already exists: " + customer.getIdentification());
        }

        // Check if the email is unique (if provided)
        if (customer.getEmail() != null && !customer.getEmail().isEmpty() &&
                !isUniqueEmail(customer.getEmail(), 0)) {
            throw new SQLException("Email already exists: " + customer.getEmail());
        }

        String sql = "INSERT INTO customers (first_name, last_name, identification, identification_type, " +
                "email, phone, address, city, state, zip_code, country, registration_date, " +
                "last_visit_date, active, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getIdentification());
            stmt.setString(4, customer.getIdentificationType());
            stmt.setString(5, customer.getEmail());
            stmt.setString(6, customer.getPhone());
            stmt.setString(7, customer.getAddress());
            stmt.setString(8, customer.getCity());
            stmt.setString(9, customer.getState());
            stmt.setString(10, customer.getZipCode());
            stmt.setString(11, customer.getCountry());

            if (customer.getRegistrationDate() != null) {
                stmt.setTimestamp(12, new Timestamp(customer.getRegistrationDate().getTime()));
            } else {
                stmt.setTimestamp(12, new Timestamp(new Date().getTime())); // Default to current date
            }

            if (customer.getLastVisitDate() != null) {
                stmt.setTimestamp(13, new Timestamp(customer.getLastVisitDate().getTime()));
            } else {
                stmt.setNull(13, Types.TIMESTAMP);
            }

            stmt.setBoolean(14, customer.isActive());
            stmt.setString(15, customer.getNotes());

            // Execute insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed, no rows affected.");
            }

            // Get generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setCustomerId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }

            LOGGER.log(Level.INFO, "Customer created with ID: {0}", customer.getCustomerId());
            return customer;
        }
    }

    /**
     * Updates an existing customer in the database.
     *
     * @param customer The customer to update
     * @return The updated customer
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Customer update(Customer customer) throws SQLException {
        // Check if the identification is unique (excluding this customer)
        if (!isUniqueIdentification(customer.getIdentification(), customer.getCustomerId())) {
            throw new SQLException("Identification already exists: " + customer.getIdentification());
        }

        // Check if the email is unique (if provided, excluding this customer)
        if (customer.getEmail() != null && !customer.getEmail().isEmpty() &&
                !isUniqueEmail(customer.getEmail(), customer.getCustomerId())) {
            throw new SQLException("Email already exists: " + customer.getEmail());
        }

        String sql = "UPDATE customers SET first_name = ?, last_name = ?, identification = ?, " +
                "identification_type = ?, email = ?, phone = ?, address = ?, city = ?, " +
                "state = ?, zip_code = ?, country = ?, registration_date = ?, " +
                "last_visit_date = ?, active = ?, notes = ? " +
                "WHERE customer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set parameters
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getIdentification());
            stmt.setString(4, customer.getIdentificationType());
            stmt.setString(5, customer.getEmail());
            stmt.setString(6, customer.getPhone());
            stmt.setString(7, customer.getAddress());
            stmt.setString(8, customer.getCity());
            stmt.setString(9, customer.getState());
            stmt.setString(10, customer.getZipCode());
            stmt.setString(11, customer.getCountry());

            if (customer.getRegistrationDate() != null) {
                stmt.setTimestamp(12, new Timestamp(customer.getRegistrationDate().getTime()));
            } else {
                stmt.setTimestamp(12, new Timestamp(new Date().getTime())); // Default to current date
            }

            if (customer.getLastVisitDate() != null) {
                stmt.setTimestamp(13, new Timestamp(customer.getLastVisitDate().getTime()));
            } else {
                stmt.setNull(13, Types.TIMESTAMP);
            }

            stmt.setBoolean(14, customer.isActive());
            stmt.setString(15, customer.getNotes());
            stmt.setInt(16, customer.getCustomerId());

            // Execute update
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating customer failed, no rows affected.");
            }

            LOGGER.log(Level.INFO, "Customer updated with ID: {0}", customer.getCustomerId());
            return customer;
        }
    }

    /**
     * Deletes a customer from the database.
     *
     * @param id The ID of the customer to delete
     * @return true if the customer was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        // Check if customer has associated vehicles or loyalty programs
        String checkVehiclesSql = "SELECT COUNT(*) FROM vehicles WHERE customer_id = ?";
        String checkLoyaltySql = "SELECT COUNT(*) FROM loyalty_program WHERE customer_id = ?";

        try (PreparedStatement vehiclesStmt = connection.prepareStatement(checkVehiclesSql);
             PreparedStatement loyaltyStmt = connection.prepareStatement(checkLoyaltySql)) {

            vehiclesStmt.setInt(1, id);
            loyaltyStmt.setInt(1, id);

            try (ResultSet vehiclesRs = vehiclesStmt.executeQuery();
                 ResultSet loyaltyRs = loyaltyStmt.executeQuery()) {

                if (vehiclesRs.next() && vehiclesRs.getInt(1) > 0) {
                    throw new SQLException("Cannot delete customer with associated vehicles.");
                }

                if (loyaltyRs.next() && loyaltyRs.getInt(1) > 0) {
                    throw new SQLException("Cannot delete customer with an associated loyalty program.");
                }
            }
        }

        // Delete the customer
        String deleteSql = "DELETE FROM customers WHERE customer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Customer deleted with ID: {0}, Rows affected: {1}",
                    new Object[]{id, affectedRows});

            return affectedRows > 0;
        }
    }

    /**
     * Finds a customer by their ID.
     *
     * @param id The ID of the customer to find
     * @return The customer, or null if not found
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Customer findById(int id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Finds all customers in the database.
     *
     * @return A list of all customers
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY last_name, first_name";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }

        return customers;
    }

    /**
     * Finds customers by their identification.
     *
     * @param identification The identification to search for
     * @return A list of customers with the specified identification
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Customer> findByIdentification(String identification) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE identification = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, identification);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        }

        return customers;
    }

    /**
     * Finds customers by their name (first name or last name).
     *
     * @param name The name to search for
     * @return A list of customers matching the name
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Customer> findByName(String name) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE first_name LIKE ? OR last_name LIKE ? ORDER BY last_name, first_name";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            stmt.setString(2, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        }

        return customers;
    }

    /**
     * Finds customers by their email.
     *
     * @param email The email to search for
     * @return A list of customers with the specified email
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Customer> findByEmail(String email) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        }

        return customers;
    }

    /**
     * Finds customers by their phone number.
     *
     * @param phone The phone number to search for
     * @return A list of customers with the specified phone number
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Customer> findByPhone(String phone) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE phone = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        }

        return customers;
    }

    /**
     * Finds customers by their registration date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of customers registered within the date range
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Customer> findByRegistrationDateRange(Date startDate, Date endDate) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE registration_date BETWEEN ? AND ? ORDER BY registration_date";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        }
        return customers;
    }

    /**
     * Finds active customers.
     *
     * @return A list of active customers
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Customer> findActive() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE active = TRUE ORDER BY last_name, first_name";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }

        return customers;
    }

    /**
     * Finds inactive customers.
     *
     * @return A list of inactive customers
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Customer> findInactive() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE active = FALSE ORDER BY last_name, first_name";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }

        return customers;
    }

    /**
     * Updates the active status of a customer.
     *
     * @param customerId The ID of the customer
     * @param active The new active status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean updateActiveStatus(int customerId, boolean active) throws SQLException {
        String sql = "UPDATE customers SET active = ? WHERE customer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, active);
            stmt.setInt(2, customerId);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Customer active status updated: ID {0}, New Status {1}",
                    new Object[]{customerId, active});

            return affectedRows > 0;
        }
    }

    /**
     * Updates the last visit date of a customer.
     *
     * @param customerId The ID of the customer
     * @param lastVisitDate The new last visit date
     * @return true if the date was updated, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean updateLastVisitDate(int customerId, Date lastVisitDate) throws SQLException {
        String sql = "UPDATE customers SET last_visit_date = ? WHERE customer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(lastVisitDate.getTime()));
            stmt.setInt(2, customerId);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Customer last visit date updated: ID {0}, New Date {1}",
                    new Object[]{customerId, lastVisitDate});

            return affectedRows > 0;
        }
    }

    /**
     * Checks if an identification is unique.
     *
     * @param identification The identification to check
     * @param excludeCustomerId The ID of the customer to exclude from the check (for updates)
     * @return true if the identification is unique, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean isUniqueIdentification(String identification, int excludeCustomerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM customers WHERE identification = ? AND customer_id != ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, identification);
            stmt.setInt(2, excludeCustomerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * Checks if an email is unique.
     *
     * @param email The email to check
     * @param excludeCustomerId The ID of the customer to exclude from the check (for updates)
     * @return true if the email is unique, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean isUniqueEmail(String email, int excludeCustomerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM customers WHERE email = ? AND customer_id != ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setInt(2, excludeCustomerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * Maps a ResultSet row to a Customer object.
     *
     * @param rs The ResultSet containing customer data
     * @return A Customer object populated with data from the ResultSet
     * @throws SQLException If a database access error occurs
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();

        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setIdentification(rs.getString("identification"));
        customer.setIdentificationType(rs.getString("identification_type"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setCity(rs.getString("city"));
        customer.setState(rs.getString("state"));
        customer.setZipCode(rs.getString("zip_code"));
        customer.setCountry(rs.getString("country"));

        Timestamp registrationDate = rs.getTimestamp("registration_date");
        if (registrationDate != null) {
            customer.setRegistrationDate(new Date(registrationDate.getTime()));
        }

        Timestamp lastVisitDate = rs.getTimestamp("last_visit_date");
        if (lastVisitDate != null) {
            customer.setLastVisitDate(new Date(lastVisitDate.getTime()));
        }

        customer.setActive(rs.getBoolean("active"));
        customer.setNotes(rs.getString("notes"));

        return customer;
    }
}