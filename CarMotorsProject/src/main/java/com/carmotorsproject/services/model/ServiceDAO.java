/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import com.carmotorsproject.config.DatabaseConnection;
import com.carmotorsproject.parts.model.PartDAO;
import com.carmotorsproject.parts.model.DAOFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of ServiceDAOInterface that provides data access operations for services.
 */
public class ServiceDAO implements ServiceDAOInterface {

    private static final Logger LOGGER = Logger.getLogger(ServiceDAO.class.getName());
    private final Connection connection;
    private final PartDAO partDAO;

    /**
     * Constructor that initializes the database connection.
     */
    public ServiceDAO() {
        // Fix: Use getInstance().getConnection() instead of static getConnection()
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.partDAO = DAOFactory.getPartDAO();
        LOGGER.log(Level.INFO, "ServiceDAO initialized with database connection");
    }

    /**
     * Saves a new service to the database.
     *
     * @param service The service to save
     * @return The saved service with its generated ID
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Service save(Service service) throws SQLException {
        String sql = "INSERT INTO services (vehicle_id, technician_id, maintenance_type, status, " +
                "start_date, description, diagnosis, labor_cost, parts_cost, total_cost, mileage, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            stmt.setInt(1, service.getVehicleId());
            stmt.setInt(2, service.getTechnicianId());
            stmt.setString(3, service.getMaintenanceType().name());
            stmt.setString(4, service.getStatus().name());
            stmt.setTimestamp(5, new Timestamp(service.getStartDate().getTime()));
            stmt.setString(6, service.getDescription());
            stmt.setString(7, service.getDiagnosis());
            stmt.setDouble(8, service.getLaborCost());
            stmt.setDouble(9, service.getPartsCost());
            stmt.setDouble(10, service.getTotalCost());
            stmt.setInt(11, service.getMileage());
            stmt.setString(12, service.getNotes());

            // Execute insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating service failed, no rows affected.");
            }

            // Get generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    service.setServiceId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating service failed, no ID obtained.");
                }
            }

            LOGGER.log(Level.INFO, "Service created with ID: {0}", service.getServiceId());
            return service;
        }
    }

    /**
     * Updates an existing service in the database.
     *
     * @param service The service to update
     * @return The updated service
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Service update(Service service) throws SQLException {
        String sql = "UPDATE services SET vehicle_id = ?, technician_id = ?, maintenance_type = ?, " +
                "status = ?, start_date = ?, end_date = ?, description = ?, diagnosis = ?, " +
                "labor_cost = ?, parts_cost = ?, total_cost = ?, mileage = ?, notes = ? " +
                "WHERE service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set parameters
            stmt.setInt(1, service.getVehicleId());
            stmt.setInt(2, service.getTechnicianId());
            stmt.setString(3, service.getMaintenanceType().name());
            stmt.setString(4, service.getStatus().name());
            stmt.setTimestamp(5, new Timestamp(service.getStartDate().getTime()));

            if (service.getEndDate() != null) {
                stmt.setTimestamp(6, new Timestamp(service.getEndDate().getTime()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            stmt.setString(7, service.getDescription());
            stmt.setString(8, service.getDiagnosis());
            stmt.setDouble(9, service.getLaborCost());
            stmt.setDouble(10, service.getPartsCost());
            stmt.setDouble(11, service.getTotalCost());
            stmt.setInt(12, service.getMileage());
            stmt.setString(13, service.getNotes());
            stmt.setInt(14, service.getServiceId());

            // Execute update
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating service failed, no rows affected.");
            }

            LOGGER.log(Level.INFO, "Service updated with ID: {0}", service.getServiceId());
            return service;
        }
    }

    /**
     * Deletes a service from the database.
     *
     * @param id The ID of the service to delete
     * @return true if the service was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        // First, delete all parts in service records
        String deletePartsSql = "DELETE FROM parts_in_service WHERE service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(deletePartsSql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        // Then, delete the service
        String deleteServiceSql = "DELETE FROM services WHERE service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(deleteServiceSql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Service deleted with ID: {0}, Rows affected: {1}",
                    new Object[]{id, affectedRows});

            return affectedRows > 0;
        }
    }

    /**
     * Finds a service by its ID.
     *
     * @param id The ID of the service to find
     * @return The service, or null if not found
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Service findById(int id) throws SQLException {
        String sql = "SELECT * FROM services WHERE service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToService(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Finds all services in the database.
     *
     * @return A list of all services
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Service> findAll() throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY start_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        }

        return services;
    }

    /**
     * Finds services for a specific vehicle.
     *
     * @param vehicleId The ID of the vehicle
     * @return A list of services for the vehicle
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Service> findByVehicle(int vehicleId) throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE vehicle_id = ? ORDER BY start_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
            }
        }

        return services;
    }

    /**
     * Finds services assigned to a specific technician.
     *
     * @param technicianId The ID of the technician
     * @return A list of services assigned to the technician
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Service> findByTechnician(int technicianId) throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE technician_id = ? ORDER BY start_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, technicianId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
            }
        }

        return services;
    }

    /**
     * Finds services with a specific status.
     *
     * @param status The status to search for
     * @return A list of services with the specified status
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Service> findByStatus(ServiceStatus status) throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE status = ? ORDER BY start_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
            }
        }

        return services;
    }

    /**
     * Finds services created within a date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of services created within the date range
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Service> findByDateRange(Date startDate, Date endDate) throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE start_date BETWEEN ? AND ? ORDER BY start_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
            }
        }

        return services;
    }

    /**
     * Adds a part to a service.
     *
     * @param partInService The part in service record to add
     * @return The added part in service record with its generated ID
     * @throws SQLException If a database access error occurs
     */
    @Override
    public PartInService addPartToService(PartInService partInService) throws SQLException {
        String sql = "INSERT INTO parts_in_service (service_id, part_id, quantity, unit_price, total_price) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            stmt.setInt(1, partInService.getServiceId());
            stmt.setInt(2, partInService.getPartId());
            stmt.setInt(3, partInService.getQuantity());
            stmt.setDouble(4, partInService.getUnitPrice());
            stmt.setDouble(5, partInService.getTotalPrice());

            // Execute insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Adding part to service failed, no rows affected.");
            }

