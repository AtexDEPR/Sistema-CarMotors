/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseOrderDAO implements PurchaseOrderDAOInterface {

    private Connection db;

    public PurchaseOrderDAO() {
        this.db = DatabaseConnection.getConnection();
    }

    @Override
    public void save(PurchaseOrder order) {
        String sql = "INSERT INTO purchase_orders (supplier_id, expected_delivery_date, status, observations) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, order.getSupplierId());
            pstmt.setDate(2, order.getExpectedDeliveryDate() != null ? new java.sql.Date(order.getExpectedDeliveryDate().getTime()) : null);
            pstmt.setString(3, order.getStatus());
            pstmt.setString(4, order.getObservations());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);
                order.setPurchaseOrderId(orderId);
                saveDetails(orderId, order.getDetails());
            }
        } catch (SQLException e) {
            System.err.println("Error saving purchase order: " + e.getMessage());
        }
    }

    private void saveDetails(int orderId, List<PurchaseOrderDetail> details) {
        String sql = "INSERT INTO purchase_order_detail (purchase_order_id, part_id, quantity_ordered, estimated_unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            for (PurchaseOrderDetail detail : details) {
                pstmt.setInt(1, orderId);
                pstmt.setInt(2, detail.getPartId());
                pstmt.setInt(3, detail.getQuantityOrdered());
                pstmt.setDouble(4, detail.getEstimatedUnitPrice());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Error saving purchase order details: " + e.getMessage());
        }
    }

    @Override
    public PurchaseOrder findById(int id) {
        String sql = "SELECT po.*, pod.part_id, pod.quantity_ordered, pod.estimated_unit_price " +
                     "FROM purchase_orders po " +
                     "LEFT JOIN purchase_order_detail pod ON po.purchase_order_id = pod.purchase_order_id " +
                     "WHERE po.purchase_order_id = ?";
        PurchaseOrder order = null;
        List<PurchaseOrderDetail> details = new ArrayList<>();
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (order == null) {
                    order = new PurchaseOrder(
                        rs.getInt("purchase_order_id"),
                        rs.getInt("supplier_id"),
                        rs.getTimestamp("creation_date"),
                        rs.getDate("expected_delivery_date"),
                        rs.getString("status"),
                        rs.getString("observations"),
                        new ArrayList<>()
                    );
                }
                int partId = rs.getInt("part_id");
                if (rs.wasNull()) continue;
                details.add(new PurchaseOrderDetail(
                    0,
                    id,
                    partId,
                    rs.getInt("quantity_ordered"),
                    rs.getDouble("estimated_unit_price")
                ));
            }
            if (order != null) {
                order.setDetails(details);
            }
        } catch (SQLException e) {
            System.err.println("Error finding purchase order: " + e.getMessage());
        }
        return order;
    }

    @Override
    public List<PurchaseOrder> findAll() {
        String sql = "SELECT po.*, pod.part_id, pod.quantity_ordered, pod.estimated_unit_price " +
                     "FROM purchase_orders po " +
                     "LEFT JOIN purchase_order_detail pod ON po.purchase_order_id = pod.purchase_order_id";
        List<PurchaseOrder> orders = new ArrayList<>();
        try (PreparedStatement stmt = db.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            int lastOrderId = -1;
            PurchaseOrder currentOrder = null;
            while (rs.next()) {
                int orderId = rs.getInt("purchase_order_id");
                if (lastOrderId != orderId) {
                    if (currentOrder != null) {
                        orders.add(currentOrder);
                    }
                    currentOrder = new PurchaseOrder(
                        orderId,
                        rs.getInt("supplier_id"),
                        rs.getTimestamp("creation_date"),
                        rs.getDate("expected_delivery_date"),
                        rs.getString("status"),
                        rs.getString("observations"),
                        new ArrayList<>()
                    );
                    lastOrderId = orderId;
                }
                int partId = rs.getInt("part_id");
                if (!rs.wasNull()) {
                    currentOrder.getDetails().add(new PurchaseOrderDetail(
                        0,
                        orderId,
                        partId,
                        rs.getInt("quantity_ordered"),
                        rs.getDouble("estimated_unit_price")
                    ));
                }
            }
            if (currentOrder != null) {
                orders.add(currentOrder);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching purchase orders: " + e.getMessage());
        }
        return orders;
    }

    @Override
    public void update(PurchaseOrder order) {
        String sql = "UPDATE purchase_orders SET supplier_id = ?, expected_delivery_date = ?, status = ?, observations = ? WHERE purchase_order_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, order.getSupplierId());
            pstmt.setDate(2, order.getExpectedDeliveryDate() != null ? new java.sql.Date(order.getExpectedDeliveryDate().getTime()) : null);
            pstmt.setString(3, order.getStatus());
            pstmt.setString(4, order.getObservations());
            pstmt.setInt(5, order.getPurchaseOrderId());
            pstmt.executeUpdate();

            String deleteSql = "DELETE FROM purchase_order_detail WHERE purchase_order_id = ?";
            try (PreparedStatement deleteStmt = db.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, order.getPurchaseOrderId());
                deleteStmt.executeUpdate();
            }

            saveDetails(order.getPurchaseOrderId(), order.getDetails());
        } catch (SQLException e) {
            System.err.println("Error updating purchase order: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM purchase_orders WHERE purchase_order_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting purchase order: " + e.getMessage());
        }
    }
}