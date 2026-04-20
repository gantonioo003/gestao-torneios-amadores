package com.torneios.dominio.compartilhado.enumeracao;

public enum FormatoTorneio {
    MATA_MATA,
    FASE_DE_GRUPOS_COM_MATA_MATA,
    PONTOS_CORRIDOS,
    FINAL_UNICA;

    public int quantidadeMinimaParticipantes() {
        return switch (this) {
            case FINAL_UNICA, MATA_MATA, PONTOS_CORRIDOS -> 2;
            case FASE_DE_GRUPOS_COM_MATA_MATA -> 4;
        };
    }
}
