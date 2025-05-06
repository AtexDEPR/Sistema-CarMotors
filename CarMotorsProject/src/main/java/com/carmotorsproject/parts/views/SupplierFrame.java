package com.carmotorsproject.parts.views;

import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.ui.animation.AnimationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Frame para mostrar la vista de proveedores en una ventana independiente.
 */
public class SupplierFrame extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(SupplierFrame.class.getName());
    private static final String FRAME_TITLE = "Gestión de Proveedores";
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 700;

    private SupplierView supplierView;

    /**
     * Constructor que inicializa el frame.
     */
    public SupplierFrame() {
        initComponents();
    }

    /**
     * Inicializa los componentes del frame.
     */
    private void initComponents() {
        // Configurar propiedades del frame
        setTitle(FRAME_TITLE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null); // Centrar en pantalla
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear la vista de proveedores
        try {
            supplierView = new SupplierView();
            add(supplierView, BorderLayout.CENTER);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar la vista de proveedores", e);
            JPanel errorPanel = new JPanel(new BorderLayout());
            errorPanel.setBackground(AppTheme.PRIMARY_WHITE);

            JLabel errorLabel = new JLabel("Error al cargar la vista de proveedores: " + e.getMessage());
            errorLabel.setForeground(AppTheme.ERROR_COLOR);
            errorLabel.setFont(AppTheme.SUBTITLE_FONT);
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            errorLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            errorPanel.add(errorLabel, BorderLayout.CENTER);
            add(errorPanel, BorderLayout.CENTER);
        }

        // Agregar listener para manejar el cierre de la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LOGGER.info("Cerrando ventana de proveedores");
                dispose();
            }
        });
    }

    /**
     * Muestra el frame con una animación de fade-in.
     */
    public void showFrame() {
        setVisible(true);
        AnimationManager.fadeIn(getRootPane(), 300);
    }
}
