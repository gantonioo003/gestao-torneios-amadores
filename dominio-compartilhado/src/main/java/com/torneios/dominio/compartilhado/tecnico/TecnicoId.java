package com.torneios.dominio.compartilhado.tecnico;

public record TecnicoId(long valor) {

    public TecnicoId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id do tecnico deve ser maior que zero.");
        }
    }
}
