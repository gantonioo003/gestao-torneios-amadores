import { Routes } from '@angular/router';
import { TIME_PESQUISA_RESOLVEDORES, TimePesquisa } from './time-pesquisa/time-pesquisa';
import { TIME_EDICAO_RESOLVEDORES, TimeEdicao } from './time-edicao/time-edicao';
import { TIME_DETALHES_RESOLVEDORES, TimeDetalhes } from './time-detalhes/time-detalhes';
import { TimeCriacao } from './time-criacao/time-criacao';
import { TIME_VINCULAR_RESOLVEDORES, TimeVincular } from './time-vincular/time-vincular';
import { PROFISSIONAL_PESQUISA_RESOLVEDORES, ProfissionalPesquisa } from './profissional-pesquisa/profissional-pesquisa';
import { PROFISSIONAL_EDICAO_RESOLVEDORES, ProfissionalEdicao } from './profissional-edicao/profissional-edicao';
import { ProfissionalCadastro } from './profissional-cadastro/profissional-cadastro';
import { PROFISSIONAL_PERFIL_RESOLVEDORES, ProfissionalPerfil } from './profissional-perfil/profissional-perfil';

export const routes: Routes = [
  { path: '', redirectTo: '/time/pesquisa', pathMatch: 'full' },
  { path: 'time/pesquisa', component: TimePesquisa, resolve: TIME_PESQUISA_RESOLVEDORES },
  { path: 'time/criacao', component: TimeCriacao },
  { path: 'time/:id/detalhes', component: TimeDetalhes, resolve: TIME_DETALHES_RESOLVEDORES },
  { path: 'time/:id/edicao', component: TimeEdicao, resolve: TIME_EDICAO_RESOLVEDORES },
  { path: 'time/:id/vincular', component: TimeVincular, resolve: TIME_VINCULAR_RESOLVEDORES },
  { path: 'profissional/pesquisa', component: ProfissionalPesquisa, resolve: PROFISSIONAL_PESQUISA_RESOLVEDORES },
  { path: 'profissional/criacao', component: ProfissionalCadastro },
  { path: 'profissional/:id/edicao', component: ProfissionalEdicao, resolve: PROFISSIONAL_EDICAO_RESOLVEDORES },
  { path: 'profissional/:id/perfil', component: ProfissionalPerfil, resolve: PROFISSIONAL_PERFIL_RESOLVEDORES },
];
