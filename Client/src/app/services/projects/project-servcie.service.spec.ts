import { TestBed } from '@angular/core/testing';

import { ProjectService} from './project-servcie.service';

describe('ProjectServcieService', () => {
  let service: ProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjectService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
