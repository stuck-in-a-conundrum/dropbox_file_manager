package com.dropbox.service;
import com.dropbox.model.FileMetadata;
import com.dropbox.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final FileMetadataRepository fileMetadataRepository;

    @Autowired
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir, FileMetadataRepository fileMetadataRepository) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.fileMetadataRepository = fileMetadataRepository;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public FileMetadata storeFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        if(targetLocation.toFile().exists()) {
            throw new FileAlreadyExistsException(fileName);
        }
        Files.copy(file.getInputStream(), targetLocation);

        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(fileName);
        metadata.setFileType(file.getContentType());
        metadata.setSize(file.getSize());
        metadata.setStoragePath(targetLocation.toString());

        return fileMetadataRepository.save(metadata);
    }

    public Optional<FileMetadata> getFileMetadata(Long fileId) {
        return fileMetadataRepository.findById(fileId);
    }

    public Path getFilePath(FileMetadata metadata) {
        return Paths.get(metadata.getStoragePath());
    }

    public List<FileMetadata> getAllFiles() {
        return fileMetadataRepository.findAll();
    }
}