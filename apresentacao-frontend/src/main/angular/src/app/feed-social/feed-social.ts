import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { catchError, forkJoin, of } from 'rxjs';
import { AuthService } from '../core/auth.service';
import { ChatApiService, ConversaChat, GrupoChat } from '../chat-privado/chat-api.service';

interface IdentidadeFeed {
  tipo: 'USUARIO' | 'TIME' | 'TORNEIO';
  id: number;
  nome: string;
  descricao: string;
}

interface ComentarioFeed {
  id: number;
  autorId: number;
  autorNome: string;
  autorNomeUsuario: string;
  autorFotoPerfilUrl?: string;
  conteudo: string;
  midias: string[];
  criadaEm: string;
}

interface PublicacaoFeed {
  id: number;
  torneioId?: number;
  tipo: string;
  autorId?: number;
  autorNome: string;
  autorNomeUsuario: string;
  autorFotoPerfilUrl?: string;
  tipoIdentidade: 'USUARIO' | 'TIME' | 'TORNEIO' | 'SISTEMA';
  identidadeId?: number;
  identidadeNome: string;
  partidaId?: number;
  conteudo: string;
  hashtags: string[];
  midias: string[];
  quantidadeCurtidas: number;
  curtidaPeloUsuario: boolean;
  reacoes: Record<string, string>;
  criadaEm: string;
  comentarios: ComentarioFeed[];
}

interface AssuntoMomento {
  hashtag: string;
  pontuacao: number;
}

type AbaFeed = 'todos' | 'torneios' | 'partidas' | 'times';

@Component({
  selector: 'app-feed-social',
  imports: [FormsModule, RouterLink],
  templateUrl: './feed-social.html',
  styleUrl: './feed-social.css'
})
export class FeedSocial implements OnInit {
  aba: AbaFeed = 'todos';
  busca = '';
  novoPost = '';
  midiasPreview: string[] = [];
  identidadeSelecionada = '';
  identidades: IdentidadeFeed[] = [];
  posts: PublicacaoFeed[] = [];
  assuntos: AssuntoMomento[] = [];
  comentariosAbertos = new Set<number>();
  comentarioPorPost: Record<number, string> = {};
  midiaComentarioPorPost: Record<number, string> = {};
  publicando = false;
  carregando = true;
  mensagem = '';
  compartilhandoPost: PublicacaoFeed | null = null;
  conversasCompartilhamento: ConversaChat[] = [];
  gruposCompartilhamento: GrupoChat[] = [];
  carregandoDestinos = false;
  enviandoPara = '';
  publicacaoSelecionadaId?: number;
  readonly usuario = this.auth.usuario;

  constructor(
    private readonly http: HttpClient,
    private readonly auth: AuthService,
    private readonly chatApi: ChatApiService,
    private readonly route: ActivatedRoute
  ) {}

  ngOnInit() {
    const publicacao = Number(this.route.snapshot.queryParamMap.get('publicacao'));
    this.publicacaoSelecionadaId = Number.isFinite(publicacao) && publicacao > 0 ? publicacao : undefined;
    forkJoin({
      posts: this.http.get<PublicacaoFeed[]>('/backend/feed/geral').pipe(catchError(() => of([]))),
      identidades: this.http.get<IdentidadeFeed[]>('/backend/feed/identidades').pipe(catchError(() => of([]))),
      assuntos: this.http.get<AssuntoMomento[]>('/backend/feed/assuntos').pipe(catchError(() => of([])))
    }).subscribe(({ posts, identidades, assuntos }) => {
      this.posts = posts;
      this.identidades = identidades;
      this.assuntos = assuntos;
      const pessoal = identidades.find(item => item.tipo === 'USUARIO');
      this.identidadeSelecionada = pessoal ? this.chaveIdentidade(pessoal) : '';
      this.carregando = false;
      this.destacarPublicacaoSelecionada();
    });
  }

  get feedFiltrado(): PublicacaoFeed[] {
    const termo = this.busca.trim().toLowerCase();
    return this.posts.filter(post => {
      const correspondeAba = this.aba === 'todos'
        || (this.aba === 'torneios' && post.tipoIdentidade === 'TORNEIO')
        || (this.aba === 'times' && post.tipoIdentidade === 'TIME')
        || (this.aba === 'partidas' && (!!post.partidaId || post.tipoIdentidade === 'SISTEMA'));
      const correspondeBusca = !termo
        || post.conteudo.toLowerCase().includes(termo)
        || post.identidadeNome.toLowerCase().includes(termo)
        || post.hashtags.some(tag => tag.includes(termo.replace('#', '')));
      return correspondeAba && correspondeBusca;
    });
  }

  get radarComunidade(): PublicacaoFeed[] {
    const identidades = new Set<string>();
    return this.posts.filter(post => {
      if (post.tipoIdentidade === 'USUARIO') return false;
      const chave = `${post.tipoIdentidade}:${post.identidadeId}`;
      if (identidades.has(chave)) return false;
      identidades.add(chave);
      return true;
    }).slice(0, 4);
  }

