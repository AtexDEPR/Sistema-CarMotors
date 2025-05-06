/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.views;

import com.carmotorsproject.services.controller.TechnicianController;
import com.carmotorsproject.services.model.Technician;
import com.carmotorsproject.services.model.TechnicianSpecialty;
import com.carmotorsproject.services.model.TechnicianStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Swing UI for managing technicians.
 */
public class TechnicianView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(TechnicianView.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Controller
    private final TechnicianController controller;

    // UI Components
    private JTable technicianTable;
    private DefaultTableModel technicianTableModel;

    // Form Components
    private JTextField nameField;
    private JTextField lastNameField;
    private JComboBox<TechnicianSpecialty> specialtyComboBox;
    private JComboBox<TechnicianStatus> statusComboBox;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField hireDateField;
    private JTextField hourlyRateField;
    private JTextField certificationsField;
    private JTextArea notesArea;

    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton;

    // Selected ID
    private int selectedTechnicianId = 0;

    /**
     * Constructor that initializes the UI and controller.
     */
    public TechnicianView() {
        this.controller = new TechnicianController(this);
        initComponents();
        loadData();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        // Set up the frame
        setTitle("Technician Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the table panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.NORTH);

        // Create the form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Create the button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);
    }

    /**
     * Creates the table panel.
     *
     * @return The table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Technicians"));

        // Create the table model
        technicianTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add columns to the table model
        technicianTableModel.addColumn("ID");
        technicianTableModel.addColumn("Name");
        technicianTableModel.addColumn("Last Name");
        technicianTableModel.addColumn("Specialty");
        technicianTableModel.addColumn("Status");
        technicianTableModel.addColumn("Phone");
        technicianTableModel.addColumn("Email");
        technicianTableModel.addColumn("Hire Date");
        technicianTableModel.addColumn("Hourly Rate");
        technicianTableModel.addColumn("Certifications");

        // Create the table
        technicianTable = new JTable(technicianTableModel);
        technicianTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        technicianTable.getTableHeader().setReorderingAllowed(false);

        // Add a mouse listener to the table
        technicianTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = technicianTable.getSelectedRow();
                if (row >= 0) {
                    selectedTechnicianId = (int) technicianTableModel.getValueAt(row, 0);
                    controller.loadTechnicianDetails(selectedTechnicianId);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(technicianTable);
        scrollPane.setPreferredSize(new Dimension(850, 200));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the form panel.
     *
     * @return The form panel
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Technician Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        lastNameField = new JTextField(20);
        panel.add(lastNameField, gbc);

        // Specialty
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Specialty:"), gbc);

        gbc.gridx = 1;
        specialtyComboBox = new JComboBox<>(TechnicianSpecialty.values());
        panel.add(specialtyComboBox, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        statusComboBox = new JComboBox<>(TechnicianStatus.values());
        panel.add(statusComboBox, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(15);
        panel.add(phoneField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(30);
        panel.add(emailField, gbc);

        // Hire Date
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Hire Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        hireDateField = new JTextField(10);
        hireDateField.setText(DATE_FORMAT.format(new Date())); // Default to current date
        panel.add(hireDateField, gbc);

        // Hourly Rate
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Hourly Rate:"), gbc);

        gbc.gridx = 1;
        hourlyRateField = new JTextField(10);
        panel.add(hourlyRateField, gbc);

        // Certifications
        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(new JLabel("Certifications:"), gbc);

        gbc.gridx = 1;
        certificationsField = new JTextField(30);
        panel.add(certificationsField, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        notesArea = new JTextArea(3, 30);
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

        addButton = new JButton("Add Technician");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTechnician();
            }
        });
        panel.add(addButton);

        updateButton = new JButton("Update Technician");
        updateButton.setEnabled(false);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTechnician();
            }
        });
        panel.add(updateButton);

        deleteButton = new JButton("Delete Technician");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTechnician();
            }
        });
        panel.add(deleteButton);

        clearButton = new JButton("Clear Form");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        panel.add(clearButton);

        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTechnicians();
            }
        });
        panel.add(searchButton);

        return panel;
    }

    /**
     * Loads data into the table.
     */
    public void loadData() {
        controller.loadTechnicians();
    }

    /**
     * Adds a new technician.
     */
    private void addTechnician() {
        try {
            // Create a new Technician object from form data
            Technician technician = getTechnicianFromForm();

            // Call the controller to add the technician
            controller.addTechnician(technician);

            // Clear the form
            clearForm();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error adding technician", ex);
            JOptionPane.showMessageDialog(this, "Error adding technician: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates an existing technician.
     */
    private void updateTechnician() {
        try {
            // Create a Technician object from form data
            Technician technician = getTechnicianFromForm();
            technician.setTechnicianId(selectedTechnicianId);

            // Call the controller to update the technician
            controller.updateTechnician(technician);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating technician", ex);
            JOptionPane.showMessageDialog(this, "Error updating technician: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a technician.
     */
    private void deleteTechnician() {
        try {
            // Confirm deletion
            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this technician?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                // Call the controller to delete the technician
                controller.deleteTechnician(selectedTechnicianId);

                // Clear the form
                clearForm();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error deleting technician", ex);
            JOptionPane.showMessageDialog(this, "Error deleting technician: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Searches for technicians by name.
     */
    private void searchTechnicians() {
        try {
            String name = nameField.getText().trim();
            String lastName = lastNameField.getText().trim();

            if (name.isEmpty() && lastName.isEmpty()) {
                // If both fields are empty, load all technicians
                controller.loadTechnicians();
            } else {
                // Search by name or last name
                String searchTerm = name.isEmpty() ? lastName : name;
                controller.searchTechniciansByName(searchTerm);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error searching technicians", ex);
            JOptionPane.showMessageDialog(this, "Error searching technicians: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the form.
     */
    private void clearForm() {
        // Reset form fields
        nameField.setText("");
        lastNameField.setText("");
        specialtyComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        phoneField.setText("");
        emailField.setText("");
        hireDateField.setText(DATE_FORMAT.format(new Date()));
        hourlyRateField.setText("");
        certificationsField.setText("");
        notesArea.setText("");

        // Reset selected ID
        selectedTechnicianId = 0;

        // Disable buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    /**
     * Creates a Technician object from the form data.
     *
     * @return A Technician object populated with form data
     * @throws ParseException If a date parsing error occurs
     */
    private Technician getTechnicianFromForm() throws ParseException {
        Technician technician = new Technician();

        // Get the name
        String name = nameField.getText().trim();
        if (!name.isEmpty()) {
            technician.setName(name);
        } else {
            throw new IllegalArgumentException("Please enter a name.");
        }

        // Get the last name
        String lastName = lastNameField.getText().trim();
        if (!lastName.isEmpty()) {
            technician.setLastName(lastName);
        } else {
            throw new IllegalArgumentException("Please enter a last name.");
        }

        // Get the specialty
        TechnicianSpecialty specialty = (TechnicianSpecialty) specialtyComboBox.getSelectedItem();
        technician.setSpecialty(specialty);

        // Get the status
        TechnicianStatus status = (TechnicianStatus) statusComboBox.getSelectedItem();
        technician.setStatus(status.name());

        // Get the phone
        technician.setPhone(phoneField.getText().trim());

        // Get the email
        technician.setEmail(emailField.getText().trim());

        // Get the hire date
        String hireDateStr = hireDateField.getText().trim();
        if (!hireDateStr.isEmpty()) {
            technician.setHireDate(DATE_FORMAT.parse(hireDateStr));
        } else {
            technician.setHireDate(new Date()); // Default to current date
        }

        // Get the hourly rate
        String hourlyRateStr = hourlyRateField.getText().trim();
        if (!hourlyRateStr.isEmpty()) {
            try {
                double hourlyRate = Double.parseDouble(hourlyRateStr);
                if (hourlyRate <= 0) {
                    throw new IllegalArgumentException("Hourly rate must be greater than zero.");
                }
                technician.setHourlyRate(hourlyRate);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Please enter a valid hourly rate.");
            }
        } else {
            throw new IllegalArgumentException("Please enter an hourly rate.");
        }

        // Get the certifications
        technician.setCertifications(certificationsField.getText().trim());

        // Get the notes
        technician.setNotes(notesArea.getText().trim());

        return technician;
    }

    /**
     * Updates the technician table with the provided technicians.
     *
     * @param technicians The list of technicians to display
     */
    public void updateTechnicianTable(List<Technician> technicians) {
        // Clear the table
        technicianTableModel.setRowCount(0);

        // Add technicians to the table
        for (Technician technician : technicians) {
            Vector<Object> row = new Vector<>();
            row.add(technician.getTechnicianId());
            row.add(technician.getName());
            row.add(technician.getLastName());
            row.add(technician.getSpecialty());
            row.add(technician.getStatus());
            row.add(technician.getPhone());
            row.add(technician.getEmail());
            row.add(technician.getHireDate() != null ? DATE_FORMAT.format(technician.getHireDate()) : "");
            row.add(technician.getHourlyRate());
            row.add(technician.getCertifications());

            technicianTableModel.addRow(row);
        }
    }

    /**
     * Populates the form with the provided technician.
     *
     * @param technician The technician to display
     */
    public void populateTechnicianForm(Technician technician) {
        nameField.setText(technician.getName());
        lastNameField.setText(technician.getLastName());
        specialtyComboBox.setSelectedItem(technician.getSpecialty());

        // Set the status
        for (TechnicianStatus status : TechnicianStatus.values()) {
            if (status.name().equals(technician.getStatus())) {
                statusComboBox.setSelectedItem(status);
                break;
            }
        }

        phoneField.setText(technician.getPhone());
        emailField.setText(technician.getEmail());

        if (technician.getHireDate() != null) {
            hireDateField.setText(DATE_FORMAT.format(technician.getHireDate()));
        } else {
            hireDateField.setText("");
        }

        hourlyRateField.setText(String.valueOf(technician.getHourlyRate()));
        certificationsField.setText(technician.getCertifications());
        notesArea.setText(technician.getNotes());
    }
}