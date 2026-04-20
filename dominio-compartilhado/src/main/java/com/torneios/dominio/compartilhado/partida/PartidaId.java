package com.torneios.dominio.compartilhado.partida;

public record PartidaId(long valor) {

    public PartidaId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id da partida deve ser maior que zero.");
        }
    }
}
