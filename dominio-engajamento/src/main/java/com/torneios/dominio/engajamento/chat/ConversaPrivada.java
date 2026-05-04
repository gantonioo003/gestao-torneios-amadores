package com.torneios.dominio.engajamento.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class ConversaPrivada {

    private final ConversaPrivadaId id;
    private final UsuarioId solicitanteId;
    private final UsuarioId destinatarioId;
    private final List<MensagemChat> mensagens = new ArrayList<>();
    private StatusConversa status;

    public ConversaPrivada(ConversaPrivadaId id, UsuarioId solicitanteId, UsuarioId destinatarioId) {
        this.id = Objects.requireNonNull(id, "O id da conversa e obrigatorio.");
        this.solicitanteId = Objects.requireNonNull(solicitanteId, "O solicitante da conversa e obrigatorio.");
        this.destinatarioId = Objects.requireNonNull(destinatarioId, "O destinatario da conversa e obrigatorio.");
        if (solicitanteId.equals(destinatarioId)) {
            throw new OperacaoNaoPermitidaException("O usuario nao pode solicitar conversa consigo mesmo.");
        }
        this.status = StatusConversa.SOLICITADA;
    }

    public ConversaPrivadaId getId() {
        return id;
    }

    public UsuarioId getSolicitanteId() {
        return solicitanteId;
    }

    public UsuarioId getDestinatarioId() {
        return destinatarioId;
    }

    public StatusConversa getStatus() {
        return status;
    }

    public List<MensagemChat> getMensagens() {
        return List.copyOf(mensagens);
    }

    public boolean estaSolicitada() {
        return status == StatusConversa.SOLICITADA;
    }

    public boolean estaAprovada() {
        return status == StatusConversa.APROVADA;
    }

    public boolean envolve(UsuarioId usuarioId) {
        return solicitanteId.equals(usuarioId) || destinatarioId.equals(usuarioId);
    }

    public boolean envolveAmbos(UsuarioId primeiroUsuario, UsuarioId segundoUsuario) {
        return envolve(primeiroUsuario) && envolve(segundoUsuario);
    }

    public void aprovar(UsuarioId usuarioId) {
        validarDestinatario(usuarioId);
        validarSolicitada();
        status = StatusConversa.APROVADA;
    }

    public void recusar(UsuarioId usuarioId) {
        validarDestinatario(usuarioId);
        validarSolicitada();
        status = StatusConversa.RECUSADA;
    }

    public MensagemChat enviarMensagem(MensagemChatId mensagemId, UsuarioId autorId, String conteudo) {
        validarParticipante(autorId);
        if (!estaAprovada()) {
            throw new OperacaoNaoPermitidaException(
                    "Mensagens so podem ser enviadas depois que a conversa for aprovada.");
        }
        MensagemChat mensagem = new MensagemChat(mensagemId, autorId, conteudo);
        mensagens.add(mensagem);
        return mensagem;
    }

    private void validarDestinatario(UsuarioId usuarioId) {
        if (!destinatarioId.equals(usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o destinatario pode aprovar ou recusar a solicitacao de conversa.");
        }
    }

    private void validarParticipante(UsuarioId usuarioId) {
        if (!envolve(usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas participantes da conversa podem enviar mensagens.");
        }
    }

    private void validarSolicitada() {
        if (!estaSolicitada()) {
            throw new OperacaoNaoPermitidaException("A solicitacao de conversa ja foi avaliada.");
        }
    }
}
