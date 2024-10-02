package com.example.MuseumTicketing.DTO.AdminScanner;

import lombok.Data;

@Data
public class SignUpRequest {

    private String name;
    private String employeeId;
    private String password;
    private String image;
    private String email;
    private String phoneNo;
    private String tempAddress;
    private String permAddress;
}
