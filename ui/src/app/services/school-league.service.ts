import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {LeagueTable} from "../models/LeagueTable";
import {MatchResponse} from "../models/MatchResponse";

@Injectable({
  providedIn: 'root'
})
export class SchoolLeagueService {

  constructor(private http: HttpClient) {
  }

  public getLeagueTable(season: string): Observable<LeagueTable[]> {
    let seasonYear = season.slice(0, -3);
    console.log(seasonYear);
    let tableDetails = this.http.get<LeagueTable[]>("/api/school/display-league-table/" + seasonYear);
    return tableDetails;
  }

  public addRandomMatch(season: String): Observable<MatchResponse> {
    let seasonYear = season.slice(0, -3);
    let matchRes = this.http.post<MatchResponse>("api/school/add-random-match/" + seasonYear, "");
    return matchRes;
  }
}

