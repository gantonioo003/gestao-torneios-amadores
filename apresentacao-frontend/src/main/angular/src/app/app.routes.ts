import { Routes } from '@angular/router';
import { TIME_PESQUISA_RESOLVEDORES, TimePesquisa } from './time-pesquisa/time-pesquisa';
import { TIME_EDICAO_RESOLVEDORES, TimeEdicao } from './time-edicao/time-edicao';
import { PROFISSIONAL_PESQUISA_RESOLVEDORES, ProfissionalPesquisa } from './profissional-pesquisa/profissional-pesquisa';
import { PROFISSIONAL_EDICAO_RESOLVEDORES, ProfissionalEdicao } from './profissional-edicao/profissional-edicao';

export const routes: Routes = [
  { path: '', redirectTo: '/time/pesquisa', pathMatch: 'full' },
  { path: 'time/pesquisa', component: TimePesquisa, resolve: TIME_PESQUISA_RESOLVEDORES },
  { path: 'time/criacao', component: TimeEdicao, resolve: {} },
  { path: 'time/:id/edicao', component: TimeEdicao, resolve: TIME_EDICAO_RESOLVEDORES },
  { path: 'profissional/pesquisa', component: ProfissionalPesquisa, resolve: PROFISSIONAL_PESQUISA_RESOLVEDORES },
  { path: 'profissional/criacao', component: ProfissionalEdicao, resolve: {} },
  { path: 'profissional/:id/edicao', component: ProfissionalEdicao, resolve: PROFISSIONAL_EDICAO_RESOLVEDORES },
];
