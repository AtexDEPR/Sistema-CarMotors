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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class PurchaseOrderView extends JFrame {
    private PurchaseOrderController controller;
    private SupplierDAO supplierDAO;
    private JTable tableOrders;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public PurchaseOrderView() {
        this.controller = new PurchaseOrderController(this);
        this.supplierDAO = new SupplierDAO();
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        setTitle("Purchase Orders - Engines and Wheels");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout());

        // Tabla de órdenes
        tableOrders = new JTable();
        tableOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tableOrders);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        JButton btnCreate = new JButton("Create New Order");
        JButton btnView = new JButton("See Details");
        JButton btnDelete = new JButton("Delete Order");
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnView);
        buttonPanel.add(btnDelete);

        mainPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
        mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnCreate.addActionListener(e -> createNewOrder());
        btnView.addActionListener(e -> viewOrderDetails());
        btnDelete.addActionListener(e -> deleteOrder());
    }

    private void createNewOrder() {
        // Lógica para crear manualmente (por ahora placeholder, se integrará con PartView)
showAlert("Manual creation functionality under development. Use the inventory module to automatically generate orders.");    }

    private void viewOrderDetails() {
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow < 0) {
            showAlert("Please select an order to view details.");
            return;
        }
        int orderId = (int) tableOrders.getValueAt(selectedRow, 0);
        PurchaseOrder order = controller.getAllPurchaseOrders().stream()
                .filter(o -> o.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
        if (order == null) {
            showAlert("Order not found.");
            return;
        }

        Supplier supplier = order.getSupplierId() != null ? supplierDAO.findById(order.getSupplierId()) : null;
        StringBuilder details = new StringBuilder("Order ID details" + orderId + ":\n");
        details.append("Date: ").append(dateFormat.format(order.getOrderDate())).append("\n");
        details.append("State: ").append(order.getStatus()).append("\n");
        details.append("Supplier: ").append(supplier != null ? supplier.getName() : "Ninguno").append("\n");
        details.append("Total: $").append(String.format("%.2f", order.getTotalAmount())).append("\n");
        details.append("Spare Parts Details:\n");
        for (PurchaseOrderDetail detail : order.getDetails()) {
            details.append("- Spare ID: ").append(detail.getPartId())
                    .append(", Quantity ").append(detail.getQuantity())
                    .append(", Unit Price: $").append(String.format("%.2f", detail.getUnitPrice()))
                    .append(",Subtotal: $").append(String.format("%.2f", detail.getSubtotal())).append("\n");
        }
        showAlert(details.toString());
    }

    private void deleteOrder() {
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow < 0) {
            showAlert("Please select an order to delete.");
            return;
        }
        int orderId = (int) tableOrders.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Are you sure you want to delete this order?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deletePurchaseOrder(orderId);
        }
    }

    public void refreshTable() {
        String[] columns = {"ID", "Date", "Status", "Supplier", "Total"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (PurchaseOrder order : controller.getAllPurchaseOrders()) {
            Supplier supplier = order.getSupplierId() != null ? supplierDAO.findById(order.getSupplierId()) : null;
            String supplierName = supplier != null ? supplier.getName() : "None";
            model.addRow(new Object[]{
                order.getOrderId(),
                dateFormat.format(order.getOrderDate()),
                order.getStatus(),
                supplierName,
                String.format("%.2f", order.getTotalAmount())
            });
        }
        tableOrders.setModel(model);
        if (model.getRowCount() == 0) {
            showAlert("There are no purchase orders recorded.");
        }
    }

    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void addPurchaseOrder(PurchaseOrder order) {
        controller.addPurchaseOrder(order);
    }
}