package com.example.MuseumTicketing.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "_pricing")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type;

    private int price;

    private String category;

}
