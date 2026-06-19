import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { IdentityEditor } from '../shared/identity-editor/identity-editor';

@Component({
  selector: 'app-time-criacao',
  imports: [FormsModule, RouterLink, IdentityEditor],
  templateUrl: './time-criacao.html',
  styleUrl: './time-criacao.css'
})
export class TimeCriacao {
  nome = '';
  imagemUrl = '';

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  criar() {
    if (!this.nome.trim()) { alert('Nome do time é obrigatório.'); return; }
    if (!this.imagemUrl) { alert('Escolha ou gere o escudo do time.'); return; }
    this.http.post('/backend/time/salvar', { nome: this.nome, imagemUrl: this.imagemUrl })
      .subscribe({
        next: () => this.router.navigate(['/buscar']),
        error: (e) => alert(e.error?.message ?? 'Erro ao criar time.')
      });
  }
}
