package com.carmotorsproject.services.views;

import com.carmotorsproject.services.controller.VehicleController;
import com.carmotorsproject.services.model.Vehicle;
import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.ModernTextField;
import com.carmotorsproject.ui.components.RoundedPanel;
import com.carmotorsproject.ui.components.TransparentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;
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

/**
 * Swing UI for managing vehicles with a modern, responsive design.
 */
public class VehicleView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(VehicleView.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Controller
    private final VehicleController controller;

    // UI Components
    private JTable vehicleTable;
    private DefaultTableModel vehicleTableModel;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private JSplitPane mainSplitPane;

    // Form Components
    private ModernTextField customerIdField;
    private ModernTextField licensePlateField;
    private ModernTextField makeField;
    private ModernTextField modelField;
    private ModernTextField yearField;
    private ModernTextField vinField;
    private ModernTextField colorField;
    private ModernTextField engineTypeField;
    private ModernTextField transmissionField;
    private ModernTextField mileageField;
    private ModernTextField lastServiceDateField;
    private JTextArea notesArea;

    // Buttons
    private ModernButton addButton;
    private ModernButton updateButton;
    private ModernButton deleteButton;
    private ModernButton clearButton;
    private ModernButton searchButton;

    // Selected ID
    private int selectedVehicleId = 0;
    private int vehicleCount = 0;

    /**
     * Constructor that initializes the UI and controller.
     */
    public VehicleView() {
        this.controller = new VehicleController(this);
        initComponents();
        loadData();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        // Set up the frame
        setTitle("Gestión de Vehículos");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppTheme.PRIMARY_WHITE);

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create the split pane for responsive layout
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerLocation(300);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setBorder(null);
        mainSplitPane.setDividerSize(5);
        mainSplitPane.setContinuousLayout(true);
        setDividerColor(mainSplitPane, AppTheme.ACCENT_RED);

        // Create the table panel
        JPanel tablePanel = createTablePanel();
        mainSplitPane.setTopComponent(tablePanel);

        // Create the form panel
        JPanel formPanel = createFormPanel();
        mainSplitPane.setBottomComponent(formPanel);

        mainPanel.add(mainSplitPane, BorderLayout.CENTER);

        // Create status panel
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);
    }

    /**
     * Creates the header panel.
     *
     * @return The header panel
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new TransparentPanel(AppTheme.PRIMARY_BLACK, 0.9f);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setPreferredSize(new Dimension(0, 70));

        // Create title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);

        JLabel iconLabel = new JLabel("🚗");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titlePanel.add(iconLabel);

        JLabel titleLabel = new JLabel("Gestión de Vehículos");
        titleLabel.setFont(AppTheme.TITLE_FONT);
        titleLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titlePanel.add(titleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        // Create counter panel
        JPanel counterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        counterPanel.setOpaque(false);

        JLabel countLabel = new JLabel("Total de vehículos: ");
        countLabel.setFont(AppTheme.REGULAR_FONT);
        countLabel.setForeground(AppTheme.PRIMARY_WHITE);
        counterPanel.add(countLabel);

        JLabel countValueLabel = new JLabel("0");
        countValueLabel.setFont(AppTheme.SUBTITLE_FONT);
        countValueLabel.setForeground(AppTheme.PRIMARY_WHITE);
        counterPanel.add(countValueLabel);

        // Update the count when vehicles are loaded
        new Timer(1000, e -> {
            countValueLabel.setText(String.valueOf(vehicleCount));
        }).start();

        panel.add(counterPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Creates the table panel.
     *
     * @return The table panel
     */
    private JPanel createTablePanel() {
        RoundedPanel panel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(15, 15, 5, 15),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(AppTheme.ACCENT_RED, 1, true),
                        "Lista de Vehículos",
                        0,
                        0,
                        AppTheme.SUBTITLE_FONT,
                        AppTheme.ACCENT_RED
                )
        ));

        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setFont(AppTheme.REGULAR_FONT);
        searchPanel.add(searchLabel);

        makeField = new ModernTextField("25", "Marca");
        searchPanel.add(makeField);

        modelField = new ModernTextField("25", "Modelo");
        searchPanel.add(modelField);

        searchButton = new ModernButton("Buscar");
        searchButton.addActionListener(e -> searchVehicles());
        searchPanel.add(searchButton);

        ModernButton resetButton = new ModernButton("Mostrar Todos");
        resetButton.addActionListener(e -> {
            makeField.setText("");
            modelField.setText("");
            loadData();
        });
        searchPanel.add(resetButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Create the table model
        vehicleTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add columns to the table model
        vehicleTableModel.addColumn("ID");
        vehicleTableModel.addColumn("Cliente");
        vehicleTableModel.addColumn("Matrícula");
        vehicleTableModel.addColumn("Marca");
        vehicleTableModel.addColumn("Modelo");
        vehicleTableModel.addColumn("Año");
        vehicleTableModel.addColumn("VIN");
        vehicleTableModel.addColumn("Color");
        vehicleTableModel.addColumn("Motor");
        vehicleTableModel.addColumn("Transmisión");
        vehicleTableModel.addColumn("Kilometraje");
        vehicleTableModel.addColumn("Último Servicio");

        // Create the table with custom styling
        vehicleTable = new JTable(vehicleTableModel);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehicleTable.setRowHeight(30);
        vehicleTable.setIntercellSpacing(new Dimension(10, 5));
        vehicleTable.setShowGrid(false);
        vehicleTable.setFillsViewportHeight(true);

        // Style the table header
        JTableHeader header = vehicleTable.getTableHeader();
        header.setBackground(AppTheme.PRIMARY_BLACK);
        header.setForeground(AppTheme.PRIMARY_WHITE);
        header.setFont(AppTheme.SUBTITLE_FONT);
        header.setBorder(new MatteBorder(0, 0, 2, 0, AppTheme.ACCENT_RED));
        header.setReorderingAllowed(false);

        // Style the table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < vehicleTable.getColumnCount(); i++) {
            vehicleTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Add zebra striping
        vehicleTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    comp.setBackground(AppTheme.ACCENT_RED);
                    comp.setForeground(AppTheme.PRIMARY_WHITE);
                } else {
                    comp.setBackground(row % 2 == 0 ? AppTheme.PRIMARY_WHITE : new Color(245, 245, 245));
                    comp.setForeground(AppTheme.PRIMARY_BLACK);
                }

                setHorizontalAlignment(JLabel.CENTER);
                setBorder(new EmptyBorder(0, 5, 0, 5));
                return comp;
            }
        });

        // Add a mouse listener to the table
        vehicleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = vehicleTable.getSelectedRow();
                if (row >= 0) {
                    selectedVehicleId = (int) vehicleTableModel.getValueAt(row, 0);
                    controller.loadVehicleDetails(selectedVehicleId);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);

                    // Update status
                    updateStatus("Vehículo seleccionado: ID " + selectedVehicleId, false);
                }
            }
        });

        // Add the table to a scroll pane with custom scrollbars
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppTheme.PRIMARY_WHITE);

        // Customize scrollbars
        scrollPane.getVerticalScrollBar().setUI(createModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(createModernScrollBarUI());

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the form panel.
     *
     * @return The form panel
     */
    private JPanel createFormPanel() {
        RoundedPanel panel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(5, 15, 15, 15),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(AppTheme.ACCENT_RED, 1, true),
                        "Detalles del Vehículo",
                        0,
                        0,
                        AppTheme.SUBTITLE_FONT,
                        AppTheme.ACCENT_RED
                )
        ));

        // Create a panel for the form fields
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.weightx = 1.0;

        // Create a two-column layout
        // Left column
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);

        addFormField(leftPanel, "ID Cliente:", customerIdField = new ModernTextField("10", "ID del cliente"), 0, 0);
        addFormField(leftPanel, "Matrícula:", licensePlateField = new ModernTextField("10", "Ej: ABC-123"), 0, 1);
        addFormField(leftPanel, "Marca:", makeField = new ModernTextField("25", "Ej: Toyota"), 0, 2);
        addFormField(leftPanel, "Modelo:", modelField = new ModernTextField("25", "Ej: Corolla"), 0, 3);
        addFormField(leftPanel, "Año:", yearField = new ModernTextField("5", "Ej: 2023"), 0, 4);
        addFormField(leftPanel, "VIN:", vinField = new ModernTextField("20", "Número de identificación del vehículo"), 0, 5);

        // Right column
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        addFormField(rightPanel, "Color:", colorField = new ModernTextField("10", "Ej: Rojo"), 0, 0);
        addFormField(rightPanel, "Tipo de Motor:", engineTypeField = new ModernTextField("15", "Ej: 2.0L Gasolina"), 0, 1);
        addFormField(rightPanel, "Transmisión:", transmissionField = new ModernTextField("10", "Ej: Automática"), 0, 2);
        addFormField(rightPanel, "Kilometraje:", mileageField = new ModernTextField("10", "Ej: 15000"), 0, 3);
        addFormField(rightPanel, "Último Servicio:", lastServiceDateField = new ModernTextField("10", "yyyy-MM-dd"), 0, 4);

        // Notes area
        JPanel notesPanel = new JPanel(new BorderLayout(5, 5));
        notesPanel.setOpaque(false);

        JLabel notesLabel = new JLabel("Notas:");
        notesLabel.setFont(AppTheme.REGULAR_FONT);
        notesPanel.add(notesLabel, BorderLayout.NORTH);

        notesArea = new JTextArea(6, 20);
        notesArea.setFont(AppTheme.REGULAR_FONT);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        notesScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Customize scrollbars
        notesScrollPane.getVerticalScrollBar().setUI(createModernScrollBarUI());
        notesScrollPane.getHorizontalScrollBar().setUI(createModernScrollBarUI());

        notesPanel.add(notesScrollPane, BorderLayout.CENTER);

        // Add columns to the main form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        fieldsPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        fieldsPanel.add(rightPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        fieldsPanel.add(notesPanel, gbc);

        panel.add(fieldsPanel, BorderLayout.CENTER);

        // Create the button panel
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Helper method to add a form field with label.
     */
    private void addFormField(JPanel panel, String labelText, JComponent field, int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 10);

        JLabel label = new JLabel(labelText);
        label.setFont(AppTheme.REGULAR_FONT);
        panel.add(label, gbc);

        gbc.gridx = x + 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    /**
     * Creates the button panel.
     *
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        addButton = new ModernButton("Agregar");
        addButton.addActionListener(e -> addVehicle());
        panel.add(addButton);

        updateButton = new ModernButton("Actualizar");
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> updateVehicle());
        panel.add(updateButton);

        deleteButton = new ModernButton("Eliminar");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> deleteVehicle());
        panel.add(deleteButton);

        clearButton = new ModernButton("Limpiar");
        clearButton.addActionListener(e -> clearForm());
        panel.add(clearButton);

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
        panel.setBorder(new EmptyBorder(5, 15, 5, 15));
        panel.setPreferredSize(new Dimension(0, 30));

        statusLabel = new JLabel("Listo");
        statusLabel.setFont(AppTheme.SMALL_FONT);
        statusLabel.setForeground(AppTheme.PRIMARY_WHITE);
        panel.add(statusLabel, BorderLayout.WEST);

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(150, 15));
        progressBar.setVisible(false);
        progressBar.setForeground(AppTheme.ACCENT_RED);
        progressBar.setBackground(AppTheme.PRIMARY_WHITE);
        progressBar.setBorderPainted(false);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(progressBar);

        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Updates the status message.
     */
    private void updateStatus(String message, boolean showProgress) {
        statusLabel.setText(message);

        if (showProgress) {
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);

            // Hide progress after 2 seconds
            Timer timer = new Timer(2000, e -> {
                progressBar.setVisible(false);
                progressBar.setIndeterminate(false);
                statusLabel.setText("Listo");
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            progressBar.setVisible(false);
        }
    }

    /**
     * Creates a modern scroll bar UI.
     */
    private BasicScrollBarUI createModernScrollBarUI() {
        return new BasicScrollBarUI() {
            private final int THUMB_SIZE = 8;

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                g.setColor(AppTheme.PRIMARY_WHITE);
                g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fill thumb with color
                g2.setColor(AppTheme.ACCENT_RED);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 5, 5);

                g2.dispose();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                scrollbar.repaint();
            }
        };
    }

    /**
     * Loads data into the table.
     */
    public void loadData() {
        updateStatus("Cargando vehículos...", true);
        controller.loadVehicles();
    }

    /**
     * Adds a new vehicle.
     */
    private void addVehicle() {
        try {
            updateStatus("Agregando vehículo...", true);

            // Create a new Vehicle object from form data
            Vehicle vehicle = getVehicleFromForm();

            // Call the controller to add the vehicle
            controller.addVehicle(vehicle);

            // Clear the form
            clearForm();

            updateStatus("Vehículo agregado correctamente", false);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error adding vehicle", ex);
            JOptionPane.showMessageDialog(this, "Error al agregar vehículo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al agregar vehículo", false);
        }
    }

    /**
     * Updates an existing vehicle.
     */
    private void updateVehicle() {
        try {
            updateStatus("Actualizando vehículo...", true);

            // Create a Vehicle object from form data
            Vehicle vehicle = getVehicleFromForm();
            vehicle.setVehicleId(selectedVehicleId);

            // Call the controller to update the vehicle
            controller.updateVehicle(vehicle);

            updateStatus("Vehículo actualizado correctamente", false);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating vehicle", ex);
            JOptionPane.showMessageDialog(this, "Error al actualizar vehículo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al actualizar vehículo", false);
        }
    }

    /**
     * Deletes a vehicle.
     */
    private void deleteVehicle() {
        try {
            // Confirm deletion with a custom dialog
            int option = JOptionPane.showOptionDialog(
                    this,
                    "¿Está seguro que desea eliminar este vehículo?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    new Object[]{"Eliminar", "Cancelar"},
                    "Cancelar"
            );

            if (option == JOptionPane.YES_OPTION) {
                updateStatus("Eliminando vehículo...", true);

                // Call the controller to delete the vehicle
                controller.deleteVehicle(selectedVehicleId);

                // Clear the form
                clearForm();

                updateStatus("Vehículo eliminado correctamente", false);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error deleting vehicle", ex);
            JOptionPane.showMessageDialog(this, "Error al eliminar vehículo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al eliminar vehículo", false);
        }
    }

    /**
     * Searches for vehicles based on make and model.
     */
    private void searchVehicles() {
        try {
            String make = makeField.getText().trim();
            String model = modelField.getText().trim();

            updateStatus("Buscando vehículos...", true);

            if (make.isEmpty() && model.isEmpty()) {
                // If both fields are empty, load all vehicles
                controller.loadVehicles();
            } else {
                // Search by make and model
                controller.searchVehiclesByMakeAndModel(make, model);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error searching vehicles", ex);
            JOptionPane.showMessageDialog(this, "Error al buscar vehículos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al buscar vehículos", false);
        }
    }

    /**
     * Clears the form.
     */
    private void clearForm() {
        // Reset form fields
        customerIdField.setText("");
        licensePlateField.setText("");
        makeField.setText("");
        modelField.setText("");
        yearField.setText("");
        vinField.setText("");
        colorField.setText("");
        engineTypeField.setText("");
        transmissionField.setText("");
        mileageField.setText("");
        lastServiceDateField.setText("");
        notesArea.setText("");

        // Reset selected ID
        selectedVehicleId = 0;

        // Disable buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        updateStatus("Formulario limpiado", false);
    }

    /**
     * Creates a Vehicle object from the form data.
     *
     * @return A Vehicle object populated with form data
     * @throws ParseException If a date parsing error occurs
     */
    private Vehicle getVehicleFromForm() throws ParseException {
        Vehicle vehicle = new Vehicle();

        // Get the customer ID
        String customerIdStr = customerIdField.getText().trim();
        if (!customerIdStr.isEmpty()) {
            try {
                vehicle.setCustomerId(Integer.parseInt(customerIdStr));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Por favor ingrese un ID de cliente válido.");
            }
        } else {
            throw new IllegalArgumentException("Por favor ingrese un ID de cliente.");
        }

        // Get the license plate
        String licensePlate = licensePlateField.getText().trim();
        if (!licensePlate.isEmpty()) {
            vehicle.setLicensePlate(licensePlate);
        } else {
            throw new IllegalArgumentException("Por favor ingrese una matrícula.");
        }

        // Get the make
        String make = makeField.getText().trim();
        if (!make.isEmpty()) {
            vehicle.setMake(make);
        } else {
            throw new IllegalArgumentException("Por favor ingrese la marca del vehículo.");
        }

        // Get the model
        String model = modelField.getText().trim();
        if (!model.isEmpty()) {
            vehicle.setModel(model);
        } else {
            throw new IllegalArgumentException("Por favor ingrese el modelo del vehículo.");
        }

        // Get the year
        String yearStr = yearField.getText().trim();
        if (!yearStr.isEmpty()) {
            try {
                int year = Integer.parseInt(yearStr);
                if (year < 1900 || year > 2100) {
                    throw new IllegalArgumentException("Por favor ingrese un año válido (1900-2100).");
                }
                vehicle.setYear(year);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Por favor ingrese un año válido.");
            }
        } else {
            throw new IllegalArgumentException("Por favor ingrese el año del vehículo.");
        }

        // Get the VIN
        vehicle.setVin(vinField.getText().trim());

        // Get the color
        vehicle.setColor(colorField.getText().trim());

        // Get the engine type
        vehicle.setEngineType(engineTypeField.getText().trim());

        // Get the transmission
        vehicle.setTransmission(transmissionField.getText().trim());

        // Get the mileage
        String mileageStr = mileageField.getText().trim();
        if (!mileageStr.isEmpty()) {
            try {
                vehicle.setCurrentMileage(Integer.parseInt(mileageStr));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Por favor ingrese un kilometraje válido.");
            }
        } else {
            throw new IllegalArgumentException("Por favor ingrese el kilometraje actual.");
        }

        // Get the last service date
        String lastServiceDateStr = lastServiceDateField.getText().trim();
        if (!lastServiceDateStr.isEmpty()) {
            try {
                vehicle.setLastServiceDate(DATE_FORMAT.parse(lastServiceDateStr));
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Por favor ingrese una fecha válida en formato yyyy-MM-dd.");
            }
        }

        // Get the notes
        vehicle.setNotes(notesArea.getText().trim());

        return vehicle;
    }

    /**
     * Updates the vehicle table with the provided vehicles.
     *
     * @param vehicles The list of vehicles to display
     */
    public void updateVehicleTable(List<Vehicle> vehicles) {
        // Clear the table
        vehicleTableModel.setRowCount(0);

        // Update vehicle count
        vehicleCount = vehicles.size();

        // Add vehicles to the table
        for (Vehicle vehicle : vehicles) {
            Vector<Object> row = new Vector<>();
            row.add(vehicle.getVehicleId());
            row.add(vehicle.getCustomerId());
            row.add(vehicle.getLicensePlate());
            row.add(vehicle.getMake());
            row.add(vehicle.getModel());
            row.add(vehicle.getYear());
            row.add(vehicle.getVin());
            row.add(vehicle.getColor());
            row.add(vehicle.getEngineType());
            row.add(vehicle.getTransmission());
            row.add(vehicle.getCurrentMileage());
            row.add(vehicle.getLastServiceDate() != null ? DATE_FORMAT.format(vehicle.getLastServiceDate()) : "");

            vehicleTableModel.addRow(row);
        }

        updateStatus("Se encontraron " + vehicles.size() + " vehículos", false);
    }

    /**
     * Populates the form with the provided vehicle.
     *
     * @param vehicle The vehicle to display
     */
    public void populateVehicleForm(Vehicle vehicle) {
        customerIdField.setText(String.valueOf(vehicle.getCustomerId()));
        licensePlateField.setText(vehicle.getLicensePlate());
        makeField.setText(vehicle.getMake());
        modelField.setText(vehicle.getModel());
        yearField.setText(String.valueOf(vehicle.getYear()));
        vinField.setText(vehicle.getVin());
        colorField.setText(vehicle.getColor());
        engineTypeField.setText(vehicle.getEngineType());
        transmissionField.setText(vehicle.getTransmission());
        mileageField.setText(String.valueOf(vehicle.getCurrentMileage()));

        if (vehicle.getLastServiceDate() != null) {
            lastServiceDateField.setText(DATE_FORMAT.format(vehicle.getLastServiceDate()));
        } else {
            lastServiceDateField.setText("");
        }

        notesArea.setText(vehicle.getNotes());

        updateStatus("Vehículo cargado: " + vehicle.getMake() + " " + vehicle.getModel(), false);
    }

    /**
     * Helper method to set the divider color of a JSplitPane.
     */
    private static void setDividerColor(JSplitPane splitPane, Color color) {
        splitPane.setBorder(null);
        splitPane.setUI(new BasicSplitPaneUI() {
            public javax.swing.plaf.basic.BasicSplitPaneDivider createDefaultDivider() {
                return new javax.swing.plaf.basic.BasicSplitPaneDivider(this) {
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
