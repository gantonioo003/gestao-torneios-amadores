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
  fotoPerfilUrl?: string;
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

interface PostPerfil {
  id: number;
  identidadeNome: string;
  autorNomeUsuario: string;
  autorFotoPerfilUrl?: string;
  conteudo: string;
  hashtags: string[];
  midias: string[];
  quantidadeCurtidas: number;
  comentarios: unknown[];
  criadaEm: string;
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
  aviso = '';
  aba: 'perfil' | 'publicacoes' = 'perfil';
  publicacoes: PostPerfil[] = [];

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
      USUARIO_COMUM: 'Usuário',
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

  denunciarPerfil() {
    if (!this.perfil || !this.usuario()) return;
    const motivo = prompt('Por que este perfil deve ser revisado?');
    if (!motivo?.trim()) return;
    this.http.post('/backend/moderacao/denuncias', {
      tipoAlvo: 'PERFIL',
      alvoId: this.perfil.id,
      motivo: motivo.trim()
    }).subscribe({
      next: () => this.aviso = 'Denuncia enviada para analise.',
      error: erro => this.aviso = erro?.error?.mensagem ?? 'Nao foi possivel enviar a denuncia.'
    });
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
          this.carregarPublicacoes(perfil.id);
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

  private carregarPublicacoes(usuarioId: number) {
    this.http.get<PostPerfil[]>(`/backend/feed/autor/${usuarioId}`).subscribe({
      next: publicacoes => this.publicacoes = publicacoes,
      error: () => this.publicacoes = []
    });
  }

  tempo(dataIso: string): string {
    const horas = Math.floor((Date.now() - new Date(dataIso).getTime()) / 3_600_000);
    if (horas < 1) return 'agora';
    if (horas < 24) return `${horas}h`;
    return `${Math.floor(horas / 24)}d`;
  }
}
