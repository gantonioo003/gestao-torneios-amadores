import { Routes } from '@angular/router';
import { Desafio } from './desafio/desafio';
import { FeedSocial } from './feed-social/feed-social';
import { HomeLogada } from './home-logada/home-logada';
import { HomePublica } from './home-publica/home-publica';
import { Login } from './login/login';
import { PartidaDetalhes } from './partida-detalhes/partida-detalhes';
import { PROFISSIONAL_PERFIL_RESOLVEDORES, ProfissionalPerfil } from './profissional-perfil/profissional-perfil';
import { PROFISSIONAL_PESQUISA_RESOLVEDORES, ProfissionalPesquisa } from './profissional-pesquisa/profissional-pesquisa';
import { ProfissionalCadastro } from './profissional-cadastro/profissional-cadastro';
import { PROFISSIONAL_EDICAO_RESOLVEDORES, ProfissionalEdicao } from './profissional-edicao/profissional-edicao';
import { Ranking } from './ranking/ranking';
import { TIME_DETALHES_RESOLVEDORES, TimeDetalhes } from './time-detalhes/time-detalhes';
import { TIME_EDICAO_RESOLVEDORES, TimeEdicao } from './time-edicao/time-edicao';
import { TIME_PESQUISA_RESOLVEDORES, TimePesquisa } from './time-pesquisa/time-pesquisa';
import { TIME_VINCULAR_RESOLVEDORES, TimeVincular } from './time-vincular/time-vincular';
import { TimeCriacao } from './time-criacao/time-criacao';
import { TorneioCriacao } from './torneio-criacao/torneio-criacao';
import { TorneioDetalhes } from './torneio-detalhes/torneio-detalhes';
import { ChatPrivado } from './chat-privado/chat-privado';
import { SumulaEstatistica } from './sumula-estatistica/sumula-estatistica';

export const routes: Routes = [
  { path: '', component: HomePublica },
  { path: 'login', component: Login },
  { path: 'home-logada', component: HomeLogada },
  { path: 'torneio', redirectTo: 'torneio/1', pathMatch: 'full' },
  { path: 'torneio/criacao', component: TorneioCriacao },
  { path: 'torneio/:id', component: TorneioDetalhes },
  { path: 'partida/:id', component: PartidaDetalhes },
  { path: 'sumula', component: SumulaEstatistica },
  { path: 'ranking', component: Ranking },
  { path: 'feed', component: FeedSocial },
  { path: 'desafio', component: Desafio },
  { path: 'chat', component: ChatPrivado },
  { path: 'time/pesquisa', component: TimePesquisa, resolve: TIME_PESQUISA_RESOLVEDORES },
  { path: 'time/criacao', component: TimeCriacao },
  { path: 'time/:id/detalhes', component: TimeDetalhes, resolve: TIME_DETALHES_RESOLVEDORES },
  { path: 'time/:id/edicao', component: TimeEdicao, resolve: TIME_EDICAO_RESOLVEDORES },
  { path: 'time/:id/vincular', component: TimeVincular, resolve: TIME_VINCULAR_RESOLVEDORES },
  { path: 'profissional/pesquisa', component: ProfissionalPesquisa, resolve: PROFISSIONAL_PESQUISA_RESOLVEDORES },
  { path: 'profissional/criacao', component: ProfissionalCadastro },
  { path: 'profissional/:id/edicao', component: ProfissionalEdicao, resolve: PROFISSIONAL_EDICAO_RESOLVEDORES },
  { path: 'profissional/:id/perfil', component: ProfissionalPerfil, resolve: PROFISSIONAL_PERFIL_RESOLVEDORES },
  { path: '**', redirectTo: '' }
];
