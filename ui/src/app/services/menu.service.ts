import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class MenuService {

  constructor(private http: HttpClient) { }

  getSeasons():Observable<string[]>{
    return this.http.get<string[]>("/api/common/list-seasons");
  }



}
