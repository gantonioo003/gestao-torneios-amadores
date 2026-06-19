import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface UsuarioChat {
  id: string;
  nome: string;
  email: string;
  tipo: string;
  fotoPerfilUrl?: string;
}

export interface MensagemChat {
  id: number;
  autorId: number;
  conteudo: string;
  enviadaEm: string;
}

export interface ConversaChat {
  id: number;
  solicitanteId: number;
  destinatarioId: number;
  outroUsuarioId: number;
  outroUsuarioNome: string;
  outroUsuarioEmail: string;
  outroUsuarioTipo: string;
  outroUsuarioFotoPerfilUrl?: string;
  status: 'SOLICITADA' | 'APROVADA' | 'RECUSADA';
  solicitadaEm: string;
  ultimaAtividadeEm: string;
  mensagens: MensagemChat[];
}

export interface ParticipanteGrupoChat {
  id: number;
  nome: string;
  email: string;
  fotoPerfilUrl?: string;
}

export interface GrupoChat {
  id: number;
  nome: string;
  criadorId: number;
  criadoEm: string;
  ultimaAtividadeEm: string;
  participantes: ParticipanteGrupoChat[];
  convitesPendentes: ParticipanteGrupoChat[];
  mensagens: MensagemChat[];
}

export interface PublicacaoCompartilhada {
  id: number;
  identidadeNome: string;
  tipoIdentidade: string;
  autorNomeUsuario: string;
  conteudo: string;
  midias: string[];
}

@Injectable({ providedIn: 'root' })
export class ChatApiService {
  private readonly baseUrl = '/backend/chat-privado';

  constructor(private readonly http: HttpClient) {}

  pesquisarUsuarios(termo: string) {
    const params = new HttpParams().set('termo', termo);
    return this.http.get<UsuarioChat[]>(`${this.baseUrl}/usuarios`, { params });
  }

  solicitarConversa(destinatarioId: string) {
    const params = new HttpParams().set('destinatarioId', destinatarioId);
    return this.http.post<ConversaChat>(`${this.baseUrl}/solicitar`, null, { params });
  }

  listarInbox() {
    return this.http.get<ConversaChat[]>(`${this.baseUrl}/inbox`);
  }

  listarRecebidas() {
    return this.http.get<ConversaChat[]>(`${this.baseUrl}/solicitacoes/recebidas`);
  }

  listarEnviadas() {
    return this.http.get<ConversaChat[]>(`${this.baseUrl}/solicitacoes/enviadas`);
  }

  consultarConversa(conversaId: number) {
    return this.http.get<ConversaChat>(`${this.baseUrl}/${conversaId}`);
  }

  aprovar(conversaId: number) {
    return this.http.post<ConversaChat>(`${this.baseUrl}/${conversaId}/aprovar`, null);
  }

  recusar(conversaId: number) {
    return this.http.post<ConversaChat>(`${this.baseUrl}/${conversaId}/recusar`, null);
  }

  enviarMensagem(conversaId: number, conteudo: string) {
    return this.http.post<MensagemChat>(`${this.baseUrl}/${conversaId}/mensagem`, {
      conteudo
    });
  }

  criarGrupo(nome: string, participantes: string[]) {
    return this.http.post<GrupoChat>(`${this.baseUrl}/grupos`, { nome, participantes });
  }

  listarGrupos() {
    return this.http.get<GrupoChat[]>(`${this.baseUrl}/grupos`);
  }

  consultarGrupo(grupoId: number) {
    return this.http.get<GrupoChat>(`${this.baseUrl}/grupos/${grupoId}`);
  }

  aceitarGrupo(grupoId: number) {
    return this.http.post<GrupoChat>(`${this.baseUrl}/grupos/${grupoId}/aceitar`, null);
  }

  recusarGrupo(grupoId: number) {
    return this.http.post<GrupoChat>(`${this.baseUrl}/grupos/${grupoId}/recusar`, null);
  }

  enviarMensagemGrupo(grupoId: number, conteudo: string) {
    return this.http.post<MensagemChat>(`${this.baseUrl}/grupos/${grupoId}/mensagem`, { conteudo });
  }

  buscarPublicacao(publicacaoId: number) {
    return this.http.get<PublicacaoCompartilhada>(`/backend/feed/${publicacaoId}`);
  }
}
