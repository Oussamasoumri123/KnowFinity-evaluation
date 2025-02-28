import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AdminSessionService } from '../../service/admin-session.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  isAdminLoggedIn: boolean = false;
  adminUser: any = null;

  constructor(private adminSessionService: AdminSessionService, private router: Router) {}

  ngOnInit() {
    this.isAdminLoggedIn = this.adminSessionService.isAdminLoggedIn();
    this.adminUser = this.adminSessionService.getAdminUser();
  }

  logout() {
    this.adminSessionService.logout();
    this.isAdminLoggedIn = false;
    this.adminUser = null;
    this.router.navigate(['/login']);
  }
}
