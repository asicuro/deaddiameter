import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICmsmenu } from '../cmsmenu.model';
import { CmsmenuService } from '../service/cmsmenu.service';

@Component({
  templateUrl: './cmsmenu-delete-dialog.component.html',
})
export class CmsmenuDeleteDialogComponent {
  cmsmenu?: ICmsmenu;

  constructor(protected cmsmenuService: CmsmenuService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cmsmenuService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
