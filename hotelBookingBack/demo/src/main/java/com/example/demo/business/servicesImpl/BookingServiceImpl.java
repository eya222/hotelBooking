package com.example.demo.business.servicesImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.example.demo.business.services.BookingService;
import com.example.demo.business.services.FilesStorageService;
import com.example.demo.dao.entities.Booking;
import com.example.demo.dao.entities.Room;
import com.example.demo.dao.repositories.BookingRepository;
import com.example.demo.dao.repositories.RoomRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                            RoomRepository roomRepository,
                            FilesStorageService filesStorageService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    // Sort contacts by name in ascending alphabetical order
    @Override
    public List<Booking> getAllBookings() {
        return this.bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Long id) {
        // Check if the ID is null and throw an IllegalArgumentException if it is
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        // Retrieve the contact by ID, throw an EntityNotFoundException if not found
        return this.bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact with id: " + id + " not found"));
    }

    // Add method
    @Override
    public Booking addBooking(Booking booking) {
        // Check if the contact is null and throw an IllegalArgumentException if it is
        if (booking == null) {
            throw new IllegalArgumentException("customer cannot be null");
        }
        
            // Save the contact in the repository
            return bookingRepository.save(booking);
        
    }
   // Update method
   @Override
public Booking updateBooking(Long id, Booking updatedBooking) {
    if (id == null || updatedBooking == null) {
        throw new IllegalArgumentException("ID or booking cannot be null");
    }

    // Fetch the existing booking first
    Booking existingBooking = getBookingById(id);

    // Update fields manually
    existingBooking.setCheckin(updatedBooking.getCheckin());
    existingBooking.setCheckout(updatedBooking.getCheckout());
    existingBooking.setConfirmed(updatedBooking.isConfirmed());
    existingBooking.setRoomNumber(updatedBooking.getRoomNumber());
    existingBooking.setUsername(updatedBooking.getUsername());

    // Now save the existingBooking (which has the correct ID)
    return bookingRepository.save(existingBooking);
}


    @Override
    @Transactional
    // the deleteContact method executes all its operations (checking for the contact, deleting the file, 
    //and deleting the contact record) within a single transaction.If any part of this process fails, 
    //the entire transaction will be rolled back, maintaining data consistency and integrity. 
    public void deleteBooking(Long id) {
        // Check if the ID is null and throw an IllegalArgumentException if it is
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try {
            bookingRepository.deleteById(id);
        } catch (DataAccessException e) {
            // Capture any data access exceptions (e.g., foreign key constraint violations)
            throw new RuntimeException("Failed to delete customer with id: " + id, e);
        }
    }
     // Vérifier si une chambre est libre
    @Override
    public boolean isRoomAvailable(Long roomNumber, LocalDate checkin, LocalDate checkout) {
        List<Booking> bookings = bookingRepository.findByRoomNumber(roomNumber);
        for (Booking booking : bookings) {
            if (booking.getCheckin().isBefore(checkout) && booking.getCheckout().isAfter(checkin)) {
                return false; // overlap
            }
        }
        return true;
    }

    // Réserver une chambre
    @Override
    public Booking bookRoom(Long roomNumber, String username, LocalDate checkin, LocalDate checkout) {
        if (!isRoomAvailable(roomNumber, checkin, checkout)) {
            throw new RuntimeException("Room is not available for selected dates");
        }

        Booking booking = Booking.builder()
                .roomNumber(roomNumber)
                .username(username)
                .checkin(checkin)
                .checkout(checkout)
                .confirmed(false) // par défaut
                .build();

        return bookingRepository.save(booking);
    }

    // Lister chambres disponibles
    @Override
    public List<Room> findAvailableRooms(LocalDate checkin, LocalDate checkout, Double maxPrice, Integer noOfGuests) {
        List<Room> allRooms = roomRepository.findAll();

        return allRooms.stream()
                .filter(room -> {
                    boolean available = isRoomAvailable(room.getRoomNumber(), checkin, checkout);
                    boolean priceOk = (maxPrice == null) || (room.getPrice() <= maxPrice);
                    boolean guestsOk = (noOfGuests == null) || (room.getNoOfGuests() >= noOfGuests);
                    return available && priceOk && guestsOk;
                })
                .collect(Collectors.toList());
    }
    public Booking confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setConfirmed(true); // assuming you have a 'confirmed' field
        return bookingRepository.save(booking);
    }
    public List<Booking> getBookingByName(String name) {
        return bookingRepository.findByUsernameContainingIgnoreCase(name);
    }
    
    
    

    
}

