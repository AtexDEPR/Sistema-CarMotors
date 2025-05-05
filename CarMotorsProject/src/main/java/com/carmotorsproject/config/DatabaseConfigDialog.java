package com.carmotorsproject.config;

import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.ModernTextField;
import com.carmotorsproject.ui.theme.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Diálogo para configurar la conexión a la base de datos.
 */
public class DatabaseConfigDialog extends JDialog {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConfigDialog.class.getName());

    private ModernTextField txtUrl;
    private ModernTextField txtUser;
    private JPasswordField txtPassword;
    private JCheckBox chkOfflineMode;
    private JLabel statusLabel;

    /**
     * Constructor del diálogo.
     *
     * @param parent Frame padre
     */
    public DatabaseConfigDialog(Frame parent) {
        super(parent, "Configuración de Base de Datos", true);
        initComponents();
        loadCurrentConfig();
    }

    /**
     * Inicializa los componentes del diálogo.
     */
    private void initComponents() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(AppTheme.PRIMARY_WHITE);

        // Panel de título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Configuración de Conexión a Base de Datos");
        titleLabel.setFont(AppTheme.TITLE_FONT);
        titleLabel.setForeground(AppTheme.PRIMARY_BLACK);
        titlePanel.add(titleLabel);

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // URL
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUrl = new JLabel("URL de la Base de Datos:");
        lblUrl.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblUrl, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        txtUrl = new ModernTextField();
        txtUrl.setPlaceholder("jdbc:mysql://localhost:3306/carmotors");
        formPanel.add(txtUrl, gbc);

        // Usuario
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblUser, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        txtUser = new ModernTextField();
        txtUser.setPlaceholder("root");
        formPanel.add(txtUser, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        txtPassword = new JPasswordField();
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_GRAY),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        txtPassword.setFont(AppTheme.REGULAR_FONT);
        formPanel.add(txtPassword, gbc);

        // Modo offline
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        chkOfflineMode = new JCheckBox("Habilitar modo offline (sin conexión a base de datos)");
        chkOfflineMode.setFont(AppTheme.REGULAR_FONT);
        chkOfflineMode.setOpaque(false);
        chkOfflineMode.addActionListener(this::toggleOfflineMode);
        formPanel.add(chkOfflineMode, gbc);

        // Panel de estado
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        statusLabel = new JLabel(" ");
        statusLabel.setFont(AppTheme.REGULAR_FONT);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(statusLabel, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        ModernButton btnTest = new ModernButton("Probar Conexión");
        btnTest.setColors(AppTheme.PRIMARY_BLACK, AppTheme.SECONDARY_BLACK,
                AppTheme.SECONDARY_BLACK.darker(), AppTheme.PRIMARY_WHITE);
        btnTest.addActionListener(this::testConnection);
        buttonPanel.add(btnTest);

        ModernButton btnSave = new ModernButton("Guardar");
        btnSave.setColors(AppTheme.PRIMARY_RED, AppTheme.SECONDARY_RED,
                AppTheme.SECONDARY_RED.darker(), AppTheme.PRIMARY_WHITE);
        btnSave.addActionListener(this::saveConfig);
        buttonPanel.add(btnSave);

        ModernButton btnCancel = new ModernButton("Cancelar");
        btnCancel.setColors(AppTheme.ACCENT_GRAY, AppTheme.ACCENT_GRAY.darker(),
                AppTheme.ACCENT_GRAY.darker().darker(), AppTheme.PRIMARY_WHITE);
        btnCancel.addActionListener(e -> dispose());
        buttonPanel.add(btnCancel);

        // Añadir paneles al panel principal
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    /**
     * Carga la configuración actual de la base de datos.
     */
    private void loadCurrentConfig() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();

        // Obtener valores actuales mediante reflexión o métodos getter
        // En una implementación real, estos métodos deberían existir en DatabaseConnection
        // Aquí usamos valores por defecto
        txtUrl.setText("jdbc:mysql://localhost:3306/carmotors");
        txtUser.setText("root");
        txtPassword.setText("");

        chkOfflineMode.setSelected(dbConnection.isOfflineMode());
        toggleOfflineFields();
    }

    /**
     * Maneja el evento de cambio del checkbox de modo offline.
     *
     * @param e Evento de acción
     */
    private void toggleOfflineMode(ActionEvent e) {
        toggleOfflineFields();
    }

    /**
     * Habilita o deshabilita los campos según el estado del modo offline.
     */
    private void toggleOfflineFields() {
        boolean offline = chkOfflineMode.isSelected();

        txtUrl.setEnabled(!offline);
        txtUser.setEnabled(!offline);
        txtPassword.setEnabled(!offline);

        if (offline) {
            statusLabel.setText("Modo offline habilitado. No se requiere conexión a la base de datos.");
            statusLabel.setForeground(AppTheme.WARNING_COLOR);
        } else {
            statusLabel.setText(" ");
        }
    }

    /**
     * Prueba la conexión a la base de datos con los parámetros actuales.
     *
     * @param e Evento de acción
     */
    private void testConnection(ActionEvent e) {
        if (chkOfflineMode.isSelected()) {
            statusLabel.setText("Modo offline habilitado. No se puede probar la conexión.");
            statusLabel.setForeground(AppTheme.WARNING_COLOR);
            return;
        }

        String url = txtUrl.getText().trim();
        String user = txtUser.getText().trim();
        String password = new String(txtPassword.getPassword());

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        statusLabel.setText("Probando conexión...");
        statusLabel.setForeground(AppTheme.PRIMARY_BLACK);

        // Ejecutar prueba en un hilo separado para no bloquear la UI
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Connection conn = null;
                try {
                    // Intentar establecer conexión con los parámetros proporcionados
                    conn = DriverManager.getConnection(url, user, password);
                    return true;
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error al probar la conexión: " + ex.getMessage(), ex);
                    return false;
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException ex) {
                            LOGGER.log(Level.WARNING, "Error al cerrar la conexión", ex);
                        }
                    }
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        statusLabel.setText("Conexión exitosa a la base de datos.");
                        statusLabel.setForeground(AppTheme.SUCCESS_COLOR);
                    } else {
                        statusLabel.setText("Error al conectar a la base de datos. Verifique los parámetros.");
                        statusLabel.setForeground(AppTheme.ERROR_COLOR);
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Error al probar la conexión: " + ex.getMessage());
                    statusLabel.setForeground(AppTheme.ERROR_COLOR);
                }
                setCursor(Cursor.getDefaultCursor());
            }
        };

        worker.execute();
    }

    /**
     * Guarda la configuración de la base de datos.
     *
     * @param e Evento de acción
     */
    private void saveConfig(ActionEvent e) {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();

        if (chkOfflineMode.isSelected()) {
            dbConnection.setOfflineMode(true);
            statusLabel.setText("Modo offline habilitado correctamente.");
            statusLabel.setForeground(AppTheme.SUCCESS_COLOR);
        } else {
            String url = txtUrl.getText().trim();
            String user = txtUser.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (url.isEmpty() || user.isEmpty()) {
                statusLabel.setText("URL y usuario son campos obligatorios.");
                statusLabel.setForeground(AppTheme.ERROR_COLOR);
                return;
            }

            // Actualizar parámetros de conexión
            dbConnection.updateConnectionParams(url, user, password);

            // Probar la conexión con los nuevos parámetros
            boolean success = dbConnection.testConnection();

            if (success) {
                statusLabel.setText("Configuración guardada y conexión exitosa.");
                statusLabel.setForeground(AppTheme.SUCCESS_COLOR);

                // Cerrar el diálogo después de un breve retraso
                Timer timer = new Timer(1500, event -> dispose());
                timer.setRepeats(false);
                timer.start();
            } else {
                statusLabel.setText("Configuración guardada pero la conexión falló.");
                statusLabel.setForeground(AppTheme.WARNING_COLOR);
            }
        }
    }
}
