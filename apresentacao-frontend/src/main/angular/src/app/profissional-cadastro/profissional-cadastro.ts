import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-profissional-cadastro',
  imports: [FormsModule, RouterLink],
  templateUrl: './profissional-cadastro.html',
  styleUrl: './profissional-cadastro.css'
})
export class ProfissionalCadastro {
  nome = '';
  tipo = '';
  tipoSelecionado = '';
  private readonly cadastranteId = 1;

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  cadastrar() {
    if (!this.nome.trim()) { alert('Nome é obrigatório.'); return; }
    if (!this.tipoSelecionado) { alert('Tipo é obrigatório.'); return; }
    this.http.post('/backend/profissional/salvar', {
      nome: this.nome,
      tipo: this.tipoSelecionado,
      cadastranteId: this.cadastranteId
    }).subscribe({
      next: () => this.router.navigate(['/profissional/pesquisa']),
      error: (e) => alert(e.error?.message ?? 'Erro ao cadastrar.')
    });
  }
}
