/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.views;
/*
 * View for adding a Supplier in the MVC pattern using JFrame.
 */

import com.carmotorsproject.parts.controller.SupplierController;
import com.carmotorsproject.parts.model.Supplier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddSupplierView extends JFrame {
    private JTextField nameField, contactField, taxIdField;
    private JButton saveButton, cancelButton;
    private SupplierController controller;

    public AddSupplierView(SupplierController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setTitle("Add New Supplier");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Contact
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        contactField = new JTextField(20);
        panel.add(contactField, gbc);

        // Tax ID
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Tax ID:"), gbc);
        gbc.gridx = 1;
        taxIdField = new JTextField(20);
        panel.add(taxIdField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);

        // Action listeners
        saveButton.addActionListener(e -> saveSupplier());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveSupplier() {
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String taxId = taxIdField.getText().trim();

        if (name.isEmpty() || contact.isEmpty() || taxId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

       // Supplier supplier = new Supplier(0, name, contact, taxId); // supplier_id = 0 (auto-generated)
        try {
            //controller.addSupplier(supplier);
            JOptionPane.showMessageDialog(this, "Supplier added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void display() {
        setVisible(true);
    }
}