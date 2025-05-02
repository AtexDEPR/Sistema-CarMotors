package com.carmotorsproject.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Utility class for animations and transitions in the UI
 */
public class AnimationUtil {
    
    /**
     * Fades in a component
     * @param component The component to fade in
     * @param duration Duration in milliseconds
     */
    public static void fadeIn(final JComponent component, int duration) {
        component.setVisible(true);
        component.setOpaque(false);
        
        final float[] opacity = {0.0f};
        final Timer timer = new Timer(20, null);
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity[0] += 0.05f;
                if (opacity[0] >= 1.0f) {
                    opacity[0] = 1.0f;
                    timer.stop();
                    component.setOpaque(true);
                }
                component.setBackground(new Color(
                        component.getBackground().getRed(),
                        component.getBackground().getGreen(),
                        component.getBackground().getBlue(),
                        (int) (opacity[0] * 255)
                ));
                component.repaint();
            }
        });
        
        timer.setInitialDelay(0);
        timer.start();
    }
    
    /**
     * Fades out a component
     * @param component The component to fade out
     * @param duration Duration in milliseconds
     */
    public static void fadeOut(final JComponent component, int duration) {
        component.setOpaque(true);
        
        final float[] opacity = {1.0f};
        final Timer timer = new Timer(20, null);
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity[0] -= 0.05f;
                if (opacity[0] <= 0.0f) {
                    opacity[0] = 0.0f;
                    timer.stop();
                    component.setVisible(false);
                }
                component.setBackground(new Color(
                        component.getBackground().getRed(),
                        component.getBackground().getGreen(),
                        component.getBackground().getBlue(),
                        (int) (opacity[0] * 255)
                ));
                component.repaint();
            }
        });
        
        timer.setInitialDelay(0);
        timer.start();
    }
    
    /**
     * Slides in a component from the left
     * @param component The component to slide in
     * @param container The container holding the component
     * @param duration Duration in milliseconds
     */
    public static void slideInFromLeft(final JComponent component, final Container container, int duration) {
        component.setVisible(true);
        final int targetX = component.getX();
        component.setLocation(-component.getWidth(), component.getY());
        
        final Timer timer = new Timer(10, null);
        final int steps = duration / 10;
        final int distancePerStep = (targetX + component.getWidth()) / steps;
        
        timer.addActionListener(new ActionListener() {
            int currentStep = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep++;
                int newX = component.getX() + distancePerStep;
                
                if (newX >= targetX || currentStep >= steps) {
                    component.setLocation(targetX, component.getY());
                    timer.stop();
                } else {
                    component.setLocation(newX, component.getY());
                }
                container.repaint();
            }
        });
        
        timer.setInitialDelay(0);
        timer.start();
    }
    
    /**
     * Creates a button hover effect
     * @param button The button to apply the effect to
     */
    public static void applyButtonHoverEffect(final JButton button) {
        final Color originalColor = button.getBackground();
        final Color hoverColor = new Color(
                Math.min(originalColor.getRed() + 20, 255),
                Math.min(originalColor.getGreen() + 20, 255),
                Math.min(originalColor.getBlue() + 20, 255)
        );
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }
    
    /**
     * Creates a pulsating effect for a component
     * @param component The component to apply the effect to
     */
    public static void applyPulsatingEffect(final JComponent component) {
        final Timer timer = new Timer(50, null);
        final float[] scale = {1.0f};
        final boolean[] increasing = {true};
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (increasing[0]) {
                    scale[0] += 0.01f;
                    if (scale[0] >= 1.05f) {
                        increasing[0] = false;
                    }
                } else {
                    scale[0] -= 0.01f;
                    if (scale[0] <= 0.95f) {
                        increasing[0] = true;
                    }
                }
                
                component.setBorder(BorderFactory.createEmptyBorder(
                        (int) (5 * scale[0]),
                        (int) (5 * scale[0]),
                        (int) (5 * scale[0]),
                        (int) (5 * scale[0])
                ));
                component.repaint();
            }
        });
        
        timer.start();
    }
}