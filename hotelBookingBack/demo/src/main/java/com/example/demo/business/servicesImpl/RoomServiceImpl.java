package com.example.demo.business.servicesImpl;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.stereotype.Service;

import com.example.demo.business.services.RoomService;
import com.example.demo.business.services.FilesStorageService;
import com.example.demo.dao.entities.Room;
import com.example.demo.dao.repositories.RoomRepository;
import com.example.demo.exceptions.DuplicateRoomException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class RoomServiceImpl implements RoomService {

  
    private final RoomRepository roomRepository;
    private final FilesStorageService filesStorageService;

    public RoomServiceImpl(RoomRepository roomRepository,
                              FilesStorageService filesStorageService) {
        this.roomRepository = roomRepository;
        this.filesStorageService = filesStorageService;
    }


    // Sort contacts by name in ascending alphabetical order
    @Override
    public List<Room> getAllRooms() {
        return this.roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Long id) {
        // Check if the ID is null and throw an IllegalArgumentException if it is
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        // Retrieve the contact by ID, throw an EntityNotFoundException if not found
        return this.roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room with id: " + id + " not found"));
    }

    // Add method
    @Override
    public Room addRoom(Room room) throws DuplicateRoomException {
        // Check if the contact is null and throw an IllegalArgumentException if it is
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        try {
            // Save the contact in the repository
            return roomRepository.save(room);
        } catch (DataIntegrityViolationException e) {
            // Handle uniqueness constraint violations
            throw new DuplicateRoomException(
                    "A Room with the same number or other unique field already exists.");
        }
    }
   // Update method
   @Override
public Room updateRoom(Long id, Room updatedRoom) throws DuplicateRoomException {
    if (id == null || updatedRoom == null) {
        throw new IllegalArgumentException("ID or Room cannot be null");
    }

    // Fetch the existing Room from the database
    Room existingRoom = getRoomById(id);

    // Update the fields manually
    existingRoom.setCategory(updatedRoom.getCategory());
    existingRoom.setRoomType(updatedRoom.getRoomType());
    existingRoom.setNoOfGuests(updatedRoom.getNoOfGuests());
    existingRoom.setPrice(updatedRoom.getPrice());
    existingRoom.setImage(updatedRoom.getImage());

    // We DO NOT change the roomNumber (primary key!)

    try {
        return roomRepository.save(existingRoom);
    } catch (DataIntegrityViolationException e) {
        throw new DuplicateRoomException(
                "A Room with the same number or other unique field already exists.");
    }
}



    @Override
    @Transactional
    // the deleteContact method executes all its operations (checking for the contact, deleting the file, 
    //and deleting the contact record) within a single transaction.If any part of this process fails, 
    //the entire transaction will be rolled back, maintaining data consistency and integrity. 
    public void deleteRoom(Long id) {
        // Check if the ID is null and throw an IllegalArgumentException if it is
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try {
            // Retrieve the contact by ID
            Room room = this.getRoomById(id);
            // Get the image filename associated with the contact
            String filename = room.getImage();
            // If the contact has an image, delete it
            if (filename != null) {
                filesStorageService.delete(filename);
            }
            // Delete the contact from the repository by ID
            roomRepository.deleteById(id);
        } catch (DataAccessException e) {
            // Capture any data access exceptions (e.g., foreign key constraint violations)
            throw new RuntimeException("Failed to delete Room with id: " + id, e);
        }
    }
    @Override
    public Room updateRoomImage(Long id, String filename) {
        // Check if the ID is null and throw an IllegalArgumentException if it is
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
      
        // Retrieve the contact by ID, throw an EntityNotFoundException if the contact
        // is not found
        Room room = getRoomById(id);

        // Check if the contact already has an image
        if (room.getImage() == null) {
            // If the contact does not have an image, set the new image
            room.setImage(filename);
        } else {
            // If the contact already has an image, delete the old image
            this.filesStorageService.delete(room.getImage());
            // Set the new image
            room.setImage(filename);
        }
        // Save and return the updated contact in the repository
        return roomRepository.save(room);
    }
}

