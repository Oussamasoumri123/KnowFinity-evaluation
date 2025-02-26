import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BackOfficeRoutingModule } from './back-office-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SideBarComponent } from './side-bar/side-bar.component';
import { FooterComponent } from './footer/footer.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { DashboardContentComponent } from './dashboard-content/dashboard-content.component';
import { ForumComponent } from './forum/forum.component';


@NgModule({
  declarations: [
    DashboardComponent,
    SideBarComponent,
    FooterComponent,
    NavBarComponent,
    DashboardContentComponent,
    ForumComponent,
  ],
  exports: [
    FooterComponent,
    NavBarComponent
  ],
  imports: [
    CommonModule,
    BackOfficeRoutingModule
  ]
})
export class BackOfficeModule {

}
