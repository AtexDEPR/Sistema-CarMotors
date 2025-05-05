package com.carmotorsproject.ui.animation;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase para manejar animaciones en la interfaz de usuario.
 */
public class AnimationManager {

    /**
     * Realiza una animación de fade-in para un componente.
     *
     * @param component Componente a animar
     * @param duration Duración en milisegundos
     */
    public static void fadeIn(JComponent component, int duration) {
        component.setVisible(true);
        component.setOpaque(false);

        // Establecer opacidad inicial
        float[] opacity = new float[]{0.0f};

        // Crear timer para la animación
        Timer timer = new Timer(20, e -> {
            opacity[0] += 0.05f;
            if (opacity[0] >= 1.0f) {
                opacity[0] = 1.0f;
                ((Timer) e.getSource()).stop();
                component.setOpaque(true);
                component.repaint();
            }

            component.putClientProperty("opacity", opacity[0]);
            component.repaint();
        });

        timer.setInitialDelay(0);
        timer.start();
    }

    /**
     * Realiza una animación de fade-out para un componente.
     *
     * @param component Componente a animar
     * @param duration Duración en milisegundos
     * @param removeAfter Si se debe eliminar el componente después de la animación
     */
    public static void fadeOut(JComponent component, int duration, boolean removeAfter) {
        component.setOpaque(false);

        // Establecer opacidad inicial
        float[] opacity = new float[]{1.0f};

        // Crear timer para la animación
        Timer timer = new Timer(20, e -> {
            opacity[0] -= 0.05f;
            if (opacity[0] <= 0.0f) {
                opacity[0] = 0.0f;
                ((Timer) e.getSource()).stop();

                if (removeAfter && component.getParent() != null) {
                    component.getParent().remove(component);
                    component.getParent().revalidate();
                    component.getParent().repaint();
                } else {
                    component.setVisible(false);
                    component.repaint();
                }
            }

            component.putClientProperty("opacity", opacity[0]);
            component.repaint();
        });

        timer.setInitialDelay(0);
        timer.start();
    }

    /**
     * Realiza una animación de slide-in desde la izquierda.
     *
     * @param component Componente a animar
     * @param duration Duración en milisegundos
     */
    public static void slideInFromLeft(JComponent component, int duration) {
        Container parent = component.getParent();
        int targetX = component.getX();

        // Posición inicial fuera de la pantalla
        component.setLocation(-component.getWidth(), component.getY());
        component.setVisible(true);

        // Calcular incremento por paso
        int distance = targetX + component.getWidth();
        int steps = duration / 20; // 20ms por paso
        final int increment = distance / steps;

        AtomicInteger currentX = new AtomicInteger(-component.getWidth());

        Timer timer = new Timer(20, e -> {
            currentX.addAndGet(increment);
            if (currentX.get() >= targetX) {
                component.setLocation(targetX, component.getY());
                ((Timer) e.getSource()).stop();
            } else {
                component.setLocation(currentX.get(), component.getY());
            }
            parent.repaint();
        });

        timer.setInitialDelay(0);
        timer.start();
    }

    /**
     * Realiza una animación de slide-out hacia la derecha.
     *
     * @param component Componente a animar
     * @param duration Duración en milisegundos
     * @param removeAfter Si se debe eliminar el componente después de la animación
     */
    public static void slideOutToRight(JComponent component, int duration, boolean removeAfter) {
        Container parent = component.getParent();
        int startX = component.getX();
        int targetX = parent.getWidth();

        // Calcular incremento por paso
        int distance = targetX - startX;
        int steps = duration / 20; // 20ms por paso
        final int increment = distance / steps;

        AtomicInteger currentX = new AtomicInteger(startX);

        Timer timer = new Timer(20, e -> {
            currentX.addAndGet(increment);
            if (currentX.get() >= targetX) {
                ((Timer) e.getSource()).stop();

                if (removeAfter && component.getParent() != null) {
                    component.getParent().remove(component);
                    component.getParent().revalidate();
                    component.getParent().repaint();
                } else {
                    component.setVisible(false);
                    component.setLocation(startX, component.getY());
                }
            } else {
                component.setLocation(currentX.get(), component.getY());
            }
            parent.repaint();
        });

        timer.setInitialDelay(0);
        timer.start();
    }
}