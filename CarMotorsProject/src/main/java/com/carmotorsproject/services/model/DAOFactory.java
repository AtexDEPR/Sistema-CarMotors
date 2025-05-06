/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;


/**
 * Factory class for creating DAO instances in the services module.
 * Implements the singleton pattern to ensure only one instance of each DAO exists.
 */
public class DAOFactory {

    // Singleton instances of DAOs
    private static ServiceDAO serviceDAO;
    private static VehicleDAO vehicleDAO;
    private static TechnicianDAO technicianDAO;
    private static DeliveryOrderDAO deliveryOrderDAO;

    // Private constructor to prevent instantiation
    private DAOFactory() {
    }

    /**
     * Gets the singleton instance of ServiceDAO.
     *
     * @return The ServiceDAO instance
     */
    public static synchronized ServiceDAO getServiceDAO() {
        if (serviceDAO == null) {
            serviceDAO = new ServiceDAO();
        }
        return serviceDAO;
    }

    /**
     * Gets the singleton instance of VehicleDAO.
     *
     * @return The VehicleDAO instance
     */
    public static synchronized VehicleDAO getVehicleDAO() {
        if (vehicleDAO == null) {
            vehicleDAO = new VehicleDAO();
        }
        return vehicleDAO;
    }

    /**
     * Gets the singleton instance of TechnicianDAO.
     *
     * @return The TechnicianDAO instance
     */
    public static synchronized TechnicianDAO getTechnicianDAO() {
        if (technicianDAO == null) {
            technicianDAO = new TechnicianDAO();
        }
        return technicianDAO;
    }

    /**
     * Gets the singleton instance of DeliveryOrderDAO.
     *
     * @return The DeliveryOrderDAO instance
     */
    public static synchronized DeliveryOrderDAO getDeliveryOrderDAO() {
        if (deliveryOrderDAO == null) {
            deliveryOrderDAO = new DeliveryOrderDAO();
        }
        return deliveryOrderDAO;
    }

    /**
     * Resets all DAO instances.
     * Primarily used for testing purposes.
     */
    public static synchronized void resetDAOs() {
        serviceDAO = null;
        vehicleDAO = null;
        technicianDAO = null;
        deliveryOrderDAO = null;
    }
}