package com.torneios.dominio.participacao.acesso;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface NotificacaoRepositorio {

    void salvar(Notificacao notificacao);

    Optional<Notificacao> buscarPorId(NotificacaoId id);

    List<Notificacao> listarPorUsuario(UsuarioId usuarioId);

    void salvarPreferencias(PreferenciasNotificacao preferencias);

    Optional<PreferenciasNotificacao> buscarPreferencias(UsuarioId usuarioId);
}
