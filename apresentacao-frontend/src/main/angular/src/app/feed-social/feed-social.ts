import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-feed-social',
  imports: [FormsModule, RouterLink],
  templateUrl: './feed-social.html'
})
export class FeedSocial {
  aba = 'todos';
  busca = '';
  novoPost = '';

  posts = [
    { autor: 'Copa Bairro 2024', tempo: '2h', tipo: 'torneios', texto: 'Que partida! Unidos do Bairro venceu Real Esperanca por 2x1 na Copa da Amizade.', tags: '#CopaBairro #Golaco', curtidas: 24, comentarios: 18, imagem: true },
    { autor: 'Vila FC', tempo: '3h', tipo: 'times', texto: 'Foco total no proximo desafio. Nosso elenco esta fechado para a rodada.', tags: '#FutAmador', curtidas: 15, comentarios: 6, imagem: false },
    { autor: 'Liga Amigos', tempo: '5h', tipo: 'partidas', texto: 'Atualizacao automatica: Unidos do Bairro 2 x 1 Real Esperanca.', tags: '#Rodada4', curtidas: 9, comentarios: 2, imagem: false }
  ];

  feedFiltrado() {
    let resultado = this.posts;
    if (this.aba !== 'todos') resultado = resultado.filter(post => post.tipo === this.aba);
    if (this.busca.trim()) resultado = resultado.filter(post => post.texto.toLowerCase().includes(this.busca.toLowerCase()));
    return resultado;
  }

  curtir(post: any) {
    post.curtidas++;
  }

  publicar() {
    if (!this.novoPost.trim()) return;
    this.posts.unshift({
      autor: 'Lucas Lima',
      tempo: 'agora',
      tipo: 'times',
      texto: this.novoPost,
      tags: '#LigaAmadora',
      curtidas: 0,
      comentarios: 0,
      imagem: false
    });
    this.novoPost = '';
  }
}
