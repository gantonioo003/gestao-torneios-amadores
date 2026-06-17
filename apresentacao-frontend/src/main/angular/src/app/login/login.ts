import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html'
})
export class Login {
  aba: 'entrar' | 'criar' = 'entrar';
  email = '';
  senha = '';
  nome = '';
  tipo = 'Jogador';

  constructor(private readonly router: Router) {}

  entrar() {
    this.router.navigate(['/home-logada']);
  }

  criar() {
    this.router.navigate(['/home-logada']);
  }
}
