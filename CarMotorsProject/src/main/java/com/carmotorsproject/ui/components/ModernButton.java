package com.carmotorsproject.ui.components;

import com.carmotorsproject.ui.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Botón personalizado con diseño moderno y efectos de hover.
 */
public class ModernButton extends JButton {

    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color textColor;
    private boolean isMenuButton;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private int radius = AppTheme.BORDER_RADIUS;

    /**
     * Constructor para botón estándar.
     *
     * @param text Texto del botón
     */
    public ModernButton(String text) {
        this(text, false);
    }

    /**
     * Constructor con opción para botón de menú.
     *
     * @param text Texto del botón
     * @param isMenuButton Si es un botón de menú
     */
    public ModernButton(String text, boolean isMenuButton) {
        super(text);
        this.isMenuButton = isMenuButton;

        if (isMenuButton) {
            backgroundColor = AppTheme.PRIMARY_BLACK;
            hoverColor = AppTheme.PRIMARY_RED;
            pressedColor = AppTheme.SECONDARY_RED;
            textColor = AppTheme.PRIMARY_WHITE;
        } else {
            backgroundColor = AppTheme.PRIMARY_RED;
            hoverColor = AppTheme.SECONDARY_RED;
            pressedColor = AppTheme.SECONDARY_RED.darker();
            textColor = AppTheme.PRIMARY_WHITE;
        }

        setup();
    }

    private void setup() {
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(textColor);
        setFont(AppTheme.REGULAR_FONT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Añadir listeners para efectos de hover
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
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determinar color de fondo basado en estado
        Color bgColor;
        if (isPressed) {
            bgColor = pressedColor;
        } else if (isHovered) {
            bgColor = hoverColor;
        } else {
            bgColor = backgroundColor;
        }

        // Dibujar fondo redondeado
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));

        // Dibujar texto
        g2.setColor(textColor);
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(getText())) / 2;
        int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }

    /**
     * Establece los colores personalizados para el botón.
     *
     * @param background Color de fondo
     * @param hover Color al pasar el mouse
     * @param pressed Color al presionar
     * @param text Color del texto
     */
    public void setColors(Color background, Color hover, Color pressed, Color text) {
        this.backgroundColor = background;
        this.hoverColor = hover;
        this.pressedColor = pressed;
        this.textColor = text;
        setForeground(text);
        repaint();
    }

    /**
     * Establece el radio de las esquinas redondeadas.
     *
     * @param radius Radio en píxeles
     */
    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }
}