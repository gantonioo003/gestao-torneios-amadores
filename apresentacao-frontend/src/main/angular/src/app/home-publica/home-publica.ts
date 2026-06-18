import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { catchError, of } from 'rxjs';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-home-publica',
  imports: [RouterLink],
  templateUrl: './home-publica.html',
  styleUrl: './home-publica.css'
})
export class HomePublica implements OnInit {
  readonly usuario = this.auth.usuario;
  torneiosDestaque: any[] = [];

  constructor(
    private readonly http: HttpClient,
    private readonly auth: AuthService
  ) {}

  ngOnInit() {
    this.http.get<any[]>('/backend/torneio/pesquisa?nome=')
      .pipe(catchError(() => of([])))
      .subscribe(torneios => {
        this.torneiosDestaque = [...torneios]
          .sort((a, b) => (b.status === 'INICIADO' ? 1 : 0) - (a.status === 'INICIADO' ? 1 : 0))
          .slice(0, 4);
      });
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

  palpites = [
    { pergunta: 'Vencedor', opcao: 'Unidos do Bairro', pct: 42 },
    { pergunta: 'Campeao', opcao: 'Unidos do Bairro', pct: 38 },
    { pergunta: 'Artilheiro', opcao: 'Joao Silva', pct: 46 },
    { pergunta: 'Assistencias', opcao: 'Lucas Lima', pct: 41 }
  ];

  posts = [
    { autor: 'Copa Bairro 2024', tempo: '2h', texto: 'Unidos do Bairro venceu Real Esperanca por 2x1 em jogo eletrizante.', tag: '#CopaBairro' },
    { autor: 'Liga Amigos', tempo: '4h', texto: 'Rodada com partidas abertas para palpites da comunidade.', tag: '#FutAmador' },
    { autor: 'Real Esperanca FC', tempo: '6h', texto: 'Proximo jogo vale lideranca do grupo.', tag: '#Juntos' }
  ];

  novidades = [
    { icone: 'bi-search', titulo: 'Busca unificada', texto: 'Torneios, times e pessoas no mesmo lugar.' },
    { icone: 'bi-bar-chart', titulo: 'Ranking por torneio', texto: 'Classificacao e destaques dentro de cada competicao.' },
    { icone: 'bi-lightning-charge', titulo: 'Palpites publicos', texto: 'Visitantes tambem participam dos percentuais.' }
  ];
}
