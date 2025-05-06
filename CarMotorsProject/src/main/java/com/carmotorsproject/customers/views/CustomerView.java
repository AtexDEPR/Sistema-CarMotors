package com.carmotorsproject.customers.views;

import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.ModernTextField;
import com.carmotorsproject.ui.components.RoundedPanel;
import com.carmotorsproject.ui.components.TransparentPanel;
import com.carmotorsproject.customers.controller.CustomerController;
import com.carmotorsproject.customers.model.Customer;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Vista mejorada para la gestión de clientes con diseño moderno y responsivo.
 */
public class CustomerView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(CustomerView.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Controller
    private final CustomerController controller;

    // UI Components
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JSplitPane mainSplitPane;
    private JLabel statusLabel;
    private JProgressBar progressBar;

    // Form Components
    private ModernTextField txtFirstName;
    private ModernTextField txtLastName;
    private ModernTextField txtIdentification;
    private JComboBox<String> cmbIdentificationType;
    private ModernTextField txtEmail;
    private ModernTextField txtPhone;
    private ModernTextField txtAddress;
    private ModernTextField txtCity;
    private ModernTextField txtState;
    private ModernTextField txtZipCode;
    private ModernTextField txtCountry;
    private JCheckBox chkActive;
    private JTextArea txtNotes;

    // Buttons
    private ModernButton btnAdd;
    private ModernButton btnUpdate;
    private ModernButton btnDelete;
    private ModernButton btnClear;
    private ModernButton btnSearch;
    private ModernTextField txtSearch;
    private JComboBox<String> cmbSearchType;

    // Selected customer ID
    private int selectedCustomerId = 0;
    private int customerCount = 0;

    /**
     * Constructor que inicializa la vista.
     */
    public CustomerView() {
        initComponents();
        controller = new CustomerController(this);
        loadCustomers();
    }

    /**
     * Inicializa los componentes de la UI.
     */
    private void initComponents() {
        setTitle("Gestión de Clientes");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Aplicar tema
        AppTheme.applyTheme();

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(AppTheme.PRIMARY_WHITE);

        // Panel de cabecera
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // SplitPane para layout responsivo
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(400);
        mainSplitPane.setResizeWeight(0.3);
        mainSplitPane.setBorder(null);
        mainSplitPane.setDividerSize(5);
        mainSplitPane.setContinuousLayout(true);
        setDividerColor(mainSplitPane, AppTheme.PRIMARY_BLUE);

        // Panel de formulario
        JPanel formPanel = createFormPanel();
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(null);
        formScrollPane.getVerticalScrollBar().setUI(createModernScrollBarUI());
        formScrollPane.getHorizontalScrollBar().setUI(createModernScrollBarUI());
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainSplitPane.setLeftComponent(formScrollPane);

        // Panel de tabla
        JPanel tablePanel = createTablePanel();
        mainSplitPane.setRightComponent(tablePanel);

        mainPanel.add(mainSplitPane, BorderLayout.CENTER);

        // Panel de estado
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        // Agregar panel principal al frame
        setContentPane(mainPanel);

        // Agregar listeners
        addActionListeners();
    }

    /**
     * Crea el panel de cabecera.
     *
     * @return El panel de cabecera
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new TransparentPanel(AppTheme.PRIMARY_BLUE, 0.9f);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setPreferredSize(new Dimension(0, 70));

        // Título con icono
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);

        JLabel iconLabel = new JLabel("👥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titlePanel.add(iconLabel);

        JLabel titleLabel = new JLabel("Gestión de Clientes");
        titleLabel.setFont(AppTheme.TITLE_FONT);
        titleLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titlePanel.add(titleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setOpaque(false);

        cmbSearchType = new JComboBox<>(new String[]{"Nombre", "Identificación", "Email", "Teléfono"});
        cmbSearchType.setFont(AppTheme.REGULAR_FONT);
        cmbSearchType.setBackground(AppTheme.PRIMARY_WHITE);
        cmbSearchType.setForeground(AppTheme.PRIMARY_BLACK);
        searchPanel.add(cmbSearchType);

        txtSearch = new ModernTextField("20", "Buscar cliente...");
        searchPanel.add(txtSearch);

        btnSearch = new ModernButton("Buscar", "primary");
        btnSearch.addActionListener(e -> searchCustomers());
        searchPanel.add(btnSearch);

        ModernButton btnReset = new ModernButton("Limpiar", "light");
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            loadCustomers();
        });
        searchPanel.add(btnReset);

        panel.add(searchPanel, BorderLayout.EAST);

        // Panel contador
        JPanel counterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        counterPanel.setOpaque(false);

        JLabel countLabel = new JLabel("Total de clientes: ");
        countLabel.setFont(AppTheme.REGULAR_FONT);
        countLabel.setForeground(AppTheme.PRIMARY_WHITE);
        counterPanel.add(countLabel);

        JLabel countValueLabel = new JLabel("0");
        countValueLabel.setFont(AppTheme.SUBTITLE_FONT);
        countValueLabel.setForeground(AppTheme.PRIMARY_WHITE);
        counterPanel.add(countValueLabel);

        // Actualizar contador cuando se cargan clientes
        new Timer(1000, e -> {
            countValueLabel.setText(String.valueOf(customerCount));
        }).start();

        panel.add(counterPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel de formulario.
     *
     * @return El panel de formulario
     */
    private JPanel createFormPanel() {
        RoundedPanel formPanel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15, true);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(AppTheme.PRIMARY_BLUE, 1, true),
                        "Datos del Cliente",
                        0,
                        0,
                        AppTheme.SUBTITLE_FONT,
                        AppTheme.PRIMARY_BLUE
                )
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        txtFirstName = new ModernTextField("25", "Nombre");
        addFormField(formPanel, "Nombre:", txtFirstName, gbc);

        // Apellido
        gbc.gridy = 1;
        txtLastName = new ModernTextField("25", "Apellido");
        addFormField(formPanel, "Apellido:", txtLastName, gbc);

        // Identificación
        gbc.gridy = 2;
        txtIdentification = new ModernTextField("20", "Número de Identificación");
        addFormField(formPanel, "Identificación:", txtIdentification, gbc);

        // Tipo de Identificación
        gbc.gridy = 3;
        JPanel idTypePanel = new JPanel(new BorderLayout(5, 0));
        idTypePanel.setOpaque(false);

        cmbIdentificationType = new JComboBox<>(new String[]{"Cédula de Ciudadanía", "Cédula de Extranjería", "Pasaporte", "NIT", "Otro"});
        cmbIdentificationType.setFont(AppTheme.REGULAR_FONT);
        cmbIdentificationType.setBackground(AppTheme.PRIMARY_WHITE);

        addFormField(formPanel, "Tipo de Identificación:", cmbIdentificationType, gbc);

        // Email
        gbc.gridy = 4;
        txtEmail = new ModernTextField("25", "Correo Electrónico");
        addFormField(formPanel, "Email:", txtEmail, gbc);

        // Teléfono
        gbc.gridy = 5;
        txtPhone = new ModernTextField("20", "Teléfono");
        addFormField(formPanel, "Teléfono:", txtPhone, gbc);

        // Dirección
        gbc.gridy = 6;
        txtAddress = new ModernTextField("30", "Dirección");
        addFormField(formPanel, "Dirección:", txtAddress, gbc);

        // Ciudad
        gbc.gridy = 7;
        txtCity = new ModernTextField("20", "Ciudad");
        addFormField(formPanel, "Ciudad:", txtCity, gbc);

        // Estado/Departamento
        gbc.gridy = 8;
        txtState = new ModernTextField("20", "Departamento");
        addFormField(formPanel, "Departamento:", txtState, gbc);

        // Código Postal
        gbc.gridy = 9;
        txtZipCode = new ModernTextField("10", "Código Postal");
        addFormField(formPanel, "Código Postal:", txtZipCode, gbc);

        // País
        gbc.gridy = 10;
        txtCountry = new ModernTextField("20", "País");
        txtCountry.setText("Colombia");
        addFormField(formPanel, "País:", txtCountry, gbc);

        // Activo
        gbc.gridy = 11;
        JPanel activePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        activePanel.setOpaque(false);
        chkActive = new JCheckBox("Cliente Activo");
        chkActive.setFont(AppTheme.REGULAR_FONT);
        chkActive.setSelected(true);
        chkActive.setOpaque(false);
        activePanel.add(chkActive);

        JLabel activeLabel = new JLabel("Estado:");
        activeLabel.setFont(AppTheme.REGULAR_FONT);
        gbc.gridx = 0;
        formPanel.add(activeLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(activePanel, gbc);

        // Notas
        gbc.gridy = 12;
        JPanel notesPanel = new JPanel(new BorderLayout(5, 5));
        notesPanel.setOpaque(false);
        JLabel lblNotes = new JLabel("Notas:");
        lblNotes.setFont(AppTheme.REGULAR_FONT);
        notesPanel.add(lblNotes, BorderLayout.NORTH);

        txtNotes = new JTextArea(6, 20);
        txtNotes.setFont(AppTheme.REGULAR_FONT);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        txtNotes.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JScrollPane notesScrollPane = new JScrollPane(txtNotes);
        notesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        notesScrollPane.getVerticalScrollBar().setUI(createModernScrollBarUI());
        notesScrollPane.getHorizontalScrollBar().setUI(createModernScrollBarUI());

        notesPanel.add(notesScrollPane, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        formPanel.add(notesPanel, gbc);

        // Panel de botones
        gbc.gridy = 13;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = createFormButtonPanel();
        formPanel.add(buttonPanel, gbc);

        return formPanel;
    }

    /**
     * Método auxiliar para agregar un campo de formulario con etiqueta.
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
     * Crea el panel de botones del formulario.
     *
     * @return El panel de botones
     */
    private JPanel createFormButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnAdd = new ModernButton("Agregar Cliente", "success");
        btnAdd.addActionListener(e -> addCustomer());
        panel.add(btnAdd);

        btnUpdate = new ModernButton("Actualizar", "primary");
        btnUpdate.setEnabled(false);
        btnUpdate.addActionListener(e -> updateCustomer());
        panel.add(btnUpdate);

        btnDelete = new ModernButton("Eliminar", "danger");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(e -> deleteCustomer());
        panel.add(btnDelete);

        btnClear = new ModernButton("Limpiar", "light");
        btnClear.addActionListener(e -> clearForm());
        panel.add(btnClear);

        return panel;
    }

    /**
     * Crea el panel de tabla.
     *
     * @return El panel de tabla
     */
    private JPanel createTablePanel() {
        RoundedPanel tablePanel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15, true);
        tablePanel.setLayout(new BorderLayout(10, 10));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(AppTheme.PRIMARY_BLUE, 1, true),
                        "Lista de Clientes",
                        0,
                        0,
                        AppTheme.SUBTITLE_FONT,
                        AppTheme.PRIMARY_BLUE
                )
        ));

        // Crear modelo de tabla
        String[] columnNames = {"ID", "Nombre", "Identificación", "Email", "Teléfono", "Activo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) { // Columna "Activo"
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        // Crear tabla
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getTableHeader().setReorderingAllowed(false);
        customerTable.setRowHeight(30);
        customerTable.setFont(AppTheme.REGULAR_FONT);
        customerTable.setIntercellSpacing(new Dimension(10, 5));
        customerTable.setShowGrid(false);
        customerTable.setFillsViewportHeight(true);

        // Estilo del encabezado de tabla
        JTableHeader header = customerTable.getTableHeader();
        header.setBackground(AppTheme.PRIMARY_BLUE);
        header.setForeground(AppTheme.PRIMARY_WHITE);
        header.setFont(AppTheme.TABLE_HEADER_FONT);
        header.setBorder(new MatteBorder(0, 0, 2, 0, AppTheme.PRIMARY_BLUE));

        // Estilo de las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < customerTable.getColumnCount(); i++) {
            customerTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Agregar estilo zebra
        customerTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    comp.setBackground(AppTheme.PRIMARY_BLUE);
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

        // Agregar listener de mouse a la tabla
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = customerTable.getSelectedRow();
                if (row >= 0) {
                    selectedCustomerId = (int) customerTable.getValueAt(row, 0);
                    controller.loadCustomerDetails(selectedCustomerId);
                    btnUpdate.setEnabled(true);
                    btnDelete.setEnabled(true);

                    // Actualizar estado
                    updateStatus("Cliente seleccionado: ID " + selectedCustomerId, false);
                }
            }
        });

        // Agregar tabla a un scroll pane con scrollbars personalizados
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppTheme.PRIMARY_WHITE);

        // Personalizar scrollbars
        scrollPane.getVerticalScrollBar().setUI(createModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(createModernScrollBarUI());

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    /**
     * Crea el panel de estado.
     *
     * @return El panel de estado
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.PRIMARY_BLUE);
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
     * Actualiza el mensaje de estado.
     */
    private void updateStatus(String message, boolean showProgress) {
        statusLabel.setText(message);

        if (showProgress) {
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);

            // Ocultar progreso después de 2 segundos
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
     * Crea un UI de scrollbar moderno.
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

                // Rellenar thumb con color
                g2.setColor(AppTheme.PRIMARY_BLUE);
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
     * Agrega listeners a los botones y tabla.
     */
    private void addActionListeners() {
        // Los listeners ya están configurados en los métodos de creación de paneles
    }

    /**
     * Carga todos los clientes y actualiza la tabla.
     */
    public void loadCustomers() {
        updateStatus("Cargando clientes...", true);
        controller.loadCustomers();
    }

    /**
     * Actualiza la tabla de clientes con la lista proporcionada.
     *
     * @param customers La lista de clientes a mostrar
     */
    public void updateCustomerTable(List<Customer> customers) {
        // Limpiar la tabla
        tableModel.setRowCount(0);

        // Actualizar contador de clientes
        customerCount = customers.size();

        // Agregar clientes a la tabla
        for (Customer customer : customers) {
            Object[] row = {
                    customer.getCustomerId(),
                    customer.getFullName(),
                    customer.getIdentification(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.isActive()
            };
            tableModel.addRow(row);
        }

        updateStatus("Se encontraron " + customers.size() + " clientes", false);
    }

    /**
     * Rellena el formulario con los detalles de un cliente.
     *
     * @param customer El cliente a mostrar
     */
    public void populateCustomerForm(Customer customer) {
        selectedCustomerId = customer.getCustomerId();
        txtFirstName.setText(customer.getFirstName());
        txtLastName.setText(customer.getLastName());
        txtIdentification.setText(customer.getIdentification());
        cmbIdentificationType.setSelectedItem(customer.getIdentificationType());
        txtEmail.setText(customer.getEmail());
        txtPhone.setText(customer.getPhone());
        txtAddress.setText(customer.getAddress());
        txtCity.setText(customer.getCity());
        txtState.setText(customer.getState());
        txtZipCode.setText(customer.getZipCode());
        txtCountry.setText(customer.getCountry());
        chkActive.setSelected(customer.isActive());
        txtNotes.setText(customer.getNotes());

        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);

        updateStatus("Cliente cargado: " + customer.getFullName(), false);
    }

    /**
     * Limpia el formulario.
     */
    public void clearForm() {
        selectedCustomerId = 0;
        txtFirstName.setText("");
        txtLastName.setText("");
        txtIdentification.setText("");
        cmbIdentificationType.setSelectedIndex(0);
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtZipCode.setText("");
        txtCountry.setText("Colombia");
        chkActive.setSelected(true);
        txtNotes.setText("");
        customerTable.clearSelection();

        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        updateStatus("Formulario limpiado", false);
    }

    /**
     * Agrega un nuevo cliente.
     */
    private void addCustomer() {
        try {
            updateStatus("Agregando cliente...", true);

            // Crear un nuevo cliente a partir de los datos del formulario
            Customer customer = getCustomerFromForm();

            // Agregar el cliente
            controller.addCustomer(customer);

            // Limpiar el formulario
            clearForm();

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            updateStatus("Cliente agregado correctamente", false);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error adding customer", ex);
            JOptionPane.showMessageDialog(this, "Error al agregar cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al agregar cliente", false);
        }
    }

    /**
     * Actualiza un cliente existente.
     */
    private void updateCustomer() {
        if (selectedCustomerId == 0) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un cliente para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            updateStatus("Actualizando cliente...", true);

            // Crear un cliente a partir de los datos del formulario
            Customer customer = getCustomerFromForm();
            customer.setCustomerId(selectedCustomerId);

            // Actualizar el cliente
            controller.updateCustomer(customer);

            // Limpiar el formulario
            clearForm();

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            updateStatus("Cliente actualizado correctamente", false);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating customer", ex);
            JOptionPane.showMessageDialog(this, "Error al actualizar cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al actualizar cliente", false);
        }
    }

    /**
     * Elimina un cliente.
     */
    private void deleteCustomer() {
        if (selectedCustomerId == 0) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un cliente para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmar eliminación
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este cliente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            updateStatus("Eliminando cliente...", true);

            // Eliminar el cliente
            controller.deleteCustomer(selectedCustomerId);

            // Limpiar el formulario
            clearForm();

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            updateStatus("Cliente eliminado correctamente", false);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error deleting customer", ex);
            JOptionPane.showMessageDialog(this, "Error al eliminar cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al eliminar cliente", false);
        }
    }

    /**
     * Busca clientes según los criterios de búsqueda.
     */
    private void searchCustomers() {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            loadCustomers();
            return;
        }

        String searchType = (String) cmbSearchType.getSelectedItem();
        updateStatus("Buscando clientes...", true);

        try {
            switch (searchType) {
                case "Nombre":
                    controller.searchCustomersByName(searchText);
                    break;
                case "Identificación":
                    controller.searchCustomersByIdentification(searchText);
                    break;
                case "Email":
                    controller.searchCustomersByEmail(searchText);
                    break;
                case "Teléfono":
                    controller.searchCustomersByPhone(searchText);
                    break;
                default:
                    loadCustomers();
                    break;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error searching customers", ex);
            JOptionPane.showMessageDialog(this, "Error al buscar clientes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al buscar clientes", false);
        }
    }

    /**
     * Crea un objeto Customer a partir de los datos del formulario.
     *
     * @return Un objeto Customer con datos del formulario
     */
    private Customer getCustomerFromForm() {
        Customer customer = new Customer();

        // Establecer propiedades del cliente desde los campos del formulario
        customer.setFirstName(txtFirstName.getText().trim());
        customer.setLastName(txtLastName.getText().trim());
        customer.setIdentification(txtIdentification.getText().trim());
        customer.setIdentificationType((String) cmbIdentificationType.getSelectedItem());
        customer.setEmail(txtEmail.getText().trim());
        customer.setPhone(txtPhone.getText().trim());
        customer.setAddress(txtAddress.getText().trim());
        customer.setCity(txtCity.getText().trim());
        customer.setState(txtState.getText().trim());
        customer.setZipCode(txtZipCode.getText().trim());
        customer.setCountry(txtCountry.getText().trim());
        customer.setActive(chkActive.isSelected());
        customer.setNotes(txtNotes.getText().trim());

        // Establecer fecha de registro a la fecha actual para nuevos clientes
        if (selectedCustomerId == 0) {
            customer.setRegistrationDate(new Date());
        }

        return customer;
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param message El mensaje de error a mostrar
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje informativo.
     *
     * @param message El mensaje informativo a mostrar
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Método auxiliar para establecer el color del divisor de un JSplitPane.
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
