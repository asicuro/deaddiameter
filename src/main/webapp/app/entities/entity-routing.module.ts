import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cmsmenu',
        data: { pageTitle: 'deadDiameterApp.cmsmenu.home.title' },
        loadChildren: () => import('./cmsmenu/cmsmenu.module').then(m => m.CmsmenuModule),
      },
      {
        path: 'cmspage',
        data: { pageTitle: 'deadDiameterApp.cmspage.home.title' },
        loadChildren: () => import('./cmspage/cmspage.module').then(m => m.CmspageModule),
      },
      {
        path: 'cmsroles',
        data: { pageTitle: 'deadDiameterApp.cmsroles.home.title' },
        loadChildren: () => import('./cmsroles/cmsroles.module').then(m => m.CmsrolesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
