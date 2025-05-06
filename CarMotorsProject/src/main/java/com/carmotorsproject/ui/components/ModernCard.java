package com.carmotorsproject.ui.components;

import com.carmotorsproject.ui.theme.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Tarjeta moderna para mostrar módulos en el panel de bienvenida.
 */
public class ModernCard extends JPanel {

    private final Runnable action;
    private Color backgroundColor = AppTheme.PRIMARY_WHITE;
    private Color hoverColor = new Color(245, 245, 245);
    private boolean isHovered = false;

    /**
     * Constructor de la tarjeta moderna.
     *
     * @param title Título de la tarjeta
     * @param description Descripción de la tarjeta
     * @param iconPath Ruta al icono
     * @param action Acción a ejecutar al hacer clic
     */
    public ModernCard(String title, String description, String iconPath, Runnable action) {
        this.action = action;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.ACCENT_LIGHT, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        setBackground(backgroundColor);

        // Panel de icono
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setOpaque(false);

        // Cargar icono de forma segura
        Icon icon = loadIconSafely(iconPath);
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            iconPanel.add(iconLabel);
        } else {
            // Icono alternativo si no se puede cargar
            JLabel altIconLabel = new JLabel("📦");
            altIconLabel.setFont(new Font("Dialog", Font.BOLD, 36));
            altIconLabel.setForeground(AppTheme.PRIMARY_RED);
            iconPanel.add(altIconLabel);
        }

        // Panel de texto
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.SUBTITLE_FONT);
        titleLabel.setForeground(AppTheme.PRIMARY_BLACK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(AppTheme.SMALL_FONT);
        descLabel.setForeground(AppTheme.ACCENT_GRAY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(descLabel);

        // Añadir componentes
        add(iconPanel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);

        // Añadir efectos de hover y clic
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                setBackground(hoverColor);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                setBackground(backgroundColor);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (action != null) {
                    action.run();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo con esquinas redondeadas
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        // Dibujar borde si está hover
        if (isHovered) {
            g2.setColor(AppTheme.PRIMARY_RED);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
        }

        g2.dispose();

        super.paintComponent(g);
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
