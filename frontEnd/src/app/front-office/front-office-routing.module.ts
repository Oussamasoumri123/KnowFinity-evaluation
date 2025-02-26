import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {HomeContentComponent} from './home-content/home-content.component';
import {CourseComponent} from "./course/course.component";
import {EvaluationComponent} from "./evaluation/evaluation.component";
import {NotFoundComponent} from "./not-found/not-found.component";


const routes: Routes = [
  {path: 'home', component: HomeComponent, children: [{path: '', component: HomeContentComponent}]},
  {path:'course',component:HomeComponent,children:[{path: '', component: CourseComponent}]},
  {path:'evaluation',component:HomeComponent,children:[{path: '', component: EvaluationComponent}]},
  {path: '**', component: HomeComponent,children:[{path: '', component: NotFoundComponent}]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FrontOfficeRoutingModule {
}
