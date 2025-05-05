/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.parts.views;

/**
 *
 * @author ADMiN
 */

import com.carmotorsproject.parts.controller.PurchaseOrderController;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PurchaseOrder;
import com.carmotorsproject.parts.model.PurchaseOrderDetail;
import com.carmotorsproject.parts.model.Supplier;
import com.carmotorsproject.utils.SwingUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Swing UI for managing purchase orders.
 */
public class PurchaseOrderView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(PurchaseOrderView.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    // Controller
    private PurchaseOrderController controller;

    // UI Components
    private JPanel contentPane;
    private JTextField txtOrderId;
    private JComboBox<Supplier> cmbSupplier;
    private JTextField txtOrderDate;
    private JTextField txtExpectedDate;
    private JComboBox<String> cmbStatus;
    private JTextArea txtNotes;
    private JTextField txtPartSearch;
    private JComboBox<Part> cmbPart;
    private JTextField txtQuantity;
    private JTextField txtUnitPrice;
    private JTable tblOrderDetails;
    private DefaultTableModel detailsTableModel;
    private JTable tblOrders;
    private DefaultTableModel ordersTableModel;
    private JButton btnAddDetail;
    private JButton btnRemoveDetail;
    private JButton btnCreateOrder;
    private JButton btnUpdateOrder;
    private JButton btnSendOrder;
    private JButton btnCancelOrder;
    private JButton btnClear;
    private JLabel lblTotal;

    // Data
    private List<PurchaseOrderDetail> orderDetails = new ArrayList<>();

    /**
     * Create the frame.
     */
    public PurchaseOrderView() {
        initComponents();
        controller = new PurchaseOrderController(this);
        loadInitialData();
    }

    /**
     * Initialize UI components.
     */
    private void initComponents() {
        setTitle("Gestión de Órdenes de Compra");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 700);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Create Order Tab
        JPanel createOrderPanel = createOrderPanel();
        tabbedPane.addTab("Crear Orden", createOrderPanel);

        // View Orders Tab
        JPanel viewOrdersPanel = viewOrdersPanel();
        tabbedPane.addTab("Ver Órdenes", viewOrdersPanel);
    }

    /**
     * Create the order creation panel.
     *
     * @return The order creation panel
     */
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Order Header Panel
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBorder(BorderFactory.createTitledBorder("Datos de la Orden"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Order ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        headerPanel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtOrderId = new JTextField();
        txtOrderId.setEditable(false);
        headerPanel.add(txtOrderId, gbc);
        txtOrderId.setColumns(10);

        // Supplier
        gbc.gridx = 0;
        gbc.gridy = 1;
        headerPanel.add(new JLabel("Proveedor:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        cmbSupplier = new JComboBox<>();
        headerPanel.add(cmbSupplier, gbc);
        gbc.gridwidth = 1;

        // Order Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        headerPanel.add(new JLabel("Fecha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        txtOrderDate = new JTextField();
        txtOrderDate.setText(DATE_FORMAT.format(new Date()));
        headerPanel.add(txtOrderDate, gbc);
        txtOrderDate.setColumns(10);

        // Expected Date
        gbc.gridx = 2;
        gbc.gridy = 2;
        headerPanel.add(new JLabel("Fecha Esperada:"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        txtExpectedDate = new JTextField();
        headerPanel.add(txtExpectedDate, gbc);
        txtExpectedDate.setColumns(10);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 3;
        headerPanel.add(new JLabel("Estado:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        cmbStatus = new JComboBox<>(new String[]{"PENDIENTE", "ENVIADA", "RECIBIDA", "CANCELADA"});
        headerPanel.add(cmbStatus, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 4;
        headerPanel.add(new JLabel("Notas:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        txtNotes = new JTextArea();
        txtNotes.setRows(3);
        JScrollPane notesScrollPane = new JScrollPane(txtNotes);
        headerPanel.add(notesScrollPane, gbc);
        gbc.gridwidth = 1;

        // Order Details Panel
        JPanel detailsPanel = new JPanel(new BorderLayout(10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Detalles de la Orden"));

        // Add Detail Panel
        JPanel addDetailPanel = new JPanel(new GridBagLayout());

        // Part Search
        gbc.gridx = 0;
        gbc.gridy = 0;
        addDetailPanel.add(new JLabel("Buscar Repuesto:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtPartSearch = new JTextField();
        addDetailPanel.add(txtPartSearch, gbc);
        txtPartSearch.setColumns(15);

        JButton btnSearchPart = new JButton("Buscar");
        btnSearchPart.addActionListener(this::btnSearchPartActionPerformed);
        gbc.gridx = 2;
        gbc.gridy = 0;
        addDetailPanel.add(btnSearchPart, gbc);

        // Part
        gbc.gridx = 0;
        gbc.gridy = 1;
        addDetailPanel.add(new JLabel("Repuesto:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        cmbPart = new JComboBox<>();
        addDetailPanel.add(cmbPart, gbc);
        gbc.gridwidth = 1;

        // Quantity
        gbc.gridx = 3;
        gbc.gridy = 1;
        addDetailPanel.add(new JLabel("Cantidad:"), gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        txtQuantity = new JTextField();
        addDetailPanel.add(txtQuantity, gbc);
        txtQuantity.setColumns(5);

        // Unit Price
        gbc.gridx = 5;
        gbc.gridy = 1;
        addDetailPanel.add(new JLabel("Precio Unitario:"), gbc);

        gbc.gridx = 6;
        gbc.gridy = 1;
        txtUnitPrice = new JTextField();
        addDetailPanel.add(txtUnitPrice, gbc);
        txtUnitPrice.setColumns(10);

        // Add Detail Button
        btnAddDetail = new JButton("Agregar Detalle");
        btnAddDetail.addActionListener(this::btnAddDetailActionPerformed);
        gbc.gridx = 7;
        gbc.gridy = 1;
        addDetailPanel.add(btnAddDetail, gbc);

        // Details Table
        String[] detailsColumnNames = {"ID", "Repuesto", "Cantidad", "Precio Unitario", "Subtotal"};
        detailsTableModel = new DefaultTableModel(detailsColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblOrderDetails = new JTable(detailsTableModel);
        JScrollPane detailsScrollPane = new JScrollPane(tblOrderDetails);

        // Remove Detail Button
        btnRemoveDetail = new JButton("Eliminar Detalle");
        btnRemoveDetail.addActionListener(this::btnRemoveDetailActionPerformed);

        // Total Label
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Dialog", Font.BOLD, 14));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel detailsButtonPanel = new JPanel(new BorderLayout());
        detailsButtonPanel.add(btnRemoveDetail, BorderLayout.WEST);
        detailsButtonPanel.add(lblTotal, BorderLayout.EAST);

        detailsPanel.add(addDetailPanel, BorderLayout.NORTH);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);
        detailsPanel.add(detailsButtonPanel, BorderLayout.SOUTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnCreateOrder = new JButton("Crear Orden");
        btnCreateOrder.addActionListener(this::btnCreateOrderActionPerformed);
        buttonsPanel.add(btnCreateOrder);

        btnUpdateOrder = new JButton("Actualizar Orden");
        btnUpdateOrder.addActionListener(this::btnUpdateOrderActionPerformed);
        buttonsPanel.add(btnUpdateOrder);

        btnSendOrder = new JButton("Enviar Orden");
        btnSendOrder.addActionListener(this::btnSendOrderActionPerformed);
        buttonsPanel.add(btnSendOrder);

        btnClear = new JButton("Limpiar");
        btnClear.addActionListener(this::btnClearActionPerformed);
        buttonsPanel.add(btnClear);

        // Add components to panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Create the orders view panel.
     *
     * @return The orders view panel
     */
    private JPanel viewOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchPanel.add(new JLabel("Estado:"));
        JComboBox<String> cmbFilterStatus = new JComboBox<>(new String[]{"TODOS", "PENDIENTE", "ENVIADA", "RECIBIDA", "CANCELADA"});
        searchPanel.add(cmbFilterStatus);

        searchPanel.add(new JLabel("Proveedor:"));
        JComboBox<Supplier> cmbFilterSupplier = new JComboBox<>();
        cmbFilterSupplier.addItem(null); // Add empty option
        searchPanel.add(cmbFilterSupplier);

        JButton btnFilter = new JButton("Filtrar");
        btnFilter.addActionListener(e -> {
            String status = cmbFilterStatus.getSelectedItem().toString();
            Supplier supplier = (Supplier) cmbFilterSupplier.getSelectedItem();

            if ("TODOS".equals(status)) {
                status = null;
            }

            controller.deleteOrder(status, supplier != null ? supplier.getId() : null);
        });
        searchPanel.add(btnFilter);

        JButton btnRefresh = new JButton("Actualizar");
        btnRefresh.addActionListener(e -> controller.loadAllOrders());
        searchPanel.add(btnRefresh);

        // Orders Table
        String[] ordersColumnNames = {"ID", "Proveedor", "Fecha", "Fecha Esperada", "Estado", "Total", "Notas"};
        ordersTableModel = new DefaultTableModel(ordersColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblOrders = new JTable(ordersTableModel);
        tblOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblOrders.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblOrders.getSelectedRow();
                    if (row != -1) {
                        int orderId = Integer.parseInt(tblOrders.getValueAt(row, 0).toString());
                        controller.loadOrder(orderId);
                    }
                }
            }
        });

        JScrollPane ordersScrollPane = new JScrollPane(tblOrders);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnCancelOrder = new JButton("Cancelar Orden");
        btnCancelOrder.addActionListener(this::btnCancelOrderActionPerformed);
        buttonsPanel.add(btnCancelOrder);

        // Add components to panel
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(ordersScrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Load initial data.
     */
    private void loadInitialData() {
        controller.loadSuppliers();
        controller.loadParts();
        controller.loadAllOrders();
    }

    /**
     * Update the suppliers combo box.
     *
     * @param suppliers List of suppliers
     */
    public void updateSupplierCombo(List<Supplier> suppliers) {
        cmbSupplier.removeAllItems();

        for (Supplier supplier : suppliers) {
            cmbSupplier.addItem(supplier);
        }

        LOGGER.log(Level.INFO, "Updated supplier combo with {0} suppliers", suppliers.size());
    }

    /**
     * Update the parts combo box.
     *
     * @param parts List of parts
     */
    public void updatePartCombo(List<Part> parts) {
        cmbPart.removeAllItems();

        for (Part part : parts) {
            cmbPart.addItem(part);
        }

        LOGGER.log(Level.INFO, "Updated part combo with {0} parts", parts.size());
    }

    /**
     * Update the order details table.
     *
     * @param details List of purchase order details
     */
    public void updateDetailTable(List<PurchaseOrderDetail> details) {
        this.orderDetails = new ArrayList<>(details);
        updateOrderDetailsTable();
        LOGGER.log(Level.INFO, "Updated order details table with {0} items", details.size());
    }

    /**
     * Update the order details table.
     */
    private void updateOrderDetailsTable() {
        detailsTableModel.setRowCount(0);
        double total = 0.0;

        for (PurchaseOrderDetail detail : orderDetails) {
            double subtotal = detail.getQuantity() * detail.getUnitPrice();
            total += subtotal;

            Object[] row = {
                    detail.getPartId(),
                    detail.getPartName(),
                    detail.getQuantity(),
                    String.format("$%.2f", detail.getUnitPrice()),
                    String.format("$%.2f", subtotal)
            };
            detailsTableModel.addRow(row);
        }

        lblTotal.setText(String.format("Total: $%.2f", total));
        LOGGER.log(Level.INFO, "Updated order details table with {0} items", orderDetails.size());
    }

    /**
     * Update the orders table.
     *
     * @param orders List of purchase orders
     */
    public void updateOrdersTable(List<PurchaseOrder> orders) {
        ordersTableModel.setRowCount(0);

        for (PurchaseOrder order : orders) {
            Object[] row = {
                    order.getId(),
                    order.getSupplier() != null ? order.getSupplier().getName() : "",
                    order.getOrderDate() != null ? DATE_FORMAT.format(order.getOrderDate()) : "",
                    order.getExpectedDate() != null ? DATE_FORMAT.format(order.getExpectedDate()) : "",
                    order.getStatus(),
                    String.format("$%.2f", order.getTotal()),
                    order.getNotes()
            };
            ordersTableModel.addRow(row);
        }

        LOGGER.log(Level.INFO, "Updated orders table with {0} orders", orders.size());
    }

    /**
     * Selects an order in the orders table.
     *
     * @param orderId The ID of the order to select
     */
    public void selectOrder(int orderId) {
        for (int i = 0; i < tblOrders.getRowCount(); i++) {
            int id = Integer.parseInt(tblOrders.getValueAt(i, 0).toString());
            if (id == orderId) {
                tblOrders.setRowSelectionInterval(i, i);
                tblOrders.scrollRectToVisible(tblOrders.getCellRect(i, 0, true));
                break;
            }
        }
        LOGGER.log(Level.INFO, "Selected order with ID: {0}", orderId);
    }

    /**
     * Load an order into the form.
     *
     * @param order The purchase order to load
     */
    public void loadOrderToForm(PurchaseOrder order) {
        txtOrderId.setText(String.valueOf(order.getId()));

        // Set supplier
        for (int i = 0; i < cmbSupplier.getItemCount(); i++) {
            Supplier supplier = cmbSupplier.getItemAt(i);
            if (supplier.getId() == order.getSupplierId()) {
                cmbSupplier.setSelectedIndex(i);
                break;
            }
        }

        txtOrderDate.setText(order.getOrderDate() != null ? DATE_FORMAT.format(order.getOrderDate()) : "");
        txtExpectedDate.setText(order.getExpectedDate() != null ? DATE_FORMAT.format(order.getExpectedDate()) : "");
        cmbStatus.setSelectedItem(order.getStatus());
        txtNotes.setText(order.getNotes());

        // Load order details
        orderDetails = order.getDetails();
        updateOrderDetailsTable();

        // Switch to create order tab
        ((JTabbedPane) contentPane.getComponent(0)).setSelectedIndex(0);

        LOGGER.log(Level.INFO, "Loaded order with ID: {0}", order.getId());
    }

    /**
     * Clear the form.
     */
    public void clearForm() {
        txtOrderId.setText("");
        if (cmbSupplier.getItemCount() > 0) {
            cmbSupplier.setSelectedIndex(0);
        }
        txtOrderDate.setText(DATE_FORMAT.format(new Date()));
        txtExpectedDate.setText("");
        cmbStatus.setSelectedIndex(0);
        txtNotes.setText("");
        txtPartSearch.setText("");
        txtQuantity.setText("");
        txtUnitPrice.setText("");

        orderDetails.clear();
        updateOrderDetailsTable();

        LOGGER.log(Level.INFO, "Form cleared");
    }

    /**
     * Validate the order form.
     *
     * @return True if the form is valid
     */
    private boolean validateOrderForm() {
        StringBuilder errors = new StringBuilder();

        if (cmbSupplier.getSelectedItem() == null) {
            errors.append("- Debe seleccionar un proveedor.\n");
        }

        try {
            if (!txtOrderDate.getText().trim().isEmpty()) {
                DATE_FORMAT.parse(txtOrderDate.getText().trim());
            }
        } catch (ParseException e) {
            errors.append("- La fecha de orden no es válida. Use el formato dd/MM/yyyy.\n");
        }

        try {
            if (!txtExpectedDate.getText().trim().isEmpty()) {
                DATE_FORMAT.parse(txtExpectedDate.getText().trim());
            }
        } catch (ParseException e) {
            errors.append("- La fecha esperada no es válida. Use el formato dd/MM/yyyy.\n");
        }

        if (orderDetails.isEmpty()) {
            errors.append("- Debe agregar al menos un detalle a la orden.\n");
        }

        if (errors.length() > 0) {
            showError("Por favor corrija los siguientes errores:\n" + errors.toString());
            return false;
        }

        return true;
    }

    /**
     * Validate the detail form.
     *
     * @return True if the form is valid
     */
    private boolean validateDetailForm() {
        StringBuilder errors = new StringBuilder();

        if (cmbPart.getSelectedItem() == null) {
            errors.append("- Debe seleccionar un repuesto.\n");
        }

        try {
            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            if (quantity <= 0) {
                errors.append("- La cantidad debe ser mayor que cero.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("- La cantidad debe ser un número entero válido.\n");
        }

        try {
            double unitPrice = Double.parseDouble(txtUnitPrice.getText().trim());
            if (unitPrice <= 0) {
                errors.append("- El precio unitario debe ser mayor que cero.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("- El precio unitario debe ser un número válido.\n");
        }

        if (errors.length() > 0) {
            showError("Por favor corrija los siguientes errores:\n" + errors.toString());
            return false;
        }

        return true;
    }

    /**
     * Create a PurchaseOrder object from form data.
     *
     * @return A new PurchaseOrder object
     */
    private PurchaseOrder createOrderFromForm() {
        PurchaseOrder order = new PurchaseOrder();

        if (!txtOrderId.getText().trim().isEmpty()) {
            order.setId(Integer.parseInt(txtOrderId.getText().trim()));
        }

        Supplier supplier = (Supplier) cmbSupplier.getSelectedItem();
        order.setSupplierId(supplier.getId());
        order.setSupplier(supplier);

        try {
            if (!txtOrderDate.getText().trim().isEmpty()) {
                order.setOrderDate(DATE_FORMAT.parse(txtOrderDate.getText().trim()));
            }
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Error parsing order date", e);
        }

        try {
            if (!txtExpectedDate.getText().trim().isEmpty()) {
                order.setExpectedDate(DATE_FORMAT.parse(txtExpectedDate.getText().trim()));
            }
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Error parsing expected date", e);
        }

        order.setStatus(cmbStatus.getSelectedItem().toString());
        order.setNotes(txtNotes.getText().trim());
        order.setDetails(new ArrayList<>(orderDetails));

        // Calculate total
        double total = 0.0;
        for (PurchaseOrderDetail detail : orderDetails) {
            total += detail.getQuantity() * detail.getUnitPrice();
        }
        order.setTotal(total);

        return order;
    }

    /**
     * Show an error message.
     *
     * @param message The error message
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show an information message.
     *
     * @param message The information message
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show a confirmation dialog.
     *
     * @param message The confirmation message
     * @return True if the user confirms, false otherwise
     */
    public boolean showConfirm(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Handle Search Part button action.
     *
     * @param evt The action event
     */
    private void btnSearchPartActionPerformed(ActionEvent evt) {
        String searchTerm = txtPartSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            controller.loadParts();
        } else {
            controller.searchParts(searchTerm);
        }
    }

    /**
     * Handle Add Detail button action.
     *
     * @param evt The action event
     */
    private void btnAddDetailActionPerformed(ActionEvent evt) {
        if (!validateDetailForm()) {
            return;
        }

        Part part = (Part) cmbPart.getSelectedItem();
        int quantity = Integer.parseInt(txtQuantity.getText().trim());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText().trim());

        // Check if part already exists in order details
        for (PurchaseOrderDetail detail : orderDetails) {
            if (detail.getPartId() == part.getId()) {
                // Update existing detail
                detail.setQuantity(detail.getQuantity() + quantity);
                updateOrderDetailsTable();

                txtPartSearch.setText("");
                txtQuantity.setText("");
                txtUnitPrice.setText("");

                return;
            }
        }

        // Add new detail
        PurchaseOrderDetail detail = new PurchaseOrderDetail();
        detail.setPartId(part.getId());
        detail.setPartName(part.getName());
        detail.setQuantity(quantity);
        detail.setUnitPrice(unitPrice);

        orderDetails.add(detail);
        updateOrderDetailsTable();

        txtPartSearch.setText("");
        txtQuantity.setText("");
        txtUnitPrice.setText("");

        LOGGER.log(Level.INFO, "Added detail for part: {0}", part.getName());
    }

    /**
     * Handle Remove Detail button action.
     *
     * @param evt The action event
     */
    private void btnRemoveDetailActionPerformed(ActionEvent evt) {
        int selectedRow = tblOrderDetails.getSelectedRow();
        if (selectedRow == -1) {
            showError("Debe seleccionar un detalle para eliminar.");
            return;
        }

        orderDetails.remove(selectedRow);
        updateOrderDetailsTable();

        LOGGER.log(Level.INFO, "Removed detail at row: {0}", selectedRow);
    }

    /**
     * Handle Create Order button action.
     *
     * @param evt The action event
     */
    private void btnCreateOrderActionPerformed(ActionEvent evt) {
        if (!validateOrderForm()) {
            return;
        }

        PurchaseOrder order = createOrderFromForm();
        controller.createOrder(order);
    }

    /**
     * Handle Update Order button action.
     *
     * @param evt The action event
     */
    private void btnUpdateOrderActionPerformed(ActionEvent evt) {
        if (txtOrderId.getText().trim().isEmpty()) {
            showError("Debe cargar una orden existente para actualizarla.");
            return;
        }

        if (!validateOrderForm()) {
            return;
        }

        PurchaseOrder order = createOrderFromForm();
        controller.updateOrder(order);
    }

    /**
     * Handle Send Order button action.
     *
     * @param evt The action event
     */
    private void btnSendOrderActionPerformed(ActionEvent evt) {
        if (txtOrderId.getText().trim().isEmpty()) {
            showError("Debe crear o cargar una orden antes de enviarla.");
            return;
        }

        if (!validateOrderForm()) {
            return;
        }

        if (showConfirm("¿Está seguro de enviar esta orden al proveedor?")) {
            PurchaseOrder order = createOrderFromForm();
            order.setStatus("ENVIADA");
            controller.updateOrder(order);
        }
    }

    /**
     * Handle Cancel Order button action.
     *
     * @param evt The action event
     */
    private void btnCancelOrderActionPerformed(ActionEvent evt) {
        int selectedRow = tblOrders.getSelectedRow();
        if (selectedRow == -1) {
            showError("Debe seleccionar una orden para cancelar.");
            return;
        }

        int orderId = Integer.parseInt(tblOrders.getValueAt(selectedRow, 0).toString());
        String status = tblOrders.getValueAt(selectedRow, 4).toString();

        if ("CANCELADA".equals(status) || "RECIBIDA".equals(status)) {
            showError("No se puede cancelar una orden que ya está cancelada o recibida.");
            return;
        }

        if (showConfirm("¿Está seguro de cancelar esta orden?")) {
            controller.cancelOrder(orderId);
        }
    }

    /**
     * Handle Clear button action.
     *
     * @param evt The action event
     */
    private void btnClearActionPerformed(ActionEvent evt) {
        clearForm();
    }

    /**
     * Main method to launch the view.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SwingUtils.setLookAndFeel();

        EventQueue.invokeLater(() -> {
            try {
                PurchaseOrderView frame = new PurchaseOrderView();
                frame.setVisible(true);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error launching PurchaseOrderView", e);
            }
        });
    }
}