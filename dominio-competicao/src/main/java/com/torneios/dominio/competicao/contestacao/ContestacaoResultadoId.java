package com.torneios.dominio.competicao.contestacao;

public record ContestacaoResultadoId(long valor) {

    public ContestacaoResultadoId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id da contestacao deve ser maior que zero.");
        }
    }
}
