/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;

import java.util.Date;

/**
 * Represents a supplier performance evaluation.
 * Stores ratings for different aspects of supplier performance and calculates a total rating.
 */
public class SupplierEvaluation implements Identifiable {
    private int evaluationId;
    private int supplierId;
    private Date evaluationDate;
    private int deliveryRating;      // 1-5 scale
    private int qualityRating;       // 1-5 scale
    private int priceRating;         // 1-5 scale
    private int communicationRating; // 1-5 scale
    private double totalRating;      // Computed average of all ratings
    private String comments;

    /**
     * Default constructor.
     */
    public SupplierEvaluation() {
        this.evaluationDate = new Date();
    }

    /**
     * Constructor with all fields except evaluationId and totalRating.
     * The totalRating is computed automatically.
     *
     * @param supplierId The ID of the supplier being evaluated
     * @param evaluationDate The date of the evaluation
     * @param deliveryRating Rating for delivery performance (1-5)
     * @param qualityRating Rating for product quality (1-5)
     * @param priceRating Rating for price competitiveness (1-5)
     * @param communicationRating Rating for communication effectiveness (1-5)
     * @param comments Additional comments about the evaluation
     */
    public SupplierEvaluation(int supplierId, Date evaluationDate, int deliveryRating,
                              int qualityRating, int priceRating, int communicationRating,
                              String comments) {
        this.supplierId = supplierId;
        this.evaluationDate = evaluationDate;
        setDeliveryRating(deliveryRating);
        setQualityRating(qualityRating);
        setPriceRating(priceRating);
        setCommunicationRating(communicationRating);
        this.comments = comments;
        computeTotalRating();
    }

    /**
     * Computes the total rating as the average of all individual ratings.
     */
    public void computeTotalRating() {
        this.totalRating = (deliveryRating + qualityRating + priceRating + communicationRating) / 4.0;
    }

    /**
     * Validates that a rating is between 1 and 5.
     *
     * @param rating The rating to validate
     * @param ratingName The name of the rating for error message
     * @throws IllegalArgumentException If the rating is not between 1 and 5
     */
    private void validateRating(int rating, String ratingName) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(ratingName + " must be between 1 and 5");
        }
    }

    @Override
    public int getId() {
        return evaluationId;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public int getDeliveryRating() {
        return deliveryRating;
    }

    public void setDeliveryRating(int deliveryRating) {
        validateRating(deliveryRating, "Delivery rating");
        this.deliveryRating = deliveryRating;
        computeTotalRating();
    }

    public int getQualityRating() {
        return qualityRating;
    }

    public void setQualityRating(int qualityRating) {
        validateRating(qualityRating, "Quality rating");
        this.qualityRating = qualityRating;
        computeTotalRating();
    }

    public int getPriceRating() {
        return priceRating;
    }

    public void setPriceRating(int priceRating) {
        validateRating(priceRating, "Price rating");
        this.priceRating = priceRating;
        computeTotalRating();
    }

    public int getCommunicationRating() {
        return communicationRating;
    }

    public void setCommunicationRating(int communicationRating) {
        validateRating(communicationRating, "Communication rating");
        this.communicationRating = communicationRating;
        computeTotalRating();
    }

    public double getTotalRating() {
        return totalRating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "SupplierEvaluation{" +
                "evaluationId=" + evaluationId +
                ", supplierId=" + supplierId +
                ", evaluationDate=" + evaluationDate +
                ", totalRating=" + totalRating +
                '}';
    }
}