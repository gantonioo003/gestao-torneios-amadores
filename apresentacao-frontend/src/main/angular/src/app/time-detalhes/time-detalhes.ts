import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, ActivatedRouteSnapshot, ResolveData, Router, RouterLink, RouterStateSnapshot } from '@angular/router';
import { catchError, finalize, of } from 'rxjs';
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
  participacoes: any[] = [];
  processando = '';
  mensagem = '';
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
    if (this.podeEditarTime) {
      this.carregarParticipacoes();
    }
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

  excluir() {
    if (!confirm('Excluir este time?')) return;
    this.http.post(`/backend/time/${this.time.id}/excluir`, {}, { responseType: 'text' })
      .subscribe({
        next: () => this.router.navigate(['/buscar']),
        error: (e) => alert(this.extrairMensagem(e) ?? 'Erro. Verifique se o time está vinculado a torneios.')
      });
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
