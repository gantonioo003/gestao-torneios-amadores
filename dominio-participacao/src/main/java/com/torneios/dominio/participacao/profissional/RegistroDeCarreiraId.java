package com.torneios.dominio.participacao.profissional;

public record RegistroDeCarreiraId(long valor) {

    public RegistroDeCarreiraId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id do registro de carreira deve ser maior que zero.");
        }
    }
}
