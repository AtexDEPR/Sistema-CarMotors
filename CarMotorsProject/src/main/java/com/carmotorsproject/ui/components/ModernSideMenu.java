package com.carmotorsproject.ui.components;

import com.carmotorsproject.ui.animation.AnimationManager;
import com.carmotorsproject.ui.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Menú lateral moderno con animaciones y efectos visuales.
 */
public class ModernSideMenu extends JPanel {

    private final int MENU_WIDTH = 250;
    private final int ITEM_HEIGHT = 45;
    private final int SUBMENU_INDENT = 20;

    private List<MenuSection> sections = new ArrayList<>();
    private Map<String, JPanel> subMenuPanels = new HashMap<>();
    private String activeSection = null;
    private String activeItem = null;

    /**
     * Constructor del menú lateral moderno.
     */
    public ModernSideMenu() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppTheme.PRIMARY_BLACK);
        setPreferredSize(new Dimension(MENU_WIDTH, 0));
        setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
    }

    /**
     * Añade una sección al menú.
     *
     * @param title Título de la sección
     * @return La sección creada
     */
    public MenuSection addSection(String title) {
        MenuSection section = new MenuSection(title);
        sections.add(section);
        return section;
    }

    /**
     * Construye y muestra el menú completo.
     */
    public void buildMenu() {
        removeAll();

        // Añadir logo y título
        JPanel logoPanel = createLogoPanel();
        add(logoPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // Añadir secciones y elementos del menú
        for (MenuSection section : sections) {
            // Añadir título de sección
            if (section.getTitle() != null && !section.getTitle().isEmpty()) {
                JLabel sectionLabel = new JLabel(section.getTitle().toUpperCase());
                sectionLabel.setForeground(AppTheme.PRIMARY_RED);
                sectionLabel.setFont(AppTheme.SMALL_FONT);
                sectionLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 0));
                add(sectionLabel);
            }

            // Añadir elementos de menú
            for (MenuItem item : section.getItems()) {
                JPanel menuItemPanel = createMenuItemPanel(item, false);
                add(menuItemPanel);

                // Crear panel de submenú si hay subelementos
                if (!item.getSubItems().isEmpty()) {
                    JPanel subMenuPanel = new JPanel();
                    subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
                    subMenuPanel.setBackground(AppTheme.SECONDARY_BLACK);
                    subMenuPanel.setVisible(false);

                    // Añadir subelementos
                    for (MenuItem subItem : item.getSubItems()) {
                        JPanel subItemPanel = createMenuItemPanel(subItem, true);
                        subMenuPanel.add(subItemPanel);
                    }

                    add(subMenuPanel);
                    subMenuPanels.put(item.getId(), subMenuPanel);
                }
            }
        }

        // Añadir espacio flexible al final
        add(Box.createVerticalGlue());

        // Añadir botón de salida
        ModernButton exitButton = new ModernButton("Salir", true);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(MENU_WIDTH - 30, ITEM_HEIGHT));
        exitButton.setColors(AppTheme.PRIMARY_RED, AppTheme.SECONDARY_RED,
                AppTheme.SECONDARY_RED.darker(), AppTheme.PRIMARY_WHITE);
        exitButton.addActionListener(e -> {
            // La acción de salir se manejará en la clase App
            firePropertyChange("exitApplication", false, true);
        });

        JPanel exitPanel = new JPanel();
        exitPanel.setLayout(new BoxLayout(exitPanel, BoxLayout.X_AXIS));
        exitPanel.setOpaque(false);
        exitPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        exitPanel.add(exitButton);

        add(exitPanel);

        revalidate();
        repaint();
    }

    /**
     * Crea el panel del logo y título.
     *
     * @return Panel con logo y título
     */
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        JLabel logoLabel = new JLabel("CM");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 32));
        logoLabel.setForeground(AppTheme.PRIMARY_RED);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(logoLabel);

        JLabel titleLabel = new JLabel("Car Motors");
        titleLabel.setFont(AppTheme.SUBTITLE_FONT);
        titleLabel.setForeground(AppTheme.PRIMARY_WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Workshop");
        subtitleLabel.setFont(AppTheme.REGULAR_FONT);
        subtitleLabel.setForeground(AppTheme.ACCENT_LIGHT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(subtitleLabel);

        return logoPanel;
    }

    /**
     * Crea un panel para un elemento de menú.
     *
     * @param item Elemento de menú
     * @param isSubItem Si es un subelemento
     * @return Panel del elemento de menú
     */
    private JPanel createMenuItemPanel(MenuItem item, boolean isSubItem) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(MENU_WIDTH, ITEM_HEIGHT));

        int leftPadding = isSubItem ? SUBMENU_INDENT : 15;
        panel.setBorder(BorderFactory.createEmptyBorder(0, leftPadding, 0, 15));

        // Crear etiqueta para el texto del elemento
        JLabel label = new JLabel(item.getText());
        label.setForeground(AppTheme.PRIMARY_WHITE);
        label.setFont(AppTheme.REGULAR_FONT);
        panel.add(label, BorderLayout.CENTER);

        // Añadir icono de flecha si tiene subelementos
        if (!item.getSubItems().isEmpty()) {
            JLabel arrowLabel = new JLabel("▼");
            arrowLabel.setForeground(AppTheme.PRIMARY_WHITE);
            arrowLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            panel.add(arrowLabel, BorderLayout.EAST);
        }

        // Añadir efectos de hover y clic
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(AppTheme.PRIMARY_RED);
                panel.setOpaque(true);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!item.getId().equals(activeItem)) {
                    panel.setOpaque(false);
                    repaint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Manejar clic en elemento de menú
                handleMenuItemClick(item);
            }
        });

        return panel;
    }

    /**
     * Maneja el clic en un elemento de menú.
     *
     * @param item Elemento de menú clicado
     */
    private void handleMenuItemClick(MenuItem item) {
        // Si tiene subelementos, mostrar/ocultar submenú
        if (!item.getSubItems().isEmpty()) {
            JPanel subMenuPanel = subMenuPanels.get(item.getId());
            if (subMenuPanel != null) {
                boolean isVisible = subMenuPanel.isVisible();

                // Animar la apertura/cierre del submenú
                if (isVisible) {
                    AnimationManager.fadeOut(subMenuPanel, 200, false);
                } else {
                    subMenuPanel.setVisible(true);
                    AnimationManager.fadeIn(subMenuPanel, 200);
                }
            }
        } else {
            // Actualizar elemento activo
            String oldActiveItem = activeItem;
            activeItem = item.getId();

            // Notificar a los listeners sobre el cambio de elemento
            firePropertyChange("menuItemSelected", oldActiveItem, item);
        }
    }

    /**
     * Clase interna para representar una sección del menú.
     */
    public class MenuSection {
        private String title;
        private List<MenuItem> items = new ArrayList<>();

        public MenuSection(String title) {
            this.title = title;
        }

        /**
         * Añade un elemento a la sección.
         *
         * @param text Texto del elemento
         * @param action Acción a ejecutar
         * @return El elemento creado
         */
        public MenuItem addItem(String text, Runnable action) {
            String id = "item_" + System.currentTimeMillis();
            MenuItem item = new MenuItem(id, text, action);
            items.add(item);
            return item;
        }

        public String getTitle() {
            return title;
        }

        public List<MenuItem> getItems() {
            return items;
        }
    }

    /**
     * Clase interna para representar un elemento de menú.
     */
    public class MenuItem {
        private String id;
        private String text;
        private Runnable action;
        private List<MenuItem> subItems = new ArrayList<>();

        public MenuItem(String id, String text, Runnable action) {
            this.id = id;
            this.text = text;
            this.action = action;
        }

        /**
         * Añade un subelemento al elemento.
         *
         * @param text Texto del subelemento
         * @param action Acción a ejecutar
         * @return El subelemento creado
         */
        public MenuItem addSubItem(String text, Runnable action) {
            String subId = id + "_sub_" + System.currentTimeMillis();
            MenuItem subItem = new MenuItem(subId, text, action);
            subItems.add(subItem);
            return subItem;
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public Runnable getAction() {
            return action;
        }

        public List<MenuItem> getSubItems() {
            return subItems;
        }
    }
}