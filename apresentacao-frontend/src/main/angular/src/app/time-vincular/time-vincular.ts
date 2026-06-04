import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, ActivatedRouteSnapshot, ResolveData, Router, RouterLink, RouterStateSnapshot } from '@angular/router';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-time-vincular',
  imports: [FormsModule, RouterLink],
  templateUrl: './time-vincular.html',
  styleUrl: './time-vincular.css'
})
export class TimeVincular implements OnInit {
  static readonly ID = 'id';
  static readonly RECURSO = 'recurso';

  timeId: any;
  termoBusca = '';
  resultados: any[] = [];
  selecionado: any = null;
  funcao = '';
  dataInicio = '';
  dataLimite = '';
  exemplos: any[] = [];
  private readonly responsavelId = 1;

  constructor(
    private readonly http: HttpClient,
    private readonly rota: ActivatedRoute,
    private readonly router: Router
  ) {}

  ngOnInit() {
    this.timeId = this.rota.snapshot.params[TimeVincular.ID];
    const state = history.state?.vinculo;
    if (state) {
      this.selecionado = { id: state.profissionalId, nome: state.nomeProfissional };
      this.funcao = state.funcao ?? '';
      this.dataInicio = state.dataInicio ?? '';
      this.dataLimite = state.dataLimiteContrato ?? '';
    }
    // carregar alguns profissionais para exibir como exemplos
    this.http.get<any[]>('/backend/profissional/pesquisa?nome=')
      .pipe(catchError(() => of([])))
      .subscribe(r => this.exemplos = r.slice(0, 3));
  }

  buscar() {
    if (this.termoBusca.length < 2) { this.resultados = []; return; }
    this.http.get<any[]>(`/backend/profissional/pesquisa?nome=${this.termoBusca}`)
      .subscribe(r => this.resultados = r);
  }

  selecionar(p: any) {
    this.selecionado = p;
    this.termoBusca = '';
    this.resultados = [];
  }

  limpar() { this.selecionado = null; }

  vincular() {
    if (!this.selecionado) { alert('Selecione um profissional.'); return; }
    if (!this.funcao) { alert('Informe a função no time.'); return; }
    if (!this.dataInicio) { alert('Informe a data de início.'); return; }
    this.http.post(
      `/backend/time/${this.timeId}/vincular-profissional?responsavelId=${this.responsavelId}`,
      { profissionalId: this.selecionado.id, funcao: this.funcao, dataInicio: this.dataInicio, dataLimiteContrato: this.dataLimite || null }
    ).subscribe({
      next: () => this.router.navigate(['/time', this.timeId, 'detalhes']),
      error: (e) => alert(e.error?.message ?? 'Erro ao vincular.')
    });
  }

  tipoLabel(tipo: string): string {
    const m: any = { JOGADOR: 'Jogador', TREINADOR: 'Treinador', AUXILIAR_TECNICO: 'Auxiliar Técnico', PREPARADOR_FISICO: 'Prep. Física', MEDICO: 'Médico' };
    return m[tipo] ?? tipo;
  }
}

export const TIME_VINCULAR_RESOLVEDORES: ResolveData = {};
TIME_VINCULAR_RESOLVEDORES[TimeVincular.RECURSO] = (_r: ActivatedRouteSnapshot, _s: RouterStateSnapshot) => of({});
