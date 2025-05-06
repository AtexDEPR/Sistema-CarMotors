/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import java.util.List;

/**
 *
 * @author camper
 */
public interface TechnicianDAOInterface {
  void save(Technician technician);
    Technician findById(int id);
    List<Technician> findAll();
    void update(Technician technician);
    void delete(int id);
}
