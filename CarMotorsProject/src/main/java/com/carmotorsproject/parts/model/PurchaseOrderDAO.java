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
import java.sql.Timestamp;
import java.sql.Types;
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
        String sqlOrder = "INSERT INTO purchase_orders (order_date, status, supplier_id, total_amount, creation_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sqlOrder, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
            pstmt.setString(2, order.getStatus());
            if (order.getSupplierId() != null) {
                pstmt.setInt(3, order.getSupplierId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setDouble(4, order.getTotalAmount());
            pstmt.setTimestamp(5, new Timestamp(order.getCreationDate().getTime()));
            pstmt.setTimestamp(6, new Timestamp(order.getLastUpdateDate().getTime()));
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Order saved, rows affected: " + rowsAffected); // Depuración

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                order.setOrderId(rs.getInt(1));
                System.out.println("Generated order ID: " + order.getOrderId()); // Depuración
            }

            // Guardar detalles
            String sqlDetail = "INSERT INTO purchase_order_details (order_id, part_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmtDetail = db.prepareStatement(sqlDetail, PreparedStatement.RETURN_GENERATED_KEYS)) {
                for (PurchaseOrderDetail detail : order.getDetails()) {
                    pstmtDetail.setInt(1, order.getOrderId());
                    pstmtDetail.setInt(2, detail.getPartId());
                    pstmtDetail.setInt(3, detail.getQuantity());
                    pstmtDetail.setDouble(4, detail.getUnitPrice());
                    pstmtDetail.setDouble(5, detail.getSubtotal());
                    int detailRowsAffected = pstmtDetail.executeUpdate();
                    System.out.println("Detail saved, rows affected: " + detailRowsAffected); // Depuración

                    ResultSet rsDetail = pstmtDetail.getGeneratedKeys();
                    if (rsDetail.next()) {
                        detail.setDetailId(rsDetail.getInt(1));
                        System.out.println("Generated detail ID:" + detail.getDetailId()); // Depuración
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving purchase order:" + e.getMessage());
            throw new RuntimeException("Error saving purchase order: " + e.getMessage());
        }
    }

    @Override
    public PurchaseOrder findById(int id) {
        String sqlOrder = "SELECT * FROM purchase_orders WHERE order_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sqlOrder)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                List<PurchaseOrderDetail> details = findDetailsByOrderId(id);
                return new PurchaseOrder(
                    rs.getInt("order_id"),
                    rs.getDate("order_date"),
                    rs.getString("status"),
                    rs.getInt("supplier_id") != 0 ? rs.getInt("supplier_id") : null,
                    rs.getDouble("total_amount"),
                    rs.getTimestamp("creation_date"),
                    rs.getTimestamp("last_update_date"),
                    details
                );
            }
        } catch (SQLException e) {
            System.err.println("Error searching for purchase order: " + e.getMessage());
        }
        return null;
    }

    private List<PurchaseOrderDetail> findDetailsByOrderId(int orderId) {
        List<PurchaseOrderDetail> details = new ArrayList<>();
        String sqlDetail = "SELECT * FROM purchase_order_details WHERE order_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sqlDetail)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                details.add(new PurchaseOrderDetail(
                    rs.getInt("detail_id"),
                    rs.getInt("order_id"),
                    rs.getInt("part_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price"),
                    rs.getDouble("subtotal")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching for order details:" + e.getMessage());
        }
        return details;
    }

    @Override
    public List<PurchaseOrder> findAll() {
        List<PurchaseOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM purchase_orders";
        try (PreparedStatement pstmt = db.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                List<PurchaseOrderDetail> details = findDetailsByOrderId(orderId);
                orders.add(new PurchaseOrder(
                    orderId,
                    rs.getDate("order_date"),
                    rs.getString("status"),
                    rs.getInt("supplier_id") != 0 ? rs.getInt("supplier_id") : null,
                    rs.getDouble("total_amount"),
                    rs.getTimestamp("creation_date"),
                    rs.getTimestamp("last_update_date"),
                    details
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting purchase orders: " + e.getMessage());
        }
        return orders;
    }

    @Override
    public void update(PurchaseOrder order) {
        String sqlOrder = "UPDATE purchase_orders SET order_date = ?, status = ?, supplier_id = ?, total_amount = ?, last_update_date = ? WHERE order_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sqlOrder)) {
            pstmt.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
            pstmt.setString(2, order.getStatus());
            if (order.getSupplierId() != null) {
                pstmt.setInt(3, order.getSupplierId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setDouble(4, order.getTotalAmount());
            pstmt.setTimestamp(5, new Timestamp(order.getLastUpdateDate().getTime()));
            pstmt.setInt(6, order.getOrderId());
            pstmt.executeUpdate();

            // Actualizar detalles
            String sqlDeleteDetails = "DELETE FROM purchase_order_details WHERE order_id = ?";
            try (PreparedStatement pstmtDelete = db.prepareStatement(sqlDeleteDetails)) {
                pstmtDelete.setInt(1, order.getOrderId());
                pstmtDelete.executeUpdate();
            }
            String sqlInsertDetail = "INSERT INTO purchase_order_details (order_id, part_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmtInsert = db.prepareStatement(sqlInsertDetail, PreparedStatement.RETURN_GENERATED_KEYS)) {
                for (PurchaseOrderDetail detail : order.getDetails()) {
                    pstmtInsert.setInt(1, order.getOrderId());
                    pstmtInsert.setInt(2, detail.getPartId());
                    pstmtInsert.setInt(3, detail.getQuantity());
                    pstmtInsert.setDouble(4, detail.getUnitPrice());
                    pstmtInsert.setDouble(5, detail.getSubtotal());
                    pstmtInsert.executeUpdate();

                    ResultSet rsDetail = pstmtInsert.getGeneratedKeys();
                    if (rsDetail.next()) {
                        detail.setDetailId(rsDetail.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating purchase order: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sqlDetail = "DELETE FROM purchase_order_details WHERE order_id = ?";
        String sqlOrder = "DELETE FROM purchase_orders WHERE order_id = ?";
        try {
            // Eliminar detalles primero
            try (PreparedStatement pstmtDetail = db.prepareStatement(sqlDetail)) {
                pstmtDetail.setInt(1, id);
                pstmtDetail.executeUpdate();
            }
            // Eliminar orden
            try (PreparedStatement pstmtOrder = db.prepareStatement(sqlOrder)) {
                pstmtOrder.setInt(1, id);
                pstmtOrder.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting purchase order: " + e.getMessage());
        }
    }
}