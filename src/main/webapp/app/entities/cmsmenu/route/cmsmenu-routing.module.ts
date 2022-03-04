import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CmsmenuComponent } from '../list/cmsmenu.component';
import { CmsmenuDetailComponent } from '../detail/cmsmenu-detail.component';
import { CmsmenuUpdateComponent } from '../update/cmsmenu-update.component';
import { CmsmenuRoutingResolveService } from './cmsmenu-routing-resolve.service';

const cmsmenuRoute: Routes = [
  {
    path: '',
    component: CmsmenuComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CmsmenuDetailComponent,
    resolve: {
      cmsmenu: CmsmenuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CmsmenuUpdateComponent,
    resolve: {
      cmsmenu: CmsmenuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CmsmenuUpdateComponent,
    resolve: {
      cmsmenu: CmsmenuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cmsmenuRoute)],
  exports: [RouterModule],
})
export class CmsmenuRoutingModule {}
