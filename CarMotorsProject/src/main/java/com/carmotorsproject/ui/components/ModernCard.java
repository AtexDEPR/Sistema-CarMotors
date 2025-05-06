package com.carmotorsproject.ui.components;

import com.carmotorsproject.ui.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Tarjeta moderna para el menú principal con efectos de hover.
 */
public class ModernCard extends JPanel {

    private String title;
    private String description;
    private ImageIcon icon;
    private Color backgroundColor = AppTheme.PRIMARY_BLACK;
    private Color hoverColor = AppTheme.SECONDARY_BLACK;
    private Color textColor = AppTheme.PRIMARY_WHITE;
    private boolean isHovered = false;
    private Runnable clickAction;

    /**
     * Constructor para tarjeta de menú.
     *
     * @param title Título de la tarjeta
     * @param description Descripción corta
     * @param iconPath Ruta al icono
     * @param clickAction Acción al hacer clic
     */
    public ModernCard(String title, String description, String iconPath, Runnable clickAction) {
        this.title = title;
        this.description = description;
        this.clickAction = clickAction;

        // Cargar icono si se proporciona una ruta
        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                this.icon = new ImageIcon(getClass().getResource(iconPath));
            } catch (Exception e) {
                System.err.println("Error cargando icono: " + e.getMessage());
            }
        }

        setup();
    }

    private void setup() {
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(200, 150));

        // Añadir listeners para efectos de hover y clic
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickAction != null) {
                    clickAction.run();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo redondeado
        g2.setColor(isHovered ? hoverColor : backgroundColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(),
                AppTheme.BORDER_RADIUS, AppTheme.BORDER_RADIUS));

        // Dibujar borde rojo cuando está hover
        if (isHovered) {
            g2.setColor(AppTheme.PRIMARY_RED);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2,
                    AppTheme.BORDER_RADIUS, AppTheme.BORDER_RADIUS));
        }

        // Dibujar icono si existe
        if (icon != null) {
            int iconSize = 48;
            int iconX = (getWidth() - iconSize) / 2;
            int iconY = 20;
            g2.drawImage(icon.getImage(), iconX, iconY, iconSize, iconSize, null);
        }

        // Dibujar título
        g2.setColor(textColor);
        g2.setFont(AppTheme.HEADER_FONT);
        FontMetrics fmTitle = g2.getFontMetrics();
        int titleX = (getWidth() - fmTitle.stringWidth(title)) / 2;
        int titleY = icon != null ? 85 : 50;
        g2.drawString(title, titleX, titleY);

        // Dibujar descripción
        g2.setFont(AppTheme.SMALL_FONT);
        FontMetrics fmDesc = g2.getFontMetrics();
        int descX = AppTheme.PADDING_MEDIUM;
        int descY = titleY + fmTitle.getHeight() + 5;

        // Dividir descripción en múltiples líneas si es necesario
        if (description != null && !description.isEmpty()) {
            int maxWidth = getWidth() - (2 * AppTheme.PADDING_MEDIUM);
            drawMultilineString(g2, description, descX, descY, maxWidth);
        }

        g2.dispose();
    }

    /**
     * Dibuja texto en múltiples líneas si es necesario.
     */
    private void drawMultilineString(Graphics2D g2, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g2.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (fm.stringWidth(currentLine + " " + word) < maxWidth) {
                if (currentLine.length() > 0) currentLine.append(" ");
                currentLine.append(word);
            } else {
                g2.drawString(currentLine.toString(), x, y);
                y += fm.getHeight();
                currentLine = new StringBuilder(word);
            }
        }

        if (currentLine.length() > 0) {
            g2.drawString(currentLine.toString(), x, y);
        }
    }

    /**
     * Establece los colores personalizados para la tarjeta.
     *
     * @param background Color de fondo
     * @param hover Color al pasar el mouse
     * @param text Color del texto
     */
    public void setColors(Color background, Color hover, Color text) {
        this.backgroundColor = background;
        this.hoverColor = hover;
        this.textColor = text;
        repaint();
    }
}