import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Cmslanguage } from 'app/entities/enumerations/cmslanguage.model';
import { ICmspage, Cmspage } from '../cmspage.model';

import { CmspageService } from './cmspage.service';

describe('Cmspage Service', () => {
  let service: CmspageService;
  let httpMock: HttpTestingController;
  let elemDefault: ICmspage;
  let expectedResult: ICmspage | ICmspage[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CmspageService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      alias: 'AAAAAAA',
      content: 'AAAAAAA',
      created: currentDate,
      published: false,
      order: 0,
      active: false,
      language: Cmslanguage.ITALIAN,
      lastModified: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          created: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Cmspage', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          created: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.create(new Cmspage()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cmspage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          alias: 'BBBBBB',
          content: 'BBBBBB',
          created: currentDate.format(DATE_TIME_FORMAT),
          published: true,
          order: 1,
          active: true,
          language: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cmspage', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          alias: 'BBBBBB',
          content: 'BBBBBB',
          published: true,
          language: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        new Cmspage()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          created: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cmspage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          alias: 'BBBBBB',
          content: 'BBBBBB',
          created: currentDate.format(DATE_TIME_FORMAT),
          published: true,
          order: 1,
          active: true,
          language: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Cmspage', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCmspageToCollectionIfMissing', () => {
      it('should add a Cmspage to an empty array', () => {
        const cmspage: ICmspage = { id: 123 };
        expectedResult = service.addCmspageToCollectionIfMissing([], cmspage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cmspage);
      });

      it('should not add a Cmspage to an array that contains it', () => {
        const cmspage: ICmspage = { id: 123 };
        const cmspageCollection: ICmspage[] = [
          {
            ...cmspage,
          },
          { id: 456 },
        ];
        expectedResult = service.addCmspageToCollectionIfMissing(cmspageCollection, cmspage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cmspage to an array that doesn't contain it", () => {
        const cmspage: ICmspage = { id: 123 };
        const cmspageCollection: ICmspage[] = [{ id: 456 }];
        expectedResult = service.addCmspageToCollectionIfMissing(cmspageCollection, cmspage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cmspage);
      });

      it('should add only unique Cmspage to an array', () => {
        const cmspageArray: ICmspage[] = [{ id: 123 }, { id: 456 }, { id: 33882 }];
        const cmspageCollection: ICmspage[] = [{ id: 123 }];
        expectedResult = service.addCmspageToCollectionIfMissing(cmspageCollection, ...cmspageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cmspage: ICmspage = { id: 123 };
        const cmspage2: ICmspage = { id: 456 };
        expectedResult = service.addCmspageToCollectionIfMissing([], cmspage, cmspage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cmspage);
        expect(expectedResult).toContain(cmspage2);
      });

      it('should accept null and undefined values', () => {
        const cmspage: ICmspage = { id: 123 };
        expectedResult = service.addCmspageToCollectionIfMissing([], null, cmspage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cmspage);
      });

      it('should return initial array if no Cmspage is added', () => {
        const cmspageCollection: ICmspage[] = [{ id: 123 }];
        expectedResult = service.addCmspageToCollectionIfMissing(cmspageCollection, undefined, null);
        expect(expectedResult).toEqual(cmspageCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
