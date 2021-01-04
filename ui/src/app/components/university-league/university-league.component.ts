import {Component, Input, OnInit} from '@angular/core';
import {LeagueTable} from "../../models/LeagueTable";
import {MatDialog} from "@angular/material/dialog";
import {DialogComponent} from "../dialog/dialog.component";
import {MatchResponse} from "../../models/MatchResponse";
import {UniversityLeagueService} from "../../services/university-league.service";

@Component({
  selector: 'app-university-league',
  templateUrl: './university-league.component.html',
  styleUrls: ['./university-league.component.scss']
})
export class UniversityLeagueComponent implements OnInit {

  dataSet: LeagueTable[] = [];

  constructor(private universityLeagueService: UniversityLeagueService, private dialog: MatDialog) { }

  @Input() season: string;

  ngOnInit(): void {
    this.populateTable();
  }

  public populateTable(){
    this.universityLeagueService.getLeagueTable(this.season).subscribe(data=>{

      if(data.length == 0){
        this.dialog.open(DialogComponent);
      }
      else{
        this.dataSet = [...data];
      }
    })
  }

  public createRandomMatch(){
    let response: MatchResponse;
    this.universityLeagueService.addRandomMatch(this.season).subscribe(data=>{
      response = data;
    });
    if(response.status == "" || response.status == null){
      this.dialog.open(DialogComponent);
    }else{
      this.populateTable();
    }
  }
}
