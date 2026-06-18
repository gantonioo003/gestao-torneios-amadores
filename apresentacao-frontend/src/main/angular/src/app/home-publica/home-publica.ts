import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-home-publica',
  imports: [RouterLink],
  templateUrl: './home-publica.html'
})
export class HomePublica {
  readonly usuario = this.auth.usuario;

  constructor(private readonly auth: AuthService) {}

  torneiosVivos = [
    { id: 1, nome: 'Copa Bairro 2024', rodada: 'Rodada 4', casa: 'Unidos do Bairro', golsCasa: 2, golsVisitante: 1, visitante: 'Real Esperanca', minuto: 78 },
    { id: 2, nome: 'Liga Amigos', rodada: 'Rodada 2', casa: 'Vila FC', golsCasa: 0, golsVisitante: 0, visitante: 'Resenha FC', minuto: 45 }
  ];

  rankTimes = [
    { pos: 1, nome: 'Unidos do Bairro', pts: 18 },
    { pos: 2, nome: 'Real Esperanca', pts: 15 },
    { pos: 3, nome: 'Resenha FC', pts: 14 },
    { pos: 4, nome: 'Vila FC', pts: 12 },
    { pos: 5, nome: 'Os Parcas', pts: 11 }
  ];

  rankJogadores = [
    { pos: 1, nome: 'Joao Silva', gols: 18 },
    { pos: 2, nome: 'Pedro Santos', gols: 15 },
    { pos: 3, nome: 'Lucas Lima', gols: 12 },
    { pos: 4, nome: 'Gabriel Souza', gols: 10 },
    { pos: 5, nome: 'Matheus Alves', gols: 9 }
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
}
