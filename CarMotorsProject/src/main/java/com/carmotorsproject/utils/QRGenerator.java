package com.carmotorsproject.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates QR codes for invoices using the ZXing library.
 */
public class QRGenerator {
    private static final Logger LOGGER = Logger.getLogger(QRGenerator.class.getName());

    /**
     * Generates a QR code as a BufferedImage.
     *
     * @param data The data to encode in the QR code
     * @param width The width of the QR code image
     * @param height The height of the QR code image
     * @return A BufferedImage containing the QR code
     * @throws RuntimeException If there is an error generating the QR code
     */
    public BufferedImage generateQRCode(String data, int width, int height) {
        LOGGER.log(Level.INFO, "Generating QR code with data length: {0}", data.length());

        try {
            // Set QR code parameters
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // High error correction
            hints.put(EncodeHintType.MARGIN, 2); // Margin (quiet zone)
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Create QR code writer
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hints);

            // Convert to BufferedImage
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            LOGGER.info("QR code generated successfully");
            return qrImage;
        } catch (WriterException e) {
            LOGGER.log(Level.SEVERE, "Error generating QR code", e);
            throw new RuntimeException("Error generating QR code", e);
        }
    }

    /**
     * Generates a QR code with DIAN-specific data for electronic invoices.
     *
     * @param invoiceNumber The invoice number
     * @param cufe The CUFE (Código Único de Facturación Electrónica)
     * @param nit The company's tax ID (NIT)
     * @param issueDate The invoice issue date
     * @param total The invoice total
     * @param width The width of the QR code image
     * @param height The height of the QR code image
     * @return A BufferedImage containing the QR code with DIAN-specific data
     * @throws RuntimeException If there is an error generating the QR code
     */
    public BufferedImage generateInvoiceQRCode(String invoiceNumber, String cufe, String nit,
                                               String issueDate, double total, int width, int height) {
        // Format the data according to DIAN requirements
        String qrData = "NumFac:" + invoiceNumber + "|" +
                "CUFE:" + cufe + "|" +
                "NitFac:" + nit + "|" +
                "FecFac:" + issueDate + "|" +
                "ValFac:" + String.format("%.2f", total);

        LOGGER.log(Level.INFO, "Generating invoice QR code for invoice: {0}", invoiceNumber);
        return generateQRCode(qrData, width, height);
    }

    /**
     * Converts a BufferedImage to a byte array.
     *
     * @param image The BufferedImage to convert
     * @return A byte array containing the image data
     * @throws IOException If an I/O error occurs
     */
    public static byte[] bufferedImageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}