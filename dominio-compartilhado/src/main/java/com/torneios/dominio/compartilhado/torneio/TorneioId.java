package com.torneios.dominio.compartilhado.torneio;

public record TorneioId(long valor) {

    public TorneioId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id do torneio deve ser maior que zero.");
        }
    }
}
