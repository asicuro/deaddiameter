import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Cmslanguage } from 'app/entities/enumerations/cmslanguage.model';
import { ICmsmenu, Cmsmenu } from '../cmsmenu.model';

import { CmsmenuService } from './cmsmenu.service';

describe('Cmsmenu Service', () => {
  let service: CmsmenuService;
  let httpMock: HttpTestingController;
  let elemDefault: ICmsmenu;
  let expectedResult: ICmsmenu | ICmsmenu[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CmsmenuService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      title: 'AAAAAAA',
      description: 'AAAAAAA',
      css: 'AAAAAAA',
      menuType: 0,
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
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Cmsmenu', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.create(new Cmsmenu()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cmsmenu', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          title: 'BBBBBB',
          description: 'BBBBBB',
          css: 'BBBBBB',
          menuType: 1,
          order: 1,
          active: true,
          language: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cmsmenu', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          active: true,
          language: 'BBBBBB',
        },
        new Cmsmenu()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cmsmenu', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          title: 'BBBBBB',
          description: 'BBBBBB',
          css: 'BBBBBB',
          menuType: 1,
          order: 1,
          active: true,
          language: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
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

    it('should delete a Cmsmenu', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCmsmenuToCollectionIfMissing', () => {
      it('should add a Cmsmenu to an empty array', () => {
        const cmsmenu: ICmsmenu = { id: 123 };
        expectedResult = service.addCmsmenuToCollectionIfMissing([], cmsmenu);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cmsmenu);
      });

      it('should not add a Cmsmenu to an array that contains it', () => {
        const cmsmenu: ICmsmenu = { id: 123 };
        const cmsmenuCollection: ICmsmenu[] = [
          {
            ...cmsmenu,
          },
          { id: 456 },
        ];
        expectedResult = service.addCmsmenuToCollectionIfMissing(cmsmenuCollection, cmsmenu);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cmsmenu to an array that doesn't contain it", () => {
        const cmsmenu: ICmsmenu = { id: 123 };
        const cmsmenuCollection: ICmsmenu[] = [{ id: 456 }];
        expectedResult = service.addCmsmenuToCollectionIfMissing(cmsmenuCollection, cmsmenu);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cmsmenu);
      });

      it('should add only unique Cmsmenu to an array', () => {
        const cmsmenuArray: ICmsmenu[] = [{ id: 123 }, { id: 456 }, { id: 18100 }];
        const cmsmenuCollection: ICmsmenu[] = [{ id: 123 }];
        expectedResult = service.addCmsmenuToCollectionIfMissing(cmsmenuCollection, ...cmsmenuArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cmsmenu: ICmsmenu = { id: 123 };
        const cmsmenu2: ICmsmenu = { id: 456 };
        expectedResult = service.addCmsmenuToCollectionIfMissing([], cmsmenu, cmsmenu2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cmsmenu);
        expect(expectedResult).toContain(cmsmenu2);
      });

      it('should accept null and undefined values', () => {
        const cmsmenu: ICmsmenu = { id: 123 };
        expectedResult = service.addCmsmenuToCollectionIfMissing([], null, cmsmenu, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cmsmenu);
      });

      it('should return initial array if no Cmsmenu is added', () => {
        const cmsmenuCollection: ICmsmenu[] = [{ id: 123 }];
        expectedResult = service.addCmsmenuToCollectionIfMissing(cmsmenuCollection, undefined, null);
        expect(expectedResult).toEqual(cmsmenuCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
