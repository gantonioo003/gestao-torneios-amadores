import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { catchError, finalize, forkJoin, of } from 'rxjs';
import { AuthService } from '../core/auth.service';

interface OpcaoPalpite {
  id: number;
  nome: string;
}

interface Percentuais {
  totalPalpites: number;
  percentuaisPorOpcao: Record<string, number>;
}

interface TorneioDisponivel {
  id: number;
  nome: string;
  status: string;
  opcoes: OpcaoPalpite[];
  percentuais: Percentuais;
}

interface PartidaDisponivel {
  id: number;
  torneioId: number;
  torneioNome: string;
  etapa: string;
  mandante: OpcaoPalpite;
  visitante: OpcaoPalpite;
  percentuais: Percentuais;
}

interface PalpiteUsuario {
  id: number;
  tipo: string;
  torneioId: number;
  partidaId?: number;
  opcao: number;
  apurado: boolean;
  acertou?: boolean;
}

@Component({
  selector: 'app-palpites',
  imports: [RouterLink],
  templateUrl: './palpites.html',
  styleUrl: './palpites.css'
})
export class Palpites implements OnInit {
  readonly usuario = this.auth.usuario;
  torneios: TorneioDisponivel[] = [];
  partidas: PartidaDisponivel[] = [];
  meusPalpites: PalpiteUsuario[] = [];
  carregando = true;
  votando = '';
  aba: 'disponiveis' | 'andamento' | 'historico' = 'disponiveis';

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

  percentual(percentuais: Percentuais, opcaoId: number): number {
    return Math.round(percentuais?.percentuaisPorOpcao?.[String(opcaoId)] ?? 0);
  }

  nomeOpcao(palpite: PalpiteUsuario): string {
    if (palpite.tipo === 'VENCEDOR_PARTIDA') {
      const partida = this.partidas.find(item => item.id === palpite.partidaId);
      return [partida?.mandante, partida?.visitante]
        .find(opcao => opcao?.id === palpite.opcao)?.nome ?? `Time #${palpite.opcao}`;
    }
    const torneio = this.torneios.find(item => item.id === palpite.torneioId);
    return torneio?.opcoes.find(opcao => opcao.id === palpite.opcao)?.nome
      ?? `Opcao #${palpite.opcao}`;
  }

  nomeEvento(palpite: PalpiteUsuario): string {
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

    forkJoin({ oportunidades, historico })
      .pipe(finalize(() => this.carregando = false))
      .subscribe(({ oportunidades, historico }) => {
        this.torneios = oportunidades.torneios ?? [];
        this.partidas = oportunidades.partidas ?? [];
        this.meusPalpites = historico;
      });
  }

  private salvarPalpite(
    tipo: string,
    torneioId: number,
    partidaId: number | null,
    opcao: number,
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
}


