package com.example.MuseumTicketing.DTO.AdminScanner;

import lombok.Data;

@Data
public class SignInRequest {

    private String employeeId;

    private String password;
}
