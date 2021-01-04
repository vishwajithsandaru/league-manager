import { TestBed } from '@angular/core/testing';

import { UniversityLeagueService } from './university-league.service';

describe('UniversityLeagueService', () => {
  let service: UniversityLeagueService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UniversityLeagueService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
