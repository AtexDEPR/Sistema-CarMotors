package com.carmotorsproject.ui.components;

import com.carmotorsproject.ui.theme.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Menú lateral moderno para la aplicación.
 */
public class ModernSideMenu extends JPanel {

    private List<MenuSection> sections = new ArrayList<>();
    private JPanel menuPanel;
    private int menuWidth = 250;
    private boolean isCollapsed = false;

    /**
     * Constructor del menú lateral.
     */
    public ModernSideMenu() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.PRIMARY_BLACK);
        setPreferredSize(new Dimension(menuWidth, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, AppTheme.PRIMARY_RED));

        // Panel de contenido del menú
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(AppTheme.PRIMARY_BLACK);
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Scroll pane para el menú
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Botón para cerrar sesión en la parte inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(AppTheme.SECONDARY_BLACK);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        ModernButton logoutButton = new ModernButton("Cerrar Sesión");
        logoutButton.setColors(AppTheme.PRIMARY_RED, AppTheme.SECONDARY_RED,
                AppTheme.SECONDARY_RED.darker(), Color.WHITE);
        logoutButton.addActionListener(e -> {
            firePropertyChange("exitApplication", false, true);
        });

        bottomPanel.add(logoutButton, BorderLayout.CENTER);

        // Añadir componentes al panel principal
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
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
     * Construye el menú con todas las secciones y elementos añadidos.
     */
    public void buildMenu() {
        menuPanel.removeAll();

        for (MenuSection section : sections) {
            if (!section.getTitle().isEmpty()) {
                JLabel titleLabel = new JLabel(section.getTitle().toUpperCase());
                titleLabel.setFont(AppTheme.SMALL_FONT);
                titleLabel.setForeground(AppTheme.ACCENT_LIGHT);
                titleLabel.setBorder(new EmptyBorder(10, 5, 5, 5));
                titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                menuPanel.add(titleLabel);
            }

            for (MenuItem item : section.getItems()) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBackground(AppTheme.PRIMARY_BLACK);
                itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                itemPanel.setMaximumSize(new Dimension(menuWidth - 20, 40));

                JLabel itemLabel = new JLabel(item.getText());
                itemLabel.setFont(AppTheme.REGULAR_FONT);
                itemLabel.setForeground(Color.WHITE);
                itemLabel.setBorder(new EmptyBorder(8, 10, 8, 10));

                itemPanel.add(itemLabel, BorderLayout.CENTER);

                // Añadir efecto hover
                itemPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        itemPanel.setBackground(AppTheme.PRIMARY_RED);
                        itemLabel.setForeground(Color.WHITE);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        itemPanel.setBackground(AppTheme.PRIMARY_BLACK);
                        itemLabel.setForeground(Color.WHITE);
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        firePropertyChange("menuItemSelected", null, item);
                    }
                });

                menuPanel.add(itemPanel);
            }

            // Añadir separador después de cada sección excepto la última
            if (sections.indexOf(section) < sections.size() - 1) {
                JSeparator separator = new JSeparator();
                separator.setForeground(AppTheme.ACCENT_LIGHT);
                separator.setBackground(AppTheme.PRIMARY_BLACK);
                separator.setMaximumSize(new Dimension(menuWidth - 20, 1));
                separator.setAlignmentX(Component.LEFT_ALIGNMENT);
                menuPanel.add(separator);
            }
        }

        revalidate();
        repaint();
    }

    /**
     * Colapsa o expande el menú.
     *
     * @param collapsed true para colapsar, false para expandir
     */
    public void setCollapsed(boolean collapsed) {
        this.isCollapsed = collapsed;

        if (collapsed) {
            setPreferredSize(new Dimension(60, 0));
        } else {
            setPreferredSize(new Dimension(menuWidth, 0));
        }

        revalidate();
        repaint();
    }

    /**
     * Clase interna para representar una sección del menú.
     */
    public class MenuSection {
        private String title;
        private List<MenuItem> items = new ArrayList<>();

        /**
         * Constructor de la sección.
         *
         * @param title Título de la sección
         */
        public MenuSection(String title) {
            this.title = title;
        }

        /**
         * Añade un elemento a la sección.
         *
         * @param text Texto del elemento
         * @param action Acción a ejecutar al hacer clic
         * @return El elemento creado
         */
        public MenuItem addItem(String text, Runnable action) {
            MenuItem item = new MenuItem(text, action);
            items.add(item);
            return item;
        }

        /**
         * Obtiene el título de la sección.
         *
         * @return Título de la sección
         */
        public String getTitle() {
            return title;
        }

        /**
         * Obtiene los elementos de la sección.
         *
         * @return Lista de elementos
         */
        public List<MenuItem> getItems() {
            return items;
        }
    }

    /**
     * Clase interna para representar un elemento del menú.
     */
    public class MenuItem {
        private String text;
        private Runnable action;

        /**
         * Constructor del elemento.
         *
         * @param text Texto del elemento
         * @param action Acción a ejecutar al hacer clic
         */
        public MenuItem(String text, Runnable action) {
            this.text = text;
            this.action = action;
        }

        /**
         * Obtiene el texto del elemento.
         *
         * @return Texto del elemento
         */
        public String getText() {
            return text;
        }

        /**
         * Obtiene la acción del elemento.
         *
         * @return Acción del elemento
         */
        public Runnable getAction() {
            return action;
        }
    }
}
