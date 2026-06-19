import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { catchError, finalize, of } from 'rxjs';
import { AuthService } from '../core/auth.service';

type SecaoConta = 'visao-geral' | 'perfil' | 'configuracoes';
type CategoriaConfiguracao = 'conta' | 'privacidade' | 'notificacoes' | 'seguranca' | 'atividade' | 'ajuda';

interface ContaAtividade {
  torneiosOrganizados: any[];
  torneiosParticipando: any[];
  torneiosSalvos: any[];
  palpites: Array<{ apurado: boolean }>;
}

@Component({
  selector: 'app-home-logada',
  imports: [FormsModule, RouterLink],
  templateUrl: './home-logada.html',
  styleUrl: './home-logada.css'
})
export class HomeLogada implements OnInit {
  readonly usuario = this.auth.usuario;
  secao: SecaoConta = 'visao-geral';
  categoriaConfiguracao: CategoriaConfiguracao = 'conta';
  meusTimes: any[] = [];
  meusTorneios: any[] = [];
  destaques: any[] = [];
  atividade?: ContaAtividade;
  formulario: any = {};
  novaSenha = '';
  confirmarSenha = '';
  salvando = false;
  mensagem = '';
  erro = '';
  constructor(
    private readonly http: HttpClient,
    private readonly auth: AuthService,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {}

  ngOnInit() {
    this.route.queryParamMap.subscribe(parametros => {
      const aba = parametros.get('aba');
      if (aba === 'perfil' || aba === 'configuracoes') {
        this.secao = aba;
      } else {
        this.secao = 'visao-geral';
      }

      const categoria = parametros.get('config');
      const categorias: CategoriaConfiguracao[] = [
        'conta',
        'privacidade',
        'notificacoes',
        'seguranca',
        'atividade',
        'ajuda'
      ];
      this.categoriaConfiguracao = categorias.includes(categoria as CategoriaConfiguracao)
        ? categoria as CategoriaConfiguracao
        : 'conta';
    });

    const conta = this.usuario();
    if (!conta) return;
    this.prepararFormulario();
    if (conta.podeGerenciarTimes) {
      this.http.get<any[]>('/backend/time/pesquisa?meus=true')
        .pipe(catchError(() => of([])))
        .subscribe(times => this.meusTimes = times);
    }

    this.http.get<any[]>('/backend/torneio/pesquisa')
      .pipe(catchError(() => of([])))
      .subscribe(torneios => {
        this.destaques = torneios.filter(torneio => torneio.status !== 'FINALIZADO').slice(0, 3);
      });

    if (conta.podeCriarTorneio) {
      this.http.get<any[]>(
        `/backend/torneio/pesquisa?organizadorId=${encodeURIComponent(String(conta.id))}`
      ).pipe(catchError(() => of([])))
        .subscribe(torneios => this.meusTorneios = torneios);
    }

    this.http.get<ContaAtividade>(
      `/backend/conta-usuario/perfil/${encodeURIComponent(conta.nomeUsuario)}/atividade`
    ).pipe(catchError(() => of(undefined))).subscribe(atividade => this.atividade = atividade);
  }

  abrirSecao(secao: SecaoConta) {
    this.secao = secao;
    this.mensagem = '';
    this.erro = '';
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: secao === 'visao-geral' ? {} : { aba: secao },
      replaceUrl: true
    });
  }

  abrirCategoriaConfiguracao(categoria: CategoriaConfiguracao) {
    this.categoriaConfiguracao = categoria;
    this.mensagem = '';
    this.erro = '';
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        aba: 'configuracoes',
        config: categoria === 'conta' ? null : categoria
      },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }

  sairConta() {
    this.auth.sair().subscribe(() => this.router.navigate(['/']));
  }

  salvarPerfil() {
    if (!this.formulario.nome?.trim() || !this.formulario.nomeUsuario?.trim() || !this.formulario.email?.trim()) {
      this.erro = 'Nome, nome de usuário e e-mail são obrigatórios.';
      return;
    }

    this.salvando = true;
    this.erro = '';
    this.mensagem = '';
    this.auth.atualizarConta({
      ...this.formulario,
      nomeUsuario: this.formulario.nomeUsuario.replace(/^@/, '')
    }).pipe(finalize(() => this.salvando = false)).subscribe({
      next: () => {
        this.prepararFormulario();
        this.mensagem = 'Perfil atualizado com sucesso.';
      },
      error: erro => this.erro = erro?.error?.mensagem || 'Não foi possível atualizar seu perfil.'
    });
  }

  alterarSenha() {
    if (this.novaSenha.length < 6 || this.novaSenha !== this.confirmarSenha) {
      this.erro = 'A senha deve ter pelo menos 6 caracteres e a confirmação precisa ser igual.';
      return;
    }

    this.erro = '';
    this.mensagem = '';
    this.auth.alterarSenha(this.novaSenha).subscribe({
      next: () => {
        this.novaSenha = '';
        this.confirmarSenha = '';
        this.mensagem = 'Senha atualizada com sucesso.';
      },
      error: erro => this.erro = erro?.error?.mensagem || 'Não foi possível alterar a senha.'
    });
  }

  excluirConta() {
    if (!confirm('Excluir permanentemente sua conta? Essa ação não pode ser desfeita.')) return;
    this.auth.excluirConta().subscribe({
      next: () => this.router.navigate(['/']),
      error: erro => this.erro = erro?.error?.mensagem || 'Não foi possível excluir a conta.'
    });
  }

  tipoLabel(tipo?: string): string {
    const labels: Record<string, string> = {
      USUARIO_COMUM: 'Usuário',
      JOGADOR: 'Jogador',
      ORGANIZADOR: 'Organizador',
      TREINADOR: 'Técnico / treinador',
      AUXILIAR_TECNICO: 'Auxiliar técnico',
      PREPARADOR_FISICO: 'Preparador físico',
      MEDICO: 'Médico'
    };
    return tipo ? labels[tipo] ?? tipo : '';
  }

  statusTorneioLabel(status?: string): string {
    const labels: Record<string, string> = {
      CONFIGURADO: 'Inscrições abertas',
      ESTRUTURA_GERADA: 'Estrutura pronta',
      INICIADO: 'Em andamento',
      FINALIZADO: 'Finalizado'
    };
    return status ? labels[status] ?? status.replaceAll('_', ' ') : '';
  }

  iniciais(nome?: string): string {
    return (nome ?? '?')
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map(parte => parte[0])
      .join('')
      .toUpperCase();
  }

  get palpitesEmAndamento(): number {
    return this.atividade?.palpites.filter(palpite => !palpite.apurado).length ?? 0;
  }

  get percentualPerfil(): number {
    const conta = this.usuario();
    if (!conta) return 0;
    const campos = [
      conta.nome,
      conta.nomeUsuario,
      conta.email,
      conta.telefone,
      conta.dataNascimento,
      conta.cidade,
      conta.estado,
      conta.biografia
    ];
    return Math.round((campos.filter(Boolean).length / campos.length) * 100);
  }

  private prepararFormulario() {
    const conta = this.usuario();
    if (!conta) return;
    this.formulario = {
      nome: conta.nome,
      nomeUsuario: conta.nomeUsuario,
      email: conta.email,
      telefone: conta.telefone ?? '',
      dataNascimento: conta.dataNascimento ?? '',
      cidade: conta.cidade ?? '',
      estado: conta.estado ?? '',
      biografia: conta.biografia ?? '',
      fotoPerfilUrl: conta.fotoPerfilUrl ?? ''
    };
  }

}
