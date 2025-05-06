/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.parts.views;

import com.carmotorsproject.parts.controller.InventoryReportController;
import com.carmotorsproject.parts.controller.PartController;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PartDAO;
import com.carmotorsproject.parts.model.PurchaseOrder;
import com.carmotorsproject.parts.model.PurchaseOrderDetail;
import com.carmotorsproject.parts.model.Supplier;
import com.carmotorsproject.parts.model.SupplierDAO;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class PartView extends JFrame {
    private PartController controller;
    private InventoryReportController reportController;
    private SupplierDAO supplierDAO;
    private JTextField inputName;
    private JComboBox<String> inputType;
    private JTextField inputCompatibleMakeModel;
    private JComboBox<String> inputSupplier;
    private JSpinner inputQuantityInStock;
    private JSpinner inputMinimumStock;
    private JDateChooser inputEntryDate;
    private JDateChooser inputEstimatedLifespan;
    private JComboBox<String> inputStatus;
    private JTextField inputBatchId;
    private JTextField inputSearch;
    private JTable tableParts;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public PartView() {
        this.controller = new PartController(this);
        this.reportController = new InventoryReportController();
        this.supplierDAO = new SupplierDAO();
        initComponents();
        populateSuppliers();
        refreshTable();
    }

    private void initComponents() {
        setTitle("Inventory Management - Engines and Wheels");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout());

        // Panel de formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Spare Part Name");
        inputName = new JTextField(20);
        JLabel lblType = new JLabel("Tipo");
        inputType = new JComboBox<>(new String[]{"Mechanical", "Electrical", "Body", "Consumable"});
        JLabel lblCompatibleMakeModel = new JLabel("Modelo Compatible");
        inputCompatibleMakeModel = new JTextField(20);
        JLabel lblSupplier = new JLabel("Supplier");
        inputSupplier = new JComboBox<>();
        JLabel lblQuantityInStock = new JLabel("Quantity in Stock");
        inputQuantityInStock = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        JLabel lblMinimumStock = new JLabel("Minimum Stock");
        inputMinimumStock = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        JLabel lblEntryDate = new JLabel("Date of Entry");
        inputEntryDate = new JDateChooser();
        JLabel lblEstimatedLifespan = new JLabel("Estimated Life");
        inputEstimatedLifespan = new JDateChooser();
        JLabel lblStatus = new JLabel("State");
        inputStatus = new JComboBox<>(new String[]{"Available", "Reserved", "Out_of_service"});
        JLabel lblBatchId = new JLabel("ID Lote");
        inputBatchId = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblName, gbc);
        gbc.gridx = 1;
        formPanel.add(inputName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblType, gbc);
        gbc.gridx = 1;
        formPanel.add(inputType, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblCompatibleMakeModel, gbc);
        gbc.gridx = 1;
        formPanel.add(inputCompatibleMakeModel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblSupplier, gbc);
        gbc.gridx = 1;
        formPanel.add(inputSupplier, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(lblQuantityInStock, gbc);
        gbc.gridx = 1;
        formPanel.add(inputQuantityInStock, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(lblMinimumStock, gbc);
        gbc.gridx = 1;
        formPanel.add(inputMinimumStock, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(lblEntryDate, gbc);
        gbc.gridx = 1;
        formPanel.add(inputEntryDate, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(lblEstimatedLifespan, gbc);
        gbc.gridx = 1;
        formPanel.add(inputEstimatedLifespan, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(lblStatus, gbc);
        gbc.gridx = 1;
        formPanel.add(inputStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 9;
        formPanel.add(lblBatchId, gbc);
        gbc.gridx = 1;
        formPanel.add(inputBatchId, gbc);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel();
        JLabel lblSearch = new JLabel("Search by Name:");
        inputSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        searchPanel.add(lblSearch);
        searchPanel.add(inputSearch);
        searchPanel.add(btnSearch);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add Spare Part");
        JButton btnUpdate = new JButton("Update Spare Part");
        JButton btnDelete = new JButton("Delete Spare");
        JButton btnPurchaseOrder = new JButton("Generate Purchase Order");
        JButton btnStatusReport = new JButton("Report by State");
        JButton btnStockReport = new JButton("Critical Stock Report");
        JButton btnExpirationReport = new JButton("Expiration Report");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnPurchaseOrder);
        buttonPanel.add(btnStatusReport);
        buttonPanel.add(btnStockReport);
        buttonPanel.add(btnExpirationReport);

        // Tabla de partes
        tableParts = new JTable();
        tableParts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableParts.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableParts.getSelectedRow();
                if (selectedRow >= 0) {
                    loadPartForEdit(selectedRow);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tableParts);

        mainPanel.add(formPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(searchPanel, java.awt.BorderLayout.CENTER);
        mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        mainPanel.add(scrollPane, java.awt.BorderLayout.EAST);

        setContentPane(mainPanel);

        btnAdd.addActionListener(e -> addPart());
        btnUpdate.addActionListener(e -> updatePart());
        btnDelete.addActionListener(e -> deletePart());
        btnSearch.addActionListener(e -> searchPart());
        btnPurchaseOrder.addActionListener(e -> generatePurchaseOrder());
        btnStatusReport.addActionListener(e -> showStatusReport());
        btnStockReport.addActionListener(e -> showCriticalStockReport());
        btnExpirationReport.addActionListener(e -> showExpirationReport());
    }

    private void populateSuppliers() {
        inputSupplier.removeAllItems();
        inputSupplier.addItem("Ninguno");
        for (Supplier supplier : supplierDAO.findAll()) {
            inputSupplier.addItem(supplier.getSupplierId() + " - " + supplier.getName());
        }
    }

    private void addPart() {
        String name = inputName.getText().trim();
        String type = (String) inputType.getSelectedItem();
        String compatibleMakeModel = inputCompatibleMakeModel.getText().trim();
        Integer supplierId = getSelectedSupplierId();
        int quantityInStock = (Integer) inputQuantityInStock.getValue();
        int minimumStock = (Integer) inputMinimumStock.getValue();
        Date entryDate = inputEntryDate.getDate();
        Date estimatedLifespan = inputEstimatedLifespan.getDate();
        String status = (String) inputStatus.getSelectedItem();
        String batchId = inputBatchId.getText().trim();

        if (name.isEmpty() || type == null || status == null) {
            showAlert("Please complete the required fields (Name, Type, Status).");
            return;
        }

        Part part = new Part(0, name, type, compatibleMakeModel, supplierId, quantityInStock, minimumStock,
                entryDate, estimatedLifespan, status, batchId, new Date(), new Date());
        controller.addPart(part);
        checkStockAlert(part);
        checkExpirationAlert(part);
        clearFields();
    }

    private void updatePart() {
        int selectedRow = tableParts.getSelectedRow();
        if (selectedRow < 0) {
            showAlert("Please select a spare part to upgrade.");
            return;
        }

        int partId = (int) tableParts.getValueAt(selectedRow, 0);
        String name = inputName.getText().trim();
        String type = (String) inputType.getSelectedItem();
        String compatibleMakeModel = inputCompatibleMakeModel.getText().trim();
        Integer supplierId = getSelectedSupplierId();
        int quantityInStock = (Integer) inputQuantityInStock.getValue();
        int minimumStock = (Integer) inputMinimumStock.getValue();
        Date entryDate = inputEntryDate.getDate();
        Date estimatedLifespan = inputEstimatedLifespan.getDate();
        String status = (String) inputStatus.getSelectedItem();
        String batchId = inputBatchId.getText().trim();

        if (name.isEmpty() || type == null || status == null) {
            showAlert("Please complete the required fields (Name, Type, Status).");
            return;
        }

        Part part = controller.getAllParts().stream()
                .filter(p -> p.getPartId() == partId)
                .findFirst()
                .orElse(null);
        if (part == null) {
            showAlert("Spare part not found.");
            return;
        }

        part.setName(name);
        part.setType(type);
        part.setCompatibleMakeModel(compatibleMakeModel);
        part.setSupplierId(supplierId);
        part.setQuantityInStock(quantityInStock);
        part.setMinimumStock(minimumStock);
        part.setEntryDate(entryDate);
        part.setEstimatedLifespan(estimatedLifespan);
        part.setStatus(status);
        part.setBatchId(batchId);
        part.setLastUpdateDate(new Date());

        controller.updatePart(part);
        checkStockAlert(part);
        checkExpirationAlert(part);
        clearFields();
    }

    private void deletePart() {
        int selectedRow = tableParts.getSelectedRow();
        if (selectedRow < 0) {
            showAlert("Please select a spare part to remove.");
            return;
        }

        int partId = (int) tableParts.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this spare part?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deletePart(partId);
            clearFields();
        }
    }

    private void searchPart() {
        String searchTerm = inputSearch.getText().trim();
        List<Part> parts;
        if (searchTerm.isEmpty()) {
            parts = controller.getAllParts();
        } else {
            parts = controller.searchPart(searchTerm);
        }
        updateTable(parts);
    }

    private void loadPartForEdit(int row) {
        int partId = (int) tableParts.getValueAt(row, 0);
        Part part = controller.getAllParts().stream()
                .filter(p -> p.getPartId() == partId)
                .findFirst()
                .orElse(null);
        if (part == null) {
            showAlert("Spare part not found.");
            return;
        }

        inputName.setText(part.getName());
        inputType.setSelectedItem(part.getType());
        inputCompatibleMakeModel.setText(part.getCompatibleMakeModel());
        setSupplierSelection(part.getSupplierId());
        inputQuantityInStock.setValue(part.getQuantityInStock());
        inputMinimumStock.setValue(part.getMinimumStock());
        inputEntryDate.setDate(part.getEntryDate());
        inputEstimatedLifespan.setDate(part.getEstimatedLifespan());
        inputStatus.setSelectedItem(part.getStatus());
        inputBatchId.setText(part.getBatchId());
    }

    private void setSupplierSelection(Integer supplierId) {
        if (supplierId == null) {
            inputSupplier.setSelectedItem("None");
            return;
        }
        for (int i = 0; i < inputSupplier.getItemCount(); i++) {
            String item = inputSupplier.getItemAt(i);
            if (item.startsWith(supplierId + " - ")) {
                inputSupplier.setSelectedItem(item);
                return;
            }
        }
        inputSupplier.setSelectedItem("None");
    }

    private Integer getSelectedSupplierId() {
        String selected = (String) inputSupplier.getSelectedItem();
        if (selected == null || selected.equals("None")) {
            return null;
        }
        try {
            return Integer.parseInt(selected.split(" - ")[0]);
        } catch (NumberFormatException e) {
            showAlert("Error parsing vendor ID: " + e.getMessage());
            return null;
        }
    }

    public void refreshTable() {
        List<Part> parts = controller.getAllParts();
        updateTable(parts);
        if (parts.isEmpty()) {
            showAlert("There are no spare parts listed in inventory. Add a spare part to get started.");
        } else {
            checkExpirationAlerts(parts);
        }
    }

    private void updateTable(List<Part> parts) {
        String[] columns = {"ID", "Name", "Type", "Compatible Model", "Supplier", "Stock", "Minimum Stock", "Entry Date", "Estimated Life", "Status", "Batch ID", "Creation Date", "Last Update"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Part part : parts) {
            Supplier supplier = part.getSupplierId() != null ? supplierDAO.findById(part.getSupplierId()) : null;
            String supplierName = supplier != null ? supplier.getName() : "None";
            model.addRow(new Object[]{
                part.getPartId(),
                part.getName(),
                part.getType(),
                part.getCompatibleMakeModel(),
                supplierName,
                part.getQuantityInStock(),
                part.getMinimumStock(),
                part.getEntryDate() != null ? dateFormat.format(part.getEntryDate()) : "",
                part.getEstimatedLifespan() != null ? dateFormat.format(part.getEstimatedLifespan()) : "",
                part.getStatus(),
                part.getBatchId(),
                part.getCreationDate() != null ? dateFormat.format(part.getCreationDate()) : "",
                part.getLastUpdateDate() != null ? dateFormat.format(part.getLastUpdateDate()) : ""
            });
        }
        tableParts.setModel(model);
    }

    private void clearFields() {
        inputName.setText("");
        inputType.setSelectedIndex(0);
        inputCompatibleMakeModel.setText("");
        inputSupplier.setSelectedIndex(0);
        inputQuantityInStock.setValue(0);
        inputMinimumStock.setValue(1);
        inputEntryDate.setDate(null);
        inputEstimatedLifespan.setDate(null);
        inputStatus.setSelectedIndex(0);
        inputBatchId.setText("");
        inputSearch.setText("");
        tableParts.clearSelection();
    }

    private void checkStockAlert(Part part) {
        if (part.getQuantityInStock() <= part.getMinimumStock()) {
            showAlert("¡Alerta! El stock de '" + part.getName() + "'is below the minimum level (" + part.getMinimumStock() + "). Consider replenishing.");
        }
    }

    private void checkExpirationAlert(Part part) {
        if (part.getEstimatedLifespan() == null) return;

        Date today = new Date();
        long diffInMillies = part.getEstimatedLifespan().getTime() - today.getTime();
        long daysUntilExpiration = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if (daysUntilExpiration < 0) {
            showAlert("Alert! The spare part'" + part.getName() + "' has expired" + dateFormat.format(part.getEstimatedLifespan()) + ".Please consider removing it from inventory.");
        } else if (daysUntilExpiration <= 30) {
            showAlert("Alert! The spare part'" + part.getName() + "'is about to expire (" + daysUntilExpiration + "days remaining). Expiration date:" + dateFormat.format(part.getEstimatedLifespan()) + ".");
        }
    }

    private void checkExpirationAlerts(List<Part> parts) {
        Date today = new Date();
        StringBuilder expirationAlerts = new StringBuilder();
        for (Part part : parts) {
            if (part.getEstimatedLifespan() == null) continue;

            long diffInMillies = part.getEstimatedLifespan().getTime() - today.getTime();
            long daysUntilExpiration = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (daysUntilExpiration < 0) {
                expirationAlerts.append("The spare part'").append(part.getName()).append("' has expired")
                        .append(dateFormat.format(part.getEstimatedLifespan())).append(".\n");
            } else if (daysUntilExpiration <= 30) {
                expirationAlerts.append("The spare part'").append(part.getName()).append("'is about to expire(")
                        .append(daysUntilExpiration).append("days remaining). Expiration date: ")
                        .append(dateFormat.format(part.getEstimatedLifespan())).append(".\n");
            }
        }
        if (!expirationAlerts.isEmpty()) {
            showAlert("Expiration Alerts:\n" + expirationAlerts.toString());
        }
    }

    private void generatePurchaseOrder() {
        List<Part> parts = controller.getAllParts();
        List<Part> lowStockParts = parts.stream()
                .filter(part -> part.getQuantityInStock() <= part.getMinimumStock())
                .toList();

        if (lowStockParts.isEmpty()) {
            showAlert("There are no low-stock spare parts that require a purchase order.");
            return;
        }

        List<PurchaseOrder> orders = new ArrayList<>();
        lowStockParts.stream()
                .collect(java.util.stream.Collectors.groupingBy(Part::getSupplierId))
                .forEach((supplierId, supplierParts) -> {
                    List<PurchaseOrderDetail> details = new ArrayList<>();
                    double totalAmount = 0;
                    for (Part part : supplierParts) {
                        int quantityNeeded = part.getMinimumStock() - part.getQuantityInStock() + 10;
                        if (quantityNeeded <= 0) quantityNeeded = 10;
                        double unitPrice = 100.0;
                        double subtotal = quantityNeeded * unitPrice;
                        details.add(new PurchaseOrderDetail(0, 0, part.getPartId(), quantityNeeded, unitPrice, subtotal));
                        totalAmount += subtotal;
                    }
                    Date now = new Date();
                    PurchaseOrder order = new PurchaseOrder(0, now, "Pending", supplierId, totalAmount, now, now, details);
                    orders.add(order);
                });

        PurchaseOrderView poView = new PurchaseOrderView();
        for (PurchaseOrder order : orders) {
            poView.addPurchaseOrder(order);
        }
        poView.setVisible(true);
        poView.refreshTable();
        showAlert("Purchase orders generated and saved successfully. Review the Purchase Orders module.");
    }

    private void showStatusReport() {
        String reportText = reportController.generateStatusReport();
        JTextArea textArea = new JTextArea(reportText);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "State Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showCriticalStockReport() {
        String reportText = reportController.generateCriticalStockReport();
        JTextArea textArea = new JTextArea(reportText);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Critical Stock Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showExpirationReport() {
        String reportText = reportController.generateExpirationReport();
        JTextArea textArea = new JTextArea(reportText);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Expiration Report", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}