import { Injectable } from '@angular/core';

import { Admin } from './models/admin';
import { Algorithm } from './models/algorithm';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  public admin: Admin;

  public isLoggedIn: boolean;

  public selectedAlgorithm: Algorithm;

  constructor() { 
    this.selectedAlgorithm = new Algorithm();
    this.isLoggedIn = false;
    this.admin = new Admin();
  }

  public logout(): void {
    this.isLoggedIn = false;
    this.admin = new Admin();
  }

}
