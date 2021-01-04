import {Component, Input, OnInit} from '@angular/core';
import {LeagueTable} from "../../models/LeagueTable";
import {MatDialog} from "@angular/material/dialog";
import {DialogComponent} from "../dialog/dialog.component";
import {MatchResponse} from "../../models/MatchResponse";
import {SchoolLeagueService} from "../../services/school-league.service";

@Component({
  selector: 'app-school-league',
  templateUrl: './school-league.component.html',
  styleUrls: ['./school-league.component.scss']
})
export class SchoolLeagueComponent implements OnInit {

  dataSet: LeagueTable[] = [];

  constructor(private schoolLeagueService: SchoolLeagueService, private dialog: MatDialog) { }

  @Input() season: string;

  ngOnInit(): void {
    this.populateTable();
  }

  public populateTable(){
    this.schoolLeagueService.getLeagueTable(this.season).subscribe(data=>{

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
    this.schoolLeagueService.addRandomMatch(this.season).subscribe(data=>{
      response = data;
    });
    if(response.status == "" || response.status == null){
      this.dialog.open(DialogComponent);
    }else{
      this.populateTable();
    }
  }

}
