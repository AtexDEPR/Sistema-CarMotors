/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.views;

import com.carmotorsproject.services.controller.ServiceController;
import com.carmotorsproject.services.model.ServiceStatus;
import com.carmotorsproject.utils.ReportUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Swing UI for generating service reports.
 */
public class ServiceReportView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(ServiceReportView.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Controller
    private final ServiceController controller;

    // UI Components
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    // Filter Components
    private JComboBox<String> reportTypeComboBox;
    private JComboBox<ServiceStatus> statusComboBox;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField vehicleIdField;
    private JTextField technicianIdField;

    // Buttons
    private JButton generateButton;
    private JButton exportButton;
    private JButton closeButton;

    /**
     * Constructor that initializes the UI and controller.
     */
    public ServiceReportView() {
        this.controller = new ServiceController();
        initComponents();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        // Set up the frame
        setTitle("Service Reports");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the filter panel
        JPanel filterPanel = createFilterPanel();
        mainPanel.add(filterPanel, BorderLayout.NORTH);

        // Create the table panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Create the button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);
    }

    /**
     * Creates the filter panel.
     *
     * @return The filter panel
     */
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Report Filters"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Report Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Report Type:"), gbc);

        gbc.gridx = 1;
        String[] reportTypes = {
                "All Services",
                "Services by Status",
                "Services by Date Range",
                "Services by Vehicle",
                "Services by Technician",
                "Revenue by Month",
                "Parts Usage"
        };
        reportTypeComboBox = new JComboBox<>(reportTypes);
        reportTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFilterFields();
            }
        });
        panel.add(reportTypeComboBox, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        statusComboBox = new JComboBox<>(ServiceStatus.values());
        statusComboBox.setEnabled(false);
        panel.add(statusComboBox, gbc);

        // Start Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Start Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        startDateField = new JTextField(10);
        startDateField.setEnabled(false);
        panel.add(startDateField, gbc);

        // End Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("End Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        endDateField = new JTextField(10);
        endDateField.setEnabled(false);
        panel.add(endDateField, gbc);

        // Vehicle ID
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Vehicle ID:"), gbc);

        gbc.gridx = 1;
        vehicleIdField = new JTextField(5);
        vehicleIdField.setEnabled(false);
        panel.add(vehicleIdField, gbc);

        // Technician ID
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Technician ID:"), gbc);

        gbc.gridx = 1;
        technicianIdField = new JTextField(5);
        technicianIdField.setEnabled(false);
        panel.add(technicianIdField, gbc);

        // Generate Button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        generateButton = new JButton("Generate Report");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        panel.add(generateButton, gbc);

        return panel;
    }

    /**
     * Creates the table panel.
     *
     * @return The table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Report Results"));

        // Create the table model
        reportTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create the table
        reportTable = new JTable(reportTableModel);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.getTableHeader().setReorderingAllowed(false);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setPreferredSize(new Dimension(850, 300));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the button panel.
     *
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        exportButton = new JButton("Export to PDF");
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToPDF();
            }
        });
        panel.add(exportButton);

        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(closeButton);

        return panel;
    }

    /**
     * Updates the filter fields based on the selected report type.
     */
    private void updateFilterFields() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();

        // Disable all filter fields
        statusComboBox.setEnabled(false);
        startDateField.setEnabled(false);
        endDateField.setEnabled(false);
        vehicleIdField.setEnabled(false);
        technicianIdField.setEnabled(false);

        // Enable relevant filter fields based on report type
        if (reportType.equals("Services by Status")) {
            statusComboBox.setEnabled(true);
        } else if (reportType.equals("Services by Date Range") || reportType.equals("Revenue by Month")) {
            startDateField.setEnabled(true);
            endDateField.setEnabled(true);
        } else if (reportType.equals("Services by Vehicle")) {
            vehicleIdField.setEnabled(true);
        } else if (reportType.equals("Services by Technician")) {
            technicianIdField.setEnabled(true);
        }
    }

    /**
     * Generates a report based on the selected filters.
     */
    private void generateReport() {
        try {
            String reportType = (String) reportTypeComboBox.getSelectedItem();

            // Clear the table
            reportTableModel.setRowCount(0);
            reportTableModel.setColumnCount(0);

            // Generate the appropriate report
            if (reportType.equals("All Services")) {
                generateAllServicesReport();
            } else if (reportType.equals("Services by Status")) {
                generateServicesByStatusReport();
            } else if (reportType.equals("Services by Date Range")) {
                generateServicesByDateRangeReport();
            } else if (reportType.equals("Services by Vehicle")) {
                generateServicesByVehicleReport();
            } else if (reportType.equals("Services by Technician")) {
                generateServicesByTechnicianReport();
            } else if (reportType.equals("Revenue by Month")) {
                generateRevenueByMonthReport();
            } else if (reportType.equals("Parts Usage")) {
                generatePartsUsageReport();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error generating report", ex);
            JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Generates a report of all services.
     */
    private void generateAllServicesReport() {
        // Add columns to the table model
        reportTableModel.addColumn("Service ID");
        reportTableModel.addColumn("Vehicle ID");
        reportTableModel.addColumn("Technician ID");
        reportTableModel.addColumn("Type");
        reportTableModel.addColumn("Status");
        reportTableModel.addColumn("Start Date");
        reportTableModel.addColumn("End Date");
        reportTableModel.addColumn("Labor Cost");
        reportTableModel.addColumn("Parts Cost");
        reportTableModel.addColumn("Total Cost");

        // Get the data from the controller
        controller.generateAllServicesReport(reportTableModel);
    }

    /**
     * Generates a report of services by status.
     */
    private void generateServicesByStatusReport() {
        // Get the selected status
        ServiceStatus status = (ServiceStatus) statusComboBox.getSelectedItem();

        // Add columns to the table model
        reportTableModel.addColumn("Service ID");
        reportTableModel.addColumn("Vehicle ID");
        reportTableModel.addColumn("Technician ID");
        reportTableModel.addColumn("Type");
        reportTableModel.addColumn("Start Date");
        reportTableModel.addColumn("End Date");
        reportTableModel.addColumn("Labor Cost");
        reportTableModel.addColumn("Parts Cost");
        reportTableModel.addColumn("Total Cost");

        // Get the data from the controller
        controller.generateServicesByStatusReport(reportTableModel, status);
    }

    /**
     * Generates a report of services by date range.
     */
    private void generateServicesByDateRangeReport() {
        // Get the date range
        Date startDate;
        Date endDate;

        try {
            startDate = DATE_FORMAT.parse(startDateField.getText().trim());
            endDate = DATE_FORMAT.parse(endDateField.getText().trim());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid dates (yyyy-MM-dd).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add columns to the table model
        reportTableModel.addColumn("Service ID");
        reportTableModel.addColumn("Vehicle ID");
        reportTableModel.addColumn("Technician ID");
        reportTableModel.addColumn("Type");
        reportTableModel.addColumn("Status");
        reportTableModel.addColumn("Start Date");
        reportTableModel.addColumn("End Date");
        reportTableModel.addColumn("Labor Cost");
        reportTableModel.addColumn("Parts Cost");
        reportTableModel.addColumn("Total Cost");

        // Get the data from the controller
        controller.generateServicesByDateRangeReport(reportTableModel, startDate, endDate);
    }

    /**
     * Generates a report of services by vehicle.
     */
    private void generateServicesByVehicleReport() {
        // Get the vehicle ID
        int vehicleId;

        try {
            vehicleId = Integer.parseInt(vehicleIdField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid vehicle ID.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add columns to the table model
        reportTableModel.addColumn("Service ID");
        reportTableModel.addColumn("Technician ID");
        reportTableModel.addColumn("Type");
        reportTableModel.addColumn("Status");
        reportTableModel.addColumn("Start Date");
        reportTableModel.addColumn("End Date");
        reportTableModel.addColumn("Labor Cost");
        reportTableModel.addColumn("Parts Cost");
        reportTableModel.addColumn("Total Cost");

        // Get the data from the controller
        controller.generateServicesByVehicleReport(reportTableModel, vehicleId);
    }

    /**
     * Generates a report of services by technician.
     */
    private void generateServicesByTechnicianReport() {
        // Get the technician ID
        int technicianId;

        try {
            technicianId = Integer.parseInt(technicianIdField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid technician ID.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add columns to the table model
        reportTableModel.addColumn("Service ID");
        reportTableModel.addColumn("Vehicle ID");
        reportTableModel.addColumn("Type");
        reportTableModel.addColumn("Status");
        reportTableModel.addColumn("Start Date");
        reportTableModel.addColumn("End Date");
        reportTableModel.addColumn("Labor Cost");
        reportTableModel.addColumn("Parts Cost");
        reportTableModel.addColumn("Total Cost");

        // Get the data from the controller
        controller.generateServicesByTechnicianReport(reportTableModel, technicianId);
    }

    /**
     * Generates a report of revenue by month.
     */
    private void generateRevenueByMonthReport() {
        // Get the date range
        Date startDate;
        Date endDate;

        try {
            startDate = DATE_FORMAT.parse(startDateField.getText().trim());
            endDate = DATE_FORMAT.parse(endDateField.getText().trim());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid dates (yyyy-MM-dd).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add columns to the table model
        reportTableModel.addColumn("Month");
        reportTableModel.addColumn("Year");
        reportTableModel.addColumn("Number of Services");
        reportTableModel.addColumn("Labor Revenue");
        reportTableModel.addColumn("Parts Revenue");
        reportTableModel.addColumn("Total Revenue");

        // Get the data from the controller
        controller.generateRevenueByMonthReport(reportTableModel, startDate, endDate);
    }

    /**
     * Generates a report of parts usage.
     */
    private void generatePartsUsageReport() {
        // Add columns to the table model
        reportTableModel.addColumn("Part ID");
        reportTableModel.addColumn("Part Name");
        reportTableModel.addColumn("Total Quantity Used");
        reportTableModel.addColumn("Total Revenue");
        reportTableModel.addColumn("Number of Services");

        // Get the data from the controller
        controller.generatePartsUsageReport(reportTableModel);
    }

    /**
     * Exports the report to a PDF file.
     */
    private void exportToPDF() {
        try {
            // Check if there is data to export
            if (reportTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No data to export.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the report type
            String reportType = (String) reportTypeComboBox.getSelectedItem();

            // Create a file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF Report");
            fileChooser.setSelectedFile(new java.io.File(reportType.replace(" ", "_") + "_Report.pdf"));

            // Show the save dialog
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();

                // Ensure the file has a .pdf extension
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                    fileToSave = new java.io.File(filePath);
                }

                // Create a map of parameters for the report
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("ReportTitle", reportType + " Report");
                parameters.put("GeneratedDate", new Date());

                // Export the report to PDF
                ReportUtil.exportTableToPDF(reportTable, fileToSave, parameters);

                // Show success message
                JOptionPane.showMessageDialog(this, "Report exported successfully to " + filePath,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error exporting report to PDF", ex);
            JOptionPane.showMessageDialog(this, "Error exporting report to PDF: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}