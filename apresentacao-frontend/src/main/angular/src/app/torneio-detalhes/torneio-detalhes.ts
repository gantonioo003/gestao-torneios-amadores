import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({ selector: 'app-torneio-detalhes', imports: [RouterLink], templateUrl: './torneio-detalhes.html' })
export class TorneioDetalhes {
  aba = 'participantes';
  confirmados = [{id:1,nome:'Unidos do Bairro'},{id:2,nome:'Resenha FC'},{id:3,nome:'Vila FC'},{id:4,nome:'Real Esperança'},{id:5,nome:'Os Parças'},{id:6,nome:'Liga Amigos'},{id:7,nome:'Super Time'},{id:8,nome:'Amigos FC'}];
  pendentes = [{id:9,nome:'Goleira FC'},{id:10,nome:'Bola na Rede'},{id:11,nome:'Amigos do Futebol'},{id:12,nome:'Unidos da Paz'}];
  classificacao = [
    {pos:1,nome:'Unidos do Bairro',j:4,v:3,e:0,d:1,gp:8,gc:3,pts:9},
    {pos:2,nome:'Resenha FC',j:4,v:2,e:1,d:1,gp:6,gc:4,pts:7},
    {pos:3,nome:'Vila FC',j:4,v:1,e:2,d:1,gp:5,gc:5,pts:5},
    {pos:4,nome:'Real Esperança',j:4,v:1,e:0,d:3,gp:3,gc:10,pts:3}
  ];
  partidas = [
    {id:1,timeCasa:'Unidos do Bairro',placar:'2 × 1',timeVisit:'Real Esperança'},
    {id:2,timeCasa:'Resenha FC',placar:'1 × 1',timeVisit:'Vila FC'}
  ];
  aprovar(s: any) { this.confirmados.push(s); this.pendentes = this.pendentes.filter(p => p.id !== s.id); }
  rejeitar(s: any) { this.pendentes = this.pendentes.filter(p => p.id !== s.id); }
}
