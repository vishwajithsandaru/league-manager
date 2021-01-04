import { Injectable } from '@angular/core';
import {LeagueTable} from "../models/LeagueTable";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {MatchResponse} from "../models/MatchResponse";

@Injectable({
  providedIn: 'root'
})
export class PremierLeagueService {

  constructor(private http: HttpClient) { }

  public getLeagueTable(season: string): Observable<LeagueTable[]>{
    let seasonYear = season.slice(0, -3);
    console.log(seasonYear);
    let tableDetails = this.http.get<LeagueTable[]>("/api/premier/display-league-table/"+seasonYear);
    return tableDetails;
  }

  public addRandomMatch(season: String): Observable<MatchResponse>{
    let seasonYear = season.slice(0, -3);
    let matchRes = this.http.post<MatchResponse>("api/premier/add-random-match/"+seasonYear, "");
    return matchRes;
  }

}
