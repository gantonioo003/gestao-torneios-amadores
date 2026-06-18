import { AfterViewInit, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from '../core/auth.service';

declare global {
  interface Window {
    google?: any;
  }
}

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login implements AfterViewInit {
  aba: 'entrar' | 'criar' = 'entrar';
  email = '';
  senha = '';
  confirmarSenha = '';
  nome = '';
  nomeUsuario = '';
  telefone = '';
  dataNascimento = '';
  cidade = '';
  estado = '';
  biografia = '';
  tipo = 'JOGADOR';
  carregando = false;
  erro = '';
  avisoChat = false;
  googleHabilitado = false;
  configuracaoCarregada = false;

  readonly tiposConta = [
    { valor: 'JOGADOR', nome: 'Jogador', descricao: 'Tenha sua página, acompanhe times e oportunidades.' },
    { valor: 'TREINADOR', nome: 'Técnico / treinador', descricao: 'Crie e administre times, elenco e comissão.' },
    { valor: 'AUXILIAR_TECNICO', nome: 'Auxiliar técnico', descricao: 'Ajude na gestão de times e profissionais.' },
    { valor: 'PREPARADOR_FISICO', nome: 'Preparador físico', descricao: 'Mantenha seu perfil profissional na plataforma.' },
    { valor: 'MEDICO', nome: 'Médico', descricao: 'Participe como profissional da comissão técnica.' },
    { valor: 'ORGANIZADOR', nome: 'Organizador', descricao: 'Crie torneios e também possa administrar times.' }
  ];

  constructor(
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly auth: AuthService
  ) {
    this.avisoChat = this.route.snapshot.queryParamMap.get('acesso') === 'chat';
  }

  ngAfterViewInit() {
    this.auth.configuracaoAutenticacao().subscribe({
      next: configuracao => {
        this.configuracaoCarregada = true;
        this.googleHabilitado = configuracao.googleHabilitado;
        if (configuracao.googleHabilitado) {
          this.carregarGoogle(configuracao.googleClientId);
        }
      },
      error: () => this.configuracaoCarregada = true
    });
  }

  trocarAba(aba: 'entrar' | 'criar') {
    this.aba = aba;
    this.erro = '';
    setTimeout(() => this.renderizarBotaoGoogle());
  }

  get descricaoTipoSelecionado(): string {
    return this.tiposConta.find(item => item.valor === this.tipo)?.descricao ?? '';
  }

  entrar() {
    if (!this.email.trim() || !this.senha) {
      this.erro = 'Informe e-mail e senha para entrar.';
      return;
    }
    this.executar(
      this.auth.entrar(this.email, this.senha),
      'Nao foi possivel entrar. Confira seu e-mail e sua senha.'
    );
  }

  criar() {
    if (!this.nome.trim() || !this.nomeUsuario.trim() || !this.email.trim()) {
      this.erro = 'Informe nome, nome de usuário e e-mail.';
      return;
    }
    if (this.senha.length < 6 || this.senha !== this.confirmarSenha) {
      this.erro = 'Use uma senha com pelo menos 6 caracteres e confirme a mesma senha.';
      return;
    }
    this.executar(
      this.auth.criarConta({
        nome: this.nome,
        nomeUsuario: this.nomeUsuario.replace(/^@/, ''),
        email: this.email,
        telefone: this.telefone || undefined,
        dataNascimento: this.dataNascimento || undefined,
        cidade: this.cidade || undefined,
        estado: this.estado || undefined,
        biografia: this.biografia || undefined,
        senha: this.senha,
        tipo: this.tipo
      }),
      'Nao foi possivel criar a conta.'
    );
  }

  private carregarGoogle(clientId: string) {
    const inicializar = () => {
      window.google?.accounts.id.initialize({
        client_id: clientId,
        callback: (resposta: { credential: string }) => this.entrarComGoogle(resposta.credential)
      });
      this.renderizarBotaoGoogle();
    };

    if (window.google?.accounts?.id) {
      inicializar();
      return;
    }

    const existente = document.querySelector<HTMLScriptElement>('script[data-google-identity]');
    if (existente) {
      existente.addEventListener('load', inicializar, { once: true });
      return;
    }

    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    script.defer = true;
    script.dataset['googleIdentity'] = 'true';
    script.addEventListener('load', inicializar, { once: true });
    document.head.appendChild(script);
  }

  private renderizarBotaoGoogle() {
    const recipiente = document.getElementById('google-signin');
    if (!recipiente || !window.google?.accounts?.id) return;
    recipiente.innerHTML = '';
    window.google.accounts.id.renderButton(recipiente, {
      type: 'standard',
      theme: 'outline',
      size: 'large',
      shape: 'rectangular',
      width: Math.min(520, recipiente.clientWidth || 520),
      text: this.aba === 'criar' ? 'signup_with' : 'signin_with',
      locale: 'pt-BR'
    });
  }

  private entrarComGoogle(credencial: string) {
    if (this.aba === 'criar' && !this.nomeUsuario.trim()) {
      this.erro = 'Escolha primeiro um nome de usuário para criar a conta com Google.';
      return;
    }
    this.executar(
      this.auth.entrarGoogle(
        credencial,
        this.aba === 'criar' ? this.tipo : undefined,
        this.aba === 'criar' ? this.nomeUsuario.replace(/^@/, '') : undefined
      ),
      'Nao foi possivel entrar com o Google.'
    );
  }

  private executar(requisicao: any, mensagemPadrao: string) {
    this.carregando = true;
    this.erro = '';
    requisicao.pipe(finalize(() => this.carregando = false)).subscribe({
      next: () => {
        const destino = this.route.snapshot.queryParamMap.get('returnUrl') || '/home-logada';
        this.router.navigateByUrl(destino);
      },
      error: (erro: any) => {
        this.erro = erro?.error?.mensagem || erro?.error?.detail || mensagemPadrao;
      }
    });
  }
}
