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
import java.util.List;

public class VehicleDAO implements VehicleDAOInterface {
    private Connection db;

    public VehicleDAO() {
        this.db = DatabaseConnection.getConnection();
    }

    @Override
    public void save(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (customer_id, license_plate, make, model, year, creation_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, vehicle.getCustomerId());
            pstmt.setString(2, vehicle.getLicensePlate());
            pstmt.setString(3, vehicle.getMake());
            pstmt.setString(4, vehicle.getModel());
            pstmt.setInt(5, vehicle.getYear());
            pstmt.setTimestamp(6, new Timestamp(vehicle.getCreationDate().getTime()));
            pstmt.setTimestamp(7, new Timestamp(vehicle.getLastUpdateDate().getTime()));
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                vehicle.setVehicleId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving vehicle:" + e.getMessage());
        }
    }

    @Override
    public Vehicle findById(int id) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Vehicle(
                    rs.getInt("vehicle_id"),
                    rs.getInt("customer_id"),
                    rs.getString("license_plate"),
                    rs.getString("make"),
                    rs.getString("model"),
                    rs.getInt("year"),
                    rs.getTimestamp("creation_date"),
                    rs.getTimestamp("last_update_date")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar vehículo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";
        try (PreparedStatement pstmt = db.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                vehicles.add(new Vehicle(
                    rs.getInt("vehicle_id"),
                    rs.getInt("customer_id"),
                    rs.getString("license_plate"),
                    rs.getString("make"),
                    rs.getString("model"),
                    rs.getInt("year"),
                    rs.getTimestamp("creation_date"),
                    rs.getTimestamp("last_update_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting vehicles:" + e.getMessage());
        }
        return vehicles;
    }

    @Override
    public void update(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET customer_id = ?, license_plate = ?, make = ?, model = ?, year = ?, last_update_date = ? WHERE vehicle_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, vehicle.getCustomerId());
            pstmt.setString(2, vehicle.getLicensePlate());
            pstmt.setString(3, vehicle.getMake());
            pstmt.setString(4, vehicle.getModel());
            pstmt.setInt(5, vehicle.getYear());
            pstmt.setTimestamp(6, new Timestamp(vehicle.getLastUpdateDate().getTime()));
            pstmt.setInt(7, vehicle.getVehicleId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating vehicle: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting vehicle: " + e.getMessage());
        }
    }
}