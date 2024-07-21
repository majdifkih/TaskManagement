import { TestBed } from '@angular/core/testing';

import { ProjectServcieService } from './project-servcie.service';

describe('ProjectServcieService', () => {
  let service: ProjectServcieService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjectServcieService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
