/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.invoices.views;

/**
 *
 * @author ADMiN
 */

import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.invoices.controller.InvoiceController;
import com.carmotorsproject.invoices.model.Invoice;
import com.carmotorsproject.services.model.Service;
import com.carmotorsproject.utils.PDFGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * View for previewing invoices before saving or sending.
 * Provides a UI to display invoice details and a PDF preview.
 */
public class InvoicePreviewView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(InvoicePreviewView.class.getName());

    // Controller
    private final InvoiceController controller;

    // Data
    private Invoice invoice;
    private Customer customer;
    private Service service;

    // UI Components
    private JPanel contentPane;
    private JPanel detailsPanel;
    private JPanel previewPanel;
    private JLabel pdfPreviewLabel;
    private JButton saveButton;
    private JButton sendButton;
    private JButton closeButton;

    // Utilities
    private final PDFGenerator pdfGenerator;
    private File tempPdfFile;

    /**
     * Constructor that initializes the preview view.
     *
     * @param controller The invoice controller
     * @param invoice The invoice to preview
     * @param customer The customer associated with the invoice
     * @param service The service associated with the invoice
     */
    public InvoicePreviewView(InvoiceController controller, Invoice invoice, Customer customer, Service service) {
        this.controller = controller;
        this.invoice = invoice;
        this.customer = customer;
        this.service = service;
        this.pdfGenerator = new PDFGenerator();

        initComponents();
        setupLayout();
        setupListeners();
        generatePreview();

        setTitle("Invoice Preview - " + invoice.getInvoiceNumber());
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        LOGGER.log(Level.INFO, "Invoice preview initialized for invoice: {0}", invoice.getInvoiceNumber());
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        detailsPanel = new JPanel();
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Invoice Details"));

        previewPanel = new JPanel();
        previewPanel.setBorder(BorderFactory.createTitledBorder("PDF Preview"));

        pdfPreviewLabel = new JLabel("Generating preview...");
        pdfPreviewLabel.setHorizontalAlignment(JLabel.CENTER);

        saveButton = new JButton("Save PDF");
        sendButton = new JButton("Send by Email");
        closeButton = new JButton("Close");
    }

    /**
     * Sets up the layout of the UI components.
     */
    private void setupLayout() {
        contentPane.setLayout(new BorderLayout(10, 10));

        // Details Panel
        detailsPanel.setLayout(new GridLayout(0, 2, 5, 5));
        detailsPanel.setPreferredSize(new Dimension(400, 200));

        // Add invoice details
        addDetailField("Invoice Number:", invoice.getInvoiceNumber());
        addDetailField("Date:", new SimpleDateFormat("yyyy-MM-dd").format(invoice.getInvoiceDate()));
        addDetailField("Customer:", customer.getFirstName() + " " + customer.getLastName());
        addDetailField("Email:", customer.getEmail());
        addDetailField("Phone:", customer.getPhone());
        addDetailField("Service:", service.getDescription());
        addDetailField("Subtotal:", new DecimalFormat("$#,##0.00").format(invoice.getSubtotal()));
        addDetailField("Tax:", new DecimalFormat("$#,##0.00").format(invoice.getTax()));
        addDetailField("Total:", new DecimalFormat("$#,##0.00").format(invoice.getTotal()));
        addDetailField("Status:", invoice.getPaymentStatus());

        // Preview Panel
        previewPanel.setLayout(new BorderLayout());
        previewPanel.add(new JScrollPane(pdfPreviewLabel), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(sendButton);
        buttonPanel.add(closeButton);

        // Add panels to content pane
        contentPane.add(detailsPanel, BorderLayout.NORTH);
        contentPane.add(previewPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Adds a field to the details panel.
     *
     * @param label The label for the field
     * @param value The value of the field
     */
    private void addDetailField(String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(labelComponent.getFont().deriveFont(Font.BOLD));
        detailsPanel.add(labelComponent);

        JLabel valueComponent = new JLabel(value);
        detailsPanel.add(valueComponent);
    }

    /**
     * Sets up the event listeners for the UI components.
     */
    private void setupListeners() {
        saveButton.addActionListener((ActionEvent e) -> {
            savePdf();
        });

        sendButton.addActionListener((ActionEvent e) -> {
            sendEmail();
        });

        closeButton.addActionListener((ActionEvent e) -> {
            dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanupTempFiles();
            }
        });
    }

    /**
     * Generates a preview of the invoice PDF.
     */
    private void generatePreview() {
        try {
            // Create a temporary file for the PDF
            tempPdfFile = File.createTempFile("invoice_preview_", ".pdf");
            tempPdfFile.deleteOnExit();

            // Generate the PDF
            pdfGenerator.generateInvoicePdf(invoice, customer, service, tempPdfFile.getAbsolutePath());

            // Create a preview image
            ImageIcon previewIcon = createPdfPreviewImage(tempPdfFile);
            if (previewIcon != null) {
                pdfPreviewLabel.setIcon(previewIcon);
                pdfPreviewLabel.setText("");
            } else {
                pdfPreviewLabel.setText("Preview not available. PDF generated successfully.");
            }

            LOGGER.log(Level.INFO, "Generated PDF preview for invoice: {0}", invoice.getInvoiceNumber());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error generating PDF preview", ex);
            pdfPreviewLabel.setText("Error generating preview: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error generating PDF preview: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates a preview image from the PDF file.
     *
     * @param pdfFile The PDF file
     * @return An ImageIcon containing the preview image
     */
    private ImageIcon createPdfPreviewImage(File pdfFile) {
        try {
            // This is a simplified implementation
            // In a real application, you would use a library like PDFBox or PDFRenderer
            // to render the PDF to an image

            // For now, we'll just use a placeholder image
            return new ImageIcon(getClass().getResource("/images/pdf_preview.png"));

            // Alternatively, you could use Desktop to open the PDF in the default viewer
            // Desktop.getDesktop().open(pdfFile);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error creating PDF preview image", ex);
            return null;
        }
    }

    /**
     * Saves the PDF to a user-specified location.
     */
    private void savePdf() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Invoice PDF");
        fileChooser.setSelectedFile(new File("invoice_" + invoice.getInvoiceNumber().replace("-", "_") + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try {
                // Generate the PDF at the selected location
                pdfGenerator.generateInvoicePdf(invoice, customer, service, fileToSave.getAbsolutePath());

                JOptionPane.showMessageDialog(this,
                        "PDF saved successfully: " + fileToSave.getAbsolutePath(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                LOGGER.log(Level.INFO, "Saved PDF for invoice: {0} to: {1}",
                        new Object[]{invoice.getInvoiceNumber(), fileToSave.getAbsolutePath()});
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error saving PDF", ex);
                JOptionPane.showMessageDialog(this,
                        "Error saving PDF: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Sends the invoice by email.
     */
    private void sendEmail() {
        int option = JOptionPane.showConfirmDialog(this,
                "Send invoice to " + customer.getEmail() + "?",
                "Confirm Email", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                controller.sendEmail(invoice.getInvoiceId());

                JOptionPane.showMessageDialog(this,
                        "Email sent successfully to: " + customer.getEmail(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                LOGGER.log(Level.INFO, "Sent email for invoice: {0} to: {1}",
                        new Object[]{invoice.getInvoiceNumber(), customer.getEmail()});

                // Close the preview window
                dispose();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error sending email", ex);
                JOptionPane.showMessageDialog(this,
                        "Error sending email: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Cleans up temporary files when the window is closed.
     */
    private void cleanupTempFiles() {
        if (tempPdfFile != null && tempPdfFile.exists()) {
            try {
                tempPdfFile.delete();
                LOGGER.log(Level.INFO, "Deleted temporary PDF file: {0}", tempPdfFile.getAbsolutePath());
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Error deleting temporary PDF file", ex);
            }
        }
    }
}