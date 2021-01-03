import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UniversityLeagueComponent } from './university-league.component';

describe('UniversityLeagueComponent', () => {
  let component: UniversityLeagueComponent;
  let fixture: ComponentFixture<UniversityLeagueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UniversityLeagueComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UniversityLeagueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
