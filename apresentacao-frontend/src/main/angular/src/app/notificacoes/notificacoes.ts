import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, finalize, forkJoin, of } from 'rxjs';

interface Notificacao {
  id: string;
  categoria: string;
  titulo: string;
  mensagem: string;
  link: string;
  lida: boolean;
  arquivada: boolean;
  criadaEm: string;
}

interface Preferencias {
  categoriasAtivas: string[];
}

type Aba = 'ativas' | 'nao-lidas' | 'arquivadas';

@Component({
  selector: 'app-notificacoes',
  imports: [FormsModule],
  templateUrl: './notificacoes.html',
  styleUrl: './notificacoes.css'
})
export class Notificacoes implements OnInit {
  notificacoes: Notificacao[] = [];
  categorias: string[] = [];
  categoriasAtivas = new Set<string>();
  aba: Aba = 'ativas';
  categoriaSelecionada = 'TODAS';
  carregando = true;
  salvandoPreferencias = false;
  mensagem = '';
  erro = '';

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router
  ) {}

  ngOnInit() {
    this.carregar();
  }

  get naoLidas(): number {
    return this.notificacoes.filter(item => !item.lida && !item.arquivada).length;
  }

  get notificacoesFiltradas(): Notificacao[] {
    return this.notificacoes.filter(item => {
      const pertenceAba = this.aba === 'arquivadas'
        ? item.arquivada
        : this.aba === 'nao-lidas'
          ? !item.arquivada && !item.lida
          : !item.arquivada;
      const pertenceCategoria = this.categoriaSelecionada === 'TODAS'
        || item.categoria === this.categoriaSelecionada;
      return pertenceAba && pertenceCategoria;
    });
  }

  abrir(item: Notificacao) {
    const concluir = () => {
      item.lida = true;
      this.notificarCabecalho();
      if (item.link) this.router.navigateByUrl(item.link);
    };
    if (item.lida) {
      concluir();
      return;
    }
    this.http.post(`/backend/notificacoes/${item.id}/ler`, {}).subscribe({
      next: concluir,
      error: () => this.erro = 'Nao foi possivel marcar a notificacao como lida.'
    });
  }

  marcarTodasComoLidas() {
    this.http.post('/backend/notificacoes/ler-todas', {}).subscribe({
      next: () => {
        this.notificacoes.filter(item => !item.arquivada).forEach(item => item.lida = true);
        this.notificarCabecalho();
        this.mensagem = 'Todas as notificacoes foram marcadas como lidas.';
      },
      error: () => this.erro = 'Nao foi possivel atualizar as notificacoes.'
    });
  }

  arquivar(item: Notificacao) {
    this.http.post(`/backend/notificacoes/${item.id}/arquivar`, {}).subscribe({
      next: () => {
        item.arquivada = true;
        item.lida = true;
        this.notificarCabecalho();
      },
      error: () => this.erro = 'Nao foi possivel arquivar a notificacao.'
    });
  }

  alternarCategoria(categoria: string, ativa: boolean) {
    if (ativa) {
      this.categoriasAtivas.add(categoria);
    } else {
      this.categoriasAtivas.delete(categoria);
    }
  }

  categoriaAtiva(categoria: string): boolean {
    return this.categoriasAtivas.has(categoria);
  }

  salvarPreferencias() {
    this.salvandoPreferencias = true;
    this.mensagem = '';
    this.erro = '';
    this.http.post<Preferencias>('/backend/notificacoes/preferencias', {
      categoriasAtivas: [...this.categoriasAtivas]
    }).pipe(finalize(() => this.salvandoPreferencias = false)).subscribe({
      next: preferencias => {
        this.categoriasAtivas = new Set(preferencias.categoriasAtivas);
        this.mensagem = 'Preferencias de notificacao salvas.';
      },
      error: () => this.erro = 'Nao foi possivel salvar suas preferencias.'
    });
  }

  categoriaLabel(categoria: string): string {
    const labels: Record<string, string> = {
      TODAS: 'Todas',
      TORNEIO: 'Torneios',
      TIME: 'Times',
      AMISTOSO: 'Amistosos',
      SOCIAL: 'Social',
      SISTEMA: 'Sistema'
    };
    return labels[categoria] ?? categoria;
  }

  categoriaDescricao(categoria: string): string {
    const descricoes: Record<string, string> = {
      TORNEIO: 'Convites, inscricoes e atualizacoes de competicoes.',
      TIME: 'Mudancas importantes relacionadas aos seus times.',
      AMISTOSO: 'Propostas, confirmacoes e alteracoes de amistosos.',
      SOCIAL: 'Interacoes do feed e novas conversas.',
      SISTEMA: 'Avisos gerais e informacoes da plataforma.'
    };
    return descricoes[categoria] ?? '';
  }

  icone(categoria: string): string {
    const icones: Record<string, string> = {
      TORNEIO: 'bi-trophy',
      TIME: 'bi-shield',
      AMISTOSO: 'bi-dribbble',
      SOCIAL: 'bi-people',
      SISTEMA: 'bi-info-circle'
    };
    return icones[categoria] ?? 'bi-bell';
  }

  formatarData(dataIso: string): string {
    return new Date(dataIso).toLocaleString('pt-BR', {
      day: '2-digit',
      month: 'short',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  private carregar() {
    this.carregando = true;
    forkJoin({
      notificacoes: this.http.get<Notificacao[]>('/backend/notificacoes?incluirArquivadas=true')
        .pipe(catchError(() => of([]))),
      categorias: this.http.get<string[]>('/backend/notificacoes/categorias')
        .pipe(catchError(() => of([]))),
      preferencias: this.http.get<Preferencias>('/backend/notificacoes/preferencias')
        .pipe(catchError(() => of({ categoriasAtivas: [] })))
    }).pipe(finalize(() => this.carregando = false)).subscribe(resultado => {
      this.notificacoes = resultado.notificacoes;
      this.categorias = resultado.categorias;
      this.categoriasAtivas = new Set(resultado.preferencias.categoriasAtivas);
    });
  }

  private notificarCabecalho() {
    window.dispatchEvent(new Event('notificacoes-atualizadas'));
  }
}
