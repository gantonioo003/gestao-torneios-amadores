import { Component } from '@angular/core';
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

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router
  ) {
    if (this.auth.estaAutenticado()) {
      this.auth.validarSessao().subscribe();
    }
  }

  sair() {
    this.auth.sair().subscribe(() => this.router.navigate(['/']));
  }
}
