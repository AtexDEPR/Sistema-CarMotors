package com.carmotorsproject.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Panel con esquinas redondeadas para una interfaz más moderna.
 */
public class RoundedPanel extends JPanel {
    private Color backgroundColor;
    private int cornerRadius = 15;
    private boolean hasShadow = false;
    private int shadowSize = 5;

    /**
     * Constructor por defecto.
     */
    public RoundedPanel(Color primaryWhite, float v, int i) {
        this(Color.WHITE, 15, false);
    }

    /**
     * Constructor con color de fondo y radio de esquinas.
     *
     * @param backgroundColor Color de fondo
     * @param cornerRadius Radio de las esquinas
     */
    public RoundedPanel(Color backgroundColor, int cornerRadius) {
        this(backgroundColor, cornerRadius, false);
    }

    /**
     * Constructor completo.
     *
     * @param backgroundColor Color de fondo
     * @param cornerRadius Radio de las esquinas
     * @param hasShadow Si debe tener sombra
     */
    public RoundedPanel(Color backgroundColor, int cornerRadius, boolean hasShadow) {
        this.backgroundColor = backgroundColor;
        this.cornerRadius = cornerRadius;
        this.hasShadow = hasShadow;
        setOpaque(false);

        if (hasShadow) {
            setBorder(BorderFactory.createEmptyBorder(shadowSize, shadowSize, shadowSize, shadowSize));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar sombra si está habilitada
        if (hasShadow) {
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fill(new RoundRectangle2D.Double(
                    shadowSize, shadowSize,
                    getWidth() - (2 * shadowSize),
                    getHeight() - (2 * shadowSize),
                    cornerRadius, cornerRadius));
        }

        // Dibujar panel redondeado
        g2d.setColor(backgroundColor);
        g2d.fill(new RoundRectangle2D.Double(
                hasShadow ? shadowSize : 0,
                hasShadow ? shadowSize : 0,
                getWidth() - (hasShadow ? 2 * shadowSize : 0),
                getHeight() - (hasShadow ? 2 * shadowSize : 0),
                cornerRadius, cornerRadius));

        g2d.dispose();
    }
}
