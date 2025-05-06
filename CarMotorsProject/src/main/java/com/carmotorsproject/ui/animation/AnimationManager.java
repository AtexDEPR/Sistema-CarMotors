package com.carmotorsproject.ui.animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase para gestionar animaciones en la interfaz de usuario.
 */
public class AnimationManager {

    /**
     * Aplica una animación de fade-in a un componente.
     *
     * @param component El componente a animar
     * @param duration La duración de la animación en milisegundos
     */
    public static void fadeIn(JComponent component, int duration) {
        component.setOpaque(false);
        float[] animationValues = new float[]{0.0f, 1.0f};

        Timer timer = new Timer(duration / 10, new ActionListener() {
            private int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < animationValues.length) {
                    float alpha = animationValues[index++];
                    component.setBackground(new Color(
                            component.getBackground().getRed(),
                            component.getBackground().getGreen(),
                            component.getBackground().getBlue(),
                            (int) (alpha * 255)
                    ));
                    component.repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
    }

    /**
     * Aplica una animación de fade-in a un JFrame.
     *
     * @param frame El frame a animar
     * @param duration La duración de la animación en milisegundos
     */
    public static void fadeInFrame(JFrame frame, int duration) {
        frame.setOpacity(0.0f);

        Timer timer = new Timer(duration / 20, new ActionListener() {
            private float opacity = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity <= 1.0f) {
                    frame.setOpacity(opacity);
                } else {
                    frame.setOpacity(1.0f);
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
    }

    /**
     * Aplica una animación de slide-in desde la derecha a un componente.
     *
     * @param component El componente a animar
     * @param duration La duración de la animación en milisegundos
     */
    public static void slideInRight(JComponent component, int duration) {
        Dimension size = component.getPreferredSize();
        int targetX = component.getX();
        int startX = targetX + size.width;

        component.setLocation(startX, component.getY());

        Timer timer = new Timer(duration / 20, new ActionListener() {
            private int currentX = startX;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentX -= (startX - targetX) / 20;
                if (currentX > targetX) {
                    component.setLocation(currentX, component.getY());
                } else {
                    component.setLocation(targetX, component.getY());
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
    }
}
