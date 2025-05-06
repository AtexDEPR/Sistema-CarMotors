package com.carmotorsproject.ui.components;

import com.carmotorsproject.ui.theme.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Campo de texto moderno con soporte para placeholder y estilo mejorado.
 */
public class ModernTextField extends JTextField {

    private String placeholder;
    private String label;
    private Icon icon;
    private Color placeholderColor = AppTheme.ACCENT_GRAY;
    private Color focusColor = AppTheme.PRIMARY_BLUE;
    private Color borderColor = AppTheme.MEDIUM_GRAY;
    private boolean isFocused = false;
    private int columns = 20;

    /**
     * Constructor sin argumentos para compatibilidad con código existente.
     */
    public ModernTextField() {
        this("", "");
    }

    /**
     * Constructor con etiqueta.
     *
     * @param label Etiqueta del campo
     */
    public ModernTextField(String label) {
        this(label, "");
    }

    /**
     * Constructor con etiqueta y placeholder.
     *
     * @param label Etiqueta del campo
     * @param placeholder Texto de placeholder
     */
    public ModernTextField(String label, String placeholder) {
        this(label, placeholder, null);
    }

    /**
     * Constructor completo.
     *
     * @param label Etiqueta del campo
     * @param placeholder Texto de placeholder
     * @param icon Icono del campo
     */
    public ModernTextField(String label, String placeholder, Icon icon) {
        super();
        this.label = label;
        this.placeholder = placeholder;
        this.icon = icon;

        setupUI();
    }

    /**
     * Configura la interfaz del campo.
     */
    private void setupUI() {
        setFont(AppTheme.REGULAR_FONT);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                new EmptyBorder(8, 10, 8, 10)));

        if (icon != null) {
            setMargin(new Insets(2, icon.getIconWidth() + 10, 2, 2));
        }

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(focusColor, 2),
                        new EmptyBorder(7, 9, 7, 9)));
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor, 1),
                        new EmptyBorder(8, 10, 8, 10)));
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar placeholder si el campo está vacío y no tiene foco
        if (getText().isEmpty() && !isFocused && placeholder != null && !placeholder.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(placeholderColor);
            g2.setFont(getFont());

            int padding = 10;
            if (icon != null) {
                padding += icon.getIconWidth();
            }

            g2.drawString(placeholder, padding, (getHeight() / 2) + (g2.getFontMetrics().getAscent() / 2) - 2);
            g2.dispose();
        }

        // Dibujar icono si existe
        if (icon != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            icon.paintIcon(this, g2, 5, (getHeight() - icon.getIconHeight()) / 2);
            g2.dispose();
        }
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
     * Establece la etiqueta del campo.
     *
     * @param label Etiqueta del campo
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Obtiene la etiqueta del campo.
     *
     * @return Etiqueta del campo
     */
    public String getLabel() {
        return label;
    }

    /**
     * Establece el icono del campo.
     *
     * @param icon Icono del campo
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
        repaint();
    }

    /**
     * Obtiene el icono del campo.
     *
     * @return Icono del campo
     */
    public Icon getIcon() {
        return icon;
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
     * Establece el color de foco.
     *
     * @param color Color de foco
     */
    public void setFocusColor(Color color) {
        this.focusColor = color;
    }

    /**
     * Establece el color del borde.
     *
     * @param color Color del borde
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        if (!isFocused) {
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, 1),
                    new EmptyBorder(8, 10, 8, 10)));
        }
    }

    /**
     * Establece el número de columnas.
     *
     * @param columns Número de columnas
     */
    @Override
    public void setColumns(int columns) {
        this.columns = columns;
        super.setColumns(columns);
    }
}
