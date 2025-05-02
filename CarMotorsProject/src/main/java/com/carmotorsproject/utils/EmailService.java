/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.utils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for sending emails
 */
public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private String host;
    private int port;
    private boolean auth;
    private boolean tls;
    private String username;
    private String password;

    /**
     * Constructor with email server configuration
     *
     * @param host SMTP host
     * @param port SMTP port
     * @param auth Whether authentication is required
     * @param tls Whether TLS is enabled
     * @param username SMTP username
     * @param password SMTP password
     */
    public EmailService(String host, int port, boolean auth, boolean tls,
                        String username, String password) {
        this.host = host;
        this.port = port;
        this.auth = auth;
        this.tls = tls;
        this.username = username;
        this.password = password;
    }

    /**
     * Send an email
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param body Email body
     * @return true if email was sent successfully, false otherwise
     */
    public boolean sendEmail(String to, String subject, String body) {
        return sendEmail(to, subject, body, null);
    }

    /**
     * Send an email with attachment
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param body Email body
     * @param attachmentPath Path to attachment file
     * @return true if email was sent successfully, false otherwise
     */
    public boolean sendEmail(String to, String subject, String body, String attachmentPath) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", tls);

        // Create session with authenticator if required
        Session session;
        if (auth) {
            session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        } else {
            session = Session.getInstance(props);
        }

        try {
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Create message parts
            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                // Create multipart message for attachment
                Multipart multipart = new MimeMultipart();

                // Text part
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(body);
                multipart.addBodyPart(textPart);

                // Attachment part
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentPath);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(source.getName());
                multipart.addBodyPart(attachmentPart);

                // Set content
                message.setContent(multipart);
            } else {
                // Simple text message
                message.setText(body);
            }

            // Send message
            Transport.send(message);
            LOGGER.log(Level.INFO, "Email sent successfully to {0}", to);
            return true;

        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Error sending email", e);
            return false;
        }
    }

    /**
     * Send an invoice by email
     *
     * @param to Recipient email address
     * @param customerName Customer name
     * @param invoiceNumber Invoice number
     * @param invoicePath Path to invoice PDF file
     * @return true if email was sent successfully, false otherwise
     */
    public boolean sendInvoiceEmail(String to, String customerName,
                                    String invoiceNumber, String invoicePath) {
        String subject = "Invoice #" + invoiceNumber;
        String body = "Dear " + customerName + ",\n\n"
                + "Please find attached your invoice #" + invoiceNumber + ".\n\n"
                + "Thank you for your business.\n\n"
                + "Best regards,\nCar Motors Team";

        return sendEmail(to, subject, body, invoicePath);
    }
}