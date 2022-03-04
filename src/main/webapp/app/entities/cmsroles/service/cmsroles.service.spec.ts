import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICmsroles, Cmsroles } from '../cmsroles.model';

import { CmsrolesService } from './cmsroles.service';

describe('Cmsroles Service', () => {
  let service: CmsrolesService;
  let httpMock: HttpTestingController;
  let elemDefault: ICmsroles;
  let expectedResult: ICmsroles | ICmsroles[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CmsrolesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      active: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Cmsroles', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Cmsroles()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cmsroles', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          active: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cmsroles', () => {
      const patchObject = Object.assign(
        {
          active: true,
        },
        new Cmsroles()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cmsroles', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          active: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Cmsroles', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCmsrolesToCollectionIfMissing', () => {
      it('should add a Cmsroles to an empty array', () => {
        const cmsroles: ICmsroles = { id: 123 };
        expectedResult = service.addCmsrolesToCollectionIfMissing([], cmsroles);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cmsroles);
      });

      it('should not add a Cmsroles to an array that contains it', () => {
        const cmsroles: ICmsroles = { id: 123 };
        const cmsrolesCollection: ICmsroles[] = [
          {
            ...cmsroles,
          },
          { id: 456 },
        ];
        expectedResult = service.addCmsrolesToCollectionIfMissing(cmsrolesCollection, cmsroles);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cmsroles to an array that doesn't contain it", () => {
        const cmsroles: ICmsroles = { id: 123 };
        const cmsrolesCollection: ICmsroles[] = [{ id: 456 }];
        expectedResult = service.addCmsrolesToCollectionIfMissing(cmsrolesCollection, cmsroles);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cmsroles);
      });

      it('should add only unique Cmsroles to an array', () => {
        const cmsrolesArray: ICmsroles[] = [{ id: 123 }, { id: 456 }, { id: 16358 }];
        const cmsrolesCollection: ICmsroles[] = [{ id: 123 }];
        expectedResult = service.addCmsrolesToCollectionIfMissing(cmsrolesCollection, ...cmsrolesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cmsroles: ICmsroles = { id: 123 };
        const cmsroles2: ICmsroles = { id: 456 };
        expectedResult = service.addCmsrolesToCollectionIfMissing([], cmsroles, cmsroles2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cmsroles);
        expect(expectedResult).toContain(cmsroles2);
      });

      it('should accept null and undefined values', () => {
        const cmsroles: ICmsroles = { id: 123 };
        expectedResult = service.addCmsrolesToCollectionIfMissing([], null, cmsroles, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cmsroles);
      });

      it('should return initial array if no Cmsroles is added', () => {
        const cmsrolesCollection: ICmsroles[] = [{ id: 123 }];
        expectedResult = service.addCmsrolesToCollectionIfMissing(cmsrolesCollection, undefined, null);
        expect(expectedResult).toEqual(cmsrolesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
