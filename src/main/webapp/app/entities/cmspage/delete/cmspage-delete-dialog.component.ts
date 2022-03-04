import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICmspage } from '../cmspage.model';
import { CmspageService } from '../service/cmspage.service';

@Component({
  templateUrl: './cmspage-delete-dialog.component.html',
})
export class CmspageDeleteDialogComponent {
  cmspage?: ICmspage;

  constructor(protected cmspageService: CmspageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cmspageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
