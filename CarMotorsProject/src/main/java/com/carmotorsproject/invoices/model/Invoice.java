/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.invoices.model;
import java.util.Date;

public class Invoice {
    private int invoiceId;
    private int serviceId;
    private Date issueDate;
    private String invoiceNumber;
    private double subtotal;
    private double taxes;
    private double total;
    private String electronicInvoiceId;
    private String qrCode;
    private String customerName;
    private String customerDocument;
    private String customerAddress;
    private String serviceDescription;
    private int partsQuantity;
    private double laborCost;

    public Invoice() {}

    public Invoice(int invoiceId, int serviceId, Date issueDate, String invoiceNumber, double subtotal,
                   double taxes, double total, String electronicInvoiceId, String qrCode,
                   String customerName, String customerDocument, String customerAddress,
                   String serviceDescription, int partsQuantity, double laborCost) {
        this.invoiceId = invoiceId;
        this.serviceId = serviceId;
        this.issueDate = issueDate;
        this.invoiceNumber = invoiceNumber;
        this.subtotal = subtotal;
        this.taxes = taxes;
        this.total = total;
        this.electronicInvoiceId = electronicInvoiceId;
        this.qrCode = qrCode;
        this.customerName = customerName;
        this.customerDocument = customerDocument;
        this.customerAddress = customerAddress;
        this.serviceDescription = serviceDescription;
        this.partsQuantity = partsQuantity;
        this.laborCost = laborCost;
    }

    // Getters and setters
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getTaxes() { return taxes; }
    public void setTaxes(double taxes) { this.taxes = taxes; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getElectronicInvoiceId() { return electronicInvoiceId; }
    public void setElectronicInvoiceId(String electronicInvoiceId) { this.electronicInvoiceId = electronicInvoiceId; }
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerDocument() { return customerDocument; }
    public void setCustomerDocument(String customerDocument) { this.customerDocument = customerDocument; }
    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }
    public String getServiceDescription() { return serviceDescription; }
    public void setServiceDescription(String serviceDescription) { this.serviceDescription = serviceDescription; }
    public int getPartsQuantity() { return partsQuantity; }
    public void setPartsQuantity(int partsQuantity) { this.partsQuantity = partsQuantity; }
    public double getLaborCost() { return laborCost; }
    public void setLaborCost(double laborCost) { this.laborCost = laborCost; }
}