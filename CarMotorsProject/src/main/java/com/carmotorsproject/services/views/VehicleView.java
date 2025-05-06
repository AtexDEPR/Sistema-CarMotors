/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.views;

import com.carmotorsproject.services.controller.VehicleController;
import com.carmotorsproject.services.model.Vehicle;
import javax.swing.JFrame;
import java.awt.*;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
public class VehicleView extends JFrame {
    private VehicleController controller;
    private JTextField txtVehicleId, txtCustomerId, txtLicensePlate, txtMake, txtModel, txtYear;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;

    public VehicleView(VehicleController controller) {
        this.controller = controller;
        initComponents();
        loadVehicles();
    }

    private void initComponents() {
        setTitle("Gestión de Vehículos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());
        setContentPane(panel);

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Vehicle ID:"));
        txtVehicleId = new JTextField();
        txtVehicleId.setEditable(false);
        formPanel.add(txtVehicleId);

        formPanel.add(new JLabel("Client ID:"));
        txtCustomerId = new JTextField();
        formPanel.add(txtCustomerId);

        formPanel.add(new JLabel("Placa:"));
        txtLicensePlate = new JTextField();
        formPanel.add(txtLicensePlate);

        formPanel.add(new JLabel("Brand:"));
        txtMake = new JTextField();
        formPanel.add(txtMake);

        formPanel.add(new JLabel("Model:"));
        txtModel = new JTextField();
        formPanel.add(txtModel);

        formPanel.add(new JLabel("Year:"));
        txtYear = new JTextField();
        formPanel.add(txtYear);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Eliminate");
        JButton btnClear = new JButton("Clean");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        formPanel.add(buttonPanel);

        // Tabla
        String[] columnNames = {"ID", "Customer ID", "Plate", "Make", "Model", "Year"};
        tableModel = new DefaultTableModel(columnNames, 0);
        vehicleTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(vehicleTable);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Acciones de los botones
        btnAdd.addActionListener(e -> addVehicle());
        btnUpdate.addActionListener(e -> updateVehicle());
        btnDelete.addActionListener(e -> deleteVehicle());
        btnClear.addActionListener(e -> clearFields());

        // Selección de fila en la tabla
        vehicleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && vehicleTable.getSelectedRow() != -1) {
                int row = vehicleTable.getSelectedRow();
                txtVehicleId.setText(tableModel.getValueAt(row, 0).toString());
                txtCustomerId.setText(tableModel.getValueAt(row, 1).toString());
                txtLicensePlate.setText(tableModel.getValueAt(row, 2).toString());
                txtMake.setText(tableModel.getValueAt(row, 3).toString());
                txtModel.setText(tableModel.getValueAt(row, 4).toString());
                txtYear.setText(tableModel.getValueAt(row, 5).toString());
            }
        });
    }

    private void addVehicle() {
        try {
            if (validateFields()) {
                Vehicle vehicle = new Vehicle(
                    0, // ID será generado por la base de datos
                    Integer.parseInt(txtCustomerId.getText()),
                    txtLicensePlate.getText(),
                    txtMake.getText(),
                    txtModel.getText(),
                    Integer.parseInt(txtYear.getText()),
                    new Date(),
                    new Date()
                );
                controller.addVehicle(vehicle);
                loadVehicles();
                clearFields();
                JOptionPane.showMessageDialog(this, "Vehicle added successfully");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values ​​for Customer ID and Year", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding vehicle: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateVehicle() {
        try {
            if (validateFields() && !txtVehicleId.getText().isEmpty()) {
                Vehicle vehicle = new Vehicle(
                    Integer.parseInt(txtVehicleId.getText()),
                    Integer.parseInt(txtCustomerId.getText()),
                    txtLicensePlate.getText(),
                    txtMake.getText(),
                    txtModel.getText(),
                    Integer.parseInt(txtYear.getText()),
                    controller.findById(Integer.parseInt(txtVehicleId.getText())).getCreationDate(),
                    new Date()
                );
                controller.updateVehicle(vehicle);
                loadVehicles();
                clearFields();
                JOptionPane.showMessageDialog(this, "Vehicle successfully updated");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating vehicle: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVehicle() {
        try {
            if (!txtVehicleId.getText().isEmpty()) {
                int vehicleId = Integer.parseInt(txtVehicleId.getText());
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this vehicle?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteVehicle(vehicleId);
                    loadVehicles();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Vehicle successfully removed");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a vehicle to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting vehicle: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtVehicleId.setText("");
        txtCustomerId.setText("");
        txtLicensePlate.setText("");
        txtMake.setText("");
        txtModel.setText("");
        txtYear.setText("");
    }

    private boolean validateFields() {
        if (txtCustomerId.getText().isEmpty() || txtLicensePlate.getText().isEmpty() ||
            txtMake.getText().isEmpty() || txtModel.getText().isEmpty() || txtYear.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void loadVehicles() {
        tableModel.setRowCount(0);
        List<Vehicle> vehicles = controller.getAllVehicles();
        for (Vehicle vehicle : vehicles) {
            tableModel.addRow(new Object[]{
                vehicle.getVehicleId(),
                vehicle.getCustomerId(),
                vehicle.getLicensePlate(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getYear()
            });
        }
    }
}