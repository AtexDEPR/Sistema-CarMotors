/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.parts.views;

import com.carmotorsproject.parts.controller.SupplierEvaluationController;
import com.carmotorsproject.parts.model.Supplier;
import com.carmotorsproject.parts.model.SupplierEvaluation;
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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Swing UI for evaluating suppliers.
 */
public class SupplierEvaluationView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(SupplierEvaluationView.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    // Controller
    private SupplierEvaluationController controller;

    // UI Components
    private JPanel contentPane;
    private JTextField txtEvaluationId;
    private JComboBox<Supplier> cmbSupplier;
    private JTextField txtEvaluationDate;
    private JSpinner spnDeliveryRating;
    private JSpinner spnQualityRating;
    private JSpinner spnPriceRating;
    private JSpinner spnCommunicationRating;
    private JTextArea txtComments;
    private JTable tblEvaluations;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JLabel lblAverageRating;

    /**
     * Create the frame.
     */
    public SupplierEvaluationView() {
        initComponents();
        controller = new SupplierEvaluationController(this);
        loadInitialData();
    }

    /**
     * Initialize UI components.
     */
    private void initComponents() {
        setTitle("Evaluación de Proveedores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 600);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos de Evaluación"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtEvaluationId = new JTextField();
        txtEvaluationId.setEditable(false);
        formPanel.add(txtEvaluationId, gbc);
        txtEvaluationId.setColumns(10);

        // Supplier
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Proveedor:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        cmbSupplier = new JComboBox<>();
        cmbSupplier.addActionListener(e -> {
            Supplier supplier = (Supplier) cmbSupplier.getSelectedItem();
            if (supplier != null) {
                updateAverageRating(supplier.getId());
            }
        });
        formPanel.add(cmbSupplier, gbc);
        gbc.gridwidth = 1;

        // Evaluation Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Fecha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        txtEvaluationDate = new JTextField();
        txtEvaluationDate.setText(DATE_FORMAT.format(new Date()));
        formPanel.add(txtEvaluationDate, gbc);
        txtEvaluationDate.setColumns(10);

        // Average Rating
        gbc.gridx = 2;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Calificación Promedio:"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        lblAverageRating = new JLabel("0.0");
        lblAverageRating.setFont(new Font("Dialog", Font.BOLD, 14));
        formPanel.add(lblAverageRating, gbc);

        // Delivery Rating
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Entrega (1-5):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        spnDeliveryRating = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        formPanel.add(spnDeliveryRating, gbc);

        // Quality Rating
        gbc.gridx = 2;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Calidad (1-5):"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        spnQualityRating = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        formPanel.add(spnQualityRating, gbc);

        // Price Rating
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Precio (1-5):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        spnPriceRating = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        formPanel.add(spnPriceRating, gbc);

        // Communication Rating
        gbc.gridx = 2;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Comunicación (1-5):"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 4;
        spnCommunicationRating = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        formPanel.add(spnCommunicationRating, gbc);

        // Comments
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Comentarios:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        txtComments = new JTextArea();
        txtComments.setRows(3);
        JScrollPane commentsScrollPane = new JScrollPane(txtComments);
        formPanel.add(commentsScrollPane, gbc);
        gbc.gridwidth = 1;

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        btnAdd = new JButton("Agregar");
        btnAdd.addActionListener(this::btnAddActionPerformed);
        buttonsPanel.add(btnAdd);

        btnUpdate = new JButton("Actualizar");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        buttonsPanel.add(btnUpdate);

        btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        buttonsPanel.add(btnDelete);

        btnClear = new JButton("Limpiar");
        btnClear.addActionListener(this::btnClearActionPerformed);
        buttonsPanel.add(btnClear);

        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Evaluaciones"));

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnFilterBySupplier = new JButton("Filtrar por Proveedor");
        btnFilterBySupplier.addActionListener(e -> {
            Supplier supplier = (Supplier) cmbSupplier.getSelectedItem();
            if (supplier != null) {
                controller.loadEvaluationsBySupplier(supplier.getId());
            } else {
                showError("Debe seleccionar un proveedor para filtrar.");
            }
        });
        filterPanel.add(btnFilterBySupplier);

        JButton btnShowAll = new JButton("Mostrar Todos");
        btnShowAll.addActionListener(e -> controller.loadAllEvaluations());
        filterPanel.add(btnShowAll);

        // Table Model
        String[] columnNames = {"ID", "Proveedor", "Fecha", "Entrega", "Calidad", "Precio", "Comunicación", "Promedio", "Comentarios"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblEvaluations = new JTable(tableModel);
        tblEvaluations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblEvaluations.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblEvaluations.getSelectedRow();
                    if (row != -1) {
                        selectEvaluation(row);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblEvaluations);

        tablePanel.add(filterPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add components to content pane
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonsPanel, BorderLayout.SOUTH);

        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(tablePanel, BorderLayout.CENTER);

        // Set initial state
        clearForm();
    }

    /**
     * Load initial data.
     */
    private void loadInitialData() {
        List<Supplier> suppliers = controller.loadSuppliers();
        updateSupplierCombo(suppliers);
        controller.loadAllEvaluations();
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
     * Update the average rating label.
     *
     * @param supplierId The supplier ID
     */
    private void updateAverageRating(int supplierId) {
        double rating = controller.getAverageRating(supplierId);
        lblAverageRating.setText(String.format("%.1f", rating));

        LOGGER.log(Level.INFO, "Updated average rating for supplier ID {0}: {1}",
                new Object[]{supplierId, rating});
    }

    /**
     * Updates the evaluations table with the provided list.
     * @param evaluations List of supplier evaluations.
     */
    public void updateTable(List<SupplierEvaluation> evaluations) {
        // Asume que tienes un DefaultTableModel llamado tableModel
        if (tableModel == null) {
            String[] columnNames = {"ID Eval", "ID Prov", "Nombre Prov", "Fecha", "Entrega", "Calidad", "Precio", "Com.", "Total", "Comentarios"}; // Añadida columna "Nombre Prov"
            tableModel = new DefaultTableModel(columnNames, 0);
            tblEvaluations.setModel(tableModel);
        }

        tableModel.setRowCount(0); // Limpia la tabla

        if (evaluations != null) {
            for (SupplierEvaluation evaluation : evaluations) {
                // --- MODIFICACIÓN AQUÍ ---
                int supplierId = evaluation.getSupplierId();
                String supplierName = getSupplierNameById(supplierId); // Llama a la función auxiliar

                Object[] row = {
                        evaluation.getEvaluationId(),
                        supplierId, // Muestra el ID del proveedor
                        supplierName, // Muestra el NOMBRE del proveedor encontrado
                        evaluation.getEvaluationDate(), // Formatea la fecha si es necesario
                        evaluation.getDeliveryRating(),
                        evaluation.getQualityRating(),
                        evaluation.getPriceRating(),
                        evaluation.getCommunicationRating(),
                        String.format("%.1f", evaluation.getTotalRating()), // Formatea el total
                        evaluation.getComments()
                };
                tableModel.addRow(row);
            }
        }
    }

    /**
     * Select an evaluation from the table and populate the form.
     *
     * @param row The selected row index
     */
    private void selectEvaluation(int row) {
        txtEvaluationId.setText(tableModel.getValueAt(row, 0).toString());

        // Set supplier
        String supplierName = tableModel.getValueAt(row, 1).toString();
        for (int i = 0; i < cmbSupplier.getItemCount(); i++) {
            Supplier supplier = cmbSupplier.getItemAt(i);
            if (supplier.getName().equals(supplierName)) {
                cmbSupplier.setSelectedIndex(i);
                break;
            }
        }

        try {
            if (tableModel.getValueAt(row, 2) != null) {
                Date evaluationDate = DATE_FORMAT.parse(tableModel.getValueAt(row, 2).toString());
                txtEvaluationDate.setText(DATE_FORMAT.format(evaluationDate));
            }
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Error parsing evaluation date", e);
            txtEvaluationDate.setText(DATE_FORMAT.format(new Date()));
        }

        spnDeliveryRating.setValue(tableModel.getValueAt(row, 3));
        spnQualityRating.setValue(tableModel.getValueAt(row, 4));
        spnPriceRating.setValue(tableModel.getValueAt(row, 5));
        spnCommunicationRating.setValue(tableModel.getValueAt(row, 6));
        txtComments.setText(tableModel.getValueAt(row, 8) != null ? tableModel.getValueAt(row, 8).toString() : "");

        LOGGER.log(Level.INFO, "Selected evaluation with ID: {0}", txtEvaluationId.getText());
    }

    /**
     * Clear the form fields.
     */
    public void clearForm() {
        txtEvaluationId.setText("");
        if (cmbSupplier.getItemCount() > 0) {
            cmbSupplier.setSelectedIndex(0);
        }
        txtEvaluationDate.setText(DATE_FORMAT.format(new Date()));
        spnDeliveryRating.setValue(3);
        spnQualityRating.setValue(3);
        spnPriceRating.setValue(3);
        spnCommunicationRating.setValue(3);
        txtComments.setText("");

        LOGGER.log(Level.INFO, "Form cleared");
    }

    /**
     * Validate form inputs.
     *
     * @return True if all required fields are valid
     */
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (cmbSupplier.getSelectedItem() == null) {
            errors.append("- Debe seleccionar un proveedor.\n");
        }

        try {
            if (!txtEvaluationDate.getText().trim().isEmpty()) {
                DATE_FORMAT.parse(txtEvaluationDate.getText().trim());
            }
        } catch (ParseException e) {
            errors.append("- La fecha de evaluación no es válida. Use el formato dd/MM/yyyy.\n");
        }

        // Validate ratings (should be between 1 and 5)
        try {
            int deliveryRating = (Integer) spnDeliveryRating.getValue();
            controller.validateRating(deliveryRating, "Calificación de entrega");
        } catch (IllegalArgumentException e) {
            errors.append("- ").append(e.getMessage()).append("\n");
        }

        try {
            int qualityRating = (Integer) spnQualityRating.getValue();
            controller.validateRating(qualityRating, "Calificación de calidad");
        } catch (IllegalArgumentException e) {
            errors.append("- ").append(e.getMessage()).append("\n");
        }

        try {
            int priceRating = (Integer) spnPriceRating.getValue();
            controller.validateRating(priceRating, "Calificación de precio");
        } catch (IllegalArgumentException e) {
            errors.append("- ").append(e.getMessage()).append("\n");
        }

        try {
            int communicationRating = (Integer) spnCommunicationRating.getValue();
            controller.validateRating(communicationRating, "Calificación de comunicación");
        } catch (IllegalArgumentException e) {
            errors.append("- ").append(e.getMessage()).append("\n");
        }

        if (errors.length() > 0) {
            showError("Por favor corrija los siguientes errores:\n" + errors.toString());
            return false;
        }

        return true;
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
     * Handle Add button action.
     *
     * @param evt The action event
     */
    private void btnAddActionPerformed(ActionEvent evt) {
        if (!validateForm()) {
            return;
        }

        Supplier supplier = (Supplier) cmbSupplier.getSelectedItem();
        Date evaluationDate;
        try {
            evaluationDate = DATE_FORMAT.parse(txtEvaluationDate.getText().trim());
        } catch (ParseException e) {
            evaluationDate = new Date();
        }

        int deliveryRating = (Integer) spnDeliveryRating.getValue();
        int qualityRating = (Integer) spnQualityRating.getValue();
        int priceRating = (Integer) spnPriceRating.getValue();
        int communicationRating = (Integer) spnCommunicationRating.getValue();
        String comments = txtComments.getText().trim();

        controller.addEvaluation(supplier.getId(), evaluationDate, deliveryRating,
                qualityRating, priceRating, communicationRating, comments);
    }

    /**
     * Handle Update button action.
     *
     * @param evt The action event
     */
    private void btnUpdateActionPerformed(ActionEvent evt) {
        if (txtEvaluationId.getText().trim().isEmpty()) {
            showError("Debe seleccionar una evaluación para actualizar.");
            return;
        }

        if (!validateForm()) {
            return;
        }

        int evaluationId = Integer.parseInt(txtEvaluationId.getText().trim());
        Supplier supplier = (Supplier) cmbSupplier.getSelectedItem();
        Date evaluationDate;
        try {
            evaluationDate = DATE_FORMAT.parse(txtEvaluationDate.getText().trim());
        } catch (ParseException e) {
            evaluationDate = new Date();
        }

        int deliveryRating = (Integer) spnDeliveryRating.getValue();
        int qualityRating = (Integer) spnQualityRating.getValue();
        int priceRating = (Integer) spnPriceRating.getValue();
        int communicationRating = (Integer) spnCommunicationRating.getValue();
        String comments = txtComments.getText().trim();

        controller.updateEvaluation(evaluationId, supplier.getId(), evaluationDate, deliveryRating,
                qualityRating, priceRating, communicationRating, comments);
    }

    /**
     * Handle Delete button action.
     *
     * @param evt The action event
     */
    private void btnDeleteActionPerformed(ActionEvent evt) {
        if (txtEvaluationId.getText().trim().isEmpty()) {
            showError("Debe seleccionar una evaluación para eliminar.");
            return;
        }

        int evaluationId = Integer.parseInt(txtEvaluationId.getText().trim());
        controller.deleteEvaluation(evaluationId);
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
     * Helper method to find the supplier name using its ID.
     * Searches through the items in the supplier ComboBox.
     *
     * @param supplierId The ID of the supplier to find.
     * @return The name of the supplier, or a default string if not found.
     */
    private String getSupplierNameById(int supplierId) {
        if (cmbSupplier != null) { // Asegúrate que el ComboBox esté inicializado
            for (int i = 0; i < cmbSupplier.getItemCount(); i++) {
                // Asume que cmbSupplier contiene objetos Supplier
                Supplier supplier = cmbSupplier.getItemAt(i);
                if (supplier != null && supplier.getId() == supplierId) {
                    // Asume que la clase Supplier tiene un método getName() o getFullName()
                    return supplier.getName();
                }
            }
        }
        return "ID: " + supplierId; // Retorna el ID si no se encuentra el nombre
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
                SupplierEvaluationView frame = new SupplierEvaluationView();
                frame.setVisible(true);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error launching SupplierEvaluationView", e);
            }
        });
    }
}