/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.invoices.controller;

import com.carmotorsproject.customers.model.CustomerDAO;
import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.invoices.model.InvoiceDAO;
import com.carmotorsproject.invoices.model.Invoice;
import com.carmotorsproject.invoices.views.InvoiceView;
import com.carmotorsproject.services.model.ServiceDAO;
import com.carmotorsproject.services.model.Service;
import com.carmotorsproject.utils.PDFGenerator;
import com.carmotorsproject.utils.EmailService;
import com.carmotorsproject.invoices.views.InvoicePreviewView;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for invoice-related operations.
 * Mediates between the InvoiceView and the InvoiceDAO.
 */
public class InvoiceController {

    private static final Logger LOGGER = Logger.getLogger(InvoiceController.class.getName());

    // View
    private final InvoiceView view;

    // DAOs
    private final InvoiceDAO invoiceDAO;
    private final ServiceDAO serviceDAO;
    private final CustomerDAO customerDAO;

    // Utilities
    private final PDFGenerator pdfGenerator;
    private final EmailService emailService;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The invoice view
     */
    public InvoiceController(InvoiceView view) {
        this.view = view;
        this.invoiceDAO = com.carmotorsproject.invoices.model.DAOFactory.getInvoiceDAO();
        this.serviceDAO = com.carmotorsproject.services.model.DAOFactory.getServiceDAO();
        this.customerDAO = com.carmotorsproject.customers.model.DAOFactory.getCustomerDAO();
        this.pdfGenerator = new PDFGenerator();
        this.emailService = new EmailService();
        LOGGER.log(Level.INFO, "InvoiceController initialized");
    }

