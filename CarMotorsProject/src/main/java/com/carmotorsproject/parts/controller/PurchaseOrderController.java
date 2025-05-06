/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;

import com.carmotorsproject.parts.model.PartDAO;
import com.carmotorsproject.parts.model.PurchaseOrder;
import com.carmotorsproject.parts.model.PurchaseOrderDAO;
import com.carmotorsproject.parts.model.PurchaseOrderDAOInterface;
import com.carmotorsproject.parts.model.PurchaseOrderDetail;
import com.carmotorsproject.parts.views.PurchaseOrderView;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderController {
    private final PurchaseOrderDAOInterface dao;
    private final PurchaseOrderView view;

    public PurchaseOrderController(PurchaseOrderView view) {
        this.dao = new PurchaseOrderDAO();
        this.view = view;
    }

    public void addPurchaseOrder(PurchaseOrder order) {
        try {
            dao.save(order);
            view.refreshTable();
            view.showAlert("Purchase order created successfully.");
        } catch (Exception e) {
            view.showAlert("Error al crear orden de compra: " + e.getMessage());
        }
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return dao.findAll();
    }

    public void updatePurchaseOrder(PurchaseOrder order) {
        try {
            dao.update(order);
            view.refreshTable();
            view.showAlert("Purchase order updated successfully.");
        } catch (Exception e) {
            view.showAlert("Error updating purchase order: " + e.getMessage());
        }
    }

    public void deletePurchaseOrder(int id) {
        try {
            dao.delete(id);
            view.refreshTable();
            view.showAlert("Purchase order successfully deleted.");
        } catch (Exception e) {
            view.showAlert("Error deleting purchase order: " + e.getMessage());
        }
    }
}