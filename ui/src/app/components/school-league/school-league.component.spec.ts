import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchoolLeagueComponent } from './school-league.component';

describe('SchoolLeagueComponent', () => {
  let component: SchoolLeagueComponent;
  let fixture: ComponentFixture<SchoolLeagueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SchoolLeagueComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SchoolLeagueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
