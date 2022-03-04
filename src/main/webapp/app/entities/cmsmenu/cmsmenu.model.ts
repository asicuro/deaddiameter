import dayjs from 'dayjs/esm';
import { ICmsroles } from 'app/entities/cmsroles/cmsroles.model';
import { ICmspage } from 'app/entities/cmspage/cmspage.model';
import { Cmslanguage } from 'app/entities/enumerations/cmslanguage.model';

export interface ICmsmenu {
  id?: number;
  name?: string;
  title?: string;
  description?: string;
  css?: string | null;
  menuType?: number | null;
  order?: number | null;
  active?: boolean | null;
  language?: Cmslanguage | null;
  lastModified?: dayjs.Dayjs | null;
  cmsmenu?: ICmsmenu | null;
  cmsroles?: ICmsroles[] | null;
  cmspages?: ICmspage[] | null;
}

export class Cmsmenu implements ICmsmenu {
  constructor(
    public id?: number,
    public name?: string,
    public title?: string,
    public description?: string,
    public css?: string | null,
    public menuType?: number | null,
    public order?: number | null,
    public active?: boolean | null,
    public language?: Cmslanguage | null,
    public lastModified?: dayjs.Dayjs | null,
    public cmsmenu?: ICmsmenu | null,
    public cmsroles?: ICmsroles[] | null,
    public cmspages?: ICmspage[] | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getCmsmenuIdentifier(cmsmenu: ICmsmenu): number | undefined {
  return cmsmenu.id;
}
