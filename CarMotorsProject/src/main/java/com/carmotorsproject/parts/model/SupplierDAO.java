/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

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
        String sql = "INSERT INTO suppliers (name, tax_id, contact) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getTaxId());
            pstmt.setString(3, supplier.getContact());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                supplier.setSupplierId(rs.getInt(1));
            }
        } catch (Exception e) {
            System.err.println("Error saving supplier: " + e.getMessage());
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
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error updating supplier: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
        }
    }
}