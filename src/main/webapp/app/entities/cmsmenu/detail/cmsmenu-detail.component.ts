import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICmsmenu } from '../cmsmenu.model';

@Component({
  selector: 'jhi-cmsmenu-detail',
  templateUrl: './cmsmenu-detail.component.html',
})
export class CmsmenuDetailComponent implements OnInit {
  cmsmenu: ICmsmenu | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cmsmenu }) => {
      this.cmsmenu = cmsmenu;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
