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


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartDAO implements PartDAOInterface {
    private Connection db;

    public PartDAO() {
        this.db = DatabaseConnection.getConnection();
    }

    @Override
    public void save(Part part) {
        String sql = "INSERT INTO parts (name, type, compatible_make_model, supplier_id, quantity_in_stock, minimum_stock, entry_date, estimated_lifespan, status, batch_id, creation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, part.getName());
            pstmt.setString(2, part.getType());
            pstmt.setString(3, part.getCompatibleMakeModel());
            pstmt.setObject(4, part.getSupplierId()); // Puede ser null
            pstmt.setInt(5, part.getQuantityInStock());
            pstmt.setInt(6, part.getMinimumStock());
            pstmt.setDate(7, part.getEntryDate() != null ? new java.sql.Date(part.getEntryDate().getTime()) : null);
            pstmt.setDate(8, part.getEstimatedLifespan() != null ? new java.sql.Date(part.getEstimatedLifespan().getTime()) : null);
            pstmt.setString(9, part.getStatus());
            pstmt.setString(10, part.getBatchId());
            pstmt.setTimestamp(11, part.getCreationDate() != null ? new java.sql.Timestamp(part.getCreationDate().getTime()) : new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                part.setPartId(rs.getInt(1));
            }
        } catch (Exception e) {
            System.err.println("Error saving part: " + e.getMessage());
        }
    }

    @Override
    public List<Part> findAll() {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts";
        try (PreparedStatement pstmt = db.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                parts.add(new Part(
                    rs.getInt("part_id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getString("compatible_make_model"),
                    rs.getObject("supplier_id") != null ? rs.getInt("supplier_id") : null,
                    rs.getInt("quantity_in_stock"),
                    rs.getInt("minimum_stock"),
                    rs.getDate("entry_date"),
                    rs.getDate("estimated_lifespan"),
                    rs.getString("status"),
                    rs.getString("batch_id"),
                    rs.getTimestamp("creation_date"),
                    rs.getTimestamp("last_update_date")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error fetching parts: " + e.getMessage());
        }
        return parts;
    }

    @Override
    public Part findById(int id) {
        String sql = "SELECT * FROM parts WHERE part_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Part(
                    rs.getInt("part_id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getString("compatible_make_model"),
                    rs.getObject("supplier_id") != null ? rs.getInt("supplier_id") : null,
                    rs.getInt("quantity_in_stock"),
                    rs.getInt("minimum_stock"),
                    rs.getDate("entry_date"),
                    rs.getDate("estimated_lifespan"),
                    rs.getString("status"),
                    rs.getString("batch_id"),
                    rs.getTimestamp("creation_date"),
                    rs.getTimestamp("last_update_date")
                );
            }
        } catch (Exception e) {
            System.err.println("Error finding part: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Part part) {
        String sql = "UPDATE parts SET name = ?, type = ?, compatible_make_model = ?, supplier_id = ?, quantity_in_stock = ?, minimum_stock = ?, entry_date = ?, estimated_lifespan = ?, status = ?, batch_id = ? WHERE part_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setString(1, part.getName());
            pstmt.setString(2, part.getType());
            pstmt.setString(3, part.getCompatibleMakeModel());
            pstmt.setObject(4, part.getSupplierId());
            pstmt.setInt(5, part.getQuantityInStock());
            pstmt.setInt(6, part.getMinimumStock());
            pstmt.setDate(7, part.getEntryDate() != null ? new java.sql.Date(part.getEntryDate().getTime()) : null);
            pstmt.setDate(8, part.getEstimatedLifespan() != null ? new java.sql.Date(part.getEstimatedLifespan().getTime()) : null);
            pstmt.setString(9, part.getStatus());
            pstmt.setString(10, part.getBatchId());
            pstmt.setInt(11, part.getPartId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error updating part: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM parts WHERE part_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error deleting part: " + e.getMessage());
        }
    }

    @Override
    public void updateStock(int partId, int quantity) {
        String sql = "UPDATE parts SET quantity_in_stock = quantity_in_stock + ? WHERE part_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, partId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error updating stock: " + e.getMessage());
        }
    }

    @Override
    public void recordPartUsage(int serviceId, int partId, int quantityUsed, double unitPrice) {
        String sql = "INSERT INTO parts_in_service (service_id, part_id, quantity_used, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, serviceId);
            pstmt.setInt(2, partId);
            pstmt.setInt(3, quantityUsed);
            pstmt.setDouble(4, unitPrice);
            pstmt.executeUpdate();
            updateStock(partId, -quantityUsed); // Reducir el stock
        } catch (Exception e) {
            throw new RuntimeException("Error recording part usage: " + e.getMessage());
        }
    }

    @Override
    public boolean checkExpiration(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}