package com.carmotorsproject.config;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stores application-wide configurations such as database credentials and email settings.
 * Provides static methods to retrieve configuration values.
 */
public class AppConfig {
    private static final Logger LOGGER = Logger.getLogger(AppConfig.class.getName());

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/carmotors";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "kova3452";

    // Email configuration
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final int EMAIL_PORT = 587;
    private static final String EMAIL_USERNAME = "workshop@carmotors.com";
    private static final String EMAIL_PASSWORD = "your_app_password"; // Use app password for Gmail
    private static final boolean EMAIL_AUTH = true;
    private static final boolean EMAIL_TLS = true;

    // Workshop details
    private static final String WORKSHOP_NAME = "Car Motors Workshop";
    private static final String WORKSHOP_ADDRESS = "Calle 123 #45-67, Bogotá, Colombia";
    private static final String WORKSHOP_PHONE = "+57 1 234 5678";
    private static final String WORKSHOP_EMAIL = "info@carmotors.com";
    private static final String WORKSHOP_TAX_ID = "NIT: 901.234.567-8";

    // Application settings
    private static final String APP_VERSION = "1.0.0";
    private static final String LOG_DIRECTORY = "logs";
    private static final int SESSION_TIMEOUT_MINUTES = 30;

    /**
     * Gets the database URL.
     *
     * @return The database URL
     */
    public static String getDbUrl() {
        LOGGER.fine("Retrieving database URL");
        return DB_URL;
    }

    /**
     * Gets the database username.
     *
     * @return The database username
     */
    public static String getDbUser() {
        LOGGER.fine("Retrieving database username");
        return DB_USER;
    }

    /**
     * Gets the database password.
     *
     * @return The database password
     */
    public static String getDbPassword() {
        LOGGER.fine("Retrieving database password");
        return DB_PASSWORD;
    }

    /**
     * Gets the email server host.
     *
     * @return The email server host
     */
    public static String getEmailHost() {
        LOGGER.fine("Retrieving email host");
        return EMAIL_HOST;
    }

    /**
     * Gets the email server port.
     *
     * @return The email server port
     */
    public static int getEmailPort() {
        LOGGER.fine("Retrieving email port");
        return EMAIL_PORT;
    }

    /**
     * Gets the email username.
     *
     * @return The email username
     */
    public static String getEmailUsername() {
        LOGGER.fine("Retrieving email username");
        return EMAIL_USERNAME;
    }

    /**
     * Gets the email password.
     *
     * @return The email password
     */
    public static String getEmailPassword() {
        LOGGER.fine("Retrieving email password");
        return EMAIL_PASSWORD;
    }

    /**
     * Checks if email authentication is required.
     *
     * @return True if email authentication is required, false otherwise
     */
    public static boolean isEmailAuthRequired() {
        LOGGER.fine("Retrieving email auth setting");
        return EMAIL_AUTH;
    }

    /**
     * Checks if email TLS is enabled.
     *
     * @return True if email TLS is enabled, false otherwise
     */
    public static boolean isEmailTlsEnabled() {
        LOGGER.fine("Retrieving email TLS setting");
        return EMAIL_TLS;
    }

    /**
     * Gets the workshop name.
     *
     * @return The workshop name
     */
    public static String getWorkshopName() {
        LOGGER.fine("Retrieving workshop name");
        return WORKSHOP_NAME;
    }

    /**
     * Gets the workshop address.
     *
     * @return The workshop address
     */
    public static String getWorkshopAddress() {
        LOGGER.fine("Retrieving workshop address");
        return WORKSHOP_ADDRESS;
    }

    /**
     * Gets the workshop phone number.
     *
     * @return The workshop phone number
     */
    public static String getWorkshopPhone() {
        LOGGER.fine("Retrieving workshop phone");
        return WORKSHOP_PHONE;
    }

    /**
     * Gets the workshop email.
     *
     * @return The workshop email
     */
    public static String getWorkshopEmail() {
        LOGGER.fine("Retrieving workshop email");
        return WORKSHOP_EMAIL;
    }

    /**
     * Gets the workshop tax ID (NIT).
     *
     * @return The workshop tax ID
     */
    public static String getWorkshopTaxId() {
        LOGGER.fine("Retrieving workshop tax ID");
        return WORKSHOP_TAX_ID;
    }

    /**
     * Gets the application version.
     *
     * @return The application version
     */
    public static String getAppVersion() {
        LOGGER.fine("Retrieving application version");
        return APP_VERSION;
    }

    /**
     * Gets the log directory.
     *
     * @return The log directory
     */
    public static String getLogDirectory() {
        LOGGER.fine("Retrieving log directory");
        return LOG_DIRECTORY;
    }

    /**
     * Gets the session timeout in minutes.
     *
     * @return The session timeout in minutes
     */
    public static int getSessionTimeoutMinutes() {
        LOGGER.fine("Retrieving session timeout");
        return SESSION_TIMEOUT_MINUTES;
    }
}