import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { catchError, finalize, forkJoin, map, of } from 'rxjs';
import { AuthService } from '../core/auth.service';
import { IdentityEditor } from '../shared/identity-editor/identity-editor';
import {
  CentralPalpites,
  EventoTorneioPalpite,
  OpcaoPalpite,
  PalpiteService,
  PartidaPalpite,
  TorneioPalpite
} from '../core/palpite.service';

type AbaTorneio = 'classificacao' | 'eliminatoria' | 'palpites' | 'estatisticas' | 'regras' | 'publicacoes' | 'configuracao';

interface ConfrontoChaveamento {
  partida: any | null;
  posicao: number;
}

interface FaseChaveamento {
  nome: string;
  confrontos: ConfrontoChaveamento[];
}

@Component({
  selector: 'app-torneio-detalhes',
  imports: [FormsModule, RouterLink, IdentityEditor],
  templateUrl: './torneio-detalhes.html',
  styleUrl: './torneio-detalhes.css'
})
export class TorneioDetalhes implements OnInit {
  aba: AbaTorneio = 'classificacao';
  torneioId = '';
  torneio: any = {};
  times: any[] = [];
  profissionais: any[] = [];
  partidas: any[] = [];
  classificacao: any[] = [];
  artilharia: any[] = [];
  assistencias: any[] = [];
  chaveamento: any = null;
  publicacoes: any[] = [];
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

  get fasesChaveamento(): FaseChaveamento[] {
    if (!this.usaChaveamento) return [];

    const nomesEsperados = this.nomesFasesEliminatorias();
    const partidasEliminatorias = this.partidas.filter(partida => this.ehPartidaEliminatoria(partida));
    const partidasPorFase = new Map<string, any[]>();

    for (const partida of partidasEliminatorias) {
      const nome = this.nomeFasePartida(partida, nomesEsperados, partidasEliminatorias);
      partidasPorFase.set(nome, [...(partidasPorFase.get(nome) ?? []), partida]);
    }

    return nomesEsperados.map((nome, indice) => {
      const partidas = partidasPorFase.get(nome) ?? [];
      const quantidadeEsperada = Math.max(1, 2 ** (nomesEsperados.length - indice - 1));
      const quantidadeConfrontos = Math.max(quantidadeEsperada, partidas.length);
      return {
        nome,
        confrontos: Array.from({ length: quantidadeConfrontos }, (_, posicao) => ({
          partida: partidas[posicao] ?? null,
          posicao
        }))
      };
    });
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
        aceitaSolicitacoes: this.aceitaSolicitacoesEdicao,
        imagemUrl: this.torneio.imagemUrl
      }),
      'Configuracao interna atualizada.'
    );
  }

  imagemTime(timeId: number): string {
    return this.times.find(time => String(time.id) === String(timeId))?.imagemUrl ?? '';
  }

  timeVencedor(partida: any, lado: 'mandante' | 'visitante'): boolean {
    if (!partida?.encerrada || partida.golsMandante == null || partida.golsVisitante == null) return false;
    return lado === 'mandante'
      ? partida.golsMandante > partida.golsVisitante
      : partida.golsVisitante > partida.golsMandante;
  }

  statusConfronto(partida: any): string {
    if (partida.encerrada) return 'Finalizada';
    if (partida.iniciada) return 'Ao vivo';
    return 'Agendada';
  }

  iniciais(nome: string): string {
    return (nome || 'T').split(/\s+/).filter(Boolean).slice(0, 2)
      .map(parte => parte[0]).join('').toUpperCase();
  }

  tempoPublicacao(dataIso: string): string {
    const horas = Math.floor((Date.now() - new Date(dataIso).getTime()) / 3_600_000);
    if (horas < 1) return 'agora';
    if (horas < 24) return `${horas}h`;
    return `${Math.floor(horas / 24)}d`;
  }

  ehVideo(midia: string): boolean {
    return midia.startsWith('data:video/') || /\.(mp4|webm|ogg)(\?.*)?$/i.test(midia);
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
      publicacoes: this.http.get<any[]>(`/backend/feed/identidade/TORNEIO/${this.torneioId}`)
        .pipe(catchError(() => of([]))),
      palpites: this.palpites.oportunidadesTorneio(this.torneioId)
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
      this.publicacoes = dados.publicacoes;
      this.oportunidadesPalpite = dados.palpites;
      this.aba = this.usaClassificacao ? 'classificacao' : 'eliminatoria';
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

  private nomesFasesEliminatorias(): string[] {
    if (this.torneio.formato === 'FINAL_UNICA') return ['Final'];

    const participantesEliminatoria = this.torneio.formato === 'FASE_DE_GRUPOS_COM_MATA_MATA'
      ? Math.max(2, Math.ceil(this.participantes.length / 2))
      : Math.max(2, this.participantes.length);
    const tamanhoChave = 2 ** Math.ceil(Math.log2(participantesEliminatoria));
    const fases: string[] = [];

    if (tamanhoChave >= 32) fases.push('16 avos de final');
    if (tamanhoChave >= 16) fases.push('Oitavas de final');
    if (tamanhoChave >= 8) fases.push('Quartas de final');
    if (tamanhoChave >= 4) fases.push('Semifinais');
    fases.push('Final');
    return fases;
  }

  private ehPartidaEliminatoria(partida: any): boolean {
    if (this.torneio.formato === 'MATA_MATA' || this.torneio.formato === 'FINAL_UNICA') return true;
    if (this.torneio.formato !== 'FASE_DE_GRUPOS_COM_MATA_MATA') return false;
    const etapa = this.normalizarTexto(partida.etapa);
    return !etapa.startsWith('grupo') && !etapa.startsWith('fase de grupos');
  }

  private nomeFasePartida(partida: any, nomesEsperados: string[], partidasEliminatorias: any[]): string {
    const etapa = this.normalizarTexto(partida.etapa);
    const faseConhecida = nomesEsperados.find(nome => this.normalizarTexto(nome) === etapa);
    if (faseConhecida) return faseConhecida;
    if (etapa.includes('final') && !etapa.includes('semi') && !etapa.includes('quarta')
      && !etapa.includes('oitava') && !etapa.includes('16 avo')) {
      return 'Final';
    }
    if (etapa.includes('semi')) return 'Semifinais';
    if (etapa.includes('quarta')) return 'Quartas de final';
    if (etapa.includes('oitava')) return 'Oitavas de final';
    if (etapa.includes('16 avo')) return '16 avos de final';

    const etapasGenericas = ['chaveamento', 'chaveamento inicial', 'fase eliminatoria', 'fases eliminatorias'];
    if (etapasGenericas.includes(etapa)) {
      const indicePrimeiraFase = nomesEsperados.length
        - Math.max(1, Math.ceil(Math.log2(Math.max(1, partidasEliminatorias.length))));
      return nomesEsperados[Math.max(0, indicePrimeiraFase)] ?? nomesEsperados[0];
    }
    return nomesEsperados[0];
  }

  private normalizarTexto(valor: string | null | undefined): string {
    return (valor ?? '').normalize('NFD').replace(/[\u0300-\u036f]/g, '').trim().toLowerCase();
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
