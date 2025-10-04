package com.dropbox.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    private long size;
    private String storagePath;
}