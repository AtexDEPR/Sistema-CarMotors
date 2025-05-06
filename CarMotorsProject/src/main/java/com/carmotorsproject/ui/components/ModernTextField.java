package com.carmotorsproject.ui.components;

import com.carmotorsproject.ui.theme.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Campo de texto moderno con efectos visuales.
 */
public class ModernTextField extends JTextField {

    private String placeholder;
    private Color placeholderColor = AppTheme.ACCENT_GRAY;
    private Color borderColor = AppTheme.ACCENT_GRAY;
    private Color focusBorderColor = AppTheme.PRIMARY_RED;
    private int borderRadius = 5;
    private boolean showPlaceholder = true;

    /**
     * Constructor por defecto.
     */
    public ModernTextField() {
        super();
        setupTextField();
    }

    /**
     * Constructor con texto inicial.
     *
     * @param text Texto inicial
     */
    public ModernTextField(String text) {
        super(text);
        setupTextField();
    }

    /**
     * Constructor con número de columnas.
     *
     * @param columns Número de columnas
     */
    public ModernTextField(int columns) {
        super(columns);
        setupTextField();
    }

    /**
     * Constructor con texto inicial y número de columnas.
     *
     * @param text Texto inicial
     * @param columns Número de columnas
     */
    public ModernTextField(String text, int columns) {
        super(text, columns);
        setupTextField();
    }

    /**
     * Configura el campo de texto.
     */
    private void setupTextField() {
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(8, 10, 8, 10)));
        setFont(AppTheme.REGULAR_FONT);
        setForeground(AppTheme.PRIMARY_BLACK);

        // Añadir listener para efectos de foco
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(focusBorderColor, 2),
                        new EmptyBorder(7, 9, 7, 9)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor),
                        new EmptyBorder(8, 10, 8, 10)));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), borderRadius, borderRadius));

        super.paintComponent(g);

        // Dibujar placeholder si está vacío y no tiene foco
        if (showPlaceholder && getText().isEmpty() && !hasFocus() && placeholder != null) {
            g2.setColor(placeholderColor);
            g2.setFont(getFont());
            int padding = (getHeight() - g2.getFontMetrics().getHeight()) / 2;
            g2.drawString(placeholder, 10, getHeight() - padding - g2.getFontMetrics().getDescent());
        }

        g2.dispose();
    }

    /**
     * Establece el texto de placeholder.
     *
     * @param placeholder Texto de placeholder
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    /**
     * Obtiene el texto de placeholder.
     *
     * @return Texto de placeholder
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * Establece el color del placeholder.
     *
     * @param color Color del placeholder
     */
    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        repaint();
    }

    /**
     * Establece el color del borde.
     *
     * @param color Color del borde
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(8, 10, 8, 10)));
        repaint();
    }

    /**
     * Establece el color del borde cuando tiene foco.
     *
     * @param color Color del borde con foco
     */
    public void setFocusBorderColor(Color color) {
        this.focusBorderColor = color;
        repaint();
    }

    /**
     * Establece el radio del borde.
     *
     * @param radius Radio del borde
     */
    public void setBorderRadius(int radius) {
        this.borderRadius = radius;
        repaint();
    }
}
