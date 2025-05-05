package com.carmotorsproject.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Panel con fondo semitransparente y esquinas redondeadas.
 */
public class TransparentPanel extends JPanel {

    private Color backgroundColor;
    private float opacity = 0.9f;
    private int radius = 10;

    /**
     * Constructor con color de fondo.
     *
     * @param backgroundColor Color de fondo
     */
    public TransparentPanel(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        setOpaque(false);
        setLayout(new BorderLayout());
    }

    /**
     * Constructor con color de fondo y opacidad.
     *
     * @param backgroundColor Color de fondo
     * @param opacity Opacidad (0.0f - 1.0f)
     */
    public TransparentPanel(Color backgroundColor, float opacity) {
        this(backgroundColor);
        this.opacity = opacity;
    }

    /**
     * Constructor con color de fondo, opacidad y radio de esquinas.
     *
     * @param backgroundColor Color de fondo
     * @param opacity Opacidad (0.0f - 1.0f)
     * @param radius Radio de las esquinas
     */
    public TransparentPanel(Color backgroundColor, float opacity, int radius) {
        this(backgroundColor, opacity);
        this.radius = radius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Aplicar opacidad
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        // Dibujar fondo redondeado
        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));

        g2.dispose();
    }

    /**
     * Establece la opacidad del panel.
     *
     * @param opacity Opacidad (0.0f - 1.0f)
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint();
    }

    /**
     * Establece el radio de las esquinas.
     *
     * @param radius Radio en píxeles
     */
    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }
}
