package com.carmotors;


import com.carmotorsproject.campaigns.views.CampaignReportView;
import com.carmotorsproject.customers.controller.CustomerController;
import com.carmotorsproject.customers.views.CustomerView;
import com.carmotorsproject.customers.views.LoyaltyProgramView;
import com.carmotorsproject.invoices.controller.InvoiceController;
import com.carmotorsproject.invoices.views.InvoicePreviewView;
import com.carmotorsproject.invoices.views.InvoiceView;
import com.carmotorsproject.parts.views.PartView;
import com.carmotorsproject.parts.views.PurchaseOrderView;
import com.carmotorsproject.parts.views.SupplierEvaluationView;
import com.carmotorsproject.parts.views.SupplierView;
import com.carmotorsproject.services.controller.TechnicianController;
import com.carmotorsproject.services.controller.VehicleController;
import com.carmotorsproject.services.views.DeliveryOrderView;
import com.carmotorsproject.services.views.ServiceReportView;
import com.carmotorsproject.services.views.ServiceView;
import com.carmotorsproject.services.views.TechnicianView;
import com.carmotorsproject.services.views.VehicleView;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    public Main() {
        setTitle("Car Motors Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear barra de menú
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menú Gestión de Inventarios
        JMenu inventoryMenu = new JMenu("Gestión de Inventarios");
        JMenuItem partItem = new JMenuItem("Partes");
        JMenuItem supplierItem = new JMenuItem("Proveedores");
        inventoryMenu.add(partItem);
        inventoryMenu.add(supplierItem);
        menuBar.add(inventoryMenu);

        // Menú Mantenimiento y Reparaciones
        JMenu maintenanceMenu = new JMenu("Mantenimiento y Reparaciones");
        JMenuItem serviceItem = new JMenuItem("Servicios");
        JMenuItem vehicleItem = new JMenuItem("Vehículos");
        JMenuItem technicianItem = new JMenuItem("Técnicos");
        JMenuItem deliveryItem = new JMenuItem("Órdenes de Entrega");
        JMenuItem serviceReportItem = new JMenuItem("Reporte de Servicios");
        maintenanceMenu.add(serviceItem);
        maintenanceMenu.add(vehicleItem);
        maintenanceMenu.add(technicianItem);
        maintenanceMenu.add(deliveryItem);
        maintenanceMenu.add(serviceReportItem);
        menuBar.add(maintenanceMenu);

        // Menú Clientes y Facturación
        JMenu customerMenu = new JMenu("Clientes y Facturación");
        JMenuItem customerItem = new JMenuItem("Clientes");
        JMenuItem loyaltyItem = new JMenuItem("Programa de Lealtad");
        JMenuItem invoiceItem = new JMenuItem("Facturas");
        JMenuItem previewItem = new JMenuItem("Vista Previa de Facturas");
        customerMenu.add(customerItem);
        customerMenu.add(loyaltyItem);
        customerMenu.add(invoiceItem);
        customerMenu.add(previewItem);
        menuBar.add(customerMenu);

        // Menú Proveedores y Compras
        JMenu supplierMenu = new JMenu("Proveedores y Compras");
        JMenuItem purchaseItem = new JMenuItem("Órdenes de Compra");
        JMenuItem evalItem = new JMenuItem("Evaluación de Proveedores");
        supplierMenu.add(purchaseItem);
        supplierMenu.add(evalItem);
        menuBar.add(supplierMenu);

        // Menú Reportes y Estadísticas
        JMenu reportMenu = new JMenu("Reportes y Estadísticas");
        JMenuItem campaignReportItem = new JMenuItem("Reporte de Campañas");
        reportMenu.add(campaignReportItem);
        reportMenu.add(serviceReportItem);
        menuBar.add(reportMenu);

        // Acciones de los menús
        partItem.addActionListener(e -> new PartView().setVisible(true));
        supplierItem.addActionListener(e -> new SupplierView().setVisible(true));
        purchaseItem.addActionListener(e -> new PurchaseOrderView().setVisible(true));
        evalItem.addActionListener(e -> new SupplierEvaluationView().setVisible(true));
        serviceItem.addActionListener(e -> new ServiceView().setVisible(true));
        vehicleItem.addActionListener(e -> {
            VehicleController controller = new VehicleController();
            new VehicleView(controller).setVisible(true);
        });
        technicianItem.addActionListener(e -> {
            TechnicianController controller = new TechnicianController();
            new TechnicianView(controller).setVisible(true);
        });
        deliveryItem.addActionListener(e -> new DeliveryOrderView().setVisible(true));
        serviceReportItem.addActionListener(e -> new ServiceReportView().setVisible(true));
        customerItem.addActionListener(e -> {
            CustomerController controller = new CustomerController();
            new CustomerView(controller).setVisible(true);
        });
        loyaltyItem.addActionListener(e -> new LoyaltyProgramView().setVisible(true));
        invoiceItem.addActionListener(e -> {
            InvoiceController controller = new InvoiceController();
            new InvoiceView(controller).setVisible(true);
        });
        previewItem.addActionListener(e -> new InvoicePreviewView().setVisible(true));
        campaignReportItem.addActionListener(e -> new CampaignReportView().setVisible(true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}