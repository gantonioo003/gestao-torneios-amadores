import { HttpClient } from '@angular/common/http';
import { Component, computed, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from '../core/auth.service';

interface PerfilPublico {
  id: number;
  nome: string;
  nomeUsuario: string;
  cidade?: string;
  estado?: string;
  biografia?: string;
  tipo: string;
  podeCriarTorneio: boolean;
  podeGerenciarTimes: boolean;
  possuiPerfilProfissional: boolean;
}

interface TorneioAtividade {
  id: number;
  nome: string;
  formato: string;
  status: string;
  aceitaSolicitacoes: boolean;
}

interface ContaAtividade {
  torneiosOrganizados: TorneioAtividade[];
  torneiosParticipando: TorneioAtividade[];
}

@Component({
  selector: 'app-conta-perfil',
  imports: [RouterLink],
  templateUrl: './conta-perfil.html',
  styleUrl: './conta-perfil.css'
})
export class ContaPerfil implements OnInit {
  perfil?: PerfilPublico;
  atividade?: ContaAtividade;
  carregando = true;
  erro = '';

  readonly usuario = this.auth.usuario;
  readonly ehProprioPerfil = computed(
    () => !!this.perfil && this.usuario()?.nomeUsuario === this.perfil.nomeUsuario
  );

  constructor(
    private readonly http: HttpClient,
    private readonly route: ActivatedRoute,
    private readonly auth: AuthService
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(parametros => {
      const nomeUsuario = parametros.get('nomeUsuario') ?? '';
      this.carregarPerfil(nomeUsuario);
    });
  }

  tipoLabel(tipo?: string): string {
    const nomes: Record<string, string> = {
      JOGADOR: 'Jogador',
      ORGANIZADOR: 'Organizador',
      TREINADOR: 'Técnico / treinador',
      AUXILIAR_TECNICO: 'Auxiliar técnico',
      PREPARADOR_FISICO: 'Preparador físico',
      MEDICO: 'Médico'
    };
    return tipo ? nomes[tipo] ?? tipo : '';
  }

  statusTorneioLabel(status?: string): string {
    const nomes: Record<string, string> = {
      CONFIGURADO: 'Inscrições',
      ESTRUTURA_GERADA: 'Estrutura pronta',
      INICIADO: 'Em andamento',
      FINALIZADO: 'Finalizado'
    };
    return status ? nomes[status] ?? status.replaceAll('_', ' ') : '';
  }

  iniciais(nome?: string): string {
    return (nome ?? '?')
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map(parte => parte[0])
      .join('')
      .toUpperCase();
  }

  private carregarPerfil(nomeUsuario: string) {
    this.carregando = true;
    this.erro = '';
    this.http.get<PerfilPublico>(`/backend/conta-usuario/perfil/${encodeURIComponent(nomeUsuario)}`)
      .pipe(finalize(() => this.carregando = false))
      .subscribe({
        next: perfil => {
          this.perfil = perfil;
          this.carregarAtividade(nomeUsuario);
        },
        error: () => {
          this.perfil = undefined;
          this.erro = 'Perfil não encontrado.';
        }
      });
  }

  private carregarAtividade(nomeUsuario: string) {
    this.http.get<ContaAtividade>(
      `/backend/conta-usuario/perfil/${encodeURIComponent(nomeUsuario)}/atividade`
    ).subscribe({
      next: atividade => this.atividade = atividade,
      error: () => this.atividade = {
        torneiosOrganizados: [],
        torneiosParticipando: []
      }
    });
  }
}
