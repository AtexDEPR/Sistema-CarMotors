package com.carmotorsproject.utils;

import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.services.model.Service;
import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.campaigns.model.Campaign;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates reports (e.g., inventory, services) as tables or charts.
 */
public class ReportUtil {
    private static final Logger LOGGER = Logger.getLogger(ReportUtil.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static void exportTableToPDF(JTable reportTable, File fileToSave, Map<String, Object> parameters) {
    }

    /**
     * Generates an inventory report as a JTable model.
     *
     * @param parts The list of parts to include in the report
     * @return A DefaultTableModel for use in a JTable
     */
    public DefaultTableModel generateInventoryReportModel(List<Part> parts) {
        return null;
    }

    /**
     * Generates a service report as a JTable model.
     *
     * @param services The list of services to include in the report
     * @return A DefaultTableModel for use in a JTable
     */
    public DefaultTableModel generateServiceReportModel(List<Service> services) {
        LOGGER.log(Level.INFO, "Generating service report for {0} services", services.size());

        // Define column names
        String[] columnNames = {"ID", "Tipo", "Vehículo", "Cliente", "Fecha", "Estado", "Técnico", "Total"};

        // Create table model
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Add rows
        for (Service service : services) {
            Object[] row = {
                    service.getId(),
                    service.getMaintenanceType(),
                    service.getVehicle() != null ? service.getVehicle().getLicensePlate() : "N/A",
                    service.getVehicle() != null && service.getVehicle().getCustomer() != null ?
                            service.getVehicle().getCustomer().getFullName() : "N/A", // Usar getFullName()
                    service.getServiceDate() != null ? DATE_FORMAT.format(service.getServiceDate()) : "N/A",
                    service.getStatus(),
                    service.getTechnician() != null ? service.getTechnician().getName() : "N/A",
                    service.getTotal()
            };
            model.addRow(row);
        }

        return model;
    }

    /**
     * Generates a customer report as a JTable model.
     *
     * @param customers The list of customers to include in the report
     * @return A DefaultTableModel for use in a JTable
     */
    public DefaultTableModel generateCustomerReportModel(List<Customer> customers) {
        LOGGER.log(Level.INFO, "Generating customer report for {0} customers", customers.size());

        // Define column names
        String[] columnNames = {"ID", "Nombre", "Identificación", "Teléfono", "Email", "Nivel", "Puntos"};

        // Create table model
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Add rows
        for (Customer customer : customers) {
            Object[] row = {
                    customer.getId(),
                    customer.getFullName(),
                    customer.getIdentification(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getLoyaltyProgram() != null ? customer.getLoyaltyProgram().getLevel() : "N/A",
                    customer.getLoyaltyProgram() != null ? customer.getLoyaltyProgram().getAccumulatedPoints() : 0
            };
            model.addRow(row);
        }

        return model;
    }

    /**
     * Generates a campaign report as a JTable model.
     *
     * @param campaigns The list of campaigns to include in the report
     * @return A DefaultTableModel for use in a JTable
     */
    public DefaultTableModel generateCampaignReportModel(List<Campaign> campaigns) {
        LOGGER.log(Level.INFO, "Generating campaign report for {0} campaigns", campaigns.size());

        // Define column names
        String[] columnNames = {"ID", "Nombre", "Fecha Inicio", "Fecha Fin", "Descuento", "Citas", "Completadas"};

        // Create table model
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Add rows
        for (Campaign campaign : campaigns) {
            Object[] row = {
                    campaign.getId(),
                    campaign.getName(),
                    campaign.getStartDate() != null ? DATE_FORMAT.format(campaign.getStartDate()) : "N/A",
                    campaign.getEndDate() != null ? DATE_FORMAT.format(campaign.getEndDate()) : "N/A",
                    campaign.getDiscountPercentage() + "%",
                    campaign.getAppointments() != null ? campaign.getAppointments().size() : 0,
                    campaign.getCompletedAppointments()
            };
            model.addRow(row);
        }

        return model;
    }

    /**
     * Exports a report to a PDF file.
     *
     * @param title The report title
     * @param columnNames The column names
     * @param data The report data as a list of rows (each row is an array of objects)
     * @param outputPath The path where the PDF will be saved
     * @return True if the PDF was generated successfully, false otherwise
     */
    public boolean exportReportToPdf(String title, String[] columnNames, List<Object[]> data, String outputPath) {
        LOGGER.log(Level.INFO, "Exporting report to PDF: {0}", outputPath);

        try {
            // Create PDF document
            PdfWriter writer = new PdfWriter(new FileOutputStream(outputPath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            document.add(new Paragraph(title)
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Generado el " + DATE_FORMAT.format(new Date()))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));

            // Create table
            Table table = new Table(UnitValue.createPercentArray(columnNames.length));
            table.setWidth(UnitValue.createPercentValue(100));

            // Add header row
            for (String columnName : columnNames) {
                Cell cell = new Cell().add(new Paragraph(columnName).setBold());
                cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                cell.setTextAlignment(TextAlignment.CENTER);
                table.addHeaderCell(cell);
            }

            // Add data rows
            for (Object[] row : data) {
                for (Object value : row) {
                    String cellValue = value != null ? value.toString() : "";
                    table.addCell(new Cell().add(new Paragraph(cellValue)));
                }
            }

            document.add(table);

            // Add footer
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Car Motors Workshop - Sistema de Gestión")
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY));

            document.close();

            LOGGER.log(Level.INFO, "PDF report generated successfully: {0}", outputPath);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error generating PDF report", e);
            return false;
        }
    }

    /**
     * Exports a JTable to a PDF file.
     *
     * @param title The report title
     * @param table The JTable containing the report data
     * @param outputPath The path where the PDF will be saved
     * @return True if the PDF was generated successfully, false otherwise
     */
    public boolean exportTableToPdf(String title, JTable table, String outputPath) {
        LOGGER.log(Level.INFO, "Exporting JTable to PDF: {0}", outputPath);

        try {
            // Get column names
            int columnCount = table.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = table.getColumnName(i);
            }

            // Get data
            int rowCount = table.getRowCount();
            Object[][] data = new Object[rowCount][columnCount];
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    data[row][col] = table.getValueAt(row, col);
                }
            }

            // Create PDF document
            PdfWriter writer = new PdfWriter(new FileOutputStream(outputPath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            document.add(new Paragraph(title)
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Generado el " + DATE_FORMAT.format(new Date()))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));

            // Create table
            Table pdfTable = new Table(UnitValue.createPercentArray(columnCount));
            pdfTable.setWidth(UnitValue.createPercentValue(100));

            // Add header row
            for (String columnName : columnNames) {
                Cell cell = new Cell().add(new Paragraph(columnName).setBold());
                cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                cell.setTextAlignment(TextAlignment.CENTER);
                pdfTable.addHeaderCell(cell);
            }

            // Add data rows
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    Object value = data[row][col];
                    String cellValue = value != null ? value.toString() : "";
                    pdfTable.addCell(new Cell().add(new Paragraph(cellValue)));
                }
            }

            document.add(pdfTable);

            // Add footer
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Car Motors Workshop - Sistema de Gestión")
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY));

            document.close();

            LOGGER.log(Level.INFO, "PDF report from JTable generated successfully: {0}", outputPath);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error generating PDF report from JTable", e);
            return false;
        }
    }

    /**
     * Exports data to a CSV file.
     *
     * @param columnNames The column names
     * @param data The data as a list of rows (each row is an array of objects)
     * @param outputPath The path where the CSV will be saved
     * @return True if the CSV was generated successfully, false otherwise
     */
    public boolean exportToCsv(String[] columnNames, List<Object[]> data, String outputPath) {
        LOGGER.log(Level.INFO, "Exporting data to CSV: {0}", outputPath);

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            // Write column headers
            StringBuilder header = new StringBuilder();
            for (int i = 0; i < columnNames.length; i++) {
                if (i > 0) {
                    header.append(",");
                }
                header.append("\"").append(columnNames[i].replace("\"", "\"\"")).append("\"");
            }
            header.append("\n");
            fos.write(header.toString().getBytes());

            // Write data rows
            for (Object[] row : data) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    if (i > 0) {
                        line.append(",");
                    }

                    String value = row[i] != null ? row[i].toString() : "";
                    line.append("\"").append(value.replace("\"", "\"\"")).append("\"");
                }
                line.append("\n");
                fos.write(line.toString().getBytes());
            }

            LOGGER.log(Level.INFO, "CSV file generated successfully: {0}", outputPath);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error generating CSV file", e);
            return false;
        }
    }

    /**
     * Generates a summary report with statistics.
     *
     * @param title The report title
     * @param statistics A map of statistic names to values
     * @param outputPath The path where the PDF will be saved
     * @return True if the PDF was generated successfully, false otherwise
     */
    public boolean generateSummaryReport(String title, Map<String, Object> statistics, String outputPath) {
        LOGGER.log(Level.INFO, "Generating summary report: {0}", outputPath);

        try {
            // Create PDF document
            PdfWriter writer = new PdfWriter(new FileOutputStream(outputPath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            document.add(new Paragraph(title)
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Generado el " + DATE_FORMAT.format(new Date()))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));

            // Create table for statistics
            Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
            table.setWidth(UnitValue.createPercentValue(100));

            // Add statistics
            for (Map.Entry<String, Object> entry : statistics.entrySet()) {
                table.addCell(new Cell().add(new Paragraph(entry.getKey()).setBold()));
                table.addCell(new Cell().add(new Paragraph(entry.getValue().toString())));
            }

            document.add(table);

            // Add footer
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Car Motors Workshop - Sistema de Gestión")
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY));

            document.close();

            LOGGER.log(Level.INFO, "Summary report generated successfully: {0}", outputPath);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error generating summary report", e);
            return false;
        }
    }
}