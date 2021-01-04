import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-league-table',
  templateUrl: './league-table.component.html',
  styleUrls: ['./league-table.component.scss']
})
export class LeagueTableComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute) { }

  public season: string;

  ngOnInit(): void {
    let season = this.activatedRoute.snapshot.paramMap.get("season");
    this.season = season;
  }

}
