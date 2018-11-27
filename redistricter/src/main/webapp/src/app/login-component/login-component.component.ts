import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Http } from '@angular/http';

import { environment } from '../../environments/environment';

import { Admin } from '../models/admin';
import { AdminService } from '../admin.service';


@Component({
  selector: 'app-login-component',
  templateUrl: './login-component.component.html',
  styleUrls: ['./login-component.component.css']
})
export class LoginComponentComponent implements OnInit {

  public showInvalidLoginAlert: boolean;

  constructor(private adminService: AdminService,
    private router: Router, private http: Http) { }

  ngOnInit() {

    // If admin is logged in then redirect home
    if (this.adminService.isLoggedIn) {
      this.router.navigateByUrl('/home');
    }

    this.showInvalidLoginAlert = false;

  }

  /**
   * Validates the admin credentials
   */
  public validateAdmin(): void {

    // For testing
    console.log(this.adminService.admin);

    if (this.adminService.admin.username === 'admin' && this.adminService.admin.username === 'admin'){
      this.adminService.isLoggedIn = true;
      this.router.navigateByUrl('/admin/home');
    }
    else{
      this.showInvalidLoginAlert = true;
      this.adminService.admin = new Admin();
    }

    // // Post to see if credentials are correct
    // this.http.post(environment.apiBaseUrl + "/admin/login", this.adminService.admin).toPromise()
    //   .then( (res) => { // Should return a boolean object whether the login succeeded
        
    //     // If valid login
    //     if (res) {
    //       this.adminService.isLoggedIn = true;
    //       this.router.navigateByUrl('/admin/home');
    //     } else {
    //       // Show error and clear fields
    //       this.showInvalidLoginAlert = true;
    //       this.adminService.admin = new Admin();
    //     }
    //   },
    //   // If an error occurs, log it
    //   (err) => {
    //     console.log("error: " + err);
    //   }) 

  }

}
