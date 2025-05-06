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

public class TechnicianDAO implements TechnicianDAOInterface {
    private Connection db;

    public TechnicianDAO() {
        this.db = DatabaseConnection.getConnection();
    }

    @Override
    public void save(Technician technician) {
        String sql = "INSERT INTO technicians (name, specialty, status, creation_date, last_update_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, technician.getName());
            pstmt.setString(2, technician.getSpecialty());
            pstmt.setString(3, technician.getStatus());
            pstmt.setTimestamp(4, new Timestamp(technician.getCreationDate().getTime()));
            pstmt.setTimestamp(5, new Timestamp(technician.getLastUpdateDate().getTime()));
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                technician.setTechnicianId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving technical:" + e.getMessage());
        }
    }

    @Override
    public Technician findById(int id) {
        String sql = "SELECT * FROM technicians WHERE technician_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Technician(
                    rs.getInt("technician_id"),
                    rs.getString("name"),
                    rs.getString("specialty"),
                    rs.getString("status"),
                    rs.getTimestamp("creation_date"),
                    rs.getTimestamp("last_update_date")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar técnico: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Technician> findAll() {
        List<Technician> technicians = new ArrayList<>();
        String sql = "SELECT * FROM technicians";
        try (PreparedStatement pstmt = db.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                technicians.add(new Technician(
                    rs.getInt("technician_id"),
                    rs.getString("name"),
                    rs.getString("specialty"),
                    rs.getString("status"),
                    rs.getTimestamp("creation_date"),
                    rs.getTimestamp("last_update_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting technicians:" + e.getMessage());
        }
        return technicians;
    }

    @Override
    public void update(Technician technician) {
        String sql = "UPDATE technicians SET name = ?, specialty = ?, status = ?, last_update_date = ? WHERE technician_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setString(1, technician.getName());
            pstmt.setString(2, technician.getSpecialty());
            pstmt.setString(3, technician.getStatus());
            pstmt.setTimestamp(4, new Timestamp(technician.getLastUpdateDate().getTime()));
            pstmt.setInt(5, technician.getTechnicianId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating technician:" + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM technicians WHERE technician_id = ?";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting technician:" + e.getMessage());
        }
    }
}