import {AfterViewInit, Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {LeagueTable} from "../../models/LeagueTable";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit, AfterViewInit, OnChanges {

  @Input() dataSet: LeagueTable[];

  displayedColumns: string[] = ['name', 'points', 'goalDifference', 'noOfWins', 'noOfDraws', 'noOfDefeats'];

  dataSource = null;

  constructor() { }

  ngOnInit(): void{
    this.dataSource = new MatTableDataSource(this.dataSet);
  }

  @ViewChild(MatSort) sort: MatSort;

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.dataSource = new MatTableDataSource(this.dataSet);
    this.dataSource.sort = this.sort;
  }

}
