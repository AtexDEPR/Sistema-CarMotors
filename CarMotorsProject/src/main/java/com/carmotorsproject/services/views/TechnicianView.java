/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.views;

import com.carmotorsproject.services.controller.TechnicianController;
import com.carmotorsproject.services.model.Technician;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TechnicianView extends JFrame {
    private TechnicianController controller;
    private JTextField txtTechnicianId, txtName, txtSpecialty;
    private JComboBox<String> cbStatus;
    private JTable technicianTable;
    private DefaultTableModel tableModel;

    public TechnicianView(TechnicianController controller) {
        this.controller = controller;
        initComponents();
        loadTechnicians();
    }

    private void initComponents() {
        setTitle("Gestión de Técnicos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());
        setContentPane(panel);

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Technical ID:"));
        txtTechnicianId = new JTextField();
        txtTechnicianId.setEditable(false);
        formPanel.add(txtTechnicianId);

        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Specialty:"));
        txtSpecialty = new JTextField();
        formPanel.add(txtSpecialty);

        formPanel.add(new JLabel("State:"));
        cbStatus = new JComboBox<>(new String[]{"AVAILABLE", "BUSY", "ON_LEAVE"});
        formPanel.add(cbStatus);

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
        String[] columnNames ={"ID", "Name", "Specialty", "State"};
        tableModel = new DefaultTableModel(columnNames, 0);
        technicianTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(technicianTable);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Acciones de los botones
        btnAdd.addActionListener(e -> addTechnician());
        btnUpdate.addActionListener(e -> updateTechnician());
        btnDelete.addActionListener(e -> deleteTechnician());
        btnClear.addActionListener(e -> clearFields());

        // Selección de fila en la tabla
        technicianTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && technicianTable.getSelectedRow() != -1) {
                int row = technicianTable.getSelectedRow();
                txtTechnicianId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtSpecialty.setText(tableModel.getValueAt(row, 2).toString());
                cbStatus.setSelectedItem(tableModel.getValueAt(row, 3).toString());
            }
        });
    }

    private void addTechnician() {
        try {
            if (validateFields()) {
                Technician technician = new Technician(
                    0, // ID generado por la base de datos
                    txtName.getText(),
                    txtSpecialty.getText(),
                    cbStatus.getSelectedItem().toString(),
                    new Date(),
                    new Date()
                );
                controller.addTechnician(technician);
                loadTechnicians();
                clearFields();
                JOptionPane.showMessageDialog(this, "Technician added successfully");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding technician: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTechnician() {
        try {
            if (validateFields() && !txtTechnicianId.getText().isEmpty()) {
                Technician technician = new Technician(
                    Integer.parseInt(txtTechnicianId.getText()),
                    txtName.getText(),
                    txtSpecialty.getText(),
                    cbStatus.getSelectedItem().toString(),
                    controller.findById(Integer.parseInt(txtTechnicianId.getText())).getCreationDate(),
                    new Date()
                );
                controller.updateTechnician(technician);
                loadTechnicians();
                clearFields();
                JOptionPane.showMessageDialog(this, "Technician successfully updated");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Technician ID error", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating technician: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTechnician() {
        try {
            if (!txtTechnicianId.getText().isEmpty()) {
                int technicianId = Integer.parseInt(txtTechnicianId.getText());
                int confirm = JOptionPane.showConfirmDialog(this, "¿Are you sure to remove this technician?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteTechnician(technicianId);
                    loadTechnicians();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Technician successfully eliminated");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a technician to remove", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting technician: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtTechnicianId.setText("");
        txtName.setText("");
        txtSpecialty.setText("");
        cbStatus.setSelectedIndex(0);
    }

    private boolean validateFields() {
        if (txtName.getText().isEmpty() || txtSpecialty.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete the required fields (Name and Specialty))", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void loadTechnicians() {
        tableModel.setRowCount(0);
        List<Technician> technicians = controller.getAllTechnicians();
        for (Technician technician : technicians) {
            tableModel.addRow(new Object[]{
                technician.getTechnicianId(),
                technician.getName(),
                technician.getSpecialty(),
                technician.getStatus()
            });
        }
    }
}