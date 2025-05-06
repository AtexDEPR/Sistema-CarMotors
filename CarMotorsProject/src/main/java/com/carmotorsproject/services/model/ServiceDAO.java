/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceDAO implements ServiceDAOInterface {
    private Connection db;

    public ServiceDAO() {
        this.db = DatabaseConnection.getConnection();
        if (this.db == null) {
            throw new RuntimeException("The connection to the database could not be established.");
        }
    }

    @Override
    public void save(Service service) {
        String sqlService = "INSERT INTO services (maintenance_type, vehicle_id, mileage, description, initial_diagnosis, final_observations, estimated_time, labor_cost, status, start_date, end_date, warranty_until) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sqlService, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, service.getMaintenanceType());
            pstmt.setInt(2, service.getVehicleId());
            pstmt.setObject(3, service.getMileage());
            pstmt.setString(4, service.getDescription());
            pstmt.setString(5, service.getInitialDiagnosis());
            pstmt.setString(6, service.getFinalObservations());
            pstmt.setObject(7, service.getEstimatedTime());
            pstmt.setObject(8, service.getLaborCost());
            pstmt.setString(9, service.getStatus());
            pstmt.setTimestamp(10, service.getStartDate() != null ? new Timestamp(service.getStartDate().getTime()) : null);
            pstmt.setTimestamp(11, service.getEndDate() != null ? new Timestamp(service.getEndDate().getTime()) : null);
            pstmt.setDate(12, service.getWarrantyUntil() != null ? new java.sql.Date(service.getWarrantyUntil().getTime()) : null);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                service.setServiceId(rs.getInt(1));
            }

            saveTechnicians(service);
            savePartsInService(service);
        } catch (SQLException e) {
            throw new RuntimeException("Error saving service:" + e.getMessage());
        }
    }

    private void saveTechnicians(Service service) throws SQLException {
        String sql = "INSERT INTO technicians_service (service_id, technician_id, assignment_date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            for (Integer technicianId : service.getTechnicianIds()) {
                pstmt.setInt(1, service.getServiceId());
                pstmt.setInt(2, technicianId);
                pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
                pstmt.executeUpdate();
            }
        }
    }

    private void savePartsInService(Service service) throws SQLException {
        String sql = "INSERT INTO parts_in_service (service_id, part_id, quantity_used, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (PartsInService usage : service.getPartsInService()) {
                pstmt.setInt(1, service.getServiceId());
                pstmt.setInt(2, usage.getPartId());
                pstmt.setInt(3, usage.getQuantityUsed());
                pstmt.setObject(4, usage.getUnitPrice());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    usage.setPartsInServiceId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Service findById(int id) {
        String sqlService = "SELECT * FROM services WHERE service_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sqlService)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                List<Integer> technicianIds = findTechnicianIdsByServiceId(id);
                List<PartsInService> partsInService = findPartsInServiceByServiceId(id);
                return new Service(
                    rs.getInt("service_id"),
                    rs.getString("maintenance_type"),
                    rs.getInt("vehicle_id"),
                    rs.getObject("mileage") != null ? rs.getInt("mileage") : null,
                    rs.getString("description"),
                    rs.getString("initial_diagnosis"),
                    rs.getString("final_observations"),
                    rs.getObject("estimated_time") != null ? rs.getDouble("estimated_time") : null,
                    rs.getObject("labor_cost") != null ? rs.getDouble("labor_cost") : null,
                    rs.getString("status"),
                    rs.getTimestamp("start_date"),
                    rs.getTimestamp("end_date"),
                    rs.getDate("warranty_until"),
                    technicianIds,
                    partsInService
                );
            }
        } catch (SQLException e) {
            System.err.println("Error searching for service:" + e.getMessage());
        }
        return null;
    }

    private List<Integer> findTechnicianIdsByServiceId(int serviceId) {
        List<Integer> technicianIds = new ArrayList<>();
        String sql = "SELECT technician_id FROM technicians_service WHERE service_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, serviceId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                technicianIds.add(rs.getInt("technician_id"));
            }
        } catch (SQLException e) {
            System.err.println("Error searching for service technicians: " + e.getMessage());
        }
        return technicianIds;
    }

    private List<PartsInService> findPartsInServiceByServiceId(int serviceId) {
        List<PartsInService> partsInService = new ArrayList<>();
        String sql = "SELECT * FROM parts_in_service WHERE service_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, serviceId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                partsInService.add(new PartsInService(
                    rs.getInt("parts_in_service_id"),
                    rs.getInt("service_id"),
                    rs.getInt("part_id"),
                    rs.getInt("quantity_used"),
                    rs.getObject("unit_price") != null ? rs.getDouble("unit_price") : null
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching for spare parts usage:" + e.getMessage());
        }
        return partsInService;
    }

    @Override
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services";
        try (PreparedStatement pstmt = db.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int serviceId = rs.getInt("service_id");
                List<Integer> technicianIds = findTechnicianIdsByServiceId(serviceId);
                List<PartsInService> partsInService = findPartsInServiceByServiceId(serviceId);
                services.add(new Service(
                    serviceId,
                    rs.getString("maintenance_type"),
                    rs.getInt("vehicle_id"),
                    rs.getObject("mileage") != null ? rs.getInt("mileage") : null,
                    rs.getString("description"),
                    rs.getString("initial_diagnosis"),
                    rs.getString("final_observations"),
                    rs.getObject("estimated_time") != null ? rs.getDouble("estimated_time") : null,
                    rs.getObject("labor_cost") != null ? rs.getDouble("labor_cost") : null,
                    rs.getString("status"),
                    rs.getTimestamp("start_date"),
                    rs.getTimestamp("end_date"),
                    rs.getDate("warranty_until"),
                    technicianIds,
                    partsInService
                ));
            }
            System.out.println("Number of services recovered: " + services.size());
        } catch (SQLException e) {
            System.err.println("Error getting services:" + e.getMessage());
            throw new RuntimeException("Error getting services: " + e.getMessage());
        }
        return services;
    }

    @Override
    public void update(Service service) {
        String sqlService = "UPDATE services SET maintenance_type = ?, vehicle_id = ?, mileage = ?, description = ?, initial_diagnosis = ?, final_observations = ?, estimated_time = ?, labor_cost = ?, status = ?, start_date = ?, end_date = ?, warranty_until = ? WHERE service_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sqlService)) {
            pstmt.setString(1, service.getMaintenanceType());
            pstmt.setInt(2, service.getVehicleId());
            pstmt.setObject(3, service.getMileage());
            pstmt.setString(4, service.getDescription());
            pstmt.setString(5, service.getInitialDiagnosis());
            pstmt.setString(6, service.getFinalObservations());
            pstmt.setObject(7, service.getEstimatedTime());
            pstmt.setObject(8, service.getLaborCost());
            pstmt.setString(9, service.getStatus());
            pstmt.setTimestamp(10, service.getStartDate() != null ? new Timestamp(service.getStartDate().getTime()) : null);
            pstmt.setTimestamp(11, service.getEndDate() != null ? new Timestamp(service.getEndDate().getTime()) : null);
            pstmt.setDate(12, service.getWarrantyUntil() != null ? new java.sql.Date(service.getWarrantyUntil().getTime()) : null);
            pstmt.setInt(13, service.getServiceId());
            pstmt.executeUpdate();

            String sqlDeleteTechnicians = "DELETE FROM technicians_service WHERE service_id = ?";
            try (PreparedStatement pstmtDelete = db.prepareStatement(sqlDeleteTechnicians)) {
                pstmtDelete.setInt(1, service.getServiceId());
                pstmtDelete.executeUpdate();
            }
            saveTechnicians(service);

            String sqlDeleteParts = "DELETE FROM parts_in_service WHERE service_id = ?";
            try (PreparedStatement pstmtDelete = db.prepareStatement(sqlDeleteParts)) {
                pstmtDelete.setInt(1, service.getServiceId());
                pstmtDelete.executeUpdate();
            }
            savePartsInService(service);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating service: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sqlDeleteParts = "DELETE FROM parts_in_service WHERE service_id = ?";
        String sqlDeleteTechnicians = "DELETE FROM technicians_service WHERE service_id = ?";
        String sqlDeleteService = "DELETE FROM services WHERE service_id = ?";
        try {
            try (PreparedStatement pstmtParts = db.prepareStatement(sqlDeleteParts)) {
                pstmtParts.setInt(1, id);
                pstmtParts.executeUpdate();
            }
            try (PreparedStatement pstmtTechnicians = db.prepareStatement(sqlDeleteTechnicians)) {
                pstmtTechnicians.setInt(1, id);
                pstmtTechnicians.executeUpdate();
            }
            try (PreparedStatement pstmtService = db.prepareStatement(sqlDeleteService)) {
                pstmtService.setInt(1, id);
                pstmtService.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting service:" + e.getMessage());
        }
    }
}