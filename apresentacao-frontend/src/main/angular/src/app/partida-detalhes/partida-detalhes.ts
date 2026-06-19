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
  aba: 'resumo' | 'estatisticas' | 'gerenciar' = 'resumo';
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

  abrirAba(aba: 'resumo' | 'estatisticas' | 'gerenciar') {
    if (aba === 'gerenciar' && !this.podeGerenciarPartida) return;
    this.aba = aba;
    this.mensagem = '';
    this.erro = '';
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
        oportunidades: this.palpites.oportunidades()
          .pipe(catchError(() => of({ torneios: [], partidas: [] } as CentralPalpites))),
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
        ).pipe(catchError(() => of([])))
      })),
      finalize(() => this.carregando = false)
    ).subscribe({
      next: dados => {
        this.partida = dados.partida;
        this.torneio = dados.torneio;
        this.times = dados.times;
        this.golsMandante = this.partida.golsMandante ?? 0;
        this.golsVisitante = this.partida.golsVisitante ?? 0;
        this.eventos = dados.eventos;
        this.artilharia = dados.artilharia;
        this.assistencias = dados.assistencias;
        this.melhoresNotas = dados.melhoresNotas;
        const elencos = [
          ...(dados.mandante?.time?.elenco ?? []),
          ...(dados.visitante?.time?.elenco ?? [])
        ];
        this.jogadores = elencos
          .filter((vinculo: any) => vinculo.tipoProfissional === 'JOGADOR'
            || String(vinculo.funcao).toUpperCase() === 'JOGADOR')
          .map((vinculo: any) => ({
            id: String(vinculo.profissionalId),
            nome: vinculo.nomeProfissional,
            time: (dados.mandante?.time?.elenco ?? []).includes(vinculo) ? this.nomeMandante : this.nomeVisitante
          }));
        this.oportunidade = dados.oportunidades.partidas.find(
          item => String(item.id) === String(this.partidaId)
        );
        if (this.oportunidade) {
          this.percentuais = this.oportunidade.percentuais;
        } else if (this.partida.torneioId) {
          this.atualizarPercentuais();
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

  private mensagemErro(erro: any): string {
    return erro?.error?.mensagem
      ?? erro?.error?.detail
      ?? erro?.error?.message
      ?? 'Nao foi possivel concluir esta acao.';
  }
}
