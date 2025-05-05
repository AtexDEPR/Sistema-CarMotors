/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;

import com.carmotorsproject.parts.model.PurchaseOrderDAO;
import com.carmotorsproject.parts.model.PartDAO;
import com.carmotorsproject.parts.model.SupplierDAO;
import com.carmotorsproject.parts.model.DAOFactory;
import com.carmotorsproject.parts.model.PurchaseOrder;
import com.carmotorsproject.parts.model.PurchaseOrderDetail;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.Supplier;
import com.carmotorsproject.parts.views.PurchaseOrderView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class that mediates between PurchaseOrderView and PurchaseOrderDAO.
 * Handles user actions from the view and updates the model and view accordingly.
 */
public class PurchaseOrderController {

    private static final Logger LOGGER = Logger.getLogger(PurchaseOrderController.class.getName());

    private final PurchaseOrderView view;
    private final PurchaseOrderDAO orderDAO;
    private final PartDAO partDAO;
    private final SupplierDAO supplierDAO;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The purchase order view
     */
    public PurchaseOrderController(PurchaseOrderView view) {
        this.view = view;
        this.orderDAO = DAOFactory.getPurchaseOrderDAO();
        this.partDAO = DAOFactory.getPartDAO();
        this.supplierDAO = DAOFactory.getSupplierDAO();
        LOGGER.log(Level.INFO, "PurchaseOrderController initialized");
    }

