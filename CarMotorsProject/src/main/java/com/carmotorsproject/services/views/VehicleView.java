package com.carmotorsproject.services.views;

import com.carmotorsproject.services.model.Vehicle;
import com.carmotorsproject.utils.UITheme;
import com.carmotorsproject.utils.AnimationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vehicle management view
 */
public class VehicleView extends JPanel {
    
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewDetailsButton;
    private JPanel formPanel;
    private JPanel detailsPanel;
    
    // Form fields
    private JTextField makeField;
    private JTextField modelField;
    private JTextField licensePlateField;
    private JTextField typeField;
    private JComboBox<String> customerComboBox;
    
    // Controller reference will be added here
    
    public VehicleView() {
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
        
        searchPanel.add(new JLabel("Buscar vehículo:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Table panel
        String[] columnNames = {"ID", "Placa", "Marca", "Modelo", "Tipo", "Cliente"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        vehicleTable = new JTable(tableModel);
        UITheme.applyTableTheme(vehicleTable);
        
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        addButton = new JButton("Agregar Vehículo");
        editButton = new JButton("Editar Vehículo");
        deleteButton = new JButton("Eliminar Vehículo");
        viewDetailsButton = new JButton("Ver Historial");
        
        UITheme.applyPrimaryButtonTheme(addButton);
        UITheme.applySecondaryButtonTheme(editButton);
        UITheme.applySecondaryButtonTheme(deleteButton);
        UITheme.applySecondaryButtonTheme(viewDetailsButton);
        
        AnimationUtil.applyButtonHoverEffect(addButton);
        AnimationUtil.applyButtonHoverEffect(editButton);
        AnimationUtil.applyButtonHoverEffect(deleteButton);
        AnimationUtil.applyButtonHoverEffect(viewDetailsButton);
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewDetailsButton);
        
        // Form panel (initially hidden)
        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(UITheme.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setVisible(false);
        
        // Form fields
        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        fieldsPanel.setBackground(UITheme.WHITE);
        
        makeField = new JTextField(20);
        modelField = new JTextField(20);
        licensePlateField = new JTextField(20);
        typeField = new JTextField(20);
        customerComboBox = new JComboBox<String>();
        
        UITheme.applyTextFieldTheme(makeField);
        UITheme.applyTextFieldTheme(modelField);
        UITheme.applyTextFieldTheme(licensePlateField);
        UITheme.applyTextFieldTheme(typeField);
        UITheme.applyComboBoxTheme(customerComboBox);
        
        // Add some dummy customer data
        customerComboBox.addItem("Seleccione un cliente");
        customerComboBox.addItem("Juan Pérez");
        customerComboBox.addItem("María López");
        customerComboBox.addItem("Carlos Rodríguez");
        
        fieldsPanel.add(new JLabel("Marca:"));
        fieldsPanel.add(makeField);
        fieldsPanel.add(new JLabel("Modelo:"));
        fieldsPanel.add(modelField);
        fieldsPanel.add(new JLabel("Placa:"));
        fieldsPanel.add(licensePlateField);
        fieldsPanel.add(new JLabel("Tipo:"));
        fieldsPanel.add(typeField);
        fieldsPanel.add(new JLabel("Cliente:"));
        fieldsPanel.add(customerComboBox);
        
        // Form buttons
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        formButtonPanel.setBackground(UITheme.WHITE);
        
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");
        
        UITheme.applyPrimaryButtonTheme(saveButton);
        UITheme.applySecondaryButtonTheme(cancelButton);
        
        AnimationUtil.applyButtonHoverEffect(saveButton);
        AnimationUtil.applyButtonHoverEffect(cancelButton);
        
        formButtonPanel.add(saveButton);
        formButtonPanel.add(cancelButton);
        
        formPanel.add(fieldsPanel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(formButtonPanel);
        
        // Details panel (initially hidden)
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBackground(UITheme.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        detailsPanel.setVisible(false);
        
        // Save button action
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveVehicle();
            }
        });
        
        // Cancel button action
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideForm();
            }
        });
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
        topPanel.add(addButton, BorderLayout.EAST);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UITheme.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        // Table with scroll pane
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel at the bottom of the table
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewDetailsButton);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add all panels to the main panel
        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);
        add(detailsPanel, BorderLayout.SOUTH);
    }
    
    private void setupListeners() {
        // Add button action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddForm();
            }
        });
        
        // Edit button action
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = vehicleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    showEditForm(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(VehicleView.this,
                            "Por favor, seleccione un vehículo para editar.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // Delete button action
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = vehicleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int option = JOptionPane.showConfirmDialog(VehicleView.this,
                            "¿Está seguro de que desea eliminar este vehículo?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION);
                    
                    if (option == JOptionPane.YES_OPTION) {
                        deleteVehicle(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(VehicleView.this,
                            "Por favor, seleccione un vehículo para eliminar.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // View details button action
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = vehicleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    showVehicleHistory(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(VehicleView.this,
                            "Por favor, seleccione un vehículo para ver su historial.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    
    // Show the add vehicle form
    private void showAddForm() {
        // Clear form fields
        makeField.setText("");
        modelField.setText("");
        licensePlateField.setText("");
        typeField.setText("");
        customerComboBox.setSelectedIndex(0);
        
        // Hide details panel if visible
        if (detailsPanel.isVisible()) {
            AnimationUtil.fadeOut(detailsPanel, 300);
            detailsPanel.setVisible(false);
        }
        
        // Show form panel with animation
        formPanel.setVisible(true);
        AnimationUtil.fadeIn(formPanel, 300);
    }
    
    // Show the edit vehicle form
    private void showEditForm(int row) {
        // Populate form fields with selected vehicle data
        makeField.setText((String) tableModel.getValueAt(row, 2));
        modelField.setText((String) tableModel.getValueAt(row, 3));
        licensePlateField.setText((String) tableModel.getValueAt(row, 1));
        typeField.setText((String) tableModel.getValueAt(row, 4));
        
        // Set customer (this is just a placeholder, in a real app you'd match by ID)
        String customerName = (String) tableModel.getValueAt(row, 5);
        for (int i = 0; i < customerComboBox.getItemCount(); i++) {
            if (customerComboBox.getItemAt(i).equals(customerName)) {
                customerComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        // Hide details panel if visible
        if (detailsPanel.isVisible()) {
            AnimationUtil.fadeOut(detailsPanel, 300);
            detailsPanel.setVisible(false);
        }
        
        // Show form panel with animation
        formPanel.setVisible(true);
        AnimationUtil.fadeIn(formPanel, 300);
    }
    
    // Hide the form
    private void hideForm() {
        AnimationUtil.fadeOut(formPanel, 300);
        formPanel.setVisible(false);
    }
    
    // Save vehicle (add or update)
    private void saveVehicle() {
        // Validate form fields
        if (makeField.getText().trim().isEmpty() || 
            modelField.getText().trim().isEmpty() || 
            licensePlateField.getText().trim().isEmpty() ||
            customerComboBox.getSelectedIndex() == 0) {
            
            JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos obligatorios.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create vehicle object
        Vehicle vehicle = new Vehicle();
        vehicle.setMake(makeField.getText().trim());
        vehicle.setModel(modelField.getText().trim());
        vehicle.setLicensePlate(licensePlateField.getText().trim());
        vehicle.setType(typeField.getText().trim());
        
        // TODO: Set customer ID and save vehicle using controller
        
        // For now, just add to table model
        Object[] row = {
                "1", // Placeholder ID
                vehicle.getLicensePlate(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getType(),
                customerComboBox.getSelectedItem()
        };
        
        tableModel.addRow(row);
        
        // Hide form
        hideForm();
        
        // Show success message
        JOptionPane.showMessageDialog(this,
                "Vehículo guardado exitosamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Delete vehicle
    private void deleteVehicle(int row) {
        // TODO: Delete vehicle using controller
        
        // For now, just remove from table model
        tableModel.removeRow(row);
        
        // Show success message
        JOptionPane.showMessageDialog(this,
                "Vehículo eliminado exitosamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Show vehicle maintenance history
    private void showVehicleHistory(int row) {
        // Hide form panel if visible
        if (formPanel.isVisible()) {
            AnimationUtil.fadeOut(formPanel, 300);
            formPanel.setVisible(false);
        }
        
        // Create details content
        detailsPanel.removeAll();
        
        // Vehicle info
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBackground(UITheme.WHITE);
        
        String id = (String) tableModel.getValueAt(row, 0);
        String licensePlate = (String) tableModel.getValueAt(row, 1);
        String make = (String) tableModel.getValueAt(row, 2);
        String model = (String) tableModel.getValueAt(row, 3);
        String type = (String) tableModel.getValueAt(row, 4);
        String customer = (String) tableModel.getValueAt(row, 5);
        
        infoPanel.add(createBoldLabel("ID:"));
        infoPanel.add(new JLabel(id));
        infoPanel.add(createBoldLabel("Placa:"));
        infoPanel.add(new JLabel(licensePlate));
        infoPanel.add(createBoldLabel("Marca:"));
        infoPanel.add(new JLabel(make));
        infoPanel.add(createBoldLabel("Modelo:"));
        infoPanel.add(new JLabel(model));
        infoPanel.add(createBoldLabel("Cliente:"));
        infoPanel.add(new JLabel(customer));
        
        // Maintenance history
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(UITheme.WHITE);
        historyPanel.setBorder(BorderFactory.createTitledBorder("Historial de Mantenimiento"));
        
        String[] historyColumns = {"Fecha", "Tipo", "Descripción", "Kilometraje", "Estado"};
        DefaultTableModel historyModel = new DefaultTableModel(historyColumns, 0);
        JTable historyTable = new JTable(historyModel);
        UITheme.applyTableTheme(historyTable);
        
        // Add some dummy data
        historyModel.addRow(new Object[]{"2023-05-15", "Preventivo", "Cambio de aceite", "15000", "Completado"});
        historyModel.addRow(new Object[]{"2023-03-10", "Correctivo", "Reparación de frenos", "12500", "Completado"});
        
        historyPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        
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
        detailsPanel.add(infoPanel, BorderLayout.NORTH);
        detailsPanel.add(historyPanel, BorderLayout.CENTER);
        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show details panel with animation
        detailsPanel.setVisible(true);
        AnimationUtil.fadeIn(detailsPanel, 300);
    }
    
    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
        return label;
    }
    
    // Method to update the table with vehicle data
    public void updateVehicleTable(List<Vehicle> vehicles) {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Add vehicles to the table
        for (Vehicle vehicle : vehicles) {
            Object[] row = {
                    vehicle.getVehicleId(),
                    vehicle.getLicensePlate(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getType(),
                    "Customer Name" // This would come from the customer object in a real implementation
            };
            tableModel.addRow(row);
        }
    }
}