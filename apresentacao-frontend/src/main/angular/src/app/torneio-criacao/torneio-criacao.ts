import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-torneio-criacao',
  imports: [FormsModule, RouterLink],
  templateUrl: './torneio-criacao.html',
  styleUrl: './torneio-criacao.css'
})
export class TorneioCriacao {
  nome = '';
  formato = 'FASE_DE_GRUPOS_COM_MATA_MATA';
  equipe = 'ONZE_POR_ONZE';
  aceitaSolicitacoes = true;
  criando = false;
  erro = '';

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router
  ) {}

  criar() {
    if (!this.nome.trim()) {
      this.erro = 'Informe o nome do torneio.';
      return;
    }

    this.criando = true;
    this.erro = '';
    this.http.post<any>('/backend/preparacao-torneio/salvar', {
      nome: this.nome.trim(),
      formato: this.formato,
      formatoEquipe: this.equipe,
      aceitaSolicitacoes: this.aceitaSolicitacoes
    }).pipe(finalize(() => this.criando = false)).subscribe({
      next: torneio => this.router.navigate(['/torneio', torneio.id], {
        queryParams: { configurar: true }
      }),
      error: erro => this.erro = erro?.error?.mensagem
        ?? erro?.error?.detail
        ?? 'Nao foi possivel criar o torneio.'
    });
  }

  get minimoParticipantes(): number {
    return this.formato === 'FASE_DE_GRUPOS_COM_MATA_MATA' ? 4 : 2;
  }

  formatoLabel(): string {
    const labels: Record<string, string> = {
      FASE_DE_GRUPOS_COM_MATA_MATA: 'Grupos + mata-mata',
      PONTOS_CORRIDOS: 'Pontos corridos',
      MATA_MATA: 'Mata-mata',
      FINAL_UNICA: 'Final unica'
    };
    return labels[this.formato];
  }
}