    /**
     * Loads all invoices and updates the view.
     */
    public void loadInvoices() {
        try {
            List<Invoice> invoices = invoiceDAO.findAll();
            view.updateInvoiceTable(invoices);
            LOGGER.log(Level.INFO, "Loaded {0} invoices", invoices.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading invoices", ex);
            view.showError("Error loading invoices: " + ex.getMessage());
        }
    }

    /**
     * Loads all services for the service combo box.
     */
    public void loadServices() {
        try {
            List<Service> services = serviceDAO.findAll();
            view.updateServiceComboBox(services);
            LOGGER.log(Level.INFO, "Loaded {0} services", services.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading services", ex);
            view.showError("Error loading services: " + ex.getMessage());
        }
    }

    /**
     * Loads all customers for the customer combo box.
     */
    public void loadCustomers() {
        try {
            List<Customer> customers = customerDAO.findAll();
            view.updateCustomerComboBox(customers);
            LOGGER.log(Level.INFO, "Loaded {0} customers", customers.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading customers", ex);
            view.showError("Error loading customers: " + ex.getMessage());
        }
    }

    /**
     * Loads the details of a specific invoice and updates the view.
     *
     * @param invoiceId The ID of the invoice to load
     */
    public void loadInvoiceDetails(int invoiceId) {
        try {
            Invoice invoice = invoiceDAO.findById(invoiceId);
            if (invoice != null) {
                view.populateInvoiceForm(invoice);
                LOGGER.log(Level.INFO, "Loaded details for invoice ID: {0}", invoiceId);
            } else {
                LOGGER.log(Level.WARNING, "Invoice not found with ID: {0}", invoiceId);
                view.showError("Invoice not found with ID: " + invoiceId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading invoice details", ex);
            view.showError("Error loading invoice details: " + ex.getMessage());
        }
    }

    /**
     * Opens the invoice preview view.
     *
     * @param invoiceId The ID of the invoice to preview
     */
    public void previewInvoice(int invoiceId) {
        try {
            // Get the invoice
            Invoice invoice = invoiceDAO.findById(invoiceId);
            if (invoice == null) {
                view.showError("Invoice not found with ID: " + invoiceId);
                return;
            }

            // Get the customer
            Customer customer = customerDAO.findById(invoice.getCustomerId());
            if (customer == null) {
                view.showError("Customer not found with ID: " + invoice.getCustomerId());
                return;
            }

            // Get the service
            Service service = serviceDAO.findById(invoice.getServiceId());
            if (service == null) {
                view.showError("Service not found with ID: " + invoice.getServiceId());
                return;
            }

            // Open the preview view
            InvoicePreviewView previewView = new InvoicePreviewView(this, invoice, customer, service);
            previewView.setVisible(true);

            LOGGER.log(Level.INFO, "Opened preview for invoice ID: {0}", invoiceId);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading invoice for preview", ex);
            view.showError("Error loading invoice for preview: " + ex.getMessage());
        }
    }

    /**
     * Adds a new invoice.
     *
     * @param invoice The invoice to add
     */
    public void addInvoice(Invoice invoice) {
        try {
            // Save the invoice
            Invoice savedInvoice = invoiceDAO.save(invoice);

            // Reload the invoice list
            loadInvoices();

            // Show success message
            view.showSuccess("Invoice created successfully with ID: " + savedInvoice.getInvoiceId());

            LOGGER.log(Level.INFO, "Added invoice with ID: {0}", savedInvoice.getInvoiceId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding invoice", ex);
            view.showError("Error adding invoice: " + ex.getMessage());
        }
    }

    /**
     * Updates an existing invoice.
     *
     * @param invoice The invoice to update
     */
    public void updateInvoice(Invoice invoice) {
        try {
            // Update the invoice
            Invoice updatedInvoice = invoiceDAO.update(invoice);

            // Reload the invoice list
            loadInvoices();

            // Show success message
            view.showSuccess("Invoice updated successfully with ID: " + updatedInvoice.getInvoiceId());

            LOGGER.log(Level.INFO, "Updated invoice with ID: {0}", updatedInvoice.getInvoiceId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating invoice", ex);
            view.showError("Error updating invoice: " + ex.getMessage());
        }
    }

    /**
     * Deletes an invoice.
     *
     * @param invoiceId The ID of the invoice to delete
     */
    public void deleteInvoice(int invoiceId) {
        try {
            boolean deleted = invoiceDAO.delete(invoiceId);
            if (deleted) {
                // Reload the invoice list
                loadInvoices();

                // Show success message
                view.showSuccess("Invoice deleted successfully with ID: " + invoiceId);

                LOGGER.log(Level.INFO, "Deleted invoice with ID: {0}", invoiceId);
            } else {
                LOGGER.log(Level.WARNING, "Invoice not found with ID: {0}", invoiceId);
                view.showError("Invoice not found with ID: " + invoiceId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting invoice", ex);
            view.showError("Error deleting invoice: " + ex.getMessage());
        }
    }

    /**
     * Generates a PDF for an invoice.
     *
     * @param invoiceId The ID of the invoice
     */
    public void generatePdf(int invoiceId) {
        try {
            // Get the invoice
            Invoice invoice = invoiceDAO.findById(invoiceId);
            if (invoice == null) {
                view.showError("Invoice not found with ID: " + invoiceId);
                return;
            }

            // Get the customer
            Customer customer = customerDAO.findById(invoice.getCustomerId());
            if (customer == null) {
                view.showError("Customer not found with ID: " + invoice.getCustomerId());
                return;
            }

            // Get the service
            Service service = serviceDAO.findById(invoice.getServiceId());
            if (service == null) {
                view.showError("Service not found with ID: " + invoice.getServiceId());
                return;
            }

            // Generate the PDF
            String pdfPath = "invoices/invoice_" + invoice.getInvoiceNumber().replace("-", "_") + ".pdf";
            File pdfFile = pdfGenerator.generateInvoicePdf(invoice, customer, service, pdfPath);

            // Show success message
            view.showSuccess("PDF generated successfully: " + pdfFile.getAbsolutePath());

            LOGGER.log(Level.INFO, "Generated PDF for invoice ID: {0}, Path: {1}",
                    new Object[]{invoiceId, pdfFile.getAbsolutePath()});
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error generating PDF", ex);
            view.showError("Error generating PDF: " + ex.getMessage());
        }
    }

    /**
     * Sends an email with the invoice PDF.
     *
     * @param invoiceId The ID of the invoice
     */
    public void sendEmail(int invoiceId) {
        try {
            // Get the invoice
            Invoice invoice = invoiceDAO.findById(invoiceId);
            if (invoice == null) {
                view.showError("Invoice not found with ID: " + invoiceId);
                return;
            }

            // Get the customer
            Customer customer = customerDAO.findById(invoice.getCustomerId());
            if (customer == null) {
                view.showError("Customer not found with ID: " + invoice.getCustomerId());
                return;
            }

            // Check if customer has an email
            if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
                view.showError("Customer does not have an email address.");
                return;
            }

            // Get the service
            Service service = serviceDAO.findById(invoice.getServiceId());
            if (service == null) {
                view.showError("Service not found with ID: " + invoice.getServiceId());
                return;
            }

            // Generate the PDF if not already sent
            String pdfPath = "invoices/invoice_" + invoice.getInvoiceNumber().replace("-", "_") + ".pdf";
            File pdfFile = new File(pdfPath);

            if (!pdfFile.exists()) {
                pdfFile = pdfGenerator.generateInvoicePdf(invoice, customer, service, pdfPath);
            }

            // Send the email
            String subject = "Invoice " + invoice.getInvoiceNumber() + " from Car Motors";
            String body = "Dear " + customer.getFirstName() + ",\n\n" +
                    "Please find attached your invoice " + invoice.getInvoiceNumber() + " for the service provided on " +
                    new java.text.SimpleDateFormat("yyyy-MM-dd").format(service.getServiceDate()) + ".\n\n" +
                    "Total amount: " + new java.text.DecimalFormat("$#,##0.00").format(invoice.getTotal()) + "\n\n" +
                    "Thank you for your business.\n\n" +
                    "Regards,\n" +
                    "Car Motors Team";

            emailService.sendEmailWithAttachment(customer.getEmail(), subject, body, pdfFile);

            // Mark the invoice as sent
            invoiceDAO.markAsSent(invoiceId);

            // Reload the invoice details
            loadInvoiceDetails(invoiceId);

            // Show success message
            view.showSuccess("Email sent successfully to: " + customer.getEmail());

            LOGGER.log(Level.INFO, "Sent email for invoice ID: {0} to: {1}",
                    new Object[]{invoiceId, customer.getEmail()});
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error sending email", ex);
            view.showError("Error sending email: " + ex.getMessage());
        }
    }

    /**
     * Marks an invoice as paid.
     *
     * @param invoiceId The ID of the invoice
     * @param paymentMethod The payment method used
     */
    public void markAsPaid(int invoiceId, String paymentMethod) {
        try {
            boolean updated = invoiceDAO.markAsPaid(invoiceId, paymentMethod);
            if (updated) {
                // Reload the invoice details
                loadInvoiceDetails(invoiceId);

                // Show success message
                view.showSuccess("Invoice marked as paid successfully.");

                LOGGER.log(Level.INFO, "Marked invoice ID: {0} as paid with method: {1}",
                        new Object[]{invoiceId, paymentMethod});
            } else {
                LOGGER.log(Level.WARNING, "Invoice not found with ID: {0}", invoiceId);
                view.showError("Invoice not found with ID: " + invoiceId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error marking invoice as paid", ex);
            view.showError("Error marking invoice as paid: " + ex.getMessage());
        }
    }

    /**
     * Marks an invoice as sent.
     *
     * @param invoiceId The ID of the invoice
     */
    public void markAsSent(int invoiceId) {
        try {
            boolean updated = invoiceDAO.markAsSent(invoiceId);
            if (updated) {
                // Reload the invoice details
                loadInvoiceDetails(invoiceId);

                // Show success message
                view.showSuccess("Invoice marked as sent successfully.");

                LOGGER.log(Level.INFO, "Marked invoice ID: {0} as sent", invoiceId);
            } else {
                LOGGER.log(Level.WARNING, "Invoice not found with ID: {0}", invoiceId);
                view.showError("Invoice not found with ID: " + invoiceId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error marking invoice as sent", ex);
            view.showError("Error marking invoice as sent: " + ex.getMessage());
        }
    }

    /**
     * Marks an invoice as cancelled.
     *
     * @param invoiceId The ID of the invoice
     * @param reason The reason for cancellation
     */
    public void markAsCancelled(int invoiceId, String reason) {
        try {
            boolean updated = invoiceDAO.markAsCancelled(invoiceId, reason);
            if (updated) {
                // Reload the invoice details
                loadInvoiceDetails(invoiceId);

                // Show success message
                view.showSuccess("Invoice marked as cancelled successfully.");

                LOGGER.log(Level.INFO, "Marked invoice ID: {0} as cancelled with reason: {1}",
                        new Object[]{invoiceId, reason});
            } else {
                LOGGER.log(Level.WARNING, "Invoice not found with ID: {0}", invoiceId);
                view.showError("Invoice not found with ID: " + invoiceId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error marking invoice as cancelled", ex);
            view.showError("Error marking invoice as cancelled: " + ex.getMessage());
        }
    }

    /**
     * Searches for invoices by invoice number.
     *
     * @param invoiceNumber The invoice number to search for
     */
    public void searchByInvoiceNumber(String invoiceNumber) {
        try {
            Invoice invoice = invoiceDAO.findByInvoiceNumber(invoiceNumber);
            if (invoice != null) {
                List<Invoice> invoices = List.of(invoice);
                view.updateInvoiceTable(invoices);
                LOGGER.log(Level.INFO, "Found invoice with number: {0}", invoiceNumber);
            } else {
                view.updateInvoiceTable(List.of());
                view.showInfo("No invoices found with number: " + invoiceNumber);
                LOGGER.log(Level.INFO, "No invoices found with number: {0}", invoiceNumber);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching invoices by number", ex);
            view.showError("Error searching invoices by number: " + ex.getMessage());
        }
    }

    /**
     * Searches for invoices by customer ID.
     *
     * @param customerId The customer ID to search for
     */
    public void searchByCustomer(int customerId) {
        try {
            List<Invoice> invoices = invoiceDAO.findByCustomer(customerId);
            view.updateInvoiceTable(invoices);
            LOGGER.log(Level.INFO, "Found {0} invoices for customer ID: {1}",
                    new Object[]{invoices.size(), customerId});

            if (invoices.isEmpty()) {
                view.showInfo("No invoices found for customer ID: " + customerId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching invoices by customer", ex);
            view.showError("Error searching invoices by customer: " + ex.getMessage());
        }
    }

    /**
     * Searches for invoices by service ID.
     *
     * @param serviceId The service ID to search for
     */
    public void searchByService(int serviceId) {
        try {
            List<Invoice> invoices = invoiceDAO.findByService(serviceId);
            view.updateInvoiceTable(invoices);
            LOGGER.log(Level.INFO, "Found {0} invoices for service ID: {1}",
                    new Object[]{invoices.size(), serviceId});

            if (invoices.isEmpty()) {
                view.showInfo("No invoices found for service ID: " + serviceId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching invoices by service", ex);
            view.showError("Error searching invoices by service: " + ex.getMessage());
        }
    }

    /**
     * Searches for invoices by payment status.
     *
     * @param paymentStatus The payment status to search for
     */
    public void searchByPaymentStatus(String paymentStatus) {
        try {
            List<Invoice> invoices = invoiceDAO.findByPaymentStatus(paymentStatus);
            view.updateInvoiceTable(invoices);
            LOGGER.log(Level.INFO, "Found {0} invoices with payment status: {1}",
                    new Object[]{invoices.size(), paymentStatus});

            if (invoices.isEmpty()) {
                view.showInfo("No invoices found with payment status: " + paymentStatus);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching invoices by payment status", ex);
            view.showError("Error searching invoices by payment status: " + ex.getMessage());
        }
    }
}