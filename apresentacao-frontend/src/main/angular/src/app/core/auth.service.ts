import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { catchError, finalize, of, tap } from 'rxjs';

export interface UsuarioSessao {
  id: number;
  nome: string;
  nomeUsuario: string;
  email: string;
  telefone?: string;
  dataNascimento?: string;
  cidade?: string;
  estado?: string;
  biografia?: string;
  fotoPerfilUrl?: string;
  tipo: string;
  provedor: string;
  podeCriarTorneio: boolean;
  podeGerenciarTimes: boolean;
  possuiPerfilProfissional: boolean;
  torneiosSalvos: number[];
}

export interface CadastroConta {
  nome: string;
  nomeUsuario: string;
  email: string;
  telefone?: string;
  dataNascimento?: string;
  cidade?: string;
  estado?: string;
  biografia?: string;
  senha: string;
  tipo: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly chaveSessao = 'liga-amadora.usuario';
  private readonly usuarioSignal = signal<UsuarioSessao | null>(this.lerSessao());

  readonly usuario = this.usuarioSignal.asReadonly();

  constructor(private readonly http: HttpClient) {}

  entrar(email: string, senha: string) {
    return this.http.post<UsuarioSessao>('/backend/conta-usuario/autenticar', { email, senha }).pipe(
      tap(usuario => this.salvarSessao(usuario))
    );
  }

  criarConta(dados: CadastroConta) {
    return this.http.post<UsuarioSessao>('/backend/conta-usuario/salvar', dados).pipe(
      tap(usuario => this.salvarSessao(usuario))
    );
  }

  entrarGoogle(credencial: string, tipo?: string, nomeUsuario?: string) {
    return this.http.post<UsuarioSessao>('/backend/conta-usuario/autenticar/google', {
      credencial,
      tipo,
      nomeUsuario
    }).pipe(tap(usuario => this.salvarSessao(usuario)));
  }

  atualizarConta(dados: Partial<UsuarioSessao>) {
    const usuario = this.usuarioSignal();
    if (!usuario) {
      throw new Error('Nenhum usuario autenticado.');
    }
    return this.http.post<UsuarioSessao>(`/backend/conta-usuario/${usuario.id}/salvar`, {
      ...usuario,
      ...dados
    }).pipe(tap(contaAtualizada => this.salvarSessao(contaAtualizada)));
  }

  alterarSenha(senhaAtual: string, novaSenha: string) {
    const usuario = this.usuarioSignal();
    if (!usuario) {
      throw new Error('Nenhum usuario autenticado.');
    }
    return this.http.post<void>(`/backend/conta-usuario/${usuario.id}/alterar-senha`, {
      senhaAtual,
      novaSenha
    });
  }

  salvarTorneio(torneioId: number) {
    return this.http.post<UsuarioSessao>(
      `/backend/conta-usuario/torneios-salvos/${torneioId}/salvar`,
      {}
    ).pipe(tap(usuario => this.salvarSessao(usuario)));
  }

  removerTorneioSalvo(torneioId: number) {
    return this.http.post<UsuarioSessao>(
      `/backend/conta-usuario/torneios-salvos/${torneioId}/remover`,
      {}
    ).pipe(tap(usuario => this.salvarSessao(usuario)));
  }

  excluirConta() {
    const usuario = this.usuarioSignal();
    if (!usuario) {
      throw new Error('Nenhum usuario autenticado.');
    }
    return this.http.post<void>(`/backend/conta-usuario/${usuario.id}/excluir`, {}).pipe(
      finalize(() => this.limparSessao())
    );
  }

  configuracaoAutenticacao() {
    return this.http.get<{ googleHabilitado: boolean; googleClientId: string }>(
      '/backend/conta-usuario/configuracao-autenticacao'
    );
  }

  estaAutenticado(): boolean {
    return this.usuarioSignal() !== null;
  }

  podeCriarTorneio(): boolean {
    return this.usuarioSignal()?.podeCriarTorneio === true;
  }

  podeGerenciarTimes(): boolean {
    return this.usuarioSignal()?.podeGerenciarTimes === true;
  }

  validarSessao() {
    return this.http.get<UsuarioSessao>('/backend/conta-usuario/sessao').pipe(
      tap(usuario => this.salvarSessao(usuario)),
      catchError(() => {
        this.limparSessao();
        return of(null);
      })
    );
  }

  sair() {
    return this.http.post<void>('/backend/conta-usuario/sair', null).pipe(
      catchError(() => of(void 0)),
      finalize(() => this.limparSessao())
    );
  }

  private salvarSessao(usuario: UsuarioSessao): void {
    const normalizado = {
      ...usuario,
      torneiosSalvos: usuario.torneiosSalvos ?? []
    };
    localStorage.setItem(this.chaveSessao, JSON.stringify(normalizado));
    this.usuarioSignal.set(normalizado);
  }

  private lerSessao(): UsuarioSessao | null {
    try {
      const sessao = localStorage.getItem(this.chaveSessao);
      if (!sessao) return null;
      const usuario = JSON.parse(sessao) as UsuarioSessao;
      return { ...usuario, torneiosSalvos: usuario.torneiosSalvos ?? [] };
    } catch {
      localStorage.removeItem(this.chaveSessao);
      return null;
    }
  }

  private limparSessao(): void {
    localStorage.removeItem(this.chaveSessao);
    this.usuarioSignal.set(null);
  }
}
