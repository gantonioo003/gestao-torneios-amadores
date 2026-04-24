package com.torneios.dominio.compartilhado.jogador;

public record JogadorId(long valor) {

    public JogadorId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id do jogador deve ser maior que zero.");
        }
    }
}
