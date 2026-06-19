import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { catchError, finalize, forkJoin, of } from 'rxjs';
import { AuthService } from '../core/auth.service';
import { OpcaoPalpite, PalpiteService, PartidaPalpite } from '../core/palpite.service';

@Component({
  selector: 'app-home-publica',
  imports: [RouterLink],
  templateUrl: './home-publica.html',
  styleUrl: './home-publica.css'
})
export class HomePublica implements OnInit {
  readonly usuario = this.auth.usuario;
  torneiosDestaque: any[] = [];
  partidasPalpite: PartidaPalpite[] = [];
  votando = '';
  mensagemPalpite = '';
  erroPalpite = '';

  constructor(
    private readonly http: HttpClient,
    private readonly auth: AuthService,
    readonly palpitesService: PalpiteService
  ) {}

  ngOnInit() {
    forkJoin({
      torneios: this.http.get<any[]>('/backend/torneio/pesquisa?nome=')
        .pipe(catchError(() => of([]))),
      palpites: this.palpitesService.oportunidades()
        .pipe(catchError(() => of({ torneios: [], partidas: [] })))
    }).subscribe(({ torneios, palpites }) => {
        this.torneiosDestaque = [...torneios]
          .sort((a, b) => (b.status === 'INICIADO' ? 1 : 0) - (a.status === 'INICIADO' ? 1 : 0))
          .slice(0, 4);
        this.partidasPalpite = palpites.partidas.slice(0, 3);
      });
  }

  votarPartida(partida: PartidaPalpite, opcao: OpcaoPalpite) {
    if (this.votando) return;
    this.votando = `${partida.id}-${opcao.id}`;
    this.mensagemPalpite = '';
    this.erroPalpite = '';
    this.palpitesService.votar(
      'VENCEDOR_PARTIDA',
      String(partida.torneioId),
      String(partida.id),
      String(opcao.id)
    ).pipe(finalize(() => this.votando = '')).subscribe({
      next: () => {
        this.mensagemPalpite = `Palpite em ${opcao.nome} registrado.`;
        this.palpitesService.percentuais(
          'VENCEDOR_PARTIDA',
          String(partida.torneioId),
          String(partida.id)
        ).subscribe(percentuais => partida.percentuais = percentuais);
      },
      error: erro => this.erroPalpite = erro?.error?.mensagem
        ?? erro?.error?.message
        ?? 'Nao foi possivel registrar o palpite.'
    });
  }

  escolhaPartida(partida: PartidaPalpite): string | null {
    return this.palpitesService.escolha(
      'VENCEDOR_PARTIDA',
      String(partida.torneioId),
      String(partida.id)
    );
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

  torneiosVivos = [
    { id: 1, nome: 'Copa Bairro 2024', rodada: 'Rodada 4', casa: 'Unidos do Bairro', golsCasa: 2, golsVisitante: 1, visitante: 'Real Esperanca', minuto: 78 },
    { id: 2, nome: 'Liga Amigos', rodada: 'Rodada 2', casa: 'Vila FC', golsCasa: 0, golsVisitante: 0, visitante: 'Resenha FC', minuto: 45 }
  ];

  posts = [
    { autor: 'Copa Bairro 2024', tempo: '2h', texto: 'Unidos do Bairro venceu Real Esperanca por 2x1 em jogo eletrizante.', tag: '#CopaBairro' },
    { autor: 'Liga Amigos', tempo: '4h', texto: 'Rodada com partidas abertas para palpites da comunidade.', tag: '#FutAmador' },
    { autor: 'Real Esperanca FC', tempo: '6h', texto: 'Proximo jogo vale lideranca do grupo.', tag: '#Juntos' }
  ];

  novidades = [
    { icone: 'bi-search', titulo: 'Busca unificada', texto: 'Torneios, times e pessoas no mesmo lugar.' },
    { icone: 'bi-bar-chart', titulo: 'Ranking por torneio', texto: 'Classificacao e destaques dentro de cada competicao.' },
    { icone: 'bi-lightning-charge', titulo: 'Palpites publicos', texto: 'Vote em jogos e acompanhe os percentuais da comunidade.' }
  ];
}
