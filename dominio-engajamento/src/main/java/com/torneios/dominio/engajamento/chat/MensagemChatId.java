package com.torneios.dominio.engajamento.chat;

public record MensagemChatId(long valor) {

    public MensagemChatId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id da mensagem deve ser maior que zero.");
        }
    }
}
