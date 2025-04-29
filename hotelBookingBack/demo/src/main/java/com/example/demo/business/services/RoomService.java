package com.example.demo.business.services;
import com.example.demo.dao.entities.Room;
import java.util.List;
import com.example.demo.exceptions.DuplicateRoomException;

public interface RoomService {

    public List<Room> getAllRooms();
    public Room getRoomById(Long id);
    public Room addRoom(Room  room) throws DuplicateRoomException;
    // Update method
    public Room updateRoom(Long id,Room room) throws DuplicateRoomException;

    public void deleteRoom(Long id);   
    public Room updateRoomImage(Long id,String filename);
}
