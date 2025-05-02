package com.carmotorsproject.invoices.views;

import com.carmotorsproject.invoices.model.Invoice;
import com.carmotorsproject.utils.UITheme;
import com.carmotorsproject.utils.AnimationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Invoice management view
 */
public class InvoiceView extends JPanel {
    
    private JTable invoiceTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton generateButton;
    private JButton viewButton;
    private JButton printButton;
    private JButton emailButton;
    private JPanel detailsPanel;
    
    // Controller reference will be added here
    
    public InvoiceView() {
        setLayout(new BorderLayout());
        setBackground(UITheme.WHITE);
        
        initComponents();
        setupLayout();
        setupListeners();
    }
    
    private void initComponents() {
        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(UITheme.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        searchField = new JTextField(20);
        UITheme.applyTextFieldTheme(searchField);
        
        JButton searchButton = new JButton("Buscar");
        UITheme.applyPrimaryButtonTheme(searchButton);
        AnimationUtil.applyButtonHoverEffect(searchButton);
        
        searchPanel.add(new JLabel("Buscar factura:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Table panel
        String[] columnNames = {"#", "Servicio", "Cliente", "Vehículo", "Fecha", "Subtotal", "Impuestos", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        invoiceTable = new JTable(tableModel);
        UITheme.applyTableTheme(invoiceTable);
        
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        generateButton = new JButton("Generar Factura");
        viewButton = new JButton("Ver Factura");
        printButton = new JButton("Imprimir");
        emailButton = new JButton("Enviar por Email");
        
        UITheme.applyPrimaryButtonTheme(generateButton);
        UITheme.applySecondaryButtonTheme(viewButton);
        UITheme.applySecondaryButtonTheme(printButton);
        UITheme.applySecondaryButtonTheme(emailButton);
        
        AnimationUtil.applyButtonHoverEffect(generateButton);
        AnimationUtil.applyButtonHoverEffect(viewButton);
        AnimationUtil.applyButtonHoverEffect(printButton);
        AnimationUtil.applyButtonHoverEffect(emailButton);
        
        buttonPanel.add(generateButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(printButton);
        buttonPanel.add(emailButton);
        
        // Details panel (initially hidden)
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBackground(UITheme.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        detailsPanel.setVisible(false);
    }
    
    private void setupLayout() {
        // Search panel at the top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UITheme.WHITE);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UITheme.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        searchPanel.add(new JLabel("Buscar:"));
        searchPanel.add(searchField);
        
        JButton searchButton = new JButton("Buscar");
        UITheme.applyPrimaryButtonTheme(searchButton);
        searchPanel.add(searchButton);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(generateButton, BorderLayout.EAST);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UITheme.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        // Table with scroll pane
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel at the bottom of the table
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.add(viewButton);
        buttonPanel.add(printButton);
        buttonPanel.add(emailButton);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add all panels to the main panel
        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);
    }
    
    private void setupListeners() {
        // Generate button action
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGenerateInvoiceDialog();
            }
        });
        
        // View button action
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = invoiceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    showInvoiceDetails(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(InvoiceView.this,
                            "Por favor, seleccione una factura para ver.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // Print button action
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = invoiceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    printInvoice(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(InvoiceView.this,
                            "Por favor, seleccione una factura para imprimir.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // Email button action
        emailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = invoiceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    emailInvoice(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(InvoiceView.this,
                            "Por favor, seleccione una factura para enviar por email.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    
    // Show dialog to generate a new invoice
    private void showGenerateInvoiceDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Generar Factura", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Service selection
        JPanel servicePanel = new JPanel(new BorderLayout());
        servicePanel.setBackground(UITheme.WHITE);
        
        JLabel serviceLabel = new JLabel("Seleccione un servicio completado:");
        serviceLabel.setFont(UITheme.SUBTITLE_FONT);
        
        String[] serviceColumns = {"ID", "Vehículo", "Cliente", "Tipo", "Estado"};
        DefaultTableModel serviceModel = new DefaultTableModel(serviceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable serviceTable = new JTable(serviceModel);
        UITheme.applyTableTheme(serviceTable);
        
        // Add some dummy data
        serviceModel.addRow(new Object[]{"1", "ABC123 - Toyota Corolla", "Juan Pérez", "Preventivo", "Completado"});
        serviceModel.addRow(new Object[]{"2", "XYZ789 - Honda Civic", "María López", "Correctivo", "Completado"});
        
        JScrollPane serviceScrollPane = new JScrollPane(serviceTable);
        
        servicePanel.add(serviceLabel, BorderLayout.NORTH);
        servicePanel.add(serviceScrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UITheme.WHITE);
        
        JButton generateButton = new JButton("Generar");
        JButton cancelButton = new JButton("Cancelar");
        
        UITheme.applyPrimaryButtonTheme(generateButton);
        UITheme.applySecondaryButtonTheme(cancelButton);
        
        AnimationUtil.applyButtonHoverEffect(generateButton);
        AnimationUtil.applyButtonHoverEffect(cancelButton);
        
        buttonPanel.add(generateButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        panel.add(servicePanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(panel);
        
        // Button actions
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = serviceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    generateInvoice(selectedRow);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Por favor, seleccione un servicio para generar la factura.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        dialog.setVisible(true);
    }
    
    // Generate invoice for selected service
    private void generateInvoice(int serviceRow) {
        // TODO: Generate invoice using controller
        
        // For now, just add to table model
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        
        Object[] row = {
                "INV-" + (tableModel.getRowCount() + 1),
                "1", // Service ID
                "Juan Pérez", // Customer
                "ABC123 - Toyota Corolla", // Vehicle
                today,
                "$100.00", // Subtotal
                "$19.00", // Taxes
                "$119.00" // Total
        };
        
        tableModel.addRow(row);
        
        // Show success message
        JOptionPane.showMessageDialog(this,
                "Factura generada exitosamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Show invoice details
    private void showInvoiceDetails(int row) {
        // Create details content
        detailsPanel.removeAll();
        
        // Invoice header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.WHITE);
        
        JLabel titleLabel = new JLabel("FACTURA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(UITheme.PRIMARY_RED);
        
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        infoPanel.setBackground(UITheme.WHITE);
        
        String invoiceNumber = (String) tableModel.getValueAt(row, 0);
        String customer = (String) tableModel.getValueAt(row, 2);
        String vehicle = (String) tableModel.getValueAt(row, 3);
        String date = (String) tableModel.getValueAt(row, 4);
        
        infoPanel.add(createBoldLabel("Factura No:"));
        infoPanel.add(new JLabel(invoiceNumber));
        infoPanel.add(createBoldLabel("Fecha:"));
        infoPanel.add(new JLabel(date));
        infoPanel.add(createBoldLabel("Cliente:"));
        infoPanel.add(new JLabel(customer));
        infoPanel.add(createBoldLabel("Vehículo:"));
        infoPanel.add(new JLabel(vehicle));
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Invoice items
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setBackground(UITheme.WHITE);
        itemsPanel.setBorder(BorderFactory.createTitledBorder("Detalle de Servicios y Repuestos"));
        
        String[] itemColumns = {"Descripción", "Cantidad", "Precio Unitario", "Subtotal"};
        DefaultTableModel itemsModel = new DefaultTableModel(itemColumns, 0);
        JTable itemsTable = new JTable(itemsModel);
        UITheme.applyTableTheme(itemsTable);
        
        // Add some dummy data
        itemsModel.addRow(new Object[]{"Cambio de aceite", "1", "$50.00", "$50.00"});
        itemsModel.addRow(new Object[]{"Filtro de aceite", "1", "$15.00", "$15.00"});
        itemsModel.addRow(new Object[]{"Aceite de motor", "5", "$7.00", "$35.00"});
        
        JScrollPane itemsScrollPane = new JScrollPane(itemsTable);
        itemsPanel.add(itemsScrollPane, BorderLayout.CENTER);
        
        // Totals panel
        JPanel totalsPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        totalsPanel.setBackground(UITheme.WHITE);
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String subtotal = (String) tableModel.getValueAt(row, 5);
        String taxes = (String) tableModel.getValueAt(row, 6);
        String total = (String) tableModel.getValueAt(row, 7);
        
        totalsPanel.add(new JLabel(""));
        totalsPanel.add(new JLabel(""));
        totalsPanel.add(createBoldLabel("Subtotal:"));
        totalsPanel.add(new JLabel(subtotal, SwingConstants.RIGHT));
        totalsPanel.add(createBoldLabel("Impuestos (19%):"));
        totalsPanel.add(new JLabel(taxes, SwingConstants.RIGHT));
        totalsPanel.add(createBoldLabel("Total:"));
        
        JLabel totalLabel = new JLabel(total, SwingConstants.RIGHT);
        totalLabel.setFont(new Font(totalLabel.getFont().getName(), Font.BOLD, 16));
        totalLabel.setForeground(UITheme.PRIMARY_RED);
        
        totalsPanel.add(totalLabel);
        
        // QR code placeholder
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setBackground(UITheme.WHITE);
        qrPanel.setBorder(BorderFactory.createTitledBorder("Código QR"));
        
        JLabel qrLabel = new JLabel("QR Code Placeholder", SwingConstants.CENTER);
        qrLabel.setPreferredSize(new Dimension(150, 150));
        qrLabel.setBorder(BorderFactory.createLineBorder(UITheme.LIGHT_GRAY));
        
        qrPanel.add(qrLabel, BorderLayout.CENTER);
        
        // Bottom panel with QR and totals
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(UITheme.WHITE);
        
        bottomPanel.add(qrPanel, BorderLayout.WEST);
        bottomPanel.add(totalsPanel, BorderLayout.EAST);
        
        // Close button
        JButton closeButton = new JButton("Cerrar");
        UITheme.applySecondaryButtonTheme(closeButton);
        AnimationUtil.applyButtonHoverEffect(closeButton);
        
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnimationUtil.fadeOut(detailsPanel, 300);
                detailsPanel.setVisible(false);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.add(closeButton);
        
        // Add all to details panel
        detailsPanel.add(headerPanel, BorderLayout.NORTH);
        detailsPanel.add(itemsPanel, BorderLayout.CENTER);
        detailsPanel.add(bottomPanel, BorderLayout.SOUTH);
        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show details panel with animation
        detailsPanel.setVisible(true);
        AnimationUtil.fadeIn(detailsPanel, 300);
    }
    
    // Print invoice
    private void printInvoice(int row) {
        // TODO: Implement printing functionality
        
        JOptionPane.showMessageDialog(this,
                "La funcionalidad de impresión será implementada en una versión futura.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Email invoice
    private void emailInvoice(int row) {
        // TODO: Implement email functionality
        
        JOptionPane.showMessageDialog(this,
                "La funcionalidad de envío por email será implementada en una versión futura.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
        return label;
    }
    
    // Method to update the table with invoice data
    public void updateInvoiceTable(List<Invoice> invoices) {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Add invoices to the table
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Invoice invoice : invoices) {
            Object[] row = {
                    invoice.getInvoiceNumber(),
                    invoice.getServiceId(),
                    "Customer Name", // This would come from the customer object in a real implementation
                    "Vehicle Info", // This would come from the vehicle object in a real implementation
                    invoice.getIssueDate() != null ? dateFormat.format(invoice.getIssueDate()) : "",
                    String.format("$%.2f", invoice.getSubtotal()),
                    String.format("$%.2f", invoice.getTaxes()),
                    String.format("$%.2f", invoice.getTotal())
            };
            tableModel.addRow(row);
        }
    }
}