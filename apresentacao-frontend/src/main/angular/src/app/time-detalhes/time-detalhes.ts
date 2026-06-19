import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, ActivatedRouteSnapshot, ResolveData, Router, RouterLink, RouterStateSnapshot } from '@angular/router';
import { catchError, finalize, forkJoin, of } from 'rxjs';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-time-detalhes',
  imports: [RouterLink, FormsModule],
  templateUrl: './time-detalhes.html',
  styleUrl: './time-detalhes.css'
})
export class TimeDetalhes implements OnInit {
  static readonly ID = 'id';
  static readonly RECURSO = 'recurso';

  time: any = {};
  elenco: any[] = [];
  torneios: any[] = [];
  publicacoes: any[] = [];
  aba: 'dados' | 'publicacoes' = 'dados';
  participacoes: any[] = [];
  confrontos: any[] = [];
  partidas: any[] = [];
  meusTimes: any[] = [];
  timesCatalogo: any[] = [];
  abaConfrontos: 'recebidos' | 'enviados' | 'confirmados' | 'historico' = 'recebidos';
  propostaAberta = false;
  timeDesafianteSelecionado = '';
  dataAmistoso = '';
  horaAmistoso = '19:00';
  localAmistoso = '';
  editandoConfrontoId = '';
  novaDataAmistoso = '';
  novaHoraAmistoso = '';
  novoLocalAmistoso = '';
  resultadoConfrontoId = '';
  golsDesafiante?: number;
  golsDesafiado?: number;
  processando = '';
  mensagem = '';
  erro = '';
  podeEditarTime = false;
  podeGerenciarElenco = false;
  readonly usuario = this.auth.usuario;

  constructor(
    private readonly http: HttpClient,
    private readonly rota: ActivatedRoute,
    private readonly router: Router,
    private readonly auth: AuthService
  ) {}

  ngOnInit() {
    const data = this.rota.snapshot.data[TimeDetalhes.RECURSO] ?? {};
    this.time = data.time ?? {};
    this.elenco = data.time?.elenco ?? [];
    this.torneios = data.torneios ?? [];
    this.podeEditarTime = data.podeEditarTime === true;
    this.podeGerenciarElenco = data.podeGerenciarElenco === true;
    if (this.time.id) {
      this.http.get<any[]>(`/backend/feed/identidade/TIME/${this.time.id}`)
        .pipe(catchError(() => of([])))
        .subscribe(publicacoes => this.publicacoes = publicacoes);
    }
    if (this.podeEditarTime) {
      this.carregarParticipacoes();
      this.carregarConfrontos();
      this.carregarPartidas();
    }
    if (this.usuario()?.tipo === 'TREINADOR' && this.usuario()?.podeGerenciarTimes) {
      this.carregarTimesDoTreinador();
      this.carregarCatalogoTimes();
    }
    this.propostaAberta = this.rota.snapshot.queryParamMap.get('desafiar') === 'true';
    const statusConfronto = this.rota.snapshot.queryParamMap.get('status');
    if (['recebidos', 'enviados', 'confirmados', 'historico'].includes(statusConfronto ?? '')) {
      this.abaConfrontos = statusConfronto as typeof this.abaConfrontos;
    }
  }

  get podeProporDesafio(): boolean {
    return this.usuario()?.tipo === 'TREINADOR'
      && this.usuario()?.podeGerenciarTimes === true
      && !this.podeEditarTime
      && this.meusTimes.length > 0;
  }

  get proximosJogos(): any[] {
    return this.partidas
      .filter(partida => !partida.encerrada)
      .sort((a, b) => this.ordemDataPartida(a) - this.ordemDataPartida(b));
  }

  get ultimosJogos(): any[] {
    return this.partidas
      .filter(partida => partida.encerrada)
      .sort((a, b) => this.ordemDataPartida(b) - this.ordemDataPartida(a));
  }

  adversarioPartida(partida: any): string {
    const adversarioId = String(partida.mandanteId) === String(this.time.id)
      ? partida.visitanteId
      : partida.mandanteId;
    return this.nomeTime(adversarioId);
  }

  torneioPartida(partida: any): string {
    return this.torneios.find(torneio => String(torneio.id) === String(partida.torneioId))?.nome
      ?? 'Torneio';
  }

