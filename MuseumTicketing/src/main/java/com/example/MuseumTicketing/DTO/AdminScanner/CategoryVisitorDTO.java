package com.example.MuseumTicketing.DTO.AdminScanner;

import lombok.Data;

@Data
public class CategoryVisitorDTO {

        private String category;
        private int totalVisitors;

    public CategoryVisitorDTO(String category, int totalVisitors) {
        this.category = category;
        this.totalVisitors = totalVisitors;
    }


}

