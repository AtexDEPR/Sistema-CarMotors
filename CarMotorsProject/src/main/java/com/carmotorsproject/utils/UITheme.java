package com.carmotorsproject.utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * UITheme class provides consistent styling across the application
 * with the red, black and white color scheme
 */
public class UITheme {
    // Main color scheme
    public static final Color PRIMARY_RED = new Color(198, 40, 40);     // Deep red
    public static final Color SECONDARY_RED = new Color(229, 57, 53);   // Lighter red
    public static final Color DARK_GRAY = new Color(33, 33, 33);        // Almost black
    public static final Color MEDIUM_GRAY = new Color(66, 66, 66);      // Dark gray
    public static final Color LIGHT_GRAY = new Color(224, 224, 224);    // Light gray
    public static final Color WHITE = new Color(255, 255, 255);         // White
    public static final Color BLACK = new Color(0, 0, 0);               // Black
    
    // Font settings
    public static final Font TITLE_FONT = new Font("Roboto", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Roboto", Font.BOLD, 18);
    public static final Font REGULAR_FONT = new Font("Roboto", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Roboto", Font.PLAIN, 12);
    
    // Common borders
    public static final Border RED_LINE_BORDER = BorderFactory.createLineBorder(PRIMARY_RED, 2);
    public static final Border DARK_LINE_BORDER = BorderFactory.createLineBorder(DARK_GRAY, 1);
    
    // Apply theme to a button
    public static void applyPrimaryButtonTheme(JButton button) {
        button.setBackground(PRIMARY_RED);
        button.setForeground(WHITE);
        button.setFont(REGULAR_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    public static void applySecondaryButtonTheme(JButton button) {
        button.setBackground(DARK_GRAY);
        button.setForeground(WHITE);
        button.setFont(REGULAR_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    // Apply theme to a panel
    public static void applyPanelTheme(JPanel panel) {
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    // Apply theme to a table
    public static void applyTableTheme(JTable table) {
        table.setFont(REGULAR_FONT);
        table.setRowHeight(25);
        table.setSelectionBackground(SECONDARY_RED);
        table.setSelectionForeground(WHITE);
        table.setGridColor(LIGHT_GRAY);
        table.getTableHeader().setFont(SUBTITLE_FONT);
        table.getTableHeader().setBackground(DARK_GRAY);
        table.getTableHeader().setForeground(WHITE);
    }
    
    // Apply theme to a text field
    public static void applyTextFieldTheme(JTextField textField) {
        textField.setFont(REGULAR_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                DARK_LINE_BORDER,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }
    
    // Apply theme to a combo box
    public static void applyComboBoxTheme(JComboBox<?> comboBox) {
        comboBox.setFont(REGULAR_FONT);
        comboBox.setBackground(WHITE);
        comboBox.setBorder(DARK_LINE_BORDER);
    }
}