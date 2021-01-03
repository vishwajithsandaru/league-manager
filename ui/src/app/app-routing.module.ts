import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LeagueTableComponent} from "./components/league-table/league-table.component";
import {MatchDetailsComponent} from "./components/match-details/match-details.component";
import {MenuComponent} from "./components/menu/menu.component";

const routes: Routes = [
  {
    path:"",
    component: MenuComponent
  },
  {
    path: "league-table",
    component: LeagueTableComponent
  },
  {
    path: "match-details",
    component: MatchDetailsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponents = [LeagueTableComponent, MatchDetailsComponent, MenuComponent]
