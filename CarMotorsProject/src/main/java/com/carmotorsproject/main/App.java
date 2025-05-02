package com.carmotorsproject.main;

import javax.swing.*;
import java.awt.*;

/**
 * Main application launcher
 */
public class App {
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch the application on the EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Show splash screen
                SplashScreen splashScreen = new SplashScreen();
                splashScreen.setVisible(true);
                
                // Simulate loading time
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                // Close splash screen and show main frame
                splashScreen.dispose();
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }
    
    /**
     * Splash screen shown during application startup
     */
    private static class SplashScreen extends JWindow {
        public SplashScreen() {
            setSize(500, 300);
            setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createLineBorder(new Color(198, 40, 40), 2));
            
            // Logo and title
            JLabel titleLabel = new JLabel("CarMotors", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
            titleLabel.setForeground(new Color(198, 40, 40));
            
            JLabel subtitleLabel = new JLabel("Sistema de Gestión de Taller Automotriz", SwingConstants.CENTER);
            subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(Color.WHITE);
            titlePanel.add(titleLabel, BorderLayout.CENTER);
            titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
            
            // Loading progress bar
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setForeground(new Color(198, 40, 40));
            progressBar.setBorderPainted(false);
            
            JPanel progressPanel = new JPanel(new BorderLayout());
            progressPanel.setBackground(Color.WHITE);
            progressPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
            progressPanel.add(progressBar, BorderLayout.CENTER);
            
            // Version info
            JLabel versionLabel = new JLabel("Versión 1.0", SwingConstants.RIGHT);
            versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            versionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 10));
            
            // Assemble the panel
            panel.add(titlePanel, BorderLayout.CENTER);
            panel.add(progressPanel, BorderLayout.SOUTH);
            panel.add(versionLabel, BorderLayout.SOUTH);
            panel.setBackground(Color.WHITE);
            
            setContentPane(panel);
        }
    }
}