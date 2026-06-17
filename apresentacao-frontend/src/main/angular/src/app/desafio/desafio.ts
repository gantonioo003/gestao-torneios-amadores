import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-desafio',
  imports: [FormsModule],
  templateUrl: './desafio.html'
})
export class Desafio {
  timeDesafiado = 'Resenha FC';
  data = '2024-05-24';
  hora = '16:00';
  local = 'Campo da Vila';
  mensagem = 'Bora um bom jogo!';

  times = ['Resenha FC', 'Vila FC', 'Real Esperanca', 'Os Parcas'];
  historico = [
    { data: '10/05/24', jogo: 'Unidos do Bairro 2 x 0 Amigos FC', status: 'Vitoria' },
    { data: '05/05/24', jogo: 'Resenha FC 0 x 1 Unidos do Bairro', status: 'Vitoria' },
    { data: '27/04/24', jogo: 'Vila FC 3 x 2 Unidos do Bairro', status: 'Derrota' },
    { data: '22/04/24', jogo: 'Os Parcas 3 x 3 Unidos do Bairro', status: 'Empate' }
  ];

  propor() {
    alert('Desafio enviado para analise do outro time.');
  }
}
