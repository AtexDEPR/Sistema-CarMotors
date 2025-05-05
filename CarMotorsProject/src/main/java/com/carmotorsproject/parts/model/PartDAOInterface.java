/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;
import java.util.List;
/**
 *
 * @author camper
 */
 public interface PartDAOInterface {
    void save(Part part);
    Part findById(int id);
    List<Part> findAll();
    void update(Part part);
    void delete(int id);
    boolean checkExpiration(int id);
    void updateStock(int id, int quantity);
    void recordPartUsage(int serviceId, int partId, int quantityUsed, double unitPrice);
}
