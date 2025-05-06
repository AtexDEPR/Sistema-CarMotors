package com.carmotorsproject.services.views;

import com.carmotorsproject.services.controller.ServiceController;
import com.carmotorsproject.services.model.*;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.ModernTextField;
import com.carmotorsproject.ui.components.RoundedPanel;
import com.carmotorsproject.ui.components.TransparentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.ScrollBarUI;

/**
 * Swing UI for managing services with improved responsive design.
 */
public class ServiceView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(ServiceView.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Controller
    private final ServiceController controller;

    // UI Components
    private JTable serviceTable;
    private DefaultTableModel serviceTableModel;
    private JTable partsTable;
    private DefaultTableModel partsTableModel;

    // Service Form Components
    private JComboBox<Vehicle> vehicleComboBox;
    private JComboBox<Technician> technicianComboBox;
    private JComboBox<MaintenanceType> maintenanceTypeComboBox;
    private JComboBox<ServiceStatus> statusComboBox;
    private ModernTextField startDateField;
    private ModernTextField endDateField;
    private ModernTextField descriptionField;
    private JTextArea diagnosisArea;
    private ModernTextField laborCostField;
    private ModernTextField mileageField;
    private JTextArea notesArea;

    // Part Form Components
    private JComboBox<Part> partComboBox;
    private ModernTextField quantityField;
    private ModernTextField unitPriceField;

    // Buttons
    private ModernButton addServiceButton;
    private ModernButton updateServiceButton;
    private ModernButton deleteServiceButton;
    private ModernButton clearServiceFormButton;
    private ModernButton addPartButton;
    private ModernButton removePartButton;
    private ModernButton generateReportButton;
    private ModernButton createDeliveryOrderButton;

    // Status components
    private JLabel statusLabel;
    private JProgressBar progressBar;

    // Selected IDs
    private int selectedServiceId = 0;
    private int selectedPartInServiceId = 0;

    /**
     * Constructor that initializes the UI and controller.
     */
    public ServiceView() {
        this.controller = new ServiceController(this);
        initComponents();
        loadData();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        // Set up the frame
        setTitle("Gestión de Servicios");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set the application icon
        setIconImage(createLogoImage());

        // Create the main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(AppTheme.PRIMARY_WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create the content panel with JSplitPane for responsiveness
        JSplitPane contentSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        contentSplitPane.setDividerLocation(250);
        contentSplitPane.setResizeWeight(0.3); // Give 30% weight to top component
        contentSplitPane.setBorder(null);
        contentSplitPane.setDividerSize(5);
        setDividerColor(contentSplitPane, AppTheme.PRIMARY_RED);

        // Create the service table panel
        JPanel serviceTablePanel = createServiceTablePanel();
        contentSplitPane.setTopComponent(serviceTablePanel);

        // Create the bottom panel with form and parts
        JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        bottomSplitPane.setDividerLocation(600);
        bottomSplitPane.setResizeWeight(0.5); // Equal weight
        bottomSplitPane.setBorder(null);
        bottomSplitPane.setDividerSize(5);
        setDividerColor(bottomSplitPane, AppTheme.PRIMARY_RED);

        // Create the service form panel
        JPanel serviceFormPanel = createServiceFormPanel();
        bottomSplitPane.setLeftComponent(serviceFormPanel);

        // Create the parts panel
        JPanel partsPanel = createPartsPanel();
        bottomSplitPane.setRightComponent(partsPanel);

        contentSplitPane.setBottomComponent(bottomSplitPane);
        mainPanel.add(contentSplitPane, BorderLayout.CENTER);

        // Create the status panel
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);

        // Initialize combo boxes
        initComboBoxes();

        // Set custom UI properties
        customizeUIComponents();
    }

    /**
     * Creates a logo image for the frame.
     *
     * @return The logo image
     */
    private Image createLogoImage() {
        // Create a simple logo (red circle with wrench icon)
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw red circle background
        g2d.setColor(AppTheme.PRIMARY_RED);
        g2d.fillOval(0, 0, 32, 32);

        // Draw wrench icon (simplified)
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(10, 10, 22, 22);
        g2d.drawOval(8, 8, 6, 6);
        g2d.drawOval(18, 18, 6, 6);

        g2d.dispose();
        return image;
    }

    /**
     * Creates the header panel.
     *
     * @return The header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new TransparentPanel(AppTheme.PRIMARY_BLACK, 0.9f);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(0, 70));

        // Create logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setOpaque(false);

        // Add logo/icon
        JLabel logoLabel = new JLabel("🔧");
        logoLabel.setFont(new Font("Dialog", Font.BOLD, 28));
        logoLabel.setForeground(AppTheme.PRIMARY_RED);
        logoPanel.add(logoLabel);

        // Add title
        JLabel titleLabel = new JLabel("Gestión de Servicios");
        titleLabel.setFont(AppTheme.TITLE_FONT);
        titleLabel.setForeground(AppTheme.PRIMARY_WHITE);
        logoPanel.add(titleLabel);

        headerPanel.add(logoPanel, BorderLayout.WEST);

        // Create action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        generateReportButton = new ModernButton("Generar Informe 📊");
        generateReportButton.setColors(AppTheme.PRIMARY_RED, AppTheme.SECONDARY_RED,
                AppTheme.SECONDARY_RED.darker(), AppTheme.PRIMARY_WHITE);
        generateReportButton.addActionListener(this::generateReport);
        actionPanel.add(generateReportButton);

        createDeliveryOrderButton = new ModernButton("Crear Orden de Entrega 📋");
        createDeliveryOrderButton.setColors(AppTheme.PRIMARY_BLACK, AppTheme.SECONDARY_BLACK,
                AppTheme.SECONDARY_BLACK.darker(), AppTheme.PRIMARY_WHITE);
        createDeliveryOrderButton.setEnabled(false);
        createDeliveryOrderButton.addActionListener(this::createDeliveryOrder);
        actionPanel.add(createDeliveryOrderButton);

        headerPanel.add(actionPanel, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Creates the service table panel.
     *
     * @return The service table panel
     */
    private JPanel createServiceTablePanel() {
        RoundedPanel panel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15);
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBackground(AppTheme.PRIMARY_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(AppTheme.PRIMARY_RED, 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        // Create title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel tableTitle = new JLabel("Lista de Servicios");
        tableTitle.setFont(AppTheme.SUBTITLE_FONT);
        tableTitle.setForeground(AppTheme.PRIMARY_BLACK);
        titlePanel.add(tableTitle, BorderLayout.WEST);

        // Add search field (placeholder for future functionality)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        ModernTextField searchField = new ModernTextField();
        searchField.setPlaceholder("Buscar servicio...");
        searchField.setColumns(20);
        searchPanel.add(searchField);

        ModernButton searchButton = new ModernButton("Buscar 🔍");
        searchButton.setColors(AppTheme.PRIMARY_BLACK, AppTheme.SECONDARY_BLACK,
                AppTheme.SECONDARY_BLACK.darker(), AppTheme.PRIMARY_WHITE);
        searchPanel.add(searchButton);

        titlePanel.add(searchPanel, BorderLayout.EAST);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Create the table model
        serviceTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add columns to the table model
        serviceTableModel.addColumn("ID");
        serviceTableModel.addColumn("Vehículo");
        serviceTableModel.addColumn("Técnico");
        serviceTableModel.addColumn("Tipo");
        serviceTableModel.addColumn("Estado");
        serviceTableModel.addColumn("Fecha Inicio");
        serviceTableModel.addColumn("Fecha Fin");
        serviceTableModel.addColumn("Descripción");
        serviceTableModel.addColumn("Costo Mano de Obra");
        serviceTableModel.addColumn("Costo Repuestos");
        serviceTableModel.addColumn("Costo Total");

        // Create the table
        serviceTable = new JTable(serviceTableModel);
        serviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serviceTable.getTableHeader().setReorderingAllowed(false);
        serviceTable.setRowHeight(35);
        serviceTable.setFont(AppTheme.REGULAR_FONT);
        serviceTable.setGridColor(AppTheme.ACCENT_LIGHT);
        serviceTable.setShowVerticalLines(true);
        serviceTable.setShowHorizontalLines(true);

        // Customize table header
        JTableHeader header = serviceTable.getTableHeader();
        header.setBackground(AppTheme.PRIMARY_BLACK);
        header.setForeground(AppTheme.PRIMARY_WHITE);
        header.setFont(AppTheme.HEADER_FONT);
        header.setBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY_RED, 1));
        header.setPreferredSize(new Dimension(0, 35));

        // Customize cell renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        serviceTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        serviceTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Tipo
        serviceTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Estado

        // Custom renderer for status column
        serviceTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    ServiceStatus status = (ServiceStatus) value;
                    if (status == ServiceStatus.PENDING) {
                        c.setForeground(new Color(255, 140, 0)); // Orange
                    } else if (status == ServiceStatus.IN_PROGRESS) {
                        c.setForeground(new Color(0, 120, 215)); // Blue
                    } else if (status == ServiceStatus.COMPLETED) {
                        c.setForeground(new Color(0, 150, 50)); // Green
                    } else if (status == ServiceStatus.CANCELLED) {
                        c.setForeground(new Color(200, 0, 0)); // Red
                    } else {
                        c.setForeground(AppTheme.PRIMARY_BLACK);
                    }
                }

                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        // Add a mouse listener to the table
        serviceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = serviceTable.getSelectedRow();
                if (row >= 0) {
                    selectedServiceId = (int) serviceTableModel.getValueAt(row, 0);
                    controller.loadServiceDetails(selectedServiceId);
                    controller.loadPartsForService(selectedServiceId);
                    updateServiceButton.setEnabled(true);
                    deleteServiceButton.setEnabled(true);
                    addPartButton.setEnabled(true);
                    createDeliveryOrderButton.setEnabled(true);

                    // Update status
                    statusLabel.setText("Servicio #" + selectedServiceId + " seleccionado");
                }
            }
        });

        // Add the table to a scroll pane with custom UI
        JScrollPane scrollPane = new JScrollPane(serviceTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.ACCENT_LIGHT));
        customizeScrollBar(scrollPane.getVerticalScrollBar());
        customizeScrollBar(scrollPane.getHorizontalScrollBar());

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the service form panel.
     *
     * @return The service form panel
     */
    private JPanel createServiceFormPanel() {
        RoundedPanel panel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBackground(AppTheme.PRIMARY_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(AppTheme.PRIMARY_RED, 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        // Create title panel
        JLabel formTitle = new JLabel("Detalles del Servicio");
        formTitle.setFont(AppTheme.SUBTITLE_FONT);
        formTitle.setForeground(AppTheme.PRIMARY_BLACK);
        formTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(formTitle, BorderLayout.NORTH);

        // Create form fields panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Vehicle
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel vehicleLabel = new JLabel("Vehículo: 🚗");
        vehicleLabel.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(vehicleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        vehicleComboBox = new JComboBox<>();
        vehicleComboBox.setFont(AppTheme.REGULAR_FONT);
        vehicleComboBox.setBackground(AppTheme.PRIMARY_WHITE);
        formPanel.add(vehicleComboBox, gbc);
        gbc.gridwidth = 1;

        // Technician
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel technicianLabel = new JLabel("Técnico: 👨‍🔧");
        technicianLabel.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(technicianLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        technicianComboBox = new JComboBox<>();
        technicianComboBox.setFont(AppTheme.REGULAR_FONT);
        technicianComboBox.setBackground(AppTheme.PRIMARY_WHITE);
        formPanel.add(technicianComboBox, gbc);
        gbc.gridwidth = 1;

        // Maintenance Type
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel typeLabel = new JLabel("Tipo: 🔧");
        typeLabel.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        maintenanceTypeComboBox = new JComboBox<>(MaintenanceType.values());
        maintenanceTypeComboBox.setFont(AppTheme.REGULAR_FONT);
        maintenanceTypeComboBox.setBackground(AppTheme.PRIMARY_WHITE);
        formPanel.add(maintenanceTypeComboBox, gbc);

        // Status
        gbc.gridx = 2;
        JLabel statusLabel = new JLabel("Estado: 🚦");
        statusLabel.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(statusLabel, gbc);

        gbc.gridx = 3;
        statusComboBox = new JComboBox<>(ServiceStatus.values());
        statusComboBox.setFont(AppTheme.REGULAR_FONT);
        statusComboBox.setBackground(AppTheme.PRIMARY_WHITE);
        formPanel.add(statusComboBox, gbc);

        // Start Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel startDateLabel = new JLabel("Fecha Inicio: 📅");
        startDateLabel.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(startDateLabel, gbc);

        gbc.gridx = 1;
        startDateField = new ModernTextField();
        startDateField.setPlaceholder("AAAA-MM-DD");
        startDateField.setText(DATE_FORMAT.format(new Date())); // Default to current date
        formPanel.add(startDateField, gbc);

        // End Date
        gbc.gridx = 2;
        JLabel endDateLabel = new JLabel("Fecha Fin: 📅");
        endDateLabel.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(endDateLabel, gbc);

        gbc.gridx = 3;
        endDateField = new ModernTextField();
        endDateField.setPlaceholder("AAAA-MM-DD");
        formPanel.add(endDateField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel descriptionLabel = new JLabel("Descripción: 📝");
        descriptionLabel.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        descriptionField = new ModernTextField();
        descriptionField.setPlaceholder("Descripción breve del servicio");
        formPanel.add(descriptionField, gbc);
        gbc.gridwidth = 1;

        // Diagnosis
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel diagnosisLabel = new JLabel("Diagnóstico: 🔍");
        diagnosisLabel.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(diagnosisLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        diagnosisArea = new JTextArea(3, 20);
        diagnosisArea.setFont(AppTheme.REGULAR_FONT);
        diagnosisArea.setLineWrap(true);
        diagnosisArea.setWrapStyleWord(true);
        diagnosisArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JScrollPane diagnosisScrollPane = new JScrollPane(diagnosisArea);
        diagnosisScrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY));
        formPanel.add(diagnosisScrollPane, gbc);
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        // Labor Cost
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel laborCostLabel = new JLabel("Costo Mano de Obra: 💰");
        laborCostLabel.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(laborCostLabel, gbc);

        gbc.gridx = 1;
        laborCostField = new ModernTextField();
        laborCostField.setPlaceholder("0.00");
        formPanel.add(laborCostField, gbc);

        // Mileage
        gbc.gridx = 2;
        JLabel mileageLabel = new JLabel("Kilometraje: 🛣️");
        mileageLabel.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(mileageLabel, gbc);

        gbc.gridx = 3;
        mileageField = new ModernTextField();
        mileageField.setPlaceholder("Kilometraje actual");
        formPanel.add(mileageField, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 8;
        JLabel notesLabel = new JLabel("Notas: 📋");
        notesLabel.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(notesLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        notesArea = new JTextArea(3, 20);
        notesArea.setFont(AppTheme.REGULAR_FONT);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        notesScrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY));
        formPanel.add(notesScrollPane, gbc);
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        // Add form panel to a scroll pane
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(null);
        customizeScrollBar(formScrollPane.getVerticalScrollBar());
        customizeScrollBar(formScrollPane.getHorizontalScrollBar());
        panel.add(formScrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setOpaque(false);

        addServiceButton = new ModernButton("Agregar Servicio ➕");
        addServiceButton.setColors(AppTheme.PRIMARY_RED, AppTheme.SECONDARY_RED,
                AppTheme.SECONDARY_RED.darker(), AppTheme.PRIMARY_WHITE);
        addServiceButton.addActionListener(this::addService);
        buttonsPanel.add(addServiceButton);

        updateServiceButton = new ModernButton("Actualizar Servicio 🔄");
        updateServiceButton.setColors(AppTheme.PRIMARY_BLACK, AppTheme.SECONDARY_BLACK,
                AppTheme.SECONDARY_BLACK.darker(), AppTheme.PRIMARY_WHITE);
        updateServiceButton.setEnabled(false);
        updateServiceButton.addActionListener(this::updateService);
        buttonsPanel.add(updateServiceButton);

        deleteServiceButton = new ModernButton("Eliminar Servicio ❌");
        deleteServiceButton.setColors(AppTheme.ERROR_COLOR, AppTheme.ERROR_COLOR.darker(),
                AppTheme.ERROR_COLOR.darker().darker(), AppTheme.PRIMARY_WHITE);
        deleteServiceButton.setEnabled(false);
        deleteServiceButton.addActionListener(this::deleteService);
        buttonsPanel.add(deleteServiceButton);

        clearServiceFormButton = new ModernButton("Limpiar Formulario 🧹");
        clearServiceFormButton.setColors(AppTheme.ACCENT_GRAY, AppTheme.ACCENT_GRAY.darker(),
                AppTheme.ACCENT_GRAY.darker().darker(), AppTheme.PRIMARY_WHITE);
        clearServiceFormButton.addActionListener(this::clearServiceForm);
        buttonsPanel.add(clearServiceFormButton);

        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the parts panel.
     *
     * @return The parts panel
     */
    private JPanel createPartsPanel() {
        RoundedPanel panel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBackground(AppTheme.PRIMARY_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(AppTheme.PRIMARY_RED, 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        // Create title panel
        JLabel partsTitle = new JLabel("Repuestos del Servicio");
        partsTitle.setFont(AppTheme.SUBTITLE_FONT);
        partsTitle.setForeground(AppTheme.PRIMARY_BLACK);
        partsTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(partsTitle, BorderLayout.NORTH);

        // Create the parts table
        JPanel partsTablePanel = new JPanel(new BorderLayout(5, 5));
        partsTablePanel.setOpaque(false);

        // Create the table model
        partsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add columns to the table model
        partsTableModel.addColumn("ID");
        partsTableModel.addColumn("Repuesto");
        partsTableModel.addColumn("Cantidad");
        partsTableModel.addColumn("Precio Unitario");
        partsTableModel.addColumn("Precio Total");

        // Create the table
        partsTable = new JTable(partsTableModel);
        partsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        partsTable.getTableHeader().setReorderingAllowed(false);
        partsTable.setRowHeight(30);
        partsTable.setFont(AppTheme.REGULAR_FONT);
        partsTable.setGridColor(AppTheme.ACCENT_LIGHT);

        // Customize table header
        JTableHeader partsHeader = partsTable.getTableHeader();
        partsHeader.setBackground(AppTheme.PRIMARY_BLACK);
        partsHeader.setForeground(AppTheme.PRIMARY_WHITE);
        partsHeader.setFont(AppTheme.HEADER_FONT);
        partsHeader.setBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY_RED, 1));
        partsHeader.setPreferredSize(new Dimension(0, 35));

        // Customize cell renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        partsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        partsTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Cantidad

        // Add a mouse listener to the table
        partsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = partsTable.getSelectedRow();
                if (row >= 0) {
                    selectedPartInServiceId = (int) partsTableModel.getValueAt(row, 0);
                    removePartButton.setEnabled(true);

                    // Update status
                    statusLabel.setText("Repuesto seleccionado para eliminar");
                }
            }
        });

        // Add the table to a scroll pane
        JScrollPane partsScrollPane = new JScrollPane(partsTable);
        partsScrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.ACCENT_LIGHT));
        customizeScrollBar(partsScrollPane.getVerticalScrollBar());
        customizeScrollBar(partsScrollPane.getHorizontalScrollBar());

        partsTablePanel.add(partsScrollPane, BorderLayout.CENTER);
        panel.add(partsTablePanel, BorderLayout.CENTER);

        // Create the parts form panel
        JPanel partsFormPanel = new JPanel(new GridBagLayout());
        partsFormPanel.setOpaque(false);
        partsFormPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Part
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel partLabel = new JLabel("Repuesto: 🔩");
        partLabel.setFont(AppTheme.REGULAR_FONT);
        partsFormPanel.add(partLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        partComboBox = new JComboBox<>();
        partComboBox.setFont(AppTheme.REGULAR_FONT);
        partComboBox.setBackground(AppTheme.PRIMARY_WHITE);
        partsFormPanel.add(partComboBox, gbc);
        gbc.gridwidth = 1;

        // Quantity
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel quantityLabel = new JLabel("Cantidad: 🔢");
        quantityLabel.setFont(AppTheme.REGULAR_FONT);
        partsFormPanel.add(quantityLabel, gbc);

        gbc.gridx = 1;
        quantityField = new ModernTextField();
        quantityField.setPlaceholder("Cantidad");
        partsFormPanel.add(quantityField, gbc);

        // Unit Price
        gbc.gridx = 2;
        JLabel priceLabel = new JLabel("Precio: 💲");
        priceLabel.setFont(AppTheme.REGULAR_FONT);
        partsFormPanel.add(priceLabel, gbc);

        gbc.gridx = 3;
        unitPriceField = new ModernTextField();
        unitPriceField.setPlaceholder("Precio unitario");
        partsFormPanel.add(unitPriceField, gbc);

        // Add and Remove Part buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel partButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        partButtonPanel.setOpaque(false);

        addPartButton = new ModernButton("Agregar Repuesto ➕");
        addPartButton.setColors(AppTheme.PRIMARY_RED, AppTheme.SECONDARY_RED,
                AppTheme.SECONDARY_RED.darker(), AppTheme.PRIMARY_WHITE);
        addPartButton.setEnabled(false);
        addPartButton.addActionListener(this::addPart);
        partButtonPanel.add(addPartButton);

        removePartButton = new ModernButton("Eliminar Repuesto ❌");
        removePartButton.setColors(AppTheme.ERROR_COLOR, AppTheme.ERROR_COLOR.darker(),
                AppTheme.ERROR_COLOR.darker().darker(), AppTheme.PRIMARY_WHITE);
        removePartButton.setEnabled(false);
        removePartButton.addActionListener(this::removePart);
        partButtonPanel.add(removePartButton);

        partsFormPanel.add(partButtonPanel, gbc);
        panel.add(partsFormPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the status panel.
     *
     * @return The status panel
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.PRIMARY_BLACK);
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        panel.setPreferredSize(new Dimension(0, 30));

        statusLabel = new JLabel("Listo");
        statusLabel.setFont(AppTheme.REGULAR_FONT);
        statusLabel.setForeground(AppTheme.PRIMARY_WHITE);
        panel.add(statusLabel, BorderLayout.WEST);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(false);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(150, 20));
        panel.add(progressBar, BorderLayout.EAST);

        return panel;
    }

    /**
     * Customizes UI components for a modern look.
     */
    private void customizeUIComponents() {
        // Set custom UI for JComboBox
        for (JComboBox<?> comboBox : new JComboBox<?>[] {
                vehicleComboBox, technicianComboBox, maintenanceTypeComboBox,
                statusComboBox, partComboBox}) {
            comboBox.setRenderer(new ModernComboBoxRenderer());
        }

        // Set custom UI for JTextArea
        for (JTextArea textArea : new JTextArea[] {diagnosisArea, notesArea}) {
            textArea.setBackground(AppTheme.PRIMARY_WHITE);
            textArea.setForeground(AppTheme.PRIMARY_BLACK);
        }
    }

    /**
     * Custom renderer for combo boxes.
     */
    private class ModernComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setBorder(new EmptyBorder(5, 5, 5, 5));

                if (isSelected) {
                    label.setBackground(AppTheme.PRIMARY_RED);
                    label.setForeground(AppTheme.PRIMARY_WHITE);
                } else {
                    label.setBackground(AppTheme.PRIMARY_WHITE);
                    label.setForeground(AppTheme.PRIMARY_BLACK);
                }
            }

            return c;
        }
    }

    /**
     * Personaliza la apariencia de un JScrollBar.
     *
     * @param scrollBar El scrollbar a personalizar
     */
    private void customizeScrollBar(JScrollBar scrollBar) {
        scrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = AppTheme.PRIMARY_RED;
                this.trackColor = AppTheme.ACCENT_LIGHT;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y,
                        thumbBounds.width, thumbBounds.height, 10, 10);

                g2.dispose();
            }
        });
    }

    /**
     * Initializes the combo boxes with data.
     */
    private void initComboBoxes() {
        controller.loadVehicles();
        controller.loadTechnicians();
        controller.loadParts();
    }

    /**
     * Loads data into the tables.
     */
    public void loadData() {
        controller.loadServices();

        // Update status
        statusLabel.setText("Datos cargados correctamente");
    }

    /**
     * Adds a new service.
     */
    private void addService(ActionEvent evt) {
        try {
            // Show progress
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            statusLabel.setText("Agregando servicio...");

            // Create a new Service object from form data
            Service service = getServiceFromForm();

            // Call the controller to add the service
            controller.addService(service);

            // Clear the form
            clearServiceForm(null);

            // Update status
            statusLabel.setText("Servicio agregado correctamente");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error adding service", ex);
            JOptionPane.showMessageDialog(this, "Error al agregar servicio: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error al agregar servicio");
        } finally {
            progressBar.setVisible(false);
        }
    }

    /**
     * Updates an existing service.
     */
    private void updateService(ActionEvent evt) {
        try {
            // Show progress
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            statusLabel.setText("Actualizando servicio...");

            // Create a Service object from form data
            Service service = getServiceFromForm();
            service.setServiceId(selectedServiceId);

            // Call the controller to update the service
            controller.updateService(service);

            // Update status
            statusLabel.setText("Servicio actualizado correctamente");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating service", ex);
            JOptionPane.showMessageDialog(this, "Error al actualizar servicio: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error al actualizar servicio");
        } finally {
            progressBar.setVisible(false);
        }
    }

    /**
     * Deletes a service.
     */
    private void deleteService(ActionEvent evt) {
        try {
            // Confirm deletion
            int option = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar este servicio?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                // Show progress
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                statusLabel.setText("Eliminando servicio...");

                // Call the controller to delete the service
                controller.deleteService(selectedServiceId);

                // Clear the form
                clearServiceForm(null);

                // Update status
                statusLabel.setText("Servicio eliminado correctamente");
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error deleting service", ex);
            JOptionPane.showMessageDialog(this, "Error al eliminar servicio: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error al eliminar servicio");
        } finally {
            progressBar.setVisible(false);
        }
    }

    /**
     * Adds a part to the service.
     */
    private void addPart(ActionEvent evt) {
        try {
            // Show progress
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            statusLabel.setText("Agregando repuesto...");

            // Get the selected part
            Part part = (Part) partComboBox.getSelectedItem();
            if (part == null) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione un repuesto.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the quantity
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText().trim());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese una cantidad válida.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the unit price
            double unitPrice;
            try {
                unitPrice = Double.parseDouble(unitPriceField.getText().trim());
                if (unitPrice <= 0) {
                    JOptionPane.showMessageDialog(this, "El precio unitario debe ser mayor que cero.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un precio unitario válido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create a PartInService object
            PartInService partInService = new PartInService();
            partInService.setServiceId(selectedServiceId);
            partInService.setPartId(part.getId());
            partInService.setQuantity(quantity);
            partInService.setUnitPrice(unitPrice);
            partInService.setTotalPrice(quantity * unitPrice);

            // Call the controller to add the part
            controller.addPartToService(partInService);

            // Clear the part form
            quantityField.setText("");
            unitPriceField.setText("");

            // Update status
            statusLabel.setText("Repuesto agregado correctamente");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error adding part to service", ex);
            JOptionPane.showMessageDialog(this, "Error al agregar repuesto al servicio: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error al agregar repuesto");
        } finally {
            progressBar.setVisible(false);
        }
    }

    /**
     * Removes a part from the service.
     */
    private void removePart(ActionEvent evt) {
        try {
            // Confirm removal
            int option = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar este repuesto?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                // Show progress
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                statusLabel.setText("Eliminando repuesto...");

                // Call the controller to remove the part
                controller.removePartFromService(selectedPartInServiceId);

                // Disable the remove button
                removePartButton.setEnabled(false);

                // Update status
                statusLabel.setText("Repuesto eliminado correctamente");
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error removing part from service", ex);
            JOptionPane.showMessageDialog(this, "Error al eliminar repuesto del servicio: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error al eliminar repuesto");
        } finally {
            progressBar.setVisible(false);
        }
    }

    /**
     * Generates a service report.
     */
    private void generateReport(ActionEvent evt) {
        try {
            // Show progress
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            statusLabel.setText("Generando informe...");

            // Open the service report view
            ServiceReportView reportView = new ServiceReportView();
            reportView.setVisible(true);

            // Update status
            statusLabel.setText("Informe generado correctamente");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error generating report", ex);
            JOptionPane.showMessageDialog(this, "Error al generar informe: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error al generar informe");
        } finally {
            progressBar.setVisible(false);
        }
    }

    /**
     * Creates a delivery order for the selected service.
     */
    private void createDeliveryOrder(ActionEvent evt) {
        try {
            // Show progress
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            statusLabel.setText("Creando orden de entrega...");

            // Check if the service is completed
            Service service = controller.getServiceById(selectedServiceId);
            if (service.getStatus() != ServiceStatus.COMPLETED) {
                JOptionPane.showMessageDialog(this,
                        "El servicio debe estar completado antes de crear una orden de entrega.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if a delivery order already exists
            if (controller.deliveryOrderExistsForService(selectedServiceId)) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe una orden de entrega para este servicio.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Open the delivery order view
            DeliveryOrderView deliveryOrderView = new DeliveryOrderView(selectedServiceId);
            deliveryOrderView.setVisible(true);

            // Update status
            statusLabel.setText("Orden de entrega creada correctamente");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error creating delivery order", ex);
            JOptionPane.showMessageDialog(this, "Error al crear orden de entrega: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error al crear orden de entrega");
        } finally {
            progressBar.setVisible(false);
        }
    }

    /**
     * Clears the service form.
     */
    private void clearServiceForm(ActionEvent evt) {
        // Reset form fields
        vehicleComboBox.setSelectedIndex(-1);
        technicianComboBox.setSelectedIndex(-1);
        maintenanceTypeComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        startDateField.setText(DATE_FORMAT.format(new Date()));
        endDateField.setText("");
        descriptionField.setText("");
        diagnosisArea.setText("");
        laborCostField.setText("");
        mileageField.setText("");
        notesArea.setText("");

        // Clear the parts table
        partsTableModel.setRowCount(0);

        // Reset selected IDs
        selectedServiceId = 0;
        selectedPartInServiceId = 0;

        // Disable buttons
        updateServiceButton.setEnabled(false);
        deleteServiceButton.setEnabled(false);
        addPartButton.setEnabled(false);
        removePartButton.setEnabled(false);
        createDeliveryOrderButton.setEnabled(false);

        // Update status
        statusLabel.setText("Formulario limpiado");
    }

    /**
     * Creates a Service object from the form data.
     *
     * @return A Service object populated with form data
     * @throws ParseException If a date parsing error occurs
     */
    private Service getServiceFromForm() throws ParseException {
        Service service = new Service();

        // Get the selected vehicle
        Vehicle vehicle = (Vehicle) vehicleComboBox.getSelectedItem();
        if (vehicle != null) {
            service.setVehicleId(vehicle.getVehicleId());
        } else {
            throw new IllegalArgumentException("Por favor seleccione un vehículo.");
        }

        // Get the selected technician
        Technician technician = (Technician) technicianComboBox.getSelectedItem();
        if (technician != null) {
            service.setTechnicianId(technician.getTechnicianId());
        } else {
            throw new IllegalArgumentException("Por favor seleccione un técnico.");
        }

        // Get the maintenance type
        MaintenanceType maintenanceType = (MaintenanceType) maintenanceTypeComboBox.getSelectedItem();
        service.setMaintenanceType(maintenanceType);

        // Get the status
        ServiceStatus status = (ServiceStatus) statusComboBox.getSelectedItem();
        service.setStatus(status);

        // Get the start date
        String startDateStr = startDateField.getText().trim();
        if (!startDateStr.isEmpty()) {
            service.setStartDate(DATE_FORMAT.parse(startDateStr));
        } else {
            throw new IllegalArgumentException("Por favor ingrese una fecha de inicio.");
        }

        // Get the end date
        String endDateStr = endDateField.getText().trim();
        if (!endDateStr.isEmpty()) {
            service.setEndDate(DATE_FORMAT.parse(endDateStr));
        }

        // Get the description
        service.setDescription(descriptionField.getText().trim());

        // Get the diagnosis
        service.setDiagnosis(diagnosisArea.getText().trim());

        // Get the labor cost
        String laborCostStr = laborCostField.getText().trim();
        if (!laborCostStr.isEmpty()) {
            try {
                service.setLaborCost(Double.parseDouble(laborCostStr));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Por favor ingrese un costo de mano de obra válido.");
            }
        } else {
            service.setLaborCost(0.0);
        }

        // Get the mileage
        String mileageStr = mileageField.getText().trim();
        if (!mileageStr.isEmpty()) {
            try {
                service.setMileage(Integer.parseInt(mileageStr));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Por favor ingrese un kilometraje válido.");
            }
        } else {
            throw new IllegalArgumentException("Por favor ingrese el kilometraje del vehículo.");
        }

        // Get the notes
        service.setNotes(notesArea.getText().trim());

        return service;
    }

    /**
     * Updates the service table with the provided services.
     *
     * @param services The list of services to display
     */
    public void updateServiceTable(List<Service> services) {
        // Clear the table
        serviceTableModel.setRowCount(0);

        // Add services to the table
        for (Service service : services) {
            Vector<Object> row = new Vector<>();
            row.add(service.getServiceId());
            row.add("ID: " + service.getVehicleId());
            row.add("ID: " + service.getTechnicianId());
            row.add(service.getMaintenanceType());
            row.add(service.getStatus());
            row.add(service.getStartDate() != null ? DATE_FORMAT.format(service.getStartDate()) : "");
            row.add(service.getEndDate() != null ? DATE_FORMAT.format(service.getEndDate()) : "");
            row.add(service.getDescription());
            row.add(service.getLaborCost());
            row.add(service.getPartsCost());
            row.add(service.getTotalCost());

            serviceTableModel.addRow(row);
        }

        // Update status
        statusLabel.setText("Se encontraron " + services.size() + " servicios");
    }

    /**
     * Updates the parts table with the provided parts.
     *
     * @param parts The list of parts to display
     */
    public void updatePartsTable(List<PartInService> parts) {
        // Clear the table
        partsTableModel.setRowCount(0);

        // Add parts to the table
        for (PartInService part : parts) {
            Vector<Object> row = new Vector<>();
            row.add(part.getPartsInServiceId());
            row.add("ID: " + part.getPartId());
            row.add(part.getQuantity());
            row.add(part.getUnitPrice());
            row.add(part.getTotalPrice());

            partsTableModel.addRow(row);
        }

        // Update status if parts were loaded for a service
        if (selectedServiceId > 0) {
            statusLabel.setText("Servicio #" + selectedServiceId + " - " + parts.size() + " repuestos");
        }
    }

    /**
     * Populates the service form with the provided service.
     *
     * @param service The service to display
     */
    public void populateServiceForm(Service service) {
        // Set the vehicle
        for (int i = 0; i < vehicleComboBox.getItemCount(); i++) {
            Vehicle vehicle = vehicleComboBox.getItemAt(i);
            if (vehicle.getVehicleId() == service.getVehicleId()) {
                vehicleComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Set the technician
        for (int i = 0; i < technicianComboBox.getItemCount(); i++) {
            Technician technician = technicianComboBox.getItemAt(i);
            if (technician.getTechnicianId() == service.getTechnicianId()) {
                technicianComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Set the maintenance type
        maintenanceTypeComboBox.setSelectedItem(service.getMaintenanceType());

        // Set the status
        statusComboBox.setSelectedItem(service.getStatus());

        // Set the start date
        if (service.getStartDate() != null) {
            startDateField.setText(DATE_FORMAT.format(service.getStartDate()));
        } else {
            startDateField.setText("");
        }

        // Set the end date
        if (service.getEndDate() != null) {
            endDateField.setText(DATE_FORMAT.format(service.getEndDate()));
        } else {
            endDateField.setText("");
        }

        // Set the description
        descriptionField.setText(service.getDescription());

        // Set the diagnosis
        diagnosisArea.setText(service.getDiagnosis());

        // Set the labor cost
        laborCostField.setText(String.valueOf(service.getLaborCost()));

        // Set the mileage
        mileageField.setText(String.valueOf(service.getMileage()));

        // Set the notes
        notesArea.setText(service.getNotes());
    }

    /**
     * Updates the vehicle combo box with the provided vehicles.
     *
     * @param vehicles The list of vehicles to display
     */
    public void updateVehicleComboBox(List<Vehicle> vehicles) {
        vehicleComboBox.removeAllItems();
        for (Vehicle vehicle : vehicles) {
            vehicleComboBox.addItem(vehicle);
        }
    }

    /**
     * Updates the technician combo box with the provided technicians.
     *
     * @param technicians The list of technicians to display
     */
    public void updateTechnicianComboBox(List<Technician> technicians) {
        technicianComboBox.removeAllItems();
        for (Technician technician : technicians) {
            technicianComboBox.addItem(technician);
        }
    }

    /**
     * Updates the part combo box with the provided parts.
     *
     * @param parts The list of parts to display
     */
    public void updatePartComboBox(List<Part> parts) {
        partComboBox.removeAllItems();
        for (Part part : parts) {
            partComboBox.addItem(part);
        }
    }

    /**
     * Helper method to set the divider color for JSplitPane.
     */
    private static void setDividerColor(JSplitPane splitPane, Color color) {
        splitPane.setBorder(null);
        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(color);
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });
    }
}
