import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({ selector: 'app-desafio', imports: [FormsModule], templateUrl: './desafio.html' })
export class Desafio {
  timeDesafiado=''; data=''; hora=''; local=''; mensagem='';
  times=[{id:1,nome:'Resenha FC'},{id:2,nome:'Vila FC'},{id:3,nome:'Real Esperança'},{id:4,nome:'Os Parças'}];
  historico=[
    {id:1,data:'10/05/24',jogo:'Unidos do Bairro × 2 × Amigos FC',placar:'2 × 0',status:'Vitória'},
    {id:2,data:'05/05/24',jogo:'Resenha FC × 1 × Unidos do Bairro',placar:'0 × 1',status:'Vitória'},
    {id:3,data:'37/04/24',jogo:'Vila FC × 3 × 2 × Unidos do Bairro',placar:'3 × 2',status:'Derrota'},
    {id:4,data:'27/04/24',jogo:'Unidos do Bairro × 3 × 7 × Goleira FC',placar:'3 × 3',status:'Empate'}
  ];
  propor() { if (!this.timeDesafiado) { alert('Selecione o time desafiado.'); return; } alert('Desafio enviado!'); }
}
