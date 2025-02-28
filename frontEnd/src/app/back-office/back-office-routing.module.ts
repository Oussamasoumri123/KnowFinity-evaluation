import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import {DashboardContentComponent} from "./dashboard-content/dashboard-content.component";
import {ForumComponent} from "./forum/forum.component";
import {NotFoundComponent} from "./not-found/not-found.component";
import {LoginComponent} from "./login/login.component";
import {ProfileComponent} from "./profile/profile.component";
import {adminAuthGuard} from "../guards/admin-auth.guard";
import {HomeComponent} from "../front-office/home/home.component";
import {HomeContentComponent} from "../front-office/home-content/home-content.component";


const routes: Routes = [

  { path: '', component: DashboardComponent ,children : [{path:'' , component:DashboardContentComponent}] , canActivate: [adminAuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent ,children : [{path:'' , component:DashboardContentComponent}] , canActivate: [adminAuthGuard] },
  { path: 'profile', component: DashboardComponent ,children : [{path:'' , component:ProfileComponent}],canActivate: [adminAuthGuard] },
  { path: 'forum', component: DashboardComponent ,children : [{path:'' , component:ForumComponent}],canActivate: [adminAuthGuard] },
  { path: '**', component: HomeComponent ,children : [{path:'' , component:HomeContentComponent}] },


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BackOfficeRoutingModule {}

