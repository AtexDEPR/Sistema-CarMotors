/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.views;

import com.carmotorsproject.customers.controller.LoyaltyProgramController;
import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.customers.model.CustomerLevel;
import com.carmotorsproject.customers.model.LoyaltyProgram;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Swing UI for managing loyalty programs.
 * Provides a graphical interface for viewing and managing customer loyalty programs.
 */
public class LoyaltyProgramView extends JFrame {

    // Controller
    private LoyaltyProgramController controller;

    // UI Components
    private JTable programTable;
    private DefaultTableModel tableModel;
    private JComboBox<Customer> cmbCustomer;
    private JTextField txtPoints;
    private JComboBox<CustomerLevel> cmbLevel;
    private JTextField txtEnrollmentDate;
    private JTextField txtLastUpdateDate;
    private JCheckBox chkActive;
    private JTextArea txtNotes;
    private JButton btnEnroll;
    private JButton btnUpdate;
    private JButton btnAddPoints;
    private JButton btnRedeemPoints;
    private JButton btnClear;
    private JTextField txtPointsToAddRedeem;

    // Selected program ID
    private int selectedProgramId = 0;

    // Date formatter
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Constructor that initializes the view.
     */
    public LoyaltyProgramView() {
        initComponents();
        controller = new LoyaltyProgramController(this);
        loadPrograms();
        loadCustomers();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        setTitle("Loyalty Program Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = createFormPanel();

        // Table panel
        JPanel tablePanel = createTablePanel();

        // Points panel
        JPanel pointsPanel = createPointsPanel();

        // Button panel
        JPanel buttonPanel = createButtonPanel();

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(pointsPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Add action listeners
        addActionListeners();
    }

    /**
     * Creates the form panel.
     *
     * @return The form panel
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Loyalty Program Details"));
        formPanel.setPreferredSize(new Dimension(350, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Customer
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Customer:"), gbc);

        gbc.gridx = 1;
        cmbCustomer = new JComboBox<>();
        formPanel.add(cmbCustomer, gbc);

        // Points
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Points:"), gbc);

        gbc.gridx = 1;
        txtPoints = new JTextField(10);
        txtPoints.setEditable(false);
        formPanel.add(txtPoints, gbc);

        // Level
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Level:"), gbc);

        gbc.gridx = 1;
        cmbLevel = new JComboBox<>(CustomerLevel.values());
        cmbLevel.setEnabled(false); // Level is determined by points
        formPanel.add(cmbLevel, gbc);

        // Enrollment Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Enrollment Date:"), gbc);

        gbc.gridx = 1;
        txtEnrollmentDate = new JTextField(10);
        txtEnrollmentDate.setEditable(false);
        formPanel.add(txtEnrollmentDate, gbc);

        // Last Update Date
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Last Update:"), gbc);

        gbc.gridx = 1;
        txtLastUpdateDate = new JTextField(10);
        txtLastUpdateDate.setEditable(false);
        formPanel.add(txtLastUpdateDate, gbc);

        // Active
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Active:"), gbc);

        gbc.gridx = 1;
        chkActive = new JCheckBox();
        chkActive.setSelected(true);
        formPanel.add(chkActive, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        gbc.gridheight = 2;
        txtNotes = new JTextArea(4, 20);
        JScrollPane notesScrollPane = new JScrollPane(txtNotes);
        formPanel.add(notesScrollPane, gbc);

        return formPanel;
    }

    /**
     * Creates the table panel.
     *
     * @return The table panel
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Loyalty Program List"));

        // Create table model
        String[] columnNames = {"ID", "Customer", "Points", "Level", "Enrollment Date", "Active"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Create table
        programTable = new JTable(tableModel);
        programTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        programTable.getTableHeader().setReorderingAllowed(false);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(programTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    /**
     * Creates the points panel.
     *
     * @return The points panel
     */
    private JPanel createPointsPanel() {
        JPanel pointsPanel = new JPanel(new GridBagLayout());
        pointsPanel.setBorder(BorderFactory.createTitledBorder("Points Management"));
        pointsPanel.setPreferredSize(new Dimension(250, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Points to Add/Redeem
        gbc.gridx = 0;
        gbc.gridy = 0;
        pointsPanel.add(new JLabel("Points:"), gbc);

        gbc.gridx = 1;
        txtPointsToAddRedeem = new JTextField(10);
        pointsPanel.add(txtPointsToAddRedeem, gbc);

        // Add Points button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        btnAddPoints = new JButton("Add Points");
        pointsPanel.add(btnAddPoints, gbc);

        // Redeem Points button
        gbc.gridx = 0;
        gbc.gridy = 2;
        btnRedeemPoints = new JButton("Redeem Points");
        pointsPanel.add(btnRedeemPoints, gbc);

        // Level information
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel levelInfoPanel = createLevelInfoPanel();
        pointsPanel.add(levelInfoPanel, gbc);

        return pointsPanel;
    }

    /**
     * Creates the level information panel.
     *
     * @return The level information panel
     */
    private JPanel createLevelInfoPanel() {
        JPanel levelInfoPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        levelInfoPanel.setBorder(BorderFactory.createTitledBorder("Level Information"));

        // Add level information
        levelInfoPanel.add(new JLabel("Level"));
        levelInfoPanel.add(new JLabel("Min Points"));

        for (CustomerLevel level : CustomerLevel.values()) {
            levelInfoPanel.add(new JLabel(level.name()));
            levelInfoPanel.add(new JLabel(String.valueOf(level.getMinPoints())));
        }

        return levelInfoPanel;
    }

    /**
     * Creates the button panel.
     *
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Enroll button
        btnEnroll = new JButton("Enroll Customer");
        buttonPanel.add(btnEnroll);

        // Update button
        btnUpdate = new JButton("Update Program");
        buttonPanel.add(btnUpdate);

        // Clear button
        btnClear = new JButton("Clear");
        buttonPanel.add(btnClear);

        return buttonPanel;
    }

    /**
     * Adds action listeners to the buttons and table.
     */
    private void addActionListeners() {
        // Enroll button
        btnEnroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enrollCustomer();
            }
        });

        // Update button
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProgram();
            }
        });

        // Add Points button
        btnAddPoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPoints();
            }
        });

        // Redeem Points button
        btnRedeemPoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redeemPoints();
            }
        });

        // Clear button
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // Table row selection
        programTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = programTable.getSelectedRow();
                if (row >= 0) {
                    selectedProgramId = (int) programTable.getValueAt(row, 0);
                    controller.loadProgramDetails(selectedProgramId);
                }
            }
        });
    }

    /**
     * Loads all loyalty programs and updates the table.
     */
    public void loadPrograms() {
        controller.loadPrograms();
    }

    /**
     * Loads all customers for the customer combo box.
     */
    public void loadCustomers() {
        controller.loadCustomers();
    }

    /**
     * Updates the loyalty program table with the provided list of programs.
     *
     * @param programs The list of loyalty programs to display
     */
    public void updateProgramTable(List<LoyaltyProgram> programs) {
        // Clear the table
        tableModel.setRowCount(0);

        // Add programs to the table
        for (LoyaltyProgram program : programs) {
            Object[] row = {
                    program.getProgramId(),
                    controller.getCustomerName(program.getCustomerId()),
                    program.getAccumulatedPoints(),
                    program.getLevel(),
                    dateFormat.format(program.getEnrollmentDate()),
                    program.isActive()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Updates the customer combo box with the provided list of customers.
     *
     * @param customers The list of customers to display
     */
    public void updateCustomerComboBox(List<Customer> customers) {
        cmbCustomer.removeAllItems();
        for (Customer customer : customers) {
            cmbCustomer.addItem(customer);
        }
    }

    /**
     * Populates the form with the details of a loyalty program.
     *
     * @param program The loyalty program to display
     */
    public void populateProgramForm(LoyaltyProgram program) {
        selectedProgramId = program.getProgramId();

        // Set customer in combo box
        for (int i = 0; i < cmbCustomer.getItemCount(); i++) {
            Customer customer = cmbCustomer.getItemAt(i);
            if (customer.getCustomerId() == program.getCustomerId()) {
                cmbCustomer.setSelectedIndex(i);
                break;
            }
        }

        txtPoints.setText(String.valueOf(program.getAccumulatedPoints()));
        cmbLevel.setSelectedItem(program.getLevel());
        txtEnrollmentDate.setText(dateFormat.format(program.getEnrollmentDate()));
        txtLastUpdateDate.setText(dateFormat.format(program.getLastUpdateDate()));
        chkActive.setSelected(program.isActive());
        txtNotes.setText(program.getNotes());

        // Disable customer selection for existing programs
        cmbCustomer.setEnabled(false);
    }

    /**
     * Clears the form.
     */
    public void clearForm() {
        selectedProgramId = 0;
        cmbCustomer.setSelectedIndex(cmbCustomer.getItemCount() > 0 ? 0 : -1);
        cmbCustomer.setEnabled(true);
        txtPoints.setText("0");
        cmbLevel.setSelectedItem(CustomerLevel.BRONZE);
        txtEnrollmentDate.setText(dateFormat.format(new Date()));
        txtLastUpdateDate.setText(dateFormat.format(new Date()));
        chkActive.setSelected(true);
        txtNotes.setText("");
        txtPointsToAddRedeem.setText("");
        programTable.clearSelection();
    }

    /**
     * Enrolls a customer in the loyalty program.
     */
    private void enrollCustomer() {
        if (cmbCustomer.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create a new loyalty program from form data
            LoyaltyProgram program = getProgramFromForm();

            // Enroll the customer
            controller.enrollCustomer(program);

            // Clear the form
            clearForm();

            // Show success message
            JOptionPane.showMessageDialog(this, "Customer enrolled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error enrolling customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates an existing loyalty program.
     */
    private void updateProgram() {
        if (selectedProgramId == 0) {
            JOptionPane.showMessageDialog(this, "Please select a loyalty program to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create a loyalty program from form data
            LoyaltyProgram program = getProgramFromForm();
            program.setProgramId(selectedProgramId);

            // Update the program
            controller.updateProgram(program);

            // Clear the form
            clearForm();

            // Show success message
            JOptionPane.showMessageDialog(this, "Loyalty program updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating loyalty program: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds points to a loyalty program.
     */
    private void addPoints() {
        if (selectedProgramId == 0) {
            JOptionPane.showMessageDialog(this, "Please select a loyalty program.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Get points to add
            int points = Integer.parseInt(txtPointsToAddRedeem.getText().trim());
            if (points <= 0) {
                JOptionPane.showMessageDialog(this, "Points must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add points
            controller.addPoints(selectedProgramId, points);

            // Clear points field
            txtPointsToAddRedeem.setText("");

            // Show success message
            JOptionPane.showMessageDialog(this, points + " points added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of points.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding points: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Redeems points from a loyalty program.
     */
    private void redeemPoints() {
        if (selectedProgramId == 0) {
            JOptionPane.showMessageDialog(this, "Please select a loyalty program.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Get points to redeem
            int points = Integer.parseInt(txtPointsToAddRedeem.getText().trim());
            if (points <= 0) {
                JOptionPane.showMessageDialog(this, "Points must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Redeem points
            boolean redeemed = controller.redeemPoints(selectedProgramId, points);

            if (redeemed) {
                // Clear points field
                txtPointsToAddRedeem.setText("");

                // Show success message
                JOptionPane.showMessageDialog(this, points + " points redeemed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Not enough points to redeem.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of points.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error redeeming points: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates a LoyaltyProgram object from the form data.
     *
     * @return A LoyaltyProgram object with data from the form
     */
    private LoyaltyProgram getProgramFromForm() {
        LoyaltyProgram program = new LoyaltyProgram();

        // Set program properties from form fields
        Customer selectedCustomer = (Customer) cmbCustomer.getSelectedItem();
        program.setCustomerId(selectedCustomer.getCustomerId());

        if (!txtPoints.getText().trim().isEmpty()) {
            program.setAccumulatedPoints(Integer.parseInt(txtPoints.getText().trim()));
        } else {
            program.setAccumulatedPoints(0);
        }

        program.setLevel((CustomerLevel) cmbLevel.getSelectedItem());
        program.setActive(chkActive.isSelected());
        program.setNotes(txtNotes.getText().trim());

        // Set enrollment date to current date for new programs
        if (selectedProgramId == 0) {
            program.setEnrollmentDate(new Date());
            program.setLastUpdateDate(new Date());
        }

        return program;
    }

    /**
     * Shows an error message.
     *
     * @param message The error message to show
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows an information message.
     *
     * @param message The information message to show
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main method for testing the view.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoyaltyProgramView().setVisible(true);
            }
        });
    }
}