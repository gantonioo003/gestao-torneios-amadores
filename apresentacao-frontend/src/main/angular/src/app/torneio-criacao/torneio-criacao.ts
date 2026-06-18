import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-torneio-criacao',
  imports: [FormsModule, RouterLink],
  templateUrl: './torneio-criacao.html'
})
export class TorneioCriacao {
  nome = '';
  formato = 'FASE_DE_GRUPOS_COM_MATA_MATA';
  equipe = 'ONZE_POR_ONZE';
  inscricao = 'ABERTA';
  participantes = 8;
  regras = 'Entrada por solicitacao, minimo de 8 times e scout opcional por partida.';
  modoPreparacao: 'sorteio' | 'manual' = 'sorteio';

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  criar() {
    if (!this.nome.trim()) {
      alert('Informe o nome do torneio.');
      return;
    }
    this.http.post('/backend/torneio/salvar', {
      nome: this.nome,
      formato: this.formato,
      formatoEquipe: this.equipe,
      aceitaSolicitacoes: this.inscricao === 'ABERTA'
    }).subscribe({
      next: () => this.router.navigate(['/torneios']),
      error: erro => alert(erro.error?.mensagem ?? 'Não foi possível criar o torneio.')
    });
  }
}
