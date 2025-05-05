/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.campaigns.views;

import com.carmotorsproject.campaigns.controller.CampaignController;
import com.carmotorsproject.campaigns.model.Campaign;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * View for managing campaigns.
 * Provides a UI to create, read, update, and delete campaigns.
 */
public class CampaignView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(CampaignView.class.getName());

    // Controller
    private CampaignController controller;

    // UI Components
    private JPanel contentPane;
    private JTable campaignTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JFormattedTextField startDateField;
    private JFormattedTextField endDateField;
    private JSpinner discountSpinner;
    private JComboBox<String> vehicleTypeComboBox;
    private JComboBox<String> customerSegmentComboBox;
    private JComboBox<String> statusComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JButton clearSearchButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton reportButton;

    // Current campaign ID
    private int currentCampaignId = -1;

    // Date format
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Constructor that initializes the view.
     */
    public CampaignView() {
        initComponents();
        setupLayout();
        setupListeners();

        setTitle("Campaign Management");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        LOGGER.log(Level.INFO, "CampaignView initialized");
    }

    /**
     * Sets the controller for this view.
     *
     * @param controller The campaign controller
     */
    public void setController(CampaignController controller) {
        this.controller = controller;
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // Table
        String[] columnNames = {"ID", "Name", "Start Date", "End Date", "Discount %", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        campaignTable = new JTable(tableModel);
        campaignTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Form fields
        nameField = new JTextField(20);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        startDateField = new JFormattedTextField(dateFormat);
        startDateField.setColumns(10);

        endDateField = new JFormattedTextField(dateFormat);
        endDateField.setColumns(10);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.0, 0.0, 100.0, 1.0);
        discountSpinner = new JSpinner(spinnerModel);

        vehicleTypeComboBox = new JComboBox<>(new String[]{"ALL", "SEDAN", "SUV", "TRUCK", "VAN", "SPORTS"});
        customerSegmentComboBox = new JComboBox<>(new String[]{"ALL", "REGULAR", "PREMIUM", "VIP"});
        statusComboBox = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE", "PLANNED", "COMPLETED"});

        // Search components
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        clearSearchButton = new JButton("Clear");

        // Action buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear Form");
        reportButton = new JButton("Reports");

        // Initially disable update and delete buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    /**
     * Sets up the layout of the UI components.
     */
    private void setupLayout() {
        contentPane.setLayout(new BorderLayout(10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearSearchButton);

        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(campaignTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 200));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Campaign Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(nameField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(descriptionScrollPane, gbc);

        // Start Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Start Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(startDateField, gbc);

        // End Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("End Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(endDateField, gbc);

        // Discount
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Discount %:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(discountSpinner, gbc);

        // Vehicle Type
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Vehicle Type:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(vehicleTypeComboBox, gbc);

        // Customer Segment
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Customer Segment:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(customerSegmentComboBox, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(statusComboBox, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(reportButton);

        // Main layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(tableScrollPane, BorderLayout.CENTER);

        contentPane.add(topPanel, BorderLayout.CENTER);
        contentPane.add(formPanel, BorderLayout.EAST);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets up the event listeners for the UI components.
     */
    private void setupListeners() {
        // Table selection listener
        campaignTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = campaignTable.getSelectedRow();
                if (row >= 0) {
                    int campaignId = (int) tableModel.getValueAt(row, 0);
                    controller.loadCampaignDetails(campaignId);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        // Search button
        searchButton.addActionListener((ActionEvent e) -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                controller.searchByName(searchTerm);
            } else {
                controller.loadCampaigns();
            }
        });

        // Clear search button
        clearSearchButton.addActionListener((ActionEvent e) -> {
            searchField.setText("");
            controller.loadCampaigns();
        });

        // Add button
        addButton.addActionListener((ActionEvent e) -> {
            try {
                Campaign campaign = getCampaignFromForm();
                controller.addCampaign(campaign);
                clearForm();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error adding campaign", ex);
                showError("Error adding campaign: " + ex.getMessage());
            }
        });

        // Update button
        updateButton.addActionListener((ActionEvent e) -> {
            try {
                Campaign campaign = getCampaignFromForm();
                campaign.setCampaignId(currentCampaignId);
                controller.updateCampaign(campaign);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error updating campaign", ex);
                showError("Error updating campaign: " + ex.getMessage());
            }
        });

        // Delete button
        deleteButton.addActionListener((ActionEvent e) -> {
            if (currentCampaignId >= 0) {
                int option = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this campaign?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    controller.deleteCampaign(currentCampaignId);
                    clearForm();
                }
            }
        });

        // Clear button
        clearButton.addActionListener((ActionEvent e) -> {
            clearForm();
        });

        // Report button
        reportButton.addActionListener((ActionEvent e) -> {
            controller.openReportView();
        });
    }

    /**
     * Updates the campaign table with the provided list of campaigns.
     *
     * @param campaigns The list of campaigns to display
     */
    public void updateCampaignTable(List<Campaign> campaigns) {
        tableModel.setRowCount(0);

        for (Campaign campaign : campaigns) {
            Object[] row = {
                    campaign.getCampaignId(),
                    campaign.getName(),
                    campaign.getStartDate() != null ? dateFormat.format(campaign.getStartDate()) : "",
                    campaign.getEndDate() != null ? dateFormat.format(campaign.getEndDate()) : "",
                    campaign.getDiscountPercentage(),
                    campaign.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Populates the form with the details of a campaign.
     *
     * @param campaign The campaign to display
     */
    public void populateCampaignForm(Campaign campaign) {
        currentCampaignId = campaign.getCampaignId();

        nameField.setText(campaign.getName());
        descriptionArea.setText(campaign.getDescription());

        if (campaign.getStartDate() != null) {
            startDateField.setValue(campaign.getStartDate());
        } else {
            startDateField.setValue(null);
        }

        if (campaign.getEndDate() != null) {
            endDateField.setValue(campaign.getEndDate());
        } else {
            endDateField.setValue(null);
        }

        discountSpinner.setValue(campaign.getDiscountPercentage());

        if (campaign.getTargetVehicleType() != null) {
            vehicleTypeComboBox.setSelectedItem(campaign.getTargetVehicleType());
        } else {
            vehicleTypeComboBox.setSelectedIndex(0);
        }

        customerSegmentComboBox.setSelectedIndex(campaign.getTargetCustomerSegment());

        if (campaign.getStatus() != null) {
            statusComboBox.setSelectedItem(campaign.getStatus());
        } else {
            statusComboBox.setSelectedIndex(0);
        }

        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    /**
     * Clears the form fields.
     */
    private void clearForm() {
        currentCampaignId = -1;

        nameField.setText("");
        descriptionArea.setText("");
        startDateField.setValue(null);
        endDateField.setValue(null);
        discountSpinner.setValue(0.0);
        vehicleTypeComboBox.setSelectedIndex(0);
        customerSegmentComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    /**
     * Gets a campaign object from the form fields.
     *
     * @return A campaign object
     * @throws ParseException If there is an error parsing the dates
     */
    private Campaign getCampaignFromForm() throws ParseException {
        Campaign campaign = new Campaign();

        campaign.setName(nameField.getText().trim());
        campaign.setDescription(descriptionArea.getText().trim());

        if (startDateField.getValue() != null) {
            campaign.setStartDate((Date) startDateField.getValue());
        }

        if (endDateField.getValue() != null) {
            campaign.setEndDate((Date) endDateField.getValue());
        }

        campaign.setDiscountPercentage((Double) discountSpinner.getValue());
        campaign.setTargetVehicleType((String) vehicleTypeComboBox.getSelectedItem());
        campaign.setTargetCustomerSegment(customerSegmentComboBox.getSelectedIndex());
        campaign.setStatus((String) statusComboBox.getSelectedItem());

        // Set audit fields
        String username = System.getProperty("user.name");
        Date now = new Date();

        if (currentCampaignId < 0) {
            // New campaign
            campaign.setCreatedBy(username);
            campaign.setCreatedDate(now);
        }

        campaign.setLastModifiedBy(username);
        campaign.setLastModifiedDate(now);

        return campaign;
    }

    /**
     * Shows an error message.
     *
     * @param message The error message
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows an information message.
     *
     * @param message The information message
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a success message.
     *
     * @param message The success message
     */
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}