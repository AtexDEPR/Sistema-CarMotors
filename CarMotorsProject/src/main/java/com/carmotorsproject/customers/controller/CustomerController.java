/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.customers.controller;

import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.customers.model.CustomerDAO;
import java.util.List;

public class CustomerController {
    private CustomerDAO customerDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
    }

    public void addCustomer(Customer customer) {
        customerDAO.save(customer);
    }

    public void updateCustomer(Customer customer) {
        customerDAO.update(customer);
    }

    public void deleteCustomer(int customerId) {
        customerDAO.delete(customerId);
    }

    public Customer findById(int customerId) {
        return customerDAO.findById(customerId);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }
}