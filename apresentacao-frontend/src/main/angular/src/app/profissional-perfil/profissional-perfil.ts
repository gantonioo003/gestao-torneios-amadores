import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, ActivatedRouteSnapshot, ResolveData, Router, RouterLink, RouterStateSnapshot } from '@angular/router';
import { catchError, of } from 'rxjs';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-profissional-perfil',
  imports: [FormsModule, RouterLink],
  templateUrl: './profissional-perfil.html',
  styleUrl: './profissional-perfil.css'
})
export class ProfissionalPerfil implements OnInit {
  static readonly ID = 'id';
  static readonly RECURSO = 'recurso';

  profissional: any = {};
  historico: any[] = [];
  timesAtuais: any[] = [];
  conta: any = {};
  publicacoes: any[] = [];
  aba: 'dados' | 'publicacoes' = 'dados';
  mostrarFormCarreira = false;
  carreiraForm: any = {};
  readonly usuario = this.auth.usuario;

  constructor(
    private readonly http: HttpClient,
    private readonly rota: ActivatedRoute,
    private readonly router: Router,
    private readonly auth: AuthService
  ) {}

  ngOnInit() {
    const data = this.rota.snapshot.data[ProfissionalPerfil.RECURSO] ?? {};
    this.profissional = data.profissional ?? {};
    this.historico = data.historico ?? [];
    this.timesAtuais = data.timesAtuais ?? [];
    if (this.profissional.cadastranteId) {
      this.http.get<any>(`/backend/conta-usuario/${this.profissional.cadastranteId}`).subscribe({
        next: conta => this.conta = conta
      });
      this.http.get<any[]>(`/backend/feed/autor/${this.profissional.cadastranteId}`).subscribe({
        next: publicacoes => this.publicacoes = publicacoes,
        error: () => this.publicacoes = []
      });
    }
  }

  podeEditar(): boolean {
    return this.usuario()?.id === this.profissional.cadastranteId;
  }

  editarPerfil() {
    const novoNome = prompt('Novo nome:', this.profissional.nome);
    if (!novoNome) return;
    this.http.post(
      `/backend/profissional/${this.profissional.id}/salvar`,
      { ...this.profissional, nome: novoNome }
    ).subscribe({
      next: () => { this.profissional.nome = novoNome; },
      error: (e) => alert(e.error?.message ?? 'Erro ao editar.')
    });
  }

  removerPerfil() {
    if (!confirm('Remover este perfil permanentemente?')) return;
    this.http.post(
      `/backend/profissional/${this.profissional.id}/excluir`, {}
    ).subscribe({
      next: () => this.router.navigate(['/buscar']),
      error: (e) => alert(e.error?.message ?? 'Erro ao remover.')
    });
  }

  adicionarCarreira() {
    if (!this.carreiraForm.nomeDoClube) { alert('Nome do clube é obrigatório.'); return; }
    if (!this.carreiraForm.dataInicio) { alert('Data de início é obrigatória.'); return; }
    this.http.post(
      `/backend/profissional/${this.profissional.id}/adicionar-carreira`,
      this.carreiraForm
    ).subscribe({
      next: () => {
        this.mostrarFormCarreira = false;
        this.carreiraForm = {};
        this.recarregar();
      },
      error: (e) => alert(e.error?.message ?? 'Erro ao adicionar registro.')
    });
  }

  private recarregar() {
    this.http.get<any>(`/backend/profissional/${this.profissional.id}/edicao`)
      .pipe(catchError(() => of({})))
      .subscribe(r => { this.historico = r.historico ?? []; });
  }

  tipoLabel(tipo: string): string {
    const m: any = { JOGADOR: 'Jogador', TREINADOR: 'Treinador', AUXILIAR_TECNICO: 'Auxiliar Técnico', PREPARADOR_FISICO: 'Prep. Física', MEDICO: 'Médico' };
    return m[tipo] ?? tipo;
  }

  motivoLabel(motivo: string): string {
    const m: any = { FIM_DE_CONTRATO: 'Fim de contrato', TRANSFERENCIA: 'Transferência', LESAO: 'Lesão', APOSENTADORIA: 'Aposentadoria' };
    return m[motivo] ?? motivo;
  }

  iniciais(nome: string): string {
    return (nome || '?').split(/\s+/).filter(Boolean).slice(0, 2)
      .map(parte => parte[0]).join('').toUpperCase();
  }

  tempo(dataIso: string): string {
    const horas = Math.floor((Date.now() - new Date(dataIso).getTime()) / 3_600_000);
    if (horas < 1) return 'agora';
    if (horas < 24) return `${horas}h`;
    return `${Math.floor(horas / 24)}d`;
  }
}

export const PROFISSIONAL_PERFIL_RESOLVEDORES: ResolveData = {};
PROFISSIONAL_PERFIL_RESOLVEDORES[ProfissionalPerfil.RECURSO] = (rota: ActivatedRouteSnapshot, _s: RouterStateSnapshot) => {
  const id = rota.params[ProfissionalPerfil.ID];
  return inject(HttpClient).get(`/backend/profissional/${id}/edicao`).pipe(catchError(() => of({})));
};
