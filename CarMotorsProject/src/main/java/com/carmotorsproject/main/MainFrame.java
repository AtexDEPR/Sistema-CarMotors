package com.carmotorsproject.main;

import com.carmotorsproject.utils.UITheme;
import com.carmotorsproject.utils.AnimationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Main application frame that serves as the container for all UI components
 */
public class MainFrame extends JFrame {
    
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private Map<String, JPanel> panelCache = new HashMap<>();
    private CardLayout contentCardLayout;
    
    public MainFrame() {
        setTitle("CarMotors - Sistema de Gestión de Taller Automotriz");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        setupLayout();
        
        // Show dashboard as the initial view
        showPanel("dashboard");
    }
    
    private void initComponents() {
        // Header panel
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.DARK_GRAY);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        // Logo and title
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(UITheme.DARK_GRAY);
        
        JLabel logoLabel = new JLabel("CarMotors");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(UITheme.PRIMARY_RED);
        logoPanel.add(logoLabel);
        
        // Title label for current section
        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setForeground(UITheme.WHITE);
        
        // User info panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(UITheme.DARK_GRAY);
        
        JLabel userLabel = new JLabel("Admin");
        userLabel.setFont(UITheme.REGULAR_FONT);
        userLabel.setForeground(UITheme.WHITE);
        
        JButton logoutButton = new JButton("Cerrar Sesión");
        UITheme.applySecondaryButtonTheme(logoutButton);
        AnimationUtil.applyButtonHoverEffect(logoutButton);
        
        userPanel.add(userLabel);
        userPanel.add(logoutButton);
        
        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        // Sidebar panel
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(UITheme.MEDIUM_GRAY);
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        
        // Add menu buttons to sidebar
        addSidebarButton("Dashboard", "dashboard");
        addSidebarButton("Vehículos", "vehicles");
        addSidebarButton("Clientes", "customers");
        addSidebarButton("Servicios", "services");
        addSidebarButton("Repuestos", "parts");
        addSidebarButton("Proveedores", "suppliers");
        addSidebarButton("Técnicos", "technicians");
        addSidebarButton("Facturas", "invoices");
        addSidebarButton("Campañas", "campaigns");
        addSidebarButton("Reportes", "reports");
        
        // Content panel with card layout
        contentCardLayout = new CardLayout();
        contentPanel = new JPanel(contentCardLayout);
        contentPanel.setBackground(UITheme.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        add(headerPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void addSidebarButton(String text, final String panelName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 50));
        button.setPreferredSize(new Dimension(220, 50));
        
        // Custom styling
        button.setBackground(UITheme.MEDIUM_GRAY);
        button.setForeground(UITheme.WHITE);
        button.setFont(UITheme.REGULAR_FONT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(UITheme.PRIMARY_RED);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!titleLabel.getText().equals(button.getText())) {
                    button.setBackground(UITheme.MEDIUM_GRAY);
                }
            }
        });
        
        // Action to switch panels
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel(panelName);
                titleLabel.setText(button.getText());
                
                // Update button states
                for (Component c : sidebarPanel.getComponents()) {
                    if (c instanceof JButton) {
                        c.setBackground(UITheme.MEDIUM_GRAY);
                    }
                }
                button.setBackground(UITheme.PRIMARY_RED);
            }
        });
        
        sidebarPanel.add(button);
        
        // Add separator
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(220, 1));
        separator.setForeground(UITheme.DARK_GRAY);
        sidebarPanel.add(separator);
    }
    
    public void showPanel(String name) {
        // Check if panel is already in cache
        if (!panelCache.containsKey(name)) {
            // Create panel based on name
            JPanel panel = createPanel(name);
            if (panel != null) {
                contentPanel.add(panel, name);
                panelCache.put(name, panel);
            }
        }
        
        // Show the panel
        contentCardLayout.show(contentPanel, name);
        
        // Apply animation
        JPanel currentPanel = panelCache.get(name);
        if (currentPanel != null) {
            AnimationUtil.fadeIn(currentPanel, 300);
        }
    }
    
    private JPanel createPanel(String name) {
        switch (name) {
            case "dashboard":
                return new DashboardPanel();
            case "vehicles":
                // Return vehicle panel when implemented
                return new JPanel(); // Placeholder
            case "customers":
                // Return customer panel when implemented
                return new JPanel(); // Placeholder
            case "services":
                // Return services panel when implemented
                return new JPanel(); // Placeholder
            case "parts":
                // Return parts panel when implemented
                return new JPanel(); // Placeholder
            case "suppliers":
                // Return suppliers panel when implemented
                return new JPanel(); // Placeholder
            case "technicians":
                // Return technicians panel when implemented
                return new JPanel(); // Placeholder
            case "invoices":
                // Return invoices panel when implemented
                return new JPanel(); // Placeholder
            case "campaigns":
                // Return campaigns panel when implemented
                return new JPanel(); // Placeholder
            case "reports":
                // Return reports panel when implemented
                return new JPanel(); // Placeholder
            default:
                return null;
        }
    }
    
    // Inner class for Dashboard Panel
    private class DashboardPanel extends JPanel {
        public DashboardPanel() {
            setLayout(new BorderLayout());
            setBackground(UITheme.WHITE);
            
            // Dashboard content will be implemented later
            JLabel placeholderLabel = new JLabel("Dashboard Content Coming Soon", SwingConstants.CENTER);
            placeholderLabel.setFont(UITheme.TITLE_FONT);
            add(placeholderLabel, BorderLayout.CENTER);
        }
    }
}