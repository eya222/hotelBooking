import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Booking } from '../models/booking';
import { Customer } from '../models/customer';
import { Room } from '../models/room';
import { RoomService } from '../services/room.service';
import { BookingService } from '../services/booking.service';
import { FileUploadService } from '../services/file-upload.service';
import { ProcessHttpmsgService } from '../services/process-httpmsg.service';
import { HttpEventType, HttpResponse } from '@angular/common/http';
// import { Review } from '../models/review'; // Import later when model is created

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  rooms?: Room[];
  customers?: Customer[];
  bookings?: Booking[];
  reviews: any[] = []; // Use any[] temporarily
  amenities: string[] = ['AC', 'WiFi', 'TV', 'MiniBar', 'Balcony'];
  selectedAmenities: string[] = [];
  roomAvailability: { [key: number]: boolean } = {};

  newroom: Room = new Room();

  oldbooking: Booking = new Booking();
  roomnumber!: number;

  selectedFiles?: FileList;
  currentFile?: File;
  progress = 0;
  message = '';
  isLoading: boolean = false;

  constructor(
    private roomservice: RoomService,
    private bookingservice: BookingService,
    private router: Router,
    private fileUploadService: FileUploadService,
    private processHTTPMsgService: ProcessHttpmsgService
  ) { }

  ngOnInit(): void {
    this.getRooms();
    this.getCustomers();
    this.getBookings();
  }

  getRooms() {
    this.roomservice.getRoomList().subscribe({
      next: (data) => {
        this.rooms = data;
        console.log(this.rooms);
      },
      error: (err) => {
        this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
          this.message = errorMsg;
          console.error(err);
        });
      }
    });
  }

  getCustomers() {
    this.roomservice.getCustomerList().subscribe({
      next: (data) => {
        this.customers = data;
        console.log(this.customers);
      },
      error: (err) => {
        this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
          this.message = errorMsg;
          console.error(err);
        });
      }
    });
  }

  getBookings() {
    this.roomservice.getBookingList().subscribe({
      next: (data) => {
        this.bookings = data;
        this.bookings.forEach(booking => {
          this.fetchRoomAvailability(booking);
        });
      },
      error: (err) => {
        this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
          this.message = errorMsg;
          console.error(err);
        });
      }
    });
  }

  fetchRoomAvailability(booking: Booking): void {
    if (!booking || !booking.id || !booking.roomNumber || !booking.checkin || !booking.checkout) {
      console.warn(`Invalid booking: ${JSON.stringify(booking)}`);
      this.roomAvailability[booking?.id] = false;
      return;
    }

    const checkinDate = typeof booking.checkin === 'string' ? new Date(booking.checkin) : booking.checkin;
    const checkoutDate = typeof booking.checkout === 'string' ? new Date(booking.checkout) : booking.checkout;

    if (isNaN(checkinDate.getTime()) || isNaN(checkoutDate.getTime())) {
      console.warn(`Invalid dates for booking ${booking.id}: checkin=${booking.checkin}, checkout=${booking.checkout}`);
      this.roomAvailability[booking.id] = false;
      return;
    }

    this.bookingservice.checkRoomAvailability(
      booking.roomNumber,
      checkinDate.toISOString().split('T')[0],
      checkoutDate.toISOString().split('T')[0]
    ).subscribe({
      next: (isAvailable: boolean) => {
        this.roomAvailability[booking.id] = isAvailable;
      },
      error: (err: any) => {
        this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
          this.message = errorMsg;
          console.error(`Error checking availability for booking ${booking.id}:`, err);
        });
        this.roomAvailability[booking.id] = false;
      }
    });
  }

  saveRoom(addform: NgForm) {
    this.isLoading = true;
    this.roomservice.createRoom(this.newroom).subscribe({
      next: (data) => {
        console.log(data);
        this.upload(data, addform);
      },
      error: (err) => {
        this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
          this.message = errorMsg;
          console.error(err);
          this.isLoading = false;
        });
      }
    });
  }

  goToAdmin() {
    this.router.navigate(['/admin']);
  }

  onSubmit(addform: NgForm) {
    console.log(this.newroom);
    this.saveRoom(addform);
  }

  updateroom(roomNumber: number) {
    console.log(roomNumber);
    this.router.navigate(['updateroom', roomNumber]);
  }

  deleteroom(roomNumber: number) {
    this.roomservice.deleteRoom(roomNumber).subscribe({
      next: (data) => {
        console.log(roomNumber);
        alert("Deleted successfully");
        this.getRooms();
        this.goToAdmin();
      },
      error: (err) => {
        this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
          this.message = errorMsg;
          console.error(err);
        });
      }
    });
  }

  deletecustomer(Customerid: number) {
    this.roomservice.deleteCustomer(Customerid).subscribe({
      next: (data) => {
        console.log(Customerid);
        alert("Deleted successfully");
        this.getCustomers();
        this.goToAdmin();
      },
      error: (err) => {
        this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
          this.message = errorMsg;
          console.error(err);
        });
      }
    });
  }

  deleteBooking(bookingId: number) {
    this.roomservice.cancelBooking(bookingId).subscribe({
      next: (data) => {
        console.log('Booking deleted successfully', data);
        this.getBookings();
      },
      error: (err) => {
        this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
          this.message = errorMsg;
          console.error(err);
        });
      }
    });
  }

  confirmBooking(bookingId: number) {
    // 1. Find the booking details from the list
    const bookingToConfirm = this.bookings?.find(b => b.id === bookingId);

    if (!bookingToConfirm) {
      console.error("Could not find booking with ID:", bookingId);
      alert("Error: Could not find booking details.");
      return;
    }

    // 2. Check room availability first
    const checkinDate = typeof bookingToConfirm.checkin === 'string' ? new Date(bookingToConfirm.checkin) : bookingToConfirm.checkin;
    const checkoutDate = typeof bookingToConfirm.checkout === 'string' ? new Date(bookingToConfirm.checkout) : bookingToConfirm.checkout;

    if (isNaN(checkinDate.getTime()) || isNaN(checkoutDate.getTime())) {
      alert("Error: Invalid check-in or check-out dates for this booking.");
      return;
    }

    const checkinStr = checkinDate.toISOString().split('T')[0];
    const checkoutStr = checkoutDate.toISOString().split('T')[0];

    this.isLoading = true; // Indicate loading state

    this.bookingservice.checkRoomAvailability(bookingToConfirm.roomNumber, checkinStr, checkoutStr).subscribe({
      next: (isAvailable: boolean) => {
        if (isAvailable) {
          // 3. If available, proceed with confirmation
          this.roomservice.confirmBooking(bookingId).subscribe({
            next: (data: any) => {
              alert("Booking confirmed successfully");
              this.getBookings(); // Refresh the booking list
              this.isLoading = false;
            },
            error: (err: any) => {
              this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
                this.message = errorMsg;
                console.error("Error confirming booking:", err);
                alert("Error confirming booking: " + errorMsg);
                this.isLoading = false;
              });
            }
          });
        } else {
          // 4. If not available, show alert and stop
          alert(`Error: Room ${bookingToConfirm.roomNumber} is not available for dates ${checkinStr} to ${checkoutStr}. Cannot confirm booking.`);
          this.isLoading = false;
        }
      },
      error: (err: any) => {
        // Handle error during availability check
        this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
          this.message = errorMsg;
          console.error("Error checking room availability:", err);
          alert("Error checking room availability: " + errorMsg);
          this.isLoading = false;
        });
      }
    });
  }

  selectFile(event: any): void {
    this.selectedFiles = event.target.files;
    if (this.selectedFiles && this.selectedFiles.length > 0) {
      this.currentFile = this.selectedFiles[0];
    } else {
      this.currentFile = undefined;
    }
  }

  upload(room: Room, addform: NgForm): void {
    this.progress = 0;
    console.log('Attempting upload. Current file:', this.currentFile);
    if (this.currentFile) {
      console.log('File details - Name:', this.currentFile.name, 'Size:', this.currentFile.size, 'Type:', this.currentFile.type);
      if (this.currentFile.size === 0) {
        alert("Error: Cannot upload an empty file.");
        this.message = "Error: Selected file is empty.";
        this.isLoading = false;
        this.currentFile = undefined;
        this.selectedFiles = undefined;
        // Consider resetting the file input visually if possible
        return; // Stop the upload process
      }
      const formData = new FormData();
      formData.append('file', this.currentFile);
      console.log('Uploading file via service for room:', room.roomNumber);
      this.fileUploadService.upload(formData, room.roomNumber).subscribe({
        next: (event: any) => {
          if (event.type === HttpEventType.UploadProgress) {
            this.progress = Math.round(100 * event.loaded / event.total);
          } else if (event instanceof HttpResponse) {
            this.message = event.body.message || 'File uploaded successfully!';
            this.getRooms();
            this.goToAdmin();
            this.isLoading = false;
            addform.reset();
            this.selectedFiles = undefined;
            this.currentFile = undefined;
            this.newroom = new Room();
            alert("Room added successfully");
          }
        },
        error: (err) => {
          this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
            this.message = errorMsg || 'Could not upload the file!';
            console.error(err);
            this.progress = 0;
            this.currentFile = undefined;
            this.isLoading = false;
          });
        }
      });
    } else {
      console.error('Upload called but this.currentFile is not set!');
      // This part handles the case where the room is added without an image, 
      // but the console error helps debug unexpected calls to upload().
      this.getRooms();
      this.goToAdmin();
      this.isLoading = false;
      addform.reset();
      this.selectedFiles = undefined;
      this.currentFile = undefined;
      this.newroom = new Room();
      alert("Room added successfully (no image uploaded)");
    }
  }

  onAmenityChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    const amenity = checkbox.value;

    if (checkbox.checked) {
      this.selectedAmenities.push(amenity);
    } else {
      const index = this.selectedAmenities.indexOf(amenity);
      if (index > -1) {
        this.selectedAmenities.splice(index, 1);
      }
    }

    this.newroom.category = this.selectedAmenities.join('/');
  }
}
