package com.torneios.dominio.competicao.escalacao;

public enum TipoVisualizacaoEscalacao {
    MESA_TATICA,
    LISTA_TITULARES,
    LISTA_COMPLETA;

    public boolean usaMesaTatica() {
        return this == MESA_TATICA;
    }
}
