import { Component, DestroyRef, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { forkJoin, interval } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService, UsuarioSessao } from '../core/auth.service';
import {
  ChatApiService,
  ConversaChat,
  GrupoChat,
  MensagemChat,
  PublicacaoCompartilhada,
  UsuarioChat
} from './chat-api.service';

type VisaoChat = 'inicio' | 'conversa' | 'explorar' | 'solicitacoes' | 'criar-grupo' | 'grupo';
type AbaSolicitacao = 'recebidas' | 'enviadas';

@Component({
  selector: 'app-chat-privado',
  imports: [FormsModule, RouterLink],
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
  grupos: GrupoChat[] = [];
  grupoAtivo: GrupoChat | null = null;
  publicacoesCompartilhadas: Record<number, PublicacaoCompartilhada> = {};
  publicacoesIndisponiveis = new Set<number>();
  private publicacoesCarregando = new Set<number>();
  nomeNovoGrupo = '';
  participantesNovoGrupo: number[] = [];

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
      , grupos: this.chatApi.listarGrupos()
    }).pipe(finalize(() => this.carregando = false)).subscribe({
      next: resultado => {
        this.inbox = resultado.inbox;
        this.solicitacoesRecebidas = resultado.recebidas;
        this.solicitacoesEnviadas = resultado.enviadas;
        this.grupos = resultado.grupos;
      },
      error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel carregar suas conversas.')
    });
  }

  abrirConversa(conversa: ConversaChat): void {
    this.erro = '';
    this.chatApi.consultarConversa(conversa.id).subscribe({
      next: conversaAtualizada => {
        this.conversaAtiva = conversaAtualizada;
        this.carregarPublicacoesCompartilhadas(conversaAtualizada.mensagens);
        this.visao = 'conversa';
        this.rolarParaFim();
      },
      error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel abrir a conversa.')
    });
  }

  mostrarInicio(): void {
    this.visao = 'inicio';
    this.conversaAtiva = null;
    this.grupoAtivo = null;
  }

  mostrarCriarGrupo(): void {
    this.visao = 'criar-grupo';
    this.conversaAtiva = null;
    this.grupoAtivo = null;
    this.nomeNovoGrupo = '';
    this.participantesNovoGrupo = [];
    if (!this.usuariosEncontrados.length) this.buscarUsuarios();
  }

  alternarParticipante(usuarioId: number): void {
    this.participantesNovoGrupo = this.participantesNovoGrupo.includes(usuarioId)
      ? this.participantesNovoGrupo.filter(id => id !== usuarioId)
      : [...this.participantesNovoGrupo, usuarioId];
  }

  criarGrupo(): void {
    if (!this.nomeNovoGrupo.trim() || !this.participantesNovoGrupo.length) return;
    this.enviando = true;
    this.chatApi.criarGrupo(this.nomeNovoGrupo.trim(), this.participantesNovoGrupo)
      .pipe(finalize(() => this.enviando = false))
      .subscribe({
        next: grupo => {
          this.grupos = [grupo, ...this.grupos];
          this.grupoAtivo = grupo;
          this.visao = 'grupo';
          this.sucesso = grupo.convitesPendentes.length
            ? `Grupo criado. ${grupo.convitesPendentes.length} pessoa(s) receberam convite.`
            : 'Grupo criado com todos os participantes liberados.';
        },
        error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel criar o grupo.')
      });
  }

  abrirGrupo(grupo: GrupoChat): void {
    if (this.convitePendenteParaMim(grupo)) {
      this.visao = 'solicitacoes';
      return;
    }
    this.chatApi.consultarGrupo(grupo.id).subscribe({
      next: atualizado => {
        this.grupoAtivo = atualizado;
        this.carregarPublicacoesCompartilhadas(atualizado.mensagens);
        this.conversaAtiva = null;
        this.visao = 'grupo';
        this.rolarParaFim();
      },
      error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel abrir o grupo.')
    });
  }

  aceitarConviteGrupo(grupo: GrupoChat): void {
    this.chatApi.aceitarGrupo(grupo.id).subscribe({
      next: atualizado => {
        this.substituirGrupo(atualizado);
        this.sucesso = `Voce entrou em ${atualizado.nome}.`;
        this.abrirGrupo(atualizado);
      },
      error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel aceitar o convite.')
    });
  }

  recusarConviteGrupo(grupo: GrupoChat): void {
    this.chatApi.recusarGrupo(grupo.id).subscribe({
      next: () => {
        this.grupos = this.grupos.filter(item => item.id !== grupo.id);
        this.sucesso = 'Convite de grupo recusado.';
      },
      error: erro => this.erro = this.mensagemErro(erro, 'Nao foi possivel recusar o convite.')
    });
  }

  convitePendenteParaMim(grupo: GrupoChat): boolean {
    return grupo.convitesPendentes.some(pessoa => pessoa.id === this.usuario.id);
  }

  get convitesGrupo(): GrupoChat[] {
    return this.grupos.filter(grupo => this.convitePendenteParaMim(grupo));
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
    if (!conteudo || (!this.conversaAtiva && !this.grupoAtivo) || this.enviando) return;

    this.enviando = true;
    this.erro = '';
    const requisicao = this.grupoAtivo
      ? this.chatApi.enviarMensagemGrupo(this.grupoAtivo.id, conteudo)
      : this.chatApi.enviarMensagem(this.conversaAtiva!.id, conteudo);
    requisicao
      .pipe(finalize(() => this.enviando = false))
      .subscribe({
        next: mensagem => {
          this.novaMensagem = '';
          if (this.grupoAtivo) {
            this.grupoAtivo = {
              ...this.grupoAtivo,
              ultimaAtividadeEm: mensagem.enviadaEm,
              mensagens: [...this.grupoAtivo.mensagens, mensagem]
            };
          } else {
            this.adicionarMensagem(mensagem);
          }
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

  tipoContaLabel(tipo: string): string {
    const labels: Record<string, string> = {
      USUARIO_COMUM: 'Usuário',
      JOGADOR: 'Jogador',
      ORGANIZADOR: 'Organizador',
      TREINADOR: 'Técnico / treinador',
      AUXILIAR_TECNICO: 'Auxiliar técnico',
      PREPARADOR_FISICO: 'Preparador físico',
      MEDICO: 'Médico'
    };
    return labels[tipo] ?? 'Usuário';
  }

  ultimaMensagem(conversa: ConversaChat): string {
    const mensagens = conversa.mensagens;
    if (!mensagens.length) return 'Conversa liberada. Envie a primeira mensagem.';
    const ultima = mensagens[mensagens.length - 1];
    return `${ultima.autorId === this.usuario.id ? 'Voce: ' : ''}${this.resumoMensagem(ultima)}`;
  }

  resumoMensagem(mensagem: MensagemChat): string {
    return this.idPublicacaoCompartilhada(mensagem.conteudo)
      ? 'Publicacao compartilhada'
      : mensagem.conteudo;
  }

  publicacaoDaMensagem(mensagem: MensagemChat): PublicacaoCompartilhada | undefined {
    const publicacaoId = this.idPublicacaoCompartilhada(mensagem.conteudo);
    return publicacaoId ? this.publicacoesCompartilhadas[publicacaoId] : undefined;
  }

  publicacaoIndisponivel(mensagem: MensagemChat): boolean {
    const publicacaoId = this.idPublicacaoCompartilhada(mensagem.conteudo);
    return !!publicacaoId && this.publicacoesIndisponiveis.has(publicacaoId);
  }

  ehVideo(midia: string): boolean {
    return midia.startsWith('data:video/')
      || /\.(mp4|webm|ogg)(\?.*)?$/i.test(midia);
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
    this.chatApi.listarGrupos().subscribe({
      next: grupos => this.grupos = grupos
    });
    if (this.conversaAtiva) {
      this.chatApi.consultarConversa(this.conversaAtiva.id).subscribe({
        next: conversa => {
          const recebeuNovaMensagem = conversa.mensagens.length !== this.conversaAtiva?.mensagens.length;
          this.conversaAtiva = conversa;
          this.carregarPublicacoesCompartilhadas(conversa.mensagens);
          if (recebeuNovaMensagem) this.rolarParaFim();
        }
      });
    }
    if (this.grupoAtivo) {
      this.chatApi.consultarGrupo(this.grupoAtivo.id).subscribe({
        next: grupo => {
          const recebeuNovaMensagem = grupo.mensagens.length !== this.grupoAtivo?.mensagens.length;
          this.grupoAtivo = grupo;
          this.carregarPublicacoesCompartilhadas(grupo.mensagens);
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

  private substituirGrupo(grupo: GrupoChat): void {
    this.grupos = this.grupos.map(item => item.id === grupo.id ? grupo : item);
  }

  private idPublicacaoCompartilhada(conteudo: string): number | undefined {
    const correspondencia = conteudo.match(/^\[\[PUBLICACAO_FEED:(\d+)]]$/);
    if (!correspondencia) return undefined;
    const id = Number(correspondencia[1]);
    return Number.isFinite(id) ? id : undefined;
  }

  private carregarPublicacoesCompartilhadas(mensagens: MensagemChat[]): void {
    mensagens.forEach(mensagem => {
      const publicacaoId = this.idPublicacaoCompartilhada(mensagem.conteudo);
      if (!publicacaoId
          || this.publicacoesCompartilhadas[publicacaoId]
          || this.publicacoesIndisponiveis.has(publicacaoId)
          || this.publicacoesCarregando.has(publicacaoId)) return;
      this.publicacoesCarregando.add(publicacaoId);
      this.chatApi.buscarPublicacao(publicacaoId).subscribe({
        next: publicacao => {
          this.publicacoesCompartilhadas[publicacaoId] = publicacao;
          this.publicacoesCarregando.delete(publicacaoId);
        },
        error: () => {
          this.publicacoesIndisponiveis.add(publicacaoId);
          this.publicacoesCarregando.delete(publicacaoId);
        }
      });
    });
  }

  private mensagemErro(erro: any, padrao: string): string {
    return erro?.error?.mensagem || padrao;
  }
}
