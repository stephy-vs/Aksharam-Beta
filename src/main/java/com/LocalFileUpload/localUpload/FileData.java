package com.LocalFileUpload.localUpload;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "_video_data_file")
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "file_name")
    private String file_name;
}
