package com.example.demo.business.servicesImpl;

import java.io.IOException;

import java.net.URI;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.business.services.FilesStorageService;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

  // Root directory for file storage
  private final Path root = Paths.get("uploads");

  // Initialize the file storage directory
  @Override
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  // Save a file to the storage directory
  @Override
  public String save(MultipartFile file) {
    String uniqueFilename = "";
    try {
      // Generate a unique filename using UUID
      uniqueFilename = generateUniqueFilename(file);

      // Copy the file to the storage directory
      Files.copy(file.getInputStream(), this.root.resolve(uniqueFilename));
      // Simulates a 2-second delay before completing tasks or responses.
      Thread.sleep(2000);
      return uniqueFilename;
    } catch (FileAlreadyExistsException e) {
      throw new RuntimeException("A file with the same name already exists.");
    } catch (IOException e) {
      throw new RuntimeException("Failed to save the file: " + e.getMessage());
    } catch (InterruptedException e) {
      throw new RuntimeException("Failed to save the file: " + e.getMessage());
    }
  }

  // Generate a unique filename based on the original filename and UUID
  private String generateUniqueFilename(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    String extension = "";
    // Get the file extension
    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    // Generate a UUID and combine it with the file extension
    return UUID.randomUUID().toString() + extension;
  }

 
 /**
 * Loads a file resource from the storage directory.
 *
 * @param filename The name of the file to load.
 * @return The loaded file resource.
 * @throws RuntimeException If an error occurs while loading the file.
 */
@Override
public Resource load(String filename) {
    try {
        // Resolve the file path
        Path file = root.resolve(filename);
        
        // Convert file path to URI
        URI uri = file.toUri();
        
        // Check if URI is not null
        if (uri != null) {
            // Create a new UrlResource object using the URI
            Resource resource = new UrlResource(uri);
            
            // Check if the resource exists and is readable
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                // Throw an exception if the resource cannot be read
                throw new IOException("Could not read the file!");
            }
        } else {
            // Throw an exception if the URI is null
            throw new RuntimeException("URI for the file is null!");
        }
    } catch (IOException e) {
        // Throw an exception if an error occurs while loading the file
        throw new RuntimeException("Error reading the file: " + e.getMessage());
    }
}

  // Delete a file from the storage directory
  @Override
  public void delete(String filename) {
    try {
      Path file = root.resolve(filename);
      Files.delete(file);
    } catch (IOException e) {

      throw new RuntimeException("Could not delete the file: " + e.getMessage());
    }
  }

}
