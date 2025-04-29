package com.example.demo.web.dto;
import com.example.demo.dao.entities.Booking;


import java.time.LocalDate;


import lombok.Builder;

@Builder
public record BookingDTO(
    Long id,
    String username,
    LocalDate checkin,
    LocalDate checkout,
    long roomNumber,
    boolean confirmed
    ) {
  public static BookingDTO toBookingDTO(Booking booking) {
    BookingDTO bookingDTO = BookingDTO.builder()
        .id(booking.getId())
        .checkin(booking.getCheckin())
        .checkout(booking.getCheckout())
        .roomNumber(booking.getRoomNumber())
        .username(booking.getUsername())
        .confirmed(booking.isConfirmed()).build();
    return bookingDTO;
    
  }

  public static Booking fromBookingDTO(BookingDTO bookingDTO) {
    Booking booking = Booking.builder()
        .id(bookingDTO.id)
        .checkin(bookingDTO.checkin)
        .checkout(bookingDTO.checkout)
        .roomNumber(bookingDTO.roomNumber)
        .username(bookingDTO.username)
        .confirmed(bookingDTO.confirmed).build();
    return booking;
    
  }
}