    /**
     * Loads all purchase orders from the database and updates the view.
     */
    public void loadAllOrders() {
        List<PurchaseOrder> orders = null;
        try {
            orders = orderDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        view.updateOrdersTable(orders);
        LOGGER.log(Level.INFO, "Loaded {0} purchase orders", orders.size());
    }

    /**
     * Loads a specific purchase order by ID.
     *
     * @param orderId The ID of the purchase order to load
     */
    public void loadOrder(int orderId) {
        try {
            PurchaseOrder order = orderDAO.findById(orderId);
            if (order != null) {
                // Load order details
                List<PurchaseOrderDetail> details = orderDAO.findDetailsByOrderId(orderId);
                order.setDetails(details);

                // Update view
                view.loadOrderToForm(order);
                LOGGER.log(Level.INFO, "Loaded order ID: {0}", orderId);
            } else {
                view.showError("Order not found with ID: " + orderId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading order", e);
            view.showError("Error loading order: " + e.getMessage());
        }
    }

    /**
     * Loads order details for a specific purchase order.
     *
     * @param orderId The ID of the purchase order
     */
    public void loadOrderDetails(int orderId) {
        try {
            List<PurchaseOrderDetail> details = orderDAO.findDetailsByOrderId(orderId);
            view.updateDetailTable(details);
            LOGGER.log(Level.INFO, "Loaded {0} details for order ID {1}", new Object[]{details.size(), orderId});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading order details", e);
            view.showError("Error loading order details: " + e.getMessage());
        }
    }

    /**
     * Creates a new purchase order.
     *
     * @param order The purchase order to create
     */
    public void createOrder(PurchaseOrder order) {
        try {
            // Validate supplier
            if (order.getSupplierId() <= 0) {
                view.showError("Invalid supplier ID.");
                return;
            }

            // Validate dates
            if (order.getOrderDate() == null) {
                view.showError("Order date is required.");
                return;
            }

            if (order.getExpectedDate() != null && order.getExpectedDate().before(order.getOrderDate())) {
                view.showError("Expected delivery date cannot be before order date.");
                return;
            }

            // Save order to database
            PurchaseOrder savedOrder = orderDAO.save(order);

            // Update view
            view.clearForm();
            loadAllOrders();
            view.selectOrder(savedOrder.getId());
            view.showInfo("Purchase order created successfully.");
            LOGGER.log(Level.INFO, "Purchase order created: ID {0}", savedOrder.getId());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating purchase order", e);
            view.showError("Error creating purchase order: " + e.getMessage());
        }
    }

    /**
     * Updates an existing purchase order.
     *
     * @param order The purchase order to update
     */
    public void updateOrder(PurchaseOrder order) {
        try {
            // Validate order ID
            if (order.getId() <= 0) {
                view.showError("Invalid order ID.");
                return;
            }

            // Validate supplier
            if (order.getSupplierId() <= 0) {
                view.showError("Invalid supplier ID.");
                return;
            }

            // Validate dates
            if (order.getOrderDate() == null) {
                view.showError("Order date is required.");
                return;
            }

            if (order.getExpectedDate() != null && order.getExpectedDate().before(order.getOrderDate())) {
                view.showError("Expected delivery date cannot be before order date.");
                return;
            }

            // Update order in database
            PurchaseOrder updatedOrder = orderDAO.update(order);

            // Update view
            loadAllOrders();
            view.selectOrder(updatedOrder.getId());
            view.showInfo("Purchase order updated successfully.");
            LOGGER.log(Level.INFO, "Purchase order updated: ID {0}", updatedOrder.getId());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating purchase order", e);
            view.showError("Error updating purchase order: " + e.getMessage());
        }
    }

    /**
     * Cancels a purchase order.
     *
     * @param orderId The ID of the order to cancel
     */
    public void cancelOrder(int orderId) {
        try {
            // Validate order ID
            PurchaseOrder order = orderDAO.findById(orderId);
            if (order == null) {
                view.showError("Invalid order ID.");
                return;
            }

            // Check if order can be canceled
            if ("CANCELADA".equals(order.getStatus()) || "RECIBIDA".equals(order.getStatus())) {
                view.showError("Cannot cancel an order that is already canceled or received.");
                return;
            }

            // Update order status
            order.setStatus("CANCELADA");
            PurchaseOrder updatedOrder = orderDAO.update(order);

            // Update view
            loadAllOrders();
            view.showInfo("Purchase order canceled successfully.");
            LOGGER.log(Level.INFO, "Purchase order canceled: ID {0}", orderId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error canceling purchase order", e);
            view.showError("Error canceling purchase order: " + e.getMessage());
        }
    }

    /**
     * Searches for parts by name or reference.
     *
     * @param searchTerm The search term
     */
    public void searchParts(String searchTerm) {
        try {
            List<Part> parts = new ArrayList<>();

            // Try to find by name
            parts.addAll(partDAO.findByName(searchTerm));

            // Try to find by reference
            Part partByRef = partDAO.findByReference(searchTerm);
            if (partByRef != null && !parts.contains(partByRef)) {
                parts.add(partByRef);
            }

            view.updatePartCombo(parts);
            LOGGER.log(Level.INFO, "Found {0} parts matching search term: {1}",
                    new Object[]{parts.size(), searchTerm});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching parts", e);
            view.showError("Error searching parts: " + e.getMessage());
        }
    }

    /**
     * Deletes a purchase order from the database.
     *
     * @param status Filter by status
     * @param supplierId Filter by supplier ID
     */
    public void filterOrders(String status, Integer supplierId) {
        try {
            List<PurchaseOrder> orders;

            if (status == null && supplierId == null) {
                // No filters, load all
                orders = orderDAO.findAll();
            } else if (status != null && supplierId == null) {
                // Filter by status only
                orders = orderDAO.findByStatus(status);
            } else if (status == null && supplierId != null) {
                // Filter by supplier only
                orders = orderDAO.findBySupplier(supplierId);
            } else {
                // Filter by both
                orders = orderDAO.findByStatusAndSupplier(status, supplierId);
            }

            view.updateOrdersTable(orders);
            LOGGER.log(Level.INFO, "Filtered orders: found {0} results", orders.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error filtering orders", e);
            view.showError("Error filtering orders: " + e.getMessage());
        }
    }

    /**
     * Deletes a purchase order from the database.
     *
     * @param status Filter by status
     * @param supplierId Filter by supplier ID
     */
    public void deleteOrder(String status, Integer supplierId) {
        // This method should actually call filterOrders since it's filtering, not deleting
        filterOrders(status, supplierId);
    }


    /**
     * Loads all suppliers for the supplier combo box.
     */
    public void loadSuppliers() {
        try {
            List<Supplier> suppliers = supplierDAO.findAll();
            view.updateSupplierCombo(suppliers);
            LOGGER.log(Level.INFO, "Loaded {0} suppliers", suppliers.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading suppliers", e);
            view.showError("Error loading suppliers: " + e.getMessage());
        }
    }



    /**
     * Loads all parts for the part combo box.
     */
    public void loadParts() {
        List<Part> parts = partDAO.findAll();
        view.updatePartCombo(parts);
        LOGGER.log(Level.INFO, "Loaded {0} parts", parts.size());
    }

    /**
     * Loads parts for a specific supplier.
     *
     * @param supplierId The ID of the supplier
     */
    public void loadPartsBySupplier(int supplierId) {
        List<Part> parts = partDAO.findBySupplier(supplierId);
        view.updatePartCombo(parts);
        LOGGER.log(Level.INFO, "Loaded {0} parts for supplier ID {1}",
                new Object[]{parts.size(), supplierId});
    }
}