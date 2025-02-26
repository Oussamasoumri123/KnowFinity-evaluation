import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FrontOfficeRoutingModule } from './front-office-routing.module';
import { HomeComponent } from './home/home.component';
import { FooterComponent } from './footer/footer.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';

@NgModule({
  declarations: [HomeComponent, FooterComponent, NavBarComponent],
  imports: [
    CommonModule,
    FrontOfficeRoutingModule
  ]
})
export class FrontOfficeModule { }
