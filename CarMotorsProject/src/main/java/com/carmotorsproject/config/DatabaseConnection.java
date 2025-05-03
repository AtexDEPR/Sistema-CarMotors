package com.carmotorsproject.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton class for managing database connections to the MySQL database.
 * Provides methods to get a connection, close connections, and handle errors.
 */
public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/carmotors";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // Singleton instance
    private static DatabaseConnection instance;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            throw new RuntimeException("MySQL JDBC Driver not found", e);
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
     * Gets a connection to the database.
     *
     * @return A Connection object
     * @throws SQLException If a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            LOGGER.info("Database connection established successfully");
            return connection;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish database connection", e);
            throw e;
        }
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
}