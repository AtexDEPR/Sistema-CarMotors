package com.carmotorsproject.services.views;

import com.carmotorsproject.services.controller.TechnicianController;
import com.carmotorsproject.services.model.Technician;
import com.carmotorsproject.services.model.TechnicianSpecialty;
import com.carmotorsproject.services.model.TechnicianStatus;
import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.ModernTextField;
import com.carmotorsproject.ui.components.RoundedPanel;
import com.carmotorsproject.ui.components.TransparentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
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
 * Vista para la gestión de técnicos con un diseño moderno y responsivo.
 */
public class TechnicianView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(TechnicianView.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Controller
    private final TechnicianController controller;

    // UI Components
    private JTable technicianTable;
    private DefaultTableModel technicianTableModel;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private JSplitPane mainSplitPane;

    // Form Components
    private ModernTextField nameField;
    private ModernTextField lastNameField;
    private JComboBox<TechnicianSpecialty> specialtyComboBox;
    private JComboBox<TechnicianStatus> statusComboBox;
    private ModernTextField phoneField;
    private ModernTextField emailField;
    private ModernTextField hireDateField;
    private ModernTextField hourlyRateField;
    private ModernTextField certificationsField;
    private JTextArea notesArea;

    // Buttons
    private ModernButton addButton;
    private ModernButton updateButton;
    private ModernButton deleteButton;
    private ModernButton clearButton;
    private ModernButton searchButton;

    // Selected ID
    private int selectedTechnicianId = 0;
    private int technicianCount = 0;

    /**
     * Constructor que inicializa la UI y el controlador.
     */
    public TechnicianView() {
        this.controller = new TechnicianController(this);
        initComponents();
        loadData();
    }

    /**
     * Inicializa los componentes de la UI.
     */
    private void initComponents() {
        // Configuración del frame
        setTitle("Gestión de Técnicos");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppTheme.PRIMARY_WHITE);

        // Panel de cabecera
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // SplitPane para layout responsivo
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerLocation(300);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setBorder(null);
        mainSplitPane.setDividerSize(5);
        mainSplitPane.setContinuousLayout(true);
        setDividerColor(mainSplitPane, AppTheme.ACCENT_RED);

        // Panel de tabla
        JPanel tablePanel = createTablePanel();
        mainSplitPane.setTopComponent(tablePanel);

        // Panel de formulario
        JPanel formPanel = createFormPanel();
        mainSplitPane.setBottomComponent(formPanel);

        mainPanel.add(mainSplitPane, BorderLayout.CENTER);

        // Panel de estado
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        // Agregar panel principal al frame
        add(mainPanel);
    }

    /**
     * Crea el panel de cabecera.
     *
     * @return El panel de cabecera
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new TransparentPanel(AppTheme.PRIMARY_BLACK, 0.9f);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setPreferredSize(new Dimension(0, 70));

        // Título con icono
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);

        JLabel iconLabel = new JLabel("🔧");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titlePanel.add(iconLabel);

        JLabel titleLabel = new JLabel("Gestión de Técnicos");
        titleLabel.setFont(AppTheme.TITLE_FONT);
        titleLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titlePanel.add(titleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        // Panel contador
        JPanel counterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        counterPanel.setOpaque(false);

        JLabel countLabel = new JLabel("Total de técnicos: ");
        countLabel.setFont(AppTheme.REGULAR_FONT);
        countLabel.setForeground(AppTheme.PRIMARY_WHITE);
        counterPanel.add(countLabel);

        JLabel countValueLabel = new JLabel("0");
        countValueLabel.setFont(AppTheme.SUBTITLE_FONT);
        countValueLabel.setForeground(AppTheme.PRIMARY_WHITE);
        counterPanel.add(countValueLabel);

        // Actualizar contador cuando se cargan técnicos
        new Timer(1000, e -> {
            countValueLabel.setText(String.valueOf(technicianCount));
        }).start();

        panel.add(counterPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Crea el panel de tabla.
     *
     * @return El panel de tabla
     */
    private JPanel createTablePanel() {
        RoundedPanel panel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(15, 15, 5, 15),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(AppTheme.ACCENT_RED, 1, true),
                        "Lista de Técnicos",
                        0,
                        0,
                        AppTheme.SUBTITLE_FONT,
                        AppTheme.ACCENT_RED
                )
        ));

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setFont(AppTheme.REGULAR_FONT);
        searchPanel.add(searchLabel);

        nameField = new ModernTextField("25", "Nombre");
        searchPanel.add(nameField);

        lastNameField = new ModernTextField("25", "Apellido");
        searchPanel.add(lastNameField);

        searchButton = new ModernButton("Buscar");
        searchButton.addActionListener(e -> searchTechnicians());
        searchPanel.add(searchButton);

        ModernButton resetButton = new ModernButton("Mostrar Todos");
        resetButton.addActionListener(e -> {
            nameField.setText("");
            lastNameField.setText("");
            loadData();
        });
        searchPanel.add(resetButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Modelo de tabla
        technicianTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Agregar columnas al modelo
        technicianTableModel.addColumn("ID");
        technicianTableModel.addColumn("Nombre");
        technicianTableModel.addColumn("Apellido");
        technicianTableModel.addColumn("Especialidad");
        technicianTableModel.addColumn("Estado");
        technicianTableModel.addColumn("Teléfono");
        technicianTableModel.addColumn("Email");
        technicianTableModel.addColumn("Fecha Contratación");
        technicianTableModel.addColumn("Tarifa Horaria");
        technicianTableModel.addColumn("Certificaciones");

        // Crear tabla con estilo personalizado
        technicianTable = new JTable(technicianTableModel);
        technicianTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        technicianTable.setRowHeight(30);
        technicianTable.setIntercellSpacing(new Dimension(10, 5));
        technicianTable.setShowGrid(false);
        technicianTable.setFillsViewportHeight(true);

        // Estilo del encabezado de tabla
        JTableHeader header = technicianTable.getTableHeader();
        header.setBackground(AppTheme.PRIMARY_BLACK);
        header.setForeground(AppTheme.PRIMARY_WHITE);
        header.setFont(AppTheme.SUBTITLE_FONT);
        header.setBorder(new MatteBorder(0, 0, 2, 0, AppTheme.ACCENT_RED));
        header.setReorderingAllowed(false);

        // Estilo de las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < technicianTable.getColumnCount(); i++) {
            technicianTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Agregar estilo zebra
        technicianTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        // Agregar listener de mouse a la tabla
        technicianTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = technicianTable.getSelectedRow();
                if (row >= 0) {
                    selectedTechnicianId = (int) technicianTableModel.getValueAt(row, 0);
                    controller.loadTechnicianDetails(selectedTechnicianId);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);

                    // Actualizar estado
                    updateStatus("Técnico seleccionado: ID " + selectedTechnicianId, false);
                }
            }
        });

        // Agregar tabla a un scroll pane con scrollbars personalizados
        JScrollPane scrollPane = new JScrollPane(technicianTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppTheme.PRIMARY_WHITE);

        // Personalizar scrollbars
        scrollPane.getVerticalScrollBar().setUI(createModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(createModernScrollBarUI());

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel de formulario.
     *
     * @return El panel de formulario
     */
    private JPanel createFormPanel() {
        RoundedPanel panel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 15);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(5, 15, 15, 15),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(AppTheme.ACCENT_RED, 1, true),
                        "Detalles del Técnico",
                        0,
                        0,
                        AppTheme.SUBTITLE_FONT,
                        AppTheme.ACCENT_RED
                )
        ));

        // Panel para los campos del formulario
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.weightx = 1.0;

        // Layout de dos columnas
        // Columna izquierda
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);

        addFormField(leftPanel, "Nombre:", nameField = new ModernTextField("25", "Nombre del técnico"), 0, 0);
        addFormField(leftPanel, "Apellido:", lastNameField = new ModernTextField("25", "Apellido del técnico"), 0, 1);

        // Especialidad
        JPanel specialtyPanel = new JPanel(new BorderLayout(5, 0));
        specialtyPanel.setOpaque(false);

        JLabel specialtyLabel = new JLabel("Especialidad:");
        specialtyLabel.setFont(AppTheme.REGULAR_FONT);

        specialtyComboBox = new JComboBox<>(TechnicianSpecialty.values());
        specialtyComboBox.setFont(AppTheme.REGULAR_FONT);
        specialtyComboBox.setBackground(AppTheme.PRIMARY_WHITE);
        specialtyComboBox.setBorder(BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1));

        addFormField(leftPanel, "Especialidad:", specialtyComboBox, 0, 2);

        // Estado
        statusComboBox = new JComboBox<>(TechnicianStatus.values());
        statusComboBox.setFont(AppTheme.REGULAR_FONT);
        statusComboBox.setBackground(AppTheme.PRIMARY_WHITE);
        statusComboBox.setBorder(BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY, 1));

        addFormField(leftPanel, "Estado:", statusComboBox, 0, 3);

        addFormField(leftPanel, "Teléfono:", phoneField = new ModernTextField("15", "Ej: +34 612345678"), 0, 4);

        // Columna derecha
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        addFormField(rightPanel, "Email:", emailField = new ModernTextField("20", "Ej: tecnico@ejemplo.com"), 0, 0);
        addFormField(rightPanel, "Fecha Contratación:", hireDateField = new ModernTextField("10", "yyyy-MM-dd"), 0, 1);
        addFormField(rightPanel, "Tarifa Horaria:", hourlyRateField = new ModernTextField("10", "Ej: 25.50"), 0, 2);
        addFormField(rightPanel, "Certificaciones:", certificationsField = new ModernTextField("20", "Ej: ASE, Toyota Master"), 0, 3);

        // Área de notas
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

        // Personalizar scrollbars
        notesScrollPane.getVerticalScrollBar().setUI(createModernScrollBarUI());
        notesScrollPane.getHorizontalScrollBar().setUI(createModernScrollBarUI());

        notesPanel.add(notesScrollPane, BorderLayout.CENTER);

        // Agregar columnas al panel principal del formulario
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

        // Panel de botones
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Método auxiliar para agregar un campo de formulario con etiqueta.
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
     * Crea el panel de botones.
     *
     * @return El panel de botones
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        addButton = new ModernButton("Agregar");
        addButton.addActionListener(e -> addTechnician());
        panel.add(addButton);

        updateButton = new ModernButton("Actualizar");
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> updateTechnician());
        panel.add(updateButton);

        deleteButton = new ModernButton("Eliminar");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> deleteTechnician());
        panel.add(deleteButton);

        clearButton = new ModernButton("Limpiar");
        clearButton.addActionListener(e -> clearForm());
        panel.add(clearButton);

        return panel;
    }

    /**
     * Crea el panel de estado.
     *
     * @return El panel de estado
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
     * Carga datos en la tabla.
     */
    public void loadData() {
        updateStatus("Cargando técnicos...", true);
        controller.loadTechnicians();
    }

    /**
     * Agrega un nuevo técnico.
     */
    private void addTechnician() {
        try {
            updateStatus("Agregando técnico...", true);

            // Crear un nuevo objeto Technician desde los datos del formulario
            Technician technician = getTechnicianFromForm();

            // Llamar al controlador para agregar el técnico
            controller.addTechnician(technician);

            // Limpiar el formulario
            clearForm();

            updateStatus("Técnico agregado correctamente", false);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error adding technician", ex);
            JOptionPane.showMessageDialog(this, "Error al agregar técnico: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al agregar técnico", false);
        }
    }

    /**
     * Actualiza un técnico existente.
     */
    private void updateTechnician() {
        try {
            updateStatus("Actualizando técnico...", true);

            // Crear un objeto Technician desde los datos del formulario
            Technician technician = getTechnicianFromForm();
            technician.setTechnicianId(selectedTechnicianId);

            // Llamar al controlador para actualizar el técnico
            controller.updateTechnician(technician);

            updateStatus("Técnico actualizado correctamente", false);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating technician", ex);
            JOptionPane.showMessageDialog(this, "Error al actualizar técnico: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al actualizar técnico", false);
        }
    }

    /**
     * Elimina un técnico.
     */
    private void deleteTechnician() {
        try {
            // Confirmar eliminación con un diálogo personalizado
            int option = JOptionPane.showOptionDialog(
                    this,
                    "¿Está seguro que desea eliminar este técnico?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    new Object[]{"Eliminar", "Cancelar"},
                    "Cancelar"
            );

            if (option == JOptionPane.YES_OPTION) {
                updateStatus("Eliminando técnico...", true);

                // Llamar al controlador para eliminar el técnico
                controller.deleteTechnician(selectedTechnicianId);

                // Limpiar el formulario
                clearForm();

                updateStatus("Técnico eliminado correctamente", false);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error deleting technician", ex);
            JOptionPane.showMessageDialog(this, "Error al eliminar técnico: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al eliminar técnico", false);
        }
    }

    /**
     * Busca técnicos por nombre.
     */
    private void searchTechnicians() {
        try {
            String name = nameField.getText().trim();
            String lastName = lastNameField.getText().trim();

            updateStatus("Buscando técnicos...", true);

            if (name.isEmpty() && lastName.isEmpty()) {
                // Si ambos campos están vacíos, cargar todos los técnicos
                controller.loadTechnicians();
            } else {
                // Buscar por nombre o apellido
                String searchTerm = name.isEmpty() ? lastName : name;
                controller.searchTechniciansByName(searchTerm);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error searching technicians", ex);
            JOptionPane.showMessageDialog(this, "Error al buscar técnicos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error al buscar técnicos", false);
        }
    }

    /**
     * Limpia el formulario.
     */
    private void clearForm() {
        // Resetear campos del formulario
        nameField.setText("");
        lastNameField.setText("");
        specialtyComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        phoneField.setText("");
        emailField.setText("");
        hireDateField.setText(DATE_FORMAT.format(new Date()));
        hourlyRateField.setText("");
        certificationsField.setText("");
        notesArea.setText("");

        // Resetear ID seleccionado
        selectedTechnicianId = 0;

        // Deshabilitar botones
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        updateStatus("Formulario limpiado", false);
    }

    /**
     * Crea un objeto Technician a partir de los datos del formulario.
     *
     * @return Un objeto Technician con los datos del formulario
     * @throws ParseException Si ocurre un error al analizar una fecha
     */
    private Technician getTechnicianFromForm() throws ParseException {
        Technician technician = new Technician();

        // Obtener el nombre
        String name = nameField.getText().trim();
        if (!name.isEmpty()) {
            technician.setName(name);
        } else {
            throw new IllegalArgumentException("Por favor ingrese un nombre.");
        }

        // Obtener el apellido
        String lastName = lastNameField.getText().trim();
        if (!lastName.isEmpty()) {
            technician.setLastName(lastName);
        } else {
            throw new IllegalArgumentException("Por favor ingrese un apellido.");
        }

        // Obtener la especialidad
        TechnicianSpecialty specialty = (TechnicianSpecialty) specialtyComboBox.getSelectedItem();
        technician.setSpecialty(specialty);

        // Obtener el estado
        TechnicianStatus status = (TechnicianStatus) statusComboBox.getSelectedItem();
        technician.setStatus(status.name());

        // Obtener el teléfono
        technician.setPhone(phoneField.getText().trim());

        // Obtener el email
        technician.setEmail(emailField.getText().trim());

        // Obtener la fecha de contratación
        String hireDateStr = hireDateField.getText().trim();
        if (!hireDateStr.isEmpty()) {
            technician.setHireDate(DATE_FORMAT.parse(hireDateStr));
        } else {
            technician.setHireDate(new Date()); // Por defecto fecha actual
        }

        // Obtener la tarifa horaria
        String hourlyRateStr = hourlyRateField.getText().trim();
        if (!hourlyRateStr.isEmpty()) {
            try {
                double hourlyRate = Double.parseDouble(hourlyRateStr);
                if (hourlyRate <= 0) {
                    throw new IllegalArgumentException("La tarifa horaria debe ser mayor que cero.");
                }
                technician.setHourlyRate(hourlyRate);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Por favor ingrese una tarifa horaria válida.");
            }
        } else {
            throw new IllegalArgumentException("Por favor ingrese una tarifa horaria.");
        }

        // Obtener las certificaciones
        technician.setCertifications(certificationsField.getText().trim());

        // Obtener las notas
        technician.setNotes(notesArea.getText().trim());

        return technician;
    }

    /**
     * Actualiza la tabla de técnicos con los técnicos proporcionados.
     *
     * @param technicians La lista de técnicos a mostrar
     */
    public void updateTechnicianTable(List<Technician> technicians) {
        // Limpiar la tabla
        technicianTableModel.setRowCount(0);

        // Actualizar contador de técnicos
        technicianCount = technicians.size();

        // Agregar técnicos a la tabla
        for (Technician technician : technicians) {
            Vector<Object> row = new Vector<>();
            row.add(technician.getTechnicianId());
            row.add(technician.getName());
            row.add(technician.getLastName());
            row.add(technician.getSpecialty());
            row.add(technician.getStatus());
            row.add(technician.getPhone());
            row.add(technician.getEmail());
            row.add(technician.getHireDate() != null ? DATE_FORMAT.format(technician.getHireDate()) : "");
            row.add(technician.getHourlyRate());
            row.add(technician.getCertifications());

            technicianTableModel.addRow(row);
        }

        updateStatus("Se encontraron " + technicians.size() + " técnicos", false);
    }

    /**
     * Rellena el formulario con el técnico proporcionado.
     *
     * @param technician El técnico a mostrar
     */
    public void populateTechnicianForm(Technician technician) {
        nameField.setText(technician.getName());
        lastNameField.setText(technician.getLastName());

        // Establecer la especialidad
        specialtyComboBox.setSelectedItem(technician.getSpecialty());

        // Establecer el estado
        for (TechnicianStatus status : TechnicianStatus.values()) {
            if (status.name().equals(technician.getStatus())) {
                statusComboBox.setSelectedItem(status);
                break;
            }
        }

        phoneField.setText(technician.getPhone());
        emailField.setText(technician.getEmail());

        if (technician.getHireDate() != null) {
            hireDateField.setText(DATE_FORMAT.format(technician.getHireDate()));
        } else {
            hireDateField.setText("");
        }

        hourlyRateField.setText(String.valueOf(technician.getHourlyRate()));
        certificationsField.setText(technician.getCertifications());
        notesArea.setText(technician.getNotes());

        updateStatus("Técnico cargado: " + technician.getName() + " " + technician.getLastName(), false);
    }

    /**
     * Método auxiliar para establecer el color del divisor de un JSplitPane.
     */
    private static void setDividerColor(JSplitPane splitPane, Color color) {
        splitPane.setBorder(null);
        splitPane.setUI(new javax.swing.plaf.basic.BasicSplitPaneUI() {
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