  dataPartida(partida: any): string {
    if (!partida.dataHoraAgendada) return 'Data a definir';
    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: 'short',
      hour: '2-digit',
      minute: '2-digit'
    }).format(new Date(partida.dataHoraAgendada));
  }

  get confrontosRecebidos(): any[] {
    return this.confrontos.filter(item =>
      item.status === 'PROPOSTO'
      && String(item.timeDesafiadoId) === String(this.time.id));
  }

  get confrontosEnviados(): any[] {
    return this.confrontos.filter(item =>
      item.status === 'PROPOSTO'
      && String(item.timeDesafianteId) === String(this.time.id));
  }

  get confrontosConfirmados(): any[] {
    return this.confrontos.filter(item => item.status === 'ACEITO');
  }

  get historicoConfrontos(): any[] {
    return this.confrontos.filter(item =>
      ['RESULTADO_REGISTRADO', 'RECUSADO', 'CANCELADO'].includes(item.status));
  }

  get confrontosDaAba(): any[] {
    const listas = {
      recebidos: this.confrontosRecebidos,
      enviados: this.confrontosEnviados,
      confirmados: this.confrontosConfirmados,
      historico: this.historicoConfrontos
    };
    return listas[this.abaConfrontos];
  }

  tipoLabel(tipo: string): string {
    const labels: Record<string, string> = {
      JOGADOR: 'Jogador',
      TREINADOR: 'Treinador',
      AUXILIAR_TECNICO: 'Auxiliar tecnico',
      PREPARADOR_FISICO: 'Preparador fisico',
      MEDICO: 'Medico'
    };
    return labels[tipo] ?? tipo;
  }

  editarVinculo(v: any) {
    this.router.navigate(['/time', this.time.id, 'vincular'], { state: { vinculo: v } });
  }

  removendoId: any = null;
  motivoSaida = '';
  descricaoSaida = '';

  iniciarRemocao(profissionalId: any) {
    this.removendoId = profissionalId;
    this.motivoSaida = '';
    this.descricaoSaida = '';
  }

  cancelarRemocao() {
    this.removendoId = null;
  }

  confirmarRemocao(profissionalId: any) {
    this.http.post(
      `/backend/time/${this.time.id}/remover-profissional/${profissionalId}`,
      { motivoDeSaida: this.motivoSaida || null, descricao: this.descricaoSaida || null },
      { responseType: 'text' }
    ).subscribe({
      next: () => { this.removendoId = null; this.recarregar(); },
      error: (e) => alert(this.extrairMensagem(e) ?? 'Erro ao remover.')
    });
  }

  funcaoLabel(funcao: string): string {
    return funcao || 'Sem função definida';
  }

  proporDesafio() {
    if (!this.timeDesafianteSelecionado || !this.dataAmistoso || !this.horaAmistoso || !this.localAmistoso.trim()) {
      this.erro = 'Escolha seu time e informe data, horario e local do amistoso.';
      return;
    }
    this.executarConfronto(
      'propor',
      this.http.post('/backend/desafio-amistoso/propor', {
        timeDesafianteId: this.timeDesafianteSelecionado,
        timeDesafiadoId: String(this.time.id),
        dataHora: `${this.dataAmistoso}T${this.horaAmistoso}:00`,
        local: this.localAmistoso.trim()
      }),
      'Desafio enviado. O tecnico adversario foi notificado.',
      false
    );
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

  aceitarDesafio(item: any) {
    this.executarConfronto(
      item.id,
      this.http.post(`/backend/desafio-amistoso/${item.id}/aceitar`, {}),
      'Amistoso confirmado. Os dois times foram notificados.'
    );
  }

  recusarDesafio(item: any) {
    this.executarConfronto(
      item.id,
      this.http.post(`/backend/desafio-amistoso/${item.id}/recusar`, {}),
      'Desafio recusado.'
    );
  }

  cancelarDesafio(item: any) {
    this.executarConfronto(
      item.id,
      this.http.post(`/backend/desafio-amistoso/${item.id}/cancelar`, {}),
      'Desafio cancelado.'
    );
  }

  abrirReagendamento(item: any) {
    this.editandoConfrontoId = String(item.id);
    const [data, hora] = String(item.dataHora ?? '').split('T');
    this.novaDataAmistoso = data ?? '';
    this.novaHoraAmistoso = (hora ?? '').slice(0, 5);
    this.novoLocalAmistoso = item.local ?? '';
  }

  reagendarDesafio(item: any) {
    this.executarConfronto(
      item.id,
      this.http.post(`/backend/desafio-amistoso/${item.id}/reagendar`, {
        novaDataHora: `${this.novaDataAmistoso}T${this.novaHoraAmistoso}:00`,
        novoLocal: this.novoLocalAmistoso.trim()
      }),
      'Data e local do amistoso atualizados.'
    );
  }

  abrirResultado(item: any) {
    this.resultadoConfrontoId = String(item.id);
    this.golsDesafiante = undefined;
    this.golsDesafiado = undefined;
  }

  registrarResultado(item: any) {
    if (this.golsDesafiante == null || this.golsDesafiado == null) {
      this.erro = 'Informe o placar dos dois times.';
      return;
    }
    this.executarConfronto(
      item.id,
      this.http.post(`/backend/desafio-amistoso/${item.id}/registrar-resultado`, {
        golsDesafiante: this.golsDesafiante,
        golsDesafiado: this.golsDesafiado
      }),
      'Resultado registrado no historico dos dois times.'
    );
  }

  nomeTime(timeId: any): string {
    if (String(timeId) === String(this.time.id)) return this.time.nome;
    return this.timesCatalogo.find(time => String(time.id) === String(timeId))?.nome
      ?? `Time #${timeId}`;
  }

  adversario(item: any): string {
    const adversarioId = String(item.timeDesafianteId) === String(this.time.id)
      ? item.timeDesafiadoId
      : item.timeDesafianteId;
    return this.nomeTime(adversarioId);
  }

  statusConfronto(status: string): string {
    const labels: Record<string, string> = {
      PROPOSTO: 'Aguardando resposta',
      ACEITO: 'Amistoso confirmado',
      RECUSADO: 'Recusado',
      CANCELADO: 'Cancelado',
      RESULTADO_REGISTRADO: 'Finalizado'
    };
    return labels[status] ?? status;
  }

  dataConfronto(dataHora: string): string {
    if (!dataHora) return 'Data a combinar';
    const [data, hora] = dataHora.split('T');
    const [ano, mes, dia] = data.split('-');
    return `${dia}/${mes}/${ano} as ${hora.slice(0, 5)}`;
  }

  aceitarConvite(item: any) {
    this.executarParticipacao(item.id, 'aprovar', 'Convite aceito. O organizador foi notificado.');
  }

  recusarConvite(item: any) {
    this.executarParticipacao(item.id, 'rejeitar', 'Convite recusado.');
  }

  cancelarCandidatura(item: any) {
    this.executarParticipacao(item.id, 'cancelar', 'Solicitacao cancelada.');
  }

  private executarParticipacao(id: any, acao: string, mensagem: string) {
    if (this.processando) return;
    this.processando = String(id);
    this.http.post(`/backend/solicitacao-participacao/${id}/${acao}`, {})
      .pipe(finalize(() => this.processando = ''))
      .subscribe({
        next: () => {
          this.mensagem = mensagem;
          this.carregarParticipacoes();
        },
        error: erro => alert(this.extrairMensagem(erro) ?? 'Nao foi possivel concluir a acao.')
      });
  }

  private carregarParticipacoes() {
    this.http.get<any[]>(`/backend/solicitacao-participacao/time?timeId=${this.time.id}`)
      .pipe(catchError(() => of([])))
      .subscribe(itens => this.participacoes = itens);
  }

  private carregarConfrontos() {
    this.http.get<any[]>(`/backend/desafio-amistoso/time?timeId=${this.time.id}`)
      .pipe(catchError(() => of([])))
      .subscribe(itens => this.confrontos = itens);
  }

  private carregarPartidas() {
    if (!this.torneios.length) {
      this.partidas = [];
      return;
    }
    forkJoin(this.torneios.map(torneio =>
      this.http.get<any[]>(`/backend/partida/pesquisa?torneioId=${torneio.id}`)
        .pipe(catchError(() => of([])))
    )).subscribe(resultados => {
      this.partidas = resultados.flat().filter(partida =>
        String(partida.mandanteId) === String(this.time.id)
        || String(partida.visitanteId) === String(this.time.id));
    });
  }

  private ordemDataPartida(partida: any): number {
    return partida.dataHoraAgendada
      ? new Date(partida.dataHoraAgendada).getTime()
      : Number.MAX_SAFE_INTEGER;
  }

  private carregarTimesDoTreinador() {
    this.http.get<any[]>('/backend/time/pesquisa?meus=true')
      .pipe(catchError(() => of([])))
      .subscribe(times => {
        this.meusTimes = times.filter(time => String(time.id) !== String(this.time.id));
        if (!this.timeDesafianteSelecionado && this.meusTimes.length) {
          this.timeDesafianteSelecionado = String(this.meusTimes[0].id);
        }
      });
  }

  private carregarCatalogoTimes() {
    this.http.get<any[]>('/backend/time/pesquisa?nome=')
      .pipe(catchError(() => of([])))
      .subscribe(times => this.timesCatalogo = times);
  }

  private executarConfronto(chave: any, requisicao: any, mensagem: string, recarregar = true) {
    if (this.processando) return;
    this.processando = String(chave);
    this.mensagem = '';
    this.erro = '';
    requisicao.pipe(finalize(() => this.processando = '')).subscribe({
      next: () => {
        this.mensagem = mensagem;
        this.editandoConfrontoId = '';
        this.resultadoConfrontoId = '';
        this.propostaAberta = false;
        if (recarregar && this.podeEditarTime) this.carregarConfrontos();
      },
      error: (erro: any) => this.erro = this.extrairMensagem(erro) ?? 'Nao foi possivel concluir a acao.'
    });
  }

  private extrairMensagem(e: any): string | null {
    try { return JSON.parse(e.error)?.mensagem ?? null; } catch { return e.error?.mensagem ?? e.error?.message ?? null; }
  }

  private recarregar() {
    this.http.get<any>(`/backend/time/${this.time.id}/edicao`)
      .pipe(catchError(() => of({})))
      .subscribe(r => { this.elenco = r.time?.elenco ?? []; });
  }
}

export const TIME_DETALHES_RESOLVEDORES: ResolveData = {};
TIME_DETALHES_RESOLVEDORES[TimeDetalhes.RECURSO] = (rota: ActivatedRouteSnapshot, _s: RouterStateSnapshot) => {
  const id = rota.params[TimeDetalhes.ID];
  return inject(HttpClient).get(`/backend/time/${id}/edicao`).pipe(catchError(() => of({})));
};
