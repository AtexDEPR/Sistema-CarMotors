package com.carmotorsproject.invoices.views;

import com.carmotorsproject.invoices.controller.InvoiceController;
import com.carmotorsproject.invoices.model.Invoice;
import com.carmotorsproject.services.model.Service;
import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.utils.PDFGenerator;
import com.carmotorsproject.utils.EmailService;
import com.carmotorsproject.utils.DateUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Swing UI for managing invoices.
 * Provides a user interface for viewing, creating, and managing invoices.
 */
public class InvoiceView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(InvoiceView.class.getName());

    // Controller
    private InvoiceController controller;

    // Utilities
    private final PDFGenerator pdfGenerator;
    private final EmailService emailService;
    private final DateUtil dateUtil;
    private final DecimalFormat currencyFormat;
    private final SimpleDateFormat dateFormat;

    // UI Components - Main Panels
    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private JPanel searchPanel;
    private JPanel statusPanel;

    // UI Components - Form Fields
    private JTextField invoiceIdField;
    private JTextField invoiceNumberField;
    private JComboBox<String> serviceComboBox;
    private JComboBox<String> customerComboBox;
    private JTextField issueDateField;
    private JTextField dueDateField;
    private JTextField subtotalField;
    private JTextField taxRateField;
    private JTextField taxAmountField;
    private JTextField discountField;
    private JTextField totalField;
    private JComboBox<String> paymentMethodComboBox;
    private JComboBox<String> paymentStatusComboBox;
    private JTextArea notesArea;

    // UI Components - Table
    private JTable invoiceTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;

    // UI Components - Buttons
    private JButton newButton;
    private JButton saveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton generatePdfButton;
    private JButton sendEmailButton;
    private JButton markAsPaidButton;
    private JButton markAsSentButton;
    private JButton markAsCancelledButton;

    // UI Components - Search
    private JTextField searchField;
    private JComboBox<String> searchTypeComboBox;
    private JButton searchButton;
    private JButton clearSearchButton;

    // UI Components - Status
    private JLabel statusLabel;

    // Data
    private List<Invoice> invoices;
    private List<Service> services;
    private List<Customer> customers;
    private Invoice currentInvoice;

    /**
     * Constructor that initializes the view.
     */
    public InvoiceView() {
        // Initialize utilities
        pdfGenerator = new PDFGenerator();
        emailService = new EmailService();
        dateUtil = new DateUtil();
        currencyFormat = new DecimalFormat("$#,##0.00");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Initialize UI
        initializeUI();

        // Initialize controller
        controller = new InvoiceController(this);

        // Load data
        loadData();
    }

    /**
     * Initializes the UI components.
     */
    private void initializeUI() {
        // Set up the frame
        setTitle("Invoice Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create main panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create form panel
        createFormPanel();

        // Create table panel
        createTablePanel();

        // Create button panel
        createButtonPanel();

        // Create search panel
        createSearchPanel();

        // Create status panel
        createStatusPanel();

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(statusPanel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        setContentPane(mainPanel);

        // Set up event handlers
        setupEventHandlers();
    }

    /**
     * Creates the form panel.
     */
    private void createFormPanel() {
        formPanel = new JPanel(new BorderLayout(5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Invoice Details",
                TitledBorder.LEFT, TitledBorder.TOP));

        // Create grid panel for form fields
        JPanel gridPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        gridPanel.add(new JLabel("Invoice ID:"), gbc);

        gbc.gridx = 1;
        invoiceIdField = new JTextField(10);
        invoiceIdField.setEditable(false);
        gridPanel.add(invoiceIdField, gbc);

        gbc.gridx = 2;
        gridPanel.add(new JLabel("Invoice Number:"), gbc);

        gbc.gridx = 3;
        invoiceNumberField = new JTextField(15);
        invoiceNumberField.setEditable(false);
        gridPanel.add(invoiceNumberField, gbc);

        // Row 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        gridPanel.add(new JLabel("Service:"), gbc);

        gbc.gridx = 1;
        serviceComboBox = new JComboBox<>();
        gridPanel.add(serviceComboBox, gbc);

        gbc.gridx = 2;
        gridPanel.add(new JLabel("Customer:"), gbc);

        gbc.gridx = 3;
        customerComboBox = new JComboBox<>();
        gridPanel.add(customerComboBox, gbc);

        // Row 3
        gbc.gridx = 0;
        gbc.gridy = 2;
        gridPanel.add(new JLabel("Issue Date:"), gbc);

        gbc.gridx = 1;
        issueDateField = new JTextField(10);
        gridPanel.add(issueDateField, gbc);

        gbc.gridx = 2;
        gridPanel.add(new JLabel("Due Date:"), gbc);

        gbc.gridx = 3;
        dueDateField = new JTextField(10);
        gridPanel.add(dueDateField, gbc);

        // Row 4
        gbc.gridx = 0;
        gbc.gridy = 3;
        gridPanel.add(new JLabel("Subtotal:"), gbc);

        gbc.gridx = 1;
        subtotalField = new JTextField(10);
        gridPanel.add(subtotalField, gbc);

        gbc.gridx = 2;
        gridPanel.add(new JLabel("Tax Rate:"), gbc);

        gbc.gridx = 3;
        taxRateField = new JTextField(10);
        gridPanel.add(taxRateField, gbc);

        // Row 5
        gbc.gridx = 0;
        gbc.gridy = 4;
        gridPanel.add(new JLabel("Tax Amount:"), gbc);

        gbc.gridx = 1;
        taxAmountField = new JTextField(10);
        taxAmountField.setEditable(false);
        gridPanel.add(taxAmountField, gbc);

        gbc.gridx = 2;
        gridPanel.add(new JLabel("Discount:"), gbc);

        gbc.gridx = 3;
        discountField = new JTextField(10);
        gridPanel.add(discountField, gbc);

        // Row 6
        gbc.gridx = 0;
        gbc.gridy = 5;
        gridPanel.add(new JLabel("Total:"), gbc);

        gbc.gridx = 1;
        totalField = new JTextField(10);
        totalField.setEditable(false);
        gridPanel.add(totalField, gbc);

        gbc.gridx = 2;
        gridPanel.add(new JLabel("Payment Method:"), gbc);

        gbc.gridx = 3;
        paymentMethodComboBox = new JComboBox<>(new String[]{"", "CASH", "CREDIT_CARD", "DEBIT_CARD", "BANK_TRANSFER", "CHECK", "OTHER"});
        gridPanel.add(paymentMethodComboBox, gbc);

        // Row 7
        gbc.gridx = 0;
        gbc.gridy = 6;
        gridPanel.add(new JLabel("Payment Status:"), gbc);

        gbc.gridx = 1;
        paymentStatusComboBox = new JComboBox<>(new String[]{"PENDING", "PAID", "CANCELLED"});
        gridPanel.add(paymentStatusComboBox, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 7;
        gridPanel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        notesArea = new JTextArea(3, 40);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        gridPanel.add(notesScrollPane, gbc);

        // Add grid panel to form panel
        formPanel.add(gridPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the table panel.
     */
    private void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Invoices",
                TitledBorder.LEFT, TitledBorder.TOP));

        // Create table model
        String[] columnNames = {
                "ID", "Invoice Number", "Service ID", "Customer ID", "Issue Date",
                "Due Date", "Total", "Payment Status", "Sent", "Paid", "Cancelled"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 8 || columnIndex == 9 || columnIndex == 10) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        // Create table
        invoiceTable = new JTable(tableModel);
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoiceTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Create table sorter
        tableSorter = new TableRowSorter<>(tableModel);
        invoiceTable.setRowSorter(tableSorter);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add search panel to table panel
        tablePanel.add(searchPanel, BorderLayout.NORTH);
    }

    /**
     * Creates the button panel.
     */
    private void createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Create buttons
        newButton = new JButton("New");
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        generatePdfButton = new JButton("Generate PDF");
        sendEmailButton = new JButton("Send Email");
        markAsPaidButton = new JButton("Mark as Paid");
        markAsSentButton = new JButton("Mark as Sent");
        markAsCancelledButton = new JButton("Mark as Cancelled");

        // Add buttons to panel
        buttonPanel.add(newButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(generatePdfButton);
        buttonPanel.add(sendEmailButton);
        buttonPanel.add(markAsPaidButton);
        buttonPanel.add(markAsSentButton);
        buttonPanel.add(markAsCancelledButton);

        // Set initial button states
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        generatePdfButton.setEnabled(false);
        sendEmailButton.setEnabled(false);
        markAsPaidButton.setEnabled(false);
        markAsSentButton.setEnabled(false);
        markAsCancelledButton.setEnabled(false);
    }

    /**
     * Creates the search panel.
     */
    private void createSearchPanel() {
        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        // Create search components
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchPanel.add(new JLabel("By:"));
        searchTypeComboBox = new JComboBox<>(new String[]{
                "Invoice Number", "Customer ID", "Service ID", "Payment Status"
        });
        searchPanel.add(searchTypeComboBox);

        searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        clearSearchButton = new JButton("Clear");
        searchPanel.add(clearSearchButton);
    }

    /**
     * Creates the status panel.
     */
    private void createStatusPanel() {
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());

        statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel);
    }

    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
        // New button
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
                enableForm(true);
                saveButton.setEnabled(true);
                updateButton.setEnabled(false);
                deleteButton.setEnabled(false);
                generatePdfButton.setEnabled(false);
                sendEmailButton.setEnabled(false);
                markAsPaidButton.setEnabled(false);
                markAsSentButton.setEnabled(false);
                markAsCancelledButton.setEnabled(false);

                // Set default values
                issueDateField.setText(dateFormat.format(new Date()));
                taxRateField.setText("0.16"); // Default tax rate (16%)
                discountField.setText("0.00");

                // Set focus to service combo box
                serviceComboBox.requestFocus();
            }
        });

        // Save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveInvoice();
            }
        });

        // Update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateInvoice();
            }
        });

        // Delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteInvoice();
            }
        });

        // Clear button
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
                enableForm(false);
                saveButton.setEnabled(true);
                updateButton.setEnabled(false);
                deleteButton.setEnabled(false);
                generatePdfButton.setEnabled(false);
                sendEmailButton.setEnabled(false);
                markAsPaidButton.setEnabled(false);
                markAsSentButton.setEnabled(false);
                markAsCancelledButton.setEnabled(false);
            }
        });

        // Generate PDF button
        generatePdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePdf();
            }
        });

        // Send Email button
        sendEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmail();
            }
        });

        // Mark as Paid button
        markAsPaidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markAsPaid();
            }
        });

        // Mark as Sent button
        markAsSentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markAsSent();
            }
        });

        // Mark as Cancelled button
        markAsCancelledButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markAsCancelled();
            }
        });

        // Search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchInvoices();
            }
        });

        // Clear Search button
        clearSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                controller.loadInvoices();
            }
        });

        // Table row selection
        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = invoiceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedRow = invoiceTable.convertRowIndexToModel(selectedRow);
                    int invoiceId = (int) tableModel.getValueAt(selectedRow, 0);
                    controller.loadInvoiceDetails(invoiceId);
                }
            }
        });

        // Subtotal, tax rate, and discount fields
        ActionListener calculateTotalsListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotals();
            }
        };

        subtotalField.addActionListener(calculateTotalsListener);
        taxRateField.addActionListener(calculateTotalsListener);
        discountField.addActionListener(calculateTotalsListener);
    }

    /**
     * Loads data from the database.
     */
    private void loadData() {
        controller.loadInvoices();
        controller.loadServices();
        controller.loadCustomers();
    }

    /**
     * Saves a new invoice.
     */
    private void saveInvoice() {
        try {
            Invoice invoice = getInvoiceFromForm();
            if (invoice != null) {
                controller.addInvoice(invoice);
                clearForm();
                enableForm(false);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error saving invoice", ex);
            showError("Error saving invoice: " + ex.getMessage());
        }
    }

    /**
     * Updates an existing invoice.
     */
    private void updateInvoice() {
        try {
            Invoice invoice = getInvoiceFromForm();
            if (invoice != null) {
                controller.updateInvoice(invoice);
                clearForm();
                enableForm(false);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating invoice", ex);
            showError("Error updating invoice: " + ex.getMessage());
        }
    }

    /**
     * Deletes an invoice.
     */
    private void deleteInvoice() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this invoice?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                controller.deleteInvoice(invoiceId);
                clearForm();
                enableForm(false);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error deleting invoice", ex);
            showError("Error deleting invoice: " + ex.getMessage());
        }
    }

    /**
     * Generates a PDF for the current invoice.
     */
    private void generatePdf() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());
            controller.generatePdf(invoiceId);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error generating PDF", ex);
            showError("Error generating PDF: " + ex.getMessage());
        }
    }

    /**
     * Sends an email with the invoice PDF.
     */
    private void sendEmail() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());
            controller.sendEmail(invoiceId);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error sending email", ex);
            showError("Error sending email: " + ex.getMessage());
        }
    }

    /**
     * Marks the current invoice as paid.
     */
    private void markAsPaid() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());
            String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();

            if (paymentMethod == null || paymentMethod.isEmpty()) {
                showError("Please select a payment method.");
                return;
            }

            controller.markAsPaid(invoiceId, paymentMethod);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error marking invoice as paid", ex);
            showError("Error marking invoice as paid: " + ex.getMessage());
        }
    }

    /**
     * Marks the current invoice as sent.
     */
    private void markAsSent() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());
            controller.markAsSent(invoiceId);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error marking invoice as sent", ex);
            showError("Error marking invoice as sent: " + ex.getMessage());
        }
    }

    /**
     * Marks the current invoice as cancelled.
     */
    private void markAsCancelled() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText());

            String reason = JOptionPane.showInputDialog(
                    this,
                    "Enter reason for cancellation:",
                    "Cancel Invoice",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (reason != null && !reason.trim().isEmpty()) {
                controller.markAsCancelled(invoiceId, reason);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error marking invoice as cancelled", ex);
            showError("Error marking invoice as cancelled: " + ex.getMessage());
        }
    }

    /**
     * Searches for invoices based on the search criteria.
     */
    private void searchInvoices() {
        String searchText = searchField.getText().trim();
        String searchType = (String) searchTypeComboBox.getSelectedItem();

        if (searchText.isEmpty()) {
            controller.loadInvoices();
            return;
        }

        try {
            if ("Invoice Number".equals(searchType)) {
                controller.searchByInvoiceNumber(searchText);
            } else if ("Customer ID".equals(searchType)) {
                int customerId = Integer.parseInt(searchText);
                controller.searchByCustomer(customerId);
            } else if ("Service ID".equals(searchType)) {
                int serviceId = Integer.parseInt(searchText);
                controller.searchByService(serviceId);
            } else if ("Payment Status".equals(searchType)) {
                controller.searchByPaymentStatus(searchText);
            }
        } catch (NumberFormatException ex) {
            showError("Invalid number format for search.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error searching invoices", ex);
            showError("Error searching invoices: " + ex.getMessage());
        }
    }

    /**
     * Gets an Invoice object from the form fields.
     *
     * @return An Invoice object populated with form data
     * @throws ParseException If there is an error parsing dates or numbers
     */
    private Invoice getInvoiceFromForm() throws ParseException {
        Invoice invoice = new Invoice();

        // Set ID if updating
        if (!invoiceIdField.getText().isEmpty()) {
            invoice.setInvoiceId(Integer.parseInt(invoiceIdField.getText()));
        }

        // Set invoice number if available
        if (!invoiceNumberField.getText().isEmpty()) {
            invoice.setInvoiceNumber(invoiceNumberField.getText());
        }

        // Get service ID from combo box
        String serviceItem = (String) serviceComboBox.getSelectedItem();
        if (serviceItem != null && !serviceItem.isEmpty()) {
            int serviceId = Integer.parseInt(serviceItem.split(" - ")[0]);
            invoice.setServiceId(serviceId);
        } else {
            showError("Please select a service.");
            return null;
        }

        // Get customer ID from combo box
        String customerItem = (String) customerComboBox.getSelectedItem();
        if (customerItem != null && !customerItem.isEmpty()) {
            int customerId = Integer.parseInt(customerItem.split(" - ")[0]);
            invoice.setCustomerId(customerId);
        } else {
            showError("Please select a customer.");
            return null;
        }

        // Set dates
        if (!issueDateField.getText().isEmpty()) {
            invoice.setIssueDate(dateFormat.parse(issueDateField.getText()));
        } else {
            invoice.setIssueDate(new Date());
        }

        if (!dueDateField.getText().isEmpty()) {
            invoice.setDueDate(dateFormat.parse(dueDateField.getText()));
        }

        // Set amounts
        if (!subtotalField.getText().isEmpty()) {
            invoice.setSubtotal(Double.parseDouble(subtotalField.getText().replace("$", "").replace(",", "")));
        } else {
            showError("Please enter a subtotal.");
            return null;
        }

        if (!taxRateField.getText().isEmpty()) {
            invoice.setTaxRate(Double.parseDouble(taxRateField.getText()));
        } else {
            invoice.setTaxRate(0.16); // Default tax rate (16%)
        }

        if (!discountField.getText().isEmpty()) {
            invoice.setDiscount(Double.parseDouble(discountField.getText().replace("$", "").replace(",", "")));
        } else {
            invoice.setDiscount(0.0);
        }

        // Calculate tax amount and total
        invoice.calculateTotals();

        // Set payment method and status
        invoice.setPaymentMethod((String) paymentMethodComboBox.getSelectedItem());
        invoice.setPaymentStatus((String) paymentStatusComboBox.getSelectedItem());

        // Set notes
        invoice.setNotes(notesArea.getText());

        return invoice;
    }

    /**
     * Calculates the tax amount and total based on the subtotal, tax rate, and discount.
     */
    private void calculateTotals() {
        try {
            double subtotal = 0.0;
            double taxRate = 0.16; // Default tax rate (16%)
            double discount = 0.0;

            if (!subtotalField.getText().isEmpty()) {
                subtotal = Double.parseDouble(subtotalField.getText().replace("$", "").replace(",", ""));
            }

            if (!taxRateField.getText().isEmpty()) {
                taxRate = Double.parseDouble(taxRateField.getText());
            }

            if (!discountField.getText().isEmpty()) {
                discount = Double.parseDouble(discountField.getText().replace("$", "").replace(",", ""));
            }

            double taxAmount = subtotal * taxRate;
            double total = subtotal + taxAmount - discount;

            taxAmountField.setText(currencyFormat.format(taxAmount));
            totalField.setText(currencyFormat.format(total));
        } catch (NumberFormatException ex) {
            // Ignore parsing errors during typing
        }
    }

    /**
     * Clears the form fields.
     */
    public void clearForm() {
        invoiceIdField.setText("");
        invoiceNumberField.setText("");
        serviceComboBox.setSelectedIndex(-1);
        customerComboBox.setSelectedIndex(-1);
        issueDateField.setText("");
        dueDateField.setText("");
        subtotalField.setText("");
        taxRateField.setText("");
        taxAmountField.setText("");
        discountField.setText("");
        totalField.setText("");
        paymentMethodComboBox.setSelectedIndex(-1);
        paymentStatusComboBox.setSelectedIndex(0); // Default to PENDING
        notesArea.setText("");

        currentInvoice = null;
    }

    /**
     * Enables or disables the form fields.
     *
     * @param enable true to enable the fields, false to disable them
     */
    public void enableForm(boolean enable) {
        serviceComboBox.setEnabled(enable);
        customerComboBox.setEnabled(enable);
        issueDateField.setEnabled(enable);
        dueDateField.setEnabled(enable);
        subtotalField.setEnabled(enable);
        taxRateField.setEnabled(enable);
        discountField.setEnabled(enable);
        paymentMethodComboBox.setEnabled(enable);
        paymentStatusComboBox.setEnabled(enable);
        notesArea.setEnabled(enable);

        saveButton.setEnabled(enable);
    }

    /**
     * Updates the invoice table with the provided list of invoices.
     *
     * @param invoices The list of invoices to display
     */
    public void updateInvoiceTable(List<Invoice> invoices) {
        this.invoices = invoices;

        // Clear the table
        tableModel.setRowCount(0);

        // Add invoices to the table
        for (Invoice invoice : invoices) {
            Object[] row = {
                    invoice.getInvoiceId(),
                    invoice.getInvoiceNumber(),
                    invoice.getServiceId(),
                    invoice.getCustomerId(),
                    dateFormat.format(invoice.getIssueDate()),
                    invoice.getDueDate() != null ? dateFormat.format(invoice.getDueDate()) : "",
                    currencyFormat.format(invoice.getTotal()),
                    invoice.getPaymentStatus(),
                    invoice.isSent(),
                    invoice.isPaid(),
                    invoice.isCancelled()
            };
            tableModel.addRow(row);
        }

        // Update status label
        statusLabel.setText("Loaded " + invoices.size() + " invoices");
    }

    /**
     * Updates the service combo box with the provided list of services.
     *
     * @param services The list of services to display
     */
    public void updateServiceComboBox(List<Service> services) {
        this.services = services;

        // Clear the combo box
        serviceComboBox.removeAllItems();

        // Add services to the combo box
        for (Service service : services) {
            serviceComboBox.addItem(service.getServiceId() + " - " + service.getDescription());
        }
    }

    /**
     * Updates the customer combo box with the provided list of customers.
     *
     * @param customers The list of customers to display
     */
    public void updateCustomerComboBox(List<Customer> customers) {
        this.customers = customers;

        // Clear the combo box
        customerComboBox.removeAllItems();

        // Add customers to the combo box
        for (Customer customer : customers) {
            customerComboBox.addItem(customer.getCustomerId() + " - " + customer.getFullName());
        }
    }

    /**
     * Populates the form with the details of the provided invoice.
     *
     * @param invoice The invoice to display
     */
    public void populateInvoiceForm(Invoice invoice) {
        this.currentInvoice = invoice;

        // Set form fields
        invoiceIdField.setText(String.valueOf(invoice.getInvoiceId()));
        invoiceNumberField.setText(invoice.getInvoiceNumber());

        // Set service
        for (int i = 0; i < serviceComboBox.getItemCount(); i++) {
            String item = serviceComboBox.getItemAt(i);
            if (item.startsWith(invoice.getServiceId() + " - ")) {
                serviceComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Set customer
        for (int i = 0; i < customerComboBox.getItemCount(); i++) {
            String item = customerComboBox.getItemAt(i);
            if (item.startsWith(invoice.getCustomerId() + " - ")) {
                customerComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Set dates
        issueDateField.setText(dateFormat.format(invoice.getIssueDate()));
        if (invoice.getDueDate() != null) {
            dueDateField.setText(dateFormat.format(invoice.getDueDate()));
        } else {
            dueDateField.setText("");
        }

        // Set amounts
        subtotalField.setText(String.format("%.2f", invoice.getSubtotal()));
        taxRateField.setText(String.format("%.2f", invoice.getTaxRate()));
        taxAmountField.setText(currencyFormat.format(invoice.getTaxAmount()));
        discountField.setText(String.format("%.2f", invoice.getDiscount()));
        totalField.setText(currencyFormat.format(invoice.getTotal()));

        // Set payment method and status
        paymentMethodComboBox.setSelectedItem(invoice.getPaymentMethod());
        paymentStatusComboBox.setSelectedItem(invoice.getPaymentStatus());

        // Set notes
        notesArea.setText(invoice.getNotes());

        // Enable/disable buttons based on invoice state
        updateButton.setEnabled(true);
        deleteButton.setEnabled(!invoice.isPaid() && !invoice.isCancelled());
        generatePdfButton.setEnabled(true);
        sendEmailButton.setEnabled(true);
        markAsPaidButton.setEnabled(!invoice.isPaid() && !invoice.isCancelled());
        markAsSentButton.setEnabled(!invoice.isSent() && !invoice.isCancelled());
        markAsCancelledButton.setEnabled(!invoice.isCancelled());

        // Enable form
        enableForm(true);
    }

    /**
     * Shows an error message.
     *
     * @param message The error message to display
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Error: " + message);
    }

    /**
     * Shows an information message.
     *
     * @param message The information message to display
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText(message);
    }

    /**
     * Shows a success message.
     *
     * @param message The success message to display
     */
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText(message);
    }

    /**
     * Gets the service name for a given service ID.
     *
     * @param serviceId The ID of the service
     * @return The name of the service, or "Unknown" if not found
     */
    public String getServiceName(int serviceId) {
        if (services != null) {
            for (Service service : services) {
                if (service.getServiceId() == serviceId) {
                    return service.getDescription();
                }
            }
        }
        return "Unknown";
    }

    /**
     * Gets the customer name for a given customer ID.
     *
     * @param customerId The ID of the customer
     * @return The name of the customer, or "Unknown" if not found
     */
    public String getCustomerName(int customerId) {
        if (customers != null) {
            for (Customer customer : customers) {
                if (customer.getCustomerId() == customerId) {
                    return customer.getFullName();
                }
            }
        }
        return "Unknown";
    }

    /**
     * Gets the customer email for a given customer ID.
     *
     * @param customerId The ID of the customer
     * @return The email of the customer, or null if not found
     */
    public String getCustomerEmail(int customerId) {
        if (customers != null) {
            for (Customer customer : customers) {
                if (customer.getCustomerId() == customerId) {
                    return customer.getEmail();
                }
            }
        }
        return null;
    }
}