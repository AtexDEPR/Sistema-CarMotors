package com.carmotorsproject.ui.theme;

import java.awt.Color;
import java.awt.Font;

/**
 * Clase que define los colores y estilos del tema de la aplicación.
 */
public class AppTheme {
    // Colores principales
    public static final Color PRIMARY_RED = new Color(227, 30, 36);       // Rojo principal
    public static final Color SECONDARY_RED = new Color(190, 30, 45);     // Rojo más oscuro
    public static final Color PRIMARY_BLACK = new Color(20, 20, 20);      // Negro principal
    public static final Color SECONDARY_BLACK = new Color(40, 40, 40);    // Negro secundario
    public static final Color PRIMARY_WHITE = new Color(245, 245, 245);   // Blanco principal
    public static final Color SECONDARY_WHITE = new Color(230, 230, 230); // Blanco secundario

    // Colores de acento
    public static final Color ACCENT_GRAY = new Color(100, 100, 100);     // Gris acento
    public static final Color ACCENT_LIGHT = new Color(200, 200, 200);    // Gris claro

    // Colores de estado
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);    // Verde éxito
    public static final Color WARNING_COLOR = new Color(241, 196, 15);    // Amarillo advertencia
    public static final Color ERROR_COLOR = new Color(231, 76, 60);       // Rojo error

    // Fuentes
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // Radios de borde
    public static final int BORDER_RADIUS = 10;

    // Espaciado
    public static final int PADDING_SMALL = 5;
    public static final int PADDING_MEDIUM = 10;
    public static final int PADDING_LARGE = 20;
}