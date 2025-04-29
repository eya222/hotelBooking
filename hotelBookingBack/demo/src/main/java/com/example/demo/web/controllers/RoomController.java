package com.example.demo.web.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.business.services.RoomService;
import com.example.demo.dao.entities.Room;
import com.example.demo.exceptions.DuplicateRoomException;
import com.example.demo.web.dto.RoomDTO;

import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/rooms")

public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllCustomers() {
        List<RoomDTO> rooms= this.roomService.getAllRooms()
                .stream()
                .map(RoomDTO::toRoomDTO)
                // .map(contact->ContactSummaryDTO.toContactSummaryDTO(contact))
                .collect(Collectors.toList());
              
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable(name="id") Long id) {
        RoomDTO room = RoomDTO.toRoomDTO(this.roomService.getRoomById(id));
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> addRoom(@RequestBody RoomDTO roomDTO) throws DuplicateRoomException  {
       
        Room room = RoomDTO.fromRoomDTO(roomDTO);
        return new ResponseEntity<>(this.roomService.addRoom(room), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable(name="id") Long id, @RequestBody RoomDTO roomDTO) throws DuplicateRoomException {
        Room room = RoomDTO.fromRoomDTO(roomDTO);
        return new ResponseEntity<>(this.roomService.updateRoom(id, room), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable(name="id") Long id) {
        this.roomService.deleteRoom(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);

    }

}
 