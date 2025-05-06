/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.controller;

import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PartDAO;
import com.carmotorsproject.services.model.PartsInService;
import com.carmotorsproject.services.model.Service;
import com.carmotorsproject.services.model.ServiceDAO;
import com.carmotorsproject.services.model.Technician;
import com.carmotorsproject.services.model.TechnicianDAO;
import com.carmotorsproject.services.model.Vehicle;
import com.carmotorsproject.services.model.VehicleDAO;
import com.carmotorsproject.services.views.ServiceView;
import java.util.Date;
import java.util.List;

public class ServiceController {
    private ServiceDAO serviceDAO;
    private TechnicianDAO technicianDAO;
    private VehicleDAO vehicleDAO;
    private PartDAO partDAO;
    private ServiceView view;

    public ServiceController(ServiceView view) {
        this.serviceDAO = new ServiceDAO();
        this.technicianDAO = new TechnicianDAO();
        this.vehicleDAO = new VehicleDAO();
        this.partDAO = new PartDAO();
        this.view = view;
    }

    public void addService(String maintenanceType, int vehicleId, Integer mileage, String description, 
                          String initialDiagnosis, Double estimatedTime, Double laborCost, String status, 
                          Date startDate, Date endDate, Date warrantyUntil, List<Integer> technicianIds, 
                          List<PartsInService> partsInService) {
        Service service = new Service(0, maintenanceType, vehicleId, mileage, description, 
                initialDiagnosis, null, estimatedTime, laborCost, status, startDate, endDate, 
                warrantyUntil, technicianIds, partsInService);
        serviceDAO.save(service);
        view.refreshTable();
        view.showAlert("Servicio registrado exitosamente.");
    }

    public void updateService(int serviceId, String maintenanceType, int vehicleId, Integer mileage, String description, 
                             String initialDiagnosis, String finalObservations, Double estimatedTime, Double laborCost, 
                             String status, Date startDate, Date endDate, Date warrantyUntil, List<Integer> technicianIds, 
                             List<PartsInService> partsInService) {
        Service service = serviceDAO.findById(serviceId);
        if (service != null) {
            service.setMaintenanceType(maintenanceType);
            service.setVehicleId(vehicleId);
            service.setMileage(mileage);
            service.setDescription(description);
            service.setInitialDiagnosis(initialDiagnosis);
            service.setFinalObservations(finalObservations);
            service.setEstimatedTime(estimatedTime);
            service.setLaborCost(laborCost);
            service.setStatus(status);
            service.setStartDate(startDate);
            service.setEndDate(endDate);
            service.setWarrantyUntil(warrantyUntil);
            service.setTechnicianIds(technicianIds);
            service.setPartsInService(partsInService);
            serviceDAO.update(service);
            view.refreshTable();
            view.showAlert("Servicio actualizado exitosamente.");
        } else {
            view.showAlert("Servicio no encontrado.");
        }
    }

    public void deleteService(int serviceId) {
        Service service = serviceDAO.findById(serviceId);
        if (service != null) {
            serviceDAO.delete(serviceId);
            view.refreshTable();
            view.showAlert("Servicio eliminado exitosamente.");
        } else {
            view.showAlert("Servicio no encontrado.");
        }
    }

    public List<Service> getAllServices() {
        return serviceDAO.findAll();
    }

    public List<Technician> getAllTechnicians() {
        return technicianDAO.findAll();
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.findAll();
    }

    public List<Part> getAllParts() {
        return partDAO.findAll();
    }

    public Service getServiceById(int serviceId) {
        return serviceDAO.findById(serviceId);
    }
}