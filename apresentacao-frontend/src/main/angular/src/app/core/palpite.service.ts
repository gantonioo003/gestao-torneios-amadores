import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthService } from './auth.service';

export interface OpcaoPalpite {
  id: string;
  nome: string;
  imagemUrl?: string;
}

export interface PercentuaisPalpite {
  totalPalpites: number;
  percentuaisPorOpcao: Record<string, number>;
}

export interface TorneioPalpite {
  id: string;
  nome: string;
  imagemUrl?: string;
  status: string;
  opcoes: OpcaoPalpite[];
  percentuais: PercentuaisPalpite;
  eventos: EventoTorneioPalpite[];
}

export type TipoPalpite =
  | 'VENCEDOR_PARTIDA'
  | 'CAMPEAO_TORNEIO'
  | 'ARTILHEIRO_TORNEIO'
  | 'LIDER_ASSISTENCIAS_TORNEIO';

export interface EventoTorneioPalpite {
  tipo: Exclude<TipoPalpite, 'VENCEDOR_PARTIDA'>;
  titulo: string;
  opcoes: OpcaoPalpite[];
  percentuais: PercentuaisPalpite;
}

export interface PartidaPalpite {
  id: string;
  torneioId: string;
  torneioNome: string;
  etapa: string;
  mandante: OpcaoPalpite;
  empate: OpcaoPalpite;
  visitante: OpcaoPalpite;
  percentuais: PercentuaisPalpite;
}

export interface CentralPalpites {
  torneios: TorneioPalpite[];
  partidas: PartidaPalpite[];
}

@Injectable({ providedIn: 'root' })
export class PalpiteService {
  constructor(
    private readonly http: HttpClient,
    private readonly auth: AuthService
  ) {}

  oportunidades(): Observable<CentralPalpites> {
    return this.http.get<CentralPalpites>('/backend/palpites/oportunidades');
  }

  oportunidadesTorneio(torneioId: string): Observable<CentralPalpites> {
    return this.http.get<CentralPalpites>(
      `/backend/palpites/oportunidades/torneio/${encodeURIComponent(torneioId)}`
    );
  }

  oportunidadePartida(partidaId: string): Observable<PartidaPalpite> {
    return this.http.get<PartidaPalpite>(
      `/backend/palpites/oportunidades/partida/${encodeURIComponent(partidaId)}`
    );
  }

  votar(
    tipo: TipoPalpite,
    torneioId: string,
    partidaId: string | null,
    opcao: string
  ): Observable<any> {
    const corpo = { tipo, torneioId, partidaId, opcao };
    const requisicao = this.auth.estaAutenticado()
      ? this.http.post('/backend/palpites/salvar', corpo)
      : this.http.post('/backend/palpites/salvar-visitante', {
          ...corpo,
          visitanteId: this.identificadorVisitante()
        });

    return requisicao.pipe(tap(() => this.salvarEscolha(tipo, torneioId, partidaId, opcao)));
  }

  percentuais(
    tipo: TipoPalpite,
    torneioId: string,
    partidaId: string | null
  ): Observable<PercentuaisPalpite> {
    const partida = partidaId ? `&partidaId=${encodeURIComponent(partidaId)}` : '';
    return this.http.get<PercentuaisPalpite>(
      `/backend/palpites/percentuais?tipo=${tipo}&torneioId=${encodeURIComponent(torneioId)}${partida}`
    );
  }

  percentual(percentuais: PercentuaisPalpite | undefined, opcaoId: string): number {
    return Math.round(percentuais?.percentuaisPorOpcao?.[String(opcaoId)] ?? 0);
  }

  escolha(
    tipo: TipoPalpite,
    torneioId: string,
    partidaId: string | null
  ): string | null {
    return localStorage.getItem(this.chaveEscolha(tipo, torneioId, partidaId));
  }

  private salvarEscolha(
    tipo: string,
    torneioId: string,
    partidaId: string | null,
    opcao: string
  ) {
    localStorage.setItem(this.chaveEscolha(tipo, torneioId, partidaId), String(opcao));
  }

  private chaveEscolha(tipo: string, torneioId: string, partidaId: string | null): string {
    return `liga-amadora.palpite:${tipo}:${torneioId}:${partidaId ?? 'torneio'}`;
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
}
