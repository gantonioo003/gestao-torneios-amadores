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
    { id: 1, nome: 'Copa Bairro 2024', rodada: 'Rodada 4', casa: 'Unidos do Bairro', golsCasa: 2, golsVisitante: 1, visitante: 'Real Esperanca', logoCasa: this.escudoDemo('UB', '#087d45', '#f4cf52'), logoVisitante: this.escudoDemo('RE', '#b63832', '#f5f0e8') },
    { id: 2, nome: 'Liga Amigos', rodada: 'Rodada 2', casa: 'Vila FC', golsCasa: 0, golsVisitante: 0, visitante: 'Resenha FC', logoCasa: this.escudoDemo('VF', '#194f89', '#ffffff'), logoVisitante: this.escudoDemo('RF', '#18211f', '#12b85f') }
  ];

  agendaHoje = [
    { id: 1, horario: 'Agora', status: 'AO_VIVO', casa: 'Unidos', visitante: 'Real Esperanca', logoCasa: this.escudoDemo('UB', '#087d45', '#f4cf52'), logoVisitante: this.escudoDemo('RE', '#b63832', '#f5f0e8') },
    { id: 2, horario: '18:30', status: 'AGENDADO', casa: 'Vila FC', visitante: 'Resenha', logoCasa: this.escudoDemo('VF', '#194f89', '#ffffff'), logoVisitante: this.escudoDemo('RF', '#18211f', '#12b85f') },
    { id: 3, horario: '20:00', status: 'AGENDADO', casa: 'Aurora FC', visitante: 'Litoral SC', logoCasa: this.escudoDemo('AF', '#0b8c4b', '#ffffff'), logoVisitante: this.escudoDemo('LS', '#1d6d88', '#f2cc4d') },
    { id: 4, horario: '21:30', status: 'AGENDADO', casa: 'Norte FC', visitante: 'Sertao', logoCasa: this.escudoDemo('NF', '#222f57', '#ffffff'), logoVisitante: this.escudoDemo('ST', '#a65b23', '#ffe081') }
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

  private escudoDemo(iniciais: string, principal: string, apoio: string): string {
    const svg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 118"><path fill="${principal}" d="M50 3 94 18v39c0 29-17 48-44 58C23 105 6 86 6 57V18Z"/><path fill="none" stroke="${apoio}" stroke-width="5" d="M50 15 82 26v30c0 20-11 34-32 43-21-9-32-23-32-43V26Z"/><text x="50" y="68" text-anchor="middle" fill="${apoio}" font-family="Arial" font-size="25" font-weight="900">${iniciais}</text></svg>`;
    return `data:image/svg+xml,${encodeURIComponent(svg)}`;
  }
}
