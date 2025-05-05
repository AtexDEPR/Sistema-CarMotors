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
        supplierDAO.save(supplier);
        view.refreshTable();
    }

    public List<Supplier> getAllSupplier() {
        return supplierDAO.findAll();
    }

    public void updateSupplier(Supplier supplier) {
        supplierDAO.update(supplier);
        view.refreshTable();
    }

    public void deleteSupplier(int id) {
        supplierDAO.delete(id);
        view.refreshTable();
    }
}