import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  ActivatedRoute,
  ActivatedRouteSnapshot,
  ResolveData,
  Router,
  RouterLink,
  RouterStateSnapshot
} from '@angular/router';
import { catchError, finalize, of } from 'rxjs';

type ModoIntegrante = 'buscar' | 'cadastrar';

interface TipoIntegrante {
  valor: string;
  nome: string;
  descricao: string;
  icone: string;
}

@Component({
  selector: 'app-time-vincular',
  imports: [FormsModule, RouterLink],
  templateUrl: './time-vincular.html',
  styleUrl: './time-vincular.css'
})
export class TimeVincular implements OnInit {
  static readonly ID = 'id';
  static readonly RECURSO = 'recurso';

  timeId!: number;
  time: any = {};
  elenco: any[] = [];
  modo: ModoIntegrante = 'buscar';
  termoBusca = '';
  resultados: any[] = [];
  exemplos: any[] = [];
  selecionado: any = null;
  nomeNovo = '';
  tipoNovo = '';
  funcao = '';
  dataInicio = '';
  dataLimite = '';
  salvando = false;

  readonly tipos: TipoIntegrante[] = [
    { valor: 'JOGADOR', nome: 'Jogador', descricao: 'Atleta do elenco', icone: 'bi-person-running' },
    { valor: 'TREINADOR', nome: 'Treinador', descricao: 'Responsavel tecnico', icone: 'bi-clipboard2-pulse' },
    { valor: 'AUXILIAR_TECNICO', nome: 'Auxiliar tecnico', descricao: 'Apoio ao treinador', icone: 'bi-person-workspace' },
    { valor: 'PREPARADOR_FISICO', nome: 'Preparador fisico', descricao: 'Condicionamento do elenco', icone: 'bi-activity' },
    { valor: 'MEDICO', nome: 'Medico', descricao: 'Saude e recuperacao', icone: 'bi-heart-pulse' }
  ];

  constructor(
    private readonly http: HttpClient,
    private readonly rota: ActivatedRoute,
    private readonly router: Router
  ) {}

  ngOnInit() {
    this.timeId = Number(this.rota.snapshot.params[TimeVincular.ID]);
    const recurso = this.rota.snapshot.data[TimeVincular.RECURSO] ?? {};
    this.time = recurso.time ?? {};
    this.elenco = recurso.time?.elenco ?? [];
    this.dataInicio = new Date().toISOString().slice(0, 10);

    const vinculo = history.state?.vinculo;
    if (vinculo) {
      this.selecionado = {
        id: vinculo.profissionalId,
        nome: vinculo.nomeProfissional,
        tipo: vinculo.tipoProfissional
      };
      this.funcao = vinculo.funcao ?? '';
      this.dataInicio = vinculo.dataInicio ?? this.dataInicio;
      this.dataLimite = vinculo.dataLimiteContrato ?? '';
      return;
    }

    this.http.get<any[]>('/backend/profissional/pesquisa?nome=')
      .pipe(catchError(() => of([])))
      .subscribe(profissionais => this.exemplos = profissionais.slice(0, 5));
  }

  get editando(): boolean {
    return !!history.state?.vinculo;
  }

  get possuiTreinador(): boolean {
    return this.elenco.some(vinculo => vinculo.tipoProfissional === 'TREINADOR');
  }

  get tiposDisponiveis(): TipoIntegrante[] {
    return this.tipos.filter(tipo => tipo.valor !== 'TREINADOR' || !this.possuiTreinador);
  }

  buscar() {
    if (this.termoBusca.trim().length < 2) {
      this.resultados = [];
      return;
    }
    this.http.get<any[]>(
      `/backend/profissional/pesquisa?nome=${encodeURIComponent(this.termoBusca.trim())}`
    ).pipe(catchError(() => of([]))).subscribe(resultados => this.resultados = resultados);
  }

