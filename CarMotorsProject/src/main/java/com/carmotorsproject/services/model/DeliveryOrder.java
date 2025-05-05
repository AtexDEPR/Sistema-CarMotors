/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import com.carmotorsproject.parts.model.Identifiable;

import java.util.Date;

/**
 * Represents a vehicle delivery order.
 * Maps to the 'delivery_orders' table in the database.
 */
public class DeliveryOrder implements Identifiable {

    public void setCustomerSatisfaction(com.carmotorsproject.services.model.CustomerSatisfaction satisfaction) {
    }

    public enum CustomerSatisfaction {
        VERY_SATISFIED,
        SATISFIED,
        NEUTRAL,
        UNSATISFIED,
        VERY_UNSATISFIED
    }

    private int deliveryOrderId;
    private int serviceId;
    private Date deliveryDate;
    private String deliveredBy;
    private String receivedBy;
    private CustomerSatisfaction customerSatisfaction;
    private String customerFeedback;
    private boolean followUpRequired;
    private String notes;

    /**
     * Default constructor.
     */
    public DeliveryOrder() {
    }

    /**
     * Constructor with essential fields.
     *
     * @param serviceId The ID of the service
     * @param deliveryDate The delivery date
     * @param deliveredBy The name of the person who delivered the vehicle
     * @param receivedBy The name of the person who received the vehicle
     */
    public DeliveryOrder(int serviceId, Date deliveryDate, String deliveredBy, String receivedBy) {
        this.serviceId = serviceId;
        this.deliveryDate = deliveryDate;
        this.deliveredBy = deliveredBy;
        this.receivedBy = receivedBy;
    }

    /**
     * Gets the unique identifier of the delivery order.
     *
     * @return The delivery order ID
     */
    @Override
    public int getId() {
        return deliveryOrderId;
    }

    /**
     * Gets the delivery order ID.
     *
     * @return The delivery order ID
     */
    public int getDeliveryOrderId() {
        return deliveryOrderId;
    }

    /**
     * Sets the delivery order ID.
     *
     * @param deliveryOrderId The delivery order ID to set
     */
    public void setDeliveryOrderId(int deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    /**
     * Gets the service ID.
     *
     * @return The service ID
     */
    public int getServiceId() {
        return serviceId;
    }

    /**
     * Sets the service ID.
     *
     * @param serviceId The service ID to set
     */
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * Gets the delivery date.
     *
     * @return The delivery date
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Sets the delivery date.
     *
     * @param deliveryDate The delivery date to set
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * Gets the name of the person who delivered the vehicle.
     *
     * @return The name of the person who delivered the vehicle
     */
    public String getDeliveredBy() {
        return deliveredBy;
    }

    /**
     * Sets the name of the person who delivered the vehicle.
     *
     * @param deliveredBy The name of the person who delivered the vehicle to set
     */
    public void setDeliveredBy(String deliveredBy) {
        this.deliveredBy = deliveredBy;
    }

    /**
     * Gets the name of the person who received the vehicle.
     *
     * @return The name of the person who received the vehicle
     */
    public String getReceivedBy() {
        return receivedBy;
    }

    /**
     * Sets the name of the person who received the vehicle.
     *
     * @param receivedBy The name of the person who received the vehicle to set
     */
    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    /**
     * Gets the customer satisfaction level.
     *
     * @return The customer satisfaction level
     */
    public CustomerSatisfaction getCustomerSatisfaction() {
        return customerSatisfaction;
    }

    /**
     * Sets the customer satisfaction level.
     *
     * @param customerSatisfaction The customer satisfaction level to set
     */
    public void setCustomerSatisfaction(CustomerSatisfaction customerSatisfaction) {
        this.customerSatisfaction = customerSatisfaction;
    }

    /**
     * Gets the customer feedback.
     *
     * @return The customer feedback
     */
    public String getCustomerFeedback() {
        return customerFeedback;
    }

    /**
     * Sets the customer feedback.
     *
     * @param customerFeedback The customer feedback to set
     */
    public void setCustomerFeedback(String customerFeedback) {
        this.customerFeedback = customerFeedback;
    }

    /**
     * Checks if follow-up is required.
     *
     * @return true if follow-up is required, false otherwise
     */
    public boolean isFollowUpRequired() {
        return followUpRequired;
    }

    /**
     * Sets whether follow-up is required.
     *
     * @param followUpRequired true if follow-up is required, false otherwise
     */
    public void setFollowUpRequired(boolean followUpRequired) {
        this.followUpRequired = followUpRequired;
    }

    /**
     * Gets the notes.
     *
     * @return The notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes.
     *
     * @param notes The notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "deliveryOrderId=" + deliveryOrderId +
                ", serviceId=" + serviceId +
                ", deliveryDate=" + deliveryDate +
                ", receivedBy='" + receivedBy + '\'' +
                ", customerSatisfaction=" + customerSatisfaction +
                '}';
    }
}