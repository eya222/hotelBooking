package com.example.demo.dao.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.dao.entities.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRoomNumber(Long roomNumber);
    List<Booking> findByUsernameContainingIgnoreCase(String userName);


        
}
