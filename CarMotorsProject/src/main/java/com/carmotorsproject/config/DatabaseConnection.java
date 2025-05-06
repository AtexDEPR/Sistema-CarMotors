package com.carmotorsproject.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

/**
 * Singleton class for managing database connections to the MySQL database.
 * Provides methods to get a connection, close connections, and handle errors.
 * Includes support for offline mode when database connection fails.
 */
public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    // Default database connection parameters
    private String url = "jdbc:mysql://localhost:3306/carMotors";
    private String user = "campus2023";
    private String password = "campus2023";

    // Configuration file path
    private static final String CONFIG_FILE = "database.properties";

    // Offline mode flag
    private boolean offlineMode = false;

    // Connection retry count
    private int retryCount = 0;
    private static final int MAX_RETRY = 3;

    // Singleton instance
    private static DatabaseConnection instance;

    /**
     * Private constructor to prevent instantiation.
     * Loads database configuration from properties file if available.
     */
    private DatabaseConnection() {
        loadConfiguration();

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            offlineMode = true;
            LOGGER.warning("Switching to offline mode due to missing JDBC driver");
        }
    }

    /**
     * Gets the singleton instance of DatabaseConnection.
     * Thread-safe implementation using double-checked locking.
     *
     * @return The singleton instance
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Loads database configuration from properties file.
     */
    private void loadConfiguration() {
        Properties props = new Properties();
        File configFile = new File(CONFIG_FILE);

        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                props.load(fis);

                // Override default values if properties exist
                if (props.containsKey("db.url")) {
                    url = props.getProperty("db.url");
                }
                if (props.containsKey("db.user")) {
                    user = props.getProperty("db.user");
                }
                if (props.containsKey("db.password")) {
                    password = props.getProperty("db.password");
                }

                LOGGER.info("Database configuration loaded from " + CONFIG_FILE);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Could not load database configuration", e);
            }
        } else {
            // Create default configuration file
            saveConfiguration();
        }
    }

    /**
     * Saves current database configuration to properties file.
     */
    public void saveConfiguration() {
        Properties props = new Properties();
        props.setProperty("db.url", url);
        props.setProperty("db.user", user);
        props.setProperty("db.password", password);

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "CarMotors Database Configuration");
            LOGGER.info("Database configuration saved to " + CONFIG_FILE);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not save database configuration", e);
        }
    }

    /**
     * Gets a connection to the database.
     * If connection fails, it will retry up to MAX_RETRY times.
     * If all retries fail, it will switch to offline mode.
     *
     * @return A Connection object or null if in offline mode
     * @throws SQLException If a database access error occurs and offline mode is not enabled
     */
    public Connection getConnection() throws SQLException {
        // If already in offline mode, return null immediately
        if (offlineMode) {
            LOGGER.info("Application is in offline mode. No database connection available.");
            return null;
        }

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            LOGGER.info("Database connection established successfully");
            retryCount = 0; // Reset retry count on successful connection
            return connection;
        } catch (SQLException e) {
            retryCount++;

            if (retryCount < MAX_RETRY) {
                LOGGER.log(Level.WARNING,
                        "Failed to establish database connection (Attempt " + retryCount +
                                " of " + MAX_RETRY + "): " + e.getMessage());

                // Wait before retrying
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                // Recursive call to retry
                return getConnection();
            } else {
                // Switch to offline mode after max retries
                offlineMode = true;
                LOGGER.log(Level.SEVERE,
                        "Failed to establish database connection after " + MAX_RETRY +
                                " attempts. Switching to offline mode. Error: " + e.getMessage(), e);

                // Provide detailed error information
                String errorDetails = "Error de conexión a la base de datos:\n\n" +
                        "URL: " + url + "\n" +
                        "Usuario: " + user + "\n" +
                        "Error: " + e.getMessage() + "\n\n" +
                        "Posibles soluciones:\n" +
                        "1. Verifique que el servidor MySQL esté en ejecución\n" +
                        "2. Verifique las credenciales de acceso\n" +
                        "3. Verifique que la base de datos 'carmotors' exista\n" +
                        "4. Verifique la configuración en el archivo " + CONFIG_FILE;

                throw new SQLException(errorDetails, e);
            }
        }
    }

    /**
     * Checks if the application is in offline mode.
     *
     * @return true if in offline mode, false otherwise
     */
    public boolean isOfflineMode() {
        return offlineMode;
    }

    /**
     * Manually sets the offline mode.
     *
     * @param offline true to enable offline mode, false to disable
     */
    public void setOfflineMode(boolean offline) {
        this.offlineMode = offline;
        LOGGER.info("Offline mode " + (offline ? "enabled" : "disabled"));
    }

    /**
     * Updates database connection parameters.
     *
     * @param url New database URL
     * @param user New database user
     * @param password New database password
     */
    public void updateConnectionParams(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        // Reset offline mode and retry count to attempt connection with new parameters
        this.offlineMode = false;
        this.retryCount = 0;

        // Save new configuration
        saveConfiguration();

        LOGGER.info("Database connection parameters updated");
    }

    /**
     * Closes a database connection safely.
     *
     * @param connection The connection to close
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Database connection closed successfully");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close database connection", e);
            }
        }
    }

    /**
     * Logs and handles SQL exceptions.
     *
     * @param e The SQLException to handle
     * @param operation Description of the operation that failed
     */
    public void handleException(SQLException e, String operation) {
        LOGGER.log(Level.SEVERE, "Database error during " + operation, e);
        // Additional error handling logic can be added here
    }

    /**
     * Tests the database connection with current parameters.
     *
     * @return true if connection successful, false otherwise
     */
    public boolean testConnection() {
        Connection conn = null;
        try {
            // Reset offline mode temporarily to force connection attempt
            boolean wasOffline = offlineMode;
            offlineMode = false;
            retryCount = 0;

            conn = DriverManager.getConnection(url, user, password);
            LOGGER.info("Test connection successful");

            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Test connection failed: " + e.getMessage(), e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }
}
