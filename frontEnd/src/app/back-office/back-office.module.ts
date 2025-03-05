import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BackOfficeRoutingModule } from './back-office-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SideBarComponent } from './side-bar/side-bar.component';
import { FooterComponent } from './footer/footer.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { DashboardContentComponent } from './dashboard-content/dashboard-content.component';
import { ForumComponent } from './forum/forum.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { LoginComponent } from './login/login.component';
import {FormsModule} from "@angular/forms";
import { ProfileComponent } from './profile/profile.component';
import { UsersComponent } from './users/users.component';


@NgModule({
  declarations: [
    DashboardComponent,
    SideBarComponent,
    FooterComponent,
    NavBarComponent,
    DashboardContentComponent,
    ForumComponent,
    NotFoundComponent,
    LoginComponent,
    ProfileComponent,
    UsersComponent,
  ],
  exports: [
    FooterComponent,
    NavBarComponent
  ],
  imports: [
    CommonModule,
    BackOfficeRoutingModule,
    FormsModule
  ]
})
export class BackOfficeModule {

}
