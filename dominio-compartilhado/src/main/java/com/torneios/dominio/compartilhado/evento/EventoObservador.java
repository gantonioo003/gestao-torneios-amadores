package com.torneios.dominio.compartilhado.evento;

@FunctionalInterface
public interface EventoObservador<E> {

    void aoOcorrer(E evento);
}
