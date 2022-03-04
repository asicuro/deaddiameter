import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CmspageComponent } from './list/cmspage.component';
import { CmspageDetailComponent } from './detail/cmspage-detail.component';
import { CmspageUpdateComponent } from './update/cmspage-update.component';
import { CmspageDeleteDialogComponent } from './delete/cmspage-delete-dialog.component';
import { CmspageRoutingModule } from './route/cmspage-routing.module';

@NgModule({
  imports: [SharedModule, CmspageRoutingModule],
  declarations: [CmspageComponent, CmspageDetailComponent, CmspageUpdateComponent, CmspageDeleteDialogComponent],
  entryComponents: [CmspageDeleteDialogComponent],
})
export class CmspageModule {}
