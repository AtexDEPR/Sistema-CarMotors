/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.invoices.views;


import com.carmotorsproject.invoices.controller.InvoiceController;
import com.carmotorsproject.invoices.model.Invoice;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class InvoiceView extends JFrame {
    private InvoiceController controller;
    private JTextField txtInvoiceId, txtServiceId, txtInvoiceNumber;
    private JTable invoiceTable;
    private DefaultTableModel tableModel;

    public InvoiceView(InvoiceController controller) {
        this.controller = controller;
        initComponents();
        loadInvoices();
    }

    private void initComponents() {
        setTitle("Gestión de Facturas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());
        setContentPane(panel);

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("ID Factura:"));
        txtInvoiceId = new JTextField();
        txtInvoiceId.setEditable(false);
        formPanel.add(txtInvoiceId);

        formPanel.add(new JLabel("ID Servicio:"));
        txtServiceId = new JTextField();
        formPanel.add(txtServiceId);

        formPanel.add(new JLabel("Número de Factura:"));
        txtInvoiceNumber = new JTextField();
        formPanel.add(txtInvoiceNumber);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Agregar");
        JButton btnDelete = new JButton("Eliminar");
        JButton btnGeneratePDF = new JButton("Generar PDF");
        JButton btnClear = new JButton("Limpiar");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnGeneratePDF);
        buttonPanel.add(btnClear);

        formPanel.add(buttonPanel);

        // Tabla
        String[] columnNames = {"ID", "ID Servicio", "Cliente", "Número", "Descripción", "Repuestos", "Mano de Obra", "Subtotal", "Impuestos", "Total", "Fecha"};
        tableModel = new DefaultTableModel(columnNames, 0);
        invoiceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(invoiceTable);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Acciones de los botones
        btnAdd.addActionListener(e -> addInvoice());
        btnDelete.addActionListener(e -> deleteInvoice());
        btnGeneratePDF.addActionListener(e -> generatePDF());
        btnClear.addActionListener(e -> clearFields());

        // Selección de fila en la tabla
        invoiceTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && invoiceTable.getSelectedRow() != -1) {
                int row = invoiceTable.getSelectedRow();
                txtInvoiceId.setText(tableModel.getValueAt(row, 0).toString());
                txtServiceId.setText(tableModel.getValueAt(row, 1).toString());
                txtInvoiceNumber.setText(tableModel.getValueAt(row, 3).toString());
            }
        });
    }

    private void addInvoice() {
        try {
            if (validateFields()) {
                Invoice invoice = new Invoice(
                    0, // ID generado por la base de datos
                    Integer.parseInt(txtServiceId.getText()),
                    new Date(),
                    txtInvoiceNumber.getText(),
                    0.0, // Subtotal calculado por el controlador
                    0.0, // Impuestos calculados por el controlador
                    0.0, // Total calculado por el controlador
                    null, // electronic_invoice_id
                    null, // qr_code
                    null, // customer_name (populated by DAO)
                    null, // customer_document
                    null, // customer_address
                    null, // service_description
                    0, // parts_quantity
                    0.0 // labor_cost
                );
                controller.addInvoice(invoice);
                loadInvoices();
                clearFields();
                JOptionPane.showMessageDialog(this, "Factura agregada con éxito");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteInvoice() {
        try {
            if (!txtInvoiceId.getText().isEmpty()) {
                int invoiceId = Integer.parseInt(txtInvoiceId.getText());
                int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta factura?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteInvoice(invoiceId);
                    loadInvoices();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Factura eliminada con éxito");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una factura para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generatePDF() {
        try {
            if (!txtInvoiceId.getText().isEmpty()) {
                int invoiceId = Integer.parseInt(txtInvoiceId.getText());
                controller.generateInvoicePDF(invoiceId);
                JOptionPane.showMessageDialog(this, "PDF generado con éxito");
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una factura para generar el PDF", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtInvoiceId.setText("");
        txtServiceId.setText("");
        txtInvoiceNumber.setText("");
    }

    private boolean validateFields() {
        if (txtServiceId.getText().isEmpty() || txtInvoiceNumber.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete the required fields (Service ID, Invoice Number)", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void loadInvoices() {
        tableModel.setRowCount(0);
        List<Invoice> invoices = controller.getAllInvoices();
        for (Invoice invoice : invoices) {
            tableModel.addRow(new Object[]{
                invoice.getInvoiceId(),
                invoice.getServiceId(),
                invoice.getCustomerName(),
                invoice.getInvoiceNumber(),
                invoice.getServiceDescription(),
                invoice.getPartsQuantity(),
                invoice.getLaborCost(),
                invoice.getSubtotal(),
                invoice.getTaxes(),
                invoice.getTotal(),
                invoice.getIssueDate()
            });
        }
    }
}