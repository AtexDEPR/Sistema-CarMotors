package com.carmotorsproject.utils;

import com.carmotorsproject.config.AppConfig;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sends emails with invoice PDFs as attachments using javax.mail.
 */
public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    /**
     * Sends an email with an invoice PDF attachment.
     *
     * @param recipient The email recipient
     * @param subject The email subject
     * @param body The email body
     * @param pdfPath The path to the PDF file to attach
     * @return True if the email was sent successfully, false otherwise
     */
    public boolean sendInvoiceEmail(String recipient, String subject, String body, String pdfPath) {
        LOGGER.log(Level.INFO, "Sending invoice email to: {0}", recipient);

        // Get email configuration from AppConfig
        String host = AppConfig.getEmailHost();
        int port = AppConfig.getEmailPort();
        final String username = AppConfig.getEmailUsername();
        final String password = AppConfig.getEmailPassword();
        boolean authRequired = AppConfig.isEmailAuthRequired();
        boolean tlsEnabled = AppConfig.isEmailTlsEnabled();

        // Set mail properties
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        if (authRequired) {
            props.put("mail.smtp.auth", "true");
        }

        if (tlsEnabled) {
            props.put("mail.smtp.starttls.enable", "true");
        }

        try {
            // Create session with authenticator if required
            Session session;
            if (authRequired) {
                session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
            } else {
                session = Session.getInstance(props);
            }

            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            // Create multipart message
            Multipart multipart = new MimeMultipart();

            // Text part
            BodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            // Attachment part
            BodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(pdfPath);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName("factura.pdf");
            multipart.addBodyPart(attachmentPart);

            // Set content
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            LOGGER.log(Level.INFO, "Email sent successfully to: {0}", recipient);
            return true;
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Error sending email", e);
            return false;
        }
    }

    /**
     * Sends a notification email without attachments.
     *
     * @param recipient The email recipient
     * @param subject The email subject
     * @param body The email body
     * @return True if the email was sent successfully, false otherwise
     */
    public boolean sendNotificationEmail(String recipient, String subject, String body) {
        LOGGER.log(Level.INFO, "Sending notification email to: {0}", recipient);

        // Get email configuration from AppConfig
        String host = AppConfig.getEmailHost();
        int port = AppConfig.getEmailPort();
        final String username = AppConfig.getEmailUsername();
        final String password = AppConfig.getEmailPassword();
        boolean authRequired = AppConfig.isEmailAuthRequired();
        boolean tlsEnabled = AppConfig.isEmailTlsEnabled();

        // Set mail properties
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        if (authRequired) {
            props.put("mail.smtp.auth", "true");
        }

        if (tlsEnabled) {
            props.put("mail.smtp.starttls.enable", "true");
        }

        try {
            // Create session with authenticator if required
            Session session;
            if (authRequired) {
                session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
            } else {
                session = Session.getInstance(props);
            }

            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            // Send message
            Transport.send(message);

            LOGGER.log(Level.INFO, "Notification email sent successfully to: {0}", recipient);
            return true;
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Error sending notification email", e);
            return false;
        }
    }

    /**
     * Tests the email configuration by sending a test email.
     *
     * @param testRecipient The recipient for the test email
     * @return True if the test email was sent successfully, false otherwise
     */
    public boolean testEmailConfiguration(String testRecipient) {
        LOGGER.info("Testing email configuration");

        String subject = "Test Email from Car Motors Workshop";
        String body = "This is a test email from the Car Motors Workshop application.\n\n" +
                "If you received this email, the email configuration is working correctly.";

        return sendNotificationEmail(testRecipient, subject, body);
    }

    public void sendEmailWithAttachment(String email, String subject, String body, File pdfFile) {

    }
}