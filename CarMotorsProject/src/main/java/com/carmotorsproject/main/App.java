package com.carmotorsproject.main;

import com.carmotorsproject.config.AppConfig;
import com.carmotorsproject.config.DatabaseConnection;
import com.carmotorsproject.parts.views.PartView;
import com.carmotorsproject.parts.views.SupplierView;
import com.carmotorsproject.parts.views.PurchaseOrderView;
import com.carmotorsproject.services.views.ServiceView;
import com.carmotorsproject.services.views.VehicleView;
import com.carmotorsproject.services.views.TechnicianView;
import com.carmotorsproject.customers.views.CustomerView;
import com.carmotorsproject.invoices.views.InvoiceView;
import com.carmotorsproject.campaigns.views.CampaignView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point to launch the application.
 */
public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final String APP_TITLE = "Car Motors Workshop - Sistema de Gestión";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel contentPanel;

    /**
     * Main method to start the application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set up logging
        configureLogging();

        // Use SwingUtilities.invokeLater for thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Test database connection
                testDatabaseConnection();

                // Create and show the application
                App app = new App();
                app.createAndShowGUI();

                LOGGER.info("Application started successfully");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error starting application", e);
                showErrorDialog("Error al iniciar la aplicación", e.getMessage());
            }
        });
    }

    /**
     * Configures the application logging.
     */
    private static void configureLogging() {
        // In a real implementation, this would configure log handlers, formatters, etc.
        LOGGER.info("Logging configured");
    }

    /**
     * Tests the database connection.
     *
     * @throws SQLException If the database connection fails
     */
    private static void testDatabaseConnection() throws SQLException {
        LOGGER.info("Testing database connection");

        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection connection = null;

        try {
            connection = dbConnection.getConnection();
            LOGGER.info("Database connection test successful");
        } finally {
            if (connection != null) {
                dbConnection.closeConnection(connection);
            }
        }
    }

    /**
     * Shows an error dialog with the specified title and message.
     *
     * @param title The dialog title
     * @param message The error message
     */
    private static void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Creates and shows the application GUI.
     */
    private void createAndShowGUI() {
        LOGGER.info("Creating application GUI");

        // Create main frame
        mainFrame = new JFrame(APP_TITLE);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainFrame.setLocationRelativeTo(null); // Center on screen

        // Add window close listener
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });

        // Create main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());

        // Create menu panel
        createMenuPanel();

        // Create content panel
        createContentPanel();

        // Add panels to main panel
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add main panel to frame
        mainFrame.add(mainPanel);

        // Show the frame
        mainFrame.setVisible(true);

        // Show welcome panel
        showWelcomePanel();

        LOGGER.info("Application GUI created and displayed");
    }

    /**
     * Creates the menu panel with navigation buttons.
     */
    private void createMenuPanel() {
        LOGGER.info("Creating menu panel");

        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(52, 73, 94)); // Dark blue background
        menuPanel.setPreferredSize(new Dimension(200, WINDOW_HEIGHT));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Add application title
        JLabel titleLabel = new JLabel("Car Motors");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Workshop");
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(subtitleLabel);

        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Add menu buttons
        addMenuButton("Inicio", e -> showWelcomePanel());

        // Parts module
        addMenuSeparator("Inventario");
        addMenuButton("Repuestos", e -> openView(new PartView()));
        addMenuButton("Proveedores", e -> openView(new SupplierView()));
        addMenuButton("Órdenes de Compra", e -> openView(new PurchaseOrderView()));

        // Services module
        addMenuSeparator("Servicios");
        addMenuButton("Servicios", e -> openView(new ServiceView()));
        addMenuButton("Vehículos", e -> openView(new VehicleView()));
        addMenuButton("Técnicos", e -> openView(new TechnicianView()));

        // Customers module
        addMenuSeparator("Clientes");
        addMenuButton("Clientes", e -> openView(new CustomerView()));

        // Invoices module
        addMenuSeparator("Facturación");
        addMenuButton("Facturas", e -> openView(new InvoiceView()));

        // Campaigns module
        addMenuSeparator("Campañas");
        addMenuButton("Campañas", e -> openView(new CampaignView()));

        // Exit button at the bottom
        menuPanel.add(Box.createVerticalGlue());
        addMenuButton("Salir", e -> exitApplication());
    }

    /**
     * Adds a menu button with the specified text and action.
     *
     * @param text The button text
     * @param action The action to perform when the button is clicked
     */
    private void addMenuButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.addActionListener(action);

        // Style the button
        button.setBackground(new Color(41, 128, 185)); // Blue
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        menuPanel.add(button);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    /**
     * Adds a separator with a label to the menu panel.
     *
     * @param text The separator text
     */
    private void addMenuSeparator(String text) {
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(label);

        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    /**
     * Creates the content panel where views will be displayed.
     */
    private void createContentPanel() {
        LOGGER.info("Creating content panel");

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Shows the welcome panel in the content panel.
     */
    private void showWelcomePanel() {
        LOGGER.info("Showing welcome panel");

        // Clear content panel
        contentPanel.removeAll();

        // Create welcome panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(Color.WHITE);

        // Add welcome message
        JLabel welcomeLabel = new JLabel("Bienvenido al Sistema de Gestión");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(welcomeLabel);

        welcomePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add version information
        JLabel versionLabel = new JLabel("Versión " + AppConfig.getAppVersion());
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(versionLabel);

        welcomePanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Add quick access buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.setMaximumSize(new Dimension(600, 200));

        addQuickAccessButton(buttonsPanel, "Repuestos", e -> openView(new PartView()));
        addQuickAccessButton(buttonsPanel, "Servicios", e -> openView(new ServiceView()));
        addQuickAccessButton(buttonsPanel, "Clientes", e -> openView(new CustomerView()));
        addQuickAccessButton(buttonsPanel, "Vehículos", e -> openView(new VehicleView()));
        addQuickAccessButton(buttonsPanel, "Facturas", e -> openView(new InvoiceView()));
        addQuickAccessButton(buttonsPanel, "Campañas", e -> openView(new CampaignView()));

        welcomePanel.add(buttonsPanel);

        // Add to content panel
        contentPanel.add(welcomePanel, BorderLayout.CENTER);

        // Refresh the UI
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Adds a quick access button to the specified panel.
     *
     * @param panel The panel to add the button to
     * @param text The button text
     * @param action The action to perform when the button is clicked
     */
    private void addQuickAccessButton(JPanel panel, String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.addActionListener(action);

        // Style the button
        button.setBackground(new Color(52, 152, 219)); // Light blue
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(button);
    }

    /**
     * Opens the specified view in the content panel.
     *
     * @param view The view to open
     */
    private void openView(JFrame view) {
        LOGGER.log(Level.INFO, "Opening view: {0}", view.getClass().getSimpleName());

        // In a real implementation, we would integrate the view into the content panel
        // For simplicity, we just show the view as a separate window
        view.setVisible(true);
    }

    /**
     * Exits the application after confirmation.
     */
    private void exitApplication() {
        LOGGER.info("Exit application requested");

        int result = JOptionPane.showConfirmDialog(
                mainFrame,
                "¿Está seguro que desea salir de la aplicación?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            LOGGER.info("Application exit confirmed");
            mainFrame.dispose();
            System.exit(0);
        }
    }
}