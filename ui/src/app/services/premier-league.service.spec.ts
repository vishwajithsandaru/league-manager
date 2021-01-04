import { TestBed } from '@angular/core/testing';

import { PremierLeagueService } from './premier-league.service';

describe('PremierLeagueService', () => {
  let service: PremierLeagueService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PremierLeagueService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
