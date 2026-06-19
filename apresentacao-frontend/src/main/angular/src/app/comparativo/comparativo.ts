import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { catchError, finalize, of } from 'rxjs';

@Component({
  selector: 'app-comparativo',
  imports: [FormsModule, RouterLink],
  templateUrl: './comparativo.html',
  styleUrl: './comparativo.css'
})
export class Comparativo implements OnInit {
  tipo: 'jogador' | 'time' = 'jogador';

  primeiro: any = null;
  segundo: any = null;

  termoBusca = '';
  resultadosBusca: any[] = [];

  resultado: any = null;
  carregando = false;
  salvando = false;
  salvo = false;
  compartilhado = false;
  erro = '';

  constructor(
    private readonly http: HttpClient,
    private readonly rota: ActivatedRoute,
    private readonly router: Router
  ) {}

  ngOnInit() {
    const params = this.rota.snapshot.queryParamMap;
    this.tipo = params.get('tipo') === 'time' ? 'time' : 'jogador';
    const id = params.get('id');
    const nome = params.get('nome') ?? '';

    if (id && nome) {
      this.primeiro = { id, nome };
    }

    const segundoId = params.get('segundoId');
    const segundoNome = params.get('segundoNome');
    if (this.primeiro && segundoId && segundoNome) {
      this.segundo = { id: segundoId, nome: segundoNome };
      this.comparar();
    }
  }

  get labelTipo(): string {
    return this.tipo === 'jogador' ? 'Jogador' : 'Time';
  }

  get labelTipoPlural(): string {
    return this.tipo === 'jogador' ? 'Jogadores' : 'Times';
  }

  get rotaPerfilPrimeiro(): string[] {
    if (!this.primeiro) return ['/buscar'];
    return this.tipo === 'jogador'
      ? ['/profissional', this.primeiro.id, 'perfil']
      : ['/time', this.primeiro.id, 'detalhes'];
  }

  buscar() {
    const termo = this.termoBusca.trim();
    if (termo.length < 2) { this.resultadosBusca = []; return; }
    const url = this.tipo === 'jogador'
      ? `/backend/profissional/pesquisa?nome=${encodeURIComponent(termo)}`
      : `/backend/time/pesquisa?nome=${encodeURIComponent(termo)}`;
    this.http.get<any[]>(url)
      .pipe(catchError(() => of([])))
      .subscribe(r => this.resultadosBusca = r
        .filter(item => this.tipo === 'time' || item.tipo === 'JOGADOR')
        .filter(item => String(item.id) !== String(this.primeiro?.id))
        .slice(0, 8));
  }

  selecionarSegundo(entidade: any) {
    this.segundo = entidade;
    this.termoBusca = '';
    this.resultadosBusca = [];
    this.resultado = null;
    this.salvo = false;
    this.compartilhado = false;
    this.atualizarLinkComparacao();
    this.comparar();
  }

