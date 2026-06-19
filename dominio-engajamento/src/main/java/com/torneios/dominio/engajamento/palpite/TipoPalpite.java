package com.torneios.dominio.engajamento.palpite;

public enum TipoPalpite {
    VENCEDOR_PARTIDA(25),
    CAMPEAO_TORNEIO(100),
    ARTILHEIRO_TORNEIO(75),
    LIDER_ASSISTENCIAS_TORNEIO(75);

    private final int xpPorAcerto;

    TipoPalpite(int xpPorAcerto) {
        this.xpPorAcerto = xpPorAcerto;
    }

    public int getXpPorAcerto() {
        return xpPorAcerto;
    }
}


