import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CmspageService } from '../service/cmspage.service';
import { ICmspage, Cmspage } from '../cmspage.model';
import { ICmsroles } from 'app/entities/cmsroles/cmsroles.model';
import { CmsrolesService } from 'app/entities/cmsroles/service/cmsroles.service';

import { CmspageUpdateComponent } from './cmspage-update.component';

describe('Cmspage Management Update Component', () => {
  let comp: CmspageUpdateComponent;
  let fixture: ComponentFixture<CmspageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cmspageService: CmspageService;
  let cmsrolesService: CmsrolesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CmspageUpdateComponent],
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
      .overrideTemplate(CmspageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CmspageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cmspageService = TestBed.inject(CmspageService);
    cmsrolesService = TestBed.inject(CmsrolesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cmsroles query and add missing value', () => {
      const cmspage: ICmspage = { id: 456 };
      const cmsroles: ICmsroles[] = [{ id: 46534 }];
      cmspage.cmsroles = cmsroles;

      const cmsrolesCollection: ICmsroles[] = [{ id: 44766 }];
      jest.spyOn(cmsrolesService, 'query').mockReturnValue(of(new HttpResponse({ body: cmsrolesCollection })));
      const additionalCmsroles = [...cmsroles];
      const expectedCollection: ICmsroles[] = [...additionalCmsroles, ...cmsrolesCollection];
      jest.spyOn(cmsrolesService, 'addCmsrolesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cmspage });
      comp.ngOnInit();

      expect(cmsrolesService.query).toHaveBeenCalled();
      expect(cmsrolesService.addCmsrolesToCollectionIfMissing).toHaveBeenCalledWith(cmsrolesCollection, ...additionalCmsroles);
      expect(comp.cmsrolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cmspage: ICmspage = { id: 456 };
      const cmsroles: ICmsroles = { id: 1788 };
      cmspage.cmsroles = [cmsroles];

      activatedRoute.data = of({ cmspage });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cmspage));
      expect(comp.cmsrolesSharedCollection).toContain(cmsroles);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cmspage>>();
      const cmspage = { id: 123 };
      jest.spyOn(cmspageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cmspage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cmspage }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cmspageService.update).toHaveBeenCalledWith(cmspage);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cmspage>>();
      const cmspage = new Cmspage();
      jest.spyOn(cmspageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cmspage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cmspage }));
      saveSubject.complete();

      // THEN
      expect(cmspageService.create).toHaveBeenCalledWith(cmspage);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cmspage>>();
      const cmspage = { id: 123 };
      jest.spyOn(cmspageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cmspage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cmspageService.update).toHaveBeenCalledWith(cmspage);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCmsrolesById', () => {
      it('Should return tracked Cmsroles primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCmsrolesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCmsroles', () => {
      it('Should return option if no Cmsroles is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCmsroles(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Cmsroles for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCmsroles(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Cmsroles is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCmsroles(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
