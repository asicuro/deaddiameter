import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICmsroles, Cmsroles } from '../cmsroles.model';
import { CmsrolesService } from '../service/cmsroles.service';

@Component({
  selector: 'jhi-cmsroles-update',
  templateUrl: './cmsroles-update.component.html',
})
export class CmsrolesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    active: [],
  });

  constructor(protected cmsrolesService: CmsrolesService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cmsroles }) => {
      this.updateForm(cmsroles);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cmsroles = this.createFromForm();
    if (cmsroles.id !== undefined) {
      this.subscribeToSaveResponse(this.cmsrolesService.update(cmsroles));
    } else {
      this.subscribeToSaveResponse(this.cmsrolesService.create(cmsroles));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICmsroles>>): void {
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

  protected updateForm(cmsroles: ICmsroles): void {
    this.editForm.patchValue({
      id: cmsroles.id,
      name: cmsroles.name,
      description: cmsroles.description,
      active: cmsroles.active,
    });
  }

  protected createFromForm(): ICmsroles {
    return {
      ...new Cmsroles(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      active: this.editForm.get(['active'])!.value,
    };
  }
}
