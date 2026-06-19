package com.torneios.aplicacao.participacao.notificacao;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificacaoParticipacaoRepositorioAplicacao {

    void salvar(NotificacaoParticipacao notificacao);

    List<NotificacaoParticipacao> listarPorUsuario(long usuarioId);

    void marcarComoLida(long notificacaoId, long usuarioId);

    record NotificacaoParticipacao(long id,
                                   long usuarioId,
                                   String titulo,
                                   String mensagem,
                                   String link,
                                   boolean lida,
                                   LocalDateTime criadaEm) {
    }
}
