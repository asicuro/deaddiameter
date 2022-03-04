import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICmspage, Cmspage } from '../cmspage.model';
import { CmspageService } from '../service/cmspage.service';

@Injectable({ providedIn: 'root' })
export class CmspageRoutingResolveService implements Resolve<ICmspage> {
  constructor(protected service: CmspageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICmspage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cmspage: HttpResponse<Cmspage>) => {
          if (cmspage.body) {
            return of(cmspage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cmspage());
  }
}
