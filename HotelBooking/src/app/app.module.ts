import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatTabsModule} from '@angular/material/tabs';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NavbarComponent } from './navbar/navbar.component';
import { AdminComponent } from './admin/admin.component';
import { UpdateroomComponent } from './updateroom/updateroom.component';
import { LoginpageComponent } from './loginpage/loginpage.component';
import { HomepageComponent } from './homepage/homepage.component';
import { ContactComponent } from './contact/contact.component';
import { UserComponent } from './user/user.component';
import { RegisterComponent } from './register/register.component';
import { BookingComponent } from './booking/booking.component';

// Import services
import { RoomService } from './services/room.service';
import { LoginService } from './services/login.service';

// Import guards
import { AuthGuard } from './guards/auth.guard';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    AdminComponent,
    UpdateroomComponent,
    LoginpageComponent,
    HomepageComponent,
    ContactComponent,
    UserComponent,
    RegisterComponent,
    BookingComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NoopAnimationsModule,
    MatTabsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    RoomService,
    LoginService,
    AuthGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
