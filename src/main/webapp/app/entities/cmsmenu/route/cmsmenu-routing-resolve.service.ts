import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICmsmenu, Cmsmenu } from '../cmsmenu.model';
import { CmsmenuService } from '../service/cmsmenu.service';

@Injectable({ providedIn: 'root' })
export class CmsmenuRoutingResolveService implements Resolve<ICmsmenu> {
  constructor(protected service: CmsmenuService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICmsmenu> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cmsmenu: HttpResponse<Cmsmenu>) => {
          if (cmsmenu.body) {
            return of(cmsmenu.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cmsmenu());
  }
}
