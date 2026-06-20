import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { catchError, finalize, forkJoin, of, switchMap } from 'rxjs';
import { AuthService } from '../core/auth.service';
import {
  CentralPalpites,
  OpcaoPalpite,
  PalpiteService,
  PartidaPalpite,
  PercentuaisPalpite
} from '../core/palpite.service';

@Component({
  selector: 'app-partida-detalhes',
  imports: [FormsModule, RouterLink],
  templateUrl: './partida-detalhes.html',
  styleUrl: './partida-detalhes.css'
})
export class PartidaDetalhes implements OnInit {
  readonly usuario = this.auth.usuario;
  aba: 'resumo' | 'estatisticas' | 'gerenciar' | 'escalacao' = 'resumo';
  partidaId = '';
  partida: any = {};
  torneio: any = {};
  times: any[] = [];
  oportunidade?: PartidaPalpite;
  percentuais: PercentuaisPalpite = { totalPalpites: 0, percentuaisPorOpcao: {} };
  golsMandante: number | null = 0;
  golsVisitante: number | null = 0;
  scoutAtivo = false;
  eventos: any[] = [];
  jogadores: any[] = [];
  artilharia: any[] = [];
  assistencias: any[] = [];
  melhoresNotas: any[] = [];
  jogadorSelecionado = '';
  tipoEvento = 'GOL';
  jogadorSaiuId = '';
  jogadorEntrouId = '';
  ordemParadaJogo: number | null = null;
  descricaoParadaJogo = '';
  carregando = true;
  processando = '';
  mensagem = '';
  erro = '';

  // F8 - Mesa Tática
  escalacaoMandante: any = null;
  escalacaoVisitante: any = null;
  modoExibicaoPublica = 'LISTAS';
  podeEditarMandante = false;
  podeEditarVisitante = false;
  elencoMandante: any[] = [];
  elencoVisitante: any[] = [];
  timeAtivoEscalacao: 'mandante' | 'visitante' = 'mandante';
  editandoEscalacao = false;
  salvandoEscalacao = false;
  tipoVisualizacaoEditando: 'LISTA_TITULARES' | 'LISTA_COMPLETA' | 'MESA_TATICA' = 'LISTA_COMPLETA';
  esquemaEditando = '';
  titularesEditando: string[] = [];
  reservasEditando: string[] = [];
  dataHoraAgendada = '';
  localPartida = '';

  readonly esquemasPorFormato: Record<string, { valor: string; label: string }[]> = {
    ONZE_POR_ONZE: [
      { valor: 'QUATRO_QUATRO_DOIS', label: '4-4-2' },
      { valor: 'QUATRO_TRES_TRES', label: '4-3-3' },
      { valor: 'TRES_CINCO_DOIS', label: '3-5-2' },
      { valor: 'QUATRO_DOIS_TRES_UM', label: '4-2-3-1' }
    ],
    SETE_POR_SETE: [
      { valor: 'DOIS_TRES_UM', label: '2-3-1' },
      { valor: 'TRES_DOIS_UM', label: '3-2-1' },
      { valor: 'TRES_UM_DOIS', label: '3-1-2' }
    ],
    CINCO_POR_CINCO: [
      { valor: 'UM_DOIS_UM', label: '1-2-1' },
      { valor: 'DOIS_UM_UM', label: '2-1-1' },
      { valor: 'DOIS_DOIS', label: '2-2' }
    ],
    TRES_POR_TRES: [
      { valor: 'UM_UM', label: '1-1' },
      { valor: 'DOIS_UM', label: '2-1' }
    ]
  };

