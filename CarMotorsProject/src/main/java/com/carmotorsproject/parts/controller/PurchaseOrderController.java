/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;

import com.carmotorsproject.parts.model.PartDAO;
import com.carmotorsproject.parts.model.PurchaseOrder;
import com.carmotorsproject.parts.model.PurchaseOrderDAO;
import com.carmotorsproject.parts.model.PurchaseOrderDetail;
import com.carmotorsproject.parts.views.PurchaseOrderView;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderController {
    private PurchaseOrderView view;
    private PurchaseOrderDAO dao;
    private PartDAO partDao;

    public PurchaseOrderController(PurchaseOrderView view) {
        this.view = view;
        this.dao = new PurchaseOrderDAO();
        this.partDao = new PartDAO();
    }

    public void addPurchaseOrder(PurchaseOrder order) {
        try {
            dao.save(order);
            view.refreshTable(getAllPurchaseOrders());
            view.showAlert("Purchase order added successfully");
        } catch (Exception e) {
            view.showAlert("Error adding purchase order: " + e.getMessage());
        }
    }

    public void updatePurchaseOrder(PurchaseOrder order) {
        try {
            dao.update(order);
            view.refreshTable(getAllPurchaseOrders());
            view.showAlert("Purchase order updated successfully");
            if ("Received".equalsIgnoreCase(order.getStatus())) {
                receiveOrder(order);
            }
        } catch (Exception e) {
            view.showAlert("Error updating purchase order: " + e.getMessage());
        }
    }

    public void deletePurchaseOrder(int id) {
        try {
            dao.delete(id);
            view.refreshTable(getAllPurchaseOrders());
            view.showAlert("Purchase order deleted successfully");
        } catch (Exception e) {
            view.showAlert("Error deleting purchase order: " + e.getMessage());
        }
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            view.showAlert("Error fetching purchase orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void receiveOrder(PurchaseOrder order) {
        try {
            for (PurchaseOrderDetail detail : order.getDetails()) {
                partDao.updateStock(detail.getPartId(), detail.getQuantityOrdered());
            }
            view.showAlert("Stock updated for received order.");
        } catch (Exception e) {
            view.showAlert("Error updating stock: " + e.getMessage());
        }
    }
}