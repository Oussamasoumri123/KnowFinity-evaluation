import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FrontOfficeRoutingModule } from './front-office-routing.module';
import { HomeComponent } from './home/home.component';

@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    FrontOfficeRoutingModule
  ]
})
export class FrontOfficeModule {
  constructor() {
    this.loadBootstrapStyles();
  }

  private loadBootstrapStyles() {
    const link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = 'assets/Templates/KnowFinity-FrontOffice/assets/bootstrap/css/bootstrap.min.css';
    link.id = 'front-office-bootstrap'; // Add an ID to remove it later
    document.head.appendChild(link);
  }
}
