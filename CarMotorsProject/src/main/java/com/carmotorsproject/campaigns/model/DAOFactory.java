package com.carmotorsproject.campaigns.model;

import com.carmotorsproject.customers.model.CustomerDAO;
import com.carmotorsproject.services.model.VehicleDAO;

/**
 * Factory for creating DAO instances
 */
public class DAOFactory {
    
    private static DAOFactory instance;
    
    private CampaignDAOInterface campaignDAO;
    private CampaignAppointmentDAOInterface campaignAppointmentDAO;
    private InspectionDAOInterface inspectionDAO;
    
    private CustomerDAO customerDAO;
    private VehicleDAO vehicleDAO;
    
    /**
     * Private constructor to enforce singleton pattern
     */
    private DAOFactory() {
        // Initialize DAOs
        campaignDAO = new CampaignDAO();
    }
    
    /**
     * Get the singleton instance
     * 
     * @return DAOFactory instance
     */
    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }
    
    /**
     * Set the customer DAO
     * 
     * @param customerDAO Customer DAO
     */
    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
        
        // Initialize DAOs that depend on customer DAO
        if (campaignAppointmentDAO == null) {
            campaignAppointmentDAO = new CampaignAppointmentDAO(customerDAO);
        }
    }
    
    /**
     * Set the vehicle DAO
     * 
     * @param vehicleDAO Vehicle DAO
     */
    public void setVehicleDAO(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }
    
    /**
     * Get the campaign DAO
     * 
     * @return CampaignDAOInterface instance
     */
    public CampaignDAOInterface getCampaignDAO() {
        return campaignDAO;
    }
    
    /**
     * Get the campaign appointment DAO
     * 
     * @return CampaignAppointmentDAOInterface instance
     */
    public CampaignAppointmentDAOInterface getCampaignAppointmentDAO() {
        if (campaignAppointmentDAO == null) {
            if (customerDAO == null) {
                throw new IllegalStateException("CustomerDAO must be set before getting CampaignAppointmentDAO");
            }
            campaignAppointmentDAO = new CampaignAppointmentDAO(customerDAO);
        }
        return campaignAppointmentDAO;
    }
    
    /**
     * Get the inspection DAO
     * 
     * @return InspectionDAOInterface instance
     */
    public InspectionDAOInterface getInspectionDAO() {
        if (inspectionDAO == null) {
            inspectionDAO = new InspectionDAO();
        }
        return inspectionDAO;
    }
}