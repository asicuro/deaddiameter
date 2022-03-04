import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CmsrolesComponent } from './list/cmsroles.component';
import { CmsrolesDetailComponent } from './detail/cmsroles-detail.component';
import { CmsrolesUpdateComponent } from './update/cmsroles-update.component';
import { CmsrolesDeleteDialogComponent } from './delete/cmsroles-delete-dialog.component';
import { CmsrolesRoutingModule } from './route/cmsroles-routing.module';

@NgModule({
  imports: [SharedModule, CmsrolesRoutingModule],
  declarations: [CmsrolesComponent, CmsrolesDetailComponent, CmsrolesUpdateComponent, CmsrolesDeleteDialogComponent],
  entryComponents: [CmsrolesDeleteDialogComponent],
})
export class CmsrolesModule {}
