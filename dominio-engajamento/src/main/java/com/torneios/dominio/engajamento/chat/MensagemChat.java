package com.torneios.dominio.engajamento.chat;

import java.time.LocalDateTime;
import java.util.Objects;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class MensagemChat {

    private final MensagemChatId id;
    private final UsuarioId autorId;
    private final String conteudo;
    private final LocalDateTime enviadaEm;

    public MensagemChat(MensagemChatId id, UsuarioId autorId, String conteudo) {
        this.id = Objects.requireNonNull(id, "O id da mensagem e obrigatorio.");
        this.autorId = Objects.requireNonNull(autorId, "O autor da mensagem e obrigatorio.");
        this.conteudo = validarConteudo(conteudo);
        this.enviadaEm = LocalDateTime.now();
    }

    public MensagemChatId getId() {
        return id;
    }

    public UsuarioId getAutorId() {
        return autorId;
    }

    public String getConteudo() {
        return conteudo;
    }

    public LocalDateTime getEnviadaEm() {
        return enviadaEm;
    }

    private static String validarConteudo(String conteudo) {
        if (conteudo == null || conteudo.isBlank()) {
            throw new IllegalArgumentException("O conteudo da mensagem e obrigatorio.");
        }
        return conteudo.trim();
    }
}
