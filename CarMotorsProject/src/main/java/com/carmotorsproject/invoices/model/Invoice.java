package com.carmotorsproject.invoices.model;

import com.carmotorsproject.services.model.Service;
import java.util.Date;

/**
 * Represents an invoice in the system.
 */
public class Invoice {
    private int invoiceId;
    private int serviceId;
    private int customerId;
    private String invoiceNumber;
    private Date issueDate;
    private Date dueDate;
    private double subtotal;
    private double taxRate;
    private double taxAmount;
    private double discount;
    private double total;
    private String paymentMethod;
    private String paymentStatus;
    private String qrCode;
    private String notes;
    private boolean sent;
    private Date sentDate;
    private boolean paid;
    private Date paidDate;
    private boolean cancelled;
    private Date cancelledDate;
    private String cancelReason;

    // New fields to support PDFGenerator
    private String cufe; // Código Único de Facturación Electrónica
    private Service service; // Associated service object
    private double laborCost; // Labor cost
    private double partsSubtotal; // Parts subtotal
    private double tax; // Tax amount (may be different from taxAmount for specific calculations)

    /**
     * Default constructor.
     */
    public Invoice() {
        this.issueDate = new Date();
    }

    /**
     * Gets the invoice ID.
     *
     * @return The invoice ID
     */
    public int getInvoiceId() {
        return invoiceId;
    }

    /**
     * Sets the invoice ID.
     *
     * @param invoiceId The invoice ID
     */
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * Gets the ID for compatibility with PDFGenerator.
     *
     * @return The invoice ID
     */
    public int getId() {
        return invoiceId;
    }

    /**
     * Sets the ID for compatibility with PDFGenerator.
     *
     * @param id The invoice ID
     */
    public void setId(int id) {
        this.invoiceId = id;
    }

    /**
     * Gets the service ID.
     *
     * @return The service ID
     */
    public int getServiceId() {
        return serviceId;
    }

    /**
     * Sets the service ID.
     *
     * @param serviceId The service ID
     */
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * Gets the customer ID.
     *
     * @return The customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Gets the invoice date (alias for issue date).
     *
     * @return The invoice date
     */
    public Date getInvoiceDate() {
        return getIssueDate();
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId The customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the invoice number.
     *
     * @return The invoice number
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * Sets the invoice number.
     *
     * @param invoiceNumber The invoice number
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * Gets the issue date.
     *
     * @return The issue date
     */
    public Date getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the issue date.
     *
     * @param issueDate The issue date
     */
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * Gets the due date.
     *
     * @return The due date
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date.
     *
     * @param dueDate The due date
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Gets the subtotal.
     *
     * @return The subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Sets the subtotal.
     *
     * @param subtotal The subtotal
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Gets the tax rate.
     *
     * @return The tax rate
     */
    public double getTaxRate() {
        return taxRate;
    }

    /**
     * Sets the tax rate.
     *
     * @param taxRate The tax rate
     */
    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * Gets the tax amount.
     *
     * @return The tax amount
     */
    public double getTaxAmount() {
        return taxAmount;
    }

    /**
     * Sets the tax amount.
     *
     * @param taxAmount The tax amount
     */
    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    /**
     * Gets the discount.
     *
     * @return The discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * Sets the discount.
     *
     * @param discount The discount
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * Gets the total.
     *
     * @return The total
     */
    public double getTotal() {
        return total;
    }

    /**
     * Sets the total.
     *
     * @param total The total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Gets the payment method.
     *
     * @return The payment method
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Sets the payment method.
     *
     * @param paymentMethod The payment method
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Gets the payment status.
     *
     * @return The payment status
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Sets the payment status.
     *
     * @param paymentStatus The payment status
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * Gets the QR code.
     *
     * @return The QR code
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * Sets the QR code.
     *
     * @param qrCode The QR code
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * Gets the notes.
     *
     * @return The notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes.
     *
     * @param notes The notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Checks if the invoice has been sent.
     *
     * @return true if the invoice has been sent, false otherwise
     */
    public boolean isSent() {
        return sent;
    }

    /**
     * Sets whether the invoice has been sent.
     *
     * @param sent Whether the invoice has been sent
     */
    public void setSent(boolean sent) {
        this.sent = sent;
    }

    /**
     * Gets the sent date.
     *
     * @return The sent date
     */
    public Date getSentDate() {
        return sentDate;
    }

    /**
     * Sets the sent date.
     *
     * @param sentDate The sent date
     */
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    /**
     * Checks if the invoice has been paid.
     *
     * @return true if the invoice has been paid, false otherwise
     */
    public boolean isPaid() {
        return paid;
    }

    /**
     * Sets whether the invoice has been paid.
     *
     * @param paid Whether the invoice has been paid
     */
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    /**
     * Gets the paid date.
     *
     * @return The paid date
     */
    public Date getPaidDate() {
        return paidDate;
    }

    /**
     * Sets the paid date.
     *
     * @param paidDate The paid date
     */
    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    /**
     * Checks if the invoice has been cancelled.
     *
     * @return true if the invoice has been cancelled, false otherwise
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether the invoice has been cancelled.
     *
     * @param cancelled Whether the invoice has been cancelled
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets the cancelled date.
     *
     * @return The cancelled date
     */
    public Date getCancelledDate() {
        return cancelledDate;
    }

    /**
     * Sets the cancelled date.
     *
     * @param cancelledDate The cancelled date
     */
    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    /**
     * Gets the cancel reason.
     *
     * @return The cancel reason
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * Sets the cancel reason.
     *
     * @param cancelReason The cancel reason
     */
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * Gets the CUFE (Código Único de Facturación Electrónica).
     *
     * @return The CUFE
     */
    public String getCufe() {
        return cufe;
    }

    /**
     * Sets the CUFE (Código Único de Facturación Electrónica).
     *
     * @param cufe The CUFE
     */
    public void setCufe(String cufe) {
        this.cufe = cufe;
    }

    /**
     * Gets the associated service.
     *
     * @return The service
     */
    public Service getService() {
        return service;
    }

    /**
     * Sets the associated service.
     *
     * @param service The service
     */
    public void setService(Service service) {
        this.service = service;
    }

    /**
     * Gets the labor cost.
     *
     * @return The labor cost
     */
    public double getLaborCost() {
        return laborCost;
    }

    /**
     * Sets the labor cost.
     *
     * @param laborCost The labor cost
     */
    public void setLaborCost(double laborCost) {
        this.laborCost = laborCost;
    }

    /**
     * Gets the parts subtotal.
     *
     * @return The parts subtotal
     */
    public double getPartsSubtotal() {
        return partsSubtotal;
    }

    /**
     * Sets the parts subtotal.
     *
     * @param partsSubtotal The parts subtotal
     */
    public void setPartsSubtotal(double partsSubtotal) {
        this.partsSubtotal = partsSubtotal;
    }

    /**
     * Gets the tax.
     *
     * @return The tax
     */
    public double getTax() {
        return tax;
    }

    /**
     * Sets the tax.
     *
     * @param tax The tax
     */
    public void setTax(double tax) {
        this.tax = tax;
    }

    /**
     * Calculates the totals for the invoice.
     */
    public void calculateTotals() {
        // Calculate tax amount
        this.taxAmount = this.subtotal * this.taxRate;

        // Calculate total
        this.total = this.subtotal + this.taxAmount - this.discount;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", issueDate=" + issueDate +
                ", total=" + total +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}