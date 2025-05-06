package com.carmotorsproject.campaigns.views;

import com.carmotorsproject.campaigns.controller.CampaignAppointmentController;
import com.carmotorsproject.campaigns.model.CampaignAppointment;
import com.carmotorsproject.campaigns.model.CampaignStatus;
import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.ModernTextField;
import com.carmotorsproject.ui.components.RoundedPanel;
import com.carmotorsproject.ui.components.TransparentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * View for managing campaign appointments.
 */
public class CampaignAppointmentView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(CampaignAppointmentView.class.getName());
    private CampaignAppointmentController controller;
    private DefaultTableModel appointmentTableModel;

    // UI Components
    private JTable tblAppointments;
    private ModernTextField txtCampaignId;
    private ModernTextField txtCustomerId;
    private ModernTextField txtVehicleId;
    private JFormattedTextField txtAppointmentDate;
    private JComboBox<String> cmbStatus;
    private JTextArea txtNotes;
    private ModernButton btnSave;
    private ModernButton btnUpdate;
    private ModernButton btnDelete;
    private ModernButton btnClear;
    private ModernTextField txtSearch;
    private ModernButton btnSearch;

    // Current appointment ID
    private int currentAppointmentId = -1;

    // Date format
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Creates new form CampaignAppointmentView
     */
    public CampaignAppointmentView() {
        initComponents();
        setupTable();
        setupListeners();
    }

    /**
     * Sets up the appointment table
     */
    private void setupTable() {
        String[] columnNames = {"ID", "Campaña", "Cliente", "Vehículo", "Fecha", "Estado", "Notas"};
        appointmentTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblAppointments.setModel(appointmentTableModel);

        // Style the table
        tblAppointments.setFont(AppTheme.REGULAR_FONT);
        tblAppointments.getTableHeader().setFont(AppTheme.TABLE_HEADER_FONT);
        tblAppointments.getTableHeader().setBackground(AppTheme.PRIMARY_RED);
        tblAppointments.getTableHeader().setForeground(Color.WHITE);
        tblAppointments.setRowHeight(30);
        tblAppointments.setShowGrid(true);
        tblAppointments.setGridColor(AppTheme.LIGHT_GRAY);
        tblAppointments.setSelectionBackground(AppTheme.SECONDARY_RED);
        tblAppointments.setSelectionForeground(Color.WHITE);
    }

    /**
     * Sets the controller for this view
     *
     * @param controller The campaign appointment controller
     */
    public void setController(CampaignAppointmentController controller) {
        this.controller = controller;
        LOGGER.log(Level.INFO, "Controller set for CampaignAppointmentView");
    }

    /**
     * Updates the appointment table with the provided list of appointments
     *
     * @param appointments List of appointments to display
     */
    public void updateAppointmentTable(List<CampaignAppointment> appointments) {
        // Clear existing data
        appointmentTableModel.setRowCount(0);

        // Add new data
        for (CampaignAppointment appointment : appointments) {
            Object[] row = {
                    appointment.getCampaignAppointmentId(),
                    appointment.getCampaignId(), // Would typically show campaign name
                    appointment.getCustomerId(), // Would typically show customer name
                    appointment.getVehicleId(), // Would typically show vehicle info
                    appointment.getAppointmentDate() != null ? dateFormat.format(appointment.getAppointmentDate()) : "",
                    appointment.getStatus(),
                    appointment.getNotes()
            };
            appointmentTableModel.addRow(row);
        }

        LOGGER.log(Level.INFO, "Updated appointment table with {0} appointments", appointments.size());
    }

    /**
     * Shows an error message
     *
     * @param message The error message to display
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        LOGGER.log(Level.WARNING, "Error shown: {0}", message);
    }

    /**
     * Shows validation errors
     *
     * @param errors Map of field names to error messages
     */
    public void showValidationErrors(Map<String, String> errors) {
        StringBuilder errorMessage = new StringBuilder("Por favor corrija los siguientes errores:\n");

        for (Map.Entry<String, String> entry : errors.entrySet()) {
            errorMessage.append("- ").append(entry.getValue()).append("\n");
        }

        JOptionPane.showMessageDialog(this, errorMessage.toString(), "Errores de Validación", JOptionPane.WARNING_MESSAGE);
        LOGGER.log(Level.WARNING, "Validation errors shown: {0}", errors.size());
    }

    /**
     * Shows a success message
     *
     * @param message The success message to display
     */
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        LOGGER.log(Level.INFO, "Success message shown: {0}", message);
    }

    /**
     * Populates the form with the details of an appointment
     *
     * @param appointment The appointment to display
     */
    public void populateAppointmentForm(CampaignAppointment appointment) {
        currentAppointmentId = appointment.getCampaignAppointmentId();

        txtCampaignId.setText(String.valueOf(appointment.getCampaignId()));
        txtCustomerId.setText(String.valueOf(appointment.getCustomerId()));
        txtVehicleId.setText(String.valueOf(appointment.getVehicleId()));

        if (appointment.getAppointmentDate() != null) {
            txtAppointmentDate.setValue(appointment.getAppointmentDate());
        } else {
            txtAppointmentDate.setValue(new Date());
        }

        cmbStatus.setSelectedItem(appointment.getStatus());
        txtNotes.setText(appointment.getNotes());

        btnSave.setEnabled(false);
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);
    }

    /**
     * Clears the form fields
     */
    private void clearForm() {
        currentAppointmentId = -1;

        txtCampaignId.setText("");
        txtCustomerId.setText("");
        txtVehicleId.setText("");
        txtAppointmentDate.setValue(new Date());
        cmbStatus.setSelectedIndex(0);
        txtNotes.setText("");

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    /**
     * Gets an appointment object from the form fields
     *
     * @return A campaign appointment object
     */
    private CampaignAppointment getAppointmentFromForm() {
        CampaignAppointment appointment = new CampaignAppointment();

        if (currentAppointmentId > 0) {
            appointment.setCampaignAppointmentId(currentAppointmentId);
        }

        try {
            appointment.setCampaignId(Integer.parseInt(txtCampaignId.getText().trim()));
            appointment.setCustomerId(Integer.parseInt(txtCustomerId.getText().trim()));
            appointment.setVehicleId(Integer.parseInt(txtVehicleId.getText().trim()));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid number format in form", e);
            return null;
        }

        appointment.setAppointmentDate((Date) txtAppointmentDate.getValue());
        appointment.setStatus(CampaignStatus.valueOf((String) cmbStatus.getSelectedItem()));
        appointment.setNotes(txtNotes.getText().trim());

        return appointment;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
        // Apply theme
        AppTheme.applyTheme();

        setTitle("Gestión de Citas de Campaña");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new RoundedPanel(AppTheme.PRIMARY_WHITE, 1.0f, 0);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BorderLayout(10, 10));
        setContentPane(mainPanel);

        // Form panel
        RoundedPanel formPanel = new RoundedPanel(Color.WHITE, 1.0f, AppTheme.BORDER_RADIUS);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.MEDIUM_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Campaign ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblCampaignId = new JLabel("ID Campaña:");
        lblCampaignId.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblCampaignId, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtCampaignId = new ModernTextField("ID Campaña");
        formPanel.add(txtCampaignId, gbc);

        // Customer ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel lblCustomerId = new JLabel("ID Cliente:");
        lblCustomerId.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblCustomerId, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtCustomerId = new ModernTextField("ID Cliente");
        formPanel.add(txtCustomerId, gbc);

        // Vehicle ID
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel lblVehicleId = new JLabel("ID Vehículo:");
        lblVehicleId.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblVehicleId, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtVehicleId = new ModernTextField("ID Vehículo");
        formPanel.add(txtVehicleId, gbc);

        // Appointment Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel lblAppointmentDate = new JLabel("Fecha (yyyy-MM-dd):");
        lblAppointmentDate.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblAppointmentDate, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtAppointmentDate = new JFormattedTextField(dateFormat);
        txtAppointmentDate.setValue(new Date());
        txtAppointmentDate.setFont(AppTheme.REGULAR_FONT);
        txtAppointmentDate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.MEDIUM_GRAY),
                new EmptyBorder(5, 5, 5, 5)));
        formPanel.add(txtAppointmentDate, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel lblStatus = new JLabel("Estado:");
        lblStatus.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblStatus, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        String[] statusOptions = {"PROGRAMADA", "CONFIRMADA", "CANCELADA", "COMPLETADA"};
        cmbStatus = new JComboBox<>(statusOptions);
        cmbStatus.setFont(AppTheme.REGULAR_FONT);
        cmbStatus.setBackground(Color.WHITE);
        formPanel.add(cmbStatus, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel lblNotes = new JLabel("Notas:");
        lblNotes.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblNotes, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNotes = new JTextArea(3, 20);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        txtNotes.setFont(AppTheme.REGULAR_FONT);
        txtNotes.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.MEDIUM_GRAY),
                new EmptyBorder(5, 5, 5, 5)));
        JScrollPane notesScrollPane = new JScrollPane(txtNotes);
        formPanel.add(notesScrollPane, gbc);

        // Search panel
        TransparentPanel searchPanel = new TransparentPanel(AppTheme.LIGHT_GRAY, 0.3f);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setFont(AppTheme.REGULAR_FONT);
        searchPanel.add(searchLabel);

        txtSearch = new ModernTextField("Buscar");
        txtSearch.setPlaceholder("Buscar por cliente o campaña");
        searchPanel.add(txtSearch);

        btnSearch = new ModernButton("Buscar", "primary");
        searchPanel.add(btnSearch);

        // Table
        tblAppointments = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(tblAppointments);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Button panel
        TransparentPanel buttonPanel = new TransparentPanel(AppTheme.LIGHT_GRAY, 0.3f);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnSave = new ModernButton("Guardar", "success");
        btnUpdate = new ModernButton("Actualizar", "primary");
        btnDelete = new ModernButton("Eliminar", "danger");
        btnClear = new ModernButton("Limpiar", "light");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // Initially disable update and delete buttons
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        // Add components to main panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(tableScrollPane, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
    }

    /**
     * Sets up the event listeners for the UI components
     */
    private void setupListeners() {
        // Table selection listener
        tblAppointments.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblAppointments.getSelectedRow();
                if (row >= 0) {
                    int appointmentId = (int) appointmentTableModel.getValueAt(row, 0);
                    controller.loadAppointmentsByCampaign(appointmentId);
                }
            }
        });

        // Save button
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CampaignAppointment appointment = getAppointmentFromForm();
                if (appointment != null) {
                    controller.saveAppointment(appointment);
                }
            }
        });

        // Update button
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CampaignAppointment appointment = getAppointmentFromForm();
                if (appointment != null) {
                    controller.updateAppointment(appointment);
                }
            }
        });

        // Delete button
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentAppointmentId > 0) {
                    int option = JOptionPane.showConfirmDialog(
                            CampaignAppointmentView.this,
                            "¿Está seguro que desea eliminar esta cita?",
                            "Confirmar Eliminación",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        controller.deleteAppointment(currentAppointmentId);
                        clearForm();
                    }
                }
            }
        });

        // Clear button
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // Search button
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = txtSearch.getText().trim();
                if (!searchTerm.isEmpty()) {
                    controller.searchAppointments(searchTerm);
                } else {
                    controller.loadAllAppointments();
                }
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the look and feel */
        AppTheme.applyTheme();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CampaignAppointmentView().setVisible(true);
            }
        });
    }
}
