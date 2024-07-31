import { TestBed } from '@angular/core/testing';

import { BacklogServiceService } from './backlog-service.service';

describe('BacklogServiceService', () => {
  let service: BacklogServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BacklogServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
