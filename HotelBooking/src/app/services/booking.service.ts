import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Booking } from '../models/booking';
import { Customer } from '../models/customer';
import { Room } from '../models/room';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private baseURL = "http://localhost:8085/api";  // Same base URL as RoomService

  constructor(private httpClient: HttpClient) { }

  // Get all bookings
  getBookingList(): Observable<Booking[]> {
    return this.httpClient.get<Booking[]>(`${this.baseURL}/bookings`);
  }

  // Get booking by its ID
  getBookingById(bookingId: number): Observable<Booking> {
    return this.httpClient.get<Booking>(`${this.baseURL}/bookings/${bookingId}`);
  }

  // Cancel booking by its ID
  cancelBooking(bookingId: number): Observable<object> {
    return this.httpClient.delete(`${this.baseURL}/bookings/${bookingId}`);
  }

  // Create a new booking
  newBooking(booking: Booking): Observable<any> {
    return this.httpClient.post(`${this.baseURL}/bookings`, booking);
  }

  // Get booking by its name
  getBookingByName(bookingName: string): Observable<Booking[]> {
    return this.httpClient.get<Booking[]>(`${this.baseURL}/bookings/bookingbyname/${bookingName}`);
  }

  // Confirm the booking
  confirmBooking(bookingId: number): Observable<any> {
    return this.httpClient.put(`${this.baseURL}/bookings/confirm/${bookingId}`, {});
  }

  // Check availability of a room for given dates
  checkRoomAvailability(roomNumber: number, checkin: string, checkout: string): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.baseURL}/bookings/check-availability/${roomNumber}/${checkin}/${checkout}`);
  }

  // Update booked room availability (added method)
  updateBookedRoom(roomNumber: number, confirmed: number): Observable<any> {
    return this.httpClient.put(`${this.baseURL}/bookings/bookedroom/${roomNumber}`, { confirmed });
  }
}
