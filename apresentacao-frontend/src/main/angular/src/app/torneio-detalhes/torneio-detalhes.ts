import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { catchError, finalize, forkJoin, map, of } from 'rxjs';
import { AuthService } from '../core/auth.service';
import {
  CentralPalpites,
  EventoTorneioPalpite,
  OpcaoPalpite,
  PalpiteService,
  PartidaPalpite,
  TorneioPalpite
} from '../core/palpite.service';

type AbaTorneio = 'competicao' | 'palpites' | 'estatisticas' | 'regras' | 'configuracao';

@Component({
  selector: 'app-torneio-detalhes',
  imports: [FormsModule, RouterLink],
  templateUrl: './torneio-detalhes.html',
  styleUrl: './torneio-detalhes.css'
})
export class TorneioDetalhes implements OnInit {
  aba: AbaTorneio = 'competicao';
  torneioId = '';
  torneio: any = {};
  times: any[] = [];
  profissionais: any[] = [];
  partidas: any[] = [];
  classificacao: any[] = [];
  artilharia: any[] = [];
  assistencias: any[] = [];
  chaveamento: any = null;
  oportunidadesPalpite: CentralPalpites = { torneios: [], partidas: [] };
  solicitacoesPendentes: any[] = [];
  meusTimes: any[] = [];

  carregando = true;
  processando = '';
  salvandoTorneio = false;
  mensagem = '';
  erro = '';
  nomeEdicao = '';
  aceitaSolicitacoesEdicao = false;
  modoPreparacao: 'SORTEIO' | 'MANUAL' = 'SORTEIO';
  ordemManualParticipantes: number[] = [];
  timeSelecionado?: number;
  timeInscricaoSelecionado?: number;
  readonly usuario = this.auth.usuario;

  constructor(
    private readonly http: HttpClient,
    private readonly route: ActivatedRoute,
    private readonly auth: AuthService,
    readonly palpites: PalpiteService
  ) {}

  ngOnInit() {
    this.torneioId = this.route.snapshot.paramMap.get('id') ?? '';
    this.carregarTudo();
  }

  get usaClassificacao(): boolean {
    return ['PONTOS_CORRIDOS', 'FASE_DE_GRUPOS_COM_MATA_MATA'].includes(this.torneio.formato);
  }

  get usaChaveamento(): boolean {
    return ['MATA_MATA', 'FINAL_UNICA', 'FASE_DE_GRUPOS_COM_MATA_MATA'].includes(this.torneio.formato);
  }

  get participantes(): number[] {
    return this.torneio.participantesAprovados ?? [];
  }

  get quantidadeMinimaParticipantes(): number {
    return this.torneio.formato === 'FASE_DE_GRUPOS_COM_MATA_MATA' ? 4 : 2;
  }

  get participantesSuficientes(): boolean {
    return this.participantes.length >= this.quantidadeMinimaParticipantes;
  }

  get partidasPorEtapa(): Array<{ nome: string; partidas: any[] }> {
    const etapas = new Map<string, any[]>();
    for (const partida of this.partidas) {
      const etapa = partida.etapa || this.etapaPadrao();
      etapas.set(etapa, [...(etapas.get(etapa) ?? []), partida]);
    }
    if (!etapas.size) {
      etapas.set(this.etapaPadrao(), []);
    }
    return Array.from(etapas, ([nome, partidas]) => ({ nome, partidas }));
  }

  get partidasFinalizadas(): number {
    return this.partidas.filter(partida => partida.encerrada).length;
  }

  get progressoParticipantes(): number {
    return Math.min(100, (this.participantes.length / this.quantidadeMinimaParticipantes) * 100);
  }

  get partidaDestaque(): any | undefined {
    return this.partidas.find(partida => !partida.encerrada) ?? this.partidas[0];
  }

  get torneioPalpite(): TorneioPalpite | undefined {
    return this.oportunidadesPalpite.torneios
      .find(torneio => String(torneio.id) === String(this.torneioId));
  }

