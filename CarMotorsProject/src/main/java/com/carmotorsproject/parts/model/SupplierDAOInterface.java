/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import com.carmotorsproject.customers.model.Customer;
import java.util.List;

/**
 *
 * @author camper
 */ 
public interface SupplierDAOInterface {
    void save(Supplier supplier);
    Supplier findById(int id);
    List<Supplier> findAll();
    void update(Supplier supplier);
    void delete(int id);
     List<Supplier> searchByName(String name);
}