  private readonly distribuicaoPorEsquema: Record<string, Record<string, number>> = {
    QUATRO_QUATRO_DOIS: { GOLEIRO: 1, DEFENSOR: 4, MEIO_CAMPISTA: 4, ATACANTE: 2 },
    QUATRO_TRES_TRES:   { GOLEIRO: 1, DEFENSOR: 4, MEIO_CAMPISTA: 3, ATACANTE: 3 },
    TRES_CINCO_DOIS:    { GOLEIRO: 1, DEFENSOR: 3, MEIO_CAMPISTA: 5, ATACANTE: 2 },
    QUATRO_DOIS_TRES_UM:{ GOLEIRO: 1, DEFENSOR: 4, MEIO_CAMPISTA: 5, ATACANTE: 1 },
    DOIS_TRES_UM:       { GOLEIRO: 1, DEFENSOR: 2, MEIO_CAMPISTA: 3, ATACANTE: 1 },
    TRES_DOIS_UM:       { GOLEIRO: 1, DEFENSOR: 3, MEIO_CAMPISTA: 2, ATACANTE: 1 },
    TRES_UM_DOIS:       { GOLEIRO: 1, DEFENSOR: 3, MEIO_CAMPISTA: 1, ATACANTE: 2 },
    UM_DOIS_UM:         { GOLEIRO: 1, DEFENSOR: 1, MEIO_CAMPISTA: 2, ATACANTE: 1 },
    DOIS_UM_UM:         { GOLEIRO: 1, DEFENSOR: 2, MEIO_CAMPISTA: 1, ATACANTE: 1 },
    DOIS_DOIS:          { GOLEIRO: 1, DEFENSOR: 2, MEIO_CAMPISTA: 0, ATACANTE: 2 },
    UM_UM:              { GOLEIRO: 1, DEFENSOR: 1, MEIO_CAMPISTA: 0, ATACANTE: 1 },
    DOIS_UM:            { GOLEIRO: 1, DEFENSOR: 2, MEIO_CAMPISTA: 0, ATACANTE: 1 }
  };

  constructor(
    private readonly http: HttpClient,
    private readonly route: ActivatedRoute,
    private readonly auth: AuthService,
    readonly palpites: PalpiteService
  ) {}

  ngOnInit() {
    this.partidaId = this.route.snapshot.paramMap.get('id') ?? '';
    if (this.route.snapshot.queryParamMap.get('gerenciar') === 'true') {
      this.aba = 'gerenciar';
      this.scoutAtivo = true;
    } else if (this.route.snapshot.queryParamMap.get('escalar') === 'true') {
      this.aba = 'escalacao';
    }
    this.carregar();
  }

  get nomeMandante(): string {
    return this.nomeTime(this.partida.mandanteId);
  }

  get nomeVisitante(): string {
    return this.nomeTime(this.partida.visitanteId);
  }

  get podeRegistrarResultado(): boolean {
    return this.partida.iniciada
      && !this.partida.encerrada
      && this.usuario()?.podeCriarTorneio === true
      && String(this.usuario()?.id) === String(this.torneio.organizadorId);
  }

  get podeIniciarPartida(): boolean {
    return !this.partida.iniciada
      && !this.partida.encerrada
      && this.usuario()?.podeCriarTorneio === true
      && String(this.usuario()?.id) === String(this.torneio.organizadorId);
  }

  get podeGerenciarScout(): boolean {
    return this.partida.iniciada
      && this.usuario()?.podeCriarTorneio === true
      && String(this.usuario()?.id) === String(this.torneio.organizadorId);
  }

  get podeGerenciarPartida(): boolean {
    return this.podeIniciarPartida || this.podeGerenciarScout;
  }

  abrirAba(aba: 'resumo' | 'estatisticas' | 'gerenciar' | 'escalacao') {
    if (aba === 'gerenciar' && !this.podeGerenciarPartida) return;
    this.aba = aba;
    this.mensagem = '';
    this.erro = '';
    if (aba === 'escalacao') this.carregarEscalacoes();
  }

