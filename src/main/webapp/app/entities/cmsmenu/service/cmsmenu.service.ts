import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICmsmenu, getCmsmenuIdentifier } from '../cmsmenu.model';

export type EntityResponseType = HttpResponse<ICmsmenu>;
export type EntityArrayResponseType = HttpResponse<ICmsmenu[]>;

@Injectable({ providedIn: 'root' })
export class CmsmenuService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cmsmenus');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cmsmenu: ICmsmenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cmsmenu);
    return this.http
      .post<ICmsmenu>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cmsmenu: ICmsmenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cmsmenu);
    return this.http
      .put<ICmsmenu>(`${this.resourceUrl}/${getCmsmenuIdentifier(cmsmenu) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cmsmenu: ICmsmenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cmsmenu);
    return this.http
      .patch<ICmsmenu>(`${this.resourceUrl}/${getCmsmenuIdentifier(cmsmenu) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICmsmenu>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICmsmenu[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCmsmenuToCollectionIfMissing(cmsmenuCollection: ICmsmenu[], ...cmsmenusToCheck: (ICmsmenu | null | undefined)[]): ICmsmenu[] {
    const cmsmenus: ICmsmenu[] = cmsmenusToCheck.filter(isPresent);
    if (cmsmenus.length > 0) {
      const cmsmenuCollectionIdentifiers = cmsmenuCollection.map(cmsmenuItem => getCmsmenuIdentifier(cmsmenuItem)!);
      const cmsmenusToAdd = cmsmenus.filter(cmsmenuItem => {
        const cmsmenuIdentifier = getCmsmenuIdentifier(cmsmenuItem);
        if (cmsmenuIdentifier == null || cmsmenuCollectionIdentifiers.includes(cmsmenuIdentifier)) {
          return false;
        }
        cmsmenuCollectionIdentifiers.push(cmsmenuIdentifier);
        return true;
      });
      return [...cmsmenusToAdd, ...cmsmenuCollection];
    }
    return cmsmenuCollection;
  }

  protected convertDateFromClient(cmsmenu: ICmsmenu): ICmsmenu {
    return Object.assign({}, cmsmenu, {
      lastModified: cmsmenu.lastModified?.isValid() ? cmsmenu.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((cmsmenu: ICmsmenu) => {
        cmsmenu.lastModified = cmsmenu.lastModified ? dayjs(cmsmenu.lastModified) : undefined;
      });
    }
    return res;
  }
}
