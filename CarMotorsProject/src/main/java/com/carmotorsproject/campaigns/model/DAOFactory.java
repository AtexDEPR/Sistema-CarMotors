package com.carmotorsproject.campaigns.model;

/**
 * Factory for campaign module DAOs.
 * Uses the singleton pattern to ensure only one instance of each DAO exists.
 */
public class DAOFactory {

    // Singleton instances
    private static CampaignDAO campaignDAO;
    private static CampaignAppointmentDAO campaignAppointmentDAO;
    private static InspectionDAO inspectionDAO;

    // Private constructor to prevent instantiation
    private DAOFactory() {
    }

    /**
     * Gets the CampaignDAO instance.
     *
     * @return The CampaignDAO instance
     */
    public static synchronized CampaignDAO getCampaignDAO() {
        if (campaignDAO == null) {
            campaignDAO = new CampaignDAO();
        }
        return campaignDAO;
    }

    /**
     * Gets the CampaignAppointmentDAO instance.
     *
     * @return The CampaignAppointmentDAO instance
     */
    public static synchronized CampaignAppointmentDAO getCampaignAppointmentDAO() {
        if (campaignAppointmentDAO == null) {
            campaignAppointmentDAO = new CampaignAppointmentDAO();
        }
        return campaignAppointmentDAO;
    }

    /**
     * Gets the InspectionDAO instance.
     *
     * @return The InspectionDAO instance
     */
    public static synchronized InspectionDAO getInspectionDAO() {
        if (inspectionDAO == null) {
            inspectionDAO = new InspectionDAO();
        }
        return inspectionDAO;
    }
}