import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin/admin.component';
import { AppComponent } from './app.component';
import { BookingComponent } from './booking/booking.component';
import { ContactComponent } from './contact/contact.component';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginpageComponent } from './loginpage/loginpage.component';
import { RegisterComponent } from './register/register.component';
import { UpdateroomComponent } from './updateroom/updateroom.component';
import { UserComponent } from './user/user.component';
import { authGuard } from './auth/auth.guard';


const routes: Routes = [
  {path:"admin",component:AdminComponent, canActivate: [authGuard]},
  {path:"login",component:LoginpageComponent},
  {path:"",component:HomepageComponent},
  {path:"home",component:HomepageComponent},
  {path:"contact",component:ContactComponent},
  {path:"user/:username",component:UserComponent, canActivate: [authGuard]},
  // {path:"user/:username/:bookingname",component:UserComponent},
  // {path:"user/:username/:bookingname/:customerid",component:UserComponent},
  {path:"register",component:RegisterComponent},
  {path:"booking/:username/:id",component:BookingComponent, canActivate: [authGuard]},
  {path:"updateroom/:id",component:UpdateroomComponent, canActivate: [authGuard]},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
