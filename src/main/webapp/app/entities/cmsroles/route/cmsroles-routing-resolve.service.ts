import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICmsroles, Cmsroles } from '../cmsroles.model';
import { CmsrolesService } from '../service/cmsroles.service';

@Injectable({ providedIn: 'root' })
export class CmsrolesRoutingResolveService implements Resolve<ICmsroles> {
  constructor(protected service: CmsrolesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICmsroles> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cmsroles: HttpResponse<Cmsroles>) => {
          if (cmsroles.body) {
            return of(cmsroles.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cmsroles());
  }
}
