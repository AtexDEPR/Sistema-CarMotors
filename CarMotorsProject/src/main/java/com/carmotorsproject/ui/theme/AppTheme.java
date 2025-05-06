package com.carmotorsproject.ui.theme;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que define el tema visual de la aplicación CarMotors.
 * Contiene colores, fuentes y estilos consistentes para toda la aplicación.
 */
public class AppTheme {
    // Colores principales
    public static final Color PRIMARY_BLUE = new Color(0, 90, 156);
    public static final Color SECONDARY_BLUE = new Color(41, 128, 185);
    public static final Color PRIMARY_RED = new Color(231, 76, 60);
    public static final Color SECONDARY_RED = new Color(192, 57, 43);
    public static final Color PRIMARY_BLACK = new Color(10, 12, 19);
    public static final Color SECONDARY_BLACK = new Color(51, 51, 51);
    public static final Color ACCENT_RED = new Color(231, 76, 60);
    public static final Color ACCENT_GREEN = new Color(46, 204, 113);
    public static final Color ACCENT_ORANGE = new Color(230, 126, 34);
    public static final Color ACCENT_YELLOW = new Color(241, 196, 15);
    public static final Color ACCENT_GRAY = new Color(127, 140, 141);
    public static final Color ACCENT_LIGHT = new Color(236, 240, 241);

    // Colores neutros
    public static final Color PRIMARY_WHITE = new Color(248, 249, 250);
    public static final Color SECONDARY_WHITE = new Color(236, 240, 241);
    public static final Color LIGHT_GRAY = new Color(236, 240, 241);
    public static final Color MEDIUM_GRAY = new Color(189, 195, 199);



    // Colores de estado
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    public static final Color WARNING_COLOR = new Color(241, 196, 15);
    public static final Color ERROR_COLOR = new Color(231, 76, 60);
    public static final Color INFO_COLOR = new Color(52, 152, 219);

    // Fuentes
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TABLE_CELL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);

    // Bordes y márgenes
    public static final int STANDARD_PADDING = 10;
    public static final int SMALL_PADDING = 5;
    public static final int LARGE_PADDING = 20;
    public static final int PADDING_MEDIUM = 15;
    public static final int BORDER_RADIUS = 10;

    /**
     * Aplica el tema de la aplicación a todos los componentes Swing.
     */
    public static void applyTheme() {
        try {
            // Intentar usar el Look and Feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Personalizar componentes comunes
            UIManager.put("Panel.background", PRIMARY_WHITE);
            UIManager.put("Button.font", BUTTON_FONT);
            UIManager.put("Label.font", REGULAR_FONT);
            UIManager.put("TextField.font", REGULAR_FONT);
            UIManager.put("TextArea.font", REGULAR_FONT);
            UIManager.put("ComboBox.font", REGULAR_FONT);
            UIManager.put("Table.font", TABLE_CELL_FONT);
            UIManager.put("TableHeader.font", TABLE_HEADER_FONT);
            UIManager.put("TabbedPane.font", REGULAR_FONT);

            // Personalizar colores de componentes
            UIManager.put("Button.background", PRIMARY_BLUE);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("TabbedPane.selected", SECONDARY_BLUE);
            UIManager.put("TabbedPane.selectedForeground", Color.WHITE);

        } catch (Exception e) {
            System.err.println("Error al aplicar el tema: " + e.getMessage());
        }
    }

    /**
     * Crea un botón estilizado según el tema de la aplicación.
     *
     * @param text Texto del botón
     * @param type Tipo de botón (primary, secondary, success, warning, danger)
     * @return Botón estilizado
     */
    public static JButton createStyledButton(String text, String type) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setOpaque(true);

        switch (type.toLowerCase()) {
            case "primary":
                button.setBackground(PRIMARY_BLUE);
                button.setForeground(Color.WHITE);
                break;
            case "secondary":
                button.setBackground(SECONDARY_BLUE);
                button.setForeground(Color.WHITE);
                break;
            case "success":
                button.setBackground(SUCCESS_COLOR);
                button.setForeground(Color.WHITE);
                break;
            case "warning":
                button.setBackground(WARNING_COLOR);
                button.setForeground(PRIMARY_BLACK);
                break;
            case "danger":
                button.setBackground(ERROR_COLOR);
                button.setForeground(Color.WHITE);
                break;
            default:
                button.setBackground(LIGHT_GRAY);
                button.setForeground(PRIMARY_BLACK);
                break;
        }

        return button;
    }

    /**
     * Configura el estilo de una tabla según el tema de la aplicación.
     *
     * @param table Tabla a estilizar
     */
    public static void styleTable(JTable table) {
        table.setFont(TABLE_CELL_FONT);
        table.getTableHeader().setFont(TABLE_HEADER_FONT);
        table.getTableHeader().setBackground(PRIMARY_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(LIGHT_GRAY);
        table.setSelectionBackground(SECONDARY_BLUE);
        table.setSelectionForeground(Color.WHITE);
    }
}
