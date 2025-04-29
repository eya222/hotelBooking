import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { Customer } from '../models/customer';
import { RoomService } from '../services/room.service';
import { LoginService } from '../services/login.service';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-loginpage',
  templateUrl: './loginpage.component.html',
  styleUrls: ['./loginpage.component.css']
})
export class LoginpageComponent implements OnInit {

  username!:string;
  password!:string;
  Result !:number;
  error !:string;
  
  constructor(private router : Router,private roomservice : RoomService,private route :ActivatedRoute, private loginService:LoginService, private authService:AuthService) { }

  ngOnInit(): void {
  
  }

  onSubmit(username:string,password:string)
  {
      console.log(username,password);
      this.roomservice.checkValidUser(username,password).subscribe((res: any) =>
        {
          this.Result = res;
          console.log(this.Result);

          if(this.Result == 1){
            this.authService.login({username:username,password:password});
            alert("Login Succesfull");
            this.router.navigate(['/admin']);
          }

          else if(this.Result == 2)
          {
            this.authService.login({username:username,password:password});
    
            alert("Login Succesfull");
            this.router.navigate(['/user',username]);
          }

          else if(this.Result == 0)
          {
            this.error = "Invalid Username";
          }

          else if(this.Result == 3)
          {
            this.error = "Invalid Password";
          }
        })




  }

  // getroombyusername(userName:string)
  // {
  //   this.roomservice.getcustomerByusername(userName).subscribe(data => 
  //     {
  //       this.customer = data;
  //       console.log(this.customer);
  //     }, error => console.log(error)
  //     );
  // }
  

}
