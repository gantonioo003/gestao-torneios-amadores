package com.torneios.dominio.compartilhado.time;

public record TimeId(long valor) {

    public TimeId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id do time deve ser maior que zero.");
        }
    }
}
