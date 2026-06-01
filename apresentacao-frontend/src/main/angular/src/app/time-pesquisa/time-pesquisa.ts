import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, ResolveData, Router, RouterModule, RouterStateSnapshot } from '@angular/router';
import { catchError, of } from 'rxjs';

@Component({ selector: 'app-time-pesquisa', imports: [RouterModule], templateUrl: './time-pesquisa.html' })
export class TimePesquisa implements OnInit {
  static readonly RECURSO = 'recurso';
  recurso: any[] = [];
  private responsavelId = 1;

  constructor(private readonly rota: ActivatedRoute, private readonly http: HttpClient, private readonly router: Router) {}

  ngOnInit() { this.recurso = this.rota.snapshot.data[TimePesquisa.RECURSO] ?? []; }

  excluir(id: number) {
    if (!confirm('Confirma a exclusão?')) return;
    this.http.post(`/backend/time/${id}/excluir?responsavelId=${this.responsavelId}`, {}).subscribe({
      next: () => this.http.get<any[]>(`/backend/time/pesquisa?responsavelId=${this.responsavelId}`).subscribe(r => this.recurso = r),
      error: (e) => alert(e.error?.mensagem ?? 'Erro ao excluir')
    });
  }
}

export const TIME_PESQUISA_RESOLVEDORES: ResolveData = {};
TIME_PESQUISA_RESOLVEDORES[TimePesquisa.RECURSO] = () =>
  inject(HttpClient).get('/backend/time/pesquisa?responsavelId=1').pipe(catchError(() => of([])));
