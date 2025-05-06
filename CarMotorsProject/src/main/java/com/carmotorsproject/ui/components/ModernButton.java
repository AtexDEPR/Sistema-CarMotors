package com.carmotorsproject.ui.components;

import com.carmotorsproject.ui.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Botón moderno con esquinas redondeadas, efectos hover y diferentes estilos.
 */
public class ModernButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color textColor;
    private int cornerRadius = 10;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private String buttonType;
    private Icon buttonIcon;
    private int iconTextGap = 10;

    /**
     * Constructor con texto.
     *
     * @param text Texto del botón
     */
    public ModernButton(String text) {
        this(text, "primary", null);
    }

    /**
     * Constructor con texto y tipo.
     *
     * @param text Texto del botón
     * @param type Tipo de botón (primary, secondary, success, warning, danger)
     */
    public ModernButton(String text, String type) {
        this(text, type, null);
    }

    /**
     * Constructor completo.
     *
     * @param text Texto del botón
     * @param type Tipo de botón (primary, secondary, success, warning, danger)
     * @param icon Icono del botón
     */
    public ModernButton(String text, String type, Icon icon) {
        super(text);
        this.buttonType = type.toLowerCase();
        this.buttonIcon = icon;

        // Configurar colores según el tipo
        setButtonColors(buttonType);

        // Configurar apariencia
        setFont(AppTheme.BUTTON_FONT);
        setForeground(textColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        if (icon != null) {
            setIcon(icon);
            setIconTextGap(iconTextGap);
        }

        // Agregar listeners para efectos hover y pressed
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

    /**
     * Configura los colores del botón según su tipo.
     *
     * @param type Tipo de botón
     */
    private void setButtonColors(String type) {
        switch (type) {
            case "primary":
                backgroundColor = AppTheme.PRIMARY_BLUE;
                hoverColor = AppTheme.PRIMARY_BLUE.darker();
                pressedColor = AppTheme.PRIMARY_BLUE.darker().darker();
                textColor = Color.WHITE;
                break;
            case "secondary":
                backgroundColor = AppTheme.SECONDARY_BLUE;
                hoverColor = AppTheme.SECONDARY_BLUE.darker();
                pressedColor = AppTheme.SECONDARY_BLUE.darker().darker();
                textColor = Color.WHITE;
                break;
            case "success":
                backgroundColor = AppTheme.SUCCESS_COLOR;
                hoverColor = AppTheme.SUCCESS_COLOR.darker();
                pressedColor = AppTheme.SUCCESS_COLOR.darker().darker();
                textColor = Color.WHITE;
                break;
            case "warning":
                backgroundColor = AppTheme.WARNING_COLOR;
                hoverColor = AppTheme.WARNING_COLOR.darker();
                pressedColor = AppTheme.WARNING_COLOR.darker().darker();
                textColor = AppTheme.PRIMARY_BLACK;
                break;
            case "danger":
                backgroundColor = AppTheme.ERROR_COLOR;
                hoverColor = AppTheme.ERROR_COLOR.darker();
                pressedColor = AppTheme.ERROR_COLOR.darker().darker();
                textColor = Color.WHITE;
                break;
            case "light":
                backgroundColor = AppTheme.LIGHT_GRAY;
                hoverColor = AppTheme.LIGHT_GRAY.darker();
                pressedColor = AppTheme.LIGHT_GRAY.darker().darker();
                textColor = AppTheme.PRIMARY_BLACK;
                break;
            case "dark":
                backgroundColor = AppTheme.PRIMARY_BLACK;
                hoverColor = new Color(AppTheme.PRIMARY_BLACK.getRed() + 20,
                        AppTheme.PRIMARY_BLACK.getGreen() + 20,
                        AppTheme.PRIMARY_BLACK.getBlue() + 20);
                pressedColor = new Color(AppTheme.PRIMARY_BLACK.getRed() + 40,
                        AppTheme.PRIMARY_BLACK.getGreen() + 40,
                        AppTheme.PRIMARY_BLACK.getBlue() + 40);
                textColor = Color.WHITE;
                break;
            default:
                backgroundColor = AppTheme.PRIMARY_BLUE;
                hoverColor = AppTheme.PRIMARY_BLUE.darker();
                pressedColor = AppTheme.PRIMARY_BLUE.darker().darker();
                textColor = Color.WHITE;
                break;
        }
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
     * Establece el radio de las esquinas.
     *
     * @param radius Radio de las esquinas
     */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determinar el color de fondo según el estado
        Color bgColor = backgroundColor;
        if (isPressed) {
            bgColor = pressedColor;
        } else if (isHovered) {
            bgColor = hoverColor;
        }

        // Dibujar el fondo redondeado
        g2d.setColor(bgColor);
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Dibujar el texto y el icono
        g2d.setColor(textColor);
        g2d.setFont(getFont());

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getHeight();

        int totalWidth = textWidth;
        int iconWidth = 0;

        if (buttonIcon != null) {
            iconWidth = buttonIcon.getIconWidth();
            totalWidth += iconWidth + iconTextGap;
        }

        int x = (getWidth() - totalWidth) / 2;
        int y = (getHeight() - textHeight) / 2 + fm.getAscent();

        if (buttonIcon != null) {
            buttonIcon.paintIcon(this, g2d, x, (getHeight() - buttonIcon.getIconHeight()) / 2);
            x += iconWidth + iconTextGap;
        }

        g2d.drawString(getText(), x, y);

        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width += 20; // Agregar un poco de padding horizontal
        size.height += 10; // Agregar un poco de padding vertical
        return size;
    }
}
