import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-chat-privado',
  imports: [FormsModule],
  templateUrl: './chat-privado.html'
})
export class ChatPrivado {
  aba: 'descobrir' | 'solicitacoes' | 'inbox' | 'conversa' = 'descobrir';
  novaMensagem = '';

  perfis = [
    { iniciais: 'MC', nome: 'Marina Costa', papel: 'Organizadora da Copa Bairro 2024', status: 'Conversa liberada', destaque: true },
    { iniciais: 'PA', nome: 'Pedro Alves', papel: 'Responsavel pelo Resenha FC', status: 'Solicitacao pendente', destaque: false },
    { iniciais: 'JN', nome: 'Julia Nunes', papel: 'Jogadora da plataforma', status: 'Solicitacao pendente', destaque: false }
  ];

  mensagens = [
    { autor: 'Marina Costa', texto: 'Oi, Lucas. Vi seu time inscrito no torneio.', minha: false, hora: 'Ontem, 18:40' },
    { autor: 'Lucas Lima', texto: 'Perfeito. Quero acompanhar o processo por aqui.', minha: true, hora: 'Ontem, 18:42' }
  ];

  enviar() {
    if (!this.novaMensagem.trim()) return;
    this.mensagens.push({ autor: 'Lucas Lima', texto: this.novaMensagem, minha: true, hora: 'Agora' });
    this.novaMensagem = '';
  }
}
