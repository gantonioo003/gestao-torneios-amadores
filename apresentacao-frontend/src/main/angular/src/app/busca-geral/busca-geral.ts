import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { catchError, forkJoin, of } from 'rxjs';
import { AuthService } from '../core/auth.service';

interface TorneioBusca {
  id: number;
  nome: string;
  formato: string;
  status: string;
  aceitaSolicitacoes: boolean;
}

interface TimeBusca {
  id: number;
  nome: string;
  responsavelId: number;
}

interface ProfissionalBusca {
  id: number;
  nome: string;
  tipo: string;
}

@Component({
  selector: 'app-busca-geral',
  imports: [FormsModule, RouterLink],
  templateUrl: './busca-geral.html',
  styleUrl: './busca-geral.css'
})
export class BuscaGeral implements OnInit {
  termo = '';
  torneios: TorneioBusca[] = [];
  times: TimeBusca[] = [];
  profissionais: ProfissionalBusca[] = [];
  meusTimesIds = new Set<string>();
  carregando = true;
  readonly usuario = this.auth.usuario;

  constructor(
    private readonly http: HttpClient,
    private readonly auth: AuthService
  ) {}

  ngOnInit() {
    if (this.usuario()?.tipo === 'TREINADOR') {
      this.http.get<TimeBusca[]>('/backend/time/pesquisa?meus=true')
        .pipe(catchError(() => of([])))
        .subscribe(times => this.meusTimesIds = new Set(times.map(time => String(time.id))));
    }
    this.buscar();
  }

  buscar() {
    const termo = encodeURIComponent(this.termo.trim());
    this.carregando = true;

    forkJoin({
      torneios: this.http.get<TorneioBusca[]>(`/backend/torneio/pesquisa?nome=${termo}`)
        .pipe(catchError(() => of([]))),
      times: this.http.get<TimeBusca[]>(`/backend/time/pesquisa?nome=${termo}`)
        .pipe(catchError(() => of([]))),
      profissionais: this.http.get<ProfissionalBusca[]>(`/backend/profissional/pesquisa?nome=${termo}`)
        .pipe(catchError(() => of([])))
    }).subscribe(({ torneios, times, profissionais }) => {
      this.torneios = torneios;
      this.times = times;
      this.profissionais = profissionais;
      this.carregando = false;
    });
  }

  get totalResultados(): number {
    return this.torneios.length + this.times.length + this.profissionais.length;
  }

  get emDescoberta(): boolean {
    return !this.termo.trim();
  }

  get torneiosDestaque(): TorneioBusca[] {
    return [...this.torneios]
      .sort((a, b) => this.pontuacaoTorneio(b) - this.pontuacaoTorneio(a))
      .slice(0, 3);
  }

  statusTorneio(status: string): string {
    const labels: Record<string, string> = {
      CONFIGURADO: 'Inscricoes abertas',
      ESTRUTURA_GERADA: 'Estrutura pronta',
      INICIADO: 'Em andamento',
      FINALIZADO: 'Finalizado'
    };
    return labels[status] ?? 'Torneio';
  }

  tipoProfissional(tipo: string): string {
    const labels: Record<string, string> = {
      JOGADOR: 'Jogador',
      TREINADOR: 'Treinador',
      AUXILIAR_TECNICO: 'Auxiliar tecnico',
      PREPARADOR_FISICO: 'Preparador fisico',
      MEDICO: 'Medico'
    };
    return labels[tipo] ?? 'Comissao';
  }

  podeDesafiar(time: TimeBusca): boolean {
    return this.usuario()?.tipo === 'TREINADOR'
      && this.usuario()?.podeGerenciarTimes === true
      && this.meusTimesIds.size > 0
      && !this.meusTimesIds.has(String(time.id));
  }

  private pontuacaoTorneio(torneio: TorneioBusca): number {
    return (torneio.status === 'INICIADO' ? 5 : 0)
      + (torneio.aceitaSolicitacoes ? 3 : 0)
      + (torneio.status === 'CONFIGURADO' ? 2 : 0);
  }
}
