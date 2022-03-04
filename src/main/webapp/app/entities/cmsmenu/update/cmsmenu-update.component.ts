import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICmsmenu, Cmsmenu } from '../cmsmenu.model';
import { CmsmenuService } from '../service/cmsmenu.service';
import { ICmsroles } from 'app/entities/cmsroles/cmsroles.model';
import { CmsrolesService } from 'app/entities/cmsroles/service/cmsroles.service';
import { ICmspage } from 'app/entities/cmspage/cmspage.model';
import { CmspageService } from 'app/entities/cmspage/service/cmspage.service';
import { Cmslanguage } from 'app/entities/enumerations/cmslanguage.model';

@Component({
  selector: 'jhi-cmsmenu-update',
  templateUrl: './cmsmenu-update.component.html',
})
export class CmsmenuUpdateComponent implements OnInit {
  isSaving = false;
  cmslanguageValues = Object.keys(Cmslanguage);

  cmsmenusCollection: ICmsmenu[] = [];
  cmsrolesSharedCollection: ICmsroles[] = [];
  cmspagesSharedCollection: ICmspage[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    title: [null, [Validators.required]],
    description: [null, [Validators.required]],
    css: [],
    menuType: [],
    order: [],
    active: [],
    language: [],
    lastModified: [],
    cmsmenu: [],
    cmsroles: [],
    cmspages: [],
  });

  constructor(
    protected cmsmenuService: CmsmenuService,
    protected cmsrolesService: CmsrolesService,
    protected cmspageService: CmspageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cmsmenu }) => {
      if (cmsmenu.id === undefined) {
        const today = dayjs().startOf('day');
        cmsmenu.lastModified = today;
      }

      this.updateForm(cmsmenu);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cmsmenu = this.createFromForm();
    if (cmsmenu.id !== undefined) {
      this.subscribeToSaveResponse(this.cmsmenuService.update(cmsmenu));
    } else {
      this.subscribeToSaveResponse(this.cmsmenuService.create(cmsmenu));
    }
  }

  trackCmsmenuById(index: number, item: ICmsmenu): number {
    return item.id!;
  }

  trackCmsrolesById(index: number, item: ICmsroles): number {
    return item.id!;
  }

  trackCmspageById(index: number, item: ICmspage): number {
    return item.id!;
  }

  getSelectedCmsroles(option: ICmsroles, selectedVals?: ICmsroles[]): ICmsroles {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedCmspage(option: ICmspage, selectedVals?: ICmspage[]): ICmspage {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICmsmenu>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cmsmenu: ICmsmenu): void {
    this.editForm.patchValue({
      id: cmsmenu.id,
      name: cmsmenu.name,
      title: cmsmenu.title,
      description: cmsmenu.description,
      css: cmsmenu.css,
      menuType: cmsmenu.menuType,
      order: cmsmenu.order,
      active: cmsmenu.active,
      language: cmsmenu.language,
      lastModified: cmsmenu.lastModified ? cmsmenu.lastModified.format(DATE_TIME_FORMAT) : null,
      cmsmenu: cmsmenu.cmsmenu,
      cmsroles: cmsmenu.cmsroles,
      cmspages: cmsmenu.cmspages,
    });

    this.cmsmenusCollection = this.cmsmenuService.addCmsmenuToCollectionIfMissing(this.cmsmenusCollection, cmsmenu.cmsmenu);
    this.cmsrolesSharedCollection = this.cmsrolesService.addCmsrolesToCollectionIfMissing(
      this.cmsrolesSharedCollection,
      ...(cmsmenu.cmsroles ?? [])
    );
    this.cmspagesSharedCollection = this.cmspageService.addCmspageToCollectionIfMissing(
      this.cmspagesSharedCollection,
      ...(cmsmenu.cmspages ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cmsmenuService
      .query({ filter: 'cmsmenu-is-null' })
      .pipe(map((res: HttpResponse<ICmsmenu[]>) => res.body ?? []))
      .pipe(
        map((cmsmenus: ICmsmenu[]) => this.cmsmenuService.addCmsmenuToCollectionIfMissing(cmsmenus, this.editForm.get('cmsmenu')!.value))
      )
      .subscribe((cmsmenus: ICmsmenu[]) => (this.cmsmenusCollection = cmsmenus));

    this.cmsrolesService
      .query()
      .pipe(map((res: HttpResponse<ICmsroles[]>) => res.body ?? []))
      .pipe(
        map((cmsroles: ICmsroles[]) =>
          this.cmsrolesService.addCmsrolesToCollectionIfMissing(cmsroles, ...(this.editForm.get('cmsroles')!.value ?? []))
        )
      )
      .subscribe((cmsroles: ICmsroles[]) => (this.cmsrolesSharedCollection = cmsroles));

    this.cmspageService
      .query()
      .pipe(map((res: HttpResponse<ICmspage[]>) => res.body ?? []))
      .pipe(
        map((cmspages: ICmspage[]) =>
          this.cmspageService.addCmspageToCollectionIfMissing(cmspages, ...(this.editForm.get('cmspages')!.value ?? []))
        )
      )
      .subscribe((cmspages: ICmspage[]) => (this.cmspagesSharedCollection = cmspages));
  }

  protected createFromForm(): ICmsmenu {
    return {
      ...new Cmsmenu(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      css: this.editForm.get(['css'])!.value,
      menuType: this.editForm.get(['menuType'])!.value,
      order: this.editForm.get(['order'])!.value,
      active: this.editForm.get(['active'])!.value,
      language: this.editForm.get(['language'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      cmsmenu: this.editForm.get(['cmsmenu'])!.value,
      cmsroles: this.editForm.get(['cmsroles'])!.value,
      cmspages: this.editForm.get(['cmspages'])!.value,
    };
  }
}
