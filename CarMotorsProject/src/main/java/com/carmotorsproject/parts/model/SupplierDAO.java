/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import com.carmotorsproject.customers.model.Customer;
import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO implements SupplierDAOInterface {
    private Connection db;

    public SupplierDAO() {
        this.db = DatabaseConnection.getConnection();
    }

    @Override
    public void save(Supplier supplier) {
        String checkSql = "SELECT COUNT(*) FROM suppliers WHERE name = ?";
        try (PreparedStatement checkStmt = db.prepareStatement(checkSql)) {
            checkStmt.setString(1, supplier.getName());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new IllegalArgumentException("A supplier with the name '" + supplier.getName() + "' already exists.");
            }

            String sql = "INSERT INTO suppliers (name, tax_id, contact) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, supplier.getName());
                pstmt.setString(2, supplier.getTaxId());
                pstmt.setString(3, supplier.getContact());
                pstmt.executeUpdate();

                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    supplier.setSupplierId(generatedKeys.getInt(1));
                }
            }
        } catch (IllegalArgumentException e) {
            throw e; // Re-lanzar la excepción de duplicado
        } catch (Exception e) {
            System.err.println("Error saving supplier: " + e.getMessage());
            throw new RuntimeException("Error saving supplier: " + e.getMessage());
        }
    }

    @Override
    public List<Supplier> findAll() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";
        try (PreparedStatement pstmt = db.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                suppliers.add(new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("tax_id"),
                    rs.getString("contact")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error fetching suppliers: " + e.getMessage());
        }
        return suppliers;
    }

    @Override
    public Supplier findById(int id) {
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("tax_id"),
                    rs.getString("contact")
                );
            }
        } catch (Exception e) {
            System.err.println("Error finding supplier: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Supplier supplier) {
        String sql = "UPDATE suppliers SET name = ?, tax_id = ?, contact = ? WHERE supplier_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getTaxId());
            pstmt.setString(3, supplier.getContact());
            pstmt.setInt(4, supplier.getSupplierId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Supplier not found for update.");
            }
        } catch (Exception e) {
            System.err.println("Error updating supplier: " + e.getMessage());
            throw new RuntimeException("Error updating supplier: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Supplier not found for deletion.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting supplier: " + e.getMessage());
        }
    }

    @Override
    public List<Supplier> searchByName(String name) {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers WHERE name LIKE ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                suppliers.add(new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("tax_id"),
                    rs.getString("contact")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error searching suppliers: " + e.getMessage());
        }
        return suppliers;
    }
}