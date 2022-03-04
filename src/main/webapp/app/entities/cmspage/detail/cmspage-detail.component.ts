import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICmspage } from '../cmspage.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-cmspage-detail',
  templateUrl: './cmspage-detail.component.html',
})
export class CmspageDetailComponent implements OnInit {
  cmspage: ICmspage | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cmspage }) => {
      this.cmspage = cmspage;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