  publicar() {
    const identidade = this.identidadeAtual();
    if (!identidade || (!this.novoPost.trim() && !this.midiasPreview.length) || this.publicando) return;
    this.publicando = true;
    this.http.post<PublicacaoFeed>('/backend/feed/publicar-social', {
      tipoIdentidade: identidade.tipo,
      identidadeId: identidade.tipo === 'USUARIO' ? null : identidade.id,
      conteudo: this.novoPost.trim(),
      hashtags: this.extrairHashtags(this.novoPost),
      midias: this.midiasPreview
    }).subscribe({
      next: post => {
        this.posts = [post, ...this.posts];
        this.novoPost = '';
        this.midiasPreview = [];
        this.publicando = false;
        this.atualizarAssuntos();
      },
      error: erro => {
        this.publicando = false;
        this.mensagem = erro?.error?.mensagem ?? 'Nao foi possivel publicar.';
      }
    });
  }

  selecionarFoto(evento: Event) {
    const input = evento.target as HTMLInputElement;
    const arquivos = Array.from(input.files ?? []);
    if (!arquivos.length) return;
    const vagas = Math.max(0, 4 - this.midiasPreview.length);
    const selecionados = arquivos.slice(0, vagas);
    const invalidos = selecionados.some(arquivo =>
      (!arquivo.type.startsWith('image/') && !arquivo.type.startsWith('video/'))
      || arquivo.size > 5_000_000);
    if (invalidos) {
      this.mensagem = 'Escolha fotos ou videos de ate 5 MB cada.';
      input.value = '';
      return;
    }
    selecionados.forEach(arquivo => {
      const leitor = new FileReader();
      leitor.onload = () => this.midiasPreview = [
        ...this.midiasPreview,
        String(leitor.result ?? '')
      ].slice(0, 4);
      leitor.readAsDataURL(arquivo);
    });
    if (arquivos.length > vagas) {
      this.mensagem = 'Cada publicacao pode ter ate 4 fotos ou videos.';
    }
    input.value = '';
  }

  removerMidia(indice: number) {
    this.midiasPreview = this.midiasPreview.filter((_, atual) => atual !== indice);
  }

  curtir(post: PublicacaoFeed) {
    this.http.post<PublicacaoFeed>(`/backend/feed/${post.id}/curtir`, null).subscribe({
      next: atualizado => this.substituirPost(atualizado),
      error: () => this.mensagem = 'Nao foi possivel atualizar a curtida.'
    });
  }

  reagir(post: PublicacaoFeed, tipoReacao: string) {
    this.http.post<PublicacaoFeed>(
      `/backend/feed/${post.id}/reagir?tipoReacao=${tipoReacao}`, null
    ).subscribe({
      next: atualizado => this.substituirPost(atualizado),
      error: () => this.mensagem = 'Nao foi possivel registrar a reacao.'
    });
  }

  comentar(post: PublicacaoFeed) {
    const conteudo = this.comentarioPorPost[post.id]?.trim();
    const midia = this.midiaComentarioPorPost[post.id];
    if (!conteudo && !midia) return;
    this.http.post<ComentarioFeed>(`/backend/feed/${post.id}/comentar`, {
      conteudo: conteudo ?? '',
      midias: midia ? [midia] : []
    }).subscribe({
      next: comentario => {
        post.comentarios = [...post.comentarios, comentario];
        this.comentarioPorPost[post.id] = '';
        this.midiaComentarioPorPost[post.id] = '';
        this.comentariosAbertos.add(post.id);
      },
      error: () => this.mensagem = 'Nao foi possivel publicar o comentario.'
    });
  }

  selecionarFotoComentario(postId: number, evento: Event) {
    const arquivo = (evento.target as HTMLInputElement).files?.[0];
    if (!arquivo) return;
    if (!arquivo.type.startsWith('image/') || arquivo.size > 2_000_000) {
      this.mensagem = 'Escolha uma imagem de ate 2 MB.';
      return;
    }
    const leitor = new FileReader();
    leitor.onload = () => this.midiaComentarioPorPost[postId] = String(leitor.result ?? '');
    leitor.readAsDataURL(arquivo);
  }

  alternarComentarios(postId: number) {
    if (this.comentariosAbertos.has(postId)) this.comentariosAbertos.delete(postId);
    else this.comentariosAbertos.add(postId);
  }

  podeComentar(postId: number): boolean {
    return !!this.comentarioPorPost[postId]?.trim() || !!this.midiaComentarioPorPost[postId];
  }

  abrirCompartilhamento(post: PublicacaoFeed) {
    this.compartilhandoPost = post;
    this.carregandoDestinos = true;
    forkJoin({
      conversas: this.chatApi.listarInbox().pipe(catchError(() => of([]))),
      grupos: this.chatApi.listarGrupos().pipe(catchError(() => of([])))
    }).subscribe(({ conversas, grupos }) => {
      this.conversasCompartilhamento = conversas;
      this.gruposCompartilhamento = grupos.filter(grupo =>
        !grupo.convitesPendentes.some(pessoa => pessoa.id === this.usuario()?.id));
      this.carregandoDestinos = false;
    });
  }

