import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-torneio-detalhes',
  imports: [RouterLink],
  templateUrl: './torneio-detalhes.html'
})
export class TorneioDetalhes {
  aba = 'participantes';

  confirmados = ['Unidos do Bairro', 'Resenha FC', 'Vila FC', 'Real Esperanca', 'Os Parcas', 'Liga Amigos', 'Super Time', 'Amigos FC'];
  pendentes = ['Goleira FC', 'Bola na Rede', 'Amigos do Futebol'];

  classificacao = [
    { pos: 1, nome: 'Unidos do Bairro', j: 4, v: 3, e: 0, d: 1, gp: 8, gc: 3, pts: 9 },
    { pos: 2, nome: 'Resenha FC', j: 4, v: 2, e: 1, d: 1, gp: 6, gc: 4, pts: 7 },
    { pos: 3, nome: 'Vila FC', j: 4, v: 1, e: 2, d: 1, gp: 5, gc: 5, pts: 5 },
    { pos: 4, nome: 'Real Esperanca', j: 4, v: 1, e: 0, d: 3, gp: 3, gc: 10, pts: 3 }
  ];

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
