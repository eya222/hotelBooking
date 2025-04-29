package com.example.demo.dao.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.dao.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
        
}
