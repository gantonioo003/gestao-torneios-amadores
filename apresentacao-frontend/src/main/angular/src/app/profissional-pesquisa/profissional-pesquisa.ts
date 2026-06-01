import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, ResolveData, RouterModule } from '@angular/router';
import { catchError, of } from 'rxjs';

@Component({ selector: 'app-profissional-pesquisa', imports: [FormsModule, RouterModule], templateUrl: './profissional-pesquisa.html' })
export class ProfissionalPesquisa implements OnInit {
  static readonly RECURSO = 'recurso';
  lista: any[] = [];
  termoBusca = '';

  constructor(private readonly rota: ActivatedRoute, private readonly http: HttpClient) {}

  ngOnInit() { this.lista = this.rota.snapshot.data[ProfissionalPesquisa.RECURSO] ?? []; }

  buscar() {
    this.http.get<any[]>(`/backend/profissional/pesquisa?nome=${this.termoBusca}`)
      .pipe(catchError(() => of([])))
      .subscribe(r => this.lista = r);
  }
}

export const PROFISSIONAL_PESQUISA_RESOLVEDORES: ResolveData = {};
PROFISSIONAL_PESQUISA_RESOLVEDORES[ProfissionalPesquisa.RECURSO] = () =>
  inject(HttpClient).get('/backend/profissional/pesquisa?nome=').pipe(catchError(() => of([])));
