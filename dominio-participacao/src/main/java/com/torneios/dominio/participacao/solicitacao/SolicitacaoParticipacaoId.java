package com.torneios.dominio.participacao.solicitacao;

public record SolicitacaoParticipacaoId(long valor) {

    public SolicitacaoParticipacaoId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id da solicitacao deve ser maior que zero.");
        }
    }
}
