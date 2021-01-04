import { TestBed } from '@angular/core/testing';

import { SchoolLeagueService } from './school-league.service';

describe('SchoolLeagueService', () => {
  let service: SchoolLeagueService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SchoolLeagueService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
