import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CmsmenuDetailComponent } from './cmsmenu-detail.component';

describe('Cmsmenu Management Detail Component', () => {
  let comp: CmsmenuDetailComponent;
  let fixture: ComponentFixture<CmsmenuDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CmsmenuDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cmsmenu: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CmsmenuDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CmsmenuDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cmsmenu on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cmsmenu).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
