import { Routes } from '@angular/router';
import { Desafio } from './desafio/desafio';
import { FeedSocial } from './feed-social/feed-social';
import { HomeLogada } from './home-logada/home-logada';
import { HomePublica } from './home-publica/home-publica';
import { Login } from './login/login';
import { PartidaDetalhes } from './partida-detalhes/partida-detalhes';
import { PROFISSIONAL_PERFIL_RESOLVEDORES, ProfissionalPerfil } from './profissional-perfil/profissional-perfil';
import { PROFISSIONAL_PESQUISA_RESOLVEDORES, ProfissionalPesquisa } from './profissional-pesquisa/profissional-pesquisa';
import { PROFISSIONAL_EDICAO_RESOLVEDORES, ProfissionalEdicao } from './profissional-edicao/profissional-edicao';
import { Ranking } from './ranking/ranking';
import { TIME_DETALHES_RESOLVEDORES, TimeDetalhes } from './time-detalhes/time-detalhes';
import { TIME_EDICAO_RESOLVEDORES, TimeEdicao } from './time-edicao/time-edicao';
import { TimePesquisa } from './time-pesquisa/time-pesquisa';
import { TIME_VINCULAR_RESOLVEDORES, TimeVincular } from './time-vincular/time-vincular';
import { TimeCriacao } from './time-criacao/time-criacao';
import { TorneioCriacao } from './torneio-criacao/torneio-criacao';
import { TorneioDetalhes } from './torneio-detalhes/torneio-detalhes';
import { ChatPrivado } from './chat-privado/chat-privado';
import { SumulaEstatistica } from './sumula-estatistica/sumula-estatistica';
import { authGuard, roleGuard, timeRosterGuard } from './core/auth.guard';
import { ContaPerfil } from './conta-perfil/conta-perfil';
import { TorneioPesquisa } from './torneio-pesquisa/torneio-pesquisa';
import { Palpites } from './palpites/palpites';

export const routes: Routes = [
  { path: '', component: HomePublica },
  { path: 'login', component: Login },
  { path: 'home-logada', component: HomeLogada, canActivate: [authGuard] },
  { path: 'perfil/:nomeUsuario', component: ContaPerfil },
  { path: 'torneio', redirectTo: 'torneios', pathMatch: 'full' },
  { path: 'torneios', component: TorneioPesquisa },
  { path: 'torneio/criacao', component: TorneioCriacao, canActivate: [authGuard, roleGuard], data: { permissao: 'criarTorneio' } },
  { path: 'torneio/:id', component: TorneioDetalhes },
  { path: 'partida/:id', component: PartidaDetalhes },
  { path: 'sumula', component: SumulaEstatistica },
  { path: 'ranking', component: Ranking },
  { path: 'palpites', component: Palpites },
  { path: 'feed', component: FeedSocial },
  { path: 'desafio', component: Desafio },
  { path: 'chat', component: ChatPrivado, canActivate: [authGuard] },
  { path: 'time/pesquisa', component: TimePesquisa },
  { path: 'time/criacao', component: TimeCriacao, canActivate: [authGuard, roleGuard], data: { permissao: 'gerenciarTimes' } },
  { path: 'time/:id/detalhes', component: TimeDetalhes, resolve: TIME_DETALHES_RESOLVEDORES },
  { path: 'time/:id/edicao', component: TimeEdicao, resolve: TIME_EDICAO_RESOLVEDORES, canActivate: [authGuard, roleGuard], data: { permissao: 'gerenciarTimes' } },
  { path: 'time/:id/vincular', component: TimeVincular, resolve: TIME_VINCULAR_RESOLVEDORES, canActivate: [authGuard, timeRosterGuard] },
  { path: 'profissional/pesquisa', component: ProfissionalPesquisa, resolve: PROFISSIONAL_PESQUISA_RESOLVEDORES },
  { path: 'profissional/:id/edicao', component: ProfissionalEdicao, resolve: PROFISSIONAL_EDICAO_RESOLVEDORES, canActivate: [authGuard] },
  { path: 'profissional/:id/perfil', component: ProfissionalPerfil, resolve: PROFISSIONAL_PERFIL_RESOLVEDORES },
  { path: '**', redirectTo: '' }
];
