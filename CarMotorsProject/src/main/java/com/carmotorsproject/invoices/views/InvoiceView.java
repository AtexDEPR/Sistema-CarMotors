package com.carmotorsproject.invoices.views;

import com.carmotorsproject.invoices.controller.InvoiceController;
import com.carmotorsproject.invoices.model.Invoice;
import com.carmotorsproject.services.model.Service;
import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.utils.PDFGenerator;
import com.carmotorsproject.utils.EmailService;
import com.carmotorsproject.utils.DateUtil;
import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.ModernTextField;
import com.carmotorsproject.ui.components.RoundedPanel;
import com.carmotorsproject.ui.components.TransparentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Vista para la gestión de facturas.
 * Proporciona una interfaz de usuario para ver, crear y gestionar facturas.
 */
public class InvoiceView extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(InvoiceView.class.getName());

    // Colores personalizados
    private final Color accentBlue = new Color(0, 120, 212);
    private final Color secondaryBackground = new Color(245, 245, 245);

    // Controlador
    private InvoiceController controller;

    // Utilidades
    private final PDFGenerator pdfGenerator;
    private final EmailService emailService;
    private final DateUtil dateUtil;
    private final DecimalFormat currencyFormat;
    private final SimpleDateFormat dateFormat;

    // Componentes de la UI - Paneles principales
    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private JPanel searchPanel;
    private JPanel statusPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Estados de la vista
    private static final String CARD_LIST = "LIST";
    private static final String CARD_FORM = "FORM";

    // Componentes de la UI - Campos del formulario
    private JTextField invoiceIdField;
    private JTextField invoiceNumberField;
    private JComboBox<String> serviceComboBox;
    private JComboBox<String> customerComboBox;
    private JTextField issueDateField;
    private JTextField dueDateField;
    private JTextField subtotalField;
    private JTextField taxRateField;
    private JTextField taxAmountField;
    private JTextField discountField;
    private JTextField totalField;
    private JComboBox<String> paymentMethodComboBox;
    private JComboBox<String> paymentStatusComboBox;
    private JTextArea notesArea;

    // Componentes de la UI - Tabla
    private JTable invoiceTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;

    // Componentes de la UI - Botones
    private ModernButton newButton;
    private ModernButton saveButton;
    private ModernButton updateButton;
    private ModernButton deleteButton;
    private ModernButton clearButton;
    private ModernButton generatePdfButton;
    private ModernButton sendEmailButton;
    private ModernButton markAsPaidButton;
    private ModernButton markAsSentButton;
    private ModernButton markAsCancelledButton;

    // Componentes de la UI - Búsqueda
    private ModernTextField searchField;
    private JComboBox<String> searchTypeComboBox;
    private ModernButton searchButton;
    private ModernButton clearSearchButton;

    // Componentes de la UI - Estado
    private JLabel statusLabel;

    // Datos
    private List<Invoice> invoices;
    private List<Service> services;
    private List<Customer> customers;
    private Invoice currentInvoice;

    /**
     * Constructor que inicializa la vista.
     */
    public InvoiceView() {
        // Inicializar utilidades
        pdfGenerator = new PDFGenerator();
        emailService = new EmailService();
        dateUtil = new DateUtil();
        currencyFormat = new DecimalFormat("$#,##0.00");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Inicializar UI
        initializeUI();

        // Inicializar controlador
        try {
            controller = new InvoiceController(this);

            // Cargar datos
            loadData();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar el controlador", e);
            showError("Error al inicializar: " + e.getMessage());
        }
    }

    /**
     * Inicializa los componentes de la UI.
     */
    private void initializeUI() {
        // Configuración del panel principal
        setLayout(new BorderLayout());
        setBackground(AppTheme.PRIMARY_WHITE);

        // Panel de título
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Panel principal con CardLayout para alternar entre lista y formulario
        cardPanel = new JPanel(new CardLayout());
        cardPanel.setOpaque(false);
        cardLayout = (CardLayout) cardPanel.getLayout();

        // Panel de lista (tabla)
        JPanel listPanel = new JPanel(new BorderLayout(10, 10));
        listPanel.setOpaque(false);
        listPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Panel de búsqueda
        searchPanel = createSearchPanel();
        listPanel.add(searchPanel, BorderLayout.NORTH);

        // Panel de tabla
        tablePanel = createTablePanel();
        listPanel.add(tablePanel, BorderLayout.CENTER);

        // Panel de botones para la lista
        JPanel listButtonPanel = createListButtonPanel();
        listPanel.add(listButtonPanel, BorderLayout.SOUTH);

        // Panel de formulario
        JPanel formContainer = new JPanel(new BorderLayout(10, 10));
        formContainer.setOpaque(false);
        formContainer.setBorder(new EmptyBorder(10, 15, 10, 15));

        formPanel = createFormPanel();
        formContainer.add(formPanel, BorderLayout.CENTER);

        // Panel de botones para el formulario
        JPanel formButtonPanel = createFormButtonPanel();
        formContainer.add(formButtonPanel, BorderLayout.SOUTH);

        // Agregar paneles al CardLayout
        cardPanel.add(listPanel, CARD_LIST);
        cardPanel.add(formContainer, CARD_FORM);

        // Mostrar la vista de lista por defecto
        cardLayout.show(cardPanel, CARD_LIST);

        // Agregar el panel de tarjetas al panel principal
        add(cardPanel, BorderLayout.CENTER);

        // Panel de estado
        statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);

        // Configurar manejadores de eventos
        setupEventHandlers();
    }

    /**
     * Crea el panel de título.
     *
     * @return Panel de título
     */
    private JPanel createTitlePanel() {
        JPanel titlePanel = new TransparentPanel(AppTheme.PRIMARY_BLACK, 0.9f);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        titlePanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("Gestión de Facturas");
        titleLabel.setFont(AppTheme.TITLE_FONT);
        titleLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        return titlePanel;
    }

    /**
     * Crea el panel de búsqueda.
     *
     * @return Panel de búsqueda
     */
    private JPanel createSearchPanel() {
        RoundedPanel panel = new RoundedPanel(secondaryBackground, 15);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Etiqueta de búsqueda
        JLabel lblSearch = new JLabel("Buscar por:");
        lblSearch.setFont(AppTheme.REGULAR_FONT);
        lblSearch.setForeground(AppTheme.PRIMARY_BLACK);
        panel.add(lblSearch);

        // Combo de tipo de búsqueda
        searchTypeComboBox = new JComboBox<>(new String[]{
                "Número de Factura", "Cliente", "Servicio", "Estado de Pago"
        });
        searchTypeComboBox.setFont(AppTheme.REGULAR_FONT);
        searchTypeComboBox.setPreferredSize(new Dimension(150, 30));
        panel.add(searchTypeComboBox);

        // Campo de búsqueda
        searchField = new ModernTextField("20");
        searchField.setPreferredSize(new Dimension(200, 30));
        panel.add(searchField);

        // Botón de búsqueda
        searchButton = new ModernButton("Buscar");
        searchButton.setPreferredSize(new Dimension(100, 30));
        panel.add(searchButton);

        // Botón para limpiar búsqueda
        clearSearchButton = new ModernButton("Limpiar");
        clearSearchButton.setPreferredSize(new Dimension(100, 30));
        clearSearchButton.setColors(AppTheme.ACCENT_GRAY, AppTheme.ACCENT_GRAY.darker(),
                AppTheme.ACCENT_GRAY.darker().darker(), AppTheme.PRIMARY_WHITE);
        panel.add(clearSearchButton);

        return panel;
    }

    /**
     * Crea el panel de tabla.
     *
     * @return Panel de tabla
     */
    private JPanel createTablePanel() {
        RoundedPanel panel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Crear modelo de tabla
        String[] columnNames = {
                "ID", "Número", "Servicio", "Cliente", "Fecha Emisión",
                "Fecha Vencimiento", "Total", "Estado", "Enviada", "Pagada", "Cancelada"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 8 || columnIndex == 9 || columnIndex == 10) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        // Crear tabla
        invoiceTable = new JTable(tableModel);
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoiceTable.setRowHeight(30);
        invoiceTable.setFont(AppTheme.REGULAR_FONT);
        invoiceTable.setShowGrid(false);
        invoiceTable.setIntercellSpacing(new Dimension(0, 0));

        // Personalizar encabezado de tabla
        JTableHeader header = invoiceTable.getTableHeader();
        header.setFont(AppTheme.SUBTITLE_FONT);
        header.setBackground(AppTheme.PRIMARY_BLACK);
        header.setForeground(AppTheme.PRIMARY_WHITE);
        header.setBorder(null);
        header.setPreferredSize(new Dimension(0, 35));

        // Personalizar renderizador de celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        // Aplicar renderizadores
        invoiceTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        invoiceTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        invoiceTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        invoiceTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        invoiceTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        invoiceTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);

        // Configurar anchos de columna
        invoiceTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        invoiceTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        invoiceTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        invoiceTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        invoiceTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        invoiceTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        invoiceTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        invoiceTable.getColumnModel().getColumn(7).setPreferredWidth(100);
        invoiceTable.getColumnModel().getColumn(8).setPreferredWidth(70);
        invoiceTable.getColumnModel().getColumn(9).setPreferredWidth(70);
        invoiceTable.getColumnModel().getColumn(10).setPreferredWidth(70);

        // Agregar ordenador de filas
        tableSorter = new TableRowSorter<>(tableModel);
        invoiceTable.setRowSorter(tableSorter);

        // Agregar tabla a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppTheme.PRIMARY_WHITE);

        // Agregar JScrollPane al panel
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel de botones para la vista de lista.
     *
     * @return Panel de botones
     */
    private JPanel createListButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setOpaque(false);

        // Botón para nueva factura
        newButton = new ModernButton("Nueva Factura");
        newButton.setPreferredSize(new Dimension(150, 40));
        newButton.setColors(AppTheme.SUCCESS_COLOR, AppTheme.SUCCESS_COLOR.darker(),
                AppTheme.SUCCESS_COLOR.darker().darker(), AppTheme.PRIMARY_WHITE);
        panel.add(newButton);

        // Botón para editar
        updateButton = new ModernButton("Editar");
        updateButton.setPreferredSize(new Dimension(120, 40));
        updateButton.setEnabled(false);
        panel.add(updateButton);

        // Botón para eliminar
        deleteButton = new ModernButton("Eliminar");
        deleteButton.setPreferredSize(new Dimension(120, 40));
        deleteButton.setColors(AppTheme.ERROR_COLOR, AppTheme.ERROR_COLOR.darker(),
                AppTheme.ERROR_COLOR.darker().darker(), AppTheme.PRIMARY_WHITE);
        deleteButton.setEnabled(false);
        panel.add(deleteButton);

        // Botón para generar PDF
        generatePdfButton = new ModernButton("Generar PDF");
        generatePdfButton.setPreferredSize(new Dimension(140, 40));
        generatePdfButton.setColors(accentBlue, accentBlue.darker(),
                accentBlue.darker().darker(), AppTheme.PRIMARY_WHITE);
        generatePdfButton.setEnabled(false);
        panel.add(generatePdfButton);

        // Botón para enviar email
        sendEmailButton = new ModernButton("Enviar Email");
        sendEmailButton.setPreferredSize(new Dimension(140, 40));
        sendEmailButton.setColors(accentBlue, accentBlue.darker(),
                accentBlue.darker().darker(), AppTheme.PRIMARY_WHITE);
        sendEmailButton.setEnabled(false);
        panel.add(sendEmailButton);

        return panel;
    }

    /**
     * Crea el panel de formulario.
     *
     * @return Panel de formulario
     */
    private JPanel createFormPanel() {
        RoundedPanel panel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel de contenido del formulario
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Panel de información básica
        JPanel basicInfoPanel = new JPanel(new GridBagLayout());
        basicInfoPanel.setOpaque(false);
        basicInfoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1),
                "Información Básica",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                AppTheme.SUBTITLE_FONT,
                AppTheme.PRIMARY_BLACK));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID de Factura
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel lblInvoiceId = new JLabel("ID:");
        lblInvoiceId.setFont(AppTheme.REGULAR_FONT);
        basicInfoPanel.add(lblInvoiceId, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        invoiceIdField = new ModernTextField("10");
        invoiceIdField.setEditable(false);
        basicInfoPanel.add(invoiceIdField, gbc);

        // Número de Factura
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel lblInvoiceNumber = new JLabel("Número:");
        lblInvoiceNumber.setFont(AppTheme.REGULAR_FONT);
        basicInfoPanel.add(lblInvoiceNumber, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        invoiceNumberField = new ModernTextField("15");
        invoiceNumberField.setEditable(false);
        basicInfoPanel.add(invoiceNumberField, gbc);

        // Servicio
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel lblService = new JLabel("Servicio:");
        lblService.setFont(AppTheme.REGULAR_FONT);
        basicInfoPanel.add(lblService, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.8;
        gbc.gridwidth = 3;
        serviceComboBox = new JComboBox<>();
        serviceComboBox.setFont(AppTheme.REGULAR_FONT);
        basicInfoPanel.add(serviceComboBox, gbc);

        // Cliente
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        gbc.gridwidth = 1;
        JLabel lblCustomer = new JLabel("Cliente:");
        lblCustomer.setFont(AppTheme.REGULAR_FONT);
        basicInfoPanel.add(lblCustomer, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.8;
        gbc.gridwidth = 3;
        customerComboBox = new JComboBox<>();
        customerComboBox.setFont(AppTheme.REGULAR_FONT);
        basicInfoPanel.add(customerComboBox, gbc);

        // Fecha de Emisión
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        gbc.gridwidth = 1;
        JLabel lblIssueDate = new JLabel("Fecha Emisión:");
        lblIssueDate.setFont(AppTheme.REGULAR_FONT);
        basicInfoPanel.add(lblIssueDate, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        issueDateField = new ModernTextField("10");
        basicInfoPanel.add(issueDateField, gbc);

        // Fecha de Vencimiento
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        JLabel lblDueDate = new JLabel("Fecha Vencimiento:");
        lblDueDate.setFont(AppTheme.REGULAR_FONT);
        basicInfoPanel.add(lblDueDate, gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        dueDateField = new ModernTextField("10");
        basicInfoPanel.add(dueDateField, gbc);

        // Panel de totales
        JPanel totalsPanel = new JPanel(new GridBagLayout());
        totalsPanel.setOpaque(false);
        totalsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1),
                "Totales",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                AppTheme.SUBTITLE_FONT,
                AppTheme.PRIMARY_BLACK));

        // Subtotal
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel lblSubtotal = new JLabel("Subtotal:");
        lblSubtotal.setFont(AppTheme.REGULAR_FONT);
        totalsPanel.add(lblSubtotal, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        subtotalField = new ModernTextField("15");
        totalsPanel.add(subtotalField, gbc);

        // Tasa de Impuesto
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel lblTaxRate = new JLabel("Tasa Impuesto:");
        lblTaxRate.setFont(AppTheme.REGULAR_FONT);
        totalsPanel.add(lblTaxRate, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        taxRateField = new ModernTextField("15");
        totalsPanel.add(taxRateField, gbc);

        // Monto de Impuesto
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel lblTaxAmount = new JLabel("Monto Impuesto:");
        lblTaxAmount.setFont(AppTheme.REGULAR_FONT);
        totalsPanel.add(lblTaxAmount, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        taxAmountField = new ModernTextField("15");
        taxAmountField.setEditable(false);
        totalsPanel.add(taxAmountField, gbc);

        // Descuento
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel lblDiscount = new JLabel("Descuento:");
        lblDiscount.setFont(AppTheme.REGULAR_FONT);
        totalsPanel.add(lblDiscount, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        discountField = new ModernTextField("15");
        totalsPanel.add(discountField, gbc);

        // Total
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        JLabel lblTotal = new JLabel("Total:");
        lblTotal.setFont(AppTheme.SUBTITLE_FONT);
        lblTotal.setForeground(AppTheme.PRIMARY_BLACK);
        totalsPanel.add(lblTotal, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        totalField = new ModernTextField("15");
        totalField.setEditable(false);
        totalField.setFont(AppTheme.SUBTITLE_FONT);
        totalField.setForeground(AppTheme.PRIMARY_BLACK);
        totalsPanel.add(totalField, gbc);

        // Método de Pago
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        JLabel lblPaymentMethod = new JLabel("Método de Pago:");
        lblPaymentMethod.setFont(AppTheme.REGULAR_FONT);
        totalsPanel.add(lblPaymentMethod, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        paymentMethodComboBox = new JComboBox<>(new String[]{"", "EFECTIVO", "TARJETA_CREDITO", "TARJETA_DEBITO", "TRANSFERENCIA", "CHEQUE", "OTRO"});
        paymentMethodComboBox.setFont(AppTheme.REGULAR_FONT);
        totalsPanel.add(paymentMethodComboBox, gbc);

        // Estado de Pago
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        JLabel lblPaymentStatus = new JLabel("Estado de Pago:");
        lblPaymentStatus.setFont(AppTheme.REGULAR_FONT);
        totalsPanel.add(lblPaymentStatus, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        paymentStatusComboBox = new JComboBox<>(new String[]{"PENDIENTE", "PAGADA", "CANCELADA"});
        paymentStatusComboBox.setFont(AppTheme.REGULAR_FONT);
        totalsPanel.add(paymentStatusComboBox, gbc);

        // Panel de notas
        JPanel notesPanel = new JPanel(new BorderLayout(10, 10));
        notesPanel.setOpaque(false);
        notesPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1),
                "Notas",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                AppTheme.SUBTITLE_FONT,
                AppTheme.PRIMARY_BLACK));

        notesArea = new JTextArea(5, 30);
        notesArea.setFont(AppTheme.REGULAR_FONT);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        notesScrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1));
        notesPanel.add(notesScrollPane, BorderLayout.CENTER);

        // Agregar paneles al panel de contenido
        contentPanel.add(basicInfoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(totalsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(notesPanel);

        // Agregar panel de contenido al panel principal
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel de botones para el formulario.
     *
     * @return Panel de botones
     */
    private JPanel createFormButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setOpaque(false);

        // Botón para guardar
        saveButton = new ModernButton("Guardar");
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.setColors(AppTheme.SUCCESS_COLOR, AppTheme.SUCCESS_COLOR.darker(),
                AppTheme.SUCCESS_COLOR.darker().darker(), AppTheme.PRIMARY_WHITE);
        panel.add(saveButton);

        // Botón para cancelar
        ModernButton cancelButton = new ModernButton("Cancelar");
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setColors(AppTheme.ACCENT_GRAY, AppTheme.ACCENT_GRAY.darker(),
                AppTheme.ACCENT_GRAY.darker().darker(), AppTheme.PRIMARY_WHITE);
        cancelButton.addActionListener(e -> {
            clearForm();
            cardLayout.show(cardPanel, CARD_LIST);
        });
        panel.add(cancelButton);

        // Botón para limpiar
        clearButton = new ModernButton("Limpiar");
        clearButton.setPreferredSize(new Dimension(120, 40));
        panel.add(clearButton);

        // Botones de estado
        markAsPaidButton = new ModernButton("Marcar como Pagada");
        markAsPaidButton.setPreferredSize(new Dimension(180, 40));
        markAsPaidButton.setColors(new Color(0, 128, 0), new Color(0, 100, 0),
                new Color(0, 80, 0), AppTheme.PRIMARY_WHITE);
        markAsPaidButton.setEnabled(false);
        panel.add(markAsPaidButton);

        markAsSentButton = new ModernButton("Marcar como Enviada");
        markAsSentButton.setPreferredSize(new Dimension(180, 40));
        markAsSentButton.setColors(accentBlue, accentBlue.darker(),
                accentBlue.darker().darker(), AppTheme.PRIMARY_WHITE);
        markAsSentButton.setEnabled(false);
        panel.add(markAsSentButton);

        markAsCancelledButton = new ModernButton("Marcar como Cancelada");
        markAsCancelledButton.setPreferredSize(new Dimension(180, 40));
        markAsCancelledButton.setColors(AppTheme.ERROR_COLOR, AppTheme.ERROR_COLOR.darker(),
                AppTheme.ERROR_COLOR.darker().darker(), AppTheme.PRIMARY_WHITE);
        markAsCancelledButton.setEnabled(false);
        panel.add(markAsCancelledButton);

        return panel;
    }

    /**
     * Crea el panel de estado.
     *
     * @return Panel de estado
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(secondaryBackground);
        panel.setBorder(new MatteBorder(1, 0, 0, 0, AppTheme.ACCENT_GRAY));
        panel.setPreferredSize(new Dimension(0, 25));

        statusLabel = new JLabel(" Sistema de Gestión de Facturas");
        statusLabel.setFont(AppTheme.SMALL_FONT);
        statusLabel.setForeground(AppTheme.ACCENT_GRAY);
        panel.add(statusLabel, BorderLayout.WEST);

        JLabel versionLabel = new JLabel("v1.0 ");
        versionLabel.setFont(AppTheme.SMALL_FONT);
        versionLabel.setForeground(AppTheme.ACCENT_GRAY);
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(versionLabel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Configura los manejadores de eventos para los componentes de la UI.
     */
    private void setupEventHandlers() {
        // Listener para la tabla
        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = invoiceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedRow = invoiceTable.convertRowIndexToModel(selectedRow);
                    int invoiceId = (int) tableModel.getValueAt(selectedRow, 0);

                    // Habilitar botones
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    generatePdfButton.setEnabled(true);
                    sendEmailButton.setEnabled(true);

                    // Si es doble clic, cargar detalles
                    if (e.getClickCount() == 2) {
                        controller.loadInvoiceDetails(invoiceId);
                        cardLayout.show(cardPanel, CARD_FORM);
                    }
                }
            }
        });

        // Listener para el botón de búsqueda
        searchButton.addActionListener(e -> searchInvoices());

        // Listener para el botón de limpiar búsqueda
        clearSearchButton.addActionListener(e -> {
            searchField.setText("");
            controller.loadInvoices();
        });

        // Listener para el botón de nueva factura
        newButton.addActionListener(e -> {
            clearForm();
            enableForm(true);
            saveButton.setEnabled(true);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            generatePdfButton.setEnabled(false);
            sendEmailButton.setEnabled(false);
            markAsPaidButton.setEnabled(false);
            markAsSentButton.setEnabled(false);
            markAsCancelledButton.setEnabled(false);

            // Establecer valores predeterminados
            issueDateField.setText(dateFormat.format(new Date()));
            taxRateField.setText("0.16"); // Tasa de impuesto predeterminada (16%)
            discountField.setText("0.00");

            // Cambiar a la vista de formulario
            cardLayout.show(cardPanel, CARD_FORM);

            // Establecer el foco en el combo de servicio
            serviceComboBox.requestFocus();
        });

        // Listener para el botón de guardar
        saveButton.addActionListener(e -> saveInvoice());

        // Listener para el botón de actualizar
        updateButton.addActionListener(e -> updateInvoice());

        // Listener para el botón de eliminar
        deleteButton.addActionListener(e -> deleteInvoice());

        // Listener para el botón de limpiar
        clearButton.addActionListener(e -> clearForm());

        // Listener para el botón de generar PDF
        generatePdfButton.addActionListener(e -> generatePdf());

        // Listener para el botón de enviar email
        sendEmailButton.addActionListener(e -> sendEmail());

        // Listener para el botón de marcar como pagada
        markAsPaidButton.addActionListener(e -> markAsPaid());

        // Listener para el botón de marcar como enviada
        markAsSentButton.addActionListener(e -> markAsSent());

        // Listener para el botón de marcar como cancelada
        markAsCancelledButton.addActionListener(e -> markAsCancelled());

        // Listeners para calcular totales
        ActionListener calculateTotalsListener = e -> calculateTotals();
        subtotalField.addActionListener(calculateTotalsListener);
        taxRateField.addActionListener(calculateTotalsListener);
        discountField.addActionListener(calculateTotalsListener);

        // Listener para cambios en los campos
        subtotalField.addCaretListener(e -> calculateTotals());
        taxRateField.addCaretListener(e -> calculateTotals());
        discountField.addCaretListener(e -> calculateTotals());
    }

    /**
     * Carga datos desde la base de datos.
     */
    private void loadData() {
        controller.loadInvoices();
        controller.loadServices();
        controller.loadCustomers();
    }

    /**
     * Guarda una nueva factura.
     */
    private void saveInvoice() {
        try {
            Invoice invoice = getInvoiceFromForm();
            if (invoice != null) {
                controller.addInvoice(invoice);
                clearForm();
                enableForm(false);
                cardLayout.show(cardPanel, CARD_LIST);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al guardar factura", ex);
            showError("Error al guardar factura: " + ex.getMessage());
        }
    }

    /**
     * Actualiza una factura existente.
     */
    private void updateInvoice() {
        try {
            Invoice invoice = getInvoiceFromForm();
            if (invoice != null) {
                controller.updateInvoice(invoice);
                clearForm();
                enableForm(false);
                cardLayout.show(cardPanel, CARD_LIST);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar factura", ex);
            showError("Error al actualizar factura: " + ex.getMessage());
        }
    }

    /**
     * Elimina una factura.
     */
    private void deleteInvoice() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro que desea eliminar esta factura?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                controller.deleteInvoice(invoiceId);
                clearForm();
                enableForm(false);
                cardLayout.show(cardPanel, CARD_LIST);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al eliminar factura", ex);
            showError("Error al eliminar factura: " + ex.getMessage());
        }
    }

    /**
     * Genera un PDF para la factura actual.
     */
    private void generatePdf() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());
            controller.generatePdf(invoiceId);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al generar PDF", ex);
            showError("Error al generar PDF: " + ex.getMessage());
        }
    }

    /**
     * Envía un email con el PDF de la factura.
     */
    private void sendEmail() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());
            controller.sendEmail(invoiceId);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al enviar email", ex);
            showError("Error al enviar email: " + ex.getMessage());
        }
    }

    /**
     * Marca la factura actual como pagada.
     */
    private void markAsPaid() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());
            String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();

            if (paymentMethod == null || paymentMethod.isEmpty()) {
                showError("Por favor, seleccione un método de pago.");
                return;
            }

            controller.markAsPaid(invoiceId, paymentMethod);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al marcar factura como pagada", ex);
            showError("Error al marcar factura como pagada: " + ex.getMessage());
        }
    }

    /**
     * Marca la factura actual como enviada.
     */
    private void markAsSent() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());
            controller.markAsSent(invoiceId);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al marcar factura como enviada", ex);
            showError("Error al marcar factura como enviada: " + ex.getMessage());
        }
    }

    /**
     * Marca la factura actual como cancelada.
     */
    private void markAsCancelled() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());

            String reason = JOptionPane.showInputDialog(
                    this,
                    "Ingrese el motivo de la cancelación:",
                    "Cancelar Factura",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (reason != null && !reason.trim().isEmpty()) {
                controller.markAsCancelled(invoiceId, reason);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al marcar factura como cancelada", ex);
            showError("Error al marcar factura como cancelada: " + ex.getMessage());
        }
    }

    /**
     * Busca facturas según los criterios especificados.
     */
    private void searchInvoices() {
        String searchText = searchField.getText().trim();
        String searchType = (String) searchTypeComboBox.getSelectedItem();

        if (searchText.isEmpty()) {
            controller.loadInvoices();
            return;
        }

        try {
            if ("Número de Factura".equals(searchType)) {
                controller.searchByInvoiceNumber(searchText);
            } else if ("Cliente".equals(searchType)) {
                try {
                    int customerId = Integer.parseInt(searchText);
                    controller.searchByCustomer(customerId);
                } catch (NumberFormatException ex) {
                    showError("El ID del cliente debe ser un número.");
                }
            } else if ("Servicio".equals(searchType)) {
                try {
                    int serviceId = Integer.parseInt(searchText);
                    controller.searchByService(serviceId);
                } catch (NumberFormatException ex) {
                    showError("El ID del servicio debe ser un número.");
                }
            } else if ("Estado de Pago".equals(searchType)) {
                controller.searchByPaymentStatus(searchText);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar facturas", ex);
            showError("Error al buscar facturas: " + ex.getMessage());
        }
    }

    /**
     * Obtiene un objeto Invoice a partir de los datos del formulario.
     *
     * @return Un objeto Invoice con los datos del formulario
     * @throws ParseException Si hay un error al analizar las fechas o números
     */
    private Invoice getInvoiceFromForm() throws ParseException {
        Invoice invoice = new Invoice();

        // Establecer ID si se está actualizando
        if (!invoiceIdField.getText().isEmpty()) {
            invoice.setInvoiceId(Integer.parseInt(invoiceIdField.getText()));
        }

        // Establecer número de factura si está disponible
        if (!invoiceNumberField.getText().isEmpty()) {
            invoice.setInvoiceNumber(invoiceNumberField.getText());
        }

        // Obtener ID de servicio del combo
        String serviceItem = (String) serviceComboBox.getSelectedItem();
        if (serviceItem != null && !serviceItem.isEmpty()) {
            int serviceId = Integer.parseInt(serviceItem.split(" - ")[0]);
            invoice.setServiceId(serviceId);
        } else {
            showError("Por favor, seleccione un servicio.");
            return null;
        }

        // Obtener ID de cliente del combo
        String customerItem = (String) customerComboBox.getSelectedItem();
        if (customerItem != null && !customerItem.isEmpty()) {
            int customerId = Integer.parseInt(customerItem.split(" - ")[0]);
            invoice.setCustomerId(customerId);
        } else {
            showError("Por favor, seleccione un cliente.");
            return null;
        }

        // Establecer fechas
        if (!issueDateField.getText().isEmpty()) {
            invoice.setIssueDate(dateFormat.parse(issueDateField.getText()));
        } else {
            invoice.setIssueDate(new Date());
        }

        if (!dueDateField.getText().isEmpty()) {
            invoice.setDueDate(dateFormat.parse(dueDateField.getText()));
        }

        // Establecer montos
        if (!subtotalField.getText().isEmpty()) {
            invoice.setSubtotal(Double.parseDouble(subtotalField.getText().replace("$", "").replace(",", "")));
        } else {
            showError("Por favor, ingrese un subtotal.");
            return null;
        }

        if (!taxRateField.getText().isEmpty()) {
            invoice.setTaxRate(Double.parseDouble(taxRateField.getText()));
        } else {
            invoice.setTaxRate(0.16); // Tasa de impuesto predeterminada (16%)
        }

        if (!discountField.getText().isEmpty()) {
            invoice.setDiscount(Double.parseDouble(discountField.getText().replace("$", "").replace(",", "")));
        } else {
            invoice.setDiscount(0.0);
        }

        // Calcular monto de impuesto y total
        invoice.calculateTotals();

        // Establecer método de pago y estado
        invoice.setPaymentMethod((String) paymentMethodComboBox.getSelectedItem());
        invoice.setPaymentStatus((String) paymentStatusComboBox.getSelectedItem());

        // Establecer notas
        invoice.setNotes(notesArea.getText());

        return invoice;
    }

    /**
     * Calcula el monto de impuesto y total basado en el subtotal, tasa de impuesto y descuento.
     */
    private void calculateTotals() {
        try {
            double subtotal = 0.0;
            double taxRate = 0.16; // Tasa de impuesto predeterminada (16%)
            double discount = 0.0;

            if (!subtotalField.getText().isEmpty()) {
                subtotal = Double.parseDouble(subtotalField.getText().replace("$", "").replace(",", ""));
            }

            if (!taxRateField.getText().isEmpty()) {
                taxRate = Double.parseDouble(taxRateField.getText());
            }

            if (!discountField.getText().isEmpty()) {
                discount = Double.parseDouble(discountField.getText().replace("$", "").replace(",", ""));
            }

            double taxAmount = subtotal * taxRate;
            double total = subtotal + taxAmount - discount;

            taxAmountField.setText(currencyFormat.format(taxAmount));
            totalField.setText(currencyFormat.format(total));
        } catch (NumberFormatException ex) {
            // Ignorar errores de análisis durante la escritura
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    public void clearForm() {
        invoiceIdField.setText("");
        invoiceNumberField.setText("");
        serviceComboBox.setSelectedIndex(-1);
        customerComboBox.setSelectedIndex(-1);
        issueDateField.setText("");
        dueDateField.setText("");
        subtotalField.setText("");
        taxRateField.setText("");
        taxAmountField.setText("");
        discountField.setText("");
        totalField.setText("");
        paymentMethodComboBox.setSelectedIndex(-1);
        paymentStatusComboBox.setSelectedIndex(0); // Predeterminado a PENDIENTE
        notesArea.setText("");

        currentInvoice = null;
    }

    /**
     * Habilita o deshabilita los campos del formulario.
     *
     * @param enable true para habilitar los campos, false para deshabilitarlos
     */
    public void enableForm(boolean enable) {
        serviceComboBox.setEnabled(enable);
        customerComboBox.setEnabled(enable);
        issueDateField.setEnabled(enable);
        dueDateField.setEnabled(enable);
        subtotalField.setEnabled(enable);
        taxRateField.setEnabled(enable);
        discountField.setEnabled(enable);
        paymentMethodComboBox.setEnabled(enable);
        paymentStatusComboBox.setEnabled(enable);
        notesArea.setEnabled(enable);

        saveButton.setEnabled(enable);
    }

    /**
     * Actualiza la tabla de facturas con la lista proporcionada.
     *
     * @param invoices La lista de facturas a mostrar
     */
    public void updateInvoiceTable(List<Invoice> invoices) {
        this.invoices = invoices;

        // Limpiar la tabla
        tableModel.setRowCount(0);

        // Agregar facturas a la tabla
        for (Invoice invoice : invoices) {
            Object[] row = {
                    invoice.getInvoiceId(),
                    invoice.getInvoiceNumber(),
                    invoice.getServiceId(),
                    invoice.getCustomerId(),
                    dateFormat.format(invoice.getIssueDate()),
                    invoice.getDueDate() != null ? dateFormat.format(invoice.getDueDate()) : "",
                    currencyFormat.format(invoice.getTotal()),
                    invoice.getPaymentStatus(),
                    invoice.isSent(),
                    invoice.isPaid(),
                    invoice.isCancelled()
            };
            tableModel.addRow(row);
        }

        // Actualizar etiqueta de estado
        statusLabel.setText("Cargadas " + invoices.size() + " facturas");
    }

    /**
     * Actualiza el combo de servicios con la lista proporcionada.
     *
     * @param services La lista de servicios a mostrar
     */
    public void updateServiceComboBox(List<Service> services) {
        this.services = services;

        // Limpiar el combo
        serviceComboBox.removeAllItems();

        // Agregar servicios al combo
        for (Service service : services) {
            serviceComboBox.addItem(service.getServiceId() + " - " + service.getDescription());
        }
    }

    /**
     * Actualiza el combo de clientes con la lista proporcionada.
     *
     * @param customers La lista de clientes a mostrar
     */
    public void updateCustomerComboBox(List<Customer> customers) {
        this.customers = customers;

        // Limpiar el combo
        customerComboBox.removeAllItems();

        // Agregar clientes al combo
        for (Customer customer : customers) {
            customerComboBox.addItem(customer.getCustomerId() + " - " + customer.getFullName());
        }
    }

    /**
     * Rellena el formulario con los detalles de la factura proporcionada.
     *
     * @param invoice La factura a mostrar
     */
    public void populateInvoiceForm(Invoice invoice) {
        this.currentInvoice = invoice;

        // Establecer campos del formulario
        invoiceIdField.setText(String.valueOf(invoice.getInvoiceId()));
        invoiceNumberField.setText(invoice.getInvoiceNumber());

        // Establecer servicio
        for (int i = 0; i < serviceComboBox.getItemCount(); i++) {
            String item = serviceComboBox.getItemAt(i);
            if (item.startsWith(invoice.getServiceId() + " - ")) {
                serviceComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Establecer cliente
        for (int i = 0; i < customerComboBox.getItemCount(); i++) {
            String item = customerComboBox.getItemAt(i);
            if (item.startsWith(invoice.getCustomerId() + " - ")) {
                customerComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Establecer fechas
        issueDateField.setText(dateFormat.format(invoice.getIssueDate()));
        if (invoice.getDueDate() != null) {
            dueDateField.setText(dateFormat.format(invoice.getDueDate()));
        } else {
            dueDateField.setText("");
        }

        // Establecer montos
        subtotalField.setText(String.format("%.2f", invoice.getSubtotal()));
        taxRateField.setText(String.format("%.2f", invoice.getTaxRate()));
        taxAmountField.setText(currencyFormat.format(invoice.getTaxAmount()));
        discountField.setText(String.format("%.2f", invoice.getDiscount()));
        totalField.setText(currencyFormat.format(invoice.getTotal()));

        // Establecer método de pago y estado
        paymentMethodComboBox.setSelectedItem(invoice.getPaymentMethod());
        paymentStatusComboBox.setSelectedItem(invoice.getPaymentStatus());

        // Establecer notas
        notesArea.setText(invoice.getNotes());

        // Habilitar/deshabilitar botones según el estado de la factura
        updateButton.setEnabled(true);
        deleteButton.setEnabled(!invoice.isPaid() && !invoice.isCancelled());
        generatePdfButton.setEnabled(true);
        sendEmailButton.setEnabled(true);
        markAsPaidButton.setEnabled(!invoice.isPaid() && !invoice.isCancelled());
        markAsSentButton.setEnabled(!invoice.isSent() && !invoice.isCancelled());
        markAsCancelledButton.setEnabled(!invoice.isCancelled());

        // Habilitar formulario
        enableForm(true);
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param message El mensaje de error a mostrar
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Error: " + message);
    }

    /**
     * Muestra un mensaje informativo.
     *
     * @param message El mensaje informativo a mostrar
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText(message);
    }

    /**
     * Muestra un mensaje de éxito.
     *
     * @param message El mensaje de éxito a mostrar
     */
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText(message);
    }

    /**
     * Obtiene el nombre del servicio para un ID de servicio dado.
     *
     * @param serviceId El ID del servicio
     * @return El nombre del servicio, o "Desconocido" si no se encuentra
     */
    public String getServiceName(int serviceId) {
        if (services != null) {
            for (Service service : services) {
                if (service.getServiceId() == serviceId) {
                    return service.getDescription();
                }
            }
        }
        return "Desconocido";
    }

    /**
     * Obtiene el nombre del cliente para un ID de cliente dado.
     *
     * @param customerId El ID del cliente
     * @return El nombre del cliente, o "Desconocido" si no se encuentra
     */
    public String getCustomerName(int customerId) {
        if (customers != null) {
            for (Customer customer : customers) {
                if (customer.getCustomerId() == customerId) {
                    return customer.getFullName();
                }
            }
        }
        return "Desconocido";
    }

    /**
     * Obtiene el email del cliente para un ID de cliente dado.
     *
     * @param customerId El ID del cliente
     * @return El email del cliente, o null si no se encuentra
     */
    public String getCustomerEmail(int customerId) {
        if (customers != null) {
            for (Customer customer : customers) {
                if (customer.getCustomerId() == customerId) {
                    return customer.getEmail();
                }
            }
        }
        return null;
    }
}
