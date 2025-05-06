package com.carmotorsproject.ui.components;

import com.carmotorsproject.ui.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Encabezado moderno para la aplicación.
 */
public class ModernHeader extends JPanel {

    private JLabel titleLabel;
    private JLabel dateTimeLabel;
    private JLabel userLabel;
    private JLabel dbStatusLabel;
    private Timer clockTimer;

    /**
     * Constructor del encabezado moderno.
     *
     * @param title Título a mostrar
     */
    public ModernHeader(String title) {
        setLayout(new BorderLayout());
        setBackground(AppTheme.PRIMARY_BLACK);
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, AppTheme.PRIMARY_RED));
        setPreferredSize(new Dimension(0, 60));

        // Panel izquierdo para título
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);

        // Botón de menú hamburguesa
        JLabel menuButton = new JLabel("☰");
        menuButton.setForeground(AppTheme.PRIMARY_WHITE);
        menuButton.setFont(new Font("Arial", Font.BOLD, 24));
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                firePropertyChange("toggleMenu", false, true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                menuButton.setForeground(AppTheme.PRIMARY_RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuButton.setForeground(AppTheme.PRIMARY_WHITE);
            }
        });
        leftPanel.add(menuButton);

        // Título
        titleLabel = new JLabel(title);
        titleLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titleLabel.setFont(AppTheme.TITLE_FONT);
        leftPanel.add(titleLabel);

        add(leftPanel, BorderLayout.WEST);

        // Panel derecho para información y acciones
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        // Estado de la base de datos
        dbStatusLabel = new JLabel();
        dbStatusLabel.setForeground(AppTheme.PRIMARY_WHITE);
        dbStatusLabel.setFont(AppTheme.SMALL_FONT);
        Icon dbDisconnectedIcon = loadIconSafely("/icons/database_disconnected.png");
        dbStatusLabel.setIcon(dbDisconnectedIcon);
        rightPanel.add(dbStatusLabel);

        // Fecha y hora
        dateTimeLabel = new JLabel();
        dateTimeLabel.setForeground(AppTheme.PRIMARY_WHITE);
        dateTimeLabel.setFont(AppTheme.SMALL_FONT);
        updateDateTime();
        rightPanel.add(dateTimeLabel);

        // Iniciar timer para actualizar la hora
        clockTimer = new Timer(1000, e -> updateDateTime());
        clockTimer.start();

        // Usuario
        userLabel = new JLabel("Admin");
        userLabel.setForeground(AppTheme.PRIMARY_WHITE);
        userLabel.setFont(AppTheme.REGULAR_FONT);
        Icon userIcon = loadIconSafely("/icons/user.png");
        userLabel.setIcon(userIcon);
        rightPanel.add(userLabel);

        add(rightPanel, BorderLayout.EAST);
    }

    /**
     * Actualiza la fecha y hora mostradas.
     */
    private void updateDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateTimeLabel.setText(sdf.format(new Date()));
    }

    /**
     * Establece el título del encabezado.
     *
     * @param title Nuevo título
     */
    public void setHeaderTitle(String title) {
        titleLabel.setText(title);
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username Nombre de usuario
     */
    public void setUsername(String username) {
        userLabel.setText(username);
    }

    /**
     * Establece el estado de la base de datos.
     *
     * @param connected true si está conectado, false en caso contrario
     * @param tooltip Texto de información adicional
     */
    public void setDatabaseStatus(boolean connected, String tooltip) {
        if (connected) {
            dbStatusLabel.setIcon(loadIconSafely("/icons/database_connected.png"));
            dbStatusLabel.setForeground(AppTheme.SUCCESS_COLOR);
            dbStatusLabel.setText("BD: Conectada");
        } else {
            dbStatusLabel.setIcon(loadIconSafely("/icons/database_disconnected.png"));
            dbStatusLabel.setForeground(AppTheme.WARNING_COLOR);
            dbStatusLabel.setText("BD: Offline");
        }

        dbStatusLabel.setToolTipText(tooltip);
    }

    /**
     * Detiene el timer del reloj cuando ya no se necesita.
     */
    public void stopClock() {
        if (clockTimer != null && clockTimer.isRunning()) {
            clockTimer.stop();
        }
    }

    /**
     * Carga un icono de forma segura, devolviendo null si no se encuentra.
     *
     * @param path Ruta al icono
     * @return El icono cargado o null si no se encuentra
     */
    private Icon loadIconSafely(String path) {
        java.net.URL iconURL = getClass().getResource(path);
        if (iconURL != null) {
            return new ImageIcon(iconURL);
        } else {
            System.out.println("Advertencia: No se pudo cargar el icono: " + path);
            return null;
        }
    }
}
