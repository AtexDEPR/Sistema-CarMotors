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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author camper
 */
public class ServiceDAO {
  private Connection db;

    public ServiceDAO() {
        this.db = DatabaseConnection.getConnection();
    }

    public void save(Service service) {
        String sql = "INSERT INTO services (maintenance_type, vehicle_id, mileage, description, initial_diagnosis, " +
                     "final_observations, estimated_time, labor_cost, status, start_date, end_date, warranty_until) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, service.getMaintenanceType().toString());
            pstmt.setInt(2, service.getVehicleId());
            pstmt.setObject(3, service.getMileage());
            pstmt.setString(4, service.getDescription());
            pstmt.setString(5, service.getInitialDiagnosis());
            pstmt.setString(6, service.getFinalObservations());
            pstmt.setObject(7, service.getEstimatedTime());
            pstmt.setObject(8, service.getLaborCost());
            pstmt.setString(9, service.getStatus().toString());
            pstmt.setTimestamp(10, service.getStartDate());
            pstmt.setTimestamp(11, service.getEndDate());
            pstmt.setDate(12, service.getWarrantyUntil() != null ? new java.sql.Date(service.getWarrantyUntil().getTime()) : null);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    service.setStatus(ServiceStatus.Pending); // Actualizar si es necesario
                }
            }
            System.out.println("Service saved successfully");
        } catch (SQLException e) {
            System.err.println("Error saving service: " + e.getMessage());
        }
    }

    public Service findById(int id) {
        String sql = "SELECT * FROM services WHERE service_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Service(
                    rs.getInt("service_id"),
                    MaintenanceType.valueOf(rs.getString("maintenance_type")),
                    rs.getInt("vehicle_id"),
                    rs.getObject("mileage") != null ? rs.getInt("mileage") : null,
                    rs.getString("description"),
                    rs.getString("initial_diagnosis"),
                    rs.getString("final_observations"),
                    rs.getObject("estimated_time") != null ? rs.getDouble("estimated_time") : null,
                    rs.getObject("labor_cost") != null ? rs.getDouble("labor_cost") : null,
                    ServiceStatus.valueOf(rs.getString("status")),
                    rs.getTimestamp("start_date"),
                    rs.getTimestamp("end_date"),
                    rs.getDate("warranty_until")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding service: " + e.getMessage());
        }
        return null;
    }

    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services";
        try (PreparedStatement stmt = db.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                services.add(new Service(
                    rs.getInt("service_id"),
                    MaintenanceType.valueOf(rs.getString("maintenance_type")),
                    rs.getInt("vehicle_id"),
                    rs.getObject("mileage") != null ? rs.getInt("mileage") : null,
                    rs.getString("description"),
                    rs.getString("initial_diagnosis"),
                    rs.getString("final_observations"),
                    rs.getObject("estimated_time") != null ? rs.getDouble("estimated_time") : null,
                    rs.getObject("labor_cost") != null ? rs.getDouble("labor_cost") : null,
                    ServiceStatus.valueOf(rs.getString("status")),
                    rs.getTimestamp("start_date"),
                    rs.getTimestamp("end_date"),
                    rs.getDate("warranty_until")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching services: " + e.getMessage());
        }
        return services;
    }

    public void update(Service service) {
        String sql = "UPDATE services SET maintenance_type = ?, vehicle_id = ?, mileage = ?, description = ?, " +
                     "initial_diagnosis = ?, final_observations = ?, estimated_time = ?, labor_cost = ?, status = ?, " +
                     "start_date = ?, end_date = ?, warranty_until = ? WHERE service_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setString(1, service.getMaintenanceType().toString());
            pstmt.setInt(2, service.getVehicleId());
            pstmt.setObject(3, service.getMileage());
            pstmt.setString(4, service.getDescription());
            pstmt.setString(5, service.getInitialDiagnosis());
            pstmt.setString(6, service.getFinalObservations());
            pstmt.setObject(7, service.getEstimatedTime());
            pstmt.setObject(8, service.getLaborCost());
            pstmt.setString(9, service.getStatus().toString());
            pstmt.setTimestamp(10, service.getStartDate());
            pstmt.setTimestamp(11, service.getEndDate());
            pstmt.setDate(12, service.getWarrantyUntil() != null ? new java.sql.Date(service.getWarrantyUntil().getTime()) : null);
            pstmt.setInt(13, service.getServiceId());
            pstmt.executeUpdate();
            System.out.println("Service updated successfully");
        } catch (SQLException e) {
            System.err.println("Error updating service: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM services WHERE service_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Service deleted successfully");
        } catch (SQLException e) {
            System.err.println("Error deleting service: " + e.getMessage());
        }
    }
    
}