  // F8 - Mesa Tática
  get slotsDoEsquema(): { posicao: string; label: string }[] {
    const dist = this.distribuicaoPorEsquema[this.esquemaEditando] ?? {};
    const slots: { posicao: string; label: string }[] = [];
    const ordemPosicoes = ['GOLEIRO', 'DEFENSOR', 'MEIO_CAMPISTA', 'ATACANTE'];
    const labelPosicao: Record<string, string> = {
      GOLEIRO: 'Goleiro', DEFENSOR: 'Defensor', MEIO_CAMPISTA: 'Meia', ATACANTE: 'Atacante'
    };
    for (const posicao of ordemPosicoes) {
      for (let i = 0; i < (dist[posicao] ?? 0); i++) {
        const qtd = dist[posicao] ?? 0;
        slots.push({ posicao, label: `${labelPosicao[posicao]}${qtd > 1 ? ' ' + (i + 1) : ''}` });
      }
    }
    return slots;
  }

  get slotsDaLista(): { posicao: string; label: string }[] {
    return Array.from({ length: this.quantidadeJogadoresFormato }, (_, indice) => ({
      posicao: indice === 0 ? 'GOLEIRO' : 'ATACANTE',
      label: indice === 0 ? 'Goleiro' : `Titular ${indice + 1}`
    }));
  }

  get slotsEditando(): { posicao: string; label: string }[] {
    return this.tipoVisualizacaoEditando === 'MESA_TATICA'
      ? this.slotsDoEsquema
      : this.slotsDaLista;
  }

  get quantidadeJogadoresFormato(): number {
    const quantidades: Record<string, number> = {
      TRES_POR_TRES: 3,
      CINCO_POR_CINCO: 5,
      SETE_POR_SETE: 7,
      ONZE_POR_ONZE: 11
    };
    return quantidades[this.formatoTorneio] ?? 11;
  }

  get esquemasDisponiveis(): { valor: string; label: string }[] {
    return this.esquemasPorFormato[this.formatoTorneio] ?? [];
  }

  get formatoTorneio(): string {
    return this.torneio?.formatoEquipe ?? 'ONZE_POR_ONZE';
  }

  get elencoAtivoEscalacao(): any[] {
    return this.timeAtivoEscalacao === 'mandante' ? this.elencoMandante : this.elencoVisitante;
  }

  get escalacaoAtiva(): any {
    return this.timeAtivoEscalacao === 'mandante' ? this.escalacaoMandante : this.escalacaoVisitante;
  }

  get timeIdAtivo(): string {
    return String(this.timeAtivoEscalacao === 'mandante' ? this.partida.mandanteId : this.partida.visitanteId);
  }

  get podeEditarEscalacaoAtiva(): boolean {
    if (!this.usuario() || this.partida.iniciada || this.partida.encerrada) return false;
    return this.timeAtivoEscalacao === 'mandante'
      ? this.podeEditarMandante
      : this.podeEditarVisitante;
  }

  get exibirMesasTaticas(): boolean {
    return false;
  }

  selecionarTimeEscalacao(lado: 'mandante' | 'visitante') {
    this.timeAtivoEscalacao = lado;
    this.editandoEscalacao = false;
    this.esquemaEditando = '';
    this.titularesEditando = [];
    this.reservasEditando = [];
  }

  iniciarEdicaoEscalacao() {
    const escalacao = this.escalacaoAtiva;
    this.tipoVisualizacaoEditando = escalacao?.tipoVisualizacao ?? 'LISTA_COMPLETA';
    this.esquemaEditando = escalacao?.esquemaTatico ?? this.esquemasDisponiveis[0]?.valor ?? '';
    this.atualizarSlotsEsquema();
    if (escalacao?.titulares?.length) {
      const ids = escalacao.titulares.map((t: any) => String(t.jogadorId));
      this.titularesEditando = ids;
    }
    this.reservasEditando = escalacao?.reservas?.map(String) ?? [];
    this.editandoEscalacao = true;
  }

  atualizarSlotsEsquema() {
    const total = this.slotsEditando.length;
    const novos: string[] = Array(total).fill('');
    for (let i = 0; i < Math.min(novos.length, this.titularesEditando.length); i++) {
      novos[i] = this.titularesEditando[i];
    }
    this.titularesEditando = novos;
  }

