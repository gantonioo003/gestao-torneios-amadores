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

  torneios: any[] = [];
  torneioId = '';

  resultado: any = null;
  carregando = false;
  salvando = false;
  salvo = false;
  erro = '';

  constructor(
    private readonly http: HttpClient,
    private readonly rota: ActivatedRoute,
    private readonly router: Router
  ) {}

  ngOnInit() {
    const params = this.rota.snapshot.queryParamMap;
    this.tipo = (params.get('tipo') as any) ?? 'jogador';
    const id = params.get('id');
    const nome = params.get('nome') ?? '';

    if (id && nome) {
      this.primeiro = { id, nome };
    }

    this.http.get<any[]>('/backend/torneio/pesquisa?nome=')
      .pipe(catchError(() => of([])))
      .subscribe(t => this.torneios = t);
  }

  get labelTipo(): string {
    return this.tipo === 'jogador' ? 'Jogador' : 'Time';
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
      .subscribe(r => this.resultadosBusca = r.slice(0, 8));
  }

  selecionarSegundo(entidade: any) {
    this.segundo = entidade;
    this.termoBusca = '';
    this.resultadosBusca = [];
    this.resultado = null;
    this.salvo = false;
  }

  trocarSegundo() {
    this.segundo = null;
    this.resultado = null;
    this.salvo = false;
  }

  podeComparar(): boolean {
    return !!this.primeiro && !!this.segundo;
  }

  comparar() {
    if (!this.podeComparar()) return;
    this.erro = '';
    this.resultado = null;
    this.salvo = false;

    if (!this.torneioId) {
      this.erro = 'Selecione um torneio para gerar o comparativo. As estatisticas sao calculadas por competicao.';
      return;
    }

    this.carregando = true;
    const torneioId = Number(this.torneioId);
    const url = this.tipo === 'jogador'
      ? '/backend/comparativo-desempenho/jogadores'
      : '/backend/comparativo-desempenho/times';
    const corpo = this.tipo === 'jogador'
      ? { torneioId, primeiroJogadorId: this.primeiro.id, segundoJogadorId: this.segundo.id }
      : { torneioId, primeiroTimeId: this.primeiro.id, segundoTimeId: this.segundo.id };

    this.http.post<any>(url, corpo)
      .pipe(finalize(() => this.carregando = false))
      .subscribe({
        next: r => this.resultado = r,
        error: e => this.erro = e?.error?.mensagem ?? e?.error?.detail ?? 'Nao foi possivel gerar o comparativo.'
      });
  }

  salvar() {
    if (!this.resultado || this.tipo !== 'time') return;
    this.salvando = true;
    this.http.post('/backend/comparativo-desempenho/salvar', {
      torneioId: Number(this.torneioId) || 0,
      primeiroTimeId: this.primeiro.id,
      segundoTimeId: this.segundo.id
    }, { responseType: 'text' })
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

  melhor(lado: 'primeiro' | 'segundo'): boolean {
    if (!this.resultado) return false;
    return this.resultado.melhorLado === (lado === 'primeiro' ? 1 : 2);
  }

  nomeEntidade(entidade: any): string {
    return entidade?.nome ?? entidade?.nomeTime ?? entidade?.name ?? '';
  }
}
