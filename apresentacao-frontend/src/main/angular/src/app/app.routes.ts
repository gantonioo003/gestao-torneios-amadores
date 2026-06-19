import { Routes } from '@angular/router';
import { Desafio } from './desafio/desafio';
import { FeedSocial } from './feed-social/feed-social';
import { HomeLogada } from './home-logada/home-logada';
import { HomePublica } from './home-publica/home-publica';
import { Login } from './login/login';
import { PartidaDetalhes } from './partida-detalhes/partida-detalhes';
import { PROFISSIONAL_PERFIL_RESOLVEDORES, ProfissionalPerfil } from './profissional-perfil/profissional-perfil';
import { PROFISSIONAL_EDICAO_RESOLVEDORES, ProfissionalEdicao } from './profissional-edicao/profissional-edicao';
import { TIME_DETALHES_RESOLVEDORES, TimeDetalhes } from './time-detalhes/time-detalhes';
import { TIME_EDICAO_RESOLVEDORES, TimeEdicao } from './time-edicao/time-edicao';
import { TIME_VINCULAR_RESOLVEDORES, TimeVincular } from './time-vincular/time-vincular';
import { TimeCriacao } from './time-criacao/time-criacao';
import { TorneioCriacao } from './torneio-criacao/torneio-criacao';
import { TorneioDetalhes } from './torneio-detalhes/torneio-detalhes';
import { ChatPrivado } from './chat-privado/chat-privado';
import { SumulaEstatistica } from './sumula-estatistica/sumula-estatistica';
import { authGuard, roleGuard, timeRosterGuard } from './core/auth.guard';
import { ContaPerfil } from './conta-perfil/conta-perfil';
import { Palpites } from './palpites/palpites';
import { BuscaGeral } from './busca-geral/busca-geral';
import { Comparativo } from './comparativo/comparativo';

export const routes: Routes = [
  { path: '', component: HomePublica },
  { path: 'login', component: Login },
  { path: 'home-logada', component: HomeLogada, canActivate: [authGuard] },
  { path: 'perfil/:nomeUsuario', component: ContaPerfil },
  { path: 'buscar', component: BuscaGeral },
  { path: 'torneio', redirectTo: 'buscar', pathMatch: 'full' },
  { path: 'torneios', redirectTo: 'buscar', pathMatch: 'full' },
  { path: 'torneio/criacao', component: TorneioCriacao, canActivate: [authGuard, roleGuard], data: { permissao: 'criarTorneio' } },
  { path: 'torneio/:id', component: TorneioDetalhes },
  { path: 'partida/:id', component: PartidaDetalhes },
  { path: 'sumula', component: SumulaEstatistica },
  { path: 'ranking', redirectTo: 'buscar', pathMatch: 'full' },
  { path: 'palpites', component: Palpites, canActivate: [authGuard] },
  { path: 'feed', component: FeedSocial, canActivate: [authGuard] },
  { path: 'desafio', component: Desafio },
  { path: 'chat', component: ChatPrivado, canActivate: [authGuard] },
  { path: 'time/pesquisa', redirectTo: 'buscar', pathMatch: 'full' },
  { path: 'time/criacao', component: TimeCriacao, canActivate: [authGuard, roleGuard], data: { permissao: 'gerenciarTimes' } },
  { path: 'time/:id/detalhes', component: TimeDetalhes, resolve: TIME_DETALHES_RESOLVEDORES },
  { path: 'time/:id/edicao', component: TimeEdicao, resolve: TIME_EDICAO_RESOLVEDORES, canActivate: [authGuard, roleGuard], data: { permissao: 'gerenciarTimes' } },
  { path: 'time/:id/vincular', component: TimeVincular, resolve: TIME_VINCULAR_RESOLVEDORES, canActivate: [authGuard, timeRosterGuard] },
  { path: 'profissional/pesquisa', redirectTo: 'buscar', pathMatch: 'full' },
  { path: 'profissional/:id/edicao', component: ProfissionalEdicao, resolve: PROFISSIONAL_EDICAO_RESOLVEDORES, canActivate: [authGuard] },
  { path: 'profissional/:id/perfil', component: ProfissionalPerfil, resolve: PROFISSIONAL_PERFIL_RESOLVEDORES },
  { path: 'comparativo', component: Comparativo },
  { path: '**', redirectTo: '' }
];
