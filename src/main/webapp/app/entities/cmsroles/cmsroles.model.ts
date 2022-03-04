import { ICmspage } from 'app/entities/cmspage/cmspage.model';
import { ICmsmenu } from 'app/entities/cmsmenu/cmsmenu.model';

export interface ICmsroles {
  id?: number;
  name?: string;
  description?: string;
  active?: boolean | null;
  cmspages?: ICmspage[] | null;
  cmsmenus?: ICmsmenu[] | null;
}

export class Cmsroles implements ICmsroles {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public active?: boolean | null,
    public cmspages?: ICmspage[] | null,
    public cmsmenus?: ICmsmenu[] | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getCmsrolesIdentifier(cmsroles: ICmsroles): number | undefined {
  return cmsroles.id;
}
