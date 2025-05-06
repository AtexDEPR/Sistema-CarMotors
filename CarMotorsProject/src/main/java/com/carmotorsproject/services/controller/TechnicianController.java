/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.controller;

import com.carmotorsproject.services.model.Technician;
import com.carmotorsproject.services.model.TechnicianDAO;
import java.util.List;

public class TechnicianController {
    private TechnicianDAO technicianDAO;

    public TechnicianController() {
        this.technicianDAO = new TechnicianDAO();
    }

    public void addTechnician(Technician technician) {
        technicianDAO.save(technician);
    }

    public void updateTechnician(Technician technician) {
        technicianDAO.update(technician);
    }

    public void deleteTechnician(int technicianId) {
        technicianDAO.delete(technicianId);
    }

    public Technician findById(int technicianId) {
        return technicianDAO.findById(technicianId);
    }

    public List<Technician> getAllTechnicians() {
        return technicianDAO.findAll();
    }
}