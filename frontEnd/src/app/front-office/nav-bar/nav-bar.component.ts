  import { Component, OnInit } from '@angular/core';
  import { UserSessionService } from '../../service/user-session.service';
  import { User} from '../../model/user';
  import {Key}  from '../../model/appstates';
  import { Router } from '@angular/router';

  @Component({
    selector: 'app-nav-bar',
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.css']
  })
  export class NavBarComponent implements OnInit {
    user: User | null = null;
    isDropdownOpen = false;

    constructor(
      private userSessionService: UserSessionService,
      private router: Router
    ) {}

    ngOnInit(): void {
      this.userSessionService.user$.subscribe((user) => {
        this.user = user;


      });
    }

    toggleDropdown(): void {
      this.isDropdownOpen = !this.isDropdownOpen;
      console.log("User:", this.user);
      //
      // @ts-ignore
      console.log("roleNames:", this.user?.roleName);
      console.log("permissions:", this.user?.permission);
    }

    logout(): void {
      localStorage.removeItem(Key.TOKEN);
      localStorage.removeItem(Key.REFRESH_TOKEN);
      this.userSessionService.setUser(null);
      this.router.navigate(['/']);
    }
  }