            // Get generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    partInService.setPartsInServiceId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Adding part to service failed, no ID obtained.");
                }
            }

            // Update service parts cost
            updateServicePartsCost(partInService.getServiceId());

            // Update part stock - using the updateStock method that we'll add to PartDAO
            partDAO.updateStock(partInService.getPartId(), -partInService.getQuantity());
            LOGGER.log(Level.INFO, "Stock updated for part ID {0}: decreased by {1}",
                    new Object[]{partInService.getPartId(), partInService.getQuantity()});

            LOGGER.log(Level.INFO, "Part added to service: Service ID {0}, Part ID {1}, Quantity {2}",
                    new Object[]{partInService.getServiceId(), partInService.getPartId(), partInService.getQuantity()});

            return partInService;
        }
    }

    /**
     * Updates a part in a service.
     *
     * @param partInService The part in service record to update
     * @return The updated part in service record
     * @throws SQLException If a database access error occurs
     */
    @Override
    public PartInService updatePartInService(PartInService partInService) throws SQLException {
        // First, get the original quantity
        int originalQuantity = 0;
        String selectSql = "SELECT quantity FROM parts_in_service WHERE parts_in_service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setInt(1, partInService.getPartsInServiceId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    originalQuantity = rs.getInt("quantity");
                } else {
                    throw new SQLException("Part in service record not found.");
                }
            }
        }

        // Update the part in service record
        String updateSql = "UPDATE parts_in_service SET quantity = ?, unit_price = ?, total_price = ? " +
                "WHERE parts_in_service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setInt(1, partInService.getQuantity());
            stmt.setDouble(2, partInService.getUnitPrice());
            stmt.setDouble(3, partInService.getTotalPrice());
            stmt.setInt(4, partInService.getPartsInServiceId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating part in service failed, no rows affected.");
            }

            // Update service parts cost
            updateServicePartsCost(partInService.getServiceId());

            // Update part stock (adjust for the difference in quantity)
            int quantityDifference = originalQuantity - partInService.getQuantity();
            if (quantityDifference != 0) {
                partDAO.updateStock(partInService.getPartId(), quantityDifference);
                LOGGER.log(Level.INFO, "Stock updated for part ID {0}: {1} by {2}",
                        new Object[]{partInService.getPartId(),
                                quantityDifference > 0 ? "increased" : "decreased",
                                Math.abs(quantityDifference)});
            }

            LOGGER.log(Level.INFO, "Part in service updated: ID {0}, New Quantity {1}",
                    new Object[]{partInService.getPartsInServiceId(), partInService.getQuantity()});

            return partInService;
        }
    }

    /**
     * Removes a part from a service.
     *
     * @param partInServiceId The ID of the part in service record to remove
     * @return true if the part was removed, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean removePartFromService(int partInServiceId) throws SQLException {
        // First, get the part details for stock update
        int partId = 0;
        int quantity = 0;
        int serviceId = 0;

        String selectSql = "SELECT part_id, quantity, service_id FROM parts_in_service WHERE parts_in_service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setInt(1, partInServiceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    partId = rs.getInt("part_id");
                    quantity = rs.getInt("quantity");
                    serviceId = rs.getInt("service_id");
                } else {
                    return false; // Record not found
                }
            }
        }

        // Delete the part in service record
        String deleteSql = "DELETE FROM parts_in_service WHERE parts_in_service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setInt(1, partInServiceId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Update service parts cost
                updateServicePartsCost(serviceId);

                // Return the parts to stock
                partDAO.updateStock(partId, quantity);
                LOGGER.log(Level.INFO, "Stock updated for part ID {0}: increased by {1}",
                        new Object[]{partId, quantity});

                LOGGER.log(Level.INFO, "Part removed from service: ID {0}, Part ID {1}, Quantity {2}",
                        new Object[]{partInServiceId, partId, quantity});

                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Finds all parts used in a specific service.
     *
     * @param serviceId The ID of the service
     * @return A list of parts used in the service
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<PartInService> findPartsByService(int serviceId) throws SQLException {
        List<PartInService> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts_in_service WHERE service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, serviceId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    parts.add(mapResultSetToPartInService(rs));
                }
            }
        }

        return parts;
    }

    /**
     * Updates the status of a service.
     *
     * @param serviceId The ID of the service
     * @param status The new status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean updateServiceStatus(int serviceId, ServiceStatus status) throws SQLException {
        String sql = "UPDATE services SET status = ? WHERE service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, serviceId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // If status is COMPLETED, set the end date
                if (status == ServiceStatus.COMPLETED) {
                    String updateEndDateSql = "UPDATE services SET end_date = ? WHERE service_id = ?";

                    try (PreparedStatement endDateStmt = connection.prepareStatement(updateEndDateSql)) {
                        endDateStmt.setTimestamp(1, new Timestamp(new Date().getTime()));
                        endDateStmt.setInt(2, serviceId);
                        endDateStmt.executeUpdate();
                    }
                }

                LOGGER.log(Level.INFO, "Service status updated: ID {0}, New Status {1}",
                        new Object[]{serviceId, status});

                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Calculates the total cost of a service (labor + parts).
     *
     * @param serviceId The ID of the service
     * @return The total cost of the service
     * @throws SQLException If a database access error occurs
     */
    @Override
    public double calculateServiceCost(int serviceId) throws SQLException {
        String sql = "SELECT labor_cost, parts_cost FROM services WHERE service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, serviceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double laborCost = rs.getDouble("labor_cost");
                    double partsCost = rs.getDouble("parts_cost");
                    return laborCost + partsCost;
                } else {
                    throw new SQLException("Service not found with ID: " + serviceId);
                }
            }
        }
    }

    /**
     * Updates the parts cost of a service based on the sum of all parts used.
     *
     * @param serviceId The ID of the service
     * @throws SQLException If a database access error occurs
     */
    private void updateServicePartsCost(int serviceId) throws SQLException {
        // Calculate the sum of all parts costs
        String sumSql = "SELECT SUM(total_price) AS total_parts_cost FROM parts_in_service WHERE service_id = ?";

        double partsCost = 0.0;

        try (PreparedStatement stmt = connection.prepareStatement(sumSql)) {
            stmt.setInt(1, serviceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    partsCost = rs.getDouble("total_parts_cost");
                }
            }
        }

        // Update the service with the new parts cost
        String updateSql = "UPDATE services SET parts_cost = ?, total_cost = labor_cost + ? WHERE service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setDouble(1, partsCost);
            stmt.setDouble(2, partsCost);
            stmt.setInt(3, serviceId);

            stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Service parts cost updated: ID {0}, Parts Cost {1}",
                    new Object[]{serviceId, partsCost});

            // Update the Service object's total cost if needed
            Service service = findById(serviceId);
            if (service != null) {
                double totalCost = service.getLaborCost() + partsCost;
                service.setTotalCost(totalCost);
                LOGGER.log(Level.INFO, "Service total cost updated: ID {0}, Total Cost {1}",
                        new Object[]{serviceId, totalCost});
            }
        }
    }

    /**
     * Maps a ResultSet row to a Service object.
     *
     * @param rs The ResultSet containing service data
     * @return A Service object populated with data from the ResultSet
     * @throws SQLException If a database access error occurs
     */
    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();

        service.setServiceId(rs.getInt("service_id"));
        service.setVehicleId(rs.getInt("vehicle_id"));
        service.setTechnicianId(rs.getInt("technician_id"));
        service.setMaintenanceType(MaintenanceType.valueOf(rs.getString("maintenance_type")));
        service.setStatus(ServiceStatus.valueOf(rs.getString("status")));
        service.setStartDate(rs.getTimestamp("start_date"));

        Timestamp endDate = rs.getTimestamp("end_date");
        if (endDate != null) {
            service.setEndDate(endDate);
        }

        service.setDescription(rs.getString("description"));
        service.setDiagnosis(rs.getString("diagnosis"));
        service.setLaborCost(rs.getDouble("labor_cost"));
        service.setPartsCost(rs.getDouble("parts_cost"));
        service.setTotalCost(rs.getDouble("total_cost"));
        service.setMileage(rs.getInt("mileage"));
        service.setNotes(rs.getString("notes"));

        return service;
    }

    /**
     * Maps a ResultSet row to a PartInService object.
     *
     * @param rs The ResultSet containing part in service data
     * @return A PartInService object populated with data from the ResultSet
     * @throws SQLException If a database access error occurs
     */
    private PartInService mapResultSetToPartInService(ResultSet rs) throws SQLException {
        PartInService partInService = new PartInService();

        partInService.setPartsInServiceId(rs.getInt("parts_in_service_id"));
        partInService.setServiceId(rs.getInt("service_id"));
        partInService.setPartId(rs.getInt("part_id"));
        partInService.setQuantity(rs.getInt("quantity"));
        partInService.setUnitPrice(rs.getDouble("unit_price"));
        partInService.setTotalPrice(rs.getDouble("total_price"));

        return partInService;
    }
}