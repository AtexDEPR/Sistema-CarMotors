/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.views;

import com.carmotorsproject.services.controller.ServiceController;
import com.carmotorsproject.services.model.CustomerSatisfaction;
import com.carmotorsproject.services.model.DeliveryOrder;
import com.carmotorsproject.services.model.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Swing UI for managing delivery orders.
 */
public class DeliveryOrderView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(DeliveryOrderView.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Controller
    private final ServiceController controller;

    // Service ID
    private final int serviceId;

    // Form Components
    private JTextField deliveryDateField;
    private JTextField deliveredByField;
    private JTextField receivedByField;
    private JComboBox<CustomerSatisfaction> satisfactionComboBox;
    private JTextArea feedbackArea;
    private JCheckBox followUpCheckBox;
    private JCheckBox signatureCheckBox;
    private JTextArea notesArea;

    // Table Components
    private JTable deliveryOrderTable;
    private DefaultTableModel tableModel;

    // Buttons
    private JButton saveButton;
    private JButton cancelButton;

    /**
     * Constructor that initializes the UI and controller.
     *
     * @param serviceId The ID of the service for which to create a delivery order
     */
    public DeliveryOrderView(int serviceId) {
        this.serviceId = serviceId;
        this.controller = new ServiceController();
        initComponents();
        loadServiceDetails();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        // Set up the frame
        setTitle("Create Delivery Order");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Create the button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);

        // Initialize table model for delivery orders
        String[] columnNames = {"ID", "Service ID", "Delivery Date", "Delivered By", "Received By", "Satisfaction", "Follow-up"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        deliveryOrderTable = new JTable(tableModel);
    }

    /**
     * Creates the form panel.
     *
     * @return The form panel
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Delivery Order Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Delivery Date
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Delivery Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        deliveryDateField = new JTextField(10);
        deliveryDateField.setText(DATE_FORMAT.format(new Date())); // Default to current date
        panel.add(deliveryDateField, gbc);

        // Delivered By
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Delivered By:"), gbc);

        gbc.gridx = 1;
        deliveredByField = new JTextField(20);
        panel.add(deliveredByField, gbc);

        // Received By
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Received By:"), gbc);

        gbc.gridx = 1;
        receivedByField = new JTextField(20);
        panel.add(receivedByField, gbc);

        // Customer Satisfaction
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Customer Satisfaction:"), gbc);

        gbc.gridx = 1;
        satisfactionComboBox = new JComboBox<>(CustomerSatisfaction.values());
        panel.add(satisfactionComboBox, gbc);

        // Customer Feedback
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Customer Feedback:"), gbc);

        gbc.gridx = 1;
        feedbackArea = new JTextArea(3, 20);
        feedbackArea.setLineWrap(true);
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackArea);
        panel.add(feedbackScrollPane, gbc);

        // Follow-up Required
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Follow-up Required:"), gbc);

        gbc.gridx = 1;
        followUpCheckBox = new JCheckBox();
        panel.add(followUpCheckBox, gbc);

        // Signature
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Customer Signature:"), gbc);

        gbc.gridx = 1;
        signatureCheckBox = new JCheckBox("I confirm the vehicle has been delivered and received");
        panel.add(signatureCheckBox, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        notesArea = new JTextArea(3, 20);
        notesArea.setLineWrap(true);
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        panel.add(notesScrollPane, gbc);

        return panel;
    }

    /**
     * Creates the button panel.
     *
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        saveButton = new JButton("Save Delivery Order");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDeliveryOrder();
            }
        });
        panel.add(saveButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(cancelButton);

        return panel;
    }

    /**
     * Loads the service details.
     */
    private void loadServiceDetails() {
        try {
            Service service = controller.getServiceById(serviceId);
            if (service == null) {
                JOptionPane.showMessageDialog(this, "Service not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error loading service details", ex);
            JOptionPane.showMessageDialog(this, "Error loading service details: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    /**
     * Saves the delivery order.
     */
    private void saveDeliveryOrder() {
        try {
            // Check if the signature checkbox is checked
            if (!signatureCheckBox.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please confirm the delivery with a signature.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create a new DeliveryOrder object from form data
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setServiceId(serviceId);

            // Get the delivery date
            try {
                deliveryOrder.setDeliveryDate(DATE_FORMAT.parse(deliveryDateField.getText().trim()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid delivery date (yyyy-MM-dd).",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the delivered by
            String deliveredBy = deliveredByField.getText().trim();
            if (deliveredBy.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter who delivered the vehicle.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            deliveryOrder.setDeliveredBy(deliveredBy);

            // Get the received by
            String receivedBy = receivedByField.getText().trim();
            if (receivedBy.isEmpty()) {
                // Fix: Corrected the showMessageDialog call
                JOptionPane.showMessageDialog(this, "Please enter who received the vehicle.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            deliveryOrder.setReceivedBy(receivedBy);

            // Get the customer satisfaction
            // Fix: Use direct CustomerSatisfaction enum from the package
            CustomerSatisfaction satisfaction = (CustomerSatisfaction) satisfactionComboBox.getSelectedItem();
            deliveryOrder.setCustomerSatisfaction(satisfaction);

            // Get the customer feedback
            deliveryOrder.setCustomerFeedback(feedbackArea.getText().trim());

            // Get the follow-up required
            deliveryOrder.setFollowUpRequired(followUpCheckBox.isSelected());

            // Get the notes
            deliveryOrder.setNotes(notesArea.getText().trim());

            // Call the controller to save the delivery order
            controller.createDeliveryOrder(deliveryOrder);

            // Show success message
            JOptionPane.showMessageDialog(this, "Delivery order created successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Close the window
            dispose();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error saving delivery order", ex);
            JOptionPane.showMessageDialog(this, "Error saving delivery order: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates the delivery order table with the provided list of delivery orders.
     *
     * @param orders The list of delivery orders to display
     */
    public void updateDeliveryOrderTable(List<DeliveryOrder> orders) {
        LOGGER.log(Level.INFO, "Updating delivery order table with {0} orders", orders.size());

        // Clear the table
        tableModel.setRowCount(0);

        // Add each delivery order to the table
        for (DeliveryOrder order : orders) {
            Object[] rowData = {
                    order.getDeliveryOrderId(),
                    order.getServiceId(),
                    DATE_FORMAT.format(order.getDeliveryDate()),
                    order.getDeliveredBy(),
                    order.getReceivedBy(),
                    order.getCustomerSatisfaction(),
                    order.isFollowUpRequired() ? "Yes" : "No"
            };
            tableModel.addRow(rowData);
        }

        // Notify the table that the data has changed
        tableModel.fireTableDataChanged();
    }

    /**
     * Populates the form fields with data from the provided delivery order.
     *
     * @param order The delivery order to display
     */
    public void populateDeliveryOrderForm(DeliveryOrder order) {
        LOGGER.log(Level.INFO, "Populating form with delivery order ID: {0}", order.getDeliveryOrderId());

        // Set the delivery date
        if (order.getDeliveryDate() != null) {
            deliveryDateField.setText(DATE_FORMAT.format(order.getDeliveryDate()));
        } else {
            deliveryDateField.setText(DATE_FORMAT.format(new Date()));
        }

        // Set the delivered by
        deliveredByField.setText(order.getDeliveredBy());

        // Set the received by
        receivedByField.setText(order.getReceivedBy());

        // Set the customer satisfaction
        satisfactionComboBox.setSelectedItem(order.getCustomerSatisfaction());

        // Set the customer feedback
        feedbackArea.setText(order.getCustomerFeedback());

        // Set the follow-up required
        followUpCheckBox.setSelected(order.isFollowUpRequired());

        // Set the notes
        notesArea.setText(order.getNotes());

        // Since we're editing an existing order, assume signature was already confirmed
        signatureCheckBox.setSelected(true);
    }
}