/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.invoices.model;

import com.carmotorsproject.invoices.model.Invoice;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interface for invoice data access operations.
 * Defines methods for CRUD operations and specialized queries.
 */
public interface InvoiceDAOInterface {

    /**
     * Saves a new invoice to the database.
     *
     * @param invoice The invoice to save
     * @return The saved invoice with its generated ID
     * @throws SQLException If a database access error occurs
     */
    Invoice save(Invoice invoice) throws SQLException;

    /**
     * Updates an existing invoice in the database.
     *
     * @param invoice The invoice to update
     * @return The updated invoice
     * @throws SQLException If a database access error occurs
     */
    Invoice update(Invoice invoice) throws SQLException;

    /**
     * Deletes an invoice from the database.
     *
     * @param id The ID of the invoice to delete
     * @return true if the invoice was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds an invoice by its ID.
     *
     * @param id The ID of the invoice to find
     * @return The invoice, or null if not found
     * @throws SQLException If a database access error occurs
     */
    Invoice findById(int id) throws SQLException;

    /**
     * Finds an invoice by its invoice number.
     *
     * @param invoiceNumber The invoice number to find
     * @return The invoice, or null if not found
     * @throws SQLException If a database access error occurs
     */
    Invoice findByInvoiceNumber(String invoiceNumber) throws SQLException;

    /**
     * Finds all invoices in the database.
     *
     * @return A list of all invoices
     * @throws SQLException If a database access error occurs
     */
    List<Invoice> findAll() throws SQLException;

    /**
     * Finds invoices by their service ID.
     *
     * @param serviceId The ID of the service
     * @return A list of invoices for the specified service
     * @throws SQLException If a database access error occurs
     */
    List<Invoice> findByService(int serviceId) throws SQLException;

    /**
     * Finds invoices by their customer ID.
     *
     * @param customerId The ID of the customer
     * @return A list of invoices for the specified customer
     * @throws SQLException If a database access error occurs
     */
    List<Invoice> findByCustomer(int customerId) throws SQLException;

    /**
     * Finds invoices by their issue date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of invoices issued within the date range
     * @throws SQLException If a database access error occurs
     */
    List<Invoice> findByIssueDateRange(Date startDate, Date endDate) throws SQLException;

    /**
     * Finds invoices by their payment status.
     *
     * @param paymentStatus The payment status to search for
     * @return A list of invoices with the specified payment status
     * @throws SQLException If a database access error occurs
     */
    List<Invoice> findByPaymentStatus(String paymentStatus) throws SQLException;

    /**
     * Finds paid invoices.
     *
     * @return A list of paid invoices
     * @throws SQLException If a database access error occurs
     */
    List<Invoice> findPaid() throws SQLException;

    /**
     * Finds unpaid invoices.
     *
     * @return A list of unpaid invoices
     * @throws SQLException If a database access error occurs
     */
    List<Invoice> findUnpaid() throws SQLException;

    /**
     * Finds cancelled invoices.
     *
     * @return A list of cancelled invoices
     * @throws SQLException If a database access error occurs
     */
    List<Invoice> findCancelled() throws SQLException;

    /**
     * Finds sent invoices.
     *
     * @return A list of sent invoices
     * @throws SQLException If a database access error occurs
     */
    List<Invoice> findSent() throws SQLException;

    /**
     * Finds unsent invoices.
     *
     * @return A list of unsent invoices
     * @throws SQLException If a database access error occurs
     */
    List<Invoice> findUnsent() throws SQLException;

    /**
     * Marks an invoice as sent.
     *
     * @param invoiceId The ID of the invoice
     * @return true if the invoice was marked as sent, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean markAsSent(int invoiceId) throws SQLException;

    /**
     * Marks an invoice as paid.
     *
     * @param invoiceId The ID of the invoice
     * @param paymentMethod The payment method used
     * @return true if the invoice was marked as paid, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean markAsPaid(int invoiceId, String paymentMethod) throws SQLException;

    /**
     * Marks an invoice as cancelled.
     *
     * @param invoiceId The ID of the invoice
     * @param reason The reason for cancellation
     * @return true if the invoice was marked as cancelled, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean markAsCancelled(int invoiceId, String reason) throws SQLException;

    /**
     * Generates a unique invoice number.
     *
     * @return A unique invoice number
     * @throws SQLException If a database access error occurs
     */
    String generateInvoiceNumber() throws SQLException;

    /**
     * Generates a QR code for an invoice.
     *
     * @param invoice The invoice to generate a QR code for
     * @return The path to the generated QR code image
     * @throws Exception If an error occurs during QR code generation
     */
    String generateQRCode(Invoice invoice) throws Exception;
}