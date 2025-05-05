/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;

import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PartDAO;
import com.carmotorsproject.parts.model.PartDAOInterface;
import com.carmotorsproject.parts.views.PartView;
import java.util.List;

public class PartController {
    private final PartDAOInterface dao;
    private final PartView view;

    public PartController(PartView view) {
        this.dao = new PartDAO();
        this.view = view;
    }

    public void addPart(Part part) {
        dao.save(part);
        view.refreshTable();
    }

    public List<Part> getAllParts() {
        return dao.findAll();
    }

    public void updatePart(Part part) {
        dao.update(part);
        view.refreshTable();
    }

    public void deletePart(int id) {
        dao.delete(id);
        view.refreshTable();
    }

    public void recordPartUsage(int serviceId, int partId, int quantityUsed, double unitPrice) {
        try {
            dao.recordPartUsage(serviceId, partId, quantityUsed, unitPrice);
            view.refreshTable();
        } catch (Exception e) {
            view.showAlert("Error recording part usage: " + e.getMessage());
        }
    }

    public void adjustStock(int partId, int quantity) {
        try {
            dao.updateStock(partId, quantity);
            view.refreshTable();
        } catch (Exception e) {
            view.showAlert("Error adjusting stock: " + e.getMessage());
        }
    }
}