/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.invoices.model;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import javax.swing.text.Document;
import org.w3c.dom.Text;

public class PDFGenerator {
   /* public static void generateInvoicePDF(Invoice invoice) {
        try {
            // Crear el archivo PDF
            String dest = "invoice_" + invoice.getInvoiceNumber() + ".pdf";
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Estilo de fuente
            float boldSize = 12f;
            float normalSize = 10f;

            // Información del taller
            document.add(new Paragraph()
                .add(new Text("Taller Automotriz Motores & Ruedas").setBold().setFontSize(boldSize))
                .setMarginBottom(5));
            document.add(new Paragraph("NIT: 123456789-0").setFontSize(normalSize));
            document.add(new Paragraph("Dirección: Calle 10 #5-23, Bucaramanga").setFontSize(normalSize));
            document.add(new Paragraph("Teléfono: (57) 300 123 4567").setFontSize(normalSize));
            document.add(new Paragraph("Correo: contacto@motoresyruedas.com").setFontSize(normalSize));
            document.add(new Paragraph("--------------------------------------------------").setFontSize(normalSize));

            // Información de la factura
            document.add(new Paragraph()
                .add(new Text("Factura Electrónica").setBold().setFontSize(boldSize))
                .setMarginTop(10)
                .setMarginBottom(5));
            document.add(new Paragraph("Número: " + invoice.getInvoiceNumber()).setFontSize(normalSize));
            document.add(new Paragraph("Fecha: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(invoice.getIssueDate())).setFontSize(normalSize));
            document.add(new Paragraph("ID Servicio: " + invoice.getServiceId()).setFontSize(normalSize));

            // Información del cliente
            document.add(new Paragraph()
                .add(new Text("Cliente").setBold().setFontSize(boldSize))
                .setMarginTop(10)
                .setMarginBottom(5));
            document.add(new Paragraph("Nombre: " + invoice.getCustomerName()).setFontSize(normalSize));
            document.add(new Paragraph("Documento: " + invoice.getCustomerDocument()).setFontSize(normalSize));
            document.add(new Paragraph("Dirección: " + invoice.getCustomerAddress()).setFontSize(normalSize));

            // Detalle del servicio
            document.add(new Paragraph()
                .add(new Text("Detalle del Servicio").setBold().setFontSize(boldSize))
                .setMarginTop(10)
                .setMarginBottom(5));
            document.add(new Paragraph("Descripción: " + invoice.getServiceDescription()).setFontSize(normalSize));
            document.add(new Paragraph("Cantidad de Repuestos: " + invoice.getPartsQuantity()).setFontSize(normalSize));
            document.add(new Paragraph("Costo de Mano de Obra: $" + invoice.getLaborCost()).setFontSize(normalSize));
            document.add(new Paragraph("Subtotal: $" + invoice.getSubtotal()).setFontSize(normalSize));
            document.add(new Paragraph("Impuestos: $" + invoice.getTaxes()).setFontSize(normalSize));
            document.add(new Paragraph("Total: $" + invoice.getTotal()).setFontSize(normalSize));

            // CUFE (simulado)
            String cufe = "CUFE-" + invoice.getInvoiceNumber() + "-SIMULATED";
            document.add(new Paragraph("CUFE: " + cufe).setFontSize(normalSize).setMarginTop(10));

            // Generar QR Code
            String qrContent = "https://factura-electronica.dian.gov.co/" + cufe;
            ByteArrayOutputStream qrStream = new ByteArrayOutputStream();
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrContent, BarcodeFormat.QR_CODE, 100, 100);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qrStream);
            Image qrImage = new Image(ImageDataFactory.create(qrStream.toByteArray()));
            qrImage.setWidth(100);
            qrImage.setHeight(100);
            document.add(qrImage);

            // Firma digital (simulada como texto)
            document.add(new Paragraph("Firma Digital: Taller Automotriz Motores & Ruedas").setFontSize(normalSize).setMarginTop(10));

            // Cerrar el documento
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage());
        }
    }*/
}