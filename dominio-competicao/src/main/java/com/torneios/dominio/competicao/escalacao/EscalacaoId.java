package com.torneios.dominio.competicao.escalacao;

public record EscalacaoId(long valor) {

    public EscalacaoId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id da escalacao deve ser maior que zero.");
        }
    }
}
