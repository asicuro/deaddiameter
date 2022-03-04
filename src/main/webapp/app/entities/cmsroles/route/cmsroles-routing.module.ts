import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CmsrolesComponent } from '../list/cmsroles.component';
import { CmsrolesDetailComponent } from '../detail/cmsroles-detail.component';
import { CmsrolesUpdateComponent } from '../update/cmsroles-update.component';
import { CmsrolesRoutingResolveService } from './cmsroles-routing-resolve.service';

const cmsrolesRoute: Routes = [
  {
    path: '',
    component: CmsrolesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CmsrolesDetailComponent,
    resolve: {
      cmsroles: CmsrolesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CmsrolesUpdateComponent,
    resolve: {
      cmsroles: CmsrolesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CmsrolesUpdateComponent,
    resolve: {
      cmsroles: CmsrolesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cmsrolesRoute)],
  exports: [RouterModule],
})
export class CmsrolesRoutingModule {}