  salvarEscalacao() {
    if (this.tipoVisualizacaoEditando === 'MESA_TATICA' && !this.esquemaEditando) return;
    if (this.salvandoEscalacao) return;
    const titularesValidos = this.titularesEditando.filter(id => !!id);
    if (titularesValidos.length !== this.slotsEditando.length) {
      this.erro = 'Preencha todos os titulares antes de salvar.';
      return;
    }
    this.salvandoEscalacao = true;
    this.erro = '';
    const titulares = this.slotsEditando
      .map((slot, i) => ({ jogadorId: this.titularesEditando[i], posicao: slot.posicao }))
      .filter(t => !!t.jogadorId);
    this.http.post('/backend/escalacao/salvar-por-responsavel', {
      partidaId: this.partidaId,
      timeId: this.timeIdAtivo,
      tipoVisualizacao: this.tipoVisualizacaoEditando,
      esquemaTatico: this.tipoVisualizacaoEditando === 'MESA_TATICA' ? this.esquemaEditando : null,
      titulares,
      reservas: this.tipoVisualizacaoEditando === 'LISTA_TITULARES'
        ? []
        : this.reservasEditando.filter(id => !!id)
    }).pipe(finalize(() => this.salvandoEscalacao = false)).subscribe({
      next: () => {
        this.editandoEscalacao = false;
        this.mensagem = 'Escalacao salva com sucesso.';
        this.carregarEscalacoes();
      },
      error: (e: any) => this.erro = this.mensagemErro(e)
    });
  }

  toggleReserva(jogadorId: string) {
    const idx = this.reservasEditando.indexOf(jogadorId);
    if (idx >= 0) {
      this.reservasEditando.splice(idx, 1);
    } else {
      this.reservasEditando.push(jogadorId);
    }
  }

  nomeAbreviado(jogadorId: string | number): string {
    const nome = this.nomeJogador(jogadorId);
    const partes = nome.split(' ');
    return partes.length > 1 ? `${partes[0]} ${partes[partes.length - 1]}` : nome;
  }

  tipoEscalacaoLabel(tipo: string): string {
    const labels: Record<string, string> = {
      MESA_TATICA: 'Mesa tatica',
      LISTA_TITULARES: 'Somente titulares',
      LISTA_COMPLETA: 'Titulares e reservas'
    };
    return labels[tipo] ?? 'Escalacao';
  }

  formacaoLabel(esquema: string | null): string {
    return this.esquemasDisponiveis.find(item => item.valor === esquema)?.label
      ?? esquema?.replaceAll('_', '-')?.toLowerCase()
      ?? '';
  }

  carregarEscalacoes() {
    if (!this.partidaId) return;
    if (this.partida.iniciada || this.partida.encerrada) {
      this.http.get<any>(`/backend/escalacao/partida/${this.partidaId}/publica`)
        .pipe(catchError(() => of({ modoExibicao: 'LISTAS', escalacoes: [] })))
        .subscribe(resultado => {
          this.modoExibicaoPublica = resultado.modoExibicao;
          this.escalacaoMandante = resultado.escalacoes.find(
            (item: any) => String(item.timeId) === String(this.partida.mandanteId)) ?? null;
          this.escalacaoVisitante = resultado.escalacoes.find(
            (item: any) => String(item.timeId) === String(this.partida.visitanteId)) ?? null;
        });
      return;
    }

    const mandante = this.podeEditarMandante
      ? this.http.get<any>(`/backend/escalacao/partida/${this.partidaId}/time/${this.partida.mandanteId}`)
          .pipe(catchError(() => of(null)))
      : of(null);
    const visitante = this.podeEditarVisitante
      ? this.http.get<any>(`/backend/escalacao/partida/${this.partidaId}/time/${this.partida.visitanteId}`)
          .pipe(catchError(() => of(null)))
      : of(null);
    forkJoin({ mandante, visitante }).subscribe(resultado => {
      this.escalacaoMandante = resultado.mandante;
      this.escalacaoVisitante = resultado.visitante;
    });
  }

