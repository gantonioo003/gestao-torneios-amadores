package com.torneios.dominio.compartilhado.enumeracao;

public enum FormatoEquipe {
    TRES_POR_TRES(3),
    CINCO_POR_CINCO(5),
    SETE_POR_SETE(7),
    ONZE_POR_ONZE(11);

    private final int quantidadeJogadores;

    FormatoEquipe(int quantidadeJogadores) {
        this.quantidadeJogadores = quantidadeJogadores;
    }

    public int getQuantidadeJogadores() {
        return quantidadeJogadores;
    }
}
