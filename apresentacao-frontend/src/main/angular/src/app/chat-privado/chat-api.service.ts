import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface UsuarioChat {
  id: number;
  nome: string;
  email: string;
  tipo: string;
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
  status: 'SOLICITADA' | 'APROVADA' | 'RECUSADA';
  solicitadaEm: string;
  ultimaAtividadeEm: string;
  mensagens: MensagemChat[];
}

@Injectable({ providedIn: 'root' })
export class ChatApiService {
  private readonly baseUrl = '/backend/chat-privado';

  constructor(private readonly http: HttpClient) {}

  pesquisarUsuarios(termo: string) {
    const params = new HttpParams().set('termo', termo);
    return this.http.get<UsuarioChat[]>(`${this.baseUrl}/usuarios`, { params });
  }

  solicitarConversa(destinatarioId: number) {
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
}
