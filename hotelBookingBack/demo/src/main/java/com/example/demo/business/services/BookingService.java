package com.example.demo.business.services;
import com.example.demo.dao.entities.Booking;
import com.example.demo.dao.entities.Room;
import java.util.List;
import java.time.LocalDate;

public interface BookingService {
    public List<Booking> getAllBookings();
    public Booking getBookingById(Long id);
    public Booking addBooking(Booking booking);
    public Booking updateBooking(Long id, Booking booking);
    public void deleteBooking(Long id);
    boolean isRoomAvailable(Long roomNumber, LocalDate checkin, LocalDate checkout);
    Booking bookRoom(Long roomNumber, String username, LocalDate checkin, LocalDate checkout);
    List<Room> findAvailableRooms(LocalDate checkin, LocalDate checkout, Double maxPrice, Integer noOfGuests);
    public Booking confirmBooking(Long id) ;
    public List<Booking> getBookingByName(String name);
}
    
    

