import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Booking } from '../models/booking';
import { Customer } from '../models/customer';
import { Room } from '../models/room';
import { RoomService } from '../services/room.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  rooms: Room[] = [];
  filteredRooms: Room[] = [];
  bookings?: Booking[];
  username !: string;
  name !: String;
  Bookedroomno !:number;
  customer : Customer = new Customer();

  // Filter properties
  priceFilter: string = '';
  capacityFilter: string = '';
  typeFilter: string = '';
  searchText: string = '';
  amenities = {
    wifi: false,
    tv: false,
    minibar: false,
    ac: false
  };

  constructor(private roomservice:RoomService, private router: Router,private route :ActivatedRoute) { }

  ngOnInit(): void {
    this.username = this.route.snapshot.params['username'];
    this.getRooms();
    
    this.roomservice.getCustomerByUsername(this.username).subscribe((data: any) => {
      this.customer = data;
      this.name  = this.customer.userName!;
      console.log(this.name);

      this.roomservice.getBookingByName(this.name).subscribe((data: any) => {
        this.bookings = data;
        console.log(this.bookings);
      }, (error: any) => console.log(error));
    });
  }

  getRooms() {
    this.roomservice.getRoomList().subscribe(data => {
      this.rooms = data;
      this.filteredRooms = [...this.rooms];
    });
  }

  applyFilters() {
    if (!this.rooms) return;
  
    this.filteredRooms = this.rooms.filter(room => {
      // Price filter
      if (this.priceFilter && room.price !== undefined) {
        const [min, max] = this.priceFilter.split('-').map(Number);
        if (max) {
          if (room.price < min || room.price > max) return false;
        } else {
          if (room.price < min) return false;
        }
      }
  
      // Capacity filter
      if (this.capacityFilter && room.noOfGuests !== undefined) {
        const capacity = parseInt(this.capacityFilter);
        if (room.noOfGuests !== capacity) return false;
      }
  
      // Type filter
      if (this.typeFilter && room.roomType) {
        if (room.roomType !== this.typeFilter) return false;
      }
  
      // Search text
      if (this.searchText && room.roomType && room.category) {
        const searchLower = this.searchText.toLowerCase();
        if (
          !room.roomType.toLowerCase().includes(searchLower) &&
          !room.category.toLowerCase().includes(searchLower)
        ) {
          return false;
        }
      }
  
      // Amenities filter
      if (room.category) {
        const amenitiesList = room.category.toLowerCase().split('/');
        if (this.amenities.wifi && !amenitiesList.includes('wifi')) return false;
        if (this.amenities.tv && !amenitiesList.includes('tv')) return false;
        if (this.amenities.minibar && !amenitiesList.includes('minibar')) return false;
        if (this.amenities.ac && !amenitiesList.includes('ac')) return false;
      }
  
      return true;
    });
  }
  

  resetFilters() {
    this.priceFilter = '';
    this.capacityFilter = '';
    this.typeFilter = '';
    this.amenities = {
      wifi: false,
      tv: false,
      minibar: false,
      ac: false
    };
    this.filteredRooms = [...this.rooms];
  }

  bookroom(roomNumber : number) {
    console.log(roomNumber);
    this.Bookedroomno = roomNumber;
    this.router.navigate(['booking',this.username,roomNumber]);
  }

  alertmsg() {
    alert("Review submitted successfully");
  }
}
