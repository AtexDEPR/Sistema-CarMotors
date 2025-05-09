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
public interface ServiceDAOInterface {
   void save(Service service);
    Service findById(int id);
    List<Service> findAll();
    void update(Service service);
    void delete(int id);
}
