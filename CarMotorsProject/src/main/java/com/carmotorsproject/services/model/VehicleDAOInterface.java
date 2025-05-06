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
public interface VehicleDAOInterface {
        void save(Vehicle vehicle);
    Vehicle findById(int id);
    List<Vehicle> findAll();
    void update(Vehicle vehicle);
    void delete(int id);
}
