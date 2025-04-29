import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { LoginService } from "../services/login.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private loginServices: LoginService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot, 
    state: RouterStateSnapshot
  ): boolean {
    if (this.isLoggedIn()) {
      return true;
    }
    this.router.navigate(['login']);
    return false;
  }

  private isLoggedIn(): boolean {
    return this.loginServices.getLoginStatus();
  }
}
