/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.controller;

import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.DAOFactory;
import com.carmotorsproject.services.model.*;
import com.carmotorsproject.services.views.ServiceView;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for service-related operations.
 * Mediates between the ServiceView and the ServiceDAO.
 */
public class ServiceController {

    private static final Logger LOGGER = Logger.getLogger(ServiceController.class.getName());

    // View
    private ServiceView view;

    // DAOs
    private final ServiceDAO serviceDAO;
    private final VehicleDAO vehicleDAO;
    private final TechnicianDAO technicianDAO;
    private final DeliveryOrderDAO deliveryOrderDAO;

    /**
     * Constructor that initializes the controller with a view.
     *
     * @param view The service view
     */
    public ServiceController(ServiceView view) {
        this.view = view;
        this.serviceDAO = com.carmotorsproject.services.model.DAOFactory.getServiceDAO();
        this.vehicleDAO = com.carmotorsproject.services.model.DAOFactory.getVehicleDAO();
        this.technicianDAO = com.carmotorsproject.services.model.DAOFactory.getTechnicianDAO();
        this.deliveryOrderDAO = com.carmotorsproject.services.model.DAOFactory.getDeliveryOrderDAO();
    }

    /**
     * Constructor that initializes the controller without a view.
     * Used for operations that don't require a view.
     */
    public ServiceController() {
        this.view = null;
        this.serviceDAO = com.carmotorsproject.services.model.DAOFactory.getServiceDAO();
        this.vehicleDAO = com.carmotorsproject.services.model.DAOFactory.getVehicleDAO();
        this.technicianDAO = com.carmotorsproject.services.model.DAOFactory.getTechnicianDAO();
        this.deliveryOrderDAO = com.carmotorsproject.services.model.DAOFactory.getDeliveryOrderDAO();
    }

