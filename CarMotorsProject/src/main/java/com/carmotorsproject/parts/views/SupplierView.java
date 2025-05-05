/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.parts.views;


import com.carmotorsproject.parts.controller.SupplierController;
import com.carmotorsproject.parts.model.Supplier;
import com.carmotorsproject.parts.model.SupplierDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SupplierView extends JFrame {
    private SupplierController controller;
    private JTextField inputName;
    private JTextField inputTaxId;
    private JTextField inputContact;
    private JTable tableSuppliers;

    public SupplierView() {
        this.controller = new SupplierController(this);
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        setTitle("Gestión de Proveedores");
        setSize(600, 400);
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
        JLabel lblTaxId = new JLabel("Tax ID");
        inputTaxId = new JTextField(20);
        JLabel lblContact = new JLabel("Contacto");
        inputContact = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblName, gbc);
        gbc.gridx = 1;
        formPanel.add(inputName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblTaxId, gbc);
        gbc.gridx = 1;
        formPanel.add(inputTaxId, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblContact, gbc);
        gbc.gridx = 1;
        formPanel.add(inputContact, gbc);

        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Agregar");
        buttonPanel.add(btnAdd);

        tableSuppliers = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableSuppliers);

        mainPanel.add(formPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(buttonPanel, java.awt.BorderLayout.CENTER);
        mainPanel.add(scrollPane, java.awt.BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnAdd.addActionListener(e -> addSupplier());
    }

    private void addSupplier() {
        String name = inputName.getText().trim();
        String taxId = inputTaxId.getText().trim();
        String contact = inputContact.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in the name.");
            return;
        }

        Supplier supplier = new Supplier(0, name, taxId, contact);
        controller.addSupplier(supplier);
        clearFields();
    }

    public void refreshTable() {
        String[] columns = {"ID", "Name", "Tax ID", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        List<Supplier> suppliers = controller.getAllSupplier();
        for (Supplier supplier : suppliers) {
            model.addRow(new Object[]{
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getTaxId(),
                supplier.getContact()
            });
        }
        tableSuppliers.setModel(model);
    }

    private void clearFields() {
        inputName.setText("");
        inputTaxId.setText("");
        inputContact.setText("");
    }

    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}