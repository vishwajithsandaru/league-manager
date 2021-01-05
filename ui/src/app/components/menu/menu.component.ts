import { Component, OnInit } from '@angular/core';
import {MenuService} from "../../services/menu.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  constructor(private _menuService: MenuService, private router: Router) { }

  public seasons: string[] = [];
  public selectedValue: string;

  ngOnInit(): void {
    this._menuService.getSeasons()
      .subscribe(data => {
        this.seasons = data;
      });
  }

  public leagueTableNav(){
    this.router.navigate(['league-table', this.selectedValue]);
  }

  public matchDetailsView(){
    this.router.navigate(['match-details', this.selectedValue]);
  }

}
