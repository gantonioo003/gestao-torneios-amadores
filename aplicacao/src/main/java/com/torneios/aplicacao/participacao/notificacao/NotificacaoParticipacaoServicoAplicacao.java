package com.torneios.aplicacao.participacao.notificacao;

import java.time.LocalDateTime;
import java.util.List;

import com.torneios.aplicacao.participacao.notificacao.NotificacaoParticipacaoRepositorioAplicacao.NotificacaoParticipacao;

public class NotificacaoParticipacaoServicoAplicacao {

    private final NotificacaoParticipacaoRepositorioAplicacao repositorio;

    public NotificacaoParticipacaoServicoAplicacao(NotificacaoParticipacaoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    public void notificarTimeAceito(long id, long treinadorId, long timeId, long torneioId) {
        salvar(id, treinadorId, "Seu time foi aceito no torneio",
                "O organizador aceitou a solicitacao de participacao do seu time.",
                "/time/" + timeId + "/detalhes");
    }

    public void notificarConviteAceito(long id, long organizadorId, long timeId, long torneioId) {
        salvar(id, organizadorId, "O time aceitou o convite",
                "O treinador confirmou que o time participara do torneio.",
                "/torneio/" + torneioId + "?configurar=true");
    }

    public List<NotificacaoParticipacao> listar(long usuarioId) {
        return repositorio.listarPorUsuario(usuarioId);
    }

    public void marcarComoLida(long notificacaoId, long usuarioId) {
        repositorio.marcarComoLida(notificacaoId, usuarioId);
    }

    private void salvar(long id, long usuarioId, String titulo, String mensagem, String link) {
        repositorio.salvar(new NotificacaoParticipacao(
                id, usuarioId, titulo, mensagem, link, false, LocalDateTime.now()));
    }
}
