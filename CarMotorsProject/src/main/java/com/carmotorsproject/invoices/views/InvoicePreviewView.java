package com.carmotorsproject.invoices.views;

import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.invoices.controller.InvoiceController;
import com.carmotorsproject.invoices.model.Invoice;
import com.carmotorsproject.services.model.Service;
import com.carmotorsproject.utils.PDFGenerator;
import com.carmotorsproject.ui.theme.AppTheme;
import com.carmotorsproject.ui.components.ModernButton;
import com.carmotorsproject.ui.components.RoundedPanel;
import com.carmotorsproject.ui.components.TransparentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
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
    private ModernButton saveButton;
    private ModernButton sendButton;
    private ModernButton closeButton;

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

        setTitle("Vista Previa de Factura - " + invoice.getInvoiceNumber());
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        LOGGER.log(Level.INFO, "Vista previa de factura inicializada para factura: {0}", invoice.getInvoiceNumber());
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        // Apply theme
        AppTheme.applyTheme();

        contentPane = new RoundedPanel(AppTheme.PRIMARY_WHITE, 1.0f, 0);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        detailsPanel = new RoundedPanel(Color.WHITE, 1.0f, AppTheme.BORDER_RADIUS);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.MEDIUM_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        previewPanel = new RoundedPanel(Color.WHITE, 1.0f, AppTheme.BORDER_RADIUS);
        previewPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.MEDIUM_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        pdfPreviewLabel = new JLabel("Generando vista previa...");
        pdfPreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        pdfPreviewLabel.setFont(AppTheme.REGULAR_FONT);

        saveButton = new ModernButton("Guardar PDF", "primary");
        sendButton = new ModernButton("Enviar por Email", "success");
        closeButton = new ModernButton("Cerrar", "light");
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
        addDetailField("Número de Factura:", invoice.getInvoiceNumber());
        addDetailField("Fecha:", new SimpleDateFormat("yyyy-MM-dd").format(invoice.getInvoiceDate()));
        addDetailField("Cliente:", customer.getFirstName() + " " + customer.getLastName());
        addDetailField("Email:", customer.getEmail());
        addDetailField("Teléfono:", customer.getPhone());
        addDetailField("Servicio:", service.getDescription());
        addDetailField("Subtotal:", new DecimalFormat("$#,##0.00").format(invoice.getSubtotal()));
        addDetailField("Impuesto:", new DecimalFormat("$#,##0.00").format(invoice.getTax()));
        addDetailField("Total:", new DecimalFormat("$#,##0.00").format(invoice.getTotal()));
        addDetailField("Estado:", invoice.getPaymentStatus());

        // Preview Panel
        previewPanel.setLayout(new BorderLayout());
        previewPanel.add(new JScrollPane(pdfPreviewLabel), BorderLayout.CENTER);

        // Button Panel
        TransparentPanel buttonPanel = new TransparentPanel(AppTheme.LIGHT_GRAY, 0.3f);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
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
        labelComponent.setFont(AppTheme.SUBTITLE_FONT);
        labelComponent.setForeground(AppTheme.PRIMARY_BLACK);
        detailsPanel.add(labelComponent);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(AppTheme.REGULAR_FONT);
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
                pdfPreviewLabel.setText("Vista previa no disponible. PDF generado correctamente.");
            }

            LOGGER.log(Level.INFO, "Vista previa de PDF generada para factura: {0}", invoice.getInvoiceNumber());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error generando vista previa de PDF", ex);
            pdfPreviewLabel.setText("Error generando vista previa: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error generando vista previa de PDF: " + ex.getMessage(),
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
            LOGGER.log(Level.SEVERE, "Error creando imagen de vista previa de PDF", ex);
            return null;
        }
    }

    /**
     * Saves the PDF to a user-specified location.
     */
    private void savePdf() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar PDF de Factura");
        fileChooser.setSelectedFile(new File("factura_" + invoice.getInvoiceNumber().replace("-", "_") + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try {
                // Generate the PDF at the selected location
                pdfGenerator.generateInvoicePdf(invoice, customer, service, fileToSave.getAbsolutePath());

                JOptionPane.showMessageDialog(this,
                        "PDF guardado correctamente: " + fileToSave.getAbsolutePath(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                LOGGER.log(Level.INFO, "PDF guardado para factura: {0} en: {1}",
                        new Object[]{invoice.getInvoiceNumber(), fileToSave.getAbsolutePath()});
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error guardando PDF", ex);
                JOptionPane.showMessageDialog(this,
                        "Error guardando PDF: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Sends the invoice by email.
     */
    private void sendEmail() {
        int option = JOptionPane.showConfirmDialog(this,
                "¿Enviar factura a " + customer.getEmail() + "?",
                "Confirmar Email", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                controller.sendEmail(invoice.getInvoiceId());

                JOptionPane.showMessageDialog(this,
                        "Email enviado correctamente a: " + customer.getEmail(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                LOGGER.log(Level.INFO, "Email enviado para factura: {0} a: {1}",
                        new Object[]{invoice.getInvoiceNumber(), customer.getEmail()});

                // Close the preview window
                dispose();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error enviando email", ex);
                JOptionPane.showMessageDialog(this,
                        "Error enviando email: " + ex.getMessage(),
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
                LOGGER.log(Level.INFO, "Archivo PDF temporal eliminado: {0}", tempPdfFile.getAbsolutePath());
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Error eliminando archivo PDF temporal", ex);
            }
        }
    }
}
