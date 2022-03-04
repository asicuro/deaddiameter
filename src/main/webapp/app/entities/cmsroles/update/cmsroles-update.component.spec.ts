import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CmsrolesService } from '../service/cmsroles.service';
import { ICmsroles, Cmsroles } from '../cmsroles.model';

import { CmsrolesUpdateComponent } from './cmsroles-update.component';

describe('Cmsroles Management Update Component', () => {
  let comp: CmsrolesUpdateComponent;
  let fixture: ComponentFixture<CmsrolesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cmsrolesService: CmsrolesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CmsrolesUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CmsrolesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CmsrolesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cmsrolesService = TestBed.inject(CmsrolesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cmsroles: ICmsroles = { id: 456 };

      activatedRoute.data = of({ cmsroles });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cmsroles));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cmsroles>>();
      const cmsroles = { id: 123 };
      jest.spyOn(cmsrolesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cmsroles });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cmsroles }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cmsrolesService.update).toHaveBeenCalledWith(cmsroles);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cmsroles>>();
      const cmsroles = new Cmsroles();
      jest.spyOn(cmsrolesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cmsroles });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cmsroles }));
      saveSubject.complete();

      // THEN
      expect(cmsrolesService.create).toHaveBeenCalledWith(cmsroles);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cmsroles>>();
      const cmsroles = { id: 123 };
      jest.spyOn(cmsrolesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cmsroles });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cmsrolesService.update).toHaveBeenCalledWith(cmsroles);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
