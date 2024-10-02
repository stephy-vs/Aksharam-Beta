package com.example.MuseumTicketing.DTO.AdminScanner;

import lombok.Data;

@Data
public class UpdateScannerPasswordRequest {

    private String employeeId;
    private String employeeName;
    private String newPassword;
    private String confirmPassword;
}
