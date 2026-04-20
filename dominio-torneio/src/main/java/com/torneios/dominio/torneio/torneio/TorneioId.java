package com.torneios.dominio.torneio.torneio;

import java.util.Objects;

public record TorneioId(long valor) {

    public TorneioId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id do torneio deve ser maior que zero.");
        }
    }

    @Override
    public String toString() {
        return Objects.toString(valor);
    }
}
