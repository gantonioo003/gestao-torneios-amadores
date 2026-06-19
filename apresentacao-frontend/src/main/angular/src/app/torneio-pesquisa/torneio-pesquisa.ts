import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { catchError, of } from 'rxjs';
import { AuthService } from '../core/auth.service';

interface TorneioResumo {
  id: number;
  nome: string;
  imagemUrl?: string;
  formato: string;
  formatoEquipe: string;
  organizadorId: number;
  status: string;
  aceitaSolicitacoes: boolean;
}

@Component({
  selector: 'app-torneio-pesquisa',
  imports: [FormsModule, RouterLink],
  templateUrl: './torneio-pesquisa.html',
  styleUrl: './torneio-pesquisa.css'
})
export class TorneioPesquisa implements OnInit {
  termo = '';
  torneios: TorneioResumo[] = [];
  carregando = true;
  readonly usuario = this.auth.usuario;

  constructor(
    private readonly http: HttpClient,
    private readonly auth: AuthService
  ) {}

  ngOnInit() {
    this.buscar();
  }

  buscar() {
    this.carregando = true;
    this.http.get<TorneioResumo[]>(`/backend/torneio/pesquisa?nome=${encodeURIComponent(this.termo)}`)
      .pipe(catchError(() => of([])))
      .subscribe(torneios => {
        this.torneios = torneios;
        this.carregando = false;
      });
  }

  get emAlta(): TorneioResumo[] {
    return [...this.torneios]
      .sort((a, b) => this.pontuacao(b) - this.pontuacao(a))
      .slice(0, 3);
  }

  formatoLabel(formato: string): string {
    const labels: Record<string, string> = {
      FASE_DE_GRUPOS_COM_MATA_MATA: 'Grupos + mata-mata',
      PONTOS_CORRIDOS: 'Pontos corridos',
      MATA_MATA: 'Mata-mata',
      FINAL_UNICA: 'Final única'
    };
    return labels[formato] ?? formato?.replaceAll('_', ' ');
  }

  statusLabel(status: string): string {
    const labels: Record<string, string> = {
      CONFIGURADO: 'Inscrições',
      ESTRUTURA_GERADA: 'Estrutura pronta',
      INICIADO: 'Em andamento',
      FINALIZADO: 'Finalizado'
    };
    return labels[status] ?? status;
  }

  private pontuacao(torneio: TorneioResumo): number {
    return (torneio.status === 'INICIADO' ? 4 : 0)
      + (torneio.aceitaSolicitacoes ? 3 : 0)
      + (torneio.status === 'CONFIGURADO' ? 2 : 0);
  }
}
