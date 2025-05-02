package com.carmotorsproject.main;

import com.carmotorsproject.utils.UITheme;
import com.carmotorsproject.utils.AnimationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Dashboard panel showing summary information and quick access to common tasks
 */
public class DashboardPanel extends JPanel {
    
    private JPanel statsPanel;
    private JPanel pendingServicesPanel;
    private JPanel lowStockPanel;
    private JPanel recentInvoicesPanel;
    private JPanel quickActionsPanel;
    
    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.WHITE);
        
        initComponents();
        setupLayout();
        loadData();
    }
    
    private void initComponents() {
        // Stats panel with key metrics
        statsPanel = createStatsPanel();
        
        // Pending services panel
        pendingServicesPanel = createPendingServicesPanel();
        
        // Low stock parts panel
        lowStockPanel = createLowStockPanel();
        
        // Recent invoices panel
        recentInvoicesPanel = createRecentInvoicesPanel();
        
        // Quick actions panel
        quickActionsPanel = createQuickActionsPanel();
    }
    
    private void setupLayout() {
        // Top panel with stats
        add(statsPanel, BorderLayout.NORTH);
        
        // Center panel with tables
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(UITheme.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left side with pending services and low stock
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        leftPanel.setBackground(UITheme.WHITE);
        leftPanel.add(pendingServicesPanel);
        leftPanel.add(lowStockPanel);
        
        // Right side with recent invoices
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(UITheme.WHITE);
        rightPanel.add(recentInvoicesPanel, BorderLayout.CENTER);
        
        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel with quick actions
        add(quickActionsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setBackground(UITheme.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Services today
        JPanel servicesPanel = createStatCard("Servicios Hoy", "5", UITheme.PRIMARY_RED);
        
        // Pending services
        JPanel pendingPanel = createStatCard("Servicios Pendientes", "12", UITheme.SECONDARY_RED);
        
        // Completed services this month
        JPanel completedPanel = createStatCard("Completados (Mes)", "45", UITheme.DARK_GRAY);
        
        // Revenue this month
        JPanel revenuePanel = createStatCard("Ingresos (Mes)", "$5,250.00", UITheme.MEDIUM_GRAY);
        
        panel.add(servicesPanel);
        panel.add(pendingPanel);
        panel.add(completedPanel);
        panel.add(revenuePanel);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.SMALL_FONT);
        titleLabel.setForeground(UITheme.MEDIUM_GRAY);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UITheme.TITLE_FONT);
        valueLabel.set.setFont(UITheme.TITLE_FONT);
        valueLabel.setForeground(color);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        // Add hover effect
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(245, 245, 245));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(UITheme.WHITE);
            }
        });
        
        return panel;
    }
    
    private JPanel createPendingServicesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Servicios Pendientes"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Table for pending services
        String[] columns = {"ID", "Vehículo", "Tipo", "Estado", "Fecha"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        UITheme.applyTableTheme(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createLowStockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Repuestos con Bajo Stock"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Table for low stock parts
        String[] columns = {"ID", "Repuesto", "Stock Actual", "Stock Mínimo"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        UITheme.applyTableTheme(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRecentInvoicesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Facturas Recientes"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Table for recent invoices
        String[] columns = {"#", "Cliente", "Fecha", "Total", "Estado"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        UITheme.applyTableTheme(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(UITheme.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Quick action buttons
        JButton newServiceButton = new JButton("Nuevo Servicio");
        JButton newCustomerButton = new JButton("Nuevo Cliente");
        JButton newVehicleButton = new JButton("Nuevo Vehículo");
        JButton generateInvoiceButton = new JButton("Generar Factura");
        
        UITheme.applyPrimaryButtonTheme(newServiceButton);
        UITheme.applySecondaryButtonTheme(newCustomerButton);
        UITheme.applySecondaryButtonTheme(newVehicleButton);
        UITheme.applySecondaryButtonTheme(generateInvoiceButton);
        
        AnimationUtil.applyButtonHoverEffect(newServiceButton);
        AnimationUtil.applyButtonHoverEffect(newCustomerButton);
        AnimationUtil.applyButtonHoverEffect(newVehicleButton);
        AnimationUtil.applyButtonHoverEffect(generateInvoiceButton);
        
        panel.add(newServiceButton);
        panel.add(newCustomerButton);
        panel.add(newVehicleButton);
        panel.add(generateInvoiceButton);
        
        return panel;
    }
    
    private void loadData() {
        // Load pending services data
        DefaultTableModel pendingModel = (DefaultTableModel) ((JScrollPane) pendingServicesPanel.getComponent(0)).getViewport().getView().getModel();
        pendingModel.addRow(new Object[]{"1", "ABC123 - Toyota Corolla", "Preventivo", "Pendiente", "2023-05-20"});
        pendingModel.addRow(new Object[]{"2", "XYZ789 - Honda Civic", "Correctivo", "En Progreso", "2023-05-19"});
        pendingModel.addRow(new Object[]{"3", "DEF456 - Ford Focus", "Preventivo", "Pendiente", "2023-05-18"});
        
        // Load low stock parts data
        DefaultTableModel stockModel = (DefaultTableModel) ((JScrollPane) lowStockPanel.getComponent(0)).getViewport().getView().getModel();
        stockModel.addRow(new Object[]{"1", "Filtro de aceite", "2", "5"});
        stockModel.addRow(new Object[]{"2", "Pastillas de freno", "1", "3"});
        stockModel.addRow(new Object[]{"3", "Bujías", "3", "10"});
        
        // Load recent invoices data
        DefaultTableModel invoiceModel = (DefaultTableModel) ((JScrollPane) recentInvoicesPanel.getComponent(0)).getViewport().getView().getModel();
        invoiceModel.addRow(new Object[]{"INV-001", "Juan Pérez", "2023-05-17", "$120.00", "Pagada"});
        invoiceModel.addRow(new Object[]{"INV-002", "María López", "2023-05-16", "$350.50", "Pendiente"});
        invoiceModel.addRow(new Object[]{"INV-003", "Carlos Rodríguez", "2023-05-15", "$75.25", "Pagada"});
        invoiceModel.addRow(new Object[]{"INV-004", "Ana Martínez", "2023-05-14", "$200.00", "Pagada"});
    }
}