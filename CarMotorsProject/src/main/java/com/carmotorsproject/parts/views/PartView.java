/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.parts.views;

import com.carmotorsproject.parts.controller.PartController;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PartType;
import com.carmotorsproject.parts.model.PartStatus;
import com.carmotorsproject.parts.model.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Swing UI for managing parts.
 * Provides a user interface for viewing, adding, editing, and deleting parts.
 */
public class PartView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(PartView.class.getName());

    // Controller
    private final PartController controller;

    // UI Components
    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;
    private JPanel searchPanel;
    private JPanel tablePanel;

    // Form fields
    private JTextField txtPartId;
    private JTextField txtName;
    private JTextField txtDescription;
    private JTextField txtPartNumber;
    private JComboBox<PartType> cmbType;
    private JComboBox<PartStatus> cmbStatus;
    private JTextField txtPurchasePrice;
    private JTextField txtSellingPrice;
    private JTextField txtStockQuantity;
    private JTextField txtMinStockLevel;
    private JTextField txtLocation;
    private JTextField txtSupplierId;

    // Search fields
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchType;

    // Table
    private JTable tblParts;
    private DefaultTableModel tableModel;

    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnSearch;
    private JButton btnShowAll;
    private JButton btnLowStock;

    /**
     * Constructor that initializes the UI and controller.
     */
    public PartView() {
        // Initialize controller
        this.controller = new PartController(this);

        // Set up the frame
        setTitle("Parts Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize UI components
        initComponents();

        // Load initial data
        loadAllParts();
    }

    /**
     * Initializes all UI components.
     */
    private void initComponents() {
        // Main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create panels
        createFormPanel();
        createButtonPanel();
        createSearchPanel();
        createTablePanel();

        // Add panels to main panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(tablePanel, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    /**
     * Creates the form panel with input fields.
     */
    private void createFormPanel() {
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Part Details"));
        formPanel.setPreferredSize(new Dimension(300, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Part ID (hidden/disabled)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Part ID:"), gbc);

        gbc.gridx = 1;
        txtPartId = new JTextField(10);
        txtPartId.setEditable(false);
        formPanel.add(txtPartId, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        txtName = new JTextField(20);
        formPanel.add(txtName, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        txtDescription = new JTextField(20);
        formPanel.add(txtDescription, gbc);

        // Part Number
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Part Number:"), gbc);

        gbc.gridx = 1;
        txtPartNumber = new JTextField(15);
        formPanel.add(txtPartNumber, gbc);

        // Type
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Type:"), gbc);

        gbc.gridx = 1;
        cmbType = new JComboBox<>(PartType.values());
        formPanel.add(cmbType, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        cmbStatus = new JComboBox<>(PartStatus.values());
        formPanel.add(cmbStatus, gbc);

        // Purchase Price
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Purchase Price:"), gbc);

        gbc.gridx = 1;
        txtPurchasePrice = new JTextField(10);
        formPanel.add(txtPurchasePrice, gbc);

        // Selling Price
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Selling Price:"), gbc);

        gbc.gridx = 1;
        txtSellingPrice = new JTextField(10);
        formPanel.add(txtSellingPrice, gbc);

        // Stock Quantity
        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(new JLabel("Stock Quantity:"), gbc);

        gbc.gridx = 1;
        txtStockQuantity = new JTextField(10);
        formPanel.add(txtStockQuantity, gbc);

        // Min Stock Level
        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(new JLabel("Min Stock Level:"), gbc);

        gbc.gridx = 1;
        txtMinStockLevel = new JTextField(10);
        formPanel.add(txtMinStockLevel, gbc);

        // Location
        gbc.gridx = 0;
        gbc.gridy = 10;
        formPanel.add(new JLabel("Location:"), gbc);

        gbc.gridx = 1;
        txtLocation = new JTextField(15);
        formPanel.add(txtLocation, gbc);

        // Supplier ID
        gbc.gridx = 0;
        gbc.gridy = 11;
        formPanel.add(new JLabel("Supplier ID:"), gbc);

        gbc.gridx = 1;
        txtSupplierId = new JTextField(10);
        formPanel.add(txtSupplierId, gbc);
    }

    /**
     * Creates the button panel with action buttons.
     */
    private void createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        // Style buttons
        styleButton(btnAdd, new Color(46, 204, 113)); // Green
        styleButton(btnUpdate, new Color(52, 152, 219)); // Blue
        styleButton(btnDelete, new Color(231, 76, 60)); // Red
        styleButton(btnClear, new Color(149, 165, 166)); // Gray

        // Add action listeners
        btnAdd.addActionListener(e -> controller.addPart());
        btnUpdate.addActionListener(e -> controller.updatePart());
        btnDelete.addActionListener(e -> controller.deletePart());
        btnClear.addActionListener(e -> clearForm());

        // Add buttons to panel
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
    }

    /**
     * Creates the search panel with search controls.
     */
    private void createSearchPanel() {
        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));

        // Search field
        txtSearch = new JTextField(20);

        // Search type combo box
        String[] searchTypes = {"Name", "Part Number", "Type", "Supplier ID"};
        cmbSearchType = new JComboBox<>(searchTypes);

        // Search button
        btnSearch = new JButton("Search");
        styleButton(btnSearch, new Color(52, 152, 219)); // Blue
        btnSearch.addActionListener(e -> controller.searchParts(
                txtSearch.getText(),
                cmbSearchType.getSelectedItem().toString()));

        // Show all button
        btnShowAll = new JButton("Show All");
        styleButton(btnShowAll, new Color(52, 73, 94)); // Dark blue
        btnShowAll.addActionListener(e -> loadAllParts());

        // Low stock button
        btnLowStock = new JButton("Low Stock");
        styleButton(btnLowStock, new Color(230, 126, 34)); // Orange
        btnLowStock.addActionListener(e -> controller.findLowStock());

        // Add components to panel
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(new JLabel("By:"));
        searchPanel.add(cmbSearchType);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);
        searchPanel.add(btnLowStock);
    }

    /**
     * Creates the table panel with parts table.
     */
    private void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Parts List"));

        // Create table model with column names
        String[] columnNames = {
                "ID", "Name", "Part Number", "Type", "Status",
                "Purchase Price", "Selling Price", "Stock", "Min Stock", "Location", "Supplier ID"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        // Create table with model
        tblParts = new JTable(tableModel);

        // Add row selection listener
        tblParts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblParts.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFormFromTable(selectedRow);
                    btnDelete.setEnabled(true); // Enable Delete button
                    btnUpdate.setEnabled(true); // Enable Update button
                } else {
                    btnDelete.setEnabled(false); // Disable Delete button
                    btnUpdate.setEnabled(false); // Disable Update button
                }
            }
        });

        // Add sorting capability
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tblParts.setRowSorter(sorter);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(tblParts);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Styles a button with the specified color.
     *
     * @param button The button to style
     * @param color The background color
     */
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(100, 30));
    }

    /**
     * Loads all parts from the database and displays them in the table.
     */
    public void loadAllParts() {
        controller.loadAllParts();
        btnDelete.setEnabled(false); // Disable Delete button initially
        btnUpdate.setEnabled(false); // Disable Update button initially
    }

    /**
     * Updates the table with the provided list of parts.
     *
     * @param parts The list of parts to display
     */
    public void updateTable(List<Part> parts) {
        // Clear the table
        tableModel.setRowCount(0);

        // Add parts to the table
        for (Part part : parts) {
            Object[] row = {
                    part.getId(),
                    part.getName(),
                    part.getPartNumber(),
                    part.getType(),
                    part.getStatus(),
                    part.getPurchasePrice(),
                    part.getSellingPrice(),
                    part.getStockQuantity(),
                    part.getMinStockLevel(),
                    part.getLocation(),
                    part.getSupplierId()
            };
            tableModel.addRow(row);
        }

        LOGGER.log(Level.INFO, "Table updated with {0} parts", parts.size());
    }

    /**
     * Populates the form fields with data from the selected table row.
     *
     * @param row The selected row index
     */
    private void populateFormFromTable(int row) {
        try {
            LOGGER.log(Level.INFO, "Populating form from table row: {0}", row);

            // Convert view row to model row (in case of sorting)
            int modelRow = tblParts.convertRowIndexToModel(row);

            // Populate fields
            txtPartId.setText(tblParts.getValueAt(row, 0).toString());
            txtName.setText(tblParts.getValueAt(row, 1) != null ? tblParts.getValueAt(row, 1).toString() : "");
            txtPartNumber.setText(tblParts.getValueAt(row, 2) != null ? tblParts.getValueAt(row, 2).toString() : "");

            // Handle PartType
            try {
                String typeStr = tblParts.getValueAt(row, 3) != null ? tblParts.getValueAt(row, 3).toString() : "";
                PartType type = PartType.valueOf(typeStr);
                cmbType.setSelectedItem(type);
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Invalid PartType in table: {0}", tblParts.getValueAt(row, 3));
                cmbType.setSelectedIndex(0); // Default to first item
            }

            // Handle PartStatus
            try {
                String statusStr = tblParts.getValueAt(row, 4) != null ? tblParts.getValueAt(row, 4).toString() : "";
                PartStatus status = PartStatus.valueOf(statusStr);
                cmbStatus.setSelectedItem(status);
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Invalid PartStatus in table: {0}", tblParts.getValueAt(row, 4));
                cmbStatus.setSelectedIndex(0); // Default to first item
            }

            txtPurchasePrice.setText(tblParts.getValueAt(row, 5) != null ? tblParts.getValueAt(row, 5).toString() : "0.0");
            txtSellingPrice.setText(tblParts.getValueAt(row, 6) != null ? tblParts.getValueAt(row, 6).toString() : "0.0");
            txtStockQuantity.setText(tblParts.getValueAt(row, 7) != null ? tblParts.getValueAt(row, 7).toString() : "0");
            txtMinStockLevel.setText(tblParts.getValueAt(row, 8) != null ? tblParts.getValueAt(row, 8).toString() : "0");
            txtLocation.setText(tblParts.getValueAt(row, 9) != null ? tblParts.getValueAt(row, 9).toString() : "");
            txtSupplierId.setText(tblParts.getValueAt(row, 10) != null ? tblParts.getValueAt(row, 10).toString() : "");

            // Load description from controller
            try {
                int partId = Integer.parseInt(txtPartId.getText());
                controller.loadPartDescription(partId);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid Part ID for description: {0}", txtPartId.getText());
                txtDescription.setText("");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error populating form from table", e);
            showError("Error loading part data: " + e.getMessage());
        }
    }

    /**
     * Sets the description field value.
     *
     * @param description The description to set
     */
    public void setDescription(String description) {
        txtDescription.setText(description != null ? description : "");
    }

    /**
     * Clears all form fields.
     */
    public void clearForm() {
        txtPartId.setText("");
        txtName.setText("");
        txtDescription.setText("");
        txtPartNumber.setText("");
        cmbType.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        txtPurchasePrice.setText("");
        txtSellingPrice.setText("");
        txtStockQuantity.setText("");
        txtMinStockLevel.setText("");
        txtLocation.setText("");
        txtSupplierId.setText("");

        // Clear table selection
        tblParts.clearSelection();
        btnDelete.setEnabled(false); // Disable Delete button
        btnUpdate.setEnabled(false); // Disable Update button
    }

    /**
     * Creates a Part object from the form fields.
     *
     * @return A Part object with data from the form, or null if validation fails
     */
    public Part getPartFromForm() {
        Part part = new Part();
        try {
            String idText = txtPartId.getText().trim();
            part.setPartId(idText.isEmpty() ? 0 : Integer.parseInt(idText));
            part.setName(txtName.getText().trim());
            part.setDescription(txtDescription.getText().trim());
            part.setPartNumber(txtPartNumber.getText().trim());
            part.setType((PartType) cmbType.getSelectedItem());
            part.setStatus((PartStatus) cmbStatus.getSelectedItem());

            // Validate numeric fields
            String purchasePriceText = txtPurchasePrice.getText().trim();
            if (purchasePriceText.isEmpty()) {
                showError("Purchase price is required.");
                return null;
            }
            part.setPurchasePrice(Double.parseDouble(purchasePriceText));

            String sellingPriceText = txtSellingPrice.getText().trim();
            if (sellingPriceText.isEmpty()) {
                showError("Selling price is required.");
                return null;
            }
            part.setSellingPrice(Double.parseDouble(sellingPriceText));

            String stockQuantityText = txtStockQuantity.getText().trim();
            if (stockQuantityText.isEmpty()) {
                showError("Stock quantity is required.");
                return null;
            }
            part.setStockQuantity(Integer.parseInt(stockQuantityText));

            String minStockLevelText = txtMinStockLevel.getText().trim();
            if (minStockLevelText.isEmpty()) {
                showError("Minimum stock level is required.");
                return null;
            }
            part.setMinStockLevel(Integer.parseInt(minStockLevelText));

            part.setLocation(txtLocation.getText().trim());

            // Validate and set supplier_id
            String supplierIdText = txtSupplierId.getText().trim();
            if (supplierIdText.isEmpty()) {
                showError("Supplier ID is required.");
                return null;
            }
            int supplierId = Integer.parseInt(supplierIdText);
            if (supplierId <= 0) {
                showError("Please enter a valid supplier ID.");
                return null;
            }
            part.setSupplierId(supplierId);

            return part;
        } catch (NumberFormatException e) {
            showError("Please enter valid numeric values for ID, prices, stock, minimum stock, or supplier ID.");
            return null;
        }
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to display
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays an information message.
     *
     * @param message The information message to display
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a confirmation dialog.
     *
     * @param message The confirmation message to display
     * @return true if the user confirms, false otherwise
     */
    public boolean showConfirm(String message) {
        int result = JOptionPane.showConfirmDialog(this, message,
                "Confirmation", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}