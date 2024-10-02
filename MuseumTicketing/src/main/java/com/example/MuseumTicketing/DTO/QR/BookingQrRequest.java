package com.example.MuseumTicketing.DTO.QR;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingQrRequest {

    private String type;
    private String institutionName;
    private String name;
    private int numberOfAdults;
    private int numberOfChildren;
    private LocalDate visitDate;
    private String paymentid;
    private String email;
    private int numberOfStudents;
    private int numberOfTeachers;
    private int numberOfSeniors;
    private String ticketId;
    private byte[] pdfData;
}