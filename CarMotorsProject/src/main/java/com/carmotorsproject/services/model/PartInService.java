package com.carmotorsproject.services.model;

import com.carmotorsproject.parts.model.Part;

/**
 * Represents a part used in a service.
 * This class tracks the relationship between services and parts,
 * including quantity and pricing information.
 */
public class PartInService {

    private int partsInServiceId;
    private int serviceId;
    private int partId;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private Part part;


    /**
     * Default constructor.
     */
    public PartInService() {
    }



    /**
     * Constructor with parameters.
     *
     * @param serviceId The ID of the service
     * @param partId The ID of the part
     * @param quantity The quantity of the part used
     * @param unitPrice The unit price of the part
     */
    public PartInService(int serviceId, int partId, int quantity, double unitPrice) {
        this.serviceId = serviceId;
        this.partId = partId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = quantity * unitPrice;
    }

    /**
     * Gets the ID of the parts in service record.
     *
     * @return The parts in service ID
     */
    public int getPartsInServiceId() {
        return partsInServiceId;
    }

    /**
     * Sets the ID of the parts in service record.
     *
     * @param partsInServiceId The parts in service ID to set
     */
    public void setPartsInServiceId(int partsInServiceId) {
        this.partsInServiceId = partsInServiceId;
    }

    /**
     * Gets the ID of the service.
     *
     * @return The service ID
     */
    public int getServiceId() {
        return serviceId;
    }

    /**
     * Sets the ID of the service.
     *
     * @param serviceId The service ID to set
     */
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * Gets the ID of the part.
     *
     * @return The part ID
     */
    public int getPartId() {
        return partId;
    }

    /**
     * Sets the ID of the part.
     *
     * @param partId The part ID to set
     */
    public void setPartId(int partId) {
        this.partId = partId;
    }

    /**
     * Gets the quantity of the part used.
     *
     * @return The quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the part used.
     *
     * @param quantity The quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        // Update total price when quantity changes
        if (this.unitPrice > 0) {
            this.totalPrice = this.quantity * this.unitPrice;
        }
    }


    // ... Constructor(es) ...
    public PartInService(Part part, int quantity) {
        this.part = part;
        this.quantity = quantity;
    }



    public Part getPart() {
        return part;
    }


    // --------------------------------------------------------------------

    // ... Setters si son necesarios ...
    public void setPart(Part part) {
        this.part = part;
    }



    /**
     * Gets the unit price of the part.
     *
     * @return The unit price
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price of the part.
     *
     * @param unitPrice The unit price to set
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        // Update total price when unit price changes
        if (this.quantity > 0) {
            this.totalPrice = this.quantity * this.unitPrice;
        }
    }

    /**
     * Gets the total price of the parts used (quantity * unit price).
     *
     * @return The total price
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price of the parts used.
     *
     * @param totalPrice The total price to set
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "PartInService{" +
                "partsInServiceId=" + partsInServiceId +
                ", serviceId=" + serviceId +
                ", partId=" + partId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
}