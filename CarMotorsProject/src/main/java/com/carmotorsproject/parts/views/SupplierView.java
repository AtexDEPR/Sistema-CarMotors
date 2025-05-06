/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.parts.views;

import com.carmotorsproject.parts.controller.SupplierController;
import com.carmotorsproject.parts.model.Supplier;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SupplierView extends JFrame {
    private SupplierController controller;
    private JTextField inputName;
    private JTextField inputTaxId;
    private JTextField inputContact;
    private JTextField inputSearch;
    private JTable tableSuppliers;

    public SupplierView() {
        this.controller = new SupplierController(this);
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        setTitle("Supplier Management");
        setSize(600, 400);
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

        JLabel lblName = new JLabel("Name");
        inputName = new JTextField(20);
        JLabel lblTaxId = new JLabel("Tax ID");
        inputTaxId = new JTextField(20);
        JLabel lblContact = new JLabel("Contact");
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

        // Panel de búsqueda
        JPanel searchPanel = new JPanel();
        JLabel lblSearch = new JLabel("Search by Name:");
        inputSearch = new JTextField(20);
        JButton btnSearch = new JButton("search");
        searchPanel.add(lblSearch);
        searchPanel.add(inputSearch);
        searchPanel.add(btnSearch);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Eliminate");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // Tabla de proveedores
        tableSuppliers = new JTable();
        tableSuppliers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableSuppliers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableSuppliers.getSelectedRow();
                if (selectedRow >= 0) {
                    inputName.setText(tableSuppliers.getValueAt(selectedRow, 1).toString());
                    inputTaxId.setText(tableSuppliers.getValueAt(selectedRow, 2).toString());
                    inputContact.setText(tableSuppliers.getValueAt(selectedRow, 3).toString());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tableSuppliers);

        mainPanel.add(formPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(searchPanel, java.awt.BorderLayout.CENTER);
        mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        mainPanel.add(scrollPane, java.awt.BorderLayout.EAST);

        setContentPane(mainPanel);

        btnAdd.addActionListener(e -> addSupplier());
        btnUpdate.addActionListener(e -> updateSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());
        btnSearch.addActionListener(e -> searchSupplier());
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

    private void updateSupplier() {
        int selectedRow = tableSuppliers.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a supplier to update.");
            return;
        }

        int supplierId = (int) tableSuppliers.getValueAt(selectedRow, 0);
        String name = inputName.getText().trim();
        String taxId = inputTaxId.getText().trim();
        String contact = inputContact.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in the name.");
            return;
        }

        Supplier supplier = new Supplier(supplierId, name, taxId, contact);
        controller.updateSupplier(supplier);
        clearFields();
    }

    private void deleteSupplier() {
        int selectedRow = tableSuppliers.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a supplier to delete.");
            return;
        }

        int supplierId = (int) tableSuppliers.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this supplier?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteSupplier(supplierId);
            clearFields();
        }
    }

    private void searchSupplier() {
        String searchTerm = inputSearch.getText().trim();
        List<Supplier> suppliers;
        if (searchTerm.isEmpty()) {
            suppliers = controller.getAllSupplier();
        } else {
            suppliers = controller.searchSupplier(searchTerm);
        }
        updateTable(suppliers);
    }

    public void refreshTable() {
        updateTable(controller.getAllSupplier());
    }

    private void updateTable(List<Supplier> suppliers) {
        String[] columns = {"ID", "Name", "Tax ID", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
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
        inputSearch.setText("");
        tableSuppliers.clearSelection();
    }

    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}