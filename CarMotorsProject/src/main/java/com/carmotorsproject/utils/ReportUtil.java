/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.carmotorsproject.config.DatabaseConnection;

/**
 * Utility class for generating reports
 */
public class ReportUtil {
    private static final Logger LOGGER = Logger.getLogger(ReportUtil.class.getName());

    /**
     * Generate a vehicle service history report
     *
     * @param vehicleId Vehicle ID
     * @param outputPath Output file path
     * @return true if report was generated successfully, false otherwise
     */
    public static boolean generateVehicleServiceHistoryReport(int vehicleId, String outputPath) {
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // Add title
            Paragraph title = new Paragraph("Vehicle Service History Report",
                    new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE);

            // Get vehicle information
            String[] vehicleInfo = getVehicleInfo(vehicleId);
            if (vehicleInfo == null) {
                throw new DocumentException("Vehicle not found");
            }

            // Add vehicle information
            Paragraph vehicleDetails = new Paragraph("Vehicle Details:",
                    new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            document.add(vehicleDetails);

            document.add(new Paragraph("License Plate: " + vehicleInfo[0]));
            document.add(new Paragraph("Make: " + vehicleInfo[1]));
            document.add(new Paragraph("Model: " + vehicleInfo[2]));
            document.add(new Paragraph("Owner: " + vehicleInfo[3]));

            document.add(Chunk.NEWLINE);

            // Get service history
            List<String[]> serviceHistory = getServiceHistory(vehicleId);

            // Add service history table
            Paragraph serviceTitle = new Paragraph("Service History:",
                    new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            document.add(serviceTitle);

            if (serviceHistory.isEmpty()) {
                document.add(new Paragraph("No service history found for this vehicle."));
            } else {
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);

                // Add table headers
                table.addCell(createCell("Date", true));
                table.addCell(createCell("Type", true));
                table.addCell(createCell("Description", true));
                table.addCell(createCell("Status", true));
                table.addCell(createCell("Cost", true));

                // Add service history rows
                for (String[] service : serviceHistory) {
                    table.addCell(createCell(service[0], false));
                    table.addCell(createCell(service[1], false));
                    table.addCell(createCell(service[2], false));
                    table.addCell(createCell(service[3], false));
                    table.addCell(createCell(service[4], false));
                }

                document.add(table);
            }

            document.add(Chunk.NEWLINE);

            // Add footer
            Paragraph footer = new Paragraph("Report generated on " + DateUtil.getCurrentDateTime(),
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

            document.close();
            LOGGER.log(Level.INFO, "Vehicle service history report generated successfully: {0}", outputPath);
            return true;

        } catch (DocumentException | IOException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Error generating vehicle service history report", e);
            if (document.isOpen()) {
                document.close();
            }
            return false;
        }
    }

    /**
     * Generate a technician productivity report
     *
     * @param startDate Start date (yyyy-MM-dd)
     * @param endDate End date (yyyy-MM-dd)
     * @param outputPath Output file path
     * @return true if report was generated successfully, false otherwise
     */
    public static boolean generateTechnicianProductivityReport(String startDate, String endDate, String outputPath) {
        Document document = new Document(PageSize.A4.rotate());

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // Add title
            Paragraph title = new Paragraph("Technician Productivity Report",
                    new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add date range
            Paragraph dateRange = new Paragraph("Period: " + startDate + " to " + endDate,
                    new Font(Font.FontFamily.HELVETICA, 12));
            dateRange.setAlignment(Element.ALIGN_CENTER);
            document.add(dateRange);

            document.add(Chunk.NEWLINE);

            // Get technician productivity data
            List<String[]> technicianData = getTechnicianProductivityData(startDate, endDate);

            if (technicianData.isEmpty()) {
                document.add(new Paragraph("No technician productivity data found for this period."));
            } else {
                PdfPTable table = new PdfPTable(7);
                table.setWidthPercentage(100);

                // Add table headers
                table.addCell(createCell("Technician", true));
                table.addCell(createCell("Specialty", true));
                table.addCell(createCell("Services Assigned", true));
                table.addCell(createCell("Services Completed", true));
                table.addCell(createCell("Total Hours", true));
                table.addCell(createCell("Avg Hours/Service", true));
                table.addCell(createCell("Efficiency Rating", true));

                // Add technician data rows
                for (String[] technician : technicianData) {
                    table.addCell(createCell(technician[0], false));
                    table.addCell(createCell(technician[1], false));
                    table.addCell(createCell(technician[2], false));
                    table.addCell(createCell(technician[3], false));
                    table.addCell(createCell(technician[4], false));
                    table.addCell(createCell(technician[5], false));
                    table.addCell(createCell(technician[6], false));
                }

                document.add(table);
            }

            document.add(Chunk.NEWLINE);

            // Add footer
            Paragraph footer = new Paragraph("Report generated on " + DateUtil.getCurrentDateTime(),
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

            document.close();
            LOGGER.log(Level.INFO, "Technician productivity report generated successfully: {0}", outputPath);
            return true;

        } catch (DocumentException | IOException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Error generating technician productivity report", e);
            if (document.isOpen()) {
                document.close();
            }
            return false;
        }
    }

    /**
     * Get vehicle information from database
     *
     * @param vehicleId Vehicle ID
     * @return Array with vehicle information [license_plate, make, model, owner_name]
     * @throws SQLException if a database access error occurs
     */
    private static String[] getVehicleInfo(int vehicleId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT v.license_plate, v.make, v.model, c.name " +
                    "FROM vehicles v " +
                    "JOIN customers c ON v.customer_id = c.customer_id " +
                    "WHERE v.vehicle_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vehicleId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new String[] {
                        rs.getString("license_plate"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getString("name")
                };
            }

            return null;

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    /**
     * Get service history for a vehicle from database
     *
     * @param vehicleId Vehicle ID
     * @return List of service history records [date, type, description, status, cost]
     * @throws SQLException if a database access error occurs
     */
    private static List<String[]> getServiceHistory(int vehicleId) throws SQLException {
        List<String[]> serviceHistory = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT s.start_date, s.maintenance_type, s.description, s.status, " +
                    "COALESCE(i.total, 0) as total_cost " +
                    "FROM services s " +
                    "LEFT JOIN invoices i ON s.service_id = i.service_id " +
                    "WHERE s.vehicle_id = ? " +
                    "ORDER BY s.start_date DESC";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vehicleId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                serviceHistory.add(new String[] {
                        rs.getString("start_date"),
                        rs.getString("maintenance_type"),
                        rs.getString("description"),
                        rs.getString("status"),
                        String.format("$%.2f", rs.getDouble("total_cost"))
                });
            }

            return serviceHistory;

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    /**
     * Get technician productivity data from database
     *
     * @param startDate Start date (yyyy-MM-dd)
     * @param endDate End date (yyyy-MM-dd)
     * @return List of technician productivity records
     * @throws SQLException if a database access error occurs
     */
    private static List<String[]> getTechnicianProductivityData(String startDate, String endDate)
            throws SQLException {
        List<String[]> technicianData = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT t.name, t.specialty, " +
                    "COUNT(ts.service_id) AS assigned_services, " +
                    "COUNT(CASE WHEN s.status = 'Completed' THEN 1 END) AS completed_services, " +
                    "SUM(ts.hours_worked) AS total_hours, " +
                    "AVG(ts.hours_worked) AS avg_hours_per_service, " +
                    "CASE " +
                    "  WHEN COUNT(ts.service_id) > 0 THEN " +
                    "    (COUNT(CASE WHEN s.status = 'Completed' THEN 1 END) * 100.0 / COUNT(ts.service_id)) " +
                    "  ELSE 0 " +
                    "END AS efficiency_rating " +
                    "FROM technicians t " +
                    "LEFT JOIN technicians_service ts ON t.technician_id = ts.technician_id " +
                    "LEFT JOIN services s ON ts.service_id = s.service_id " +
                    "WHERE (s.start_date IS NULL OR (s.start_date BETWEEN ? AND ?)) " +
                    "GROUP BY t.technician_id, t.name, t.specialty " +
                    "ORDER BY efficiency_rating DESC";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            rs = stmt.executeQuery();

            while (rs.next()) {
                technicianData.add(new String[] {
                        rs.getString("name"),
                        rs.getString("specialty"),
                        rs.getString("assigned_services"),
                        rs.getString("completed_services"),
                        String.format("%.2f", rs.getDouble("total_hours")),
                        String.format("%.2f", rs.getDouble("avg_hours_per_service")),
                        String.format("%.1f%%", rs.getDouble("efficiency_rating"))
                });
            }

            return technicianData;

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    /**
     * Create a cell for a PDF table
     */
    private static PdfPCell createCell(String content, boolean isHeader) {
        Font font = new Font(Font.FontFamily.HELVETICA, 10, isHeader ? Font.BOLD : Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        if (isHeader) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }
        return cell;
    }
}