  fecharCompartilhamento() {
    this.compartilhandoPost = null;
    this.enviandoPara = '';
  }

  encaminharParaConversa(conversa: ConversaChat) {
    const post = this.compartilhandoPost;
    if (!post || this.enviandoPara) return;
    this.enviandoPara = `conversa:${conversa.id}`;
    this.chatApi.enviarMensagem(conversa.id, this.referenciaCompartilhamento(post.id)).subscribe({
      next: () => {
        this.mensagem = `Publicacao enviada para ${conversa.outroUsuarioNome}.`;
        this.fecharCompartilhamento();
      },
      error: () => {
        this.enviandoPara = '';
        this.mensagem = 'Nao foi possivel encaminhar a publicacao.';
      }
    });
  }

  encaminharParaGrupo(grupo: GrupoChat) {
    const post = this.compartilhandoPost;
    if (!post || this.enviandoPara) return;
    this.enviandoPara = `grupo:${grupo.id}`;
    this.chatApi.enviarMensagemGrupo(grupo.id, this.referenciaCompartilhamento(post.id)).subscribe({
      next: () => {
        this.mensagem = `Publicacao enviada para ${grupo.nome}.`;
        this.fecharCompartilhamento();
      },
      error: () => {
        this.enviandoPara = '';
        this.mensagem = 'Nao foi possivel encaminhar a publicacao.';
      }
    });
  }

  ehVideo(midia: string): boolean {
    return midia.startsWith('data:video/')
      || /\.(mp4|webm|ogg)(\?.*)?$/i.test(midia);
  }

  filtrarHashtag(hashtag: string) {
    this.aba = 'todos';
    this.busca = `#${hashtag}`;
  }

  abrirNoRadar(post: PublicacaoFeed) {
    this.aba = post.tipoIdentidade === 'TIME'
      ? 'times'
      : post.tipoIdentidade === 'TORNEIO' ? 'torneios' : 'partidas';
    this.busca = post.identidadeNome;
    document.querySelector('.feed-main')?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  denunciar(post: PublicacaoFeed) {
    const motivo = prompt('Por que esta publicacao deve ser revisada?');
    if (!motivo?.trim()) return;
    this.enviarDenuncia('PUBLICACAO', post.id, motivo.trim());
  }

  denunciarComentario(comentario: ComentarioFeed) {
    const motivo = prompt('Por que este comentario deve ser revisado?');
    if (!motivo?.trim()) return;
    this.enviarDenuncia('COMENTARIO', comentario.id, motivo.trim());
  }

  identidadeAtual(): IdentidadeFeed | undefined {
    return this.identidades.find(item => this.chaveIdentidade(item) === this.identidadeSelecionada);
  }

  chaveIdentidade(identidade: IdentidadeFeed): string {
    return `${identidade.tipo}:${identidade.id}`;
  }

  iniciais(nome: string): string {
    return nome.split(/\s+/).filter(Boolean).slice(0, 2).map(parte => parte[0]).join('').toUpperCase();
  }

  tempo(dataIso: string): string {
    const diferenca = Date.now() - new Date(dataIso).getTime();
    const minutos = Math.max(0, Math.floor(diferenca / 60000));
    if (minutos < 1) return 'agora';
    if (minutos < 60) return `${minutos}min`;
    const horas = Math.floor(minutos / 60);
    if (horas < 24) return `${horas}h`;
    return `${Math.floor(horas / 24)}d`;
  }

  totalReacoes(post: PublicacaoFeed): number {
    return Object.keys(post.reacoes ?? {}).length;
  }

  private substituirPost(atualizado: PublicacaoFeed) {
    this.posts = this.posts.map(post => post.id === atualizado.id ? atualizado : post);
  }

  private extrairHashtags(texto: string): string[] {
    return Array.from(texto.matchAll(/#([\p{L}\p{N}_]+)/gu)).map(resultado => resultado[1]);
  }

  private atualizarAssuntos() {
    this.http.get<AssuntoMomento[]>('/backend/feed/assuntos')
      .subscribe(assuntos => this.assuntos = assuntos);
  }

  private enviarDenuncia(tipoAlvo: string, alvoId: number, motivo: string) {
    this.http.post('/backend/moderacao/denuncias', {
      tipoAlvo,
      alvoId,
      motivo
    }).subscribe({
      next: () => this.mensagem = 'Denuncia enviada para analise.',
      error: erro => this.mensagem = erro?.error?.mensagem ?? 'Nao foi possivel enviar a denuncia.'
    });
  }

  private referenciaCompartilhamento(publicacaoId: number): string {
    return `[[PUBLICACAO_FEED:${publicacaoId}]]`;
  }

  private destacarPublicacaoSelecionada() {
    if (!this.publicacaoSelecionadaId) return;
    setTimeout(() => document.getElementById(`publicacao-${this.publicacaoSelecionadaId}`)
      ?.scrollIntoView({ behavior: 'smooth', block: 'center' }));
  }
}
