import { Component, DestroyRef, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin, interval } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService, UsuarioSessao } from '../core/auth.service';
import {
  ChatApiService,
  ConversaChat,
  MensagemChat,
  UsuarioChat
} from './chat-api.service';

type VisaoChat = 'inicio' | 'conversa' | 'explorar' | 'solicitacoes';
type AbaSolicitacao = 'recebidas' | 'enviadas';

@Component({
  selector: 'app-chat-privado',
  imports: [FormsModule],
  templateUrl: './chat-privado.html',
  styleUrl: './chat-privado.css'
})
export class ChatPrivado implements OnInit {
  @ViewChild('mensagensArea') mensagensArea?: ElementRef<HTMLElement>;

  private readonly destroyRef = inject(DestroyRef);
  private temporizadorBusca?: ReturnType<typeof setTimeout>;

  readonly usuario: UsuarioSessao;

  visao: VisaoChat = 'inicio';
  abaSolicitacao: AbaSolicitacao = 'recebidas';
  inbox: ConversaChat[] = [];
  solicitacoesRecebidas: ConversaChat[] = [];
  solicitacoesEnviadas: ConversaChat[] = [];
  usuariosEncontrados: UsuarioChat[] = [];
  conversaAtiva: ConversaChat | null = null;

  filtroInbox = '';
  termoBusca = '';
  novaMensagem = '';
  carregando = true;
  buscando = false;
  enviando = false;
  erro = '';
  sucesso = '';

  constructor(
    private readonly chatApi: ChatApiService,
    private readonly auth: AuthService
  ) {
    this.usuario = this.auth.usuario()!;
  }

