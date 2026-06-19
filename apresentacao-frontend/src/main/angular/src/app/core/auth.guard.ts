import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = (_, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const acesso = state.url.startsWith('/chat')
    ? 'chat'
    : state.url.startsWith('/palpites')
      ? 'palpites'
      : state.url.startsWith('/feed')
        ? 'feed'
        : 'restrito';

  const redirecionarParaLogin = () => router.createUrlTree(['/login'], {
    queryParams: { returnUrl: state.url, acesso }
  });

  if (!auth.estaAutenticado()) {
    return redirecionarParaLogin();
  }

  return auth.validarSessao().pipe(
    map(usuario => usuario ? true : redirecionarParaLogin())
  );
};

export const roleGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const permissao = route.data['permissao'] as 'criarTorneio' | 'gerenciarTimes';

  const permitido = permissao === 'criarTorneio'
    ? auth.podeCriarTorneio()
    : auth.podeGerenciarTimes();

  if (!auth.estaAutenticado()) {
    return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url, acesso: 'restrito' } });
  }
  return permitido ? true : router.createUrlTree(['/home-logada'], { queryParams: { acessoNegado: permissao } });
};

export const timeRosterGuard: CanActivateFn = (route) => {
  const http = inject(HttpClient);
  const router = inject(Router);
  const timeId = route.paramMap.get('id');

  return http.get<{ podeGerenciarElenco: boolean }>(`/backend/time/${timeId}/edicao`).pipe(
    map(recurso => recurso.podeGerenciarElenco
      ? true
      : router.createUrlTree(['/time', timeId, 'detalhes'], {
          queryParams: { acessoNegado: 'elenco' }
        })),
    catchError(() => of(router.createUrlTree(['/time', timeId, 'detalhes'])))
  );
};
