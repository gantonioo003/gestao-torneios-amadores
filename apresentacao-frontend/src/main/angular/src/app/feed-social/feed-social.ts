import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({ selector: 'app-feed-social', imports: [FormsModule], templateUrl: './feed-social.html' })
export class FeedSocial {
  aba='todos'; busca=''; novoPost=''; hashtag='';
  hashtags=['#CopaBairro','#Gólaco','#FutAmador','#Juntos','#PartidaDoAno'];
  posts = [
    {id:1,av:'⚽',autor:'Copa Bairro 2024',tempo:'2h',tipo:'Torneio',texto:'Que partida! Unidos do Bairro venceu Real Esperança por 2×1 na Copa da Amizade. Rodada 4 foi de tirar o fôlego!',img:'Foto do jogo',tags:'#CopaBairro #Gólaco',curtidas:24,comentarios:18,atualizacao:''},
    {id:2,av:'🏃',autor:'Vila FC',tempo:'3h',tipo:'Time',texto:'Face total no próximo desafio! Paleta na bola!',img:'',tags:'#FutAmador',curtidas:15,comentarios:6,atualizacao:''},
    {id:3,av:'🏆',autor:'Liga Amigos',tempo:'5h',tipo:'Torneio',texto:'Atualização de partida: Unidos do Bairro 2 × 1 Real Esperança. N° Rodada 4 · Copa da Amizade.',img:'',tags:'',curtidas:9,comentarios:2,atualizacao:'Unidos do Bairro 2 × 1 Real Esperança · N° · Rodada 4 · Copa da Amizade'}
  ];
  feedFiltrado() {
    let f = this.posts;
    if (this.aba!=='todos') f = f.filter(p => p.tipo.toLowerCase()===this.aba.slice(0,-1));
    if (this.busca) f = f.filter(p => p.texto.toLowerCase().includes(this.busca.toLowerCase()));
    return f;
  }
  curtir(p: any) { p.curtidas++; }
  publicar() { if (!this.novoPost.trim()) return; this.posts.unshift({id:Date.now(),av:'👤',autor:'Lucas Lima',tempo:'agora',tipo:'Todos',texto:this.novoPost,img:'',tags:'',curtidas:0,comentarios:0,atualizacao:''}); this.novoPost=''; }
}
