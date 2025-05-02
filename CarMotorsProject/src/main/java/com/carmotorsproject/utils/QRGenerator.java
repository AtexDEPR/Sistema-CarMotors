/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for generating QR codes
 */
public class QRGenerator {
    private static final Logger LOGGER = Logger.getLogger(QRGenerator.class.getName());

    /**
     * Generate a QR code image
     *
     * @param text Text to encode in the QR code
     * @param width Width of the QR code
     * @param height Height of the QR code
     * @param filePath Output file path
     * @return true if QR code was generated successfully, false otherwise
     */
    public static boolean generateQRCode(String text, int width, int height, String filePath) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            LOGGER.log(Level.INFO, "QR code generated successfully: {0}", filePath);
            return true;

        } catch (WriterException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error generating QR code", e);
            return false;
        }
    }

    /**
     * Generate a QR code for an invoice
     *
     * @param invoiceNumber Invoice number
     * @param customerName Customer name
     * @param total Total amount
     * @param date Invoice date
     * @param filePath Output file path
     * @return true if QR code was generated successfully, false otherwise
     */
    public static boolean generateInvoiceQRCode(String invoiceNumber, String customerName,
                                                double total, String date, String filePath) {
        // Create a string with invoice information
        String qrContent = String.format("INVOICE:%s\nCUSTOMER:%s\nTOTAL:$%.2f\nDATE:%s",
                invoiceNumber, customerName, total, date);

        // Generate QR code with this information
        return generateQRCode(qrContent, 200, 200, filePath);
    }
}