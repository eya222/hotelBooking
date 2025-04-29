import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Booking } from '../models/booking';
import { Customer } from '../models/customer';
import { Room } from '../models/room';
import { RoomService } from '../services/room.service';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.css']
})
export class BookingComponent implements OnInit {

  RoomNumber!: number;
  userName!: string;
  room: Room = new Room();
  customer: Customer = new Customer();
  booking: Booking = new Booking();
  sendname?: string;

  constructor(
    private roomService: RoomService,
    private bookingService: BookingService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.userName = this.route.snapshot.params['username'];
    this.RoomNumber = this.route.snapshot.params['id'];

    console.log(this.RoomNumber, this.userName);

    this.roomService.getRoomByRoomNo(this.RoomNumber).subscribe({
      next: (data: Room) => {
        this.room = data;
        console.log(this.room);

        // Update the booked room availability to 0
        this.bookingService.updateBookedRoom(this.RoomNumber, 0).subscribe({
          next: (data: any) => {
            console.log('Room availability updated:', data);
          },
          error: (error: any) => {
            console.log('Error updating room:', error);
          }
        });
      },
      error: (error: any) => {
        console.log('Error fetching room:', error);
      }
    });

    this.roomService.getCustomerByUsername(this.userName).subscribe({
      next: (data: Customer) => {
        this.customer = data;
        console.log(this.customer);
      },
      error: (error: any) => {
        console.log('Error fetching customer:', error);
      }
    });
  }

  onSubmit(bookform: NgForm) {
    this.booking = bookform.value;
    this.booking.username = this.userName;
    this.booking.roomNumber = this.RoomNumber;
    this.booking.confirmed = false;
    console.log(this.booking);
    this.newbooking();
    alert("Room booked successfully");
  }

  newbooking() {
    this.bookingService.newBooking(this.booking).subscribe({
      next: (data: any) => {
        console.log(data);
        this.goToUser();
      },
      error: (error: any) => {
        console.log('Error booking room:', error);
      }
    });
  }

  goToUser() {
    this.router.navigate(['/user', this.userName]);
  }
}
