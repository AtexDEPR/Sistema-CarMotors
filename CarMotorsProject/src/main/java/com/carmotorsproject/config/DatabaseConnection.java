package com.carmotorsproject.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton class for managing database connections to Clever Cloud MySQL database
 */
public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    private static DatabaseConnection instance;
    private Connection connection;

    // Database configuration for Clever Cloud
    private String url = "jdbc:mysql://ufalxvcetxjje2vb:5lIvFtFo0HtkbXY8ScG7@btxzpsqiipyryzhogsuu-mysql.services.clever-cloud.com:3306/btxzpsqiipyryzhogsuu";
    private String username = "ufalxvcetxjje2vb";
    private String password = "5lIvFtFo0HtkbXY8ScG7";

    private DatabaseConnection() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Get the singleton instance of DatabaseConnection
     * @return DatabaseConnection instance
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Get a connection to the database
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // For Clever Cloud connections, we need to use the proper JDBC URL format
                // and we might need to set additional properties
                connection = DriverManager.getConnection(url, username, password);
                LOGGER.log(Level.INFO, "Database connection established to Clever Cloud");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to establish database connection to Clever Cloud", e);
                throw e;
            }
        }
        return connection;
    }

    /**
     * Close the database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.log(Level.INFO, "Database connection closed");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close database connection", e);
            }
        }
    }

    /**
     * Update database connection parameters for Clever Cloud
     * @param url JDBC URL for Clever Cloud database
     * @param username Clever Cloud database username
     * @param password Clever Cloud database password
     */
    public void updateConnectionParams(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        // Close existing connection to force reconnect with new parameters
        closeConnection();
    }

    /**
     * Configure the connection with Clever Cloud parameters
     * @param host Clever Cloud host address
     * @param port Database port (usually 3306)
     * @param dbName Database name
     * @param username Database username
     * @param password Database password
     */
    public void configureCleverCloudConnection(String host, int port, String dbName,
                                               String username, String password) {
        // Make sure to use the correct JDBC URL format
        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=true&requireSSL=false",
                host, port, dbName);
        updateConnectionParams(url, username, password);
        LOGGER.log(Level.INFO, "Configured connection to Clever Cloud database: {0}", dbName);
    }
}