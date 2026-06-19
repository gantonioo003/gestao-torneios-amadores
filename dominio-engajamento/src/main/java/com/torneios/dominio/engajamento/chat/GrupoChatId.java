package com.torneios.dominio.engajamento.chat;

public record GrupoChatId(long valor) {
    public GrupoChatId {
        if (valor <= 0) throw new IllegalArgumentException("O id do grupo deve ser positivo.");
    }
}
