/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.invoices.controller;

import com.carmotorsproject.invoices.model.Invoice;
import com.carmotorsproject.invoices.model.InvoiceDAO;
import com.carmotorsproject.invoices.model.PDFGenerator;
import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InvoiceController {
    private InvoiceDAO invoiceDAO;
    private Connection db;

    public InvoiceController() {
        this.invoiceDAO = new InvoiceDAO();
        this.db = DatabaseConnection.getConnection();
    }

    public void addInvoice(Invoice invoice) {
        // Calcular costos
        double partsCost = calculatePartsCost(invoice.getServiceId());
        double laborCost = getLaborCost(invoice.getServiceId());
        double subtotal = partsCost + laborCost;
        double taxes = subtotal * 0.19; // 19% IVA
        double total = subtotal + taxes;

        invoice.setSubtotal(subtotal);
        invoice.setTaxes(taxes);
        invoice.setTotal(total);

        invoiceDAO.save(invoice);
    }

    public void deleteInvoice(int invoiceId) {
        invoiceDAO.delete(invoiceId);
    }

    public Invoice findById(int invoiceId) {
        return invoiceDAO.findById(invoiceId);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceDAO.findAll();
    }

    public void generateInvoicePDF(int invoiceId) {
        /*Invoice invoice = findById(invoiceId);
        if (invoice != null) {
            PDFGenerator.generateInvoicePDF(invoice);
        } else {
            throw new RuntimeException("Factura no encontrada");
        }*/
    }

    private double calculatePartsCost(int serviceId) {
        String sql = "SELECT SUM(pis.quantity_used * pis.unit_price) AS parts_cost " +
                    "FROM parts_in_service pis " +
                    "WHERE pis.service_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, serviceId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("parts_cost") != 0 ? rs.getDouble("parts_cost") : 0.0;
            }
        } catch (SQLException e) {
            System.err.println("Error calculating cost of parts: " + e.getMessage());
        }
        return 0.0;
    }

    private double getLaborCost(int serviceId) {
        String sql = "SELECT labor_cost FROM services WHERE service_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, serviceId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("labor_cost") != 0 ? rs.getDouble("labor_cost") : 0.0;
            }
        } catch (SQLException e) {
            System.err.println("Error getting labor cost: " + e.getMessage());
        }
        return 0.0;
    }
}