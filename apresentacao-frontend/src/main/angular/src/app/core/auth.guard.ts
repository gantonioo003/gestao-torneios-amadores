import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = (_, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const redirecionarParaLogin = () => router.createUrlTree(['/login'], {
    queryParams: { returnUrl: state.url, acesso: state.url.startsWith('/chat') ? 'chat' : 'restrito' }
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
