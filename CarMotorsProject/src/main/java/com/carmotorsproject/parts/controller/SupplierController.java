/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;

import com.carmotorsproject.parts.model.Supplier;
import com.carmotorsproject.parts.model.SupplierDAO;
import com.carmotorsproject.parts.model.SupplierDAOInterface;
import com.carmotorsproject.parts.views.SupplierView;
import java.util.List;

public class SupplierController {
    private final SupplierDAOInterface supplierDAO;
    private final SupplierView view;

    public SupplierController(SupplierView view) {
        this.supplierDAO = new SupplierDAO();
        this.view = view;
    }

    public void addSupplier(Supplier supplier) {
        try {
            supplierDAO.save(supplier);
            view.showAlert("Supplier added successfully.");
            view.refreshTable();
        } catch (IllegalArgumentException e) {
            view.showAlert(e.getMessage());
        } catch (Exception e) {
            view.showAlert("Error adding supplier: " + e.getMessage());
        }
    }

    public List<Supplier> getAllSupplier() {
        return supplierDAO.findAll();
    }

    public void updateSupplier(Supplier supplier) {
        try {
            supplierDAO.update(supplier);
            view.showAlert("Supplier updated successfully.");
            view.refreshTable();
        } catch (Exception e) {
            view.showAlert("Error updating supplier: " + e.getMessage());
        }
    }

    public void deleteSupplier(int id) {
        try {
            supplierDAO.delete(id);
            view.showAlert("Supplier deleted successfully.");
            view.refreshTable();
        } catch (Exception e) {
            view.showAlert("Error deleting supplier: " + e.getMessage());
        }
    }

    public List<Supplier> searchSupplier(String searchTerm) {
        return supplierDAO.searchByName(searchTerm);
    }
}