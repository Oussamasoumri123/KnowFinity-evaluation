import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import {DashboardContentComponent} from "./dashboard-content/dashboard-content.component";
import {ForumComponent} from "./forum/forum.component";
import {NotFoundComponent} from "./not-found/not-found.component";


const routes: Routes = [
  { path: '', component: DashboardComponent ,children : [{path:'' , component:DashboardContentComponent}] },
  { path: 'forum', component: DashboardComponent ,children : [{path:'' , component:ForumComponent}] },
  { path: '**', component: DashboardComponent ,children : [{path:'' , component:NotFoundComponent}] },


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BackOfficeRoutingModule {}

