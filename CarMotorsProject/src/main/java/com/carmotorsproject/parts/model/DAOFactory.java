/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import com.carmotorsproject.parts.model.PartDAO;
import com.carmotorsproject.parts.model.SupplierDAO;
import com.carmotorsproject.parts.model.PurchaseOrderDAO;
import com.carmotorsproject.parts.model.SupplierEvaluationDAO;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory class for creating DAO instances for the parts module.
 * Implements the Factory pattern to provide a centralized way to create DAO objects.
 * Uses the Singleton pattern to ensure only one instance of each DAO exists.
 */
public class DAOFactory {

    private static final Logger LOGGER = Logger.getLogger(DAOFactory.class.getName());

    // Private static instances for singleton pattern
    private static volatile PartDAO partDAO;
    private static volatile SupplierDAO supplierDAO;
    private static volatile PurchaseOrderDAO purchaseOrderDAO;
    private static volatile SupplierEvaluationDAO supplierEvaluationDAO;

    /**
     * Private constructor to prevent instantiation.
     */
    private DAOFactory() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Gets an instance of PartDAO.
     * Uses double-checked locking for thread safety.
     *
     * @return A singleton instance of PartDAO
     */
    public static PartDAO getPartDAO() {
        if (partDAO == null) {
            synchronized (DAOFactory.class) {
                if (partDAO == null) {
                    LOGGER.log(Level.INFO, "Creating new PartDAO instance");
                    partDAO = new PartDAO();
                }
            }
        }
        return partDAO;
    }

    /**
     * Gets an instance of SupplierDAO.
     * Uses double-checked locking for thread safety.
     *
     * @return A singleton instance of SupplierDAO
     */
    public static SupplierDAO getSupplierDAO() {
        if (supplierDAO == null) {
            synchronized (DAOFactory.class) {
                if (supplierDAO == null) {
                    LOGGER.log(Level.INFO, "Creating new SupplierDAO instance");
                    supplierDAO = new SupplierDAO();
                }
            }
        }
        return supplierDAO;
    }

    /**
     * Gets an instance of PurchaseOrderDAO.
     * Uses double-checked locking for thread safety.
     *
     * @return A singleton instance of PurchaseOrderDAO
     */
    public static PurchaseOrderDAO getPurchaseOrderDAO() {
        if (purchaseOrderDAO == null) {
            synchronized (DAOFactory.class) {
                if (purchaseOrderDAO == null) {
                    LOGGER.log(Level.INFO, "Creating new PurchaseOrderDAO instance");
                    purchaseOrderDAO = new PurchaseOrderDAO();
                }
            }
        }
        return purchaseOrderDAO;
    }

    /**
     * Gets an instance of SupplierEvaluationDAO.
     * Uses double-checked locking for thread safety.
     *
     * @return A singleton instance of SupplierEvaluationDAO
     */
    public static SupplierEvaluationDAO getSupplierEvaluationDAO() {
        if (supplierEvaluationDAO == null) {
            synchronized (DAOFactory.class) {
                if (supplierEvaluationDAO == null) {
                    LOGGER.log(Level.INFO, "Creating new SupplierEvaluationDAO instance");
                    supplierEvaluationDAO = new SupplierEvaluationDAO();
                }
            }
        }
        return supplierEvaluationDAO;
    }

    /**
     * Resets all DAO instances.
     * Primarily used for testing purposes.
     */
    public static void resetDAOs() {
        synchronized (DAOFactory.class) {
            LOGGER.log(Level.INFO, "Resetting all DAO instances");
            partDAO = null;
            supplierDAO = null;
            purchaseOrderDAO = null;
            supplierEvaluationDAO = null;
        }
    }
}