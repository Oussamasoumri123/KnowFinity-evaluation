import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FrontOfficeRoutingModule } from './front-office-routing.module';

// Import FrontOffice Components
import { HomeComponent } from './home/home.component';
import { HomeContentComponent } from './home-content/home-content.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { FooterComponent } from './footer/footer.component';
import {RouterModule} from "@angular/router";
import { CourseComponent } from './course/course.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { EvaluationComponent } from './evaluation/evaluation.component';

@NgModule({
  declarations: [
    HomeComponent,
    HomeContentComponent,
    NavBarComponent,  // ✅ Correct Front-Office Navbar
    FooterComponent, CourseComponent, NotFoundComponent, EvaluationComponent   // ✅ Correct Front-Office Footer
  ],
  imports: [
    CommonModule,
    FrontOfficeRoutingModule,
  ]
})
export class FrontOfficeModule { }
