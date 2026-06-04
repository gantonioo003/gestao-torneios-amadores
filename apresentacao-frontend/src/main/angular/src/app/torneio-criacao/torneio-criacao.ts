import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({ selector: 'app-torneio-criacao', imports: [FormsModule], templateUrl: './torneio-criacao.html' })
export class TorneioCriacao {
  nome=''; formato=''; equipe=''; inscricao='aberta'; qtdParticipantes=8; regras=''; modo='';
  constructor(private readonly router: Router) {}
  criar() { if (!this.nome.trim()){alert('Nome obrigatório.');return;} this.router.navigate(['/torneio']); }
}
