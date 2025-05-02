package com.carmotorsproject.customers.views;

import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.utils.UITheme;
import com.carmotorsproject.utils.AnimationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Customer management view
 */
public class CustomerView extends JPanel {

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewDetailsButton;
    private JPanel formPanel;
    private JPanel detailsPanel;

    // Form fields
    private JTextField nameField;
    private JTextField idField;
    private JTextField phoneField;
    private JTextField emailField;

    // Controller reference will be added here

    public CustomerView() {
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

        searchPanel.add(new JLabel("Buscar cliente:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Table panel
        String[] columnNames = {"ID", "Nombre", "Identificación", "Teléfono", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        customerTable = new JTable(tableModel);
        UITheme.applyTableTheme(customerTable);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = new JButton("Agregar Cliente");
        editButton = new JButton("Editar Cliente");
        deleteButton = new JButton("Eliminar Cliente");
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
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        fieldsPanel.setBackground(UITheme.WHITE);

        nameField = new JTextField(20);
        idField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);

        UITheme.applyTextFieldTheme(nameField);
        UITheme.applyTextFieldTheme(idField);
        UITheme.applyTextFieldTheme(phoneField);
        UITheme.applyTextFieldTheme(emailField);

        fieldsPanel.add(new JLabel("Nombre:"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Identificación:"));
        fieldsPanel.add(idField);
        fieldsPanel.add(new JLabel("Teléfono:"));
        fieldsPanel.add(phoneField);
        fieldsPanel.add(new JLabel("Email:"));
        fieldsPanel.add(emailField);

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
                saveCustomer();
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
        JScrollPane scrollPane = new JScrollPane(customerTable);
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
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow >= 0) {
                    showEditForm(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(CustomerView.this,
                            "Por favor, seleccione un cliente para editar.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Delete button action
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int option = JOptionPane.showConfirmDialog(CustomerView.this,
                            "¿Está seguro de que desea eliminar este cliente?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        deleteCustomer(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(CustomerView.this,
                            "Por favor, seleccione un cliente para eliminar.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // View details button action
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow >= 0) {
                    showCustomerDetails(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(CustomerView.this,
                            "Por favor, seleccione un cliente para ver detalles.",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // Show the add customer form
    private void showAddForm() {
        // Clear form fields
        nameField.setText("");
        idField.setText("");
        phoneField.setText("");
        emailField.setText("");

        // Hide details panel if visible
        if (detailsPanel.isVisible()) {
            AnimationUtil.fadeOut(detailsPanel, 300);
            detailsPanel.setVisible(false);
        }

        // Show form panel with animation
        formPanel.setVisible(true);
        AnimationUtil.fadeIn(formPanel, 300);
    }

    // Show the edit customer form
    private void showEditForm(int row) {
        // Populate form fields with selected customer data
        nameField.setText((String) tableModel.getValueAt(row, 1));
        idField.setText((String) tableModel.getValueAt(row, 2));
        phoneField.setText((String) tableModel.getValueAt(row, 3));
        emailField.setText((String) tableModel.getValueAt(row, 4));

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

    // Save customer (add or update)
    private void saveCustomer() {
        // Validate form fields
        if (nameField.getText().trim().isEmpty() || idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, complete los campos obligatorios (Nombre e Identificación).",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create customer object
        Customer customer = new Customer();
        customer.setName(nameField.getText().trim());
        customer.setIdentification(idField.getText().trim());
        customer.setPhone(phoneField.getText().trim());
        customer.setEmail(emailField.getText().trim());

        // TODO: Save customer using controller

        // For now, just add to table model
        Object[] row = {
                "1", // Placeholder ID
                customer.getName(),
                customer.getIdentification(),
                customer.getPhone(),
                customer.getEmail()
        };

        tableModel.addRow(row);

        // Hide form
        hideForm();

        // Show success message
        JOptionPane.showMessageDialog(this,
                "Cliente guardado exitosamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Delete customer
    private void deleteCustomer(int row) {
        // TODO: Delete customer using controller

        // For now, just remove from table model
        tableModel.removeRow(row);

        // Show success message
        JOptionPane.showMessageDialog(this,
                "Cliente eliminado exitosamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Show customer details
    private void showCustomerDetails(int row) {
        // Hide form panel if visible
        if (formPanel.isVisible()) {
            AnimationUtil.fadeOut(formPanel, 300);
            formPanel.setVisible(false);
        }

        // Create details content
        detailsPanel.removeAll();

        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBackground(UITheme.WHITE);

        String id = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        String identification = (String) tableModel.getValueAt(row, 2);
        String phone = (String) tableModel.getValueAt(row, 3);
        String email = (String) tableModel.getValueAt(row, 4);

        infoPanel.add(createBoldLabel("ID:"));
        infoPanel.add(new JLabel(id));
        infoPanel.add(createBoldLabel("Nombre:"));
        infoPanel.add(new JLabel(name));
        infoPanel.add(createBoldLabel("Identificación:"));
        infoPanel.add(new JLabel(identification));
        infoPanel.add(createBoldLabel("Teléfono:"));
        infoPanel.add(new JLabel(phone));
        infoPanel.add(createBoldLabel("Email:"));
        infoPanel.add(new JLabel(email));

        // Add vehicle list (placeholder)
        JPanel vehiclePanel = new JPanel(new BorderLayout());
        vehiclePanel.setBackground(UITheme.WHITE);
        vehiclePanel.setBorder(BorderFactory.createTitledBorder("Vehículos registrados"));

        String[] vehicleColumns = {"Placa", "Marca", "Modelo", "Tipo"};
        DefaultTableModel vehicleModel = new DefaultTableModel(vehicleColumns, 0);
        JTable vehicleTable = new JTable(vehicleModel);
        UITheme.applyTableTheme(vehicleTable);

        vehiclePanel.add(new JScrollPane(vehicleTable), BorderLayout.CENTER);

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
        detailsPanel.add(vehiclePanel, BorderLayout.CENTER);
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

    // Method to update the table with customer data
    public void updateCustomerTable(List<Customer> customers) {
        // Clear the table
        tableModel.setRowCount(0);

        // Add customers to the table
        for (Customer customer : customers) {
            Object[] row = {
                    customer.getCustomerId(),
                    customer.getName(),
                    customer.getIdentification(),
                    customer.getPhone(),
                    customer.getEmail()
            };
            tableModel.addRow(row);
        }
    }
}