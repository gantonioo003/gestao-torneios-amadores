import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, ActivatedRouteSnapshot, ResolveData, Router, RouterModule } from '@angular/router';
import { catchError, of } from 'rxjs';

@Component({ selector: 'app-profissional-edicao', imports: [FormsModule, RouterModule], templateUrl: './profissional-edicao.html' })
export class ProfissionalEdicao {
  static readonly ID = 'id';
  static readonly RECURSO = 'recurso';

  recurso: any = {};
  historico: any[] = [];
  mostrarFormCarreira = false;
  carreiraForm: any = {};
  private cadastranteId = 1;

  constructor(private readonly http: HttpClient, private readonly rota: ActivatedRoute, private readonly router: Router) {}

  ngOnInit() {
    const data = this.rota.snapshot.data[ProfissionalEdicao.RECURSO];
    if (data) { this.recurso = data; this.historico = data.historico ?? []; }
  }

  salvar() {
    const url = this.recurso.id
      ? `/backend/profissional/${this.recurso.id}/salvar?cadastranteId=${this.cadastranteId}`
      : '/backend/profissional/salvar';
    this.http.post(url, { ...this.recurso, cadastranteId: this.cadastranteId })
      .subscribe({ next: () => this.router.navigate(['/profissional/pesquisa']), error: e => alert(e.error?.mensagem ?? 'Erro') });
  }

  excluir() {
    if (!confirm('Remover este perfil?')) return;
    this.http.post(`/backend/profissional/${this.recurso.id}/excluir?cadastranteId=${this.cadastranteId}`, {})
      .subscribe({ next: () => this.router.navigate(['/profissional/pesquisa']), error: e => alert(e.error?.mensagem ?? 'Erro') });
  }

  adicionarCarreira() {
    this.http.post(`/backend/profissional/${this.recurso.id}/adicionar-carreira?cadastranteId=${this.cadastranteId}`, this.carreiraForm)
      .subscribe({ next: () => { this.mostrarFormCarreira = false; this.carreiraForm = {}; this.recarregarHistorico(); }, error: e => alert(e.error?.mensagem ?? 'Erro') });
  }

  removerCarreira(registroId: number) {
    if (!confirm('Remover este registro?')) return;
    this.http.post(`/backend/profissional/${this.recurso.id}/remover-carreira/${registroId}?cadastranteId=${this.cadastranteId}`, {})
      .subscribe({ next: () => this.recarregarHistorico(), error: e => alert(e.error?.mensagem ?? 'Erro') });
  }

  private recarregarHistorico() {
    this.http.get<any>(`/backend/profissional/${this.recurso.id}/edicao`)
      .pipe(catchError(() => of({ historico: [] })))
      .subscribe(r => this.historico = r.historico ?? []);
  }
}

export const PROFISSIONAL_EDICAO_RESOLVEDORES: ResolveData = {};
PROFISSIONAL_EDICAO_RESOLVEDORES[ProfissionalEdicao.RECURSO] = (rota: ActivatedRouteSnapshot) => {
  const id = rota.params[ProfissionalEdicao.ID];
  if (!id) return of(null);
  return inject(HttpClient).get(`/backend/profissional/${id}/edicao`).pipe(catchError(() => of({})));
};
