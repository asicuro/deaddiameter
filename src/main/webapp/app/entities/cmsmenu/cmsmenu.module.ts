import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CmsmenuComponent } from './list/cmsmenu.component';
import { CmsmenuDetailComponent } from './detail/cmsmenu-detail.component';
import { CmsmenuUpdateComponent } from './update/cmsmenu-update.component';
import { CmsmenuDeleteDialogComponent } from './delete/cmsmenu-delete-dialog.component';
import { CmsmenuRoutingModule } from './route/cmsmenu-routing.module';

@NgModule({
  imports: [SharedModule, CmsmenuRoutingModule],
  declarations: [CmsmenuComponent, CmsmenuDetailComponent, CmsmenuUpdateComponent, CmsmenuDeleteDialogComponent],
  entryComponents: [CmsmenuDeleteDialogComponent],
})
export class CmsmenuModule {}
