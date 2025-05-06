package com.carmotorsproject.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Panel con soporte para transparencia y color de fondo personalizable.
 * Útil para crear interfaces con efectos visuales modernos.
 */
public class TransparentPanel extends JPanel {
    private Color backgroundColor;
    private float alpha;
    private int cornerRadius = 0;
    private boolean hasRoundedCorners = false;

    /**
     * Constructor por defecto.
     * Crea un panel completamente transparente.
     */
    public TransparentPanel() {
        this(new Color(255, 255, 255), 0.0f);
    }

    /**
     * Constructor con color de fondo y nivel de transparencia.
     *
     * @param backgroundColor Color de fondo
     * @param alpha Nivel de transparencia (0.0f - 1.0f)
     */
    public TransparentPanel(Color backgroundColor, float alpha) {
        this.backgroundColor = backgroundColor;
        this.alpha = alpha;
        this.hasRoundedCorners = false;
        setOpaque(false);
    }

    /**
     * Constructor con color de fondo, nivel de transparencia y radio de esquinas.
     *
     * @param backgroundColor Color de fondo
     * @param alpha Nivel de transparencia (0.0f - 1.0f)
     * @param cornerRadius Radio de las esquinas
     */
    public TransparentPanel(Color backgroundColor, float alpha, int cornerRadius) {
        this.backgroundColor = backgroundColor;
        this.alpha = alpha;
        this.cornerRadius = cornerRadius;
        this.hasRoundedCorners = true;
        setOpaque(false);
    }

    /**
     * Establece el color de fondo.
     *
     * @param backgroundColor Nuevo color de fondo
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        repaint();
    }

    /**
     * Establece el nivel de transparencia.
     *
     * @param alpha Nivel de transparencia (0.0f - 1.0f)
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    /**
     * Establece el radio de las esquinas.
     *
     * @param cornerRadius Radio de las esquinas
     */
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        this.hasRoundedCorners = cornerRadius > 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(backgroundColor);

        if (hasRoundedCorners) {
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        } else {
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        g2d.dispose();
        super.paintComponent(g);
    }
}
