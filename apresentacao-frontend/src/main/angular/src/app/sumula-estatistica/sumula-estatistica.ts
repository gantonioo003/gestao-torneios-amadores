import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-sumula-estatistica',
  imports: [FormsModule],
  templateUrl: './sumula-estatistica.html'
})
export class SumulaEstatistica {
  aba = 'Todos';
  abas = ['Todos', 'Gols', 'Assistencias', 'Cartoes', 'Substituicoes'];
  eventos = [
    { min: 12, jogador: 'Joao Silva', time: 'Unidos do Bairro', tipo: 'Gol', detalhe: 'Finalizacao cruzada' },
    { min: 27, jogador: 'Carlos Lima', time: 'Unidos do Bairro', tipo: 'Falta', detalhe: 'Falta lateral' },
    { min: 45, jogador: 'Pedro Santos', time: 'Unidos do Bairro', tipo: 'Gol', detalhe: 'Cabecada' },
    { min: 67, jogador: 'Matheus Alves', time: 'Real Esperanca', tipo: 'Cartao', detalhe: 'Amarelo' },
    { min: 71, jogador: 'Gabriel Sousa', time: 'Unidos do Bairro', tipo: 'Substituicao', detalhe: 'Entrou no segundo tempo' }
  ];

  eventosFiltrados() {
    if (this.aba === 'Todos') return this.eventos;
    if (this.aba === 'Gols') return this.eventos.filter(e => e.tipo === 'Gol');
    if (this.aba === 'Cartoes') return this.eventos.filter(e => e.tipo === 'Cartao');
    if (this.aba === 'Substituicoes') return this.eventos.filter(e => e.tipo === 'Substituicao');
    return this.eventos;
  }
}
