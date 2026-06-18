import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { catchError, forkJoin, of } from 'rxjs';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-torneio-detalhes',
  imports: [RouterLink],
  templateUrl: './torneio-detalhes.html',
  styleUrl: './torneio-detalhes.css'
})
export class TorneioDetalhes implements OnInit {
  aba = 'participantes';
  torneio: any = {};
  times: any[] = [];
  profissionais: any[] = [];
  classificacao: any[] = [];
  artilharia: any[] = [];
  assistencias: any[] = [];
  carregandoRanking = true;
  salvandoTorneio = false;
  readonly usuario = this.auth.usuario;

  constructor(
    private readonly http: HttpClient,
    private readonly route: ActivatedRoute,
    private readonly auth: AuthService
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    forkJoin({
      torneios: this.http.get<any[]>('/backend/torneio/pesquisa').pipe(catchError(() => of([]))),
      times: this.http.get<any[]>('/backend/time/pesquisa?nome=').pipe(catchError(() => of([]))),
      profissionais: this.http.get<any[]>('/backend/profissional/pesquisa?nome=').pipe(catchError(() => of([]))),
      classificacao: this.http.get<any[]>(`/backend/resultado-competicao/${id}/classificacao`).pipe(catchError(() => of([]))),
      artilharia: this.http.get<any[]>(`/backend/ranking-estatistico/${id}/artilharia`).pipe(catchError(() => of([]))),
      assistencias: this.http.get<any[]>(`/backend/ranking-estatistico/${id}/assistencias`).pipe(catchError(() => of([])))
    }).subscribe({
      next: dados => {
        this.torneio = dados.torneios.find(torneio => torneio.id === id) ?? {};
        this.times = dados.times;
        this.profissionais = dados.profissionais;
        this.classificacao = [...dados.classificacao]
          .sort((a, b) => b.pontos - a.pontos || b.saldoGols - a.saldoGols);
        this.artilharia = dados.artilharia;
        this.assistencias = dados.assistencias;
        this.carregandoRanking = false;
      },
      error: () => {
        this.torneio = {};
        this.carregandoRanking = false;
      }
    });
  }

  podeGerenciar(): boolean {
    return this.usuario()?.podeCriarTorneio === true
      && this.usuario()?.id === this.torneio.organizadorId;
  }

  torneioEstaSalvo(): boolean {
    return !!this.torneio.id && (this.usuario()?.torneiosSalvos ?? []).includes(this.torneio.id);
  }

  alternarTorneioSalvo() {
    if (!this.usuario() || !this.torneio.id || this.salvandoTorneio) return;
    this.salvandoTorneio = true;
    const requisicao = this.torneioEstaSalvo()
      ? this.auth.removerTorneioSalvo(this.torneio.id)
      : this.auth.salvarTorneio(this.torneio.id);
    requisicao.subscribe({
      next: () => this.salvandoTorneio = false,
      error: () => this.salvandoTorneio = false
    });
  }

  statusLabel(): string {
    const labels: Record<string, string> = {
      CONFIGURADO: 'Inscrições',
      ESTRUTURA_GERADA: 'Estrutura pronta',
      INICIADO: 'Ao vivo',
      FINALIZADO: 'Finalizado'
    };
    return labels[this.torneio.status] ?? 'Torneio';
  }

  nomeTime(timeId: number): string {
    return this.times.find(time => time.id === timeId)?.nome ?? `Time #${timeId}`;
  }

  nomeProfissional(jogadorId: number): string {
    return this.profissionais.find(profissional => profissional.id === jogadorId)?.nome
      ?? `Jogador #${jogadorId}`;
  }

  confirmados = ['Unidos do Bairro', 'Resenha FC', 'Vila FC', 'Real Esperanca', 'Os Parcas', 'Liga Amigos', 'Super Time', 'Amigos FC'];
  pendentes = ['Goleira FC', 'Bola na Rede', 'Amigos do Futebol'];

  partidas = [
    { id: 1, casa: 'Unidos do Bairro', placar: '2 x 1', visitante: 'Real Esperanca', status: 'Finalizada' },
    { id: 2, casa: 'Resenha FC', placar: '1 x 1', visitante: 'Vila FC', status: 'Finalizada' },
    { id: 3, casa: 'Os Parcas', placar: 'x', visitante: 'Liga Amigos', status: 'Pendente' }
  ];

  aprovar(time: string) {
    this.confirmados.push(time);
    this.pendentes = this.pendentes.filter(item => item !== time);
  }

  rejeitar(time: string) {
    this.pendentes = this.pendentes.filter(item => item !== time);
  }
}
