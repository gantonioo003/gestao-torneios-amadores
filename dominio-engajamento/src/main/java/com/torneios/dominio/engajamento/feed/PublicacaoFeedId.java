package com.torneios.dominio.engajamento.feed;

public record PublicacaoFeedId(long valor) {

    public PublicacaoFeedId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id da publicacao do feed deve ser maior que zero.");
        }
    }
}
