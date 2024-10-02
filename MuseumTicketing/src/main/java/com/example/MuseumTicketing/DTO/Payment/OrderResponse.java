package com.example.MuseumTicketing.DTO.Payment;

import lombok.Data;

@Data
public class OrderResponse {
    private String razorpayKeyId;
    private double amount;
    private String currency;
    private String orderId;
    private String sessionId;


    public OrderResponse(String razorpayKeyId, double amount, String currency, String orderId, String sessionId) {
        this.razorpayKeyId = razorpayKeyId;
        this.amount = amount;
        this.currency = currency;
        this.orderId = orderId;
        this.sessionId = sessionId;
    }
}