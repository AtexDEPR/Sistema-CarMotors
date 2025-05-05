/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.model;

import com.carmotorsproject.customers.model.CustomerDAO;
import com.carmotorsproject.customers.model.LoyaltyProgramDAO;

/**
 * Factory class for creating DAO instances in the customers module.
 * Implements the singleton pattern to ensure only one instance of each DAO exists.
 */
public class DAOFactory {

    // Singleton instances of DAOs
    private static CustomerDAO customerDAO;
    private static LoyaltyProgramDAO loyaltyProgramDAO;

    // Private constructor to prevent instantiation
    private DAOFactory() {
    }

    /**
     * Gets the singleton instance of CustomerDAO.
     *
     * @return The CustomerDAO instance
     */
    public static synchronized CustomerDAO getCustomerDAO() {
        if (customerDAO == null) {
            customerDAO = new CustomerDAO();
        }
        return customerDAO;
    }

    /**
     * Gets the singleton instance of LoyaltyProgramDAO.
     *
     * @return The LoyaltyProgramDAO instance
     */
    public static synchronized LoyaltyProgramDAO getLoyaltyProgramDAO() {
        if (loyaltyProgramDAO == null) {
            loyaltyProgramDAO = new LoyaltyProgramDAO();
        }
        return loyaltyProgramDAO;
    }

    /**
     * Resets all DAO instances.
     * Primarily used for testing purposes.
     */
    public static synchronized void resetDAOs() {
        customerDAO = null;
        loyaltyProgramDAO = null;
    }
}