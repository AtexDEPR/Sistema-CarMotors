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
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Vista moderna y responsiva para la gestión de proveedores.
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
    private JSplitPane mainSplitPane;
    private JLabel statusLabel;
    private JLabel totalSuppliersLabel;

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
        contentPanel.setLayout(new BorderLayout(0, 0));

        // Panel de título/header
        JPanel headerPanel = createHeaderPanel();

        // Panel principal con SplitPane para responsividad
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerLocation(350);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setDividerSize(8);
        mainSplitPane.setBorder(null);

        // Panel superior (formulario y búsqueda)
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(15, 15, 5, 15));

        // Panel de formulario
        JPanel formPanel = createFormPanel();

        // Panel de búsqueda
        JPanel searchPanel = createSearchPanel();

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Panel inferior (tabla)
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(5, 15, 15, 15));

        // Panel de tabla
        JPanel tablePanel = createTablePanel();

        // Panel de estado
        JPanel statusPanel = createStatusPanel();

        bottomPanel.add(tablePanel, BorderLayout.CENTER);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        // Añadir paneles al SplitPane
        mainSplitPane.setTopComponent(topPanel);
        mainSplitPane.setBottomComponent(bottomPanel);

        // Añadir componentes al panel de contenido
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(mainSplitPane, BorderLayout.CENTER);

        // Añadir al panel con CardLayout
        cardPanel.add(contentPanel, "main");
        add(cardPanel, BorderLayout.CENTER);

        // Ajustar el divisor del SplitPane cuando se redimensiona la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainSplitPane.setDividerLocation(0.4);
            }
        });

        // Aplicar animación de entrada
        SwingUtilities.invokeLater(() -> AnimationManager.fadeIn(this, 300));
    }

    /**
     * Crea el panel de encabezado.
     *
     * @return Panel de encabezado
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new TransparentPanel(AppTheme.PRIMARY_BLACK, 1.0f);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(0, 70));

        // Icono y título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titlePanel.setOpaque(false);

        // Icono (simulado con un panel redondeado)
        JPanel iconPanel = new TransparentPanel(AppTheme.PRIMARY_RED, 1.0f, 20);
        iconPanel.setPreferredSize(new Dimension(40, 40));
        titlePanel.add(iconPanel);

        // Título
        JLabel titleLabel = new JLabel("Gestión de Proveedores");
        titleLabel.setFont(AppTheme.TITLE_FONT);
        titleLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titlePanel.add(titleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Panel de información
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setOpaque(false);

        JLabel infoLabel = new JLabel("Sistema de Gestión CarMotors");
        infoLabel.setFont(AppTheme.SMALL_FONT);
        infoLabel.setForeground(AppTheme.ACCENT_LIGHT);
        infoPanel.add(infoLabel);

        headerPanel.add(infoPanel, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Crea el panel de formulario.
     *
     * @return Panel de formulario
     */
    private JPanel createFormPanel() {
        JPanel formWrapper = new TransparentPanel(AppTheme.PRIMARY_WHITE, 0.95f, AppTheme.BORDER_RADIUS);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.PRIMARY_RED, 2),
                new EmptyBorder(20, 20, 20, 20)));
        formWrapper.setLayout(new BorderLayout(0, 15));

        // Título del formulario con icono
        JPanel formTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        formTitlePanel.setOpaque(false);

        JLabel formIcon = new JLabel("\uD83D\uDCDD"); // Emoji de formulario
        formIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        formIcon.setForeground(AppTheme.PRIMARY_BLACK);
        formTitlePanel.add(formIcon);

        JLabel formTitle = new JLabel("Datos del Proveedor");
        formTitle.setFont(AppTheme.SUBTITLE_FONT);
        formTitle.setForeground(AppTheme.PRIMARY_BLACK);
        formTitlePanel.add(formTitle);

        formWrapper.add(formTitlePanel, BorderLayout.NORTH);

        // Panel de campos del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        JLabel lblId = new JLabel("ID:");
        lblId.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblId, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setFont(AppTheme.REGULAR_FONT);
        txtId.setBackground(AppTheme.SECONDARY_WHITE);
        txtId.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        formPanel.add(txtId, gbc);
        txtId.setColumns(5);

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        JLabel lblName = new JLabel("Nombre:");
        lblName.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblName, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        txtName = new ModernTextField();
        txtName.setPlaceholder("Nombre del proveedor");
        formPanel.add(txtName, gbc);
        txtName.setColumns(20);

        // Contacto
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        JLabel lblContact = new JLabel("Contacto:");
        lblContact.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblContact, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.4;
        txtContactName = new ModernTextField();
        txtContactName.setPlaceholder("Nombre de la persona de contacto");
        formPanel.add(txtContactName, gbc);
        txtContactName.setColumns(20);

        // Teléfono
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.1;
        JLabel lblPhone = new JLabel("Teléfono:");
        lblPhone.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblPhone, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.4;
        txtPhone = new ModernTextField();
        txtPhone.setPlaceholder("Número de teléfono");
        formPanel.add(txtPhone, gbc);
        txtPhone.setColumns(15);

        // Email
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblEmail, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        txtEmail = new ModernTextField();
        txtEmail.setPlaceholder("Correo electrónico");
        formPanel.add(txtEmail, gbc);
        txtEmail.setColumns(20);

        // Dirección
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        JLabel lblAddress = new JLabel("Dirección:");
        lblAddress.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblAddress, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 0.4;
        txtAddress = new ModernTextField();
        txtAddress.setPlaceholder("Dirección completa");
        formPanel.add(txtAddress, gbc);
        txtAddress.setColumns(30);

        // NIF/CIF
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0.1;
        JLabel lblTaxId = new JLabel("NIF/CIF:");
        lblTaxId.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblTaxId, gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 0.4;
        txtTaxId = new ModernTextField();
        txtTaxId.setPlaceholder("Identificación fiscal");
        formPanel.add(txtTaxId, gbc);
        txtTaxId.setColumns(15);

        // Estado
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.1;
        JLabel lblStatus = new JLabel("Estado:");
        lblStatus.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblStatus, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 0.4;
        cmbStatus = new JComboBox<>(new String[]{"ACTIVO", "INACTIVO"});
        cmbStatus.setFont(AppTheme.REGULAR_FONT);
        cmbStatus.setBackground(AppTheme.PRIMARY_WHITE);
        cmbStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        formPanel.add(cmbStatus, gbc);

        formWrapper.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnAdd = new ModernButton("Agregar", "success");
        btnAdd.setCornerRadius(20);
        btnAdd.addActionListener(this::btnAddActionPerformed);
        buttonsPanel.add(btnAdd);

        btnUpdate = new ModernButton("Actualizar", "primary");
        btnUpdate.setCornerRadius(20);
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        buttonsPanel.add(btnUpdate);

        btnDelete = new ModernButton("Eliminar", "danger");
        btnDelete.setCornerRadius(20);
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        buttonsPanel.add(btnDelete);

        btnClear = new ModernButton("Limpiar", "light");
        btnClear.setCornerRadius(20);
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
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 2),
                new EmptyBorder(15, 15, 15, 15)));

        // Usar GridBagLayout para mejor responsividad
        searchWrapper.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Icono de búsqueda
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel searchIcon = new JLabel("\uD83D\uDD0D"); // Emoji de lupa
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        searchWrapper.add(searchIcon, gbc);

        // Etiqueta de búsqueda
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setFont(AppTheme.REGULAR_FONT);
        searchWrapper.add(searchLabel, gbc);

        // Campo de búsqueda
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        txtSearch = new ModernTextField();
        txtSearch.setPlaceholder("Buscar por nombre, contacto, teléfono o email");
        txtSearch.setColumns(30);
        searchWrapper.add(txtSearch, gbc);

        // Botón de búsqueda
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        btnSearch = new ModernButton("Buscar", "secondary");
        btnSearch.setCornerRadius(20);
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        searchWrapper.add(btnSearch, gbc);

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
                BorderFactory.createLineBorder(AppTheme.PRIMARY_RED, 2),
                new EmptyBorder(20, 20, 20, 20)));
        tableWrapper.setLayout(new BorderLayout(0, 15));

        // Título de la tabla con icono
        JPanel tableTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tableTitlePanel.setOpaque(false);

        JLabel tableIcon = new JLabel("\uD83D\uDCC3"); // Emoji de lista
        tableIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        tableIcon.setForeground(AppTheme.PRIMARY_BLACK);
        tableTitlePanel.add(tableIcon);

        JLabel tableTitle = new JLabel("Lista de Proveedores");
        tableTitle.setFont(AppTheme.SUBTITLE_FONT);
        tableTitle.setForeground(AppTheme.PRIMARY_BLACK);
        tableTitlePanel.add(tableTitle);

        tableWrapper.add(tableTitlePanel, BorderLayout.NORTH);

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
        tblSuppliers.setRowHeight(35);
        tblSuppliers.setFont(AppTheme.REGULAR_FONT);
        tblSuppliers.setGridColor(AppTheme.ACCENT_LIGHT);
        tblSuppliers.setShowVerticalLines(true);
        tblSuppliers.setShowHorizontalLines(true);
        tblSuppliers.setRowMargin(5);
        tblSuppliers.setIntercellSpacing(new Dimension(10, 5));
        tblSuppliers.setFillsViewportHeight(true);
        tblSuppliers.setSelectionBackground(new Color(231, 76, 60, 50));
        tblSuppliers.setSelectionForeground(AppTheme.PRIMARY_BLACK);

        // Personalizar encabezado de tabla
        JTableHeader header = tblSuppliers.getTableHeader();
        header.setBackground(AppTheme.PRIMARY_BLACK);
        header.setForeground(AppTheme.SECONDARY_BLACK);
        header.setFont(AppTheme.HEADER_FONT);
        header.setBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY_RED, 2));
        header.setPreferredSize(new Dimension(0, 40));

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
                        setBackground(isSelected ? new Color(46, 204, 113, 100) : new Color(46, 204, 113, 50));
                        setForeground(AppTheme.SUCCESS_COLOR);
                    } else if ("INACTIVO".equals(status)) {
                        setBackground(isSelected ? new Color(231, 76, 60, 100) : new Color(231, 76, 60, 50));
                        setForeground(AppTheme.ERROR_COLOR);
                    }
                }

                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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

        // Scroll pane con bordes redondeados
        JScrollPane scrollPane = new JScrollPane(tblSuppliers);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.ACCENT_LIGHT));
        scrollPane.getViewport().setBackground(AppTheme.PRIMARY_WHITE);

        // Personalizar barras de desplazamiento
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = AppTheme.PRIMARY_RED;
                this.trackColor = AppTheme.ACCENT_LIGHT;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(AppTheme.PRIMARY_BLACK);
                button.setBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY_RED));
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createIncreaseButton(orientation);
                button.setBackground(AppTheme.PRIMARY_BLACK);
                button.setBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY_RED));
                return button;
            }
        });

        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        return tableWrapper;
    }

    /**
     * Crea el panel de estado.
     *
     * @return Panel de estado
     */
    private JPanel createStatusPanel() {
        JPanel statusWrapper = new TransparentPanel(AppTheme.PRIMARY_BLACK, 0.9f, AppTheme.BORDER_RADIUS);
        statusWrapper.setBorder(new EmptyBorder(10, 15, 10, 15));
        statusWrapper.setLayout(new BorderLayout());

        // Panel izquierdo para información de estado
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);

        statusLabel = new JLabel("Estado: Listo");
        statusLabel.setFont(AppTheme.REGULAR_FONT);
        statusLabel.setForeground(AppTheme.PRIMARY_WHITE);
        leftPanel.add(statusLabel);

        // Panel derecho para contadores
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        totalSuppliersLabel = new JLabel("Total proveedores: 0");
        totalSuppliersLabel.setFont(AppTheme.REGULAR_FONT);
        totalSuppliersLabel.setForeground(AppTheme.PRIMARY_WHITE);
        rightPanel.add(totalSuppliersLabel);

        statusWrapper.add(leftPanel, BorderLayout.WEST);
        statusWrapper.add(rightPanel, BorderLayout.EAST);

        return statusWrapper;
    }

    /**
     * Carga todos los proveedores en la tabla.
     */
    public void loadSuppliers() {
        try {
            controller.loadAllSuppliers();
            statusLabel.setText("Estado: Datos cargados correctamente");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar proveedores: " + e.getMessage(), e);

            // Limpiar la tabla y mostrar mensaje
            tableModel.setRowCount(0);

            // Añadir una fila con mensaje de error
            tableModel.addRow(new Object[]{"", "Error al cargar datos", "", "", "", "", "", ""});
            statusLabel.setText("Estado: Error al cargar datos");
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

        // Actualizar contador de proveedores
        totalSuppliersLabel.setText("Total proveedores: " + suppliers.size());

        // Actualizar estado
        statusLabel.setText("Estado: Datos actualizados correctamente");
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

        // Actualizar estado
        statusLabel.setText("Estado: Proveedor seleccionado (ID: " + txtId.getText() + ")");

        // Resaltar visualmente la selección y hacer scroll al formulario
        txtName.requestFocus();
        mainSplitPane.setDividerLocation(0.4);
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

        // Actualizar estado
        statusLabel.setText("Estado: Formulario limpiado");
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
        statusLabel.setText("Estado: Error - Acción no completada");
    }

    /**
     * Muestra un mensaje de información.
     *
     * @param message El mensaje de información
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText("Estado: Operación completada con éxito");
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
            statusLabel.setText("Estado: Búsqueda realizada");
        }
    }
}