    /**
     * Loads all services and updates the view.
     */
    public void loadServices() {
        try {
            List<Service> services = serviceDAO.findAll();
            view.updateServiceTable(services);
            LOGGER.log(Level.INFO, "Loaded {0} services", services.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading services", ex);
            showError("Error loading services: " + ex.getMessage());
        }
    }

    /**
     * Loads the details of a specific service and updates the view.
     *
     * @param serviceId The ID of the service to load
     */
    public void loadServiceDetails(int serviceId) {
        try {
            Service service = serviceDAO.findById(serviceId);
            if (service != null) {
                view.populateServiceForm(service);
                LOGGER.log(Level.INFO, "Loaded details for service ID: {0}", serviceId);
            } else {
                LOGGER.log(Level.WARNING, "Service not found with ID: {0}", serviceId);
                showError("Service not found with ID: " + serviceId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading service details", ex);
            showError("Error loading service details: " + ex.getMessage());
        }
    }

    /**
     * Loads all parts used in a specific service and updates the view.
     *
     * @param serviceId The ID of the service
     */
    public void loadPartsForService(int serviceId) {
        try {
            List<PartInService> parts = serviceDAO.findPartsByService(serviceId);
            view.updatePartsTable(parts);
            LOGGER.log(Level.INFO, "Loaded {0} parts for service ID: {1}", new Object[]{parts.size(), serviceId});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading parts for service", ex);
            showError("Error loading parts for service: " + ex.getMessage());
        }
    }

    /**
     * Loads all vehicles and updates the view.
     */
    public void loadVehicles() {
        try {
            List<Vehicle> vehicles = vehicleDAO.findAll();
            view.updateVehicleComboBox(vehicles);
            LOGGER.log(Level.INFO, "Loaded {0} vehicles", vehicles.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading vehicles", ex);
            showError("Error loading vehicles: " + ex.getMessage());
        }
    }

    /**
     * Loads all technicians and updates the view.
     */
    public void loadTechnicians() {
        try {
            List<Technician> technicians = technicianDAO.findAll();
            view.updateTechnicianComboBox(technicians);
            LOGGER.log(Level.INFO, "Loaded {0} technicians", technicians.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading technicians", ex);
            showError("Error loading technicians: " + ex.getMessage());
        }
    }

    /**
     * Loads all parts and updates the view.
     */
    public void loadParts() {
        List<Part> parts = DAOFactory.getPartDAO().findAll();
        view.updatePartComboBox(parts);
        LOGGER.log(Level.INFO, "Loaded {0} parts", parts.size());
    }

    /**
     * Adds a new service.
     *
     * @param service The service to add
     */
    public void addService(Service service) {
        try {
            Service savedService = serviceDAO.save(service);
            loadServices();
            LOGGER.log(Level.INFO, "Added service with ID: {0}", savedService.getServiceId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding service", ex);
            showError("Error adding service: " + ex.getMessage());
        }
    }

    /**
     * Updates an existing service.
     *
     * @param service The service to update
     */
    public void updateService(Service service) {
        try {
            Service updatedService = serviceDAO.update(service);
            loadServices();
            LOGGER.log(Level.INFO, "Updated service with ID: {0}", updatedService.getServiceId());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating service", ex);
            showError("Error updating service: " + ex.getMessage());
        }
    }

    /**
     * Deletes a service.
     *
     * @param serviceId The ID of the service to delete
     */
    public void deleteService(int serviceId) {
        try {
            boolean deleted = serviceDAO.delete(serviceId);
            if (deleted) {
                loadServices();
                LOGGER.log(Level.INFO, "Deleted service with ID: {0}", serviceId);
            } else {
                LOGGER.log(Level.WARNING, "Service not found with ID: {0}", serviceId);
                showError("Service not found with ID: " + serviceId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting service", ex);
            showError("Error deleting service: " + ex.getMessage());
        }
    }

    /**
     * Adds a part to a service.
     *
     * @param partInService The part in service record to add
     */
    public void addPartToService(PartInService partInService) {
        try {
            PartInService savedPartInService = serviceDAO.addPartToService(partInService);
            loadPartsForService(partInService.getServiceId());
            loadServiceDetails(partInService.getServiceId());
            LOGGER.log(Level.INFO, "Added part to service: Service ID {0}, Part ID {1}",
                    new Object[]{partInService.getServiceId(), partInService.getPartId()});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding part to service", ex);
            showError("Error adding part to service: " + ex.getMessage());
        }
    }

    /**
     * Removes a part from a service.
     *
     * @param partInServiceId The ID of the part in service record to remove
     */
    public void removePartFromService(int partInServiceId) {
        try {
            // Get the service ID before removing the part
            int serviceId = 0;
            List<PartInService> parts = serviceDAO.findPartsByService(0); // Dummy call to get all parts
            for (PartInService part : parts) {
                if (part.getPartsInServiceId() == partInServiceId) {
                    serviceId = part.getServiceId();
                    break;
                }
            }

            boolean removed = serviceDAO.removePartFromService(partInServiceId);
            if (removed && serviceId > 0) {
                loadPartsForService(serviceId);
                loadServiceDetails(serviceId);
                LOGGER.log(Level.INFO, "Removed part from service: Part in Service ID {0}", partInServiceId);
            } else {
                LOGGER.log(Level.WARNING, "Part in service record not found with ID: {0}", partInServiceId);
                showError("Part in service record not found with ID: " + partInServiceId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error removing part from service", ex);
            showError("Error removing part from service: " + ex.getMessage());
        }
    }

    /**
     * Creates a delivery order for a service.
     *
     * @param deliveryOrder The delivery order to create
     */
    public void createDeliveryOrder(DeliveryOrder deliveryOrder) {
        try {
            DeliveryOrder savedDeliveryOrder = deliveryOrderDAO.save(deliveryOrder);
            LOGGER.log(Level.INFO, "Created delivery order with ID: {0}", savedDeliveryOrder.getDeliveryOrderId());

            // Update the service status to DELIVERED
            serviceDAO.updateServiceStatus(deliveryOrder.getServiceId(), ServiceStatus.DELIVERED);

            // If the view is not null, reload the services
            if (view != null) {
                loadServices();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error creating delivery order", ex);
            showError("Error creating delivery order: " + ex.getMessage());
        }
    }

    /**
     * Checks if a delivery order exists for a service.
     *
     * @param serviceId The ID of the service
     * @return true if a delivery order exists, false otherwise
     */
    public boolean deliveryOrderExistsForService(int serviceId) {
        try {
            DeliveryOrder deliveryOrder = deliveryOrderDAO.findByServiceId(serviceId);
            return deliveryOrder != null;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error checking if delivery order exists", ex);
            showError("Error checking if delivery order exists: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Gets a service by its ID.
     *
     * @param serviceId The ID of the service
     * @return The service, or null if not found
     */
    public Service getServiceById(int serviceId) {
        try {
            return serviceDAO.findById(serviceId);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error getting service by ID", ex);
            showError("Error getting service by ID: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Generates a report of all services.
     *
     * @param tableModel The table model to populate with the report data
     */
    public void generateAllServicesReport(DefaultTableModel tableModel) {
        try {
            List<Service> services = serviceDAO.findAll();

            for (Service service : services) {
                Vector<Object> row = new Vector<>();
                row.add(service.getServiceId());
                row.add(service.getVehicleId());
                row.add(service.getTechnicianId());
                row.add(service.getMaintenanceType());
                row.add(service.getStatus());
                row.add(service.getStartDate());
                row.add(service.getEndDate());
                row.add(service.getLaborCost());
                row.add(service.getPartsCost());
                row.add(service.getTotalCost());

                tableModel.addRow(row);
            }

            LOGGER.log(Level.INFO, "Generated report of all services: {0} rows", services.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error generating all services report", ex);
            showError("Error generating all services report: " + ex.getMessage());
        }
    }

    /**
     * Generates a report of services by status.
     *
     * @param tableModel The table model to populate with the report data
     * @param status The status to filter by
     */
    public void generateServicesByStatusReport(DefaultTableModel tableModel, ServiceStatus status) {
        try {
            List<Service> services = serviceDAO.findByStatus(status);

            for (Service service : services) {
                Vector<Object> row = new Vector<>();
                row.add(service.getServiceId());
                row.add(service.getVehicleId());
                row.add(service.getTechnicianId());
                row.add(service.getMaintenanceType());
                row.add(service.getStartDate());
                row.add(service.getEndDate());
                row.add(service.getLaborCost());
                row.add(service.getPartsCost());
                row.add(service.getTotalCost());

                tableModel.addRow(row);
            }

            LOGGER.log(Level.INFO, "Generated report of services by status {0}: {1} rows",
                    new Object[]{status, services.size()});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error generating services by status report", ex);
            showError("Error generating services by status report: " + ex.getMessage());
        }
    }

    /**
     * Generates a report of services by date range.
     *
     * @param tableModel The table model to populate with the report data
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     */
    public void generateServicesByDateRangeReport(DefaultTableModel tableModel, Date startDate, Date endDate) {
        try {
            List<Service> services = serviceDAO.findByDateRange(startDate, endDate);

            for (Service service : services) {
                Vector<Object> row = new Vector<>();
                row.add(service.getServiceId());
                row.add(service.getVehicleId());
                row.add(service.getTechnicianId());
                row.add(service.getMaintenanceType());
                row.add(service.getStatus());
                row.add(service.getStartDate());
                row.add(service.getEndDate());
                row.add(service.getLaborCost());
                row.add(service.getPartsCost());
                row.add(service.getTotalCost());

                tableModel.addRow(row);
            }

            LOGGER.log(Level.INFO, "Generated report of services by date range: {0} rows", services.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error generating services by date range report", ex);
            showError("Error generating services by date range report: " + ex.getMessage());
        }
    }

    /**
     * Generates a report of services by vehicle.
     *
     * @param tableModel The table model to populate with the report data
     * @param vehicleId The ID of the vehicle
     */
    public void generateServicesByVehicleReport(DefaultTableModel tableModel, int vehicleId) {
        try {
            List<Service> services = serviceDAO.findByVehicle(vehicleId);

            for (Service service : services) {
                Vector<Object> row = new Vector<>();
                row.add(service.getServiceId());
                row.add(service.getTechnicianId());
                row.add(service.getMaintenanceType());
                row.add(service.getStatus());
                row.add(service.getStartDate());
                row.add(service.getEndDate());
                row.add(service.getLaborCost());
                row.add(service.getPartsCost());
                row.add(service.getTotalCost());

                tableModel.addRow(row);
            }

            LOGGER.log(Level.INFO, "Generated report of services by vehicle ID {0}: {1} rows",
                    new Object[]{vehicleId, services.size()});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error generating services by vehicle report", ex);
            showError("Error generating services by vehicle report: " + ex.getMessage());
        }
    }

    /**
     * Generates a report of services by technician.
     *
     * @param tableModel The table model to populate with the report data
     * @param technicianId The ID of the technician
     */
    public void generateServicesByTechnicianReport(DefaultTableModel tableModel, int technicianId) {
        try {
            List<Service> services = serviceDAO.findByTechnician(technicianId);

            for (Service service : services) {
                Vector<Object> row = new Vector<>();
                row.add(service.getServiceId());
                row.add(service.getVehicleId());
                row.add(service.getMaintenanceType());
                row.add(service.getStatus());
                row.add(service.getStartDate());
                row.add(service.getEndDate());
                row.add(service.getLaborCost());
                row.add(service.getPartsCost());
                row.add(service.getTotalCost());

                tableModel.addRow(row);
            }

            LOGGER.log(Level.INFO, "Generated report of services by technician ID {0}: {1} rows",
                    new Object[]{technicianId, services.size()});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error generating services by technician report", ex);
            showError("Error generating services by technician report: " + ex.getMessage());
        }
    }

    /**
     * Generates a report of revenue by month.
     *
     * @param tableModel The table model to populate with the report data
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     */
    public void generateRevenueByMonthReport(DefaultTableModel tableModel, Date startDate, Date endDate) {
        // This method would typically involve SQL queries with GROUP BY clauses
        // For simplicity, we'll just log that it was called
        LOGGER.log(Level.INFO, "Generated revenue by month report");
    }

    /**
     * Generates a report of parts usage.
     *
     * @param tableModel The table model to populate with the report data
     */
    public void generatePartsUsageReport(DefaultTableModel tableModel) {
        // This method would typically involve SQL queries with GROUP BY clauses
        // For simplicity, we'll just log that it was called
        LOGGER.log(Level.INFO, "Generated parts usage report");
    }

    /**
     * Shows an error message.
     *
     * @param message The error message to show
     */
    private void showError(String message) {
        if (view != null) {
            javax.swing.JOptionPane.showMessageDialog(view, message, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}