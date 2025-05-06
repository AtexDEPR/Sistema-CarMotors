package com.carmotorsproject.parts.views;

import com.carmotorsproject.parts.controller.PartController;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PartType;
import com.carmotorsproject.parts.model.PartStatus;
import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.ModernTextField;
import com.carmotorsproject.ui.components.RoundedPanel;
import com.carmotorsproject.ui.components.TransparentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Swing UI for managing parts.
 * Provides a user interface for viewing, adding, editing, and deleting parts.
 */
public class PartView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(PartView.class.getName());

    // Controller
    private final PartController controller;

    // UI Components
    private JSplitPane splitPane;
    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;
    private JPanel searchPanel;
    private JPanel tablePanel;
    private JPanel headerPanel;
    private JPanel statusPanel;

    // Form fields
    private ModernTextField txtPartId;
    private ModernTextField txtName;
    private ModernTextField txtDescription;
    private ModernTextField txtPartNumber;
    private JComboBox<PartType> cmbType;
    private JComboBox<PartStatus> cmbStatus;
    private ModernTextField txtPurchasePrice;
    private ModernTextField txtSellingPrice;
    private ModernTextField txtStockQuantity;
    private ModernTextField txtMinStockLevel;
    private ModernTextField txtLocation;
    private ModernTextField txtSupplierId;

    // Search fields
    private ModernTextField txtSearch;
    private JComboBox<String> cmbSearchType;

    // Table
    private JTable tblParts;
    private DefaultTableModel tableModel;

    // Buttons
    private ModernButton btnAdd;
    private ModernButton btnUpdate;
    private ModernButton btnDelete;
    private ModernButton btnClear;
    private ModernButton btnSearch;
    private ModernButton btnShowAll;
    private ModernButton btnLowStock;

    // Status indicators
    private JLabel lblTotalParts;
    private JLabel lblLowStockCount;

    // Colors
    private final Color PRIMARY_BLACK = new Color(20, 20, 20);
    private final Color PRIMARY_RED = new Color(220, 20, 20);
    private final Color PRIMARY_WHITE = new Color(255, 255, 255);
    private final Color SECONDARY_RED = new Color(180, 20, 20);
    private final Color LIGHT_GRAY = new Color(121, 121, 121);
    private final Color MEDIUM_GRAY = new Color(91, 91, 91);
    private final Color DARK_GRAY = new Color(100, 100, 100);
    private final Color TRANSPARENT_BLACK = new Color(0, 0, 0, 80);

    /**
     * Constructor that initializes the UI and controller.
     */
    public PartView() {
        // Initialize controller
        this.controller = new PartController(this);

        // Set up the frame
        setTitle("Gestión de Inventario de Repuestos");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        // Initialize UI components
        initComponents();

        // Add resize listener for responsive design
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustSplitPaneDivider();
            }
        });

        // Load initial data
        loadAllParts();
    }

    /**
     * Adjusts the split pane divider location based on window size
     */
    private void adjustSplitPaneDivider() {
        int width = getWidth();
        int dividerLocation = (int)(width * 0.3); // 30% of window width
        splitPane.setDividerLocation(dividerLocation);
    }

    /**
     * Initializes all UI components.
     */
    private void initComponents() {
        // Main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(PRIMARY_WHITE);
        setContentPane(mainPanel);

        // Create header panel
        createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create split pane for responsive layout
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(5);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerLocation(350);
        splitPane.setBorder(null);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Create panels
        createFormPanel();
        createButtonPanel();
        createSearchPanel();
        createTablePanel();
        createStatusPanel();

        // Add panels to split pane
        JPanel leftPanel = new JPanel(new BorderLayout(0, 0));
        leftPanel.setBackground(PRIMARY_WHITE);
        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 0));
        rightPanel.setBackground(PRIMARY_WHITE);
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(tablePanel, BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        // Add status panel to main panel
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the header panel with title and logo.
     */
    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_BLACK);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("SISTEMA DE GESTIÓN DE REPUESTOS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false);
        JLabel logoLabel = new JLabel("CM");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(PRIMARY_RED);
        logoPanel.add(logoLabel);

        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
    }

    /**
     * Creates the form panel with input fields.
     */
    private void createFormPanel() {
        formPanel = new RoundedPanel(PRIMARY_WHITE, 1.0f, 15);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 5, 10),
                BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 0, 0, 0, MEDIUM_GRAY),
                        new EmptyBorder(15, 15, 15, 15)
                )
        ));

        // Add title to form panel
        JLabel formTitle = new JLabel("DETALLES DEL REPUESTO");
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));
        formTitle.setForeground(PRIMARY_BLACK);
        formTitle.setHorizontalAlignment(SwingConstants.CENTER);
        formTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        formPanel.setLayout(new BorderLayout());
        formPanel.add(formTitle, BorderLayout.NORTH);

        // Create fields panel
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        formPanel.add(fieldsPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Part ID (hidden/disabled)
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblPartId = new JLabel("ID:");
        lblPartId.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblPartId, gbc);

        gbc.gridx = 1;
        txtPartId = new ModernTextField("");
        txtPartId.setEditable(false);
        txtPartId.setBackground(LIGHT_GRAY);
        fieldsPanel.add(txtPartId, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblName = new JLabel("Nombre:");
        lblName.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblName, gbc);

        gbc.gridx = 1;
        txtName = new ModernTextField("");
        txtName.setPlaceholder("Ingrese nombre del repuesto");
        fieldsPanel.add(txtName, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblDescription = new JLabel("Descripción:");
        lblDescription.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblDescription, gbc);

        gbc.gridx = 1;
        txtDescription = new ModernTextField("");
        txtDescription.setPlaceholder("Ingrese descripción");
        fieldsPanel.add(txtDescription, gbc);

        // Part Number
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblPartNumber = new JLabel("Número de Parte:");
        lblPartNumber.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblPartNumber, gbc);

        gbc.gridx = 1;
        txtPartNumber = new ModernTextField("");
        txtPartNumber.setPlaceholder("Ingrese número de parte");
        fieldsPanel.add(txtPartNumber, gbc);

        // Type
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblType = new JLabel("Tipo:");
        lblType.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblType, gbc);

        gbc.gridx = 1;
        cmbType = new JComboBox<>(PartType.values());
        cmbType.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbType.setBackground(PRIMARY_WHITE);
        cmbType.setBorder(BorderFactory.createLineBorder(MEDIUM_GRAY));
        fieldsPanel.add(cmbType, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel lblStatus = new JLabel("Estado:");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblStatus, gbc);

        gbc.gridx = 1;
        cmbStatus = new JComboBox<>(PartStatus.values());
        cmbStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbStatus.setBackground(PRIMARY_WHITE);
        cmbStatus.setBorder(BorderFactory.createLineBorder(MEDIUM_GRAY));
        fieldsPanel.add(cmbStatus, gbc);

        // Purchase Price
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel lblPurchasePrice = new JLabel("Precio Compra:");
        lblPurchasePrice.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblPurchasePrice, gbc);

        gbc.gridx = 1;
        txtPurchasePrice = new ModernTextField("");
        txtPurchasePrice.setPlaceholder("Ingrese precio de compra");
        fieldsPanel.add(txtPurchasePrice, gbc);

        // Selling Price
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel lblSellingPrice = new JLabel("Precio Venta:");
        lblSellingPrice.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblSellingPrice, gbc);

        gbc.gridx = 1;
        txtSellingPrice = new ModernTextField("");
        txtSellingPrice.setPlaceholder("Ingrese precio de venta");
        fieldsPanel.add(txtSellingPrice, gbc);

        // Stock Quantity
        gbc.gridx = 0;
        gbc.gridy = 8;
        JLabel lblStockQuantity = new JLabel("Cantidad Stock:");
        lblStockQuantity.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblStockQuantity, gbc);

        gbc.gridx = 1;
        txtStockQuantity = new ModernTextField("");
        txtStockQuantity.setPlaceholder("Ingrese cantidad en stock");
        fieldsPanel.add(txtStockQuantity, gbc);

        // Min Stock Level
        gbc.gridx = 0;
        gbc.gridy = 9;
        JLabel lblMinStockLevel = new JLabel("Stock Mínimo:");
        lblMinStockLevel.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblMinStockLevel, gbc);

        gbc.gridx = 1;
        txtMinStockLevel = new ModernTextField("");
        txtMinStockLevel.setPlaceholder("Ingrese nivel mínimo de stock");
        fieldsPanel.add(txtMinStockLevel, gbc);

        // Location
        gbc.gridx = 0;
        gbc.gridy = 10;
        JLabel lblLocation = new JLabel("Ubicación:");
        lblLocation.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblLocation, gbc);

        gbc.gridx = 1;
        txtLocation = new ModernTextField("");
        txtLocation.setPlaceholder("Ingrese ubicación en almacén");
        fieldsPanel.add(txtLocation, gbc);

        // Supplier ID
        gbc.gridx = 0;
        gbc.gridy = 11;
        JLabel lblSupplierId = new JLabel("ID Proveedor:");
        lblSupplierId.setFont(new Font("Arial", Font.BOLD, 12));
        fieldsPanel.add(lblSupplierId, gbc);

        gbc.gridx = 1;
        txtSupplierId = new ModernTextField("");
        txtSupplierId.setPlaceholder("Ingrese ID del proveedor");
        fieldsPanel.add(txtSupplierId, gbc);
    }

    /**
     * Creates the button panel with action buttons.
     */
    private void createButtonPanel() {
        buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBackground(PRIMARY_WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 15, 15, 15));

        btnAdd = new ModernButton("Agregar", "success");
        btnAdd.setIcon(createIcon("add", 16, 16));

        btnUpdate = new ModernButton("Actualizar", "primary");
        btnUpdate.setIcon(createIcon("update", 16, 16));

        btnDelete = new ModernButton("Eliminar", "danger");
        btnDelete.setIcon(createIcon("delete", 16, 16));

        btnClear = new ModernButton("Limpiar", "secondary");
        btnClear.setIcon(createIcon("clear", 16, 16));

        // Add action listeners
        btnAdd.addActionListener(e -> controller.addPart());
        btnUpdate.addActionListener(e -> controller.updatePart());
        btnDelete.addActionListener(e -> controller.deletePart());
        btnClear.addActionListener(e -> clearForm());

        // Add buttons to panel
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
    }

    /**
     * Creates the search panel with search controls.
     */
    private void createSearchPanel() {
        searchPanel = new RoundedPanel(PRIMARY_WHITE, 1.0f, 15);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 5, 10),
                BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 0, 1, 0, MEDIUM_GRAY),
                        new EmptyBorder(10, 15, 10, 15)
                )
        ));

        // Create a title for search panel
        JLabel searchTitle = new JLabel("BÚSQUEDA DE REPUESTOS");
        searchTitle.setFont(new Font("Arial", Font.BOLD, 14));
        searchTitle.setForeground(PRIMARY_BLACK);

        // Create search controls panel
        JPanel searchControlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchControlsPanel.setOpaque(false);

        // Search field
        txtSearch = new ModernTextField("");
        txtSearch.setPlaceholder("Buscar repuestos...");
        txtSearch.setPreferredSize(new Dimension(200, 30));

        // Search type combo box
        String[] searchTypes = {"Nombre", "Número de Parte", "Tipo", "ID Proveedor"};
        cmbSearchType = new JComboBox<>(searchTypes);
        cmbSearchType.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbSearchType.setBackground(PRIMARY_WHITE);
        cmbSearchType.setBorder(BorderFactory.createLineBorder(MEDIUM_GRAY));
        cmbSearchType.setPreferredSize(new Dimension(150, 30));

        // Search button
        btnSearch = new ModernButton("Buscar", "primary");
        btnSearch.setIcon(createIcon("search", 16, 16));
        btnSearch.addActionListener(e -> controller.searchParts(
                txtSearch.getText(),
                cmbSearchType.getSelectedItem().toString()));

        // Show all button
        btnShowAll = new ModernButton("Mostrar Todos", "secondary");
        btnShowAll.addActionListener(e -> loadAllParts());

        // Low stock button
        btnLowStock = new ModernButton("Stock Bajo", "warning");
        btnLowStock.setIcon(createIcon("warning", 16, 16));
        btnLowStock.addActionListener(e -> controller.findLowStock());

        // Add components to search controls panel
        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        searchControlsPanel.add(searchLabel);
        searchControlsPanel.add(txtSearch);

        JLabel byLabel = new JLabel("Por:");
        byLabel.setFont(new Font("Arial", Font.BOLD, 12));
        searchControlsPanel.add(byLabel);
        searchControlsPanel.add(cmbSearchType);
        searchControlsPanel.add(btnSearch);
        searchControlsPanel.add(btnShowAll);
        searchControlsPanel.add(btnLowStock);

        // Add components to search panel
        searchPanel.setLayout(new BorderLayout());
        searchPanel.add(searchTitle, BorderLayout.NORTH);
        searchPanel.add(searchControlsPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the table panel with parts table.
     */
    private void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout(0, 0));
        tablePanel.setBackground(PRIMARY_WHITE);
        tablePanel.setBorder(new EmptyBorder(5, 10, 10, 10));

        // Create table model with column names
        String[] columnNames = {
                "ID", "Nombre", "Número Parte", "Tipo", "Estado",
                "Precio Compra", "Precio Venta", "Stock", "Stock Mín", "Ubicación", "ID Prov."
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 7 || columnIndex == 8 || columnIndex == 10) {
                    return Integer.class;
                } else if (columnIndex == 5 || columnIndex == 6) {
                    return Double.class;
                }
                return String.class;
            }
        };

        // Create table with model
        tblParts = new JTable(tableModel);
        tblParts.setFont(new Font("Arial", Font.PLAIN, 12));
        tblParts.setRowHeight(30);
        tblParts.setShowGrid(true);
        tblParts.setGridColor(LIGHT_GRAY);
        tblParts.setSelectionBackground(SECONDARY_RED);
        tblParts.setSelectionForeground(PRIMARY_WHITE);
        tblParts.setAutoCreateRowSorter(true);

        // Style the table header
        JTableHeader header = tblParts.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(PRIMARY_BLACK);
        header.setForeground(DARK_GRAY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_RED));
        header.setReorderingAllowed(false);

        // Set column widths
        tblParts.getColumnModel().getColumn(0).setPreferredWidth(40); // ID
        tblParts.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        tblParts.getColumnModel().getColumn(2).setPreferredWidth(100); // Part Number
        tblParts.getColumnModel().getColumn(3).setPreferredWidth(80); // Type
        tblParts.getColumnModel().getColumn(4).setPreferredWidth(80); // Status
        tblParts.getColumnModel().getColumn(5).setPreferredWidth(90); // Purchase Price
        tblParts.getColumnModel().getColumn(6).setPreferredWidth(90); // Selling Price
        tblParts.getColumnModel().getColumn(7).setPreferredWidth(60); // Stock
        tblParts.getColumnModel().getColumn(8).setPreferredWidth(60); // Min Stock
        tblParts.getColumnModel().getColumn(9).setPreferredWidth(100); // Location
        tblParts.getColumnModel().getColumn(10).setPreferredWidth(60); // Supplier ID

        // Add row selection listener
        tblParts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblParts.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedRow = tblParts.convertRowIndexToModel(selectedRow);
                    populateFormFromTable(selectedRow);
                }
            }
        });

        // Add sorting capability
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tblParts.setRowSorter(sorter);

        // Add table to scroll pane with custom styling
        JScrollPane scrollPane = new JScrollPane(tblParts);
        scrollPane.setBorder(BorderFactory.createLineBorder(MEDIUM_GRAY));
        scrollPane.getViewport().setBackground(PRIMARY_WHITE);

        // Add a title for the table
        JLabel tableTitle = new JLabel("LISTADO DE REPUESTOS");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 14));
        tableTitle.setForeground(PRIMARY_BLACK);
        tableTitle.setBorder(new EmptyBorder(0, 0, 5, 0));

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates the status panel with counters and information.
     */
    private void createStatusPanel() {
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(PRIMARY_BLACK);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 30));
        statusPanel.setBorder(new EmptyBorder(5, 15, 5, 15));

        JPanel leftStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftStatus.setOpaque(false);

        lblTotalParts = new JLabel("Total Repuestos: 0");
        lblTotalParts.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTotalParts.setForeground(PRIMARY_WHITE);

        lblLowStockCount = new JLabel("Repuestos con Stock Bajo: 0");
        lblLowStockCount.setFont(new Font("Arial", Font.PLAIN, 12));
        lblLowStockCount.setForeground(PRIMARY_RED);

        leftStatus.add(lblTotalParts);
        leftStatus.add(lblLowStockCount);

        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setForeground(MEDIUM_GRAY);

        statusPanel.add(leftStatus, BorderLayout.WEST);
        statusPanel.add(versionLabel, BorderLayout.EAST);
    }

    /**
     * Creates an icon for buttons.
     *
     * @param iconType The type of icon to create
     * @param width The width of the icon
     * @param height The height of the icon
     * @return An ImageIcon object
     */
    private ImageIcon createIcon(String iconType, int width, int height) {
        // This is a placeholder. In a real application, you would load actual icons.
        // For now, we'll create colored squares as placeholders
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        switch (iconType) {
            case "add":
                g2d.setColor(new Color(46, 204, 113));
                break;
            case "update":
                g2d.setColor(new Color(52, 152, 219));
                break;
            case "delete":
                g2d.setColor(new Color(231, 76, 60));
                break;
            case "clear":
                g2d.setColor(new Color(149, 165, 166));
                break;
            case "search":
                g2d.setColor(new Color(52, 152, 219));
                break;
            case "warning":
                g2d.setColor(new Color(230, 126, 34));
                break;
            default:
                g2d.setColor(Color.GRAY);
        }

        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        return new ImageIcon(image);
    }

    /**
     * Loads all parts from the database and displays them in the table.
     */
    public void loadAllParts() {
        controller.loadAllParts();
    }

    /**
     * Updates the table with the provided list of parts.
     *
     * @param parts The list of parts to display
     */
    public void updateTable(List<Part> parts) {
        // Clear the table
        tableModel.setRowCount(0);

        // Count low stock items
        int lowStockCount = 0;

        // Add parts to the table
        for (Part part : parts) {
            Object[] row = {
                    part.getId(),
                    part.getName(),
                    part.getPartNumber(),
                    part.getType(),
                    part.getStatus(),
                    part.getPurchasePrice(),
                    part.getSellingPrice(),
                    part.getStockQuantity(),
                    part.getMinStockLevel(),
                    part.getLocation(),
                    part.getSupplierId()
            };
            tableModel.addRow(row);

            // Check if this is a low stock item
            if (part.getStockQuantity() <= part.getMinStockLevel()) {
                lowStockCount++;
            }
        }

        // Update status indicators
        lblTotalParts.setText("Total Repuestos: " + parts.size());
        lblLowStockCount.setText("Repuestos con Stock Bajo: " + lowStockCount);

        LOGGER.log(Level.INFO, "Tabla actualizada con {0} repuestos", parts.size());
    }

    /**
     * Populates the form fields with data from the selected table row.
     *
     * @param row The selected row index
     */
    private void populateFormFromTable(int row) {
        txtPartId.setText(tableModel.getValueAt(row, 0).toString());
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        txtPartNumber.setText(tableModel.getValueAt(row, 2).toString());
        cmbType.setSelectedItem(PartType.valueOf(tableModel.getValueAt(row, 3).toString()));
        cmbStatus.setSelectedItem(PartStatus.valueOf(tableModel.getValueAt(row, 4).toString()));
        txtPurchasePrice.setText(tableModel.getValueAt(row, 5).toString());
        txtSellingPrice.setText(tableModel.getValueAt(row, 6).toString());
        txtStockQuantity.setText(tableModel.getValueAt(row, 7).toString());
        txtMinStockLevel.setText(tableModel.getValueAt(row, 8).toString());
        txtLocation.setText(tableModel.getValueAt(row, 9).toString());
        txtSupplierId.setText(tableModel.getValueAt(row, 10).toString());

        // Get description from controller (not shown in table)
        int partId = Integer.parseInt(txtPartId.getText());
        controller.loadPartDescription(partId);

        // Enable update and delete buttons
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);
    }

    /**
     * Sets the description field value.
     *
     * @param description The description to set
     */
    public void setDescription(String description) {
        txtDescription.setText(description);
    }

    /**
     * Clears all form fields.
     */
    public void clearForm() {
        txtPartId.setText("");
        txtName.setText("");
        txtDescription.setText("");
        txtPartNumber.setText("");
        cmbType.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        txtPurchasePrice.setText("");
        txtSellingPrice.setText("");
        txtStockQuantity.setText("");
        txtMinStockLevel.setText("");
        txtLocation.setText("");
        txtSupplierId.setText("");

        // Clear table selection
        tblParts.clearSelection();

        // Disable update and delete buttons
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    /**
     * Creates a Part object from the form fields.
     *
     * @return A Part object with data from the form
     */
    public Part getPartFromForm() {
        Part part = new Part();

        // Set ID if it exists (for updates)
        if (!txtPartId.getText().isEmpty()) {
            part.setPartId(Integer.parseInt(txtPartId.getText()));
        }

        part.setName(txtName.getText());
        part.setDescription(txtDescription.getText());
        part.setPartNumber(txtPartNumber.getText());
        part.setType((PartType) cmbType.getSelectedItem());
        part.setStatus((PartStatus) cmbStatus.getSelectedItem());

        try {
            part.setPurchasePrice(Double.parseDouble(txtPurchasePrice.getText()));
            part.setSellingPrice(Double.parseDouble(txtSellingPrice.getText()));
            part.setStockQuantity(Integer.parseInt(txtStockQuantity.getText()));
            part.setMinStockLevel(Integer.parseInt(txtMinStockLevel.getText()));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Formato de número inválido en el formulario", e);
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese números válidos para precios y cantidades.",
                    "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        part.setLocation(txtLocation.getText());

        try {
            part.setSupplierId(Integer.parseInt(txtSupplierId.getText()));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Formato de ID de proveedor inválido", e);
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un ID de proveedor válido.",
                    "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return part;
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to display
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Displays a confirmation dialog.
     *
     * @param message The confirmation message to display
     * @return true if the user confirms, false otherwise
     */
    public boolean showConfirm(String message) {
        int result = JOptionPane.showConfirmDialog(this, message,
                "Confirmación", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}
