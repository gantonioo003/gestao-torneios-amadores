package com.torneios.dominio.estatisticas.comparacao;

import java.util.Objects;

public class DesempenhoComparado {

    private final String rotulo;
    private final int gols;
    private final int assistencias;
    private final int cartoesAmarelos;
    private final int cartoesVermelhos;
    private final int partidasComEventos;
    private final int posicaoRanking;
    private final double pontuacaoComparativa;

    public DesempenhoComparado(String rotulo,
                               int gols,
                               int assistencias,
                               int cartoesAmarelos,
                               int cartoesVermelhos,
                               int partidasComEventos,
                               int posicaoRanking,
                               double pontuacaoComparativa) {
        this.rotulo = Objects.requireNonNull(rotulo, "O rotulo do desempenho comparado e obrigatorio.");
        this.gols = gols;
        this.assistencias = assistencias;
        this.cartoesAmarelos = cartoesAmarelos;
        this.cartoesVermelhos = cartoesVermelhos;
        this.partidasComEventos = partidasComEventos;
        this.posicaoRanking = posicaoRanking;
        this.pontuacaoComparativa = pontuacaoComparativa;
    }

    public String getRotulo() {
        return rotulo;
    }

    public int getGols() {
        return gols;
    }

    public int getAssistencias() {
        return assistencias;
    }

    public int getCartoesAmarelos() {
        return cartoesAmarelos;
    }

    public int getCartoesVermelhos() {
        return cartoesVermelhos;
    }

    public int getTotalCartoes() {
        return cartoesAmarelos + cartoesVermelhos;
    }

    public int getPartidasComEventos() {
        return partidasComEventos;
    }

    public int getPosicaoRanking() {
        return posicaoRanking;
    }

    public double getPontuacaoComparativa() {
        return pontuacaoComparativa;
    }

    public boolean possuiDados() {
        return partidasComEventos > 0;
    }
}
