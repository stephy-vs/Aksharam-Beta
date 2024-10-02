package com.example.MuseumTicketing.DTO.AdminScanner;

import lombok.Data;

@Data
public class TotalIncomeDTO {
    private String month; // Nullable type(only for bar graph showing monthly income)
    private double totalPublicIncome;
    private double totalInstitutionIncome;
    private double totalForeignerIncome;
    private double totalIncome;

    public TotalIncomeDTO(String month,double totalPublicIncome, double totalInstitutionIncome, double totalForeignerIncome, double totalIncome) {
        this.month = month;
        this.totalPublicIncome = totalPublicIncome;
        this.totalInstitutionIncome = totalInstitutionIncome;
        this.totalForeignerIncome = totalForeignerIncome;
        this.totalIncome = totalIncome;
    }
    public TotalIncomeDTO(double totalPublicIncome, double totalInstitutionIncome, double totalForeignerIncome, double totalIncome) {
        this.totalPublicIncome = totalPublicIncome;
        this.totalInstitutionIncome = totalInstitutionIncome;
        this.totalForeignerIncome = totalForeignerIncome;
        this.totalIncome = totalIncome;
    }
}
