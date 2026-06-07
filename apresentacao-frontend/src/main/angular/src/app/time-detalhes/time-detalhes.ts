import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, ResolveData, Router, RouterLink, RouterStateSnapshot } from '@angular/router';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-time-detalhes',
  imports: [RouterLink],
  templateUrl: './time-detalhes.html',
  styleUrl: './time-detalhes.css'
})
export class TimeDetalhes implements OnInit {
  static readonly ID = 'id';
  static readonly RECURSO = 'recurso';

  time: any = {};
  elenco: any[] = [];
  torneios: any[] = [];
  private readonly responsavelId = 1;

  constructor(
    private readonly http: HttpClient,
    private readonly rota: ActivatedRoute,
    private readonly router: Router
  ) {}

  ngOnInit() {
    const data = this.rota.snapshot.data[TimeDetalhes.RECURSO] ?? {};
    this.time = data.time ?? {};
    this.elenco = data.time?.elenco ?? [];
    this.torneios = data.torneios ?? [];
  }

  editarVinculo(v: any) {
    this.router.navigate(['/time', this.time.id, 'vincular'], { state: { vinculo: v } });
  }

  removerVinculo(profissionalId: number) {
    if (!confirm('Remover profissional do elenco?')) return;
    this.http.post(
      `/backend/time/${this.time.id}/remover-profissional/${profissionalId}?responsavelId=${this.responsavelId}`, {}
    ).subscribe({
      next: () => this.recarregar(),
      error: (e) => alert(e.error?.message ?? 'Erro ao remover.')
    });
  }

  funcaoLabel(funcao: string): string {
    return funcao || 'Sem função definida';
  }

  excluir() {
    if (!confirm('Excluir este time?')) return;
    this.http.post(`/backend/time/${this.time.id}/excluir?responsavelId=${this.responsavelId}`, {})
      .subscribe({
        next: () => this.router.navigate(['/time/pesquisa']),
        error: (e) => alert(e.error?.message ?? 'Erro. Verifique se o time está vinculado a torneios.')
      });
  }

  private recarregar() {
    this.http.get<any>(`/backend/time/${this.time.id}/edicao`)
      .pipe(catchError(() => of({})))
      .subscribe(r => { this.elenco = r.time?.elenco ?? []; });
  }
}

export const TIME_DETALHES_RESOLVEDORES: ResolveData = {};
TIME_DETALHES_RESOLVEDORES[TimeDetalhes.RECURSO] = (rota: ActivatedRouteSnapshot, _s: RouterStateSnapshot) => {
  const id = rota.params[TimeDetalhes.ID];
  return inject(HttpClient).get(`/backend/time/${id}/edicao`).pipe(catchError(() => of({})));
};
