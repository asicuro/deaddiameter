import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICmspage, getCmspageIdentifier } from '../cmspage.model';

export type EntityResponseType = HttpResponse<ICmspage>;
export type EntityArrayResponseType = HttpResponse<ICmspage[]>;

@Injectable({ providedIn: 'root' })
export class CmspageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cmspages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cmspage: ICmspage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cmspage);
    return this.http
      .post<ICmspage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cmspage: ICmspage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cmspage);
    return this.http
      .put<ICmspage>(`${this.resourceUrl}/${getCmspageIdentifier(cmspage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cmspage: ICmspage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cmspage);
    return this.http
      .patch<ICmspage>(`${this.resourceUrl}/${getCmspageIdentifier(cmspage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICmspage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICmspage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCmspageToCollectionIfMissing(cmspageCollection: ICmspage[], ...cmspagesToCheck: (ICmspage | null | undefined)[]): ICmspage[] {
    const cmspages: ICmspage[] = cmspagesToCheck.filter(isPresent);
    if (cmspages.length > 0) {
      const cmspageCollectionIdentifiers = cmspageCollection.map(cmspageItem => getCmspageIdentifier(cmspageItem)!);
      const cmspagesToAdd = cmspages.filter(cmspageItem => {
        const cmspageIdentifier = getCmspageIdentifier(cmspageItem);
        if (cmspageIdentifier == null || cmspageCollectionIdentifiers.includes(cmspageIdentifier)) {
          return false;
        }
        cmspageCollectionIdentifiers.push(cmspageIdentifier);
        return true;
      });
      return [...cmspagesToAdd, ...cmspageCollection];
    }
    return cmspageCollection;
  }

  protected convertDateFromClient(cmspage: ICmspage): ICmspage {
    return Object.assign({}, cmspage, {
      created: cmspage.created?.isValid() ? cmspage.created.toJSON() : undefined,
      lastModified: cmspage.lastModified?.isValid() ? cmspage.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((cmspage: ICmspage) => {
        cmspage.created = cmspage.created ? dayjs(cmspage.created) : undefined;
        cmspage.lastModified = cmspage.lastModified ? dayjs(cmspage.lastModified) : undefined;
      });
    }
    return res;
  }
}
