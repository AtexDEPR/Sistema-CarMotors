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
        try {
            dao.save(part);
            view.refreshTable();
            view.showAlert("Repuesto agregado exitosamente.");
        } catch (Exception e) {
            view.showAlert("Error al agregar repuesto: " + e.getMessage());
        }
    }

    public List<Part> getAllParts() {
        return dao.findAll();
    }

    public void updatePart(Part part) {
        try {
            dao.update(part);
            view.refreshTable();
            view.showAlert("Repuesto actualizado exitosamente.");
        } catch (Exception e) {
            view.showAlert("Error al actualizar repuesto: " + e.getMessage());
        }
    }

    public void deletePart(int id) {
        try {
            dao.delete(id);
            view.refreshTable();
            view.showAlert("Repuesto eliminado exitosamente.");
        } catch (Exception e) {
            view.showAlert("Error al eliminar repuesto: " + e.getMessage());
        }
    }

    public List<Part> searchPart(String searchTerm) {
        return dao.searchByName(searchTerm);
    }

    public void recordPartUsage(int serviceId, int partId, int quantityUsed, double unitPrice) {
        try {
            dao.recordPartUsage(serviceId, partId, quantityUsed, unitPrice);
            view.refreshTable();
        } catch (Exception e) {
            view.showAlert("Error registering spare usage: " + e.getMessage());
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