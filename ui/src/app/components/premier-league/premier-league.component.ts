import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {LeagueTable} from "../../models/LeagueTable";
import {PremierLeagueService} from "../../services/premier-league.service";
import {MatDialog} from "@angular/material/dialog";
import {MatchResponse} from "../../models/MatchResponse";

@Component({
  selector: 'app-premier-league',
  templateUrl: './premier-league.component.html',
  styleUrls: ['./premier-league.component.scss']
})
export class PremierLeagueComponent implements OnInit {

  dataSet: LeagueTable[] = [];

  constructor(private premierLeagueService: PremierLeagueService, private dialog: MatDialog) { }

  @Input() season: string;

  ngOnInit(): void {
   this.populateTable();
  }

  public populateTable(){
    this.premierLeagueService.getLeagueTable(this.season).subscribe(data=>{

      if(data.length == 0){

      }
      else{
        this.dataSet = [...data];
      }
    });
  }

  public createRandomMatch(){
    let response: MatchResponse;
    this.premierLeagueService.addRandomMatch(this.season).subscribe(data=>{
      response = data;
      this.populateTable();
    });

  }


}
