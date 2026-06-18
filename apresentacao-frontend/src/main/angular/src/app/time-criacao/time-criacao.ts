import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-time-criacao',
  imports: [FormsModule, RouterLink],
  templateUrl: './time-criacao.html',
  styleUrl: './time-criacao.css'
})
export class TimeCriacao {
  nome = '';

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  criar() {
    if (!this.nome.trim()) { alert('Nome do time é obrigatório.'); return; }
    this.http.post('/backend/time/salvar', { nome: this.nome })
      .subscribe({
        next: () => this.router.navigate(['/time/pesquisa']),
        error: (e) => alert(e.error?.message ?? 'Erro ao criar time.')
      });
  }
}
