package com.carmotorsproject.campaigns.views;

import com.carmotorsproject.campaigns.controller.CampaignController;
import com.carmotorsproject.campaigns.model.Campaign;
import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.ModernTextField;
import com.carmotorsproject.ui.components.TransparentPanel;
import com.carmotorsproject.ui.components.RoundedPanel;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Vista mejorada para la gestión de campañas con diseño moderno y responsivo.
 */
public class CampaignView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(CampaignView.class.getName());

    // Controller
    private CampaignController controller;

    // UI Components
    private JPanel contentPane;
    private JTable campaignTable;
    private DefaultTableModel tableModel;
    private JSplitPane mainSplitPane;
    private JLabel statusLabel;
    private JProgressBar progressBar;

    // Form Components
    private ModernTextField nameField;
    private JTextArea descriptionArea;
    private JFormattedTextField startDateField;
    private JFormattedTextField endDateField;
    private JSpinner discountSpinner;
    private JComboBox<String> vehicleTypeComboBox;
    private JComboBox<String> customerSegmentComboBox;
    private JComboBox<String> statusComboBox;
    private ModernTextField searchField;

    // Buttons
    private ModernButton searchButton;
    private ModernButton clearSearchButton;
    private ModernButton addButton;
    private ModernButton updateButton;
    private ModernButton deleteButton;
    private ModernButton clearButton;
    private ModernButton reportButton;

    // Current campaign ID
    private int currentCampaignId = -1;
    private int campaignCount = 0;

    // Date format
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Constructor que inicializa la vista.
     */
    public CampaignView() {
        initComponents();
        setupLayout();
        setupListeners();

        setTitle("Gestión de Campañas");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        LOGGER.log(Level.INFO, "CampaignView initialized");
    }

    /**
     * Sets the controller for this view.
     *
     * @param controller The campaign controller
     */
    public void setController(CampaignController controller) {
        this.controller = controller;
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        // Apply theme
        AppTheme.applyTheme();

        contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBackground(AppTheme.PRIMARY_WHITE);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        contentPane.add(headerPanel, BorderLayout.NORTH);

        // SplitPane for responsive layout
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(400);
        mainSplitPane.setResizeWeight(0.3);
        mainSplitPane.setBorder(null);
        mainSplitPane.setDividerSize(5);
        mainSplitPane.setContinuousLayout(true);
        setDividerColor(mainSplitPane, AppTheme.PRIMARY_RED);

        // Form panel
        JPanel formPanel = createFormPanel();
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(null);
        formScrollPane.getVerticalScrollBar().setUI(createModernScrollBarUI());
        formScrollPane.getHorizontalScrollBar().setUI(createModernScrollBarUI());
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainSplitPane.setLeftComponent(formScrollPane);

        // Table panel
        JPanel tablePanel = createTablePanel();
        mainSplitPane.setRightComponent(tablePanel);

        contentPane.add(mainSplitPane, BorderLayout.CENTER);

        // Status panel
        JPanel statusPanel = createStatusPanel();
        contentPane.add(statusPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the header panel.
     *
     * @return The header panel
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new TransparentPanel(AppTheme.PRIMARY_RED, 0.9f);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setPreferredSize(new Dimension(0, 70));

        // Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);

        JLabel iconLabel = new JLabel("🏷️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titlePanel.add(iconLabel);

        JLabel titleLabel = new JLabel("Gestión de Campañas");
        titleLabel.setFont(AppTheme.TITLE_FONT);
        titleLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titlePanel.add(titleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setOpaque(false);

        searchField = new ModernTextField("20", "Buscar por nombre...");
        searchPanel.add(searchField);

        searchButton = new ModernButton("Buscar", "primary");
        searchPanel.add(searchButton);

        clearSearchButton = new ModernButton("Limpiar", "light");
        searchPanel.add(clearSearchButton);

        panel.add(searchPanel, BorderLayout.EAST);

        // Counter panel
        JPanel counterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        counterPanel.setOpaque(false);

        JLabel countLabel = new JLabel("Total de campañas: ");
        countLabel.setFont(AppTheme.REGULAR_FONT);
        countLabel.setForeground(AppTheme.PRIMARY_WHITE);
        counterPanel.add(countLabel);

        JLabel countValueLabel = new JLabel("0");
        countValueLabel.setFont(AppTheme.SUBTITLE_FONT);
        countValueLabel.setForeground(AppTheme.PRIMARY_WHITE);
        counterPanel.add(countValueLabel);

        // Update counter when campaigns are loaded
        new Timer(1000, e -> {
            countValueLabel.setText(String.valueOf(campaignCount));
        }).start();

        panel.add(counterPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the form panel.
     *
     * @return The form panel
     */
    private JPanel createFormPanel() {
        RoundedPanel formPanel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15, true);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(AppTheme.PRIMARY_RED, 1, true),
                        "Detalles de la Campaña",
                        0,
                        0,
                        AppTheme.SUBTITLE_FONT,
                        AppTheme.PRIMARY_RED
                )
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        nameField = new ModernTextField("25", "Nombre de la campaña");
        addFormField(formPanel, "Nombre:", nameField, gbc);

        // Description
        gbc.gridy = 1;
        JPanel descPanel = new JPanel(new BorderLayout(5, 5));
        descPanel.setOpaque(false);
        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(AppTheme.REGULAR_FONT);
        descPanel.add(lblDesc, BorderLayout.NORTH);

        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(AppTheme.REGULAR_FONT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setBorder(BorderFactory.createEmptyBorder());
        descScrollPane.getVerticalScrollBar().setUI(createModernScrollBarUI());
        descScrollPane.getHorizontalScrollBar().setUI(createModernScrollBarUI());

        descPanel.add(descScrollPane, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        formPanel.add(descPanel, gbc);

        gbc.weighty = 0.0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Start Date
        gbc.gridy = 2;
        JPanel startDatePanel = new JPanel(new BorderLayout(5, 0));
        startDatePanel.setOpaque(false);

        startDateField = new JFormattedTextField(dateFormat);
        startDateField.setColumns(15);
        startDateField.setFont(AppTheme.REGULAR_FONT);
        startDateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        addFormField(formPanel, "Fecha Inicio (yyyy-MM-dd):", startDateField, gbc);

        // End Date
        gbc.gridy = 3;
        JPanel endDatePanel = new JPanel(new BorderLayout(5, 0));
        endDatePanel.setOpaque(false);

        endDateField = new JFormattedTextField(dateFormat);
        endDateField.setColumns(15);
        endDateField.setFont(AppTheme.REGULAR_FONT);
        endDateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        addFormField(formPanel, "Fecha Fin (yyyy-MM-dd):", endDateField, gbc);

        // Discount
        gbc.gridy = 4;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.0, 0.0, 100.0, 1.0);
        discountSpinner = new JSpinner(spinnerModel);
        discountSpinner.setFont(AppTheme.REGULAR_FONT);
        JComponent editor = discountSpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setFont(AppTheme.REGULAR_FONT);
        }

        addFormField(formPanel, "Descuento %:", discountSpinner, gbc);

        // Vehicle Type
        gbc.gridy = 5;
        vehicleTypeComboBox = new JComboBox<>(new String[]{"TODOS", "SEDAN", "SUV", "CAMIONETA", "VAN", "DEPORTIVO"});
        vehicleTypeComboBox.setFont(AppTheme.REGULAR_FONT);
        vehicleTypeComboBox.setBackground(AppTheme.PRIMARY_WHITE);

        addFormField(formPanel, "Tipo de Vehículo:", vehicleTypeComboBox, gbc);

        // Customer Segment
        gbc.gridy = 6;
        customerSegmentComboBox = new JComboBox<>(new String[]{"TODOS", "REGULAR", "PREMIUM", "VIP"});
        customerSegmentComboBox.setFont(AppTheme.REGULAR_FONT);
        customerSegmentComboBox.setBackground(AppTheme.PRIMARY_WHITE);

        addFormField(formPanel, "Segmento de Cliente:", customerSegmentComboBox, gbc);

        // Status
        gbc.gridy = 7;
        statusComboBox = new JComboBox<>(new String[]{"ACTIVA", "INACTIVA", "PLANIFICADA", "COMPLETADA"});
        statusComboBox.setFont(AppTheme.REGULAR_FONT);
        statusComboBox.setBackground(AppTheme.PRIMARY_WHITE);

        addFormField(formPanel, "Estado:", statusComboBox, gbc);

        // Button panel
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = createFormButtonPanel();
        formPanel.add(buttonPanel, gbc);

        return formPanel;
    }

    /**
     * Helper method to add a form field with label.
     */
    private void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(AppTheme.REGULAR_FONT);

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    /**
     * Creates the form button panel.
     *
     * @return The button panel
     */
    private JPanel createFormButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        addButton = new ModernButton("Agregar Campaña", "success");
        panel.add(addButton);

        updateButton = new ModernButton("Actualizar", "primary");
        updateButton.setEnabled(false);
        panel.add(updateButton);

        deleteButton = new ModernButton("Eliminar", "danger");
        deleteButton.setEnabled(false);
        panel.add(deleteButton);

        clearButton = new ModernButton("Limpiar", "light");
        panel.add(clearButton);

        reportButton = new ModernButton("Reportes", "secondary");
        panel.add(reportButton);

        return panel;
    }

    /**
     * Creates the table panel.
     *
     * @return The table panel
     */
    private JPanel createTablePanel() {
        RoundedPanel tablePanel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15, true);
        tablePanel.setLayout(new BorderLayout(10, 10));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(AppTheme.PRIMARY_RED, 1, true),
                        "Lista de Campañas",
                        0,
                        0,
                        AppTheme.SUBTITLE_FONT,
                        AppTheme.PRIMARY_RED
                )
        ));

        // Create table model
        String[] columnNames = {"ID", "Nombre", "Fecha Inicio", "Fecha Fin", "Descuento %", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Create table
        campaignTable = new JTable(tableModel);
        campaignTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        campaignTable.getTableHeader().setReorderingAllowed(false);
        campaignTable.setRowHeight(30);
        campaignTable.setFont(AppTheme.REGULAR_FONT);
        campaignTable.setIntercellSpacing(new Dimension(10, 5));
        campaignTable.setShowGrid(false);
        campaignTable.setFillsViewportHeight(true);

        // Table header style
        JTableHeader header = campaignTable.getTableHeader();
        header.setBackground(AppTheme.PRIMARY_RED);
        header.setForeground(AppTheme.PRIMARY_WHITE);
        header.setFont(AppTheme.TABLE_HEADER_FONT);
        header.setBorder(new MatteBorder(0, 0, 2, 0, AppTheme.PRIMARY_RED));

        // Cell style
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < campaignTable.getColumnCount(); i++) {
            campaignTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Add zebra style
        campaignTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    comp.setBackground(AppTheme.PRIMARY_RED);
                    comp.setForeground(AppTheme.PRIMARY_WHITE);
                } else {
                    comp.setBackground(row % 2 == 0 ? AppTheme.PRIMARY_WHITE : new Color(255, 245, 245));
                    comp.setForeground(AppTheme.PRIMARY_BLACK);
                }

                setHorizontalAlignment(JLabel.CENTER);
                setBorder(new EmptyBorder(0, 5, 0, 5));
                return comp;
            }
        });

        // Add table to scroll pane with custom scrollbars
        JScrollPane scrollPane = new JScrollPane(campaignTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppTheme.PRIMARY_WHITE);

        // Customize scrollbars
        scrollPane.getVerticalScrollBar().setUI(createModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(createModernScrollBarUI());

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    /**
     * Creates the status panel.
     *
     * @return The status panel
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.PRIMARY_RED);
        panel.setBorder(new EmptyBorder(5, 15, 5, 15));
        panel.setPreferredSize(new Dimension(0, 30));

        statusLabel = new JLabel("Listo");
        statusLabel.setFont(AppTheme.SMALL_FONT);
        statusLabel.setForeground(AppTheme.PRIMARY_WHITE);
        panel.add(statusLabel, BorderLayout.WEST);

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(150, 15));
        progressBar.setVisible(false);
        progressBar.setForeground(AppTheme.ACCENT_GREEN);
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
     * Creates a modern scrollbar UI.
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
                g2.setColor(AppTheme.PRIMARY_RED);
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
     * Sets up the layout of the UI components.
     */
    private void setupLayout() {
        // Layout is already set up in the component creation methods
    }

    /**
     * Sets up the event listeners for the UI components.
     */
    private void setupListeners() {
        // Table selection listener
        campaignTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = campaignTable.getSelectedRow();
                if (row >= 0) {
                    int campaignId = (int) tableModel.getValueAt(row, 0);
                    controller.loadCampaignDetails(campaignId);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);

                    // Update status
                    updateStatus("Campaña seleccionada: ID " + campaignId, false);
                }
            }
        });

        // Search button
        searchButton.addActionListener((ActionEvent e) -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                updateStatus("Buscando campañas...", true);
                controller.searchByName(searchTerm);
            } else {
                controller.loadCampaigns();
            }
        });

        // Clear search button
        clearSearchButton.addActionListener((ActionEvent e) -> {
            searchField.setText("");
            controller.loadCampaigns();
        });

        // Add button
        addButton.addActionListener((ActionEvent e) -> {
            try {
                updateStatus("Agregando campaña...", true);
                Campaign campaign = getCampaignFromForm();
                controller.addCampaign(campaign);
                clearForm();
                showSuccess("Campaña agregada exitosamente");
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error adding campaign", ex);
                showError("Error al agregar campaña: " + ex.getMessage());
                updateStatus("Error al agregar campaña", false);
            }
        });

        // Update button
        updateButton.addActionListener((ActionEvent e) -> {
            try {
                updateStatus("Actualizando campaña...", true);
                Campaign campaign = getCampaignFromForm();
                campaign.setCampaignId(currentCampaignId);
                controller.updateCampaign(campaign);
                showSuccess("Campaña actualizada exitosamente");
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error updating campaign", ex);
                showError("Error al actualizar campaña: " + ex.getMessage());
                updateStatus("Error al actualizar campaña", false);
            }
        });

        // Delete button
        deleteButton.addActionListener((ActionEvent e) -> {
            if (currentCampaignId >= 0) {
                int option = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro que desea eliminar esta campaña?",
                        "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    updateStatus("Eliminando campaña...", true);
                    controller.deleteCampaign(currentCampaignId);
                    clearForm();
                    showSuccess("Campaña eliminada exitosamente");
                }
            }
        });

        // Clear button
        clearButton.addActionListener((ActionEvent e) -> {
            clearForm();
        });

        // Report button
        reportButton.addActionListener((ActionEvent e) -> {
            updateStatus("Abriendo reportes...", true);
            controller.openReportView();
        });
    }

    /**
     * Updates the campaign table with the provided list of campaigns.
     *
     * @param campaigns The list of campaigns to display
     */
    public void updateCampaignTable(List<Campaign> campaigns) {
        tableModel.setRowCount(0);

        // Update campaign count
        campaignCount = campaigns.size();

        for (Campaign campaign : campaigns) {
            Object[] row = {
                    campaign.getCampaignId(),
                    campaign.getName(),
                    campaign.getStartDate() != null ? dateFormat.format(campaign.getStartDate()) : "",
                    campaign.getEndDate() != null ? dateFormat.format(campaign.getEndDate()) : "",
                    campaign.getDiscountPercentage(),
                    campaign.getStatus()
            };
            tableModel.addRow(row);
        }

        updateStatus("Se encontraron " + campaigns.size() + " campañas", false);
    }

    /**
     * Populates the form with the details of a campaign.
     *
     * @param campaign The campaign to display
     */
    public void populateCampaignForm(Campaign campaign) {
        currentCampaignId = campaign.getCampaignId();

        nameField.setText(campaign.getName());
        descriptionArea.setText(campaign.getDescription());

        if (campaign.getStartDate() != null) {
            startDateField.setValue(campaign.getStartDate());
        } else {
            startDateField.setValue(null);
        }

        if (campaign.getEndDate() != null) {
            endDateField.setValue(campaign.getEndDate());
        } else {
            endDateField.setValue(null);
        }

        discountSpinner.setValue(campaign.getDiscountPercentage());

        if (campaign.getTargetVehicleType() != null) {
            vehicleTypeComboBox.setSelectedItem(campaign.getTargetVehicleType());
        } else {
            vehicleTypeComboBox.setSelectedIndex(0);
        }

        customerSegmentComboBox.setSelectedIndex(campaign.getTargetCustomerSegment());

        if (campaign.getStatus() != null) {
            statusComboBox.setSelectedItem(campaign.getStatus());
        } else {
            statusComboBox.setSelectedIndex(0);
        }

        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);

        updateStatus("Campaña cargada: " + campaign.getName(), false);
    }

    /**
     * Clears the form fields.
     */
    private void clearForm() {
        currentCampaignId = -1;

        nameField.setText("");
        descriptionArea.setText("");
        startDateField.setValue(null);
        endDateField.setValue(null);
        discountSpinner.setValue(0.0);
        vehicleTypeComboBox.setSelectedIndex(0);
        customerSegmentComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        updateStatus("Formulario limpiado", false);
    }

    /**
     * Gets a campaign object from the form fields.
     *
     * @return A campaign object
     * @throws ParseException If there is an error parsing the dates
     */
    private Campaign getCampaignFromForm() throws ParseException {
        Campaign campaign = new Campaign();

        campaign.setName(nameField.getText().trim());
        campaign.setDescription(descriptionArea.getText().trim());

        if (startDateField.getValue() != null) {
            campaign.setStartDate((Date) startDateField.getValue());
        }

        if (endDateField.getValue() != null) {
            campaign.setEndDate((Date) endDateField.getValue());
        }

        campaign.setDiscountPercentage((Double) discountSpinner.getValue());
        campaign.setTargetVehicleType((String) vehicleTypeComboBox.getSelectedItem());
        campaign.setTargetCustomerSegment(customerSegmentComboBox.getSelectedIndex());
        campaign.setStatus((String) statusComboBox.getSelectedItem());

        // Set audit fields
        String username = System.getProperty("user.name");
        Date now = new Date();

        if (currentCampaignId < 0) {
            // New campaign
            campaign.setCreatedBy(username);
            campaign.setCreatedDate(now);
        }

        campaign.setLastModifiedBy(username);
        campaign.setLastModifiedDate(now);

        return campaign;
    }

    /**
     * Shows an error message.
     *
     * @param message The error message
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows an information message.
     *
     * @param message The information message
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a success message.
     *
     * @param message The success message
     */
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
