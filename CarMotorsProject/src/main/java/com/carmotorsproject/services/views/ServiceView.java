package com.carmotorsproject.services.views;

import com.carmotorsproject.services.model.Service;
import com.carmotorsproject.services.model.MaintenanceType;
import com.carmotorsproject.services.model.ServiceStatus;
import com.carmotorsproject.utils.UITheme;
import com.carmotorsproject.utils.AnimationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Service management view
 */
public class ServiceView extends JPanel {
    
    private JTable serviceTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewDetailsButton;
    private JPanel formPanel;
    private JPanel detailsPanel;
    
    // Form fields
    private JComboBox<String> maintenanceTypeCombo;
    private JComboBox<String> vehicleCombo;
    private JTextField mileageField;
    private JTextArea descriptionArea;
    private JTextArea diagnosisArea;
    private JTextField estimatedTimeField;
    private JTextField laborCostField;
    private JComboBox<String> statusCombo;
    
    // Controller reference will be added here
    
    public ServiceView() {
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
        
        searchPanel.add(new JLabel("Buscar servicio:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Table panel
        String[] columnNames = {"ID", "Tipo", "Vehículo", "Cliente", "Descripción", "Estado", "Fecha Inicio"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        serviceTable = new JTable(tableModel);
        UITheme.applyTableTheme(serviceTable);
        
        JScrollPane scrollPane = new JScrollPane(serviceTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        addButton = new JButton("Nuevo Servicio");
        editButton = new JButton("Editar Servicio");
        deleteButton = new JButton("Eliminar Servicio");
        viewDetailsButton = new JButton("Ver Detalles");
        
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
        JPanel fieldsPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        fieldsPanel.setBackground(UITheme.WHITE);
        
        maintenanceTypeCombo = new JComboBox<>(new String[]{"Seleccione tipo", "Preventivo", "Correctivo"});
        vehicleCombo = new JComboBox<>(new String[]{"Seleccione vehículo", "ABC123 - Toyota Corolla", "XYZ789 - Honda Civic"});
        mileageField = new JTextField(10);
        descriptionArea = new JTextArea(3, 20);
        diagnosisArea = new JTextArea(3, 20);
        estimatedTimeField = new JTextField(10);
        laborCostField = new JTextField(10);
        statusCombo = new JComboBox<>(new String[]{"Pendiente", "En progreso", "Completado", "Entregado"});
        
        UITheme.applyComboBoxTheme(maintenanceTypeCombo);
        UITheme.applyComboBoxTheme(vehicleCombo);
        UITheme.applyTextFieldTheme(mileageField);
        UITheme.applyTextFieldTheme(estimatedTimeField);
        UITheme.applyTextFieldTheme(laborCostField);
        UITheme.applyComboBoxTheme(statusCombo);
        
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        diagnosisArea.setLineWrap(true);
        diagnosisArea.setWrapStyleWord(true);
        diagnosisArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        JScrollPane diagnosisScroll = new JScrollPane(diagnosisArea);
        
        fieldsPanel.add(new JLabel("Tipo de Mantenimiento:"));
        fieldsPanel.add(maintenanceTypeCombo);
        fieldsPanel.add(new JLabel("Vehículo:"));
        fieldsPanel.add(vehicleCombo);
        fieldsPanel.add(new JLabel("Kilometraje:"));
        fieldsPanel.add(mileageField);
        fieldsPanel.add(new JLabel("Descripción:"));
        fieldsPanel.add(descriptionScroll);
        fieldsPanel.add(new JLabel("Diagnóstico Inicial:"));
        fieldsPanel.add(diagnosisScroll);
        fieldsPanel.add(new JLabel("Tiempo Estimado (horas):"));
        fieldsPanel.add(estimatedTimeField);
        fieldsPanel.add(new JLabel("Costo de Mano de Obra:"));
        fieldsPanel.add(laborCostField);
        fieldsPanel.add(new JLabel("Estado:"));
        fieldsPanel.add(statusCombo);
        
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
                saveService();
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
        JScrollPane scrollPane = new JScrollPane(serviceTable);
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
                int selectedRow = serviceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    showEditForm(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(ServiceView.this,
                            "Por favor, seleccione un servicio para editar.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // Delete button action
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = serviceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int option = JOptionPane.showConfirmDialog(ServiceView.this,
                            "¿Está seguro de que desea eliminar este servicio?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION);
                    
                    if (option == JOptionPane.YES_OPTION) {
                        deleteService(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(ServiceView.this,
                            "Por favor, seleccione un servicio para eliminar.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // View details button action
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = serviceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    showServiceDetails(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(ServiceView.this,
                            "Por favor, seleccione un servicio para ver detalles.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    
    // Show the add service form
    private void showAddForm() {
        // Clear form fields
        maintenanceTypeCombo.setSelectedIndex(0);
        vehicleCombo.setSelectedIndex(0);
        mileageField.setText("");
        descriptionArea.setText("");
        diagnosisArea.setText("");
        estimatedTimeField.setText("");
        laborCostField.setText("");
        statusCombo.setSelectedIndex(0);
        
        // Hide details panel if visible
        if (detailsPanel.isVisible()) {
            AnimationUtil.fadeOut(detailsPanel, 300);
            detailsPanel.setVisible(false);
        }
        
        // Show form panel with animation
        formPanel.setVisible(true);
        AnimationUtil.fadeIn(formPanel, 300);
    }
    
    // Show the edit service form
    private void showEditForm(int row) {
        // TODO: Populate form fields with selected service data
        // This is just a placeholder implementation
        
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
    
    // Save service (add or update)
    private void saveService() {
        // Validate form fields
        if (maintenanceTypeCombo.getSelectedIndex() == 0 || 
            vehicleCombo.getSelectedIndex() == 0 || 
            descriptionArea.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this,
                    "Por favor, complete los campos obligatorios.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create service object
        Service service = new Service();
        service.setMaintenanceType(maintenanceTypeCombo.getSelectedIndex() == 1 ? 
                MaintenanceType.Preventive : MaintenanceType.Corrective);
        
        // In a real app, you would set the vehicle ID
        // service.setVehicleId(vehicleId);
        
        try {
            if (!mileageField.getText().trim().isEmpty()) {
                service.setMileage(Integer.parseInt(mileageField.getText().trim()));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El kilometraje debe ser un número entero.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        service.setDescription(descriptionArea.getText().trim());
        service.setInitialDiagnosis(diagnosisArea.getText().trim());
        
        try {
            if (!estimatedTimeField.getText().trim().isEmpty()) {
                service.setEstimatedTime(Double.parseDouble(estimatedTimeField.getText().trim()));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El tiempo estimado debe ser un número decimal.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if (!laborCostField.getText().trim().isEmpty()) {
                service.setLaborCost(Double.parseDouble(laborCostField.getText().trim()));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El costo de mano de obra debe ser un número decimal.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Set status based on selection
        switch (statusCombo.getSelectedIndex()) {
            case 0:
                service.setStatus(ServiceStatus.Pending);
                break;
            case 1:
                service.setStatus(ServiceStatus.In_progress);
                break;
            case 2:
                service.setStatus(ServiceStatus.Completed);
                break;
            case 3:
                service.setStatus(ServiceStatus.Delivered);
                break;
        }
        
        // Set start date to current date
        service.setStartDate(new Date());
        
        // TODO: Save service using controller
        
        // For now, just add to table model
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        Object[] row = {
                "1", // Placeholder ID
                service.getMaintenanceType().toString(),
                vehicleCombo.getSelectedItem().toString(),
                "Cliente", // This would come from the vehicle's customer in a real implementation
                service.getDescription(),
                service.getStatus().toString(),
                dateFormat.format(service.getStartDate())
        };
        
        tableModel.addRow(row);
        
        // Hide form
        hideForm();
        
        // Show success message
        JOptionPane.showMessageDialog(this,
                "Servicio guardado exitosamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Delete service
    private void deleteService(int row) {
        // TODO: Delete service using controller
        
        // For now, just remove from table model
        tableModel.removeRow(row);
        
        // Show success message
        JOptionPane.showMessageDialog(this,
                "Servicio eliminado exitosamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Show service details
    private void showServiceDetails(int row) {
        // Hide form panel if visible
        if (formPanel.isVisible()) {
            AnimationUtil.fadeOut(formPanel, 300);
            formPanel.setVisible(false);
        }
        
        // Create details content
        detailsPanel.removeAll();
        
        // Service info
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBackground(UITheme.WHITE);
        
        String id = (String) tableModel.getValueAt(row, 0);
        String type = (String) tableModel.getValueAt(row, 1);
        String vehicle = (String) tableModel.getValueAt(row, 2);
        String customer = (String) tableModel.getValueAt(row, 3);
        String description = (String) tableModel.getValueAt(row, 4);
        String status = (String) tableModel.getValueAt(row, 5);
        String startDate = (String) tableModel.getValueAt(row, 6);
        
        infoPanel.add(createBoldLabel("ID:"));
        infoPanel.add(new JLabel(id));
        infoPanel.add(createBoldLabel("Tipo:"));
        infoPanel.add(new JLabel(type));
        infoPanel.add(createBoldLabel("Vehículo:"));
        infoPanel.add(new JLabel(vehicle));
        infoPanel.add(createBoldLabel("Cliente:"));
        infoPanel.add(new JLabel(customer));
        infoPanel.add(createBoldLabel("Descripción:"));
        infoPanel.add(new JLabel(description));
        infoPanel.add(createBoldLabel("Estado:"));
        infoPanel.add(new JLabel(status));
        infoPanel.add(createBoldLabel("Fecha Inicio:"));
        infoPanel.add(new JLabel(startDate));
        
        // Parts used in service
        JPanel partsPanel = new JPanel(new BorderLayout());
        partsPanel.setBackground(UITheme.WHITE);
        partsPanel.setBorder(BorderFactory.createTitledBorder("Repuestos Utilizados"));
        
        String[] partsColumns = {"Repuesto", "Cantidad", "Precio Unitario", "Subtotal"};
        DefaultTableModel partsModel = new DefaultTableModel(partsColumns, 0);
        JTable partsTable = new JTable(partsModel);
        UITheme.applyTableTheme(partsTable);
        
        // Add some dummy data
        partsModel.addRow(new Object[]{"Filtro de aceite", "1", "$15.00", "$15.00"});
        partsModel.addRow(new Object[]{"Aceite de motor", "5", "$8.00", "$40.00"});
        
        partsPanel.add(new JScrollPane(partsTable), BorderLayout.CENTER);
        
        // Technicians assigned
        JPanel techniciansPanel = new JPanel(new BorderLayout());
        techniciansPanel.setBackground(UITheme.WHITE);
        techniciansPanel.setBorder(BorderFactory.createTitledBorder("Técnicos Asignados"));
        
        String[] techColumns = {"Técnico", "Especialidad", "Horas Trabajadas"};
        DefaultTableModel techModel = new DefaultTableModel(techColumns, 0);
        JTable techTable = new JTable(techModel);
        UITheme.applyTableTheme(techTable);
        
        // Add some dummy data
        techModel.addRow(new Object[]{"Juan Pérez", "Mecánico", "2.5"});
        
        techniciansPanel.add(new JScrollPane(techTable), BorderLayout.CENTER);
        
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
        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        tablesPanel.setBackground(UITheme.WHITE);
        tablesPanel.add(partsPanel);
        tablesPanel.add(techniciansPanel);
        
        detailsPanel.add(infoPanel, BorderLayout.NORTH);
        detailsPanel.add(tablesPanel, BorderLayout.CENTER);
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
    
    // Method to update the table with service data
    public void updateServiceTable(List<Service> services) {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Add services to the table
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Service service : services) {
            Object[] row = {
                    service.getServiceId(),
                    service.getMaintenanceType().toString(),
                    "Vehicle Info", // This would come from the vehicle object in a real implementation
                    "Customer Info", // This would come from the customer object in a real implementation
                    service.getDescription(),
                    service.getStatus().toString(),
                    service.getStartDate() != null ? dateFormat.format(service.getStartDate()) : ""
            };
            tableModel.addRow(row);
        }
    }
}