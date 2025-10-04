package com.dropbox.controller;

import com.dropbox.model.FileMetadata;
import com.dropbox.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin("*") // Allows requests from React app
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<FileMetadata> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileMetadata metadata = fileStorageService.storeFile(file);
            return ResponseEntity.ok(metadata);
        } catch(FileAlreadyExistsException e) {
            return ResponseEntity.status(409).build(); 
        }catch (IOException e) {
            System.out.println(e);
            return ResponseEntity.status(500).build();
        } 
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileMetadata>> getListOfFiles() {
        List<FileMetadata> files = fileStorageService.getAllFiles();
        return ResponseEntity.ok(files);
    }

    
    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        // First, find the file metadata from the database
        Optional<FileMetadata> fileMetadataOptional = fileStorageService.getFileMetadata(id);

        // If no metadata is found, return a 404 Not Found response immediately
        if (!fileMetadataOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        FileMetadata metadata = fileMetadataOptional.get();
        
        try {
            // Get the file path and create a resource from it
            Path filePath = fileStorageService.getFilePath(metadata);
            Resource resource = new UrlResource(filePath.toUri());

            // Check if the file exists and is readable
            if (resource.exists() && resource.isReadable()) {
                // If it's all good, return the file with the correct headers
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(metadata.getFileType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getFileName() + "\"")
                        .body(resource);
            } else {
                // If the file doesn't exist on the filesystem (though metadata exists), it's a server error
                throw new RuntimeException("Could not read the file: " + metadata.getFileName());
            }
        } catch (MalformedURLException e) {
            // If the file path is somehow invalid, it's a server error
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
