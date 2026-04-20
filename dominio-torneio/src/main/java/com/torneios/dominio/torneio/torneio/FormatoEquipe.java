package com.torneios.dominio.torneio.torneio;

public record FormatoEquipe(int quantidadeJogadores) {

    public FormatoEquipe {
        if (quantidadeJogadores <= 0) {
            throw new IllegalArgumentException("A quantidade de jogadores por equipe deve ser maior que zero.");
        }
    }
}
