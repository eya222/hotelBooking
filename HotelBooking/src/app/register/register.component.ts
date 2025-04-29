import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Customer } from '../models/customer';
import { RoomService } from '../services/room.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(private roomservice:RoomService, private router: Router) {}

  newcustomer : Customer = new Customer();
  reppassword !: string;

  ngOnInit(): void {
  }

  
  onSubmit()
  {
    // this.newcustomer.id = 1;
    this.newcustomer.identifier= "user";
    console.log(this.newcustomer);
    this.savecustomer();
  }

  savecustomer()
  {
    this.roomservice.addCustomer(this.newcustomer).subscribe((data: any) => {
      console.log(data);
      alert("Account created successfully");
      this.goTologin();
    }, (error: any) => console.log(error));
  }
  goTologin()
  {
    this.router.navigate(['/login']);
  }

}
