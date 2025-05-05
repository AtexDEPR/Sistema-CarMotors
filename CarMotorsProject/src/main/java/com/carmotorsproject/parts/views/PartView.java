/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.parts.views;

import com.carmotorsproject.parts.controller.PartController;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PartDAO;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class PartView extends JFrame {
    private PartController controller;
    private JTextField inputName;
    private JComboBox<String> inputType;
    private JTextField inputCompatibleMakeModel;
    private JComboBox<Integer> inputSupplierId;
    private JSpinner inputQuantityInStock;
    private JSpinner inputMinimumStock;
    private JDateChooser inputEntryDate;
    private JDateChooser inputEstimatedLifespan;
    private JComboBox<String> inputStatus;
    private JTextField inputBatchId;
    private JTable tableParts;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public PartView() {
        this.controller = new PartController(this);
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        setTitle("Gestión de Partes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Nombre");
        inputName = new JTextField(20);
        JLabel lblType = new JLabel("Tipo");
        inputType = new JComboBox<>(new String[]{"Mechanical", "Electrical", "Body", "Consumable"});
        JLabel lblCompatibleMakeModel = new JLabel("Modelo Compatible");
        inputCompatibleMakeModel = new JTextField(20);
        JLabel lblSupplierId = new JLabel("ID Proveedor");
        inputSupplierId = new JComboBox<>();
        JLabel lblQuantityInStock = new JLabel("Cantidad en Stock");
        inputQuantityInStock = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        JLabel lblMinimumStock = new JLabel("Stock Mínimo");
        inputMinimumStock = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        JLabel lblEntryDate = new JLabel("Fecha de Entrada");
        inputEntryDate = new JDateChooser();
        JLabel lblEstimatedLifespan = new JLabel("Vida Estimada");
        inputEstimatedLifespan = new JDateChooser();
        JLabel lblStatus = new JLabel("Estado");
        inputStatus = new JComboBox<>(new String[]{"Available", "Reserved", "Out_of_service"});
        JLabel lblBatchId = new JLabel("Batch ID");
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
        formPanel.add(lblSupplierId, gbc);
        gbc.gridx = 1;
        formPanel.add(inputSupplierId, gbc);

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

        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Agregar");
        buttonPanel.add(btnAdd);

        tableParts = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableParts);

        mainPanel.add(formPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(buttonPanel, java.awt.BorderLayout.CENTER);
        mainPanel.add(scrollPane, java.awt.BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnAdd.addActionListener(e -> addPart());
    }

    private void addPart() {
        String name = inputName.getText().trim();
        String type = (String) inputType.getSelectedItem();
        String compatibleMakeModel = inputCompatibleMakeModel.getText().trim();
        Integer supplierId = (Integer) inputSupplierId.getSelectedItem();
        int quantityInStock = (Integer) inputQuantityInStock.getValue();
        int minimumStock = (Integer) inputMinimumStock.getValue();
        Date entryDate = inputEntryDate.getDate();
        Date estimatedLifespan = inputEstimatedLifespan.getDate();
        String status = (String) inputStatus.getSelectedItem();
        String batchId = inputBatchId.getText().trim();

        if (name.isEmpty() || type == null || status == null) {
            JOptionPane.showMessageDialog(this, "Please fill in required fields (Name, Type, Status).");
            return;
        }

        Part part = new Part(0, name, type, compatibleMakeModel, supplierId, quantityInStock, minimumStock,
                entryDate, estimatedLifespan, status, batchId, new Date(), new Date());
        controller.addPart(part);
        clearFields();
    }

    public void refreshTable() {
        String[] columns = {"ID", "Name", "Type", "Compatible Model", "Supplier ID", "Stock", "Min Stock", "Entry Date", "Lifespan", "Status", "Batch ID", "Creation Date", "Last Update"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Part part : controller.getAllParts()) {
            model.addRow(new Object[]{
                part.getPartId(),
                part.getName(),
                part.getType(),
                part.getCompatibleMakeModel(),
                part.getSupplierId(),
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
        inputSupplierId.setSelectedItem(null);
        inputQuantityInStock.setValue(0);
        inputMinimumStock.setValue(1);
        inputEntryDate.setDate(null);
        inputEstimatedLifespan.setDate(null);
        inputStatus.setSelectedIndex(0);
        inputBatchId.setText("");
    }

    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}