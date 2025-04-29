package com.example.demo.web.controllers;

import com.example.demo.business.services.BookingService;
import com.example.demo.dao.entities.Booking;
import com.example.demo.dao.entities.Room;
import com.example.demo.web.dto.BookingDTO;
import com.example.demo.web.dto.RoomDTO;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
public class BookingControllers {

    private final BookingService bookingService;

    public BookingControllers(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllBookings() {
        log.info("Fetching all bookings");
        List<BookingDTO> bookings = this.bookingService.getAllBookings()
                .stream()
                .map(BookingDTO::toBookingDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable(name = "id") Long id) {
        log.info("Fetching booking with id: {}", id);
        BookingDTO booking = BookingDTO.toBookingDTO(this.bookingService.getBookingById(id));
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> addBooking(@RequestBody BookingDTO bookingDTO)  {
        log.info("Adding new booking: {}", bookingDTO);
        Booking booking = BookingDTO.fromBookingDTO(bookingDTO);
        return new ResponseEntity<>(this.bookingService.addBooking(booking), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable(name = "id") Long id, @RequestBody BookingDTO bookingDTO){
        log.info("Updating booking with id: {}", id);
        Booking booking = BookingDTO.fromBookingDTO(bookingDTO);
        return new ResponseEntity<>(this.bookingService.updateBooking(id, booking), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable(name = "id") Long id) {
        log.info("Deleting booking with id: {}", id);
        this.bookingService.deleteBooking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // Vérifier disponibilité
    @GetMapping("/check-availability/{roomNumber}/{checkin}/{checkout}")
    public ResponseEntity<Boolean> checkAvailability(
        @PathVariable Long roomNumber,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkin,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkout
    ) {
        boolean available = bookingService.isRoomAvailable(roomNumber, checkin, checkout);
        return ResponseEntity.ok(available);
    }
    

// Réserver une chambre
@PostMapping("/book")
public ResponseEntity<BookingDTO> bookRoom(
        @RequestParam Long roomNumber,
        @RequestParam String username,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkin,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkout
) {
    Booking booking = bookingService.bookRoom(roomNumber, username, checkin, checkout);
    return new ResponseEntity<>(BookingDTO.toBookingDTO(booking), HttpStatus.CREATED);
}

// Lister chambres disponibles
@GetMapping("/available-rooms")
public ResponseEntity<List<RoomDTO>> findAvailableRooms(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkin,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkout,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Integer noOfGuests
) {
    List<Room> rooms = bookingService.findAvailableRooms(checkin, checkout, maxPrice, noOfGuests);
    List<RoomDTO> roomDTOs = rooms.stream()
            .map(RoomDTO::toRoomDTO)
            .collect(Collectors.toList());
    return ResponseEntity.ok(roomDTOs);
}
@PutMapping("/confirm/{id}")
public ResponseEntity<?> confirmBooking(@PathVariable(name = "id") Long id) {
    log.info("Confirming booking with id: {}", id);
    Booking booking = bookingService.confirmBooking(id);
    return new ResponseEntity<>(BookingDTO.toBookingDTO(booking), HttpStatus.OK);
}
@GetMapping("/bookingbyname/{name}")
public ResponseEntity<List<BookingDTO>> getBookingByName(@PathVariable(name = "name") String name) {
    log.info("Fetching bookings with name: {}", name);
    List<Booking> bookings = bookingService.getBookingByName(name);
    List<BookingDTO> bookingDTOs = bookings.stream()
            .map(BookingDTO::toBookingDTO)
            .collect(Collectors.toList());
    return new ResponseEntity<>(bookingDTOs, HttpStatus.OK);
}



}
