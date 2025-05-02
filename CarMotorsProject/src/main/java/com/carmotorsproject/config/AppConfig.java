package com.carmotorsproject.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for managing application configuration
 */
public class AppConfig {
    private static final Logger LOGGER = Logger.getLogger(AppConfig.class.getName());
    private static final String CONFIG_FILE = "config.properties";

    private static AppConfig instance;
    private Configuration config;

    // Default Clever Cloud database configuration
    private static final String DEFAULT_DB_URL = "jdbc:mysql://btxzpsqiipyryzhogsuu-mysql.services.clever-cloud.com:3306/btxzpsqiipyryzhogsuu";
    private static final String DEFAULT_DB_USERNAME = "ufalxvcetxjje2vb";
    private static final String DEFAULT_DB_PASSWORD = "5lIvFtFo0HtkbXY8ScG7";

    private AppConfig() {
        loadConfiguration();
    }

    /**
     * Get the singleton instance of AppConfig
     * @return AppConfig instance
     */
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    /**
     * Load configuration from file
     */
    private void loadConfiguration() {
        Configurations configs = new Configurations();
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                config = configs.properties(configFile);
                LOGGER.log(Level.INFO, "Configuration loaded from {0}", CONFIG_FILE);
            } else {
                LOGGER.log(Level.WARNING, "Configuration file {0} not found. Using default values.", CONFIG_FILE);
                // Create a default configuration
                createDefaultConfig();
            }
        } catch (ConfigurationException e) {
            LOGGER.log(Level.SEVERE, "Error loading configuration", e);
        }
    }

    /**
     * Create default configuration if config file doesn't exist
     */
    private void createDefaultConfig() {
        // Create a default configuration file with Clever Cloud database settings
        try {
            Properties props = new Properties();
            props.setProperty("database.url", DEFAULT_DB_URL);
            props.setProperty("database.username", DEFAULT_DB_USERNAME);
            props.setProperty("database.password", DEFAULT_DB_PASSWORD);

            // Add other default properties
            props.setProperty("app.name", "Car Motors Project");
            props.setProperty("app.version", "1.0");
            props.setProperty("app.log.level", "INFO");

            // Save properties to file
            File configFile = new File(CONFIG_FILE);
            try (FileOutputStream out = new FileOutputStream(configFile)) {
                props.store(out, "Car Motors Project Default Configuration");
                LOGGER.log(Level.INFO, "Created default configuration file: {0}", CONFIG_FILE);
            }

            // Reload configuration
            loadConfiguration();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not create default configuration file", e);
            LOGGER.log(Level.INFO, "Using default configuration values");
        }
    }

    /**
     * Get a string configuration value
     * @param key Configuration key
     * @param defaultValue Default value if key not found
     * @return Configuration value
     */
    public String getString(String key, String defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        return config.getString(key, defaultValue);
    }

    /**
     * Get an integer configuration value
     * @param key Configuration key
     * @param defaultValue Default value if key not found
     * @return Configuration value
     */
    public int getInt(String key, int defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        return config.getInt(key, defaultValue);
    }

    /**
     * Get a boolean configuration value
     * @param key Configuration key
     * @param defaultValue Default value if key not found
     * @return Configuration value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        return config.getBoolean(key, defaultValue);
    }

    /**
     * Get database connection URL
     * @return Database URL
     */
    public String getDatabaseUrl() {
        return getString("database.url", DEFAULT_DB_URL);
    }

    /**
     * Get database username
     * @return Database username
     */
    public String getDatabaseUsername() {
        return getString("database.username", DEFAULT_DB_USERNAME);
    }

    /**
     * Get database password
     * @return Database password
     */
    public String getDatabasePassword() {
        return getString("database.password", DEFAULT_DB_PASSWORD);
    }
}