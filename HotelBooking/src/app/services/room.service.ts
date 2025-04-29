import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Booking } from '../models/booking';
import { Customer } from '../models/customer';
import { Room } from '../models/room';
import { log } from 'console';

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  private baseURL = "http://localhost:8085/api";

  constructor(private httpClient: HttpClient) { }

  getRoomList(): Observable<Room[]> {
    return this.httpClient.get<Room[]>(`${this.baseURL}/rooms`);
  }
  createRoom(room: Room): Observable<any> {
    return this.httpClient.post(`${this.baseURL}/rooms`, room);
  }

  getRoomByRoomNo(roomNumber: number): Observable<Room> {
    return this.httpClient.get<Room>(`${this.baseURL}/rooms/${roomNumber}`);
  }
  updateRoom(roomNumber: number, room: Room): Observable<object> {
    return this.httpClient.put(`${this.baseURL}/rooms/${roomNumber}`, room);
  }

  deleteRoom(roomNumber: number): Observable<object> {
    return this.httpClient.delete(`${this.baseURL}/rooms/${roomNumber}`);
  }

  getCustomerList(): Observable<Customer[]> {
    return this.httpClient.get<Customer[]>(`${this.baseURL}/contacts`);
  }
  deleteCustomer(customerId: number): Observable<object> {
    return this.httpClient.delete(`${this.baseURL}/contacts/${customerId}`);
    
  }
  checkValidUser(userName: string, password: string): Observable<number> {
    return this.httpClient.get<number>(`${this.baseURL}/contacts/${userName}/${password}`);
  }

  getCustomerByUsername(userName: string): Observable<Customer> {
    return this.httpClient.get<Customer>(`${this.baseURL}/contacts/username/${userName}`);
  }

  addCustomer(customer: Customer): Observable<any> {
    console.log(customer);
    return this.httpClient.post(`${this.baseURL}/contacts`, customer);
  }

  getCustomerById(customerId: number): Observable<Customer> {
    return this.httpClient.get<Customer>(`${this.baseURL}/contacts/${customerId}`);
  }

  getBookingList(): Observable<Booking[]> {
    return this.httpClient.get<Booking[]>(`${this.baseURL}/bookings`);
  }

  getBookingById(bookingId: number): Observable<Booking> {
    return this.httpClient.get<Booking>(`${this.baseURL}/bookings/${bookingId}`);
  }
  cancelBooking(bookingId: number): Observable<any> {
    return this.httpClient.delete(`${this.baseURL}/bookings/${bookingId}`);
  }

  confirmBooking(bookingId: number): Observable<any> {
    return this.httpClient.put(`${this.baseURL}/bookings/confirm/${bookingId}`, {});
  }

  newBooking(booking: Booking): Observable<any> {
    return this.httpClient.post(`${this.baseURL}/bookings`, booking);
  }


  getBookingByName(bookingName: String): Observable<Booking[]> {
    return this.httpClient.get<Booking[]>(`${this.baseURL}/bookings/bookingbyname/${bookingName}`);
  }

  checkRoomAvailability(roomNumber: number, checkin: string, checkout: string): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.baseURL}/bookings/check-availability/${roomNumber}/${checkin}/${checkout}`);
  }
}
