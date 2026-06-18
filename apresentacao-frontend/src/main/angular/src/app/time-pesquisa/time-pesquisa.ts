import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { catchError, of } from 'rxjs';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-time-pesquisa',
  imports: [FormsModule, RouterModule],
  templateUrl: './time-pesquisa.html'
})
export class TimePesquisa implements OnInit {
  recurso: any[] = [];
  termo = '';
  aba: 'explorar' | 'meus' = 'explorar';
  carregando = true;
  readonly usuario = this.auth.usuario;

  constructor(
    private readonly http: HttpClient,
    private readonly auth: AuthService
  ) {}

  ngOnInit() {
    this.buscar();
  }

  trocarAba(aba: 'explorar' | 'meus') {
    this.aba = aba;
    this.termo = '';
    this.buscar();
  }

  buscar() {
    this.carregando = true;
    const url = this.aba === 'meus'
      ? '/backend/time/pesquisa?meus=true'
      : `/backend/time/pesquisa?nome=${encodeURIComponent(this.termo)}`;
    this.http.get<any[]>(url)
      .pipe(catchError(() => of([])))
      .subscribe(times => {
        this.recurso = times;
        this.carregando = false;
      });
  }

  ehMeuTime(time: any): boolean {
    return this.usuario()?.id === time.responsavelId;
  }

  excluir(id: number) {
    if (!confirm('Confirma a exclusão deste time?')) return;
    this.http.post(`/backend/time/${id}/excluir`, {}).subscribe({
      next: () => this.buscar(),
      error: erro => alert(erro.error?.mensagem ?? 'Não foi possível excluir o time.')
    });
  }
}
