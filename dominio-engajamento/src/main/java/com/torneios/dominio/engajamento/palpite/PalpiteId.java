package com.torneios.dominio.engajamento.palpite;

public record PalpiteId(long valor) {

    public PalpiteId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id do palpite deve ser maior que zero.");
        }
    }
}
