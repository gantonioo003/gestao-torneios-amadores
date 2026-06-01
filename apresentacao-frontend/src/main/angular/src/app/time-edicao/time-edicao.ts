import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, ActivatedRouteSnapshot, ResolveData, Router, RouterModule, RouterStateSnapshot } from '@angular/router';
import { catchError, of } from 'rxjs';

@Component({ selector: 'app-time-edicao', imports: [FormsModule, RouterModule], templateUrl: './time-edicao.html' })
export class TimeEdicao {
  static readonly ID = 'id';
  static readonly RECURSO = 'recurso';

  recurso: any = {};
  elenco: any[] = [];
  mostrarFormVinculo = false;
  buscaProfissional = '';
  resultadosBusca: any[] = [];
  vinculoForm: any = {};
  private responsavelId = 1;

  constructor(private readonly http: HttpClient, private readonly rota: ActivatedRoute, private readonly router: Router) {}

  ngOnInit() {
    const data = this.rota.snapshot.data[TimeEdicao.RECURSO];
    if (data) { this.recurso = data; this.elenco = data.elenco ?? []; }
  }

  salvar() {
    const url = this.recurso.id ? `/backend/time/${this.recurso.id}/salvar` : '/backend/time/salvar';
    this.http.post(url, { ...this.recurso, responsavelId: this.responsavelId })
      .subscribe({ next: () => this.router.navigate(['/time/pesquisa']), error: e => alert(e.error?.mensagem ?? 'Erro') });
  }

  buscarProfissionais() {
    if (this.buscaProfissional.length < 2) { this.resultadosBusca = []; return; }
    this.http.get<any[]>(`/backend/profissional/pesquisa?nome=${this.buscaProfissional}`)
      .pipe(catchError(() => of([])))
      .subscribe(r => this.resultadosBusca = r);
  }

  selecionarProfissional(p: any) {
    this.vinculoForm = { profissionalId: p.id, nomeProfissional: p.nome, funcao: '', dataInicio: '', dataLimiteContrato: '' };
    this.buscaProfissional = p.nome;
    this.resultadosBusca = [];
  }

  vincularProfissional() {
    this.http.post(`/backend/time/${this.recurso.id}/vincular-profissional?responsavelId=${this.responsavelId}`, this.vinculoForm)
      .subscribe({ next: () => this.recarregarElenco(), error: e => alert(e.error?.mensagem ?? 'Erro') });
  }

  prepararEdicaoVinculo(v: any) { this.vinculoForm = { ...v }; this.mostrarFormVinculo = true; }

  removerProfissional(profissionalId: number) {
    if (!confirm('Remover profissional do elenco?')) return;
    this.http.post(`/backend/time/${this.recurso.id}/remover-profissional/${profissionalId}?responsavelId=${this.responsavelId}`, {})
      .subscribe({ next: () => this.recarregarElenco(), error: e => alert(e.error?.mensagem ?? 'Erro') });
  }

  cancelarVinculo() { this.mostrarFormVinculo = false; this.vinculoForm = {}; this.buscaProfissional = ''; }

  private recarregarElenco() {
    this.cancelarVinculo();
    this.http.get<any>(`/backend/time/${this.recurso.id}/edicao`)
      .pipe(catchError(() => of({ elenco: [] })))
      .subscribe(r => this.elenco = r.elenco ?? []);
  }
}

export const TIME_EDICAO_RESOLVEDORES: ResolveData = {};
TIME_EDICAO_RESOLVEDORES[TimeEdicao.RECURSO] = (rota: ActivatedRouteSnapshot) => {
  const id = rota.params[TimeEdicao.ID];
  if (!id) return of(null);
  return inject(HttpClient).get(`/backend/time/${id}/edicao`).pipe(catchError(() => of({})));
};
