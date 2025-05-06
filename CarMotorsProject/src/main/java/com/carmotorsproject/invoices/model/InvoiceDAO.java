/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.invoices.model;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO implements InvoiceDAOInterface {
    private Connection db;

    public InvoiceDAO() {
        this.db = DatabaseConnection.getConnection();
    }

    @Override
    public void save(Invoice invoice) {
        String sql = "INSERT INTO invoices (service_id, issue_date, invoice_number, subtotal, taxes, total, electronic_invoice_id, qr_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, invoice.getServiceId());
            pstmt.setTimestamp(2, new Timestamp(invoice.getIssueDate().getTime()));
            pstmt.setString(3, invoice.getInvoiceNumber());
            pstmt.setDouble(4, invoice.getSubtotal());
            pstmt.setDouble(5, invoice.getTaxes());
            pstmt.setDouble(6, invoice.getTotal());
            pstmt.setString(7, invoice.getElectronicInvoiceId());
            pstmt.setString(8, invoice.getQrCode());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                invoice.setInvoiceId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving invoice: " + e.getMessage());
        }
    }

    @Override
    public Invoice findById(int id) {
        String sql = "SELECT i.*, c.name AS customer_name, c.identification_number AS customer_document, c.address AS customer_address, " +
                    "s.description AS service_description, s.labor_cost, " +
                    "COALESCE(SUM(pis.quantity_used), 0) AS parts_quantity " +
                    "FROM invoices i " +
                    "JOIN services s ON i.service_id = s.service_id " +
                    "JOIN vehicles v ON s.vehicle_id = v.vehicle_id " +
                    "JOIN customers c ON v.customer_id = c.customer_id " +
                    "LEFT JOIN parts_in_service pis ON s.service_id = pis.service_id " +
                    "WHERE i.invoice_id = ? " +
                    "GROUP BY i.invoice_id";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Invoice(
                    rs.getInt("invoice_id"),
                    rs.getInt("service_id"),
                    rs.getTimestamp("issue_date"),
                    rs.getString("invoice_number"),
                    rs.getDouble("subtotal"),
                    rs.getDouble("taxes"),
                    rs.getDouble("total"),
                    rs.getString("electronic_invoice_id"),
                    rs.getString("qr_code"),
                    rs.getString("customer_name"),
                    rs.getString("customer_document"),
                    rs.getString("customer_address"),
                    rs.getString("service_description"),
                    rs.getInt("parts_quantity"),
                    rs.getDouble("labor_cost")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error searching for invoice: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Invoice> findAll() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT i.*, c.name AS customer_name, c.identification_number AS customer_document, c.address AS customer_address, " +
                    "s.description AS service_description, s.labor_cost, " +
                    "COALESCE(SUM(pis.quantity_used), 0) AS parts_quantity " +
                    "FROM invoices i " +
                    "JOIN services s ON i.service_id = s.service_id " +
                    "JOIN vehicles v ON s.vehicle_id = v.vehicle_id " +
                    "JOIN customers c ON v.customer_id = c.customer_id " +
                    "LEFT JOIN parts_in_service pis ON s.service_id = pis.service_id " +
                    "GROUP BY i.invoice_id";
        try (PreparedStatement pstmt = db.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                invoices.add(new Invoice(
                    rs.getInt("invoice_id"),
                    rs.getInt("service_id"),
                    rs.getTimestamp("issue_date"),
                    rs.getString("invoice_number"),
                    rs.getDouble("subtotal"),
                    rs.getDouble("taxes"),
                    rs.getDouble("total"),
                    rs.getString("electronic_invoice_id"),
                    rs.getString("qr_code"),
                    rs.getString("customer_name"),
                    rs.getString("customer_document"),
                    rs.getString("customer_address"),
                    rs.getString("service_description"),
                    rs.getInt("parts_quantity"),
                    rs.getDouble("labor_cost")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting invoices:" + e.getMessage());
        }
        return invoices;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM invoices WHERE invoice_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting invoice: " + e.getMessage());
        }
    }
}