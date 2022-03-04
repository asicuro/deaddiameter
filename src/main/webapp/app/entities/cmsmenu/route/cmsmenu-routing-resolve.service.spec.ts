import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICmsmenu, Cmsmenu } from '../cmsmenu.model';
import { CmsmenuService } from '../service/cmsmenu.service';

import { CmsmenuRoutingResolveService } from './cmsmenu-routing-resolve.service';

describe('Cmsmenu routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CmsmenuRoutingResolveService;
  let service: CmsmenuService;
  let resultCmsmenu: ICmsmenu | undefined;

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
    routingResolveService = TestBed.inject(CmsmenuRoutingResolveService);
    service = TestBed.inject(CmsmenuService);
    resultCmsmenu = undefined;
  });

  describe('resolve', () => {
    it('should return ICmsmenu returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCmsmenu = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCmsmenu).toEqual({ id: 123 });
    });

    it('should return new ICmsmenu if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCmsmenu = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCmsmenu).toEqual(new Cmsmenu());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Cmsmenu })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCmsmenu = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCmsmenu).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
