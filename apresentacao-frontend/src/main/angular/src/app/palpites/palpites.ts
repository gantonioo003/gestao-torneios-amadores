import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { catchError, finalize, forkJoin, of } from 'rxjs';
import { AuthService } from '../core/auth.service';

interface OpcaoPalpite {
  id: string;
  nome: string;
}

interface Percentuais {
  totalPalpites: number;
  percentuaisPorOpcao: Record<string, number>;
}

interface TorneioDisponivel {
  id: string;
  nome: string;
  status: string;
  opcoes: OpcaoPalpite[];
  percentuais: Percentuais;
}

interface PartidaDisponivel {
  id: string;
  torneioId: string;
  torneioNome: string;
  etapa: string;
  mandante: OpcaoPalpite;
  empate: OpcaoPalpite;
  visitante: OpcaoPalpite;
  percentuais: Percentuais;
}

interface PalpiteUsuario {
  id: string;
  tipo: string;
  torneioId: string;
  partidaId?: string;
  opcao: string;
  apurado: boolean;
  acertou?: boolean;
  torneioNome?: string;
  eventoNome?: string;
  opcaoNome?: string;
  opcaoImagemUrl?: string;
  resultadoDescricao?: string;
}

interface ProgressoPalpite {
  pontos: number;
  nivel: number;
  pontosProximoNivel: number;
  sequenciaAtual: number;
  maiorSequencia: number;
  totalPalpites: number;
  totalAcertos: number;
  posicaoRanking: number;
  selos: string[];
}

interface RankingPalpite {
  usuarioId: number;
  nome: string;
  pontos: number;
  acertos: number;
  sequencia: number;
  selos: string[];
}

@Component({
  selector: 'app-palpites',
  imports: [FormsModule, RouterLink],
  templateUrl: './palpites.html',
  styleUrl: './palpites.css'
})
export class Palpites implements OnInit {
  readonly usuario = this.auth.usuario;
  torneios: TorneioDisponivel[] = [];
  partidas: PartidaDisponivel[] = [];
  meusPalpites: PalpiteUsuario[] = [];
  progresso: ProgressoPalpite | null = null;
  ranking: RankingPalpite[] = [];
  readonly recompensas = [
    { titulo: '+25 XP', descricao: 'Acertar vencedor de partida', icone: 'bi-lightning-charge-fill' },
    { titulo: '+75 XP', descricao: 'Acertar artilheiro ou assistencias', icone: 'bi-bullseye' },
    { titulo: '+100 XP', descricao: 'Cravar o campeao do torneio', icone: 'bi-trophy-fill' }
  ];
  readonly selosDisponiveis = [
    'PRIMEIRO_PALPITE',
    'TRES_DIAS_SEGUIDOS',
    'SETE_DIAS_SEGUIDOS',
    'DEZ_ACERTOS',
    'CINQUENTA_PALPITES'
  ];
  carregando = true;
  votando = '';
  aba: 'disponiveis' | 'andamento' | 'historico' = 'disponiveis';
  buscaPartidas = '';
  buscaTorneios = '';
  buscaAndamento = '';
  buscaHistorico = '';

  constructor(
    private readonly http: HttpClient,
    private readonly auth: AuthService
  ) {}

  ngOnInit() {
    this.carregar();
  }

  get palpitesEmAndamento(): PalpiteUsuario[] {
    return this.meusPalpites.filter(palpite => !palpite.apurado);
  }

  get palpitesAnteriores(): PalpiteUsuario[] {
    return this.meusPalpites.filter(palpite => palpite.apurado);
  }

  get progressoAtual(): ProgressoPalpite {
    return this.progresso ?? {
      pontos: 0,
      nivel: 1,
      pontosProximoNivel: 100,
      sequenciaAtual: 0,
      maiorSequencia: 0,
      totalPalpites: 0,
      totalAcertos: 0,
      posicaoRanking: 0,
      selos: []
    };
  }

  get partidasFiltradas(): PartidaDisponivel[] {
    const busca = this.normalizarBusca(this.buscaPartidas);
    if (!busca) return this.partidas;
    return this.partidas.filter(partida => this.correspondeBusca(busca,
      partida.torneioNome,
      partida.etapa,
      partida.mandante.nome,
      partida.visitante.nome,
      `${partida.mandante.nome} x ${partida.visitante.nome}`
    ));
  }

  get torneiosFiltrados(): TorneioDisponivel[] {
    const busca = this.normalizarBusca(this.buscaTorneios);
    if (!busca) return this.torneios;
    return this.torneios.filter(torneio => this.correspondeBusca(
      busca,
      torneio.nome,
      ...torneio.opcoes.map(opcao => opcao.nome)
    ));
  }

  get palpitesEmAndamentoFiltrados(): PalpiteUsuario[] {
    return this.filtrarPalpites(this.palpitesEmAndamento, this.buscaAndamento);
  }

  get palpitesAnterioresFiltrados(): PalpiteUsuario[] {
    return this.filtrarPalpites(this.palpitesAnteriores, this.buscaHistorico);
  }

  get percentualNivel(): number {
    const progresso = this.progressoAtual;
    const inicioNivel = (progresso.nivel - 1) * 100;
    return Math.max(0, Math.min(100, progresso.pontos - inicioNivel));
  }

  get xpRestanteNivel(): number {
    const progresso = this.progressoAtual;
    return Math.max(0, progresso.pontosProximoNivel - progresso.pontos);
  }

  seloDesbloqueado(selo: string): boolean {
    return this.progressoAtual.selos.includes(selo);
  }

  seloLabel(selo: string): string {
    const labels: Record<string, string> = {
      PRIMEIRO_PALPITE: 'Primeiro chute',
      TRES_DIAS_SEGUIDOS: 'Ritmo de 3 dias',
      SETE_DIAS_SEGUIDOS: 'Semana perfeita',
      DEZ_ACERTOS: 'Olho de craque',
      CINQUENTA_PALPITES: 'Veterano'
    };
    return labels[selo] ?? selo;
  }

  votarPartida(partida: PartidaDisponivel, opcao: OpcaoPalpite) {
    this.salvarPalpite('VENCEDOR_PARTIDA', partida.torneioId, partida.id, opcao.id, () => {
      this.atualizarPercentuaisPartida(partida);
    });
  }

  votarCampeao(torneio: TorneioDisponivel, opcao: OpcaoPalpite) {
    this.salvarPalpite('CAMPEAO_TORNEIO', torneio.id, null, opcao.id, () => {
      this.atualizarPercentuaisTorneio(torneio);
    });
  }

  percentual(percentuais: Percentuais, opcaoId: string): number {
    return Math.round(percentuais?.percentuaisPorOpcao?.[String(opcaoId)] ?? 0);
  }

  nomeOpcao(palpite: PalpiteUsuario): string {
    if (palpite.opcaoNome) return palpite.opcaoNome;
    if (palpite.tipo === 'VENCEDOR_PARTIDA') {
      const partida = this.partidas.find(item => item.id === palpite.partidaId);
      return [partida?.mandante, partida?.empate, partida?.visitante]
        .find(opcao => opcao?.id === palpite.opcao)?.nome ?? `Time #${palpite.opcao}`;
    }
    const torneio = this.torneios.find(item => item.id === palpite.torneioId);
    return torneio?.opcoes.find(opcao => opcao.id === palpite.opcao)?.nome
      ?? `Opcao #${palpite.opcao}`;
  }

  nomeEvento(palpite: PalpiteUsuario): string {
    if (palpite.eventoNome) return palpite.eventoNome;
    const torneio = this.torneios.find(item => item.id === palpite.torneioId);
    if (palpite.tipo === 'VENCEDOR_PARTIDA') {
      const partida = this.partidas.find(item => item.id === palpite.partidaId);
      return partida
        ? `${partida.mandante.nome} x ${partida.visitante.nome}`
        : `Partida #${palpite.partidaId}`;
    }
    return `Campeao de ${torneio?.nome ?? `Torneio #${palpite.torneioId}`}`;
  }

  tipoLabel(tipo: string): string {
    const labels: Record<string, string> = {
      VENCEDOR_PARTIDA: 'Vencedor da partida',
      CAMPEAO_TORNEIO: 'Campeao do torneio',
      ARTILHEIRO_TORNEIO: 'Artilheiro',
      LIDER_ASSISTENCIAS_TORNEIO: 'Lider de assistencias'
    };
    return labels[tipo] ?? tipo;
  }

  private carregar() {
    const oportunidades = this.http.get<{ torneios: TorneioDisponivel[]; partidas: PartidaDisponivel[] }>(
      '/backend/palpites/oportunidades'
    ).pipe(catchError(() => of({ torneios: [], partidas: [] })));
    const historico = this.usuario()
      ? this.http.get<PalpiteUsuario[]>('/backend/palpites/meus').pipe(catchError(() => of([])))
      : of([]);
    const progresso = this.usuario()
      ? this.http.get<ProgressoPalpite>('/backend/palpites/progresso').pipe(catchError(() => of(null)))
      : of(null);
    const ranking = this.http.get<RankingPalpite[]>('/backend/palpites/ranking')
      .pipe(catchError(() => of([])));

    forkJoin({ oportunidades, historico, progresso, ranking })
      .pipe(finalize(() => this.carregando = false))
      .subscribe(({ oportunidades, historico, progresso, ranking }) => {
        this.torneios = oportunidades.torneios ?? [];
        this.partidas = oportunidades.partidas ?? [];
        this.meusPalpites = historico;
        this.progresso = progresso;
        this.ranking = ranking;
      });
  }

  private salvarPalpite(
    tipo: string,
    torneioId: string,
    partidaId: string | null,
    opcao: string,
    depoisDeSalvar: () => void
  ) {
    const chave = `${tipo}:${torneioId}:${partidaId ?? 'torneio'}`;
    if (this.votando) return;
    this.votando = chave;

    const corpo = { tipo, torneioId, partidaId, opcao };
    const requisicao = this.usuario()
      ? this.http.post<PalpiteUsuario>('/backend/palpites/salvar', corpo)
      : this.http.post<PalpiteUsuario>('/backend/palpites/salvar-visitante', {
          ...corpo,
          visitanteId: this.identificadorVisitante()
        });

    requisicao.pipe(finalize(() => this.votando = '')).subscribe({
      next: palpite => {
        if (this.usuario()) {
          const indice = this.meusPalpites.findIndex(item =>
            item.tipo === palpite.tipo
            && item.torneioId === palpite.torneioId
            && item.partidaId === palpite.partidaId
          );
          if (indice >= 0) {
            this.meusPalpites[indice] = palpite;
          } else {
            this.meusPalpites = [palpite, ...this.meusPalpites];
          }
        }
        if (this.usuario()) this.atualizarGamificacao();
        depoisDeSalvar();
      },
      error: erro => alert(
        erro?.error?.mensagem
        ?? erro?.error?.message
        ?? 'Nao foi possivel registrar o palpite.'
      )
    });
  }

  private atualizarPercentuaisPartida(partida: PartidaDisponivel) {
    this.http.get<Percentuais>(
      `/backend/palpites/percentuais?tipo=VENCEDOR_PARTIDA&torneioId=${partida.torneioId}&partidaId=${partida.id}`
    ).subscribe(percentuais => partida.percentuais = percentuais);
  }

  private atualizarPercentuaisTorneio(torneio: TorneioDisponivel) {
    this.http.get<Percentuais>(
      `/backend/palpites/percentuais?tipo=CAMPEAO_TORNEIO&torneioId=${torneio.id}`
    ).subscribe(percentuais => torneio.percentuais = percentuais);
  }

  private identificadorVisitante(): string {
    const chave = 'liga-amadora.visitante-palpite';
    const existente = localStorage.getItem(chave);
    if (existente) return existente;
    const novo = globalThis.crypto?.randomUUID?.()
      ?? `visitante-${Date.now()}-${Math.random().toString(16).slice(2)}`;
    localStorage.setItem(chave, novo);
    return novo;
  }

  private atualizarGamificacao() {
    forkJoin({
      progresso: this.http.get<ProgressoPalpite>('/backend/palpites/progresso'),
      ranking: this.http.get<RankingPalpite[]>('/backend/palpites/ranking')
    }).subscribe(({ progresso, ranking }) => {
      this.progresso = progresso;
      this.ranking = ranking;
    });
  }

  private filtrarPalpites(palpites: PalpiteUsuario[], termo: string): PalpiteUsuario[] {
    const busca = this.normalizarBusca(termo);
    if (!busca) return palpites;
    return palpites.filter(palpite => this.correspondeBusca(
      busca,
      this.nomeEvento(palpite),
      this.nomeOpcao(palpite),
      this.tipoLabel(palpite.tipo),
      palpite.apurado ? (palpite.acertou ? 'acertou' : 'nao acertou') : 'em andamento'
    ));
  }

  private correspondeBusca(busca: string, ...valores: Array<string | undefined>): boolean {
    return valores.some(valor => this.normalizarBusca(valor ?? '').includes(busca));
  }

  private normalizarBusca(valor: string): string {
    return valor.trim().toLocaleLowerCase('pt-BR')
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }
}


