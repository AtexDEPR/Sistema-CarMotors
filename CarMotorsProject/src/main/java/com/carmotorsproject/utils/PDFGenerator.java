package com.carmotorsproject.utils;

import com.carmotorsproject.config.AppConfig;
import com.carmotorsproject.invoices.modelo.Invoice;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.image.ImageDataFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Generates PDF invoices using iText library.
 */
public class PDFGenerator {
    private static final Logger LOGGER = Logger.getLogger(PDFGenerator.class.getName());
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Generates a PDF invoice and saves it to the specified path.
     *
     * @param invoice The invoice to generate a PDF for
     * @param outputPath The path where the PDF will be saved
     * @return True if the PDF was generated successfully, false otherwise
     */
    public boolean generateInvoicePDF(Invoice invoice, String outputPath) {
        LOGGER.log(Level.INFO, "Generating PDF invoice for invoice ID: {0}", invoice.getId());

        try {
            // Create PDF document
            PdfWriter writer = new PdfWriter(outputPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(36, 36, 36, 36);

            // Add header with workshop information
            addHeader(document);

            // Add invoice information
            addInvoiceInfo(document, invoice);

            // Add customer information
            addCustomerInfo(document, invoice);

            // Add service details
            addServiceDetails(document, invoice);

            // Add parts used
            addPartsUsed(document, invoice);

            // Add totals
            addTotals(document, invoice);

            // Add QR code
            addQRCode(document, invoice);

            // Add footer with legal information
            addFooter(document);

            // Close document
            document.close();

            LOGGER.log(Level.INFO, "PDF invoice generated successfully: {0}", outputPath);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating PDF invoice", e);
            return false;
        }
    }

    /**
     * Adds the header with workshop information to the document.
     *
     * @param document The PDF document
     */
    private void addHeader(Document document) {
        // Workshop logo and information table
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
        headerTable.setWidth(UnitValue.createPercentValue(100));

        // Logo placeholder (in a real implementation, this would be the actual logo)
        Cell logoCell = new Cell();
        Paragraph logoParagraph = new Paragraph(AppConfig.getWorkshopName())
                .setFontSize(16)
                .setBold();
        logoCell.add(logoParagraph);
        logoCell.setBorder(null);
        headerTable.addCell(logoCell);

        // Workshop information
        Cell infoCell = new Cell();
        infoCell.add(new Paragraph(AppConfig.getWorkshopName()).setBold().setFontSize(14));
        infoCell.add(new Paragraph(AppConfig.getWorkshopAddress()));
        infoCell.add(new Paragraph("Tel: " + AppConfig.getWorkshopPhone()));
        infoCell.add(new Paragraph("Email: " + AppConfig.getWorkshopEmail()));
        infoCell.add(new Paragraph(AppConfig.getWorkshopTaxId()).setBold());
        infoCell.setTextAlignment(TextAlignment.RIGHT);
        infoCell.setBorder(null);
        headerTable.addCell(infoCell);

        document.add(headerTable);
        document.add(new Paragraph("\n"));
    }

    /**
     * Adds invoice information to the document.
     *
     * @param document The PDF document
     * @param invoice The invoice
     */
    private void addInvoiceInfo(Document document, Invoice invoice) {
        // Invoice title and information table
        document.add(new Paragraph("FACTURA ELECTRÓNICA DE VENTA")
                .setBold()
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));

        Table invoiceTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
        invoiceTable.setWidth(UnitValue.createPercentValue(100));

        // Invoice number
        invoiceTable.addCell(new Cell().add(new Paragraph("Número de Factura:").setBold()).setBorder(null));
        invoiceTable.addCell(new Cell().add(new Paragraph(invoice.getInvoiceNumber())).setBorder(null));

        // Invoice date
        invoiceTable.addCell(new Cell().add(new Paragraph("Fecha de Emisión:").setBold()).setBorder(null));
        invoiceTable.addCell(new Cell().add(new Paragraph(DATE_FORMAT.format(invoice.getIssueDate()))).setBorder(null));

        // CUFE (Código Único de Facturación Electrónica)
        invoiceTable.addCell(new Cell().add(new Paragraph("CUFE:").setBold()).setBorder(null));
        invoiceTable.addCell(new Cell().add(new Paragraph(invoice.getCufe())).setBorder(null));

        document.add(invoiceTable);
        document.add(new Paragraph("\n"));
    }

    /**
     * Adds customer information to the document.
     *
     * @param document The PDF document
     * @param invoice The invoice
     */
    private void addCustomerInfo(Document document, Invoice invoice) {
        document.add(new Paragraph("INFORMACIÓN DEL CLIENTE")
                .setBold()
                .setFontSize(12));

        Table customerTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
        customerTable.setWidth(UnitValue.createPercentValue(100));

        // Customer name
        customerTable.addCell(new Cell().add(new Paragraph("Nombre:").setBold()).setBorder(null));
        customerTable.addCell(new Cell().add(new Paragraph(invoice.getService().getVehicle().getCustomer().getName())).setBorder(null));

        // Customer ID
        customerTable.addCell(new Cell().add(new Paragraph("Identificación:").setBold()).setBorder(null));
        customerTable.addCell(new Cell().add(new Paragraph(invoice.getService().getVehicle().getCustomer().getIdentification())).setBorder(null));

        // Customer address
        customerTable.addCell(new Cell().add(new Paragraph("Dirección:").setBold()).setBorder(null));
        customerTable.addCell(new Cell().add(new Paragraph(invoice.getService().getVehicle().getCustomer().getAddress())).setBorder(null));

        // Customer phone
        customerTable.addCell(new Cell().add(new Paragraph("Teléfono:").setBold()).setBorder(null));
        customerTable.addCell(new Cell().add(new Paragraph(invoice.getService().getVehicle().getCustomer().getPhone())).setBorder(null));

        // Customer email
        customerTable.addCell(new Cell().add(new Paragraph("Email:").setBold()).setBorder(null));
        customerTable.addCell(new Cell().add(new Paragraph(invoice.getService().getVehicle().getCustomer().getEmail())).setBorder(null));

        document.add(customerTable);
        document.add(new Paragraph("\n"));
    }

    /**
     * Adds service details to the document.
     *
     * @param document The PDF document
     * @param invoice The invoice
     */
    private void addServiceDetails(Document document, Invoice invoice) {
        document.add(new Paragraph("DETALLES DEL SERVICIO")
                .setBold()
                .setFontSize(12));

        Table serviceTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
        serviceTable.setWidth(UnitValue.createPercentValue(100));

        // Service ID
        serviceTable.addCell(new Cell().add(new Paragraph("ID de Servicio:").setBold()).setBorder(null));
        serviceTable.addCell(new Cell().add(new Paragraph(String.valueOf(invoice.getService().getId()))).setBorder(null));

        // Service type
        serviceTable.addCell(new Cell().add(new Paragraph("Tipo de Mantenimiento:").setBold()).setBorder(null));
        serviceTable.addCell(new Cell().add(new Paragraph(invoice.getService().getMaintenanceType().toString())).setBorder(null));

        // Vehicle information
        serviceTable.addCell(new Cell().add(new Paragraph("Vehículo:").setBold()).setBorder(null));
        serviceTable.addCell(new Cell().add(new Paragraph(
                invoice.getService().getVehicle().getMake() + " " +
                        invoice.getService().getVehicle().getModel() + " (" +
                        invoice.getService().getVehicle().getYear() + ")")
        ).setBorder(null));

        // License plate
        serviceTable.addCell(new Cell().add(new Paragraph("Placa:").setBold()).setBorder(null));
        serviceTable.addCell(new Cell().add(new Paragraph(invoice.getService().getVehicle().getLicensePlate())).setBorder(null));

        // Service description
        serviceTable.addCell(new Cell().add(new Paragraph("Descripción:").setBold()).setBorder(null));
        serviceTable.addCell(new Cell().add(new Paragraph(invoice.getService().getDescription())).setBorder(null));

        // Service date
        serviceTable.addCell(new Cell().add(new Paragraph("Fecha de Servicio:").setBold()).setBorder(null));
        serviceTable.addCell(new Cell().add(new Paragraph(DATE_FORMAT.format(invoice.getService().getServiceDate()))).setBorder(null));

        document.add(serviceTable);
        document.add(new Paragraph("\n"));
    }

    /**
     * Adds parts used in the service to the document.
     *
     * @param document The PDF document
     * @param invoice The invoice
     */
    private void addPartsUsed(Document document, Invoice invoice) {
        document.add(new Paragraph("REPUESTOS UTILIZADOS")
                .setBold()
                .setFontSize(12));

        Table partsTable = new Table(UnitValue.createPercentArray(new float[]{5, 40, 15, 20, 20}));
        partsTable.setWidth(UnitValue.createPercentValue(100));

        // Table header
        partsTable.addHeaderCell(new Cell().add(new Paragraph("#").setBold()).setTextAlignment(TextAlignment.CENTER));
        partsTable.addHeaderCell(new Cell().add(new Paragraph("Descripción").setBold()).setTextAlignment(TextAlignment.CENTER));
        partsTable.addHeaderCell(new Cell().add(new Paragraph("Cantidad").setBold()).setTextAlignment(TextAlignment.CENTER));
        partsTable.addHeaderCell(new Cell().add(new Paragraph("Precio Unitario").setBold()).setTextAlignment(TextAlignment.CENTER));
        partsTable.addHeaderCell(new Cell().add(new Paragraph("Subtotal").setBold()).setTextAlignment(TextAlignment.CENTER));

        // Parts data (in a real implementation, this would iterate through actual parts)
        int count = 1;
        for (var partInService : invoice.getService().getPartsInService()) {
            partsTable.addCell(new Cell().add(new Paragraph(String.valueOf(count))).setTextAlignment(TextAlignment.CENTER));
            partsTable.addCell(new Cell().add(new Paragraph(partInService.getPart().getName())));
            partsTable.addCell(new Cell().add(new Paragraph(String.valueOf(partInService.getQuantity()))).setTextAlignment(TextAlignment.CENTER));
            partsTable.addCell(new Cell().add(new Paragraph(CURRENCY_FORMAT.format(partInService.getPart().getPrice()))).setTextAlignment(TextAlignment.RIGHT));
            partsTable.addCell(new Cell().add(new Paragraph(CURRENCY_FORMAT.format(partInService.getPart().getPrice() * partInService.getQuantity()))).setTextAlignment(TextAlignment.RIGHT));
            count++;
        }

        document.add(partsTable);
        document.add(new Paragraph("\n"));
    }

    /**
     * Adds invoice totals to the document.
     *
     * @param document The PDF document
     * @param invoice The invoice
     */
    private void addTotals(Document document, Invoice invoice) {
        Table totalsTable = new Table(UnitValue.createPercentArray(new float[]{70, 30}));
        totalsTable.setWidth(UnitValue.createPercentValue(100));

        // Labor cost
        totalsTable.addCell(new Cell().add(new Paragraph("Costo de Mano de Obra:").setBold()).setTextAlignment(TextAlignment.RIGHT).setBorder(null));
        totalsTable.addCell(new Cell().add(new Paragraph(CURRENCY_FORMAT.format(invoice.getLaborCost()))).setTextAlignment(TextAlignment.RIGHT).setBorder(null));

        // Parts subtotal
        totalsTable.addCell(new Cell().add(new Paragraph("Subtotal Repuestos:").setBold()).setTextAlignment(TextAlignment.RIGHT).setBorder(null));
        totalsTable.addCell(new Cell().add(new Paragraph(CURRENCY_FORMAT.format(invoice.getPartsSubtotal()))).setTextAlignment(TextAlignment.RIGHT).setBorder(null));

        // Subtotal
        totalsTable.addCell(new Cell().add(new Paragraph("Subtotal:").setBold()).setTextAlignment(TextAlignment.RIGHT).setBorder(null));
        totalsTable.addCell(new Cell().add(new Paragraph(CURRENCY_FORMAT.format(invoice.getSubtotal()))).setTextAlignment(TextAlignment.RIGHT).setBorder(null));

        // Tax
        totalsTable.addCell(new Cell().add(new Paragraph("IVA (19%):").setBold()).setTextAlignment(TextAlignment.RIGHT).setBorder(null));
        totalsTable.addCell(new Cell().add(new Paragraph(CURRENCY_FORMAT.format(invoice.getTax()))).setTextAlignment(TextAlignment.RIGHT).setBorder(null));

        // Total
        Cell totalLabelCell = new Cell().add(new Paragraph("TOTAL:").setBold()).setTextAlignment(TextAlignment.RIGHT);
        totalLabelCell.setBorder(null);
        totalLabelCell.setBorderTop(new SolidBorder(1));
        totalsTable.addCell(totalLabelCell);

        Cell totalValueCell = new Cell().add(new Paragraph(CURRENCY_FORMAT.format(invoice.getTotal())).setBold()).setTextAlignment(TextAlignment.RIGHT);
        totalValueCell.setBorder(null);
        totalValueCell.setBorderTop(new SolidBorder(1));
        totalsTable.addCell(totalValueCell);

        document.add(totalsTable);
        document.add(new Paragraph("\n"));
    }

    /**
     * Adds a QR code to the document.
     *
     * @param document The PDF document
     * @param invoice The invoice
     * @throws IOException If there is an error creating the QR code
     */
    private void addQRCode(Document document, Invoice invoice) throws IOException {
        // Generate QR code data
        String qrData = "CUFE:" + invoice.getCufe() + "|" +
                "NIT:" + AppConfig.getWorkshopTaxId() + "|" +
                "FACTURA:" + invoice.getInvoiceNumber() + "|" +
                "FECHA:" + DATE_FORMAT.format(invoice.getIssueDate()) + "|" +
                "TOTAL:" + invoice.getTotal();

        // Generate QR code image
        QRGenerator qrGenerator = new QRGenerator();
        BufferedImage qrImage = qrGenerator.generateQRCode(qrData, 200, 200);

        // Convert BufferedImage to iText Image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);
        Image qrCodeImage = new Image(ImageDataFactory.create(baos.toByteArray()));
        qrCodeImage.setHorizontalAlignment(HorizontalAlignment.CENTER);

        document.add(qrCodeImage);
        document.add(new Paragraph("Escanee el código QR para verificar la autenticidad de esta factura.")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setItalic());
        document.add(new Paragraph("\n"));
    }

    /**
     * Adds a footer with legal information to the document.
     *
     * @param document The PDF document
     */
    private void addFooter(Document document) {
        document.add(new Paragraph("INFORMACIÓN IMPORTANTE")
                .setBold()
                .setFontSize(10));

        document.add(new Paragraph(
                "Esta factura electrónica cumple con los requisitos establecidos por la DIAN según la Resolución 000042 de 2020. " +
                        "La representación gráfica de la factura electrónica está sujeta a verificación. " +
                        "Conserve este documento como soporte de su compra.")
                .setFontSize(8));

        document.add(new Paragraph("Factura generada por Car Motors Workshop - " + new Date())
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY));
    }
}