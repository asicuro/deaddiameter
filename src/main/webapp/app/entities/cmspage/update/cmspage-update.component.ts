import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICmspage, Cmspage } from '../cmspage.model';
import { CmspageService } from '../service/cmspage.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICmsroles } from 'app/entities/cmsroles/cmsroles.model';
import { CmsrolesService } from 'app/entities/cmsroles/service/cmsroles.service';
import { Cmslanguage } from 'app/entities/enumerations/cmslanguage.model';

@Component({
  selector: 'jhi-cmspage-update',
  templateUrl: './cmspage-update.component.html',
})
export class CmspageUpdateComponent implements OnInit {
  isSaving = false;
  cmslanguageValues = Object.keys(Cmslanguage);

  cmsrolesSharedCollection: ICmsroles[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    alias: [null, [Validators.required]],
    content: [],
    created: [],
    published: [],
    order: [],
    active: [],
    language: [],
    lastModified: [],
    cmsroles: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected cmspageService: CmspageService,
    protected cmsrolesService: CmsrolesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cmspage }) => {
      if (cmspage.id === undefined) {
        const today = dayjs().startOf('day');
        cmspage.created = today;
        cmspage.lastModified = today;
      }

      this.updateForm(cmspage);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('deadDiameterApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cmspage = this.createFromForm();
    if (cmspage.id !== undefined) {
      this.subscribeToSaveResponse(this.cmspageService.update(cmspage));
    } else {
      this.subscribeToSaveResponse(this.cmspageService.create(cmspage));
    }
  }

  trackCmsrolesById(index: number, item: ICmsroles): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICmspage>>): void {
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

  protected updateForm(cmspage: ICmspage): void {
    this.editForm.patchValue({
      id: cmspage.id,
      name: cmspage.name,
      alias: cmspage.alias,
      content: cmspage.content,
      created: cmspage.created ? cmspage.created.format(DATE_TIME_FORMAT) : null,
      published: cmspage.published,
      order: cmspage.order,
      active: cmspage.active,
      language: cmspage.language,
      lastModified: cmspage.lastModified ? cmspage.lastModified.format(DATE_TIME_FORMAT) : null,
      cmsroles: cmspage.cmsroles,
    });

    this.cmsrolesSharedCollection = this.cmsrolesService.addCmsrolesToCollectionIfMissing(
      this.cmsrolesSharedCollection,
      ...(cmspage.cmsroles ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cmsrolesService
      .query()
      .pipe(map((res: HttpResponse<ICmsroles[]>) => res.body ?? []))
      .pipe(
        map((cmsroles: ICmsroles[]) =>
          this.cmsrolesService.addCmsrolesToCollectionIfMissing(cmsroles, ...(this.editForm.get('cmsroles')!.value ?? []))
        )
      )
      .subscribe((cmsroles: ICmsroles[]) => (this.cmsrolesSharedCollection = cmsroles));
  }

  protected createFromForm(): ICmspage {
    return {
      ...new Cmspage(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      alias: this.editForm.get(['alias'])!.value,
      content: this.editForm.get(['content'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      published: this.editForm.get(['published'])!.value,
      order: this.editForm.get(['order'])!.value,
      active: this.editForm.get(['active'])!.value,
      language: this.editForm.get(['language'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      cmsroles: this.editForm.get(['cmsroles'])!.value,
    };
  }
}
