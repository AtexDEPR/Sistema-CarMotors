/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.invoices.model;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory class for creating DAO instances in the invoices module.
 * Implements the singleton pattern to ensure only one instance of each DAO exists.
 */
public class DAOFactory {

    private static final Logger LOGGER = Logger.getLogger(DAOFactory.class.getName());

    // Singleton instances of DAOs
    private static volatile InvoiceDAO invoiceDAO;

    /**
     * Private constructor to prevent instantiation.
     */
    private DAOFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets the singleton instance of InvoiceDAO.
     * Uses double-checked locking for thread safety.
     *
     * @return The singleton instance of InvoiceDAO
     */
    public static InvoiceDAO getInvoiceDAO() {
        if (invoiceDAO == null) {
            synchronized (DAOFactory.class) {
                if (invoiceDAO == null) {
                    invoiceDAO = new InvoiceDAO();
                    LOGGER.log(Level.INFO, "Created new instance of InvoiceDAO");
                }
            }
        }
        return invoiceDAO;
    }

    /**
     * Resets all DAO instances.
     * Useful for testing or when database connection changes.
     */
    public static void resetDAOs() {
        synchronized (DAOFactory.class) {
            invoiceDAO = null;
            LOGGER.log(Level.INFO, "Reset all DAO instances");
        }
    }
}