  get partidasPalpite(): PartidaPalpite[] {
    return this.oportunidadesPalpite.partidas
      .filter(partida => String(partida.torneioId) === String(this.torneioId));
  }

  get timesDisponiveisParaAdicionar(): any[] {
    const negociando = this.solicitacoesPendentes
      .filter(solicitacao => solicitacao.status === 'PENDENTE')
      .map(solicitacao => String(solicitacao.timeId));
    return this.times.filter(time =>
      !this.participantes.some(id => String(id) === String(time.id))
      && !negociando.includes(String(time.id)));
  }

  get candidaturasPendentes(): any[] {
    return this.solicitacoesPendentes.filter(item =>
      item.tipo === 'CANDIDATURA' && item.status === 'PENDENTE');
  }

  get convitesPendentes(): any[] {
    return this.solicitacoesPendentes.filter(item =>
      item.tipo === 'CONVITE' && item.status === 'PENDENTE');
  }

  podeGerenciar(): boolean {
    return this.usuario()?.podeCriarTorneio === true
      && String(this.usuario()?.id) === String(this.torneio.organizadorId);
  }

  podeConfigurarAntesDoInicio(): boolean {
    return ['CONFIGURADO', 'ESTRUTURA_GERADA'].includes(this.torneio.status);
  }

  torneioEstaSalvo(): boolean {
    return !!this.torneio.id && (this.usuario()?.torneiosSalvos ?? [])
      .some(id => String(id) === String(this.torneio.id));
  }

  abrirAba(aba: AbaTorneio) {
    if (aba === 'configuracao' && !this.podeGerenciar()) return;
    this.aba = aba;
    this.mensagem = '';
    this.erro = '';
  }

  alternarTorneioSalvo() {
    if (!this.usuario() || !this.torneio.id || this.salvandoTorneio) return;
    this.salvandoTorneio = true;
    const requisicao = this.torneioEstaSalvo()
      ? this.auth.removerTorneioSalvo(this.torneio.id)
      : this.auth.salvarTorneio(this.torneio.id);
    requisicao.pipe(finalize(() => this.salvandoTorneio = false)).subscribe();
  }

  statusLabel(status = this.torneio.status): string {
    const labels: Record<string, string> = {
      CRIADO: 'Criado',
      CONFIGURADO: 'Em configuracao',
      ESTRUTURA_GERADA: 'Estrutura pronta',
      INICIADO: 'Em andamento',
      FINALIZADO: 'Finalizado'
    };
    return labels[status] ?? 'Torneio';
  }

  formatoLabel(formato = this.torneio.formato): string {
    const labels: Record<string, string> = {
      FASE_DE_GRUPOS_COM_MATA_MATA: 'Grupos + mata-mata',
      PONTOS_CORRIDOS: 'Pontos corridos',
      MATA_MATA: 'Mata-mata',
      FINAL_UNICA: 'Final unica'
    };
    return labels[formato] ?? 'Formato do torneio';
  }

  formatoEquipeLabel(formato = this.torneio.formatoEquipe): string {
    const labels: Record<string, string> = {
      TRES_POR_TRES: '3 por 3',
      CINCO_POR_CINCO: '5 por 5',
      SETE_POR_SETE: '7 por 7',
      ONZE_POR_ONZE: '11 por 11'
    };
    return labels[formato] ?? 'Equipe definida';
  }

  nomeTime(timeId: number): string {
    return this.times.find(time => String(time.id) === String(timeId))?.nome ?? `Time #${timeId}`;
  }

  nomeProfissional(jogadorId: number): string {
    return this.profissionais.find(profissional => String(profissional.id) === String(jogadorId))?.nome
      ?? `Jogador #${jogadorId}`;
  }

  placarPartida(partida: any): string {
    return partida.encerrada && partida.golsMandante != null && partida.golsVisitante != null
      ? `${partida.golsMandante} - ${partida.golsVisitante}`
      : 'x';
  }

  votarEventoTorneio(evento: EventoTorneioPalpite, opcao: OpcaoPalpite) {
    const torneio = this.torneioPalpite;
    if (!torneio || this.processando) return;
    this.processando = `${evento.tipo}-${opcao.id}`;
    this.mensagem = '';
    this.erro = '';
    this.palpites.votar(evento.tipo, String(torneio.id), null, String(opcao.id))
      .pipe(finalize(() => this.processando = ''))
      .subscribe({
        next: () => {
          this.mensagem = `Seu palpite em ${opcao.nome} foi registrado.`;
          this.atualizarPercentuaisEvento(torneio, evento);
        },
        error: erro => this.erro = this.mensagemErroPalpite(erro)
      });
  }

  votarPartida(partida: PartidaPalpite, opcao: OpcaoPalpite) {
    if (this.processando) return;
    this.processando = `partida-${partida.id}-${opcao.id}`;
    this.mensagem = '';
    this.erro = '';
    this.palpites.votar(
      'VENCEDOR_PARTIDA',
      String(partida.torneioId),
      String(partida.id),
      String(opcao.id)
    ).pipe(finalize(() => this.processando = '')).subscribe({
      next: () => {
        this.mensagem = `Palpite em ${opcao.nome} registrado.`;
        this.atualizarPercentuaisPartida(partida);
      },
      error: erro => this.erro = this.mensagemErroPalpite(erro)
    });
  }

  escolhaEventoTorneio(evento: EventoTorneioPalpite): string | null {
    return this.palpites.escolha(evento.tipo, this.torneioId, null);
  }

  iconeEventoTorneio(evento: EventoTorneioPalpite): string {
    if (evento.tipo === 'CAMPEAO_TORNEIO') return 'bi bi-trophy';
    if (evento.tipo === 'ARTILHEIRO_TORNEIO') return 'bi bi-bullseye';
    return 'bi bi-person-check';
  }

  escolhaPartida(partida: PartidaPalpite): string | null {
    return this.palpites.escolha(
      'VENCEDOR_PARTIDA',
      String(partida.torneioId),
      String(partida.id)
    );
  }

  salvarConfiguracao() {
    if (!this.nomeEdicao.trim()) {
      this.erro = 'Informe um nome para o torneio.';
      return;
    }
    this.executar(
      'configuracao',
      this.http.post(`/backend/preparacao-torneio/${this.torneioId}/configuracao`, {
        nome: this.nomeEdicao.trim(),
        aceitaSolicitacoes: this.aceitaSolicitacoesEdicao
      }),
      'Configuracao interna atualizada.'
    );
  }

  moverParticipante(indice: number, deslocamento: number) {
    const destino = indice + deslocamento;
    if (destino < 0 || destino >= this.ordemManualParticipantes.length) return;
    const novaOrdem = [...this.ordemManualParticipantes];
    [novaOrdem[indice], novaOrdem[destino]] = [novaOrdem[destino], novaOrdem[indice]];
    this.ordemManualParticipantes = novaOrdem;
  }

  adicionarParticipante() {
    if (!this.timeSelecionado) {
      this.erro = 'Escolha um time para adicionar.';
      return;
    }
    this.executar(
      'adicionar-time',
      this.http.post(
        `/backend/solicitacao-participacao/convidar?timeId=${this.timeSelecionado}&torneioId=${this.torneioId}`,
        {}
      ),
      'Convite enviado ao treinador do time.'
    );
  }

  removerParticipante(timeId: number) {
    this.executar(
      `remover-${timeId}`,
      this.http.post(this.urlPreparacao('remover-participante', { timeId }), {}),
      'Time removido do torneio.'
    );
  }

  aprovarSolicitacao(solicitacao: any) {
    this.executar(
      `aprovar-${solicitacao.id}`,
      this.http.post(`/backend/solicitacao-participacao/${solicitacao.id}/aprovar`, {}),
      'Candidatura aprovada e treinador notificado.'
    );
  }

  rejeitarSolicitacao(solicitacaoId: number) {
    this.executar(
      `rejeitar-${solicitacaoId}`,
      this.http.post(`/backend/solicitacao-participacao/${solicitacaoId}/rejeitar`, {}),
      'Solicitacao rejeitada.'
    );
  }

  cancelarSolicitacao(solicitacaoId: number) {
    this.executar(
      `cancelar-${solicitacaoId}`,
      this.http.post(`/backend/solicitacao-participacao/${solicitacaoId}/cancelar`, {}),
      'Convite cancelado.'
    );
  }

  prepararCompeticao() {
    const manual = this.modoPreparacao === 'MANUAL';
    this.executar(
      'preparar',
      this.http.post(
        this.urlPreparacao(manual ? 'preparar-competicao-manual' : 'preparar-competicao-sorteio'),
        manual ? this.ordemManualParticipantes : {}
      ),
      manual
        ? 'Estrutura, rodadas e partidas geradas na ordem definida.'
        : 'Estrutura, rodadas e partidas geradas por sorteio.'
    );
  }

  iniciarTorneio() {
    this.executar(
      'iniciar',
      this.http.post(this.urlPreparacao('iniciar'), {}),
      'Torneio iniciado.'
    );
  }

  finalizarTorneio() {
    this.executar(
      'finalizar',
      this.http.post(this.urlPreparacao('finalizar'), {}),
      'Edicao finalizada.'
    );
  }

  repetirTorneio() {
    this.executar(
      'repetir',
      this.http.post(this.urlPreparacao('repetir', { abrirSolicitacoes: true }), {}),
      'Nova edicao criada com inscricoes abertas.'
    );
  }

  solicitarParticipacao() {
    if (!this.timeInscricaoSelecionado) {
      this.erro = 'Escolha qual time deseja inscrever.';
      return;
    }
    this.executar(
      'inscricao',
      this.http.post(
        `/backend/solicitacao-participacao/solicitar?timeId=${this.timeInscricaoSelecionado}&torneioId=${this.torneioId}`,
        {}
      ),
      'Solicitacao enviada ao organizador.'
    );
  }

  private carregarTudo() {
    this.carregando = true;
    const detalheTorneio = this.http.get<any>(`/backend/preparacao-torneio/${this.torneioId}`)
      .pipe(catchError(() => this.http.get<any[]>('/backend/torneio/pesquisa').pipe(
        map(torneios => {
          const resumo = torneios.find(torneio => torneio.id === this.torneioId) ?? {};
          return { ...resumo, edicaoAtual: 1, participantesAprovados: [] };
        }),
        catchError(() => of({}))
      )));
    forkJoin({
      torneio: detalheTorneio,
      times: this.http.get<any[]>('/backend/time/pesquisa?nome=').pipe(catchError(() => of([]))),
      profissionais: this.http.get<any[]>('/backend/profissional/pesquisa?nome=').pipe(catchError(() => of([]))),
      partidas: this.http.get<any[]>(`/backend/partida/pesquisa?torneioId=${this.torneioId}`)
        .pipe(catchError(() => of([]))),
      classificacao: this.http.get<any[]>(`/backend/resultado-competicao/${this.torneioId}/classificacao`)
        .pipe(catchError(() => of([]))),
      chaveamento: this.http.get<any>(`/backend/resultado-competicao/${this.torneioId}/chaveamento`)
        .pipe(catchError(() => of(null))),
      artilharia: this.http.get<any[]>(`/backend/ranking-estatistico/${this.torneioId}/artilharia`)
        .pipe(catchError(() => of([]))),
      assistencias: this.http.get<any[]>(`/backend/ranking-estatistico/${this.torneioId}/assistencias`)
        .pipe(catchError(() => of([]))),
      palpites: this.palpites.oportunidades()
        .pipe(catchError(() => of({ torneios: [], partidas: [] } as CentralPalpites)))
    }).pipe(finalize(() => this.carregando = false)).subscribe(dados => {
      this.torneio = dados.torneio;
      this.nomeEdicao = this.torneio.nome ?? '';
      this.aceitaSolicitacoesEdicao = this.torneio.aceitaSolicitacoes === true;
      this.ordemManualParticipantes = [...(this.torneio.participantesAprovados ?? [])];
      this.times = dados.times;
      this.profissionais = dados.profissionais;
      this.partidas = dados.partidas;
      this.classificacao = [...dados.classificacao]
        .sort((a, b) => b.pontos - a.pontos || b.saldoGols - a.saldoGols);
      this.chaveamento = dados.chaveamento;
      this.artilharia = dados.artilharia;
      this.assistencias = dados.assistencias;
      this.oportunidadesPalpite = dados.palpites;
      if (this.route.snapshot.queryParamMap.get('configurar') === 'true' && this.podeGerenciar()) {
        this.aba = 'configuracao';
      }
      this.carregarDadosDaConta();
    });
  }

  private carregarDadosDaConta() {
    if (this.podeGerenciar()) {
      this.http.get<any[]>(
        `/backend/solicitacao-participacao/torneio?torneioId=${this.torneioId}`
      ).pipe(catchError(() => of([])))
        .subscribe(solicitacoes => this.solicitacoesPendentes = solicitacoes);
    }

    if (this.usuario()?.tipo === 'TREINADOR') {
      this.http.get<any[]>('/backend/time/pesquisa?meus=true')
        .pipe(catchError(() => of([])))
        .subscribe(times => this.meusTimes = times);
    }
  }

  private executar(chave: string, requisicao: any, mensagem: string) {
    if (this.processando) return;
    this.processando = chave;
    this.mensagem = '';
    this.erro = '';
    requisicao.pipe(finalize(() => this.processando = '')).subscribe({
      next: () => {
        this.mensagem = mensagem;
        this.timeSelecionado = undefined;
        this.timeInscricaoSelecionado = undefined;
        this.carregarTudo();
      },
      error: (erro: any) => {
        this.erro = erro?.error?.mensagem
          ?? erro?.error?.detail
          ?? erro?.error?.message
          ?? 'Nao foi possivel concluir esta acao.';
      }
    });
  }

  private urlPreparacao(acao: string, parametros: Record<string, string | number | boolean> = {}): string {
    const usuarioId = this.usuario()?.id ?? 0;
    const query = new URLSearchParams({
      organizadorId: String(usuarioId),
      ...Object.fromEntries(Object.entries(parametros).map(([chave, valor]) => [chave, String(valor)]))
    });
    return `/backend/preparacao-torneio/${this.torneioId}/${acao}?${query}`;
  }

  private etapaPadrao(): string {
    if (this.torneio.formato === 'FINAL_UNICA') return 'Final';
    if (this.torneio.formato === 'MATA_MATA') return 'Chaveamento';
    if (this.torneio.formato === 'FASE_DE_GRUPOS_COM_MATA_MATA') return 'Fase eliminatoria';
    return 'Rodadas';
  }

  private atualizarPercentuaisEvento(torneio: TorneioPalpite, evento: EventoTorneioPalpite) {
    this.palpites.percentuais(evento.tipo, String(torneio.id), null)
      .subscribe(percentuais => {
        evento.percentuais = percentuais;
        if (evento.tipo === 'CAMPEAO_TORNEIO') torneio.percentuais = percentuais;
      });
  }

  private atualizarPercentuaisPartida(partida: PartidaPalpite) {
    this.palpites.percentuais(
      'VENCEDOR_PARTIDA',
      String(partida.torneioId),
      String(partida.id)
    ).subscribe(percentuais => partida.percentuais = percentuais);
  }

  private mensagemErroPalpite(erro: any): string {
    return erro?.error?.mensagem
      ?? erro?.error?.detail
      ?? erro?.error?.message
      ?? 'Nao foi possivel registrar o palpite.';
  }
}
