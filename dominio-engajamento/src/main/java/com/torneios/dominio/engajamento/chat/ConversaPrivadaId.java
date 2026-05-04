package com.torneios.dominio.engajamento.chat;

public record ConversaPrivadaId(long valor) {

    public ConversaPrivadaId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id da conversa deve ser maior que zero.");
        }
    }
}
