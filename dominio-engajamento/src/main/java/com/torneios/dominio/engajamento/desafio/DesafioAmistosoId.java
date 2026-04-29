package com.torneios.dominio.engajamento.desafio;

public record DesafioAmistosoId(long valor) {

    public DesafioAmistosoId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id do desafio deve ser maior que zero.");
        }
    }
}
