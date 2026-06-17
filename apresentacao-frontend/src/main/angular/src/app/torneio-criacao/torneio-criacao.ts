import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-torneio-criacao',
  imports: [FormsModule, RouterLink],
  templateUrl: './torneio-criacao.html'
})
export class TorneioCriacao {
  nome = 'Copa da Amizade';
  formato = 'Grupos + mata-mata';
  equipe = '11 titulares + 7 reservas';
  inscricao = 'Aberta';
  participantes = 8;
  regras = 'Entrada por solicitacao, minimo de 8 times e scout opcional por partida.';
  modoPreparacao: 'sorteio' | 'manual' = 'sorteio';

  constructor(private readonly router: Router) {}

  criar() {
    if (!this.nome.trim()) {
      alert('Informe o nome do torneio.');
      return;
    }
    this.router.navigate(['/torneio/1']);
  }
}