  ngOnInit(): void {
    this.carregarPainel();
    interval(4000)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.atualizarSilenciosamente());
  }

  get inboxFiltrado(): ConversaChat[] {
    const filtro = this.filtroInbox.trim().toLowerCase();
    if (!filtro) return this.inbox;
    return this.inbox.filter(conversa =>
      conversa.outroUsuarioNome.toLowerCase().includes(filtro)
      || conversa.outroUsuarioEmail.toLowerCase().includes(filtro)
    );
  }

  get totalSolicitacoes(): number {
    return this.solicitacoesRecebidas.length;
  }

  carregarPainel(): void {
    this.carregando = true;
    this.erro = '';
    forkJoin({
      inbox: this.chatApi.listarInbox(),
      recebidas: this.chatApi.listarRecebidas(),
      enviadas: this.chatApi.listarEnviadas()
    }).pipe(finalize(() => this.carregando = false)).subscribe({
      next: resultado => {
        this.inbox = resultado.inbox;
        this.solicitacoesRecebidas = resultado.recebidas;
        this.solicitacoesEnviadas = resultado.enviadas;
      },
      error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel carregar suas conversas.')
    });
  }

  abrirConversa(conversa: ConversaChat): void {
    this.erro = '';
    this.chatApi.consultarConversa(conversa.id).subscribe({
      next: conversaAtualizada => {
        this.conversaAtiva = conversaAtualizada;
        this.visao = 'conversa';
        this.rolarParaFim();
      },
      error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel abrir a conversa.')
    });
  }

  mostrarInicio(): void {
    this.visao = 'inicio';
    this.conversaAtiva = null;
  }

  mostrarExplorar(): void {
    this.visao = 'explorar';
    this.conversaAtiva = null;
    if (this.usuariosEncontrados.length === 0) {
      this.buscarUsuarios();
    }
  }

  mostrarSolicitacoes(): void {
    this.visao = 'solicitacoes';
    this.conversaAtiva = null;
  }

  agendarBusca(): void {
    clearTimeout(this.temporizadorBusca);
    this.temporizadorBusca = setTimeout(() => this.buscarUsuarios(), 280);
  }

  buscarUsuarios(): void {
    this.buscando = true;
    this.erro = '';
    this.chatApi.pesquisarUsuarios(this.termoBusca)
      .pipe(finalize(() => this.buscando = false))
      .subscribe({
        next: usuarios => this.usuariosEncontrados = usuarios,
        error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel pesquisar as contas.')
      });
  }

  solicitarConversa(destinatario: UsuarioChat): void {
    this.limparAvisos();
    this.chatApi.solicitarConversa(destinatario.id).subscribe({
      next: conversa => {
        this.solicitacoesEnviadas = [conversa, ...this.solicitacoesEnviadas];
        this.sucesso = `Solicitacao enviada para ${destinatario.nome}.`;
      },
      error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel enviar a solicitacao.')
    });
  }

  aprovar(conversa: ConversaChat): void {
    this.limparAvisos();
    this.chatApi.aprovar(conversa.id).subscribe({
      next: aprovada => {
        this.solicitacoesRecebidas = this.solicitacoesRecebidas.filter(item => item.id !== conversa.id);
        this.inbox = [aprovada, ...this.inbox];
        this.sucesso = `Conversa com ${aprovada.outroUsuarioNome} liberada.`;
        this.abrirConversa(aprovada);
      },
      error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel aprovar a solicitacao.')
    });
  }

  recusar(conversa: ConversaChat): void {
    this.limparAvisos();
    this.chatApi.recusar(conversa.id).subscribe({
      next: () => {
        this.solicitacoesRecebidas = this.solicitacoesRecebidas.filter(item => item.id !== conversa.id);
        this.sucesso = 'Solicitacao recusada.';
      },
      error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel recusar a solicitacao.')
    });
  }

  enviarMensagem(): void {
    const conteudo = this.novaMensagem.trim();
    if (!conteudo || !this.conversaAtiva || this.enviando) return;

    this.enviando = true;
    this.erro = '';
    this.chatApi.enviarMensagem(this.conversaAtiva.id, conteudo)
      .pipe(finalize(() => this.enviando = false))
      .subscribe({
        next: mensagem => {
          this.novaMensagem = '';
          this.adicionarMensagem(mensagem);
          this.rolarParaFim();
        },
        error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel enviar a mensagem.')
      });
  }

  aoPressionarEnter(evento: Event): void {
    const eventoTeclado = evento as KeyboardEvent;
    if (eventoTeclado.shiftKey) return;
    eventoTeclado.preventDefault();
    this.enviarMensagem();
  }

  jaPossuiContato(usuarioId: number): boolean {
    return this.inbox.some(item => item.outroUsuarioId === usuarioId);
  }

  solicitacaoPendente(usuarioId: number): boolean {
    return this.solicitacoesEnviadas.some(item => item.outroUsuarioId === usuarioId)
      || this.solicitacoesRecebidas.some(item => item.outroUsuarioId === usuarioId);
  }

  iniciais(nome: string): string {
    return nome.split(/\s+/).filter(Boolean).slice(0, 2)
      .map(parte => parte[0].toUpperCase()).join('');
  }

  ultimaMensagem(conversa: ConversaChat): string {
    const mensagens = conversa.mensagens;
    if (!mensagens.length) return 'Conversa liberada. Envie a primeira mensagem.';
    const ultima = mensagens[mensagens.length - 1];
    return `${ultima.autorId === this.usuario.id ? 'Voce: ' : ''}${ultima.conteudo}`;
  }

  formatarHora(dataIso: string): string {
    if (!dataIso) return '';
    const data = new Date(dataIso);
    const hoje = new Date();
    if (data.toDateString() === hoje.toDateString()) {
      return data.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    }
    return data.toLocaleDateString('pt-BR', { day: '2-digit', month: 'short' });
  }

  private atualizarSilenciosamente(): void {
    this.chatApi.listarInbox().subscribe({
      next: inbox => this.inbox = inbox
    });
    this.chatApi.listarRecebidas().subscribe({
      next: recebidas => this.solicitacoesRecebidas = recebidas
    });
    if (this.conversaAtiva) {
      this.chatApi.consultarConversa(this.conversaAtiva.id).subscribe({
        next: conversa => {
          const recebeuNovaMensagem = conversa.mensagens.length !== this.conversaAtiva?.mensagens.length;
          this.conversaAtiva = conversa;
          if (recebeuNovaMensagem) this.rolarParaFim();
        }
      });
    }
  }

  private adicionarMensagem(mensagem: MensagemChat): void {
    if (!this.conversaAtiva) return;
    this.conversaAtiva = {
      ...this.conversaAtiva,
      ultimaAtividadeEm: mensagem.enviadaEm,
      mensagens: [...this.conversaAtiva.mensagens, mensagem]
    };
  }

  private rolarParaFim(): void {
    setTimeout(() => {
      const elemento = this.mensagensArea?.nativeElement;
      if (elemento) elemento.scrollTop = elemento.scrollHeight;
    });
  }

  private limparAvisos(): void {
    this.erro = '';
    this.sucesso = '';
  }

  private mensagemErro(erro: any, padrao: string): string {
    return erro?.error?.mensagem || padrao;
  }
}
