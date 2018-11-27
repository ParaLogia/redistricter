import { Component, OnInit } from '@angular/core';

import { AdminService } from "../admin.service";

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent implements OnInit {

  constructor(private adminService: AdminService) { }

  ngOnInit() {
  }

}
