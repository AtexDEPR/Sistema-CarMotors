/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.parts.views;

import com.carmotorsproject.parts.controller.PurchaseOrderController;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PartDAO;
import com.carmotorsproject.parts.model.PurchaseOrder;
import com.carmotorsproject.parts.model.PurchaseOrderDetail;
import com.carmotorsproject.parts.model.Supplier;
import com.carmotorsproject.parts.model.SupplierDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import javax.swing.JFrame;

public class PurchaseOrderView extends javax.swing.JFrame {

    private PurchaseOrderController controller;
    private SupplierDAO supplierDAO;
    private PartDAO partDAO;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private List<PurchaseOrderDetail> detailsList;

    public PurchaseOrderView() {
        supplierDAO = new SupplierDAO();
        partDAO = new PartDAO();
        detailsList = new ArrayList<>();
        initComponents();
        controller = new PurchaseOrderController(this);
        inputOrderDate.setDate(new Date());
        inputExpectedDeliveryDate.setDate(null);
        inputObservations.setText("");
        populateSuppliers();
        populateParts();
        populateStatuses();
        refreshDetailsTable();
        refreshTable(controller.getAllPurchaseOrders());
    }

    private void initComponents() {
        setTitle("Gestión de Órdenes de Compra");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.JPanel formPanel = new javax.swing.JPanel();
        formPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        javax.swing.JLabel lblSupplier = new javax.swing.JLabel("Proveedor");
        comboSupplier = new javax.swing.JComboBox<>();
        javax.swing.JLabel lblOrderDate = new javax.swing.JLabel("Fecha de Orden");
        inputOrderDate = new com.toedter.calendar.JDateChooser();
        inputOrderDate.setEnabled(false); // creation_date es auto-generado
        javax.swing.JLabel lblExpectedDate = new javax.swing.JLabel("Fecha Entrega Estimada");
        inputExpectedDeliveryDate = new com.toedter.calendar.JDateChooser();
        javax.swing.JLabel lblStatus = new javax.swing.JLabel("Estado");
        comboStatus = new javax.swing.JComboBox<>();
        javax.swing.JLabel lblObservations = new javax.swing.JLabel("Observaciones");
        inputObservations = new javax.swing.JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblSupplier, gbc);
        gbc.gridx = 1;
        formPanel.add(comboSupplier, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblOrderDate, gbc);
        gbc.gridx = 1;
        formPanel.add(inputOrderDate, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblExpectedDate, gbc);
        gbc.gridx = 1;
        formPanel.add(inputExpectedDeliveryDate, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblStatus, gbc);
        gbc.gridx = 1;
        formPanel.add(comboStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(lblObservations, gbc);
        gbc.gridx = 1;
        formPanel.add(inputObservations, gbc);

        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        javax.swing.JButton btnAdd = new javax.swing.JButton("Agregar");
        javax.swing.JButton btnUpdate = new javax.swing.JButton("Actualizar");
        javax.swing.JButton btnDelete = new javax.swing.JButton("Eliminar");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        javax.swing.JPanel detailsPanel = new javax.swing.JPanel();
        detailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles de la Orden"));
        detailsPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.JPanel detailFormPanel = new javax.swing.JPanel();
        detailFormPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbcDetail = new java.awt.GridBagConstraints();
        gbcDetail.insets = new java.awt.Insets(5, 5, 5, 5);
        gbcDetail.fill = java.awt.GridBagConstraints.HORIZONTAL;

        javax.swing.JLabel lblPart = new javax.swing.JLabel("Pieza");
        comboPart = new javax.swing.JComboBox<>();
        javax.swing.JLabel lblQuantity = new javax.swing.JLabel("Cantidad");
        inputQuantity = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(1, 1, 1000, 1));
        javax.swing.JLabel lblUnitPrice = new javax.swing.JLabel("Precio Unitario");
        inputUnitPrice = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(0.0, 0.0, 100000.0, 0.1));
        javax.swing.JButton btnAddDetail = new javax.swing.JButton("Agregar Detalle");
        javax.swing.JButton btnRemoveDetail = new javax.swing.JButton("Remover Detalle");

        gbcDetail.gridx = 0; gbcDetail.gridy = 0;
        detailFormPanel.add(lblPart, gbcDetail);
        gbcDetail.gridx = 1;
        detailFormPanel.add(comboPart, gbcDetail);

        gbcDetail.gridx = 0; gbcDetail.gridy = 1;
        detailFormPanel.add(lblQuantity, gbcDetail);
        gbcDetail.gridx = 1;
        detailFormPanel.add(inputQuantity, gbcDetail);

        gbcDetail.gridx = 0; gbcDetail.gridy = 2;
        detailFormPanel.add(lblUnitPrice, gbcDetail);
        gbcDetail.gridx = 1;
        detailFormPanel.add(inputUnitPrice, gbcDetail);

        gbcDetail.gridx = 2; gbcDetail.gridy = 1;
        detailFormPanel.add(btnAddDetail, gbcDetail);
        gbcDetail.gridx = 2; gbcDetail.gridy = 2;
        detailFormPanel.add(btnRemoveDetail, gbcDetail);

        jTableDetails = new javax.swing.JTable();
        javax.swing.JScrollPane scrollDetails = new javax.swing.JScrollPane(jTableDetails);
        detailsPanel.add(detailFormPanel, java.awt.BorderLayout.NORTH);
        detailsPanel.add(scrollDetails, java.awt.BorderLayout.CENTER);

        jTableOrders = new javax.swing.JTable();
        javax.swing.JScrollPane scrollOrders = new javax.swing.JScrollPane(jTableOrders);

        mainPanel.add(formPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(buttonPanel, java.awt.BorderLayout.CENTER);
        mainPanel.add(detailsPanel, java.awt.BorderLayout.SOUTH);
        mainPanel.add(scrollOrders, java.awt.BorderLayout.EAST);

        setContentPane(mainPanel);

        btnAdd.addActionListener(e -> savePurchaseOrder());
        btnUpdate.addActionListener(e -> updatePurchaseOrder());
        btnDelete.addActionListener(e -> deletePurchaseOrder());
        btnAddDetail.addActionListener(e -> addDetail());
        btnRemoveDetail.addActionListener(e -> removeDetail());
    }

    public void populateSuppliers() {
        comboSupplier.removeAllItems();
        comboSupplier.addItem("Select");
        for (Supplier supplier : supplierDAO.findAll()) {
            comboSupplier.addItem(supplier.getSupplierId() + "-" + supplier.getName());
        }
    }

    public void populateParts() {
        comboPart.removeAllItems();
        comboPart.addItem("Select");
        for (Part part : partDAO.findAll()) {
            comboPart.addItem(part.getPartId() + "-" + part.getName());
        }
    }

    public void populateStatuses() {
        comboStatus.removeAllItems();
        comboStatus.addItem("Select");
        for (String status : new String[]{"Pending", "Sent", "Received", "Cancelled"}) {
            comboStatus.addItem(status);
        }
    }

    public void addDetail() {
        String partStr = (String) comboPart.getSelectedItem();
        Object quantityObj = inputQuantity.getValue();
        Object unitPriceObj = inputUnitPrice.getValue();

        if (partStr == null || partStr.equals("Select") || quantityObj == null || unitPriceObj == null) {
            showAlert("Please select a part, quantity, and unit price.");
            return;
        }

        try {
            int partId = Integer.parseInt(partStr.split("-")[0]);
            int quantity = (Integer) quantityObj;
            double unitPrice = ((Number) unitPriceObj).doubleValue();

            if (quantity <= 0 || unitPrice <= 0) {
                showAlert("Quantity and unit price must be positive.");
                return;
            }

            PurchaseOrderDetail detail = new PurchaseOrderDetail(0, 0, partId, quantity, unitPrice);
            detailsList.add(detail);
            refreshDetailsTable();
            comboPart.setSelectedIndex(0);
            inputQuantity.setValue(0);
            inputUnitPrice.setValue(0.0);
        } catch (NumberFormatException e) {
            showAlert("Invalid part ID, quantity, or unit price.");
        }
    }

    public void removeDetail() {
        int selectedRow = jTableDetails.getSelectedRow();
        if (selectedRow >= 0) {
            detailsList.remove(selectedRow);
            refreshDetailsTable();
        } else {
            showAlert("Select a detail to remove.");
        }
    }

    public void refreshDetailsTable() {
        String[] columns = {"Part ID", "Part Name", "Quantity Ordered", "Estimated Unit Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (PurchaseOrderDetail detail : detailsList) {
            Part part = partDAO.findById(detail.getPartId());
            model.addRow(new Object[]{
                detail.getPartId(),
                part != null ? part.getName() : "Unknown",
                detail.getQuantityOrdered(),
                detail.getEstimatedUnitPrice()
            });
        }
        jTableDetails.setModel(model);
    }

    public void clearFields() {
        comboSupplier.setSelectedIndex(0);
        inputOrderDate.setDate(new Date());
        inputExpectedDeliveryDate.setDate(null);
        comboStatus.setSelectedIndex(0);
        inputObservations.setText("");
        detailsList.clear();
        refreshDetailsTable();
    }

    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void savePurchaseOrder() {
        int supplierId = getSupplierId();
        Date orderDate = inputOrderDate.getDate();
        Date expectedDeliveryDate = inputExpectedDeliveryDate.getDate();
        String status = (String) comboStatus.getSelectedItem();
        String observations = inputObservations.getText().trim();

        if (supplierId == 0 || orderDate == null || status == null || status.equals("Select") || detailsList.isEmpty()) {
            showAlert("Please complete the required fields (Supplier, Order Date, Status) and add at least one detail.");
            return;
        }

        if (expectedDeliveryDate != null && expectedDeliveryDate.before(orderDate)) {
            showAlert("Expected delivery date must be after order date.");
            return;
        }

        try {
            PurchaseOrder order = new PurchaseOrder(0, supplierId, orderDate, expectedDeliveryDate,
                                                   status, observations, new ArrayList<>(detailsList));
            controller.addPurchaseOrder(order);
            clearFields();
        } catch (Exception e) {
            showAlert("Error adding purchase order: " + e.getMessage());
        }
    }

    public void updatePurchaseOrder() {
        int selectedRow = jTableOrders.getSelectedRow();
        if (selectedRow >= 0) {
            int orderId = (int) jTableOrders.getValueAt(selectedRow, 0);
            int supplierId = getSupplierId();
            Date orderDate = inputOrderDate.getDate();
            Date expectedDeliveryDate = inputExpectedDeliveryDate.getDate();
            String status = (String) comboStatus.getSelectedItem();
            String observations = inputObservations.getText().trim();

            if (supplierId == 0 || orderDate == null || status == null || status.equals("Select")) {
                showAlert("Please complete the required fields (Supplier, Order Date, Status).");
                return;
            }

            if (expectedDeliveryDate != null && expectedDeliveryDate.before(orderDate)) {
                showAlert("Expected delivery date must be after order date.");
                return;
            }

            try {
                PurchaseOrder order = controller.getAllPurchaseOrders().stream()
                    .filter(o -> o.getPurchaseOrderId() == orderId)
                    .findFirst()
                    .orElse(null);
                if (order != null) {
                    order.setSupplierId(supplierId);
                    order.setCreationDate(orderDate);
                    order.setExpectedDeliveryDate(expectedDeliveryDate);
                    order.setStatus(status);
                    order.setObservations(observations);
                    order.setDetails(new ArrayList<>(detailsList));
                    controller.updatePurchaseOrder(order);
                    clearFields();
                } else {
                    showAlert("Purchase order not found.");
                }
            } catch (Exception e) {
                showAlert("Error updating purchase order: " + e.getMessage());
            }
        } else {
            showAlert("Select a purchase order to update.");
        }
    }

    public void deletePurchaseOrder() {
        int selectedRow = jTableOrders.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) jTableOrders.getValueAt(selectedRow, 0);
            try {
                controller.deletePurchaseOrder(id);
                clearFields();
            } catch (Exception e) {
                showAlert("Error deleting purchase order: " + e.getMessage());
            }
        } else {
            showAlert("Select a purchase order to delete.");
        }
    }

    public int getSupplierId() {
        String selected = (String) comboSupplier.getSelectedItem();
        if (selected == null || selected.equals("Select")) {
            return 0;
        }
        try {
            String[] parts = selected.split("-");
            return Integer.parseInt(parts[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            showAlert("Error getting supplier ID: " + e.getMessage());
            return 0;
        }
    }

    public void refreshTable(List<PurchaseOrder> orders) {
        String[] columns = {"ID", "Supplier Name", "Creation Date", "Expected Delivery", "Status", "Observations"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (PurchaseOrder order : orders) {
            Supplier supplier = supplierDAO.findById(order.getSupplierId());
            model.addRow(new Object[]{
                order.getPurchaseOrderId(),
                supplier != null ? supplier.getName() : "",
                order.getCreationDate() != null ? dateFormat.format(order.getCreationDate()) : "",
                order.getExpectedDeliveryDate() != null ? dateFormat.format(order.getExpectedDeliveryDate()) : "",
                order.getStatus(),
                order.getObservations() != null ? order.getObservations() : ""
            });
        }
        jTableOrders.setModel(model);
    }

    private javax.swing.JComboBox<String> comboPart;
    private javax.swing.JComboBox<String> comboSupplier;
    private javax.swing.JComboBox<String> comboStatus;
    private com.toedter.calendar.JDateChooser inputOrderDate;
    private com.toedter.calendar.JDateChooser inputExpectedDeliveryDate;
    private javax.swing.JTextField inputObservations;
    private javax.swing.JSpinner inputQuantity;
    private javax.swing.JSpinner inputUnitPrice;
    private javax.swing.JTable jTableOrders;
    private javax.swing.JTable jTableDetails;
}