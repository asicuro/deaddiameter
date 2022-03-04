import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICmsroles } from '../cmsroles.model';
import { CmsrolesService } from '../service/cmsroles.service';

@Component({
  templateUrl: './cmsroles-delete-dialog.component.html',
})
export class CmsrolesDeleteDialogComponent {
  cmsroles?: ICmsroles;

  constructor(protected cmsrolesService: CmsrolesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cmsrolesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
