package com.carmotorsproject.utils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Utility class for Swing components and operations.
 * Provides methods for common Swing tasks and UI enhancements.
 */
public class SwingUtils {

    private static final Logger LOGGER = Logger.getLogger(SwingUtils.class.getName());

    // Common date formats
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    // Common number formats
    public static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");
    public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#0.00%");

    // Common colors
    public static final Color ERROR_COLOR = new Color(255, 200, 200);
    public static final Color WARNING_COLOR = new Color(255, 255, 200);
    public static final Color SUCCESS_COLOR = new Color(200, 255, 200);
    public static final Color HIGHLIGHT_COLOR = new Color(230, 230, 255);

    // Common borders
    public static final Border ERROR_BORDER = BorderFactory.createLineBorder(Color.RED, 1);
    public static final Border WARNING_BORDER = BorderFactory.createLineBorder(Color.ORANGE, 1);
    public static final Border SUCCESS_BORDER = BorderFactory.createLineBorder(Color.GREEN, 1);

    // Private constructor to prevent instantiation
    private SwingUtils() {
        throw new AssertionError("SwingUtils is a utility class and should not be instantiated");
    }

    /**
     * Sets the look and feel to the system look and feel.
     * Falls back to the cross-platform look and feel if the system look and feel is not available.
     */
    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Could not set look and feel", ex);
            }
        }
    }

    /**
     * Sets a specific look and feel by class name.
     *
     * @param className The class name of the look and feel
     * @return true if the look and feel was set successfully, false otherwise
     */
    public static boolean setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not set look and feel: " + className, e);
            return false;
        }
    }

    /**
     * Centers a window on the screen.
     *
     * @param window The window to center
     */
    public static void centerOnScreen(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }

    /**
     * Centers a window relative to another window.
     *
     * @param window The window to center
     * @param parent The parent window
     */
    public static void centerOnParent(Window window, Window parent) {
        if (parent == null) {
            centerOnScreen(window);
            return;
        }

        int x = parent.getX() + (parent.getWidth() - window.getWidth()) / 2;
        int y = parent.getY() + (parent.getHeight() - window.getHeight()) / 2;
        window.setLocation(x, y);
    }

    /**
     * Creates a panel with a grid bag layout.
     *
     * @return A new JPanel with a GridBagLayout
     */
    public static JPanel createGridBagPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        return panel;
    }

    /**
     * Creates a panel with a border layout.
     *
     * @return A new JPanel with a BorderLayout
     */
    public static JPanel createBorderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        return panel;
    }

    /**
     * Creates a panel with a flow layout.
     *
     * @param alignment The alignment (FlowLayout.LEFT, FlowLayout.CENTER, FlowLayout.RIGHT)
     * @return A new JPanel with a FlowLayout
     */
    public static JPanel createFlowPanel(int alignment) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(alignment));
        return panel;
    }

    /**
     * Creates a titled border with the specified title.
     *
     * @param title The title for the border
     * @return A new TitledBorder
     */
    public static TitledBorder createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title);
    }

    /**
     * Creates a scroll pane for a component.
     *
     * @param component The component to scroll
     * @return A new JScrollPane containing the component
     */
    public static JScrollPane createScrollPane(Component component) {
        return new JScrollPane(component);
    }

    /**
     * Creates a label with the specified text.
     *
     * @param text The text for the label
     * @return A new JLabel
     */
    public static JLabel createLabel(String text) {
        return new JLabel(text);
    }

    /**
     * Creates a button with the specified text and action listener.
     *
     * @param text The text for the button
     * @param listener The action listener
     * @return A new JButton
     */
    public static JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        if (listener != null) {
            button.addActionListener(listener);
        }
        return button;
    }

    /**
     * Creates a text field with the specified columns.
     *
     * @param columns The number of columns
     * @return A new JTextField
     */
    public static JTextField createTextField(int columns) {
        return new JTextField(columns);
    }

    /**
     * Creates a formatted text field with the specified mask.
     *
     * @param mask The mask pattern (e.g., "##/##/####" for dates)
     * @param placeholder The placeholder character
     * @return A new JFormattedTextField
     */
    public static JFormattedTextField createFormattedTextField(String mask, char placeholder) {
        try {
            MaskFormatter formatter = new MaskFormatter(mask);
            formatter.setPlaceholderCharacter(placeholder);
            return new JFormattedTextField(formatter);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Invalid mask format: " + mask, e);
            return new JFormattedTextField();
        }
    }

    /**
     * Creates a combo box with the specified items.
     *
     * @param <T> The type of items
     * @param items The items for the combo box
     * @return A new JComboBox
     */
    @SafeVarargs
    public static <T> JComboBox<T> createComboBox(T... items) {
        JComboBox<T> comboBox = new JComboBox<>();
        for (T item : items) {
            comboBox.addItem(item);
        }
        return comboBox;
    }

    /**
     * Creates a spinner with the specified model.
     *
     * @param model The spinner model
     * @return A new JSpinner
     */
    public static JSpinner createSpinner(SpinnerModel model) {
        return new JSpinner(model);
    }

    /**
     * Creates a number spinner with the specified range and step.
     *
     * @param value The initial value
     * @param minimum The minimum value
     * @param maximum The maximum value
     * @param step The step size
     * @return A new JSpinner
     */
    public static JSpinner createNumberSpinner(int value, int minimum, int maximum, int step) {
        return new JSpinner(new SpinnerNumberModel(value, minimum, maximum, step));
    }

    /**
     * Creates a date spinner with the specified date format.
     *
     * @param date The initial date
     * @param format The date format
     * @return A new JSpinner
     */
    public static JSpinner createDateSpinner(Date date, String format) {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, format);
        spinner.setEditor(editor);
        if (date != null) {
            spinner.setValue(date);
        }
        return spinner;
    }

    /**
     * Creates a check box with the specified text and selection state.
     *
     * @param text The text for the check box
     * @param selected The initial selection state
     * @return A new JCheckBox
     */
    public static JCheckBox createCheckBox(String text, boolean selected) {
        return new JCheckBox(text, selected);
    }

    /**
     * Creates a radio button with the specified text and selection state.
     *
     * @param text The text for the radio button
     * @param selected The initial selection state
     * @return A new JRadioButton
     */
    public static JRadioButton createRadioButton(String text, boolean selected) {
        return new JRadioButton(text, selected);
    }

    /**
     * Creates a button group and adds the specified radio buttons.
     *
     * @param buttons The radio buttons to add
     * @return A new ButtonGroup
     */
    public static ButtonGroup createButtonGroup(JRadioButton... buttons) {
        ButtonGroup group = new ButtonGroup();
        for (JRadioButton button : buttons) {
            group.add(button);
        }
        return group;
    }

    /**
     * Creates a text area with the specified rows and columns.
     *
     * @param rows The number of rows
     * @param columns The number of columns
     * @return A new JTextArea
     */
    public static JTextArea createTextArea(int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    /**
     * Creates a password field with the specified columns.
     *
     * @param columns The number of columns
     * @return A new JPasswordField
     */
    public static JPasswordField createPasswordField(int columns) {
        return new JPasswordField(columns);
    }

    /**
     * Creates a table with the specified column names and row data.
     *
     * @param columnNames The column names
     * @param data The row data
     * @return A new JTable
     */
    public static JTable createTable(String[] columnNames, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    /**
     * Creates a file chooser with the specified title and file filters.
     *
     * @param title The title for the file chooser
     * @param filters The file filters
     * @return A new JFileChooser
     */
    public static JFileChooser createFileChooser(String title, FileFilter... filters) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        for (FileFilter filter : filters) {
            chooser.addChoosableFileFilter(filter);
        }
        if (filters.length > 0) {
            chooser.setFileFilter(filters[0]);
        }
        return chooser;
    }

    /**
     * Creates a file filter for the specified description and extensions.
     *
     * @param description The description for the filter
     * @param extensions The file extensions
     * @return A new FileNameExtensionFilter
     */
    public static FileNameExtensionFilter createFileFilter(String description, String... extensions) {
        return new FileNameExtensionFilter(description, extensions);
    }

    /**
     * Creates a menu item with the specified text and action listener.
     *
     * @param text The text for the menu item
     * @param listener The action listener
     * @return A new JMenuItem
     */
    public static JMenuItem createMenuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        if (listener != null) {
            item.addActionListener(listener);
        }
        return item;
    }

    /**
     * Creates a menu with the specified text and menu items.
     *
     * @param text The text for the menu
     * @param items The menu items
     * @return A new JMenu
     */
    public static JMenu createMenu(String text, JMenuItem... items) {
        JMenu menu = new JMenu(text);
        for (JMenuItem item : items) {
            menu.add(item);
        }
        return menu;
    }

    /**
     * Creates a menu bar with the specified menus.
     *
     * @param menus The menus
     * @return A new JMenuBar
     */
    public static JMenuBar createMenuBar(JMenu... menus) {
        JMenuBar menuBar = new JMenuBar();
        for (JMenu menu : menus) {
            menuBar.add(menu);
        }
        return menuBar;
    }

    /**
     * Creates a tool bar with the specified components.
     *
     * @param components The components
     * @return A new JToolBar
     */
    public static JToolBar createToolBar(Component... components) {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        for (Component component : components) {
            toolBar.add(component);
        }
        return toolBar;
    }

    /**
     * Creates a tool bar button with the specified icon and action listener.
     *
     * @param icon The icon for the button
     * @param toolTip The tool tip text
     * @param listener The action listener
     * @return A new JButton
     */
    public static JButton createToolBarButton(Icon icon, String toolTip, ActionListener listener) {
        JButton button = new JButton(icon);
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        if (listener != null) {
            button.addActionListener(listener);
        }
        return button;
    }

    /**
     * Creates a status bar with the specified components.
     *
     * @param components The components
     * @return A new JPanel
     */
    public static JPanel createStatusBar(Component... components) {
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        for (Component component : components) {
            statusBar.add(component);
            statusBar.add(Box.createHorizontalStrut(5));
        }

        return statusBar;
    }

    /**
     * Creates a status label with the specified text.
     *
     * @param text The text for the label
     * @return A new JLabel
     */
    public static JLabel createStatusLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        return label;
    }

    /**
     * Creates a progress bar with the specified minimum and maximum values.
     *
     * @param min The minimum value
     * @param max The maximum value
     * @return A new JProgressBar
     */
    public static JProgressBar createProgressBar(int min, int max) {
        JProgressBar progressBar = new JProgressBar(min, max);
        progressBar.setStringPainted(true);
        return progressBar;
    }

    /**
     * Creates a split pane with the specified components and orientation.
     *
     * @param orientation The orientation (JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT)
     * @param leftComponent The left/top component
     * @param rightComponent The right/bottom component
     * @return A new JSplitPane
     */
    public static JSplitPane createSplitPane(int orientation, Component leftComponent, Component rightComponent) {
        JSplitPane splitPane = new JSplitPane(orientation, leftComponent, rightComponent);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);
        return splitPane;
    }

    /**
     * Creates a tabbed pane with the specified tabs.
     *
     * @param titles The tab titles
     * @param components The tab components
     * @return A new JTabbedPane
     */
    public static JTabbedPane createTabbedPane(String[] titles, Component[] components) {
        if (titles.length != components.length) {
            throw new IllegalArgumentException("The number of titles must match the number of components");
        }

        JTabbedPane tabbedPane = new JTabbedPane();
        for (int i = 0; i < titles.length; i++) {
            tabbedPane.addTab(titles[i], components[i]);
        }
        return tabbedPane;
    }

    /**
     * Creates a dialog with the specified title, content, and buttons.
     *
     * @param parent The parent component
     * @param title The title for the dialog
     * @param content The content component
     * @param modal Whether the dialog is modal
     * @param buttons The buttons
     * @return A new JDialog
     */
    public static JDialog createDialog(Component parent, String title, Component content, boolean modal, JButton... buttons) {
        JDialog dialog = new JDialog((Frame) getWindowForComponent(parent), title, modal);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(content, BorderLayout.CENTER);

        if (buttons.length > 0) {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            for (JButton button : buttons) {
                buttonPanel.add(button);
            }
            dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        }

        dialog.pack();
        centerOnParent(dialog, getWindowForComponent(parent));

        return dialog;
    }

    /**
     * Gets the window for a component.
     *
     * @param component The component
     * @return The window containing the component, or null if not found
     */
    public static Window getWindowForComponent(Component component) {
        if (component == null) {
            return JOptionPane.getRootFrame();
        }
        if (component instanceof Window) {
            return (Window) component;
        }
        return SwingUtilities.getWindowAncestor(component);
    }

    /**
     * Shows an error message dialog.
     *
     * @param parent The parent component
     * @param message The error message
     */
    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a warning message dialog.
     *
     * @param parent The parent component
     * @param message The warning message
     */
    public static void showWarningMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows an information message dialog.
     *
     * @param parent The parent component
     * @param message The information message
     */
    public static void showInfoMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a confirmation dialog.
     *
     * @param parent The parent component
     * @param message The confirmation message
     * @return true if the user confirms, false otherwise
     */
    public static boolean showConfirmDialog(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Shows an input dialog.
     *
     * @param parent The parent component
     * @param message The input message
     * @param initialValue The initial value
     * @return The user input, or null if canceled
     */
    public static String showInputDialog(Component parent, String message, String initialValue) {
        return JOptionPane.showInputDialog(parent, message, initialValue);
    }

    /**
     * Shows a custom dialog with the specified content and buttons.
     *
     * @param parent The parent component
     * @param title The title for the dialog
     * @param content The content component
     * @param buttonTexts The button texts
     * @return The index of the button clicked, or -1 if the dialog was closed
     */
    public static int showCustomDialog(Component parent, String title, Component content, String... buttonTexts) {
        final int[] result = {-1};

        JDialog dialog = new JDialog((Frame) getWindowForComponent(parent), title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(content, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        for (int i = 0; i < buttonTexts.length; i++) {
            final int index = i;
            JButton button = new JButton(buttonTexts[i]);
            button.addActionListener(e -> {
                result[0] = index;
                dialog.dispose();
            });
            buttonPanel.add(button);
        }
        dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        centerOnParent(dialog, getWindowForComponent(parent));
        dialog.setVisible(true);

        return result[0];
    }

    /**
     * Adds a document listener to a text component.
     *
     * @param textComponent The text component
     * @param listener The document listener
     */
    public static void addDocumentListener(JTextComponent textComponent, DocumentListener listener) {
        textComponent.getDocument().addDocumentListener(listener);
    }

    /**
     * Creates a document listener that calls the specified action when the document changes.
     *
     * @param action The action to call
     * @return A new DocumentListener
     */
    public static DocumentListener createDocumentListener(Runnable action) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                action.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                action.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                action.run();
            }
        };
    }

    /**
     * Adds a focus listener to a component.
     *
     * @param component The component
     * @param listener The focus listener
     */
    public static void addFocusListener(Component component, FocusListener listener) {
        component.addFocusListener(listener);
    }

    /**
     * Creates a focus listener that calls the specified actions when the component gains or loses focus.
     *
     * @param gainedAction The action to call when the component gains focus
     * @param lostAction The action to call when the component loses focus
     * @return A new FocusListener
     */
    public static FocusListener createFocusListener(Runnable gainedAction, Runnable lostAction) {
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (gainedAction != null) {
                    gainedAction.run();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (lostAction != null) {
                    lostAction.run();
                }
            }
        };
    }

    /**
     * Adds a key listener to a component.
     *
     * @param component The component
     * @param listener The key listener
     */
    public static void addKeyListener(Component component, KeyListener listener) {
        component.addKeyListener(listener);
    }

    /**
     * Creates a key listener that calls the specified action when a key is pressed.
     *
     * @param action The action to call
     * @return A new KeyListener
     */
    public static KeyListener createKeyListener(Consumer<KeyEvent> action) {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                action.accept(e);
            }
        };
    }

    /**
     * Adds a mouse listener to a component.
     *
     * @param component The component
     * @param listener The mouse listener
     */
    public static void addMouseListener(Component component, MouseListener listener) {
        component.addMouseListener(listener);
    }

    /**
     * Creates a mouse listener that calls the specified action when the mouse is clicked.
     *
     * @param action The action to call
     * @return A new MouseListener
     */
    public static MouseListener createMouseClickListener(Consumer<MouseEvent> action) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.accept(e);
            }
        };
    }

    /**
     * Adds a window listener to a window.
     *
     * @param window The window
     * @param listener The window listener
     */
    public static void addWindowListener(Window window, WindowListener listener) {
        window.addWindowListener(listener);
    }

    /**
     * Creates a window listener that calls the specified action when the window is closed.
     *
     * @param action The action to call
     * @return A new WindowListener
     */
    public static WindowListener createWindowCloseListener(Runnable action) {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                action.run();
            }
        };
    }

    /**
     * Sets the enabled state of multiple components.
     *
     * @param enabled The enabled state
     * @param components The components
     */
    public static void setEnabled(boolean enabled, Component... components) {
        for (Component component : components) {
            component.setEnabled(enabled);
        }
    }

    /**
     * Sets the visible state of multiple components.
     *
     * @param visible The visible state
     * @param components The components
     */
    public static void setVisible(boolean visible, Component... components) {
        for (Component component : components) {
            component.setVisible(visible);
        }
    }

    /**
     * Sets the background color of multiple components.
     *
     * @param color The background color
     * @param components The components
     */
    public static void setBackground(Color color, Component... components) {
        for (Component component : components) {
            component.setBackground(color);
        }
    }

    /**
     * Sets the foreground color of multiple components.
     *
     * @param color The foreground color
     * @param components The components
     */
    public static void setForeground(Color color, Component... components) {
        for (Component component : components) {
            component.setForeground(color);
        }
    }

    /**
     * Sets the font of multiple components.
     *
     * @param font The font
     * @param components The components
     */
    public static void setFont(Font font, Component... components) {
        for (Component component : components) {
            component.setFont(font);
        }
    }

    /**
     * Sets the border of multiple components.
     *
     * @param border The border
     * @param components The components
     */
    public static void setBorder(Border border, JComponent... components) {
        for (JComponent component : components) {
            component.setBorder(border);
        }
    }

    /**
     * Sets the tool tip text of multiple components.
     *
     * @param toolTipText The tool tip text
     * @param components The components
     */
    public static void setToolTipText(String toolTipText, JComponent... components) {
        for (JComponent component : components) {
            component.setToolTipText(toolTipText);
        }
    }

    /**
     * Creates a grid bag constraints object with the specified parameters.
     *
     * @param gridx The grid x position
     * @param gridy The grid y position
     * @param gridwidth The grid width
     * @param gridheight The grid height
     * @param weightx The weight x
     * @param weighty The weight y
     * @param anchor The anchor
     * @param fill The fill
     * @param insets The insets
     * @return A new GridBagConstraints
     */
    public static GridBagConstraints createGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight,
                                                              double weightx, double weighty, int anchor, int fill, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.insets = insets;
        return gbc;
    }

    /**
     * Creates a simple grid bag constraints object with common defaults.
     *
     * @param gridx The grid x position
     * @param gridy The grid y position
     * @return A new GridBagConstraints
     */
    public static GridBagConstraints createGridBagConstraints(int gridx, int gridy) {
        return createGridBagConstraints(gridx, gridy, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5));
    }

    /**
     * Adds a component to a container with a grid bag layout.
     *
     * @param container The container
     * @param component The component
     * @param gridx The grid x position
     * @param gridy The grid y position
     */
    public static void addToGridBag(Container container, Component component, int gridx, int gridy) {
        container.add(component, createGridBagConstraints(gridx, gridy));
    }

    /**
     * Adds a component to a container with a grid bag layout.
     *
     * @param container The container
     * @param component The component
     * @param gridx The grid x position
     * @param gridy The grid y position
     * @param gridwidth The grid width
     * @param gridheight The grid height
     * @param weightx The weight x
     * @param weighty The weight y
     * @param anchor The anchor
     * @param fill The fill
     * @param insets The insets
     */
    public static void addToGridBag(Container container, Component component, int gridx, int gridy,
                                    int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets) {
        container.add(component, createGridBagConstraints(gridx, gridy, gridwidth, gridheight,
                weightx, weighty, anchor, fill, insets));
    }

    /**
     * Sets the column widths of a table.
     *
     * @param table The table
     * @param widths The column widths
     */
    public static void setColumnWidths(JTable table, int... widths) {
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }

    /**
     * Sets the column headers of a table.
     *
     * @param table The table
     * @param headers The column headers
     */
    public static void setColumnHeaders(JTable table, String... headers) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setColumnIdentifiers(headers);
    }

    /**
     * Sets a custom cell renderer for a table column.
     *
     * @param table The table
     * @param column The column index
     * @param renderer The cell renderer
     */
    public static void setColumnRenderer(JTable table, int column, DefaultTableCellRenderer renderer) {
        table.getColumnModel().getColumn(column).setCellRenderer(renderer);
    }

    /**
     * Creates a table row sorter for a table.
     *
     * @param table The table
     * @return A new TableRowSorter
     */
    public static TableRowSorter<DefaultTableModel> createTableRowSorter(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        return sorter;
    }

    /**
     * Clears a table.
     *
     * @param table The table
     */
    public static void clearTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }

    /**
     * Adds a row to a table.
     *
     * @param table The table
     * @param rowData The row data
     */
    public static void addTableRow(JTable table, Object... rowData) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(rowData);
    }

    /**
     * Gets the selected row data from a table.
     *
     * @param table The table
     * @return The selected row data, or null if no row is selected
     */
    public static Object[] getSelectedRowData(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }

        int columnCount = table.getColumnCount();
        Object[] rowData = new Object[columnCount];

        for (int i = 0; i < columnCount; i++) {
            rowData[i] = table.getValueAt(selectedRow, i);
        }

        return rowData;
    }

    /**
     * Gets the selected row index from a table.
     *
     * @param table The table
     * @return The selected row index, or -1 if no row is selected
     */
    public static int getSelectedRowIndex(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return -1;
        }

        return table.convertRowIndexToModel(selectedRow);
    }

    /**
     * Validates that a text field is not empty.
     *
     * @param textField The text field
     * @param fieldName The field name for the error message
     * @return true if the text field is not empty, false otherwise
     */
    public static boolean validateNotEmpty(JTextField textField, String fieldName) {
        if (textField.getText().trim().isEmpty()) {
            textField.setBorder(ERROR_BORDER);
            showErrorMessage(textField, fieldName + " no puede estar vacío.");
            textField.requestFocus();
            return false;
        }

        textField.setBorder(UIManager.getBorder("TextField.border"));
        return true;
    }

    /**
     * Validates that a text field contains a valid integer.
     *
     * @param textField The text field
     * @param fieldName The field name for the error message
     * @return true if the text field contains a valid integer, false otherwise
     */
    public static boolean validateInteger(JTextField textField, String fieldName) {
        try {
            if (!textField.getText().trim().isEmpty()) {
                Integer.parseInt(textField.getText().trim());
            }
            textField.setBorder(UIManager.getBorder("TextField.border"));
            return true;
        } catch (NumberFormatException e) {
            textField.setBorder(ERROR_BORDER);
            showErrorMessage(textField, fieldName + " debe ser un número entero válido.");
            textField.requestFocus();
            return false;
        }
    }

    /**
     * Validates that a text field contains a valid double.
     *
     * @param textField The text field
     * @param fieldName The field name for the error message
     * @return true if the text field contains a valid double, false otherwise
     */
    public static boolean validateDouble(JTextField textField, String fieldName) {
        try {
            if (!textField.getText().trim().isEmpty()) {
                Double.parseDouble(textField.getText().trim());
            }
            textField.setBorder(UIManager.getBorder("TextField.border"));
            return true;
        } catch (NumberFormatException e) {
            textField.setBorder(ERROR_BORDER);
            showErrorMessage(textField, fieldName + " debe ser un número decimal válido.");
            textField.requestFocus();
            return false;
        }
    }

    /**
     * Validates that a text field contains a valid date.
     *
     * @param textField The text field
     * @param fieldName The field name for the error message
     * @param dateFormat The date format
     * @return true if the text field contains a valid date, false otherwise
     */
    public static boolean validateDate(JTextField textField, String fieldName, SimpleDateFormat dateFormat) {
        try {
            if (!textField.getText().trim().isEmpty()) {
                dateFormat.parse(textField.getText().trim());
            }
            textField.setBorder(UIManager.getBorder("TextField.border"));
            return true;
        } catch (ParseException e) {
            textField.setBorder(ERROR_BORDER);
            showErrorMessage(textField, fieldName + " debe ser una fecha válida en formato " +
                    dateFormat.toPattern() + ".");
            textField.requestFocus();
            return false;
        }
    }

    /**
     * Validates that a text field contains a valid email address.
     *
     * @param textField The text field
     * @param fieldName The field name for the error message
     * @return true if the text field contains a valid email address, false otherwise
     */
    public static boolean validateEmail(JTextField textField, String fieldName) {
        String email = textField.getText().trim();
        if (email.isEmpty()) {
            textField.setBorder(UIManager.getBorder("TextField.border"));
            return true;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (Pattern.matches(emailRegex, email)) {
            textField.setBorder(UIManager.getBorder("TextField.border"));
            return true;
        } else {
            textField.setBorder(ERROR_BORDER);
            showErrorMessage(textField, fieldName + " debe ser una dirección de correo electrónico válida.");
            textField.requestFocus();
            return false;
        }
    }

    /**
     * Validates that a text field contains a valid phone number.
     *
     * @param textField The text field
     * @param fieldName The field name for the error message
     * @return true if the text field contains a valid phone number, false otherwise
     */
    public static boolean validatePhone(JTextField textField, String fieldName) {
        String phone = textField.getText().trim();
        if (phone.isEmpty()) {
            textField.setBorder(UIManager.getBorder("TextField.border"));
            return true;
        }

        String phoneRegex = "^[0-9\\-\\+\\s()]*$";
        if (Pattern.matches(phoneRegex, phone)) {
            textField.setBorder(UIManager.getBorder("TextField.border"));
            return true;
        } else {
            textField.setBorder(ERROR_BORDER);
            showErrorMessage(textField, fieldName + " debe ser un número de teléfono válido.");
            textField.requestFocus();
            return false;
        }
    }

    /**
     * Validates that a text field contains a value within a specified range.
     *
     * @param textField The text field
     * @param fieldName The field name for the error message
     * @param min The minimum value
     * @param max The maximum value
     * @return true if the text field contains a value within the specified range, false otherwise
     */
    public static boolean validateRange(JTextField textField, String fieldName, double min, double max) {
        try {
            if (!textField.getText().trim().isEmpty()) {
                double value = Double.parseDouble(textField.getText().trim());
                if (value < min || value > max) {
                    textField.setBorder(ERROR_BORDER);
                    showErrorMessage(textField, fieldName + " debe estar entre " + min + " y " + max + ".");
                    textField.requestFocus();
                    return false;
                }
            }
            textField.setBorder(UIManager.getBorder("TextField.border"));
            return true;
        } catch (NumberFormatException e) {
            textField.setBorder(ERROR_BORDER);
            showErrorMessage(textField, fieldName + " debe ser un número válido.");
            textField.requestFocus();
            return false;
        }
    }

    /**
     * Validates that a text field contains a value that matches a regular expression.
     *
     * @param textField The text field
     * @param fieldName The field name for the error message
     * @param regex The regular expression
     * @param errorMessage The error message
     * @return true if the text field contains a value that matches the regular expression, false otherwise
     */
    public static boolean validateRegex(JTextField textField, String fieldName, String regex, String errorMessage) {
        String value = textField.getText().trim();
        if (value.isEmpty()) {
            textField.setBorder(UIManager.getBorder("TextField.border"));
            return true;
        }

        if (Pattern.matches(regex, value)) {
            textField.setBorder(UIManager.getBorder("TextField.border"));
            return true;
        } else {
            textField.setBorder(ERROR_BORDER);
            showErrorMessage(textField, fieldName + " " + errorMessage);
            textField.requestFocus();
            return false;
        }
    }

    /**
     * Validates that a combo box has a selected item.
     *
     * @param comboBox The combo box
     * @param fieldName The field name for the error message
     * @return true if the combo box has a selected item, false otherwise
     */
    public static boolean validateComboBox(JComboBox<?> comboBox, String fieldName) {
        if (comboBox.getSelectedItem() == null) {
            comboBox.setBorder(ERROR_BORDER);
            showErrorMessage(comboBox, "Debe seleccionar un " + fieldName + ".");
            comboBox.requestFocus();
            return false;
        }

        comboBox.setBorder(UIManager.getBorder("ComboBox.border"));
        return true;
    }

    /**
     * Validates multiple conditions.
     *
     * @param conditions The conditions to validate
     * @return true if all conditions are true, false otherwise
     */
    public static boolean validateAll(boolean... conditions) {
        for (boolean condition : conditions) {
            if (!condition) {
                return false;
            }
        }
        return true;
    }

    /**
     * Formats a date as a string.
     *
     * @param date The date
     * @param format The date format
     * @return The formatted date string
     */
    public static String formatDate(Date date, SimpleDateFormat format) {
        if (date == null) {
            return "";
        }
        return format.format(date);
    }

    /**
     * Parses a date string.
     *
     * @param dateString The date string
     * @param format The date format
     * @return The parsed date, or null if the string is empty or invalid
     */
    public static Date parseDate(String dateString, SimpleDateFormat format) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            return format.parse(dateString.trim());
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Error parsing date: " + dateString, e);
            return null;
        }
    }

    /**
     * Formats a number as a string.
     *
     * @param number The number
     * @param format The number format
     * @return The formatted number string
     */
    public static String formatNumber(Number number, DecimalFormat format) {
        if (number == null) {
            return "";
        }
        return format.format(number);
    }

    /**
     * Parses a number string.
     *
     * @param numberString The number string
     * @return The parsed number, or null if the string is empty or invalid
     */
    public static Number parseNumber(String numberString) {
        if (numberString == null || numberString.trim().isEmpty()) {
            return null;
        }

        try {
            return Double.parseDouble(numberString.trim());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Error parsing number: " + numberString, e);
            return null;
        }
    }

    /**
     * Executes a task on the Event Dispatch Thread.
     *
     * @param task The task to execute
     */
    public static void invokeLater(Runnable task) {
        SwingUtilities.invokeLater(task);
    }

    /**
     * Executes a task on the Event Dispatch Thread and waits for it to complete.
     *
     * @param task The task to execute
     * @throws Exception If an exception occurs during execution
     */
    public static void invokeAndWait(Runnable task) throws Exception {
        SwingUtilities.invokeAndWait(task);
    }

    /**
     * Executes a task on the Event Dispatch Thread and returns the result.
     *
     * @param <T> The result type
     * @param task The task to execute
     * @return The result of the task
     * @throws Exception If an exception occurs during execution
     */
    public static <T> T invokeAndWait(Callable<T> task) throws Exception {
        final T[] result = (T[]) new Object[1];
        final Exception[] exception = new Exception[1];

        SwingUtilities.invokeAndWait(() -> {
            try {
                result[0] = task.call();
            } catch (Exception e) {
                exception[0] = e;
            }
        });

        if (exception[0] != null) {
            throw exception[0];
        }

        return result[0];
    }

    /**
     * Creates a map from an array of key-value pairs.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param entries The key-value pairs
     * @return A new map
     */
    @SafeVarargs
    public static <K, V> Map<K, V> createMap(Map.Entry<K, V>... entries) {
        Map<K, V> map = new HashMap<>();
        for (Map.Entry<K, V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * Creates a map entry.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param key The key
     * @param value The value
     * @return A new map entry
     */
    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new Map.Entry<K, V>() {
            @Override
            public K getKey() {
                return key;
            }

            @Override
            public V getValue() {
                return value;
            }

            @Override
            public V setValue(V value) {
                throw new UnsupportedOperationException("Entry is immutable");
            }
        };
    }

    /**
     * Gets the screen size.
     *
     * @return The screen size
     */
    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Gets the screen width.
     *
     * @return The screen width
     */
    public static int getScreenWidth() {
        return getScreenSize().width;
    }

    /**
     * Gets the screen height.
     *
     * @return The screen height
     */
    public static int getScreenHeight() {
        return getScreenSize().height;
    }

    /**
     * Scales a dimension by a factor.
     *
     * @param dimension The dimension
     * @param factor The scaling factor
     * @return The scaled dimension
     */
    public static Dimension scaleDimension(Dimension dimension, double factor) {
        return new Dimension((int) (dimension.width * factor), (int) (dimension.height * factor));
    }

    /**
     * Gets a dimension that is a percentage of the screen size.
     *
     * @param widthPercent The width percentage
     * @param heightPercent The height percentage
     * @return The dimension
     */
    public static Dimension getScreenPercentageDimension(double widthPercent, double heightPercent) {
        Dimension screenSize = getScreenSize();
        return new Dimension((int) (screenSize.width * widthPercent), (int) (screenSize.height * heightPercent));
    }

    /**
     * Sets the size of a component to a percentage of the screen size.
     *
     * @param component The component
     * @param widthPercent The width percentage
     * @param heightPercent The height percentage
     */
    public static void setSizeAsScreenPercentage(Component component, double widthPercent, double heightPercent) {
        component.setSize(getScreenPercentageDimension(widthPercent, heightPercent));
    }

    /**
     * Sets the preferred size of a component to a percentage of the screen size.
     *
     * @param component The component
     * @param widthPercent The width percentage
     * @param heightPercent The height percentage
     */
    public static void setPreferredSizeAsScreenPercentage(JComponent component, double widthPercent, double heightPercent) {
        component.setPreferredSize(getScreenPercentageDimension(widthPercent, heightPercent));
    }

    /**
     * Sets the minimum size of a component to a percentage of the screen size.
     *
     * @param component The component
     * @param widthPercent The width percentage
     * @param heightPercent The height percentage
     */
    public static void setMinimumSizeAsScreenPercentage(JComponent component, double widthPercent, double heightPercent) {
        component.setMinimumSize(getScreenPercentageDimension(widthPercent, heightPercent));
    }

    /**
     * Sets the maximum size of a component to a percentage of the screen size.
     *
     * @param component The component
     * @param widthPercent The width percentage
     * @param heightPercent The height percentage
     */
    public static void setMaximumSizeAsScreenPercentage(JComponent component, double widthPercent, double heightPercent) {
        component.setMaximumSize(getScreenPercentageDimension(widthPercent, heightPercent));
    }
}