  escolhaAtual(): string | null {
    if (!this.partida.torneioId) return null;
    return this.palpites.escolha(
      'VENCEDOR_PARTIDA',
      String(this.partida.torneioId),
      this.partidaId
    );
  }

  votar(opcao: OpcaoPalpite) {
    if (!this.oportunidade || this.processando) return;
    this.processando = `voto-${opcao.id}`;
    this.mensagem = '';
    this.erro = '';
    this.palpites.votar(
      'VENCEDOR_PARTIDA',
      String(this.oportunidade.torneioId),
      String(this.oportunidade.id),
      String(opcao.id)
    ).pipe(finalize(() => this.processando = '')).subscribe({
      next: () => {
        this.mensagem = `Palpite em ${opcao.nome} registrado.`;
        this.atualizarPercentuais();
      },
      error: (erro: any) => this.erro = this.mensagemErro(erro)
    });
  }

  salvarResultado() {
    if (!this.podeRegistrarResultado || this.processando) return;
    if (this.golsMandante == null || this.golsVisitante == null
        || this.golsMandante < 0 || this.golsVisitante < 0) {
      this.erro = 'Informe o placar oficial completo com valores iguais ou maiores que zero.';
      return;
    }
    this.processando = 'resultado';
    this.mensagem = '';
    this.erro = '';
    this.http.post(
      `/backend/partida/${this.partidaId}/registrar-resultado`
        + `?torneioId=${encodeURIComponent(String(this.partida.torneioId))}`
        + `&organizadorId=${encodeURIComponent(String(this.usuario()?.id))}`,
      { golsMandante: this.golsMandante, golsVisitante: this.golsVisitante }
    ).pipe(finalize(() => this.processando = '')).subscribe({
      next: (resultado: any) => {
        this.artilharia = resultado.artilharia ?? [];
        this.assistencias = resultado.assistencias ?? [];
        this.melhoresNotas = resultado.melhoresNotas ?? [];
        this.mensagem = 'Placar salvo. Andamento, palpites e rankings foram atualizados automaticamente.';
        this.carregar();
      },
      error: (erro: any) => this.erro = this.mensagemErro(erro)
    });
  }

  registrarEvento() {
    if (!this.podeGerenciarScout || !this.jogadorSelecionado || this.processando) return;
    const rotas: Record<string, string> = {
      GOL: 'registrar-gol',
      ASSISTENCIA: 'registrar-assistencia',
      CARTAO_AMARELO: 'registrar-cartao-amarelo',
      CARTAO_VERMELHO: 'registrar-cartao-vermelho'
    };
    this.executarScout(
      this.http.post(`/backend/sumula-estatistica/${rotas[this.tipoEvento]}`, {
        torneioId: this.partida.torneioId,
        partidaId: this.partidaId,
        organizadorId: this.usuario()?.id,
        jogadorId: this.jogadorSelecionado
      }),
      'Evento registrado e rankings recalculados.'
    );
  }

  registrarSubstituicao() {
    if (!this.podeGerenciarScout || !this.jogadorSaiuId || !this.jogadorEntrouId || this.processando) return;
    this.executarScout(
      this.http.post('/backend/sumula-estatistica/registrar-substituicao', {
        torneioId: this.partida.torneioId,
        partidaId: this.partidaId,
        organizadorId: this.usuario()?.id,
        jogadorSaiuId: this.jogadorSaiuId,
        jogadorEntrouId: this.jogadorEntrouId,
        ordemParadaJogo: this.ordemParadaJogo,
        descricaoParadaJogo: this.descricaoParadaJogo || null
      }),
      'Substituicao registrada no scout.'
    );
  }

  corrigirEvento(evento: any, novoTipo: string) {
    if (!this.podeGerenciarScout || evento.automatico || novoTipo === evento.tipo || this.processando) return;
    this.executarScout(
      this.http.post(`/backend/sumula-estatistica/${evento.id}/corrigir-evento`, {
        torneioId: this.partida.torneioId,
        partidaId: this.partidaId,
        organizadorId: this.usuario()?.id,
        jogadorId: evento.jogadorId,
        novoTipo
      }),
      'Evento corrigido e efeitos derivados reprocessados.'
    );
  }

  removerEvento(evento: any) {
    if (!this.podeGerenciarScout || evento.automatico || this.processando) return;
    const query = new URLSearchParams({
      torneioId: String(this.partida.torneioId),
      partidaId: this.partidaId,
      organizadorId: String(this.usuario()?.id)
    });
    this.executarScout(
      this.http.post(`/backend/sumula-estatistica/${evento.id}/remover?${query}`, {}),
      'Evento removido e efeitos derivados reprocessados.'
    );
  }

  nomeJogador(id: string | number): string {
    return this.jogadores.find(jogador => String(jogador.id) === String(id))?.nome ?? `Jogador #${id}`;
  }

  notaJogador(id: string | number): number | null {
    return this.melhoresNotas.find(item => String(item.jogadorId) === String(id))?.media ?? null;
  }

  iniciarPartida() {
    if (!this.podeIniciarPartida || this.processando) return;
    this.processando = 'iniciar';
    this.mensagem = '';
    this.erro = '';
    this.http.post(
      `/backend/partida/${this.partidaId}/iniciar`
        + `?torneioId=${encodeURIComponent(String(this.partida.torneioId))}`
        + `&organizadorId=${encodeURIComponent(String(this.usuario()?.id))}`,
      {}
    ).pipe(finalize(() => this.processando = '')).subscribe({
      next: () => {
        this.mensagem = 'Partida iniciada. A janela de palpites foi encerrada.';
        this.carregar();
      },
      error: (erro: any) => this.erro = this.mensagemErro(erro)
    });
  }

  salvarAgendamento() {
    if (!this.podeIniciarPartida || !this.dataHoraAgendada || this.processando) return;
    this.processando = 'agendamento';
    this.mensagem = '';
    this.erro = '';
    this.http.post(`/backend/partida/${this.partidaId}/agendar`, {
      torneioId: this.partida.torneioId,
      dataHora: this.dataHoraAgendada,
      local: this.localPartida || null
    }).pipe(finalize(() => this.processando = '')).subscribe({
      next: () => {
        this.mensagem = 'Data, horario e local da partida foram atualizados.';
        this.carregar();
      },
      error: (erro: any) => this.erro = this.mensagemErro(erro)
    });
  }

