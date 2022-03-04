import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CmsmenuService } from '../service/cmsmenu.service';
import { ICmsmenu, Cmsmenu } from '../cmsmenu.model';
import { ICmsroles } from 'app/entities/cmsroles/cmsroles.model';
import { CmsrolesService } from 'app/entities/cmsroles/service/cmsroles.service';
import { ICmspage } from 'app/entities/cmspage/cmspage.model';
import { CmspageService } from 'app/entities/cmspage/service/cmspage.service';

import { CmsmenuUpdateComponent } from './cmsmenu-update.component';

describe('Cmsmenu Management Update Component', () => {
  let comp: CmsmenuUpdateComponent;
  let fixture: ComponentFixture<CmsmenuUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cmsmenuService: CmsmenuService;
  let cmsrolesService: CmsrolesService;
  let cmspageService: CmspageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CmsmenuUpdateComponent],
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
      .overrideTemplate(CmsmenuUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CmsmenuUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cmsmenuService = TestBed.inject(CmsmenuService);
    cmsrolesService = TestBed.inject(CmsrolesService);
    cmspageService = TestBed.inject(CmspageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call cmsmenu query and add missing value', () => {
      const cmsmenu: ICmsmenu = { id: 456 };
      const cmsmenu: ICmsmenu = { id: 21196 };
      cmsmenu.cmsmenu = cmsmenu;

      const cmsmenuCollection: ICmsmenu[] = [{ id: 23832 }];
      jest.spyOn(cmsmenuService, 'query').mockReturnValue(of(new HttpResponse({ body: cmsmenuCollection })));
      const expectedCollection: ICmsmenu[] = [cmsmenu, ...cmsmenuCollection];
      jest.spyOn(cmsmenuService, 'addCmsmenuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cmsmenu });
      comp.ngOnInit();

      expect(cmsmenuService.query).toHaveBeenCalled();
      expect(cmsmenuService.addCmsmenuToCollectionIfMissing).toHaveBeenCalledWith(cmsmenuCollection, cmsmenu);
      expect(comp.cmsmenusCollection).toEqual(expectedCollection);
    });

    it('Should call Cmsroles query and add missing value', () => {
      const cmsmenu: ICmsmenu = { id: 456 };
      const cmsroles: ICmsroles[] = [{ id: 61391 }];
      cmsmenu.cmsroles = cmsroles;

      const cmsrolesCollection: ICmsroles[] = [{ id: 63176 }];
      jest.spyOn(cmsrolesService, 'query').mockReturnValue(of(new HttpResponse({ body: cmsrolesCollection })));
      const additionalCmsroles = [...cmsroles];
      const expectedCollection: ICmsroles[] = [...additionalCmsroles, ...cmsrolesCollection];
      jest.spyOn(cmsrolesService, 'addCmsrolesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cmsmenu });
      comp.ngOnInit();

      expect(cmsrolesService.query).toHaveBeenCalled();
      expect(cmsrolesService.addCmsrolesToCollectionIfMissing).toHaveBeenCalledWith(cmsrolesCollection, ...additionalCmsroles);
      expect(comp.cmsrolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cmspage query and add missing value', () => {
      const cmsmenu: ICmsmenu = { id: 456 };
      const cmspages: ICmspage[] = [{ id: 66917 }];
      cmsmenu.cmspages = cmspages;

      const cmspageCollection: ICmspage[] = [{ id: 77677 }];
      jest.spyOn(cmspageService, 'query').mockReturnValue(of(new HttpResponse({ body: cmspageCollection })));
      const additionalCmspages = [...cmspages];
      const expectedCollection: ICmspage[] = [...additionalCmspages, ...cmspageCollection];
      jest.spyOn(cmspageService, 'addCmspageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cmsmenu });
      comp.ngOnInit();

      expect(cmspageService.query).toHaveBeenCalled();
      expect(cmspageService.addCmspageToCollectionIfMissing).toHaveBeenCalledWith(cmspageCollection, ...additionalCmspages);
      expect(comp.cmspagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cmsmenu: ICmsmenu = { id: 456 };
      const cmsmenu: ICmsmenu = { id: 90478 };
      cmsmenu.cmsmenu = cmsmenu;
      const cmsroles: ICmsroles = { id: 33764 };
      cmsmenu.cmsroles = [cmsroles];
      const cmspages: ICmspage = { id: 62160 };
      cmsmenu.cmspages = [cmspages];

      activatedRoute.data = of({ cmsmenu });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cmsmenu));
      expect(comp.cmsmenusCollection).toContain(cmsmenu);
      expect(comp.cmsrolesSharedCollection).toContain(cmsroles);
      expect(comp.cmspagesSharedCollection).toContain(cmspages);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cmsmenu>>();
      const cmsmenu = { id: 123 };
      jest.spyOn(cmsmenuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cmsmenu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cmsmenu }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cmsmenuService.update).toHaveBeenCalledWith(cmsmenu);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cmsmenu>>();
      const cmsmenu = new Cmsmenu();
      jest.spyOn(cmsmenuService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cmsmenu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cmsmenu }));
      saveSubject.complete();

      // THEN
      expect(cmsmenuService.create).toHaveBeenCalledWith(cmsmenu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cmsmenu>>();
      const cmsmenu = { id: 123 };
      jest.spyOn(cmsmenuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cmsmenu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cmsmenuService.update).toHaveBeenCalledWith(cmsmenu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCmsmenuById', () => {
      it('Should return tracked Cmsmenu primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCmsmenuById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCmsrolesById', () => {
      it('Should return tracked Cmsroles primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCmsrolesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCmspageById', () => {
      it('Should return tracked Cmspage primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCmspageById(0, entity);
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

    describe('getSelectedCmspage', () => {
      it('Should return option if no Cmspage is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCmspage(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Cmspage for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCmspage(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Cmspage is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCmspage(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
