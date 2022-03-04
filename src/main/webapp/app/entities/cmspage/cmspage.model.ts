import dayjs from 'dayjs/esm';
import { ICmsroles } from 'app/entities/cmsroles/cmsroles.model';
import { ICmsmenu } from 'app/entities/cmsmenu/cmsmenu.model';
import { Cmslanguage } from 'app/entities/enumerations/cmslanguage.model';

export interface ICmspage {
  id?: number;
  name?: string;
  alias?: string;
  content?: string | null;
  created?: dayjs.Dayjs | null;
  published?: boolean | null;
  order?: number | null;
  active?: boolean | null;
  language?: Cmslanguage | null;
  lastModified?: dayjs.Dayjs | null;
  cmsroles?: ICmsroles[] | null;
  cmsmenus?: ICmsmenu[] | null;
}

export class Cmspage implements ICmspage {
  constructor(
    public id?: number,
    public name?: string,
    public alias?: string,
    public content?: string | null,
    public created?: dayjs.Dayjs | null,
    public published?: boolean | null,
    public order?: number | null,
    public active?: boolean | null,
    public language?: Cmslanguage | null,
    public lastModified?: dayjs.Dayjs | null,
    public cmsroles?: ICmsroles[] | null,
    public cmsmenus?: ICmsmenu[] | null
  ) {
    this.published = this.published ?? false;
    this.active = this.active ?? false;
  }
}

export function getCmspageIdentifier(cmspage: ICmspage): number | undefined {
  return cmspage.id;
}
