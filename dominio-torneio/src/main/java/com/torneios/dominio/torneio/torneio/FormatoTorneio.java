package com.torneios.dominio.torneio.torneio;

public enum FormatoTorneio {
    MATA_MATA,
    FASE_DE_GRUPOS_COM_MATA_MATA,
    PONTOS_CORRIDOS,
    FINAL_UNICA;

    public boolean exigeClassificacao() {
        return this == PONTOS_CORRIDOS || this == FASE_DE_GRUPOS_COM_MATA_MATA;
    }

    public boolean exigeChaveamento() {
        return this == MATA_MATA || this == FASE_DE_GRUPOS_COM_MATA_MATA || this == FINAL_UNICA;
    }

    public int quantidadeMinimaParticipantes() {
        return switch (this) {
            case FINAL_UNICA -> 2;
            case MATA_MATA -> 2;
            case PONTOS_CORRIDOS -> 2;
            case FASE_DE_GRUPOS_COM_MATA_MATA -> 4;
        };
    }
}
