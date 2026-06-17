import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-ranking',
  imports: [FormsModule, RouterLink],
  templateUrl: './ranking.html'
})
export class Ranking {
  busca = '';
  jogadorFoco: any = null;

  notas = [
    { pos: 1, nome: 'Joao Silva', nota: 7.8 },
    { pos: 2, nome: 'Pedro Santos', nota: 7.5 },
    { pos: 3, nome: 'Lucas Lima', nota: 7.3 },
    { pos: 4, nome: 'Gabriel Souza', nota: 6.9 },
    { pos: 5, nome: 'Matheus Alves', nota: 6.8 }
  ];

  artilharia = [
    { pos: 1, nome: 'Joao Silva', gols: 18 },
    { pos: 2, nome: 'Pedro Santos', gols: 15 },
    { pos: 3, nome: 'Lucas Lima', gols: 12 },
    { pos: 4, nome: 'Gabriel Souza', gols: 9 }
  ];

  assistencias = [
    { pos: 1, nome: 'Gabriel Souza', assist: 11 },
    { pos: 2, nome: 'Joao Silva', assist: 9 },
    { pos: 3, nome: 'Lucas Lima', assist: 8 }
  ];

  jogadores = [
    { nome: 'Joao Silva', time: 'Unidos do Bairro', posicao: 'Atacante', nota: 7.8, gols: 18, assist: 7 },
    { nome: 'Pedro Santos', time: 'Resenha FC', posicao: 'Meia', nota: 7.5, gols: 15, assist: 5 },
    { nome: 'Lucas Lima', time: 'Vila FC', posicao: 'Meia', nota: 7.3, gols: 12, assist: 8 },
    { nome: 'Gabriel Souza', time: 'Unidos do Bairro', posicao: 'Ponta', nota: 6.9, gols: 9, assist: 11 }
  ];

  comparativo = [
    { item: 'Gols', a: 18, b: 15 },
    { item: 'Assistencias', a: 7, b: 5 },
    { item: 'Cartoes', a: 2, b: 3 },
    { item: 'Ranking geral', a: '1o', b: '2o' }
  ];

  filtrar() {
    this.jogadorFoco = this.jogadores.find(j => j.nome.toLowerCase().includes(this.busca.toLowerCase())) ?? this.jogadores[0];
  }
}
