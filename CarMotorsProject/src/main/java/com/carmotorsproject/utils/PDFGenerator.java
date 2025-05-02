/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for generating PDF documents
 */
public class PDFGenerator {
    private static final Logger LOGGER = Logger.getLogger(PDFGenerator.class.getName());

    /**
     * Generate a basic PDF invoice
     *
     * @param invoiceNumber Invoice number
     * @param customerName Customer name
     * @param vehicleInfo Vehicle information
     * @param services Services provided
     * @param parts Parts used
     * @param laborCost Labor cost
     * @param subtotal Subtotal
     * @param taxes Taxes
     * @param total Total amount
     * @param qrCodePath Path to QR code image
     * @param outputPath Output file path
     * @return true if PDF was generated successfully, false otherwise
     */
    public boolean generateInvoice(String invoiceNumber, String customerName, String vehicleInfo,
                                   String[] services, String[] parts, double laborCost,
                                   double subtotal, double taxes, double total,
                                   String qrCodePath, String outputPath) {
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // Add header
            addHeader(document, invoiceNumber);

            // Add customer and vehicle info
            addCustomerInfo(document, customerName, vehicleInfo);

            // Add services table
            addServicesTable(document, services);

            // Add parts table
            addPartsTable(document, parts);

            // Add costs
            addCosts(document, laborCost, subtotal, taxes, total);

            // Add QR code if available
            if (qrCodePath != null && !qrCodePath.isEmpty()) {
                addQRCode(document, qrCodePath);
            }

            // Add footer
            addFooter(document);

            document.close();
            LOGGER.log(Level.INFO, "Invoice PDF generated successfully: {0}", outputPath);
            return true;

        } catch (DocumentException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error generating PDF", e);
            if (document.isOpen()) {
                document.close();
            }
            return false;
        }
    }

    /**
     * Add header to the PDF document
     */
    private void addHeader(Document document, String invoiceNumber) throws DocumentException {
        Paragraph header = new Paragraph("INVOICE #" + invoiceNumber,
                new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph date = new Paragraph("Date: " + DateUtil.getCurrentDate(),
                new Font(Font.FontFamily.HELVETICA, 12));
        date.setAlignment(Element.ALIGN_RIGHT);
        document.add(date);

        document.add(Chunk.NEWLINE);
    }

    /**
     * Add customer and vehicle information to the PDF document
     */
    private void addCustomerInfo(Document document, String customerName, String vehicleInfo)
            throws DocumentException {
        Paragraph customerPara = new Paragraph("Customer: " + customerName,
                new Font(Font.FontFamily.HELVETICA, 12));
        document.add(customerPara);

        Paragraph vehiclePara = new Paragraph("Vehicle: " + vehicleInfo,
                new Font(Font.FontFamily.HELVETICA, 12));
        document.add(vehiclePara);

        document.add(Chunk.NEWLINE);
    }

    /**
     * Add services table to the PDF document
     */
    private void addServicesTable(Document document, String[] services) throws DocumentException {
        Paragraph serviceTitle = new Paragraph("Services",
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        document.add(serviceTitle);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        // Add table headers
        table.addCell(createCell("Description", true));
        table.addCell(createCell("Details", true));

        // Add services
        for (String service : services) {
            String[] parts = service.split("\\|");
            if (parts.length >= 2) {
                table.addCell(createCell(parts[0], false));
                table.addCell(createCell(parts[1], false));
            } else {
                table.addCell(createCell(service, false));
                table.addCell(createCell("", false));
            }
        }

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    /**
     * Add parts table to the PDF document
     */
    private void addPartsTable(Document document, String[] parts) throws DocumentException {
        Paragraph partsTitle = new Paragraph("Parts Used",
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        document.add(partsTitle);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        // Add table headers
        table.addCell(createCell("Part Name", true));
        table.addCell(createCell("Quantity", true));
        table.addCell(createCell("Price", true));

        // Add parts
        for (String part : parts) {
            String[] partInfo = part.split("\\|");
            if (partInfo.length >= 3) {
                table.addCell(createCell(partInfo[0], false));
                table.addCell(createCell(partInfo[1], false));
                table.addCell(createCell(partInfo[2], false));
            } else {
                table.addCell(createCell(part, false));
                table.addCell(createCell("", false));
                table.addCell(createCell("", false));
            }
        }

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    /**
     * Add costs to the PDF document
     */
    private void addCosts(Document document, double laborCost, double subtotal,
                          double taxes, double total) throws DocumentException {
        PdfPTable costsTable = new PdfPTable(2);
        costsTable.setWidthPercentage(50);
        costsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        costsTable.addCell(createCell("Labor Cost:", false));
        costsTable.addCell(createCell(String.format("$%.2f", laborCost), false));

        costsTable.addCell(createCell("Subtotal:", false));
        costsTable.addCell(createCell(String.format("$%.2f", subtotal), false));

        costsTable.addCell(createCell("Taxes:", false));
        costsTable.addCell(createCell(String.format("$%.2f", taxes), false));

        costsTable.addCell(createCell("Total:", true));
        costsTable.addCell(createCell(String.format("$%.2f", total), true));

        document.add(costsTable);
        document.add(Chunk.NEWLINE);
    }

    /**
     * Add QR code to the PDF document
     */
    private void addQRCode(Document document, String qrCodePath) throws DocumentException, IOException {
        Image qrCode = Image.getInstance(qrCodePath);
        qrCode.setAlignment(Element.ALIGN_CENTER);
        qrCode.scaleToFit(100, 100);
        document.add(qrCode);
    }

    /**
     * Add footer to the PDF document
     */
    private void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph("Thank you for your business!",
                new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    /**
     * Create a cell for a PDF table
     */
    private PdfPCell createCell(String content, boolean isHeader) {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, isHeader ? Font.BOLD : Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        if (isHeader) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }
        return cell;
    }
}