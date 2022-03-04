import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CmspageComponent } from '../list/cmspage.component';
import { CmspageDetailComponent } from '../detail/cmspage-detail.component';
import { CmspageUpdateComponent } from '../update/cmspage-update.component';
import { CmspageRoutingResolveService } from './cmspage-routing-resolve.service';

const cmspageRoute: Routes = [
  {
    path: '',
    component: CmspageComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CmspageDetailComponent,
    resolve: {
      cmspage: CmspageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CmspageUpdateComponent,
    resolve: {
      cmspage: CmspageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CmspageUpdateComponent,
    resolve: {
      cmspage: CmspageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cmspageRoute)],
  exports: [RouterModule],
})
export class CmspageRoutingModule {}
