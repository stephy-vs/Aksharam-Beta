package com.example.MuseumTicketing.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "_scanned_details")
public class ScannedDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ticketId;

    private LocalDateTime scannedTime;

    private String name;

    private String type;

}
