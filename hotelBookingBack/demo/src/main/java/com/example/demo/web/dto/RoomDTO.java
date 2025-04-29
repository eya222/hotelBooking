package com.example.demo.web.dto;
import com.example.demo.dao.entities.Room;

import lombok.Builder;

@Builder
public record RoomDTO(
    long roomNumber,
    String category,
    String roomType,
    int noOfGuests,
    double price,
    String image
    ) {
  public static RoomDTO toRoomDTO(Room room) {
    RoomDTO roomDTO = RoomDTO.builder()
        .roomNumber(room.getRoomNumber())
        .category(room.getCategory())
        .roomType(room.getRoomType())
        .noOfGuests(room.getNoOfGuests())
        .price(room.getPrice())
        .image(room.getImage()).build();
    return roomDTO;
    
  }

  public static Room fromRoomDTO(RoomDTO roomDTO) {
    Room room = Room.builder()
        .roomNumber(roomDTO.roomNumber)
        .category(roomDTO.category)
        .roomType(roomDTO.roomType)
        .noOfGuests(roomDTO.noOfGuests)
        .price(roomDTO.price)
        .image(roomDTO.image).build();
    return room;
    
  }
}
