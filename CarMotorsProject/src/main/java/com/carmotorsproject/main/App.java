package com.carmotorsproject.main;

import com.carmotorsproject.config.AppConfig;
import com.carmotorsproject.config.DatabaseConnection;
import com.carmotorsproject.parts.views.PartView;
import com.carmotorsproject.parts.views.SupplierView;
import com.carmotorsproject.parts.views.PurchaseOrderView;
import com.carmotorsproject.services.views.ServiceView;
import com.carmotorsproject.services.views.VehicleView;
import com.carmotorsproject.services.views.TechnicianView;
import com.carmotorsproject.customers.views.CustomerView;
import com.carmotorsproject.invoices.views.InvoiceView;
import com.carmotorsproject.campaigns.views.CampaignView;
import com.carmotorsproject.ui.animation.AnimationManager;
import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.ModernCard;
import com.carmotorsproject.ui.components.ModernHeader;
import com.carmotorsproject.ui.components.ModernSideMenu;
import com.carmotorsproject.ui.components.TransparentPanel;
import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.config.DatabaseConfigDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase principal para la aplicación CarMotorsProject.
 * Esta clase contiene el método main para iniciar la aplicación y la configuración inicial.
 */
/**
 * Punto de entrada principal para iniciar la aplicación.
 */
public class App implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final String APP_TITLE = "Car Motors Workshop - Sistema de Gestión";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private JFrame mainFrame;
    private JPanel mainPanel;
    private ModernSideMenu sideMenu;
    private JPanel contentPanel;
    private ModernHeader header;
    private CardLayout cardLayout;
    private boolean menuCollapsed = false;

    // Seguimiento de la vista actual
    private JFrame currentViewFrame;
    private String currentView = "welcome";

    // Estado de la base de datos
    private boolean databaseConnected = false;

    /**
     * Constructor de la clase App.
     * Inicializa la interfaz gráfica de usuario.
     */
    public App() {
        //initialize();
    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     */
    private void initialize() {
        /*frame = new JFrame("CarMotorsProject");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Crear un panel para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Botón para el formulario de clientes
        JButton clienteButton = new JButton("Clientes");
        clienteButton.addActionListener(this::abrirFormularioCliente);
        buttonPanel.add(clienteButton);

        // Botón para el formulario de vehículos
        JButton vehiculoButton = new JButton("Vehículos");
        vehiculoButton.addActionListener(this::abrirFormularioVehiculo);
        buttonPanel.add(vehiculoButton);

        // Botón para el formulario de facturas
        JButton facturaButton = new JButton("Facturas");
        facturaButton.addActionListener(this::abrirFormularioFactura);
        buttonPanel.add(facturaButton);

        // Añadir el panel de botones al centro del frame
        frame.add(buttonPanel, BorderLayout.CENTER);*/
    }

    /**
     * Configura el logging de la aplicación.
     */
    private static void configureLogging() {
        // En una implementación real, esto configuraría manejadores de log, formateadores, etc.
        LOGGER.info("Logging configurado");
    }

    /**
     * Muestra un diálogo de error con el mensaje especificado.
     *
     * @param title   Título del diálogo.
     * @param message Mensaje de error a mostrar.
     */
    private static void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Prueba la conexión a la base de datos.
     *
     * @throws SQLException Si la conexión a la base de datos falla
     */
    private static boolean testDatabaseConnection() {
        LOGGER.info("Probando conexión a la base de datos");

        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection connection = null;
        boolean connected = false;

        try {
            connection = dbConnection.getConnection();
            LOGGER.info("Prueba de conexión a la base de datos exitosa");
            connected = true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de conexión a la base de datos: " + e.getMessage(), e);

            // Mostrar diálogo de advertencia pero permitir que la aplicación continúe
            SwingUtilities.invokeLater(() -> {
                int option = JOptionPane.showOptionDialog(
                        null,
                        "No se pudo conectar a la base de datos.\n\n" +
                                "Error: " + e.getMessage() + "\n\n" +
                                "¿Desea configurar la conexión a la base de datos ahora?",
                        "Error de Conexión a la Base de Datos",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        new String[]{"Configurar Ahora", "Continuar en Modo Offline"},
                        "Configurar Ahora");

                if (option == JOptionPane.YES_OPTION) {
                    // Mostrar diálogo de configuración de base de datos
                    DatabaseConfigDialog configDialog = new DatabaseConfigDialog(null);
                    configDialog.setVisible(true);
                }
            });
        } finally {
            if (connection != null) {
                try {
                    dbConnection.closeConnection(connection);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error al cerrar la conexión", e);
                }
            }
        }

        return connected;
    }

    /**
     * Establece el estado de conexión de la base de datos.
     *
     * @param connected true si está conectado, false en caso contrario
     */
    public void setDatabaseConnected(boolean connected) {
        this.databaseConnected = connected;

        // Actualizar la interfaz si ya está creada
        if (header != null) {
            updateDatabaseStatus();
        }
    }

    /**
     * Actualiza el indicador de estado de la base de datos en la interfaz.
     */
    private void updateDatabaseStatus() {
        if (databaseConnected) {
            header.setDatabaseStatus(true, "Conectado a la base de datos");
        } else {
            header.setDatabaseStatus(false, "Modo Offline - Sin conexión a la base de datos");
        }
    }

    /**
     * Crea y muestra la interfaz gráfica de usuario.
     */
    private void createAndShowGUI() {
        LOGGER.info("Creando GUI de la aplicación");

        // Crear frame principal
        mainFrame = new JFrame(APP_TITLE);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainFrame.setLocationRelativeTo(null); // Centrar en pantalla
        mainFrame.setBackground(AppTheme.PRIMARY_BLACK);

        // Añadir listener de cierre de ventana
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });

        // Crear panel principal con BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppTheme.PRIMARY_BLACK);

        // Crear header
        header = new ModernHeader(APP_TITLE);
        header.addPropertyChangeListener(this);

        // Actualizar estado de la base de datos
        updateDatabaseStatus();

        // Crear menú lateral
        createSideMenu();

        // Crear panel de contenido
        createContentPanel();

        // Añadir paneles al panel principal
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(sideMenu, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Añadir panel principal al frame
        mainFrame.add(mainPanel);

        // Mostrar el frame
        mainFrame.setVisible(true);

        // Mostrar panel de bienvenida con animación
        showWelcomePanel();

        LOGGER.info("GUI de la aplicación creada y mostrada");
    }

    /**
     * Crea el menú lateral con las secciones y elementos.
     */
    private void createSideMenu() {
        LOGGER.info("Creando menú lateral");

        sideMenu = new ModernSideMenu();
        sideMenu.addPropertyChangeListener(this);

        // Sección de inicio
        ModernSideMenu.MenuSection homeSection = sideMenu.addSection("");
        homeSection.addItem("Inicio", () -> showWelcomePanel());

        // Sección de inventario
        ModernSideMenu.MenuSection inventorySection = sideMenu.addSection("Inventario");
        inventorySection.addItem("Repuestos", () -> openView(new PartView()));
        inventorySection.addItem("Proveedores", () -> openView(new SupplierView()));
        inventorySection.addItem("Órdenes de Compra", () -> openView(new PurchaseOrderView()));

        // Sección de servicios
        ModernSideMenu.MenuSection servicesSection = sideMenu.addSection("Servicios");
        servicesSection.addItem("Servicios", () -> openView(new ServiceView()));
        servicesSection.addItem("Vehículos", () -> openView(new VehicleView()));
        servicesSection.addItem("Técnicos", () -> openView(new TechnicianView()));

        // Sección de clientes
        ModernSideMenu.MenuSection customersSection = sideMenu.addSection("Clientes");
        customersSection.addItem("Clientes", () -> openView(new CustomerView()));

        // Sección de facturación
        ModernSideMenu.MenuSection billingSection = sideMenu.addSection("Facturación");
        billingSection.addItem("Facturas", () -> openView(new InvoiceView()));

        // Sección de campañas
        ModernSideMenu.MenuSection campaignsSection = sideMenu.addSection("Campañas");
        campaignsSection.addItem("Campañas", () -> openView(new CampaignView()));

        // Sección de configuración
        ModernSideMenu.MenuSection configSection = sideMenu.addSection("Configuración");
        configSection.addItem("Base de Datos", () -> showDatabaseConfig());

        // Construir el menú
        sideMenu.buildMenu();
    }

    /**
     * Muestra el diálogo de configuración de la base de datos.
     */
    private void showDatabaseConfig() {
        DatabaseConfigDialog configDialog = new DatabaseConfigDialog(mainFrame);
        configDialog.setVisible(true);

        // Actualizar estado de conexión después de cerrar el diálogo
        setDatabaseConnected(DatabaseConnection.getInstance().testConnection());
    }

    /**
     * Crea el panel de contenido donde se mostrarán las vistas.
     */
    private void createContentPanel() {
        LOGGER.info("Creando panel de contenido");

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppTheme.PRIMARY_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Muestra el panel de bienvenida en el panel de contenido.
     */
    private void showWelcomePanel() {
        LOGGER.info("Mostrando panel de bienvenida");

        // Cerrar cualquier frame de vista abierto
        closeCurrentViewFrame();

        // Actualizar título del encabezado
        header.setHeaderTitle("Inicio - Dashboard");

        // Si ya existe el panel de bienvenida, simplemente mostrarlo
        if (contentPanel.getComponentCount() > 0 && currentView.equals("welcome")) {
            cardLayout.show(contentPanel, "welcome");
            return;
        }

        // Crear panel de bienvenida
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBackground(AppTheme.PRIMARY_WHITE);

        // Panel superior con mensaje de bienvenida
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 0, 40, 0));

        JLabel welcomeLabel = new JLabel("Bienvenido al Sistema de Gestión");
        welcomeLabel.setFont(AppTheme.TITLE_FONT);
        welcomeLabel.setForeground(AppTheme.PRIMARY_BLACK);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(welcomeLabel);

        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel versionLabel = new JLabel("Versión " + AppConfig.getAppVersion());
        versionLabel.setFont(AppTheme.REGULAR_FONT);
        versionLabel.setForeground(AppTheme.ACCENT_GRAY);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(versionLabel);

        // Mostrar estado de la base de datos
        JLabel dbStatusLabel = new JLabel(databaseConnected ?
                "Estado: Conectado a la base de datos" :
                "Estado: Modo Offline - Sin conexión a la base de datos");
        dbStatusLabel.setFont(AppTheme.REGULAR_FONT);
        dbStatusLabel.setForeground(databaseConnected ? AppTheme.SUCCESS_COLOR : AppTheme.WARNING_COLOR);
        dbStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(dbStatusLabel);

        welcomePanel.add(topPanel, BorderLayout.NORTH);

        // Panel central con tarjetas de acceso rápido
        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Añadir tarjetas de acceso rápido
        cardsPanel.add(createModuleCard("Repuestos", "Gestión de inventario de repuestos",
                "/icons/parts.png", () -> openView(new PartView())));

        cardsPanel.add(createModuleCard("Servicios", "Registro y seguimiento de servicios",
                "/icons/services.png", () -> openView(new ServiceView())));

        cardsPanel.add(createModuleCard("Clientes", "Administración de clientes",
                "/icons/customers.png", () -> openView(new CustomerView())));

        cardsPanel.add(createModuleCard("Vehículos", "Registro de vehículos",
                "/icons/vehicles.png", () -> openView(new VehicleView())));

        cardsPanel.add(createModuleCard("Facturas", "Generación y gestión de facturas",
                "/icons/invoices.png", () -> openView(new InvoiceView())));

        cardsPanel.add(createModuleCard("Proveedores", "Gestión de proveedores",
                "/icons/suppliers.png", () -> openView(new SupplierView())));

        welcomePanel.add(cardsPanel, BorderLayout.CENTER);

        // Panel inferior con botones de acción
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setOpaque(false);
        actionPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Botón de configuración de base de datos
        ModernButton dbConfigButton = new ModernButton("Configurar Base de Datos");
        dbConfigButton.setColors(AppTheme.PRIMARY_BLACK, AppTheme.SECONDARY_BLACK,
                AppTheme.SECONDARY_BLACK.darker(), AppTheme.PRIMARY_WHITE);
        dbConfigButton.addActionListener(e -> showDatabaseConfig());
        actionPanel.add(dbConfigButton);

        welcomePanel.add(actionPanel, BorderLayout.SOUTH);

        // Añadir al panel de contenido con CardLayout
        contentPanel.add(welcomePanel, "welcome");
        cardLayout.show(contentPanel, "welcome");
        currentView = "welcome";

        // Aplicar animación de fade-in
        AnimationManager.fadeIn(welcomePanel, 500);
    }

    /**
     * Crea una tarjeta de módulo para el panel de bienvenida.
     *
     * @param title Título de la tarjeta
     * @param description Descripción de la tarjeta
     * @param iconPath Ruta al icono
     * @param action Acción a ejecutar al hacer clic
     * @return La tarjeta creada
     */
    private ModernCard createModuleCard(String title, String description, String iconPath, Runnable action) {
        return new ModernCard(title, description, iconPath, action);
    }

    /**
     * Abre el formulario de clientes.
     */
    private void abrirFormularioCliente() {
        // Implementación futura
    }

    /**
     * Abre el formulario de vehículos.
     */
    private void abrirFormularioVehiculo() {
        // Implementación futura
    }

    /**
     * Abre el formulario de facturas.
     */
    private void abrirFormularioFactura() {
        // Implementación futura
    }

    /**
     * Abre la vista especificada.
     * Este método maneja tanto vistas JPanel como JFrame.
     *
     * @param view La vista a abrir
     */
    private void openView(Object view) {
        LOGGER.log(Level.INFO, "Abriendo vista: {0}", view.getClass().getSimpleName());

        // Cerrar cualquier frame de vista abierto
        closeCurrentViewFrame();

        // Actualizar título del encabezado
        header.setHeaderTitle(view.getClass().getSimpleName().replace("View", ""));

        if (view instanceof JPanel) {
            // Si la vista es un JPanel, añadirla al panel de contenido
            JPanel viewPanel = (JPanel) view;

            // Crear un panel contenedor con borde
            JPanel containerPanel = new TransparentPanel(AppTheme.PRIMARY_WHITE, 0.95f);
            containerPanel.setLayout(new BorderLayout());
            containerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            containerPanel.add(viewPanel, BorderLayout.CENTER);

            // Añadir al panel de contenido con CardLayout
            String viewName = view.getClass().getSimpleName();
            contentPanel.add(containerPanel, viewName);

            // Aplicar animación de transición
            cardLayout.show(contentPanel, viewName);
            currentView = viewName;

            // Aplicar animación de fade-in
            AnimationManager.fadeIn(containerPanel, 300);

            LOGGER.log(Level.INFO, "Vista JPanel añadida al panel de contenido: {0}", viewName);
        } else if (view instanceof JFrame) {
            // Si la vista es un JFrame, mostrarla como una ventana separada
            JFrame viewFrame = (JFrame) view;

            // Establecer la vista para que esté centrada respecto al frame principal
            viewFrame.setLocationRelativeTo(mainFrame);

            // Mostrar la vista
            viewFrame.setVisible(true);

            // Guardar referencia a la vista actual
            currentViewFrame = viewFrame;

            // Añadir un mensaje de marcador de posición en el panel de contenido
            JPanel placeholderPanel = new TransparentPanel(AppTheme.PRIMARY_WHITE, 0.9f);
            placeholderPanel.setLayout(new BorderLayout());

            JLabel placeholderLabel = new JLabel("Vista abierta en ventana separada: " + view.getClass().getSimpleName());
            placeholderLabel.setFont(AppTheme.SUBTITLE_FONT);
            placeholderLabel.setForeground(AppTheme.PRIMARY_BLACK);
            placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);

            placeholderPanel.add(placeholderLabel, BorderLayout.CENTER);

            // Añadir al panel de contenido con CardLayout
            String viewName = "placeholder_" + view.getClass().getSimpleName();
            contentPanel.add(placeholderPanel, viewName);
            cardLayout.show(contentPanel, viewName);
            currentView = viewName;

            // Aplicar animación de fade-in
            AnimationManager.fadeIn(placeholderPanel, 300);

            LOGGER.log(Level.INFO, "Vista JFrame abierta en ventana separada: {0}", view.getClass().getSimpleName());
        } else {
            // Si la vista no es ni JPanel ni JFrame, mostrar un mensaje de error
            JPanel errorPanel = new TransparentPanel(AppTheme.PRIMARY_WHITE, 0.9f);
            errorPanel.setLayout(new BorderLayout());

            JLabel errorLabel = new JLabel("Error: Tipo de vista no soportado: " + view.getClass().getSimpleName());
            errorLabel.setFont(AppTheme.SUBTITLE_FONT);
            errorLabel.setForeground(AppTheme.ERROR_COLOR);
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

            errorPanel.add(errorLabel, BorderLayout.CENTER);

            // Añadir al panel de contenido con CardLayout
            String viewName = "error";
            contentPanel.add(errorPanel, viewName);
            cardLayout.show(contentPanel, viewName);
            currentView = viewName;

            // Aplicar animación de fade-in
            AnimationManager.fadeIn(errorPanel, 300);

            LOGGER.log(Level.SEVERE, "Tipo de vista no soportado: {0}", view.getClass().getSimpleName());
        }
    }

    /**
     * Cierra el frame de vista actual si hay uno abierto.
     */
    private void closeCurrentViewFrame() {
        if (currentViewFrame != null && currentViewFrame.isVisible()) {
            LOGGER.log(Level.INFO, "Cerrando frame de vista actual: {0}", currentViewFrame.getClass().getSimpleName());
            currentViewFrame.dispose();
            currentViewFrame = null;
        }
    }

    /**
     * Muestra el diálogo de configuración.
     */
    private void showConfigDialog() {
        LOGGER.info("Mostrando diálogo de configuración");

        // Aquí se implementaría el diálogo de configuración
        JOptionPane.showMessageDialog(mainFrame,
                "Funcionalidad de configuración en desarrollo",
                "Configuración",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Sale de la aplicación después de confirmación.
     */
    private void exitApplication() {
        LOGGER.info("Solicitud de salida de la aplicación");

        // Crear un panel personalizado para el diálogo de confirmación
        JPanel confirmPanel = new JPanel(new BorderLayout(10, 10));
        confirmPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel confirmLabel = new JLabel("¿Está seguro que desea salir de la aplicación?");
        confirmLabel.setFont(AppTheme.REGULAR_FONT);
        confirmPanel.add(confirmLabel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                mainFrame,
                confirmPanel,
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            LOGGER.info("Salida de la aplicación confirmada");

            // Cerrar cualquier frame de vista abierto
            closeCurrentViewFrame();

            // Detener el reloj del encabezado
            header.stopClock();

            // Disponer el frame principal
            mainFrame.dispose();

            // Salir de la aplicación
            System.exit(0);
        }
    }

    /**
     * Alterna la visibilidad del menú lateral.
     */
    private void toggleMenu() {
        menuCollapsed = !menuCollapsed;

        if (menuCollapsed) {
            // Ocultar menú con animación
            AnimationManager.slideOutToRight(sideMenu, 300, false);
            sideMenu.setVisible(false);
        } else {
            // Mostrar menú con animación
            sideMenu.setVisible(true);
            AnimationManager.slideInFromLeft(sideMenu, 300);
        }
    }

    /**
     * Maneja los eventos de cambio de propiedad.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("toggleMenu".equals(evt.getPropertyName())) {
            toggleMenu();
        } else if ("exitApplication".equals(evt.getPropertyName())) {
            exitApplication();
        } else if ("menuItemSelected".equals(evt.getPropertyName())) {
            if (evt.getNewValue() instanceof ModernSideMenu.MenuItem) {
                ModernSideMenu.MenuItem item = (ModernSideMenu.MenuItem) evt.getNewValue();
                if (item.getAction() != null) {
                    item.getAction().run();
                }
            }
        }
    }

    /**
     * Método principal para iniciar la aplicación.
     *
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // Configurar logging
        configureLogging();

        // Usar SwingUtilities.invokeLater para seguridad de hilos
        SwingUtilities.invokeLater(() -> {
            try {
                // Establecer look and feel moderno
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Probar conexión a la base de datos
                boolean dbConnected = testDatabaseConnection();

                // Crear y mostrar la aplicación
                App app = new App();
                app.setDatabaseConnected(dbConnected);
                app.createAndShowGUI();

                LOGGER.info("Aplicación iniciada correctamente");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al iniciar la aplicación", e);
                showErrorDialog("Error al iniciar la aplicación", e.getMessage());
            }
        });
    }
}