  trocarSegundo() {
    this.segundo = null;
    this.resultado = null;
    this.salvo = false;
    this.compartilhado = false;
    this.router.navigate([], {
      relativeTo: this.rota,
      queryParams: { segundoId: null, segundoNome: null },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }

  alterarTipo(tipo: 'jogador' | 'time') {
    if (this.tipo === tipo) return;
    this.tipo = tipo;
    this.primeiro = null;
    this.segundo = null;
    this.resultado = null;
    this.resultadosBusca = [];
    this.termoBusca = '';
    this.erro = '';
    this.salvo = false;
    this.compartilhado = false;
    this.router.navigate([], {
      relativeTo: this.rota,
      queryParams: {
        tipo,
        id: null,
        nome: null,
        segundoId: null,
        segundoNome: null
      },
      replaceUrl: true
    });
  }

  podeComparar(): boolean {
    return !!this.primeiro && !!this.segundo;
  }

  comparar() {
    if (!this.podeComparar()) return;
    this.erro = '';
    this.resultado = null;
    this.salvo = false;

    this.carregando = true;
    const url = this.tipo === 'jogador'
      ? '/backend/comparativo-desempenho/jogadores'
      : '/backend/comparativo-desempenho/times';
    const corpo = this.tipo === 'jogador'
      ? { primeiroJogadorId: this.primeiro.id, segundoJogadorId: this.segundo.id }
      : { primeiroTimeId: this.primeiro.id, segundoTimeId: this.segundo.id };

    this.http.post<any>(url, corpo)
      .pipe(finalize(() => this.carregando = false))
      .subscribe({
        next: r => this.resultado = r,
        error: e => this.erro = e?.error?.mensagem ?? e?.error?.detail ?? 'Nao foi possivel gerar o comparativo.'
      });
  }

  salvar() {
    if (!this.resultado) return;
    this.salvando = true;
    const url = this.tipo === 'jogador'
      ? '/backend/comparativo-desempenho/salvar-jogadores'
      : '/backend/comparativo-desempenho/salvar-times';
    const corpo = this.tipo === 'jogador'
      ? { primeiroJogadorId: this.primeiro.id, segundoJogadorId: this.segundo.id }
      : { primeiroTimeId: this.primeiro.id, segundoTimeId: this.segundo.id };
    this.http.post(url, corpo, { responseType: 'text' })
      .pipe(catchError(() => of(null)))
      .subscribe(() => { this.salvando = false; this.salvo = true; });
  }

  exportarCsv() {
    if (!this.resultado) return;
    const r = this.resultado;
    const linhas = [
      ['Metrica', r.primeiro.rotulo, r.segundo.rotulo],
      ['Gols', r.primeiro.gols, r.segundo.gols],
      ['Assistencias', r.primeiro.assistencias, r.segundo.assistencias],
      ['Cartoes amarelos', r.primeiro.cartoesAmarelos, r.segundo.cartoesAmarelos],
      ['Cartoes vermelhos', r.primeiro.cartoesVermelhos, r.segundo.cartoesVermelhos],
      ['Partidas com eventos', r.primeiro.partidasComEventos, r.segundo.partidasComEventos],
      ['Posicao no ranking', r.primeiro.posicaoRanking, r.segundo.posicaoRanking],
      ['Pontuacao comparativa', r.primeiro.pontuacaoComparativa.toFixed(1), r.segundo.pontuacaoComparativa.toFixed(1)]
    ];
    const csv = linhas.map(l => l.join(';')).join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `comparativo-${r.primeiro.rotulo}-vs-${r.segundo.rotulo}.csv`.replace(/\s+/g, '-');
    a.click();
    URL.revokeObjectURL(url);
  }

  async compartilhar() {
    if (!this.resultado) return;
    const texto = this.resumoCompartilhamento();
    const dados = {
      title: `${this.resultado.primeiro.rotulo} x ${this.resultado.segundo.rotulo}`,
      text: texto,
      url: window.location.href
    };

    try {
      if (navigator.share) {
        await navigator.share(dados);
      } else {
        await navigator.clipboard.writeText(`${texto}\n${window.location.href}`);
      }
      this.compartilhado = true;
    } catch (erro: any) {
      if (erro?.name !== 'AbortError') {
        this.erro = 'Nao foi possivel compartilhar o comparativo.';
      }
    }
  }

  melhor(lado: 'primeiro' | 'segundo'): boolean {
    if (!this.resultado) return false;
    return this.resultado.melhorLado === (lado === 'primeiro' ? 1 : 2);
  }

  pontosRadar(lado: 'primeiro' | 'segundo'): string {
    if (!this.resultado) return '';
    const outro = lado === 'primeiro' ? 'segundo' : 'primeiro';
    const valores = [
      this.normalizarPositivo(this.resultado[lado].gols, this.resultado[outro].gols),
      this.normalizarPositivo(this.resultado[lado].assistencias, this.resultado[outro].assistencias),
      this.normalizarPositivo(
        this.resultado[lado].partidasComEventos,
        this.resultado[outro].partidasComEventos
      ),
      this.normalizarDisciplina(this.resultado[lado], this.resultado[outro]),
      this.normalizarPositivo(
        this.resultado[lado].pontuacaoComparativa,
        this.resultado[outro].pontuacaoComparativa
      )
    ];
    const centroX = 150;
    const centroY = 126;
    const raio = 88;

    return valores.map((valor, indice) => {
      const angulo = (-90 + indice * 72) * Math.PI / 180;
      const distancia = raio * valor / 100;
      return `${(centroX + Math.cos(angulo) * distancia).toFixed(1)},${(centroY + Math.sin(angulo) * distancia).toFixed(1)}`;
    }).join(' ');
  }

  valorResumo(lado: 'primeiro' | 'segundo', metrica: 'gols' | 'assistencias' | 'partidasComEventos' | 'pontuacaoComparativa'): string {
    if (!this.resultado) return '-';
    const valor = this.resultado[lado][metrica];
    return metrica === 'pontuacaoComparativa' ? Number(valor).toFixed(1) : String(valor);
  }

  nomeEntidade(entidade: any): string {
    return entidade?.nome ?? entidade?.nomeTime ?? entidade?.name ?? '';
  }

  private normalizarPositivo(valor: number, outroValor: number): number {
    const maior = Math.max(Number(valor) || 0, Number(outroValor) || 0);
    if (maior === 0) return 55;
    return 25 + 75 * ((Number(valor) || 0) / maior);
  }

  private normalizarDisciplina(entidade: any, outraEntidade: any): number {
    const penalidade = (Number(entidade.cartoesAmarelos) || 0)
      + (Number(entidade.cartoesVermelhos) || 0) * 2;
    const outraPenalidade = (Number(outraEntidade.cartoesAmarelos) || 0)
      + (Number(outraEntidade.cartoesVermelhos) || 0) * 2;
    const maior = Math.max(penalidade, outraPenalidade);
    if (maior === 0) return 100;
    return 100 - 75 * (penalidade / maior);
  }

  private atualizarLinkComparacao() {
    this.router.navigate([], {
      relativeTo: this.rota,
      queryParams: {
        tipo: this.tipo,
        id: this.primeiro?.id,
        nome: this.nomeEntidade(this.primeiro),
        segundoId: this.segundo?.id,
        segundoNome: this.nomeEntidade(this.segundo)
      },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }

  private resumoCompartilhamento(): string {
    const primeiro = this.resultado.primeiro;
    const segundo = this.resultado.segundo;
    return [
      `Comparativo: ${primeiro.rotulo} x ${segundo.rotulo}`,
      `Pontuacao: ${primeiro.pontuacaoComparativa.toFixed(1)} x ${segundo.pontuacaoComparativa.toFixed(1)}`,
      `Gols: ${primeiro.gols} x ${segundo.gols}`,
      `Assistencias: ${primeiro.assistencias} x ${segundo.assistencias}`
    ].join(' | ');
  }
}
