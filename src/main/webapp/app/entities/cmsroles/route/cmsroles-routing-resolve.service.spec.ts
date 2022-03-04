import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICmsroles, Cmsroles } from '../cmsroles.model';
import { CmsrolesService } from '../service/cmsroles.service';

import { CmsrolesRoutingResolveService } from './cmsroles-routing-resolve.service';

describe('Cmsroles routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CmsrolesRoutingResolveService;
  let service: CmsrolesService;
  let resultCmsroles: ICmsroles | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(CmsrolesRoutingResolveService);
    service = TestBed.inject(CmsrolesService);
    resultCmsroles = undefined;
  });

  describe('resolve', () => {
    it('should return ICmsroles returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCmsroles = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCmsroles).toEqual({ id: 123 });
    });

    it('should return new ICmsroles if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCmsroles = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCmsroles).toEqual(new Cmsroles());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Cmsroles })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCmsroles = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCmsroles).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