  selecionar(profissional: any) {
    if (profissional.tipo === 'TREINADOR' && this.possuiTreinador) {
      alert('Este time ja possui treinador. Remova o atual antes de adicionar outro.');
      return;
    }
    this.selecionado = profissional;
    this.termoBusca = '';
    this.resultados = [];
    this.funcao = this.funcoesDisponiveis(profissional.tipo)[0] ?? '';
  }

  limpar() {
    this.selecionado = null;
    this.funcao = '';
  }

  trocarModo(modo: ModoIntegrante) {
    this.modo = modo;
    this.limpar();
    this.nomeNovo = '';
    this.tipoNovo = '';
  }

  selecionarTipoNovo(tipo: string) {
    this.tipoNovo = tipo;
    this.funcao = this.funcoesDisponiveis(tipo)[0] ?? '';
  }

  salvarVinculo() {
    if (!this.selecionado) {
      alert('Selecione um profissional.');
      return;
    }
    if (!this.validarDadosDoVinculo()) return;

    const url = this.editando
      ? `/backend/time/${this.timeId}/editar-vinculo/${this.selecionado.id}`
      : `/backend/time/${this.timeId}/vincular-profissional`;

    this.salvando = true;
    this.http.post(url, {
      profissionalId: this.selecionado.id,
      funcao: this.funcao,
      dataInicio: this.dataInicio,
      dataLimiteContrato: this.dataLimite || null
    }).pipe(finalize(() => this.salvando = false)).subscribe({
      next: () => this.voltarAoTime(),
      error: erro => alert(this.mensagemErro(erro, 'Nao foi possivel atualizar o elenco.'))
    });
  }

  cadastrarEVincular() {
    if (!this.nomeNovo.trim()) {
      alert('Informe o nome completo.');
      return;
    }
    if (!this.tipoNovo) {
      alert('Escolha o tipo de integrante.');
      return;
    }
    if (!this.validarDadosDoVinculo()) return;

    this.salvando = true;
    this.http.post(`/backend/time/${this.timeId}/cadastrar-profissional`, {
      nome: this.nomeNovo.trim(),
      tipo: this.tipoNovo,
      funcao: this.funcao,
      dataInicio: this.dataInicio,
      dataLimiteContrato: this.dataLimite || null
    }).pipe(finalize(() => this.salvando = false)).subscribe({
      next: () => this.voltarAoTime(),
      error: erro => alert(this.mensagemErro(erro, 'Nao foi possivel cadastrar o integrante.'))
    });
  }

  tipoLabel(tipo: string): string {
    return this.tipos.find(item => item.valor === tipo)?.nome ?? tipo;
  }

  funcoesDisponiveis(tipo: string): string[] {
    const funcoes: Record<string, string[]> = {
      JOGADOR: ['Goleiro', 'Zagueiro', 'Lateral', 'Volante', 'Meia', 'Atacante'],
      TREINADOR: ['Treinador'],
      AUXILIAR_TECNICO: ['Auxiliar tecnico'],
      PREPARADOR_FISICO: ['Preparador fisico'],
      MEDICO: ['Medico']
    };
    return funcoes[tipo] ?? [];
  }

  private validarDadosDoVinculo(): boolean {
    if (!this.funcao) {
      alert('Informe a funcao no time.');
      return false;
    }
    if (!this.dataInicio) {
      alert('Informe a data de inicio.');
      return false;
    }
    return true;
  }

  private voltarAoTime() {
    this.router.navigate(['/time', this.timeId, 'detalhes']);
  }

  private mensagemErro(erro: any, padrao: string): string {
    return erro?.error?.mensagem ?? erro?.error?.message ?? padrao;
  }
}

export const TIME_VINCULAR_RESOLVEDORES: ResolveData = {};
TIME_VINCULAR_RESOLVEDORES[TimeVincular.RECURSO] = (
  rota: ActivatedRouteSnapshot,
  _estado: RouterStateSnapshot
) => inject(HttpClient)
  .get(`/backend/time/${rota.params[TimeVincular.ID]}/edicao`)
  .pipe(catchError(() => of({})));
