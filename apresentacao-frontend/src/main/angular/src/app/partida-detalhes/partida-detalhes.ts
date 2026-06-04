import { Component } from '@angular/core';

@Component({ selector: 'app-partida-detalhes', imports: [], templateUrl: './partida-detalhes.html' })
export class PartidaDetalhes {
  abaEv='Todos'; abasEventos=['Todos','Gols','Assistências','Cartões','Substituições'];
  titulares=[1,2,3,4,5,6,7,8,9,10,11];
  eventos=[
    {id:1,min:12,ico:'⚽',desc:'João Silva',time:'Unidos do Bairro'},
    {id:2,min:27,ico:'🟨',desc:'Cartão amarelo Lucas Lima',time:'Real Esperança'},
    {id:3,min:45,ico:'⚽',desc:'Pedro Santos',time:'Unidos do Bairro'},
    {id:4,min:62,ico:'⚽',desc:'Gabriel Sousa',time:'Real Esperança'},
    {id:5,min:71,ico:'🔄',desc:'Substituição Matheus Alves',time:'Unidos do Bairro'}
  ];
  classificacao=[
    {pos:1,nome:'Unidos do Bairro',j:4,v:3,d:1,gp:8,gc:3,pts:9},
    {pos:2,nome:'Resenha FC',j:4,v:2,d:1,gp:6,gc:4,pts:7},
    {pos:3,nome:'Vila FC',j:4,v:1,d:1,gp:5,gc:5,pts:5},
    {pos:4,nome:'Real Esperança',j:4,v:1,d:3,gp:3,gc:10,pts:3}
  ];
  proximosJogos=[{id:1,timeCasa:'Vila FC',timeVisit:'Os Parças'},{id:2,timeCasa:'Resenha FC',timeVisit:'Unidos do Bairro'}];
}
