/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.invoices.model;

import java.util.List;

/**
 *
 * @author ADMiN
 */
public interface InvoiceDAOInterface {
        void save(Invoice invoice);
    Invoice findById(int id);
    List<Invoice> findAll();
    void delete(int id);
}
