import { Injectable } from '@angular/core';

import { Admin } from './models/admin';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  public admin: Admin;

  public isLoggedIn: boolean;

  constructor() { 
    this.isLoggedIn = false;
    this.admin = new Admin();
  }

  public logout(): void {
    this.isLoggedIn = false;
    this.admin = new Admin();
  }

}
