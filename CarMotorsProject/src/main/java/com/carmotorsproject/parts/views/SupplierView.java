package com.carmotorsproject.parts.views;

import com.carmotorsproject.parts.controller.SupplierController;
import com.carmotorsproject.parts.model.Supplier;
import com.carmotorsproject.ui.animation.AnimationManager;
import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.ModernTextField;
import com.carmotorsproject.ui.components.TransparentPanel;
import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.utils.SwingUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Vista moderna para la gestión de proveedores.
 */
public class SupplierView extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(SupplierView.class.getName());

    // Controller
    private SupplierController controller;

    // UI Components
    private JTextField txtId;
    private ModernTextField txtName;
    private ModernTextField txtContactName;
    private ModernTextField txtPhone;
    private ModernTextField txtEmail;
    private ModernTextField txtAddress;
    private ModernTextField txtTaxId;
    private JComboBox<String> cmbStatus;
    private ModernTextField txtSearch;
    private JTable tblSuppliers;
    private DefaultTableModel tableModel;
    private ModernButton btnAdd;
    private ModernButton btnUpdate;
    private ModernButton btnDelete;
    private ModernButton btnClear;
    private ModernButton btnSearch;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    /**
     * Constructor de la vista.
     */
    public SupplierView() {
        initComponents();

        try {
            controller = new SupplierController(this);
            loadSuppliers();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar el controlador: " + e.getMessage(), e);

            // Mostrar mensaje de error pero permitir que la vista se muestre
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        "No se pudieron cargar los datos de proveedores.\n" +
                                "Algunas funcionalidades pueden estar limitadas.\n\n" +
                                "Error: " + e.getMessage(),
                        "Error de Datos",
                        JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    /**
     * Inicializa los componentes de la UI.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.PRIMARY_WHITE);

        // Panel principal con CardLayout para animaciones
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        // Panel de contenido principal
        JPanel contentPanel = new TransparentPanel(AppTheme.PRIMARY_WHITE, 1.0f);
        contentPanel.setLayout(new BorderLayout(0, 20));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel de título
        JPanel titlePanel = new TransparentPanel(AppTheme.PRIMARY_BLACK, 0.9f);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        titlePanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("Gestión de Proveedores");
        titleLabel.setFont(AppTheme.TITLE_FONT);
        titleLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Panel de formulario
        JPanel formPanel = createFormPanel();

        // Panel de búsqueda
        JPanel searchPanel = createSearchPanel();

        // Panel de tabla
        JPanel tablePanel = createTablePanel();

        // Añadir componentes al panel de contenido
        contentPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel central que contiene formulario y búsqueda
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(searchPanel, BorderLayout.CENTER);

        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(tablePanel, BorderLayout.SOUTH);

        // Añadir al panel con CardLayout
        cardPanel.add(contentPanel, "main");
        add(cardPanel, BorderLayout.CENTER);

        // Aplicar animación de entrada
        SwingUtilities.invokeLater(() -> AnimationManager.fadeIn(this, 300));
    }

    /**
     * Crea el panel de formulario.
     *
     * @return Panel de formulario
     */
    private JPanel createFormPanel() {
        JPanel formWrapper = new TransparentPanel(AppTheme.PRIMARY_WHITE, 0.95f, AppTheme.BORDER_RADIUS);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.PRIMARY_RED, 1),
                new EmptyBorder(15, 15, 15, 15)));
        formWrapper.setLayout(new BorderLayout(0, 10));

        // Título del formulario
        JLabel formTitle = new JLabel("Datos del Proveedor");
        formTitle.setFont(AppTheme.SUBTITLE_FONT);
        formTitle.setForeground(AppTheme.PRIMARY_BLACK);
        formWrapper.add(formTitle, BorderLayout.NORTH);

        // Panel de campos del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblId = new JLabel("ID:");
        lblId.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblId, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setFont(AppTheme.REGULAR_FONT);
        txtId.setBackground(AppTheme.SECONDARY_WHITE);
        txtId.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        formPanel.add(txtId, gbc);
        txtId.setColumns(5);

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblName = new JLabel("Nombre:");
        lblName.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblName, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtName = new ModernTextField();
        txtName.setPlaceholder("Nombre del proveedor");
        formPanel.add(txtName, gbc);
        txtName.setColumns(20);

        // Contacto
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblContact = new JLabel("Contacto:");
        lblContact.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblContact, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        txtContactName = new ModernTextField();
        txtContactName.setPlaceholder("Nombre de la persona de contacto");
        formPanel.add(txtContactName, gbc);
        txtContactName.setColumns(20);

        // Teléfono
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblPhone = new JLabel("Teléfono:");
        lblPhone.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblPhone, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        txtPhone = new ModernTextField();
        txtPhone.setPlaceholder("Número de teléfono");
        formPanel.add(txtPhone, gbc);
        txtPhone.setColumns(15);

        // Email
        gbc.gridx = 2;
        gbc.gridy = 1;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblEmail, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        txtEmail = new ModernTextField();
        txtEmail.setPlaceholder("Correo electrónico");
        formPanel.add(txtEmail, gbc);
        txtEmail.setColumns(20);

        // Dirección
        gbc.gridx = 2;
        gbc.gridy = 2;
        JLabel lblAddress = new JLabel("Dirección:");
        lblAddress.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblAddress, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        txtAddress = new ModernTextField();
        txtAddress.setPlaceholder("Dirección completa");
        formPanel.add(txtAddress, gbc);
        txtAddress.setColumns(30);

        // NIF/CIF
        gbc.gridx = 2;
        gbc.gridy = 3;
        JLabel lblTaxId = new JLabel("NIF/CIF:");
        lblTaxId.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblTaxId, gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        txtTaxId = new ModernTextField();
        txtTaxId.setPlaceholder("Identificación fiscal");
        formPanel.add(txtTaxId, gbc);
        txtTaxId.setColumns(15);

        // Estado
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblStatus = new JLabel("Estado:");
        lblStatus.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblStatus, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        cmbStatus = new JComboBox<>(new String[]{"ACTIVO", "INACTIVO"});
        cmbStatus.setFont(AppTheme.REGULAR_FONT);
        cmbStatus.setBackground(AppTheme.PRIMARY_WHITE);
        cmbStatus.setBorder(BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY));
        formPanel.add(cmbStatus, gbc);

        formWrapper.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setOpaque(false);

        btnAdd = new ModernButton("Agregar");
        btnAdd.setColors(AppTheme.PRIMARY_RED, AppTheme.SECONDARY_RED,
                AppTheme.SECONDARY_RED.darker(), AppTheme.PRIMARY_WHITE);
        btnAdd.addActionListener(this::btnAddActionPerformed);
        buttonsPanel.add(btnAdd);

        btnUpdate = new ModernButton("Actualizar");
        btnUpdate.setColors(AppTheme.PRIMARY_BLACK, AppTheme.SECONDARY_BLACK,
                AppTheme.SECONDARY_BLACK.darker(), AppTheme.PRIMARY_WHITE);
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        buttonsPanel.add(btnUpdate);

        btnDelete = new ModernButton("Eliminar");
        btnDelete.setColors(AppTheme.ERROR_COLOR, AppTheme.ERROR_COLOR.darker(),
                AppTheme.ERROR_COLOR.darker().darker(), AppTheme.PRIMARY_WHITE);
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        buttonsPanel.add(btnDelete);

        btnClear = new ModernButton("Limpiar");
        btnClear.setColors(AppTheme.ACCENT_GRAY, AppTheme.ACCENT_GRAY.darker(),
                AppTheme.ACCENT_GRAY.darker().darker(), AppTheme.PRIMARY_WHITE);
        btnClear.addActionListener(this::btnClearActionPerformed);
        buttonsPanel.add(btnClear);

        formWrapper.add(buttonsPanel, BorderLayout.SOUTH);

        return formWrapper;
    }

    /**
     * Crea el panel de búsqueda.
     *
     * @return Panel de búsqueda
     */
    private JPanel createSearchPanel() {
        JPanel searchWrapper = new TransparentPanel(AppTheme.PRIMARY_WHITE, 0.95f, AppTheme.BORDER_RADIUS);
        searchWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1),
                new EmptyBorder(10, 15, 10, 15)));
        searchWrapper.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setFont(AppTheme.REGULAR_FONT);
        searchWrapper.add(searchLabel);

        txtSearch = new ModernTextField();
        txtSearch.setPlaceholder("Buscar por nombre, contacto, teléfono o email");
        txtSearch.setColumns(30);
        searchWrapper.add(txtSearch);

        btnSearch = new ModernButton("Buscar");
        btnSearch.setColors(AppTheme.PRIMARY_BLACK, AppTheme.SECONDARY_BLACK,
                AppTheme.SECONDARY_BLACK.darker(), AppTheme.PRIMARY_WHITE);
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        searchWrapper.add(btnSearch);

        return searchWrapper;
    }

    /**
     * Crea el panel de tabla.
     *
     * @return Panel de tabla
     */
    private JPanel createTablePanel() {
        JPanel tableWrapper = new TransparentPanel(AppTheme.PRIMARY_WHITE, 0.95f, AppTheme.BORDER_RADIUS);
        tableWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.PRIMARY_RED, 1),
                new EmptyBorder(15, 15, 15, 15)));
        tableWrapper.setLayout(new BorderLayout(0, 10));

        // Título de la tabla
        JLabel tableTitle = new JLabel("Lista de Proveedores");
        tableTitle.setFont(AppTheme.SUBTITLE_FONT);
        tableTitle.setForeground(AppTheme.PRIMARY_BLACK);
        tableWrapper.add(tableTitle, BorderLayout.NORTH);

        // Modelo de tabla
        String[] columnNames = {"ID", "Nombre", "Contacto", "Teléfono", "Email", "Dirección", "NIF/CIF", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Tabla
        tblSuppliers = new JTable(tableModel);
        tblSuppliers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSuppliers.setRowHeight(30);
        tblSuppliers.setFont(AppTheme.REGULAR_FONT);
        tblSuppliers.setGridColor(AppTheme.ACCENT_LIGHT);
        tblSuppliers.setShowVerticalLines(true);
        tblSuppliers.setShowHorizontalLines(true);

        // Personalizar encabezado de tabla
        JTableHeader header = tblSuppliers.getTableHeader();
        header.setBackground(AppTheme.PRIMARY_BLACK);
        header.setForeground(AppTheme.PRIMARY_WHITE);
        header.setFont(AppTheme.HEADER_FONT);
        header.setBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY_RED, 1));
        header.setPreferredSize(new Dimension(0, 35));

        // Personalizar renderizador de celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Aplicar renderizador a columnas específicas
        tblSuppliers.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        tblSuppliers.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Teléfono
        tblSuppliers.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // Estado

        // Personalizar renderizador para la columna de estado
        tblSuppliers.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    String status = value.toString();
                    if ("ACTIVO".equals(status)) {
                        c.setForeground(AppTheme.SUCCESS_COLOR);
                    } else if ("INACTIVO".equals(status)) {
                        c.setForeground(AppTheme.ERROR_COLOR);
                    }
                }

                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        // Ajustar anchos de columna
        tblSuppliers.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tblSuppliers.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
        tblSuppliers.getColumnModel().getColumn(2).setPreferredWidth(150); // Contacto
        tblSuppliers.getColumnModel().getColumn(3).setPreferredWidth(100); // Teléfono
        tblSuppliers.getColumnModel().getColumn(4).setPreferredWidth(150); // Email
        tblSuppliers.getColumnModel().getColumn(5).setPreferredWidth(200); // Dirección
        tblSuppliers.getColumnModel().getColumn(6).setPreferredWidth(100); // NIF/CIF
        tblSuppliers.getColumnModel().getColumn(7).setPreferredWidth(80);  // Estado

        // Listener para doble clic
        tblSuppliers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblSuppliers.getSelectedRow();
                    if (row != -1) {
                        selectSupplier(row);
                    }
                }
            }
        });

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tblSuppliers);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.ACCENT_LIGHT));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        return tableWrapper;
    }

    /**
     * Carga todos los proveedores en la tabla.
     */
    public void loadSuppliers() {
        try {
            controller.loadAllSuppliers();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar proveedores: " + e.getMessage(), e);

            // Limpiar la tabla y mostrar mensaje
            tableModel.setRowCount(0);

            // Añadir una fila con mensaje de error
            tableModel.addRow(new Object[]{"", "Error al cargar datos", "", "", "", "", "", ""});
        }
    }

    /**
     * Actualiza la tabla de proveedores con la lista proporcionada.
     *
     * @param suppliers Lista de proveedores a mostrar
     */
    public void updateTable(List<Supplier> suppliers) {
        tableModel.setRowCount(0);

        for (Supplier supplier : suppliers) {
            Object[] row = {
                    supplier.getId(),
                    supplier.getName(),
                    supplier.getContactName(),
                    supplier.getPhone(),
                    supplier.getEmail(),
                    supplier.getAddress(),
                    supplier.getTaxId(),
                    supplier.getStatus()
            };
            tableModel.addRow(row);
        }

        LOGGER.log(Level.INFO, "Tabla de proveedores actualizada con {0} registros", suppliers.size());
    }

    /**
     * Selecciona un proveedor de la tabla y rellena el formulario.
     *
     * @param row El índice de fila seleccionado
     */
    private void selectSupplier(int row) {
        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        txtContactName.setText(tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
        txtPhone.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
        txtEmail.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
        txtAddress.setText(tableModel.getValueAt(row, 5) != null ? tableModel.getValueAt(row, 5).toString() : "");
        txtTaxId.setText(tableModel.getValueAt(row, 6) != null ? tableModel.getValueAt(row, 6).toString() : "");
        cmbStatus.setSelectedItem(tableModel.getValueAt(row, 7).toString());

        LOGGER.log(Level.INFO, "Proveedor seleccionado con ID: {0}", txtId.getText());

        // Resaltar visualmente la selección
        txtName.requestFocus();
    }

    /**
     * Limpia los campos del formulario.
     */
    public void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtContactName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtTaxId.setText("");
        cmbStatus.setSelectedIndex(0);
        txtSearch.setText("");

        LOGGER.log(Level.INFO, "Formulario limpiado");
    }

    /**
     * Valida las entradas del formulario.
     *
     * @return True si todos los campos requeridos son válidos
     */
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (txtName.getText().trim().isEmpty()) {
            errors.append("- El nombre del proveedor es obligatorio.\n");
            txtName.setBackground(new Color(255, 220, 220));
        } else {
            txtName.setBackground(AppTheme.PRIMARY_WHITE);
        }

        if (txtPhone.getText().trim().isEmpty()) {
            errors.append("- El teléfono es obligatorio.\n");
            txtPhone.setBackground(new Color(255, 220, 220));
        } else {
            txtPhone.setBackground(AppTheme.PRIMARY_WHITE);
        }

        if (!txtEmail.getText().trim().isEmpty() && !isValidEmail(txtEmail.getText().trim())) {
            errors.append("- El formato del email no es válido.\n");
            txtEmail.setBackground(new Color(255, 220, 220));
        } else {
            txtEmail.setBackground(AppTheme.PRIMARY_WHITE);
        }

        if (errors.length() > 0) {
            showError("Por favor corrija los siguientes errores:\n" + errors.toString());
            return false;
        }

        return true;
    }

    /**
     * Comprueba si una dirección de email es válida.
     *
     * @param email La dirección de email a validar
     * @return True si el email es válido
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    /**
     * Crea un objeto Supplier a partir de los datos del formulario.
     *
     * @return Un nuevo objeto Supplier
     */
    public Supplier getSupplierFromForm() {
        Supplier supplier = new Supplier();

        if (!txtId.getText().trim().isEmpty()) {
            supplier.setId(Integer.parseInt(txtId.getText().trim()));
        }

        supplier.setName(txtName.getText().trim());
        supplier.setContactName(txtContactName.getText().trim());
        supplier.setPhone(txtPhone.getText().trim());
        supplier.setEmail(txtEmail.getText().trim());
        supplier.setAddress(txtAddress.getText().trim());
        supplier.setTaxId(txtTaxId.getText().trim());
        supplier.setStatus(cmbStatus.getSelectedItem().toString());

        return supplier;
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param message El mensaje de error
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje de información.
     *
     * @param message El mensaje de información
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un diálogo de confirmación.
     *
     * @param message El mensaje de confirmación
     * @return True si el usuario confirma, false en caso contrario
     */
    public boolean showConfirm(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Maneja la acción del botón Agregar.
     *
     * @param evt El evento de acción
     */
    private void btnAddActionPerformed(ActionEvent evt) {
        if (!validateForm()) {
            return;
        }

        // Crear objeto proveedor y pasarlo directamente al controlador
        Supplier supplier = getSupplierFromForm();
        controller.addSupplier(supplier);

        LOGGER.log(Level.INFO, "Llamada a addSupplier con objeto proveedor directamente");
    }

    /**
     * Maneja la acción del botón Actualizar.
     *
     * @param evt El evento de acción
     */
    private void btnUpdateActionPerformed(ActionEvent evt) {
        if (txtId.getText().trim().isEmpty()) {
            showError("Debe seleccionar un proveedor para actualizar.");
            return;
        }

        if (!validateForm()) {
            return;
        }

        // Crear objeto proveedor y pasarlo directamente al controlador
        Supplier supplier = getSupplierFromForm();
        controller.updateSupplier(supplier);

        LOGGER.log(Level.INFO, "Llamada a updateSupplier con objeto proveedor directamente");
    }

    /**
     * Maneja la acción del botón Eliminar.
     *
     * @param evt El evento de acción
     */
    private void btnDeleteActionPerformed(ActionEvent evt) {
        if (txtId.getText().trim().isEmpty()) {
            showError("Debe seleccionar un proveedor para eliminar.");
            return;
        }

        if (showConfirm("¿Está seguro de eliminar este proveedor?")) {
            int id = Integer.parseInt(txtId.getText().trim());
            controller.deleteSupplier(id);

            LOGGER.log(Level.INFO, "Llamada a deleteSupplier con ID directamente");
        }
    }

    /**
     * Maneja la acción del botón Limpiar.
     *
     * @param evt El evento de acción
     */
    private void btnClearActionPerformed(ActionEvent evt) {
        clearForm();
    }

    /**
     * Maneja la acción del botón Buscar.
     *
     * @param evt El evento de acción
     */
    private void btnSearchActionPerformed(ActionEvent evt) {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            loadSuppliers();
        } else {
            // Llamar a searchSuppliers con dos parámetros como se requiere
            // Añadiendo una cadena vacía como segundo parámetro
            controller.searchSuppliers(searchTerm, "");
        }
    }
}
