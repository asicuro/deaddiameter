import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICmsroles, getCmsrolesIdentifier } from '../cmsroles.model';

export type EntityResponseType = HttpResponse<ICmsroles>;
export type EntityArrayResponseType = HttpResponse<ICmsroles[]>;

@Injectable({ providedIn: 'root' })
export class CmsrolesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cmsroles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cmsroles: ICmsroles): Observable<EntityResponseType> {
    return this.http.post<ICmsroles>(this.resourceUrl, cmsroles, { observe: 'response' });
  }

  update(cmsroles: ICmsroles): Observable<EntityResponseType> {
    return this.http.put<ICmsroles>(`${this.resourceUrl}/${getCmsrolesIdentifier(cmsroles) as number}`, cmsroles, { observe: 'response' });
  }

  partialUpdate(cmsroles: ICmsroles): Observable<EntityResponseType> {
    return this.http.patch<ICmsroles>(`${this.resourceUrl}/${getCmsrolesIdentifier(cmsroles) as number}`, cmsroles, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICmsroles>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICmsroles[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCmsrolesToCollectionIfMissing(cmsrolesCollection: ICmsroles[], ...cmsrolesToCheck: (ICmsroles | null | undefined)[]): ICmsroles[] {
    const cmsroles: ICmsroles[] = cmsrolesToCheck.filter(isPresent);
    if (cmsroles.length > 0) {
      const cmsrolesCollectionIdentifiers = cmsrolesCollection.map(cmsrolesItem => getCmsrolesIdentifier(cmsrolesItem)!);
      const cmsrolesToAdd = cmsroles.filter(cmsrolesItem => {
        const cmsrolesIdentifier = getCmsrolesIdentifier(cmsrolesItem);
        if (cmsrolesIdentifier == null || cmsrolesCollectionIdentifiers.includes(cmsrolesIdentifier)) {
          return false;
        }
        cmsrolesCollectionIdentifiers.push(cmsrolesIdentifier);
        return true;
      });
      return [...cmsrolesToAdd, ...cmsrolesCollection];
    }
    return cmsrolesCollection;
  }
}
