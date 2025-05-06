/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.views;

import com.carmotorsproject.customers.controller.CustomerController;
import com.carmotorsproject.customers.model.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class CustomerView extends JFrame {
    private CustomerController controller;
    private JTextField txtCustomerId, txtName, txtIdentificationNumber, txtPhone, txtEmail, txtAddress;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerView(CustomerController controller) {
        this.controller = controller;
        initComponents();
        loadCustomers();
    }

    private void initComponents() {
        setTitle("Gestión de Clientes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());
        setContentPane(panel);

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("ID Cliente:"));
        txtCustomerId = new JTextField();
        txtCustomerId.setEditable(false);
        formPanel.add(txtCustomerId);

        formPanel.add(new JLabel("Nombre:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Número de Identificación:"));
        txtIdentificationNumber = new JTextField();
        formPanel.add(txtIdentificationNumber);

        formPanel.add(new JLabel("Teléfono:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Correo Electrónico:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Dirección:"));
        txtAddress = new JTextField();
        formPanel.add(txtAddress);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Agregar");
        JButton btnUpdate = new JButton("Actualizar");
        JButton btnDelete = new JButton("Eliminar");
        JButton btnClear = new JButton("Limpiar");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        formPanel.add(buttonPanel);

        // Tabla
        String[] columnNames = {"ID", "Nombre", "Identificación", "Teléfono", "Correo", "Dirección"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Acciones de los botones
        btnAdd.addActionListener(e -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnClear.addActionListener(e -> clearFields());

        // Selección de fila en la tabla
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && customerTable.getSelectedRow() != -1) {
                int row = customerTable.getSelectedRow();
                txtCustomerId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtIdentificationNumber.setText(tableModel.getValueAt(row, 2).toString());
                txtPhone.setText(tableModel.getValueAt(row, 3).toString());
                txtEmail.setText(tableModel.getValueAt(row, 4).toString());
                txtAddress.setText(tableModel.getValueAt(row, 5).toString());
            }
        });
    }

    private void addCustomer() {
        try {
            if (validateFields()) {
                Customer customer = new Customer(
                    0, // ID será generado por la base de datos
                    txtName.getText(),
                    txtIdentificationNumber.getText(),
                    txtPhone.getText(),
                    txtEmail.getText(),
                    txtAddress.getText(),
                    new Date(),
                    new Date()
                );
                controller.addCustomer(customer);
                loadCustomers();
                clearFields();
                JOptionPane.showMessageDialog(this, "Cliente agregado con éxito");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {
        try {
            if (validateFields() && !txtCustomerId.getText().isEmpty()) {
                Customer customer = new Customer(
                    Integer.parseInt(txtCustomerId.getText()),
                    txtName.getText(),
                    txtIdentificationNumber.getText(),
                    txtPhone.getText(),
                    txtEmail.getText(),
                    txtAddress.getText(),
                    controller.findById(Integer.parseInt(txtCustomerId.getText())).getCreationDate(),
                    new Date()
                );
                controller.updateCustomer(customer);
                loadCustomers();
                clearFields();
                JOptionPane.showMessageDialog(this, "Cliente actualizado con éxito");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en el ID del cliente", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        try {
            if (!txtCustomerId.getText().isEmpty()) {
                int customerId = Integer.parseInt(txtCustomerId.getText());
                int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteCustomer(customerId);
                    loadCustomers();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Cliente eliminado con éxito");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtCustomerId.setText("");
        txtName.setText("");
        txtIdentificationNumber.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
    }

    private boolean validateFields() {
        if (txtName.getText().isEmpty() || txtIdentificationNumber.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete los campos obligatorios (Nombre y Número de Identificación)", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Customer> customers = controller.getAllCustomers();
        for (Customer customer : customers) {
            tableModel.addRow(new Object[]{
                customer.getCustomerId(),
                customer.getName(),
                customer.getIdentificationNumber(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress()
            });
        }
    }
}