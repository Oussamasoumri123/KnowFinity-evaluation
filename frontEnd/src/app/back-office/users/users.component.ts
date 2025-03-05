import {Component, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {UserService} from "../../service/user-service.service";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  users: User[] = [];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe(response => {
      this.users = response.data.users;
    });
  }
}
