import { HttpClient } from '@angular/common/http';
import { Component, HostListener } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './core/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  readonly usuario = this.auth.usuario;
  notificacoes: any[] = [];
  notificacoesAbertas = false;

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
    private readonly http: HttpClient
  ) {
    if (this.auth.estaAutenticado()) {
      this.auth.validarSessao().subscribe(usuario => {
        if (usuario) this.carregarNotificacoes();
      });
    }
  }

  get notificacoesNaoLidas(): number {
    return this.notificacoes.filter(item => !item.lida).length;
  }

  @HostListener('window:notificacoes-atualizadas')
  aoAtualizarNotificacoes() {
    this.carregarNotificacoes();
  }

  alternarNotificacoes() {
    this.notificacoesAbertas = !this.notificacoesAbertas;
    if (this.notificacoesAbertas) this.carregarNotificacoes();
  }

  abrirNotificacao(notificacao: any) {
    this.http.post(`/backend/notificacoes/${notificacao.id}/ler`, {}).subscribe(() => {
      notificacao.lida = true;
      this.notificacoesAbertas = false;
      this.router.navigateByUrl(notificacao.link);
    });
  }

  iconeNotificacao(categoria: string): string {
    const icones: Record<string, string> = {
      TORNEIO: 'bi-trophy',
      TIME: 'bi-shield',
      AMISTOSO: 'bi-dribbble',
      SOCIAL: 'bi-people',
      SISTEMA: 'bi-info-circle'
    };
    return icones[categoria] ?? 'bi-bell';
  }

  sair() {
    this.auth.sair().subscribe(() => this.router.navigate(['/']));
  }

  private carregarNotificacoes() {
    this.http.get<any[]>('/backend/notificacoes').subscribe({
      next: notificacoes => this.notificacoes = notificacoes,
      error: () => this.notificacoes = []
    });
  }
}