  private carregar() {
    this.carregando = true;
    this.http.get<any>(`/backend/partida/${this.partidaId}`).pipe(
      switchMap(partida => forkJoin({
        partida: of(partida),
        torneio: this.http.get<any>(`/backend/preparacao-torneio/${partida.torneioId}`)
          .pipe(catchError(() => this.http.get<any[]>('/backend/torneio/pesquisa').pipe(
            switchMap(torneios => of(
              torneios.find(item => String(item.id) === String(partida.torneioId)) ?? {}
            ))
          ))),
        times: this.http.get<any[]>('/backend/time/pesquisa?nome=').pipe(catchError(() => of([]))),
        oportunidade: this.palpites.oportunidadePartida(this.partidaId)
          .pipe(catchError(() => of(undefined))),
        mandante: this.http.get<any>(`/backend/time/${partida.mandanteId}/edicao`).pipe(catchError(() => of(null))),
        visitante: this.http.get<any>(`/backend/time/${partida.visitanteId}/edicao`).pipe(catchError(() => of(null))),
        eventos: this.http.get<any[]>(`/backend/sumula-estatistica/partida/${this.partidaId}`)
          .pipe(catchError(() => of([]))),
        artilharia: this.http.get<any[]>(`/backend/ranking-estatistico/${partida.torneioId}/artilharia`)
          .pipe(catchError(() => of([]))),
        assistencias: this.http.get<any[]>(`/backend/ranking-estatistico/${partida.torneioId}/assistencias`)
          .pipe(catchError(() => of([]))),
        melhoresNotas: this.http.get<any[]>(
          `/backend/ranking-estatistico/${partida.torneioId}/melhores-medias?minimoPartidas=1`
        ).pipe(catchError(() => of([]))),
        profissionais: this.http.get<any[]>('/backend/profissional/pesquisa?nome=')
          .pipe(catchError(() => of([])))
      })),
      finalize(() => this.carregando = false)
    ).subscribe({
      next: dados => {
        this.partida = dados.partida;
        this.torneio = dados.torneio;
        this.times = dados.times;
        this.golsMandante = this.partida.golsMandante ?? 0;
        this.golsVisitante = this.partida.golsVisitante ?? 0;
        this.dataHoraAgendada = this.paraDataHoraLocal(this.partida.dataHoraAgendada);
        this.localPartida = this.partida.localPartida ?? '';
        this.eventos = dados.eventos;
        this.artilharia = dados.artilharia;
        this.assistencias = dados.assistencias;
        this.melhoresNotas = dados.melhoresNotas;
        const elencos = [
          ...(dados.mandante?.time?.elenco ?? []),
          ...(dados.visitante?.time?.elenco ?? [])
        ];
        this.elencoMandante = dados.mandante?.time?.elenco ?? [];
        this.elencoVisitante = dados.visitante?.time?.elenco ?? [];
        this.podeEditarMandante = dados.mandante?.podeEscalarTime === true;
        this.podeEditarVisitante = dados.visitante?.podeEscalarTime === true;
        if (!this.podeEditarMandante && this.podeEditarVisitante) {
          this.timeAtivoEscalacao = 'visitante';
        }
        const jogadoresElenco = elencos
          .filter((vinculo: any) => vinculo.tipoProfissional === 'JOGADOR'
            || String(vinculo.funcao).toUpperCase() === 'JOGADOR')
          .map((vinculo: any) => ({
            id: String(vinculo.profissionalId),
            nome: vinculo.nomeProfissional,
            time: (dados.mandante?.time?.elenco ?? []).includes(vinculo) ? this.nomeMandante : this.nomeVisitante
          }));
        this.jogadores = dados.profissionais.map((profissional: any) => ({
          id: String(profissional.id),
          nome: profissional.nome,
          time: jogadoresElenco.find((item: any) => String(item.id) === String(profissional.id))?.time
        }));
        this.oportunidade = dados.oportunidade;
        if (this.oportunidade) {
          this.percentuais = this.oportunidade.percentuais;
        } else if (this.partida.torneioId) {
          this.atualizarPercentuais();
        }
        if (this.aba === 'escalacao') {
          this.carregarEscalacoes();
        }
      },
      error: (erro: any) => this.erro = this.mensagemErro(erro)
    });
  }

  private atualizarPercentuais() {
    this.palpites.percentuais(
      'VENCEDOR_PARTIDA',
      String(this.partida.torneioId),
      this.partidaId
    ).subscribe(percentuais => {
      this.percentuais = percentuais;
      if (this.oportunidade) this.oportunidade.percentuais = percentuais;
    });
  }

  private executarScout(requisicao: any, mensagem: string) {
    this.processando = 'scout';
    this.mensagem = '';
    this.erro = '';
    requisicao.pipe(finalize(() => this.processando = '')).subscribe({
      next: () => {
        this.mensagem = mensagem;
        this.jogadorSelecionado = '';
        this.jogadorSaiuId = '';
        this.jogadorEntrouId = '';
        this.ordemParadaJogo = null;
        this.descricaoParadaJogo = '';
        this.carregar();
      },
      error: (erro: any) => this.erro = this.mensagemErro(erro)
    });
  }

  private nomeTime(id: string): string {
    return this.times.find(time => String(time.id) === String(id))?.nome ?? `Time #${id}`;
  }

  private paraDataHoraLocal(valor: string | null | undefined): string {
    if (!valor) return '';
    return valor.length >= 16 ? valor.slice(0, 16) : valor;
  }

  private mensagemErro(erro: any): string {
    return erro?.error?.mensagem
      ?? erro?.error?.detail
      ?? erro?.error?.message
      ?? 'Nao foi possivel concluir esta acao.';
  }
}
