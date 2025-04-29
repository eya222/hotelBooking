package com.example.demo.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // Changed here
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.business.services.RoomService;
import com.example.demo.business.services.FilesStorageService;
import com.example.demo.web.dto.ResponseMessage;

@Controller
@RequestMapping("/api/storage")
@CrossOrigin("http://localhost:4200")
public class FilesStorageController {

  @Autowired
  private FilesStorageService storageService;

  @Autowired
  private RoomService roomService;

  @PostMapping("/upload/{id}") // Changed from @PatchMapping to @PostMapping
  public ResponseEntity<ResponseMessage> uploadFile(
      @PathVariable(name = "id") Long id,
      @RequestParam("file") MultipartFile file) {
      
    String message = "";
    try {
      String filename = storageService.save(file);
      
      // Update the room image
      roomService.updateRoomImage(id, filename);

      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }
  }

  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<?> getFile(@PathVariable(name = "filename") String filename) {
    try {
      Resource file = storageService.load(filename);
      return ResponseEntity.ok().body(file);
    } catch (Exception e) {
      String message = "Could not get the file: " + filename;
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }
  }
}
