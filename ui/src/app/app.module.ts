import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule, routingComponents } from './app-routing.module';
import { AppComponent } from './app.component';
import { PremierLeagueComponent } from './components/premier-league/premier-league.component';
import { UniversityLeagueComponent } from './components/university-league/university-league.component';
import { SchoolLeagueComponent } from './components/school-league/school-league.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatTabsModule} from "@angular/material/tabs";
import { MenuComponent } from './components/menu/menu.component';
import {MatCard, MatCardModule} from "@angular/material/card";
import {MatSelectModule} from "@angular/material/select";
import {MatButtonModule} from "@angular/material/button";
import {MenuService} from "./services/menu.service";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";
import {MatTableModule} from "@angular/material/table";
import { TableComponent } from './components/table/table.component';
import {MatSortModule} from "@angular/material/sort";
import { DialogComponent } from './components/dialog/dialog.component';
import {MatDialogModule} from "@angular/material/dialog";

@NgModule({
  declarations: [
    AppComponent,
    PremierLeagueComponent,
    UniversityLeagueComponent,
    SchoolLeagueComponent,
    MenuComponent,
    routingComponents,
    TableComponent,
    DialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatTabsModule,
    MatCardModule,
    MatSelectModule,
    MatButtonModule,
    HttpClientModule,
    FormsModule,
    MatIconModule,
    MatTableModule,
    MatSortModule,
    MatDialogModule
  ],
  providers: [MenuService],
  bootstrap: [AppComponent]
})
export class AppModule { }
