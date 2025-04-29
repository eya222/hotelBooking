import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Room } from '../models/room';
import { RoomService } from '../services/room.service';
import { FileUploadService } from '../services/file-upload.service';
import { ProcessHttpmsgService } from '../services/process-httpmsg.service';

@Component({
  selector: 'app-updateroom',
  templateUrl: './updateroom.component.html',
  styleUrls: ['./updateroom.component.css']
})
export class UpdateroomComponent implements OnInit {

  RoomNumber!: number;
  newroom: Room = new Room();
  amenities: string[] = ['AC', 'WiFi', 'TV', 'MiniBar', 'Balcony'];
  selectedAmenities: string[] = [];

  selectedFiles?: FileList;
  currentFile?: File;
  progress = 0;
  message = '';
  isLoading: boolean = false;

  constructor(
    private roomService: RoomService,
    private router: Router,
    private route: ActivatedRoute,
    private fileUploadService: FileUploadService,
    private processHTTPMsgService: ProcessHttpmsgService
  ) { }

  ngOnInit(): void {
    this.RoomNumber = this.route.snapshot.params['id'];
    console.log(this.RoomNumber);
    this.roomService.getRoomByRoomNo(this.RoomNumber).subscribe(data => {
      this.newroom = data;
      if (this.newroom.category) {
        this.selectedAmenities = this.newroom.category.split('/');
      }
      console.log(this.newroom);
    }, error => console.log(error));
  }

  onSubmit(roomform: NgForm) {
    this.isLoading = true;
    this.roomService.updateRoom(this.RoomNumber, this.newroom).subscribe({
      next: (data) => {
        console.log("Update successful", data);
        if (this.currentFile) {
          this.upload(this.newroom, roomform);
        } else {
          alert("Updated Successfully (no image change)");
          this.isLoading = false;
          this.goToAdmin();
        }
      },
      error: (err) => {
        this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
          this.message = errorMsg || 'Update failed!';
          console.error(err);
          this.isLoading = false;
        });
      }
    });
  }

  goToAdmin() {
    this.router.navigate(['/admin']);
  }

  onAmenityChange(event: any) {
    const amenity = event.target.value;
    if (event.target.checked) {
      this.selectedAmenities.push(amenity);
    } else {
      const index = this.selectedAmenities.indexOf(amenity);
      if (index > -1) {
        this.selectedAmenities.splice(index, 1);
      }
    }
    this.newroom.category = this.selectedAmenities.join('/');
    console.log("Updated amenities:", this.newroom.category);
  }

  selectFile(event: any): void {
    this.selectedFiles = event.target.files;
    if (this.selectedFiles) {
      this.currentFile = this.selectedFiles.item(0) || undefined;
      this.message = this.currentFile ? 'File selected: ' + this.currentFile.name : '';
      this.progress = 0;
    }
  }

  upload(room: Room, form: NgForm): void {
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
            alert("Room and image updated successfully");
            this.goToAdmin();
            this.isLoading = false;
            form.reset();
            this.selectedFiles = undefined;
            this.currentFile = undefined;
            this.newroom = new Room();
          }
        },
        error: (err) => {
          this.processHTTPMsgService.handleError(err).subscribe(errorMsg => {
            this.message = errorMsg || 'Could not upload the file!';
            console.error(err);
            this.progress = 0;
            this.currentFile = undefined;
            this.isLoading = false;
            alert("Room updated, but image upload failed: " + this.message);
          });
        }
      });
    } else {
      // This block should ideally not be reached in the update component 
      // because upload() is only called if this.currentFile is set.
      // If it IS reached, it indicates a logic error elsewhere.
      console.error('Upload called in update component, but this.currentFile is not set!');
      alert("Internal error: Tried to upload but no file was selected.");
      this.isLoading = false; 
    }
  }
}

