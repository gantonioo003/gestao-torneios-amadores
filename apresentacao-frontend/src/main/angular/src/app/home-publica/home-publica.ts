import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({ selector: 'app-home-publica', imports: [RouterLink], templateUrl: './home-publica.html' })
export class HomePublica {
  torneiosVivos = [
    { id:1, nome:'Copa Bairro 2024 · Rodada 4', timeCasa:'Unidos do Bairro', golsCasa:2, golsVisit:1, timeVisit:'Real Esperança', min:67 },
    { id:2, nome:'Liga Amigos · Rodada 3', timeCasa:'Vila FC', golsCasa:0, golsVisit:0, timeVisit:'Os Parças', min:45 }
  ];
  palpites = [
    { id:1, label:'Vencedor', opcoes:[{nome:'Unidos Bairro',pct:42},{nome:'Campeão',pct:38}] },
    { id:2, label:'Artilheiro', opcoes:[{nome:'João Silva',pct:46},{nome:'Lucas Lima',pct:41}] }
  ];
  rankTimes = [{pos:1,nome:'Unidos do Bairro',pts:18},{pos:2,nome:'Real Esperança',pts:15},{pos:3,nome:'Resenha FC',pts:12},{pos:4,nome:'Vila FC',pts:10},{pos:5,nome:'Os Parças',pts:9}];
  rankJogadores = [{pos:1,nome:'João Silva',gols:18},{pos:2,nome:'Pedro Santos',gols:15},{pos:3,nome:'Lucas Lima',gols:12},{pos:4,nome:'Gabriel Sousa',gols:10},{pos:5,nome:'Matheus Alves',gols:9}];
  feedPosts = [
    {id:1,av:'⚽',autor:'Copa Bairro 2024',tempo:'2h',texto:'Que jogo incrível! Unidos do Bairro venceu Real Esperança por 2x1!',tags:'#CopaBairro #Gólaco'},
    {id:2,av:'🏆',autor:'Liga Amigos',tempo:'4h',texto:'Campeonato muito emocionante. Vamos programar juntos!',tags:'#FutAmador'},
    {id:3,av:'🛡',autor:'Real Esperança FC',tempo:'6h',texto:'Vamos pra cima! 💪',tags:'#Juntos'}
  ];
}
