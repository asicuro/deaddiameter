import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CmsrolesDetailComponent } from './cmsroles-detail.component';

describe('Cmsroles Management Detail Component', () => {
  let comp: CmsrolesDetailComponent;
  let fixture: ComponentFixture<CmsrolesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CmsrolesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cmsroles: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CmsrolesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CmsrolesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cmsroles on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cmsroles).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
