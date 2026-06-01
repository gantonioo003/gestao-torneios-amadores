package com.torneios.dominio.participacao.profissional;

public record ProfissionalEsportivoId(long valor) {

    public ProfissionalEsportivoId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id do profissional deve ser maior que zero.");
        }
    }
}
