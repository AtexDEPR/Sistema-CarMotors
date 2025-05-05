/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.invoices.model;

import com.carmotorsproject.config.DatabaseConnection;
import com.carmotorsproject.utils.QRGenerator;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of InvoiceDAOInterface that provides data access operations for invoices.
 */
public class InvoiceDAO implements InvoiceDAOInterface {

    private static final Logger LOGGER = Logger.getLogger(InvoiceDAO.class.getName());
    private final Connection connection;
    private final QRGenerator qrGenerator;

    /**
     * Constructor that initializes the database connection and QR generator.
     */
    public InvoiceDAO() {
        // Fix: Use getInstance().getConnection() instead of static getConnection()
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.qrGenerator = new QRGenerator();
        LOGGER.log(Level.INFO, "InvoiceDAO initialized with database connection");
    }

    /**
     * Saves a new invoice to the database.
     *
     * @param invoice The invoice to save
     * @return The saved invoice with its generated ID
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Invoice save(Invoice invoice) throws SQLException {
        // Generate invoice number if not provided
        if (invoice.getInvoiceNumber() == null || invoice.getInvoiceNumber().isEmpty()) {
            invoice.setInvoiceNumber(generateInvoiceNumber());
        }

        // Set due date if not provided (default: 30 days from issue date)
        if (invoice.getDueDate() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(invoice.getIssueDate());
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            invoice.setDueDate(calendar.getTime());
        }

        // Calculate totals
        invoice.calculateTotals();

        // Generate QR code
        try {
            String qrCodePath = generateQRCode(invoice);
            invoice.setQrCode(qrCodePath);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Failed to generate QR code for invoice", ex);
        }

        String sql = "INSERT INTO invoices (service_id, customer_id, invoice_number, issue_date, due_date, " +
                "subtotal, tax_rate, tax_amount, discount, total, payment_method, payment_status, " +
                "qr_code, notes, sent, sent_date, paid, paid_date, cancelled, cancelled_date, cancel_reason) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            stmt.setInt(1, invoice.getServiceId());
            stmt.setInt(2, invoice.getCustomerId());
            stmt.setString(3, invoice.getInvoiceNumber());

            if (invoice.getIssueDate() != null) {
                stmt.setTimestamp(4, new Timestamp(invoice.getIssueDate().getTime()));
            } else {
                stmt.setTimestamp(4, new Timestamp(new Date().getTime())); // Default to current date
            }

            if (invoice.getDueDate() != null) {
                stmt.setTimestamp(5, new Timestamp(invoice.getDueDate().getTime()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }

            stmt.setDouble(6, invoice.getSubtotal());
            stmt.setDouble(7, invoice.getTaxRate());
            stmt.setDouble(8, invoice.getTaxAmount());
            stmt.setDouble(9, invoice.getDiscount());
            stmt.setDouble(10, invoice.getTotal());
            stmt.setString(11, invoice.getPaymentMethod());
            stmt.setString(12, invoice.getPaymentStatus());
            stmt.setString(13, invoice.getQrCode());
            stmt.setString(14, invoice.getNotes());
            stmt.setBoolean(15, invoice.isSent());

            if (invoice.getSentDate() != null) {
                stmt.setTimestamp(16, new Timestamp(invoice.getSentDate().getTime()));
            } else {
                stmt.setNull(16, Types.TIMESTAMP);
            }

            stmt.setBoolean(17, invoice.isPaid());

            if (invoice.getPaidDate() != null) {
                stmt.setTimestamp(18, new Timestamp(invoice.getPaidDate().getTime()));
            } else {
                stmt.setNull(18, Types.TIMESTAMP);
            }

            stmt.setBoolean(19, invoice.isCancelled());

            if (invoice.getCancelledDate() != null) {
                stmt.setTimestamp(20, new Timestamp(invoice.getCancelledDate().getTime()));
            } else {
                stmt.setNull(20, Types.TIMESTAMP);
            }

            stmt.setString(21, invoice.getCancelReason());

            // Execute insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating invoice failed, no rows affected.");
            }

            // Get generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice.setInvoiceId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating invoice failed, no ID obtained.");
                }
            }

            LOGGER.log(Level.INFO, "Invoice created with ID: {0}, Number: {1}",
                    new Object[]{invoice.getInvoiceId(), invoice.getInvoiceNumber()});
            return invoice;
        }
    }

    /**
     * Updates an existing invoice in the database.
     *
     * @param invoice The invoice to update
     * @return The updated invoice
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Invoice update(Invoice invoice) throws SQLException {
        // Calculate totals
        invoice.calculateTotals();

        String sql = "UPDATE invoices SET service_id = ?, customer_id = ?, invoice_number = ?, " +
                "issue_date = ?, due_date = ?, subtotal = ?, tax_rate = ?, tax_amount = ?, " +
                "discount = ?, total = ?, payment_method = ?, payment_status = ?, qr_code = ?, " +
                "notes = ?, sent = ?, sent_date = ?, paid = ?, paid_date = ?, cancelled = ?, " +
                "cancelled_date = ?, cancel_reason = ? WHERE invoice_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set parameters
            stmt.setInt(1, invoice.getServiceId());
            stmt.setInt(2, invoice.getCustomerId());
            stmt.setString(3, invoice.getInvoiceNumber());

            if (invoice.getIssueDate() != null) {
                stmt.setTimestamp(4, new Timestamp(invoice.getIssueDate().getTime()));
            } else {
                stmt.setTimestamp(4, new Timestamp(new Date().getTime())); // Default to current date
            }

            if (invoice.getDueDate() != null) {
                stmt.setTimestamp(5, new Timestamp(invoice.getDueDate().getTime()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }

            stmt.setDouble(6, invoice.getSubtotal());
            stmt.setDouble(7, invoice.getTaxRate());
            stmt.setDouble(8, invoice.getTaxAmount());
            stmt.setDouble(9, invoice.getDiscount());
            stmt.setDouble(10, invoice.getTotal());
            stmt.setString(11, invoice.getPaymentMethod());
            stmt.setString(12, invoice.getPaymentStatus());
            stmt.setString(13, invoice.getQrCode());
            stmt.setString(14, invoice.getNotes());
            stmt.setBoolean(15, invoice.isSent());

            if (invoice.getSentDate() != null) {
                stmt.setTimestamp(16, new Timestamp(invoice.getSentDate().getTime()));
            } else {
                stmt.setNull(16, Types.TIMESTAMP);
            }

            stmt.setBoolean(17, invoice.isPaid());

            if (invoice.getPaidDate() != null) {
                stmt.setTimestamp(18, new Timestamp(invoice.getPaidDate().getTime()));
            } else {
                stmt.setNull(18, Types.TIMESTAMP);
            }

            stmt.setBoolean(19, invoice.isCancelled());

            if (invoice.getCancelledDate() != null) {
                stmt.setTimestamp(20, new Timestamp(invoice.getCancelledDate().getTime()));
            } else {
                stmt.setNull(20, Types.TIMESTAMP);
            }

            stmt.setString(21, invoice.getCancelReason());
            stmt.setInt(22, invoice.getInvoiceId());

            // Execute update
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating invoice failed, no rows affected.");
            }

            LOGGER.log(Level.INFO, "Invoice updated with ID: {0}, Number: {1}",
                    new Object[]{invoice.getInvoiceId(), invoice.getInvoiceNumber()});
            return invoice;
        }
    }

    /**
     * Deletes an invoice from the database.
     *
     * @param id The ID of the invoice to delete
     * @return true if the invoice was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM invoices WHERE invoice_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Invoice deleted with ID: {0}, Rows affected: {1}",
                    new Object[]{id, affectedRows});

            return affectedRows > 0;
        }
    }

    /**
     * Finds an invoice by its ID.
     *
     * @param id The ID of the invoice to find
     * @return The invoice, or null if not found
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Invoice findById(int id) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE invoice_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoice(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Finds an invoice by its invoice number.
     *
     * @param invoiceNumber The invoice number to find
     * @return The invoice, or null if not found
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Invoice findByInvoiceNumber(String invoiceNumber) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE invoice_number = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, invoiceNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoice(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Finds all invoices in the database.
     *
     * @return A list of all invoices
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Invoice> findAll() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices ORDER BY issue_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        }

        return invoices;
    }

    /**
     * Finds invoices by their service ID.
     *
     * @param serviceId The ID of the service
     * @return A list of invoices for the specified service
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Invoice> findByService(int serviceId) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE service_id = ? ORDER BY issue_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, serviceId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        }

        return invoices;
    }

    /**
     * Finds invoices by their customer ID.
     *
     * @param customerId The ID of the customer
     * @return A list of invoices for the specified customer
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Invoice> findByCustomer(int customerId) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE customer_id = ? ORDER BY issue_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        }

        return invoices;
    }

    /**
     * Finds invoices by their issue date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of invoices issued within the date range
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Invoice> findByIssueDateRange(Date startDate, Date endDate) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE issue_date BETWEEN ? AND ? ORDER BY issue_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        }

        return invoices;
    }

    /**
     * Finds invoices by their payment status.
     *
     * @param paymentStatus The payment status to search for
     * @return A list of invoices with the specified payment status
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Invoice> findByPaymentStatus(String paymentStatus) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE payment_status = ? ORDER BY issue_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paymentStatus);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        }

        return invoices;
    }

    /**
     * Finds paid invoices.
     *
     * @return A list of paid invoices
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Invoice> findPaid() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE paid = TRUE ORDER BY paid_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        }

        return invoices;
    }

    /**
     * Finds unpaid invoices.
     *
     * @return A list of unpaid invoices
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Invoice> findUnpaid() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE paid = FALSE AND cancelled = FALSE ORDER BY due_date ASC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        }

        return invoices;
    }

    /**
     * Finds cancelled invoices.
     *
     * @return A list of cancelled invoices
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Invoice> findCancelled() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE cancelled = TRUE ORDER BY cancelled_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        }

        return invoices;
    }

    /**
     * Finds sent invoices.
     *
     * @return A list of sent invoices
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Invoice> findSent() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE sent = TRUE ORDER BY sent_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        }

        return invoices;
    }

    /**
     * Finds unsent invoices.
     *
     * @return A list of unsent invoices
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Invoice> findUnsent() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE sent = FALSE AND cancelled = FALSE ORDER BY issue_date ASC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        }

        return invoices;
    }

    /**
     * Marks an invoice as sent.
     *
     * @param invoiceId The ID of the invoice
     * @return true if the invoice was marked as sent, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean markAsSent(int invoiceId) throws SQLException {
        String sql = "UPDATE invoices SET sent = TRUE, sent_date = ? WHERE invoice_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            stmt.setInt(2, invoiceId);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Invoice marked as sent: ID {0}", invoiceId);

            return affectedRows > 0;
        }
    }

    /**
     * Marks an invoice as paid.
     *
     * @param invoiceId The ID of the invoice
     * @param paymentMethod The payment method used
     * @return true if the invoice was marked as paid, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean markAsPaid(int invoiceId, String paymentMethod) throws SQLException {
        String sql = "UPDATE invoices SET paid = TRUE, paid_date = ?, payment_method = ?, payment_status = 'PAID' WHERE invoice_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            stmt.setString(2, paymentMethod);
            stmt.setInt(3, invoiceId);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Invoice marked as paid: ID {0}, Payment Method: {1}",
                    new Object[]{invoiceId, paymentMethod});

            return affectedRows > 0;
        }
    }

    /**
     * Marks an invoice as cancelled.
     *
     * @param invoiceId The ID of the invoice
     * @param reason The reason for cancellation
     * @return true if the invoice was marked as cancelled, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean markAsCancelled(int invoiceId, String reason) throws SQLException {
        String sql = "UPDATE invoices SET cancelled = TRUE, cancelled_date = ?, cancel_reason = ?, payment_status = 'CANCELLED' WHERE invoice_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            stmt.setString(2, reason);
            stmt.setInt(3, invoiceId);

            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Invoice marked as cancelled: ID {0}, Reason: {1}",
                    new Object[]{invoiceId, reason});

            return affectedRows > 0;
        }
    }

    /**
     * Generates a unique invoice number.
     *
     * @return A unique invoice number
     * @throws SQLException If a database access error occurs
     */
    @Override
    public String generateInvoiceNumber() throws SQLException {
        // Format: INV-YYYYMMDD-XXXX where XXXX is a sequential number
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePart = dateFormat.format(new Date());

        // Get the highest invoice number for today
        String sql = "SELECT MAX(invoice_number) FROM invoices WHERE invoice_number LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "INV-" + datePart + "-%");

            try (ResultSet rs = stmt.executeQuery()) {
                int sequenceNumber = 1;

                if (rs.next() && rs.getString(1) != null) {
                    String lastInvoiceNumber = rs.getString(1);
                    String[] parts = lastInvoiceNumber.split("-");
                    if (parts.length == 3) {
                        sequenceNumber = Integer.parseInt(parts[2]) + 1;
                    }
                }

                return String.format("INV-%s-%04d", datePart, sequenceNumber);
            }
        }
    }

    /**
     * Generates a QR code for an invoice.
     *
     * @param invoice The invoice to generate a QR code for
     * @return The path to the generated QR code image
     * @throws Exception If an error occurs during QR code generation
     */
    @Override
    public String generateQRCode(Invoice invoice) throws Exception {
        // Create QR code content
        String qrContent = String.format(
                "Invoice: %s\nDate: %s\nCustomer ID: %d\nTotal: $%.2f\nStatus: %s",
                invoice.getInvoiceNumber(),
                new SimpleDateFormat("yyyy-MM-dd").format(invoice.getIssueDate()),
                invoice.getCustomerId(),
                invoice.getTotal(),
                invoice.getPaymentStatus()
        );

        // Generate QR code
        String fileName = "invoice_" + invoice.getInvoiceNumber().replace("-", "_") + ".png";
        String filePath = "qrcodes/" + fileName;

        // Fix: Remove the extra parameter (filePath) from the generateQRCode call
        qrGenerator.generateQRCode(qrContent, 300, 300);

        LOGGER.log(Level.INFO, "QR code generated for invoice {0}: {1}",
                new Object[]{invoice.getInvoiceNumber(), filePath});

        return filePath;
    }

    /**
     * Maps a ResultSet row to an Invoice object.
     *
     * @param rs The ResultSet containing invoice data
     * @return An Invoice object populated with data from the ResultSet
     * @throws SQLException If a database access error occurs
     */
    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();

        invoice.setInvoiceId(rs.getInt("invoice_id"));
        invoice.setServiceId(rs.getInt("service_id"));
        invoice.setCustomerId(rs.getInt("customer_id"));
        invoice.setInvoiceNumber(rs.getString("invoice_number"));

        Timestamp issueDate = rs.getTimestamp("issue_date");
        if (issueDate != null) {
            invoice.setIssueDate(new Date(issueDate.getTime()));
        }

        Timestamp dueDate = rs.getTimestamp("due_date");
        if (dueDate != null) {
            invoice.setDueDate(new Date(dueDate.getTime()));
        }

        invoice.setSubtotal(rs.getDouble("subtotal"));
        invoice.setTaxRate(rs.getDouble("tax_rate"));
        invoice.setTaxAmount(rs.getDouble("tax_amount"));
        invoice.setDiscount(rs.getDouble("discount"));
        invoice.setTotal(rs.getDouble("total"));
        invoice.setPaymentMethod(rs.getString("payment_method"));
        invoice.setPaymentStatus(rs.getString("payment_status"));
        invoice.setQrCode(rs.getString("qr_code"));
        invoice.setNotes(rs.getString("notes"));
        invoice.setSent(rs.getBoolean("sent"));

        Timestamp sentDate = rs.getTimestamp("sent_date");
        if (sentDate != null) {
            invoice.setSentDate(new Date(sentDate.getTime()));
        }

        invoice.setPaid(rs.getBoolean("paid"));

        Timestamp paidDate = rs.getTimestamp("paid_date");
        if (paidDate != null) {
            invoice.setPaidDate(new Date(paidDate.getTime()));
        }

        invoice.setCancelled(rs.getBoolean("cancelled"));

        Timestamp cancelledDate = rs.getTimestamp("cancelled_date");
        if (cancelledDate != null) {
            invoice.setCancelledDate(new Date(cancelledDate.getTime()));
        }

        invoice.setCancelReason(rs.getString("cancel_reason"));

        return invoice;
    }
}