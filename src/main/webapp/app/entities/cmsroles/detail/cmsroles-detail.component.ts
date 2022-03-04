import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICmsroles } from '../cmsroles.model';

@Component({
  selector: 'jhi-cmsroles-detail',
  templateUrl: './cmsroles-detail.component.html',
})
export class CmsrolesDetailComponent implements OnInit {
  cmsroles: ICmsroles | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cmsroles }) => {
      this.cmsroles = cmsroles;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
