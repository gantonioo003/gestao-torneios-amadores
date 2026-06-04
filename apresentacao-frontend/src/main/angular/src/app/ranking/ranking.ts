import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({ selector: 'app-ranking', imports: [FormsModule], templateUrl: './ranking.html' })
export class Ranking {
  aba='geral'; busca=''; jogadorFoco: any = null;
  notas=[{pos:1,nome:'João Silva',nota:7.8},{pos:2,nome:'Pedro Santos',nota:7.5},{pos:3,nome:'Lucas Lima',nota:7.3},{pos:4,nome:'Gabriel Sousa',nota:6.9},{pos:5,nome:'Matheus Alves',nota:6.8}];
  artilharia=[{pos:1,nome:'João Silva',gols:18},{pos:2,nome:'Pedro Santos',gols:15},{pos:3,nome:'Lucas Lima',gols:12},{pos:4,nome:'Gabriel Sousa',gols:9},{pos:5,nome:'Matheus Alves',gols:8}];
  assists=[{pos:1,nome:'Gabriel Sousa',assist:11},{pos:2,nome:'João Silva',assist:9},{pos:3,nome:'Lucas Lima',assist:8},{pos:4,nome:'Pedro Santos',assist:7},{pos:5,nome:'Matheus Alves',assist:6}];
  rankTimes=[{pos:1,nome:'Unidos do Bairro',pts:1540},{pos:2,nome:'Real Esperança',pts:1520},{pos:3,nome:'Resenha FC',pts:1490},{pos:4,nome:'Vila FC',pts:1470},{pos:5,nome:'Os Parças',pts:1420}];
  jogadores=[
    {nome:'João Silva',hist:[{ano:2024,jogos:22,gols:18,assist:7},{ano:2023,jogos:20,gols:14,assist:8},{ano:2022,jogos:18,gols:11,assist:6}]},
    {nome:'Pedro Santos',hist:[{ano:2024,jogos:20,gols:15,assist:5}]}
  ];
  filtrar() { this.jogadorFoco = this.jogadores.find(j => j.nome.toLowerCase().includes(this.busca.toLowerCase())) ?? null; }
}
