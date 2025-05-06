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
public interface  PurchaseOrderDAOInterface {
   void save(PurchaseOrder order);
    PurchaseOrder findById(int id);
    List<PurchaseOrder> findAll();
    void update(PurchaseOrder order);
    void delete(int id);
}
