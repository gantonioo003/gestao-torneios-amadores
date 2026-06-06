package com.torneios.dominio.compartilhado.evento;

public interface EventoBarramento {

    <E> void adicionar(EventoObservador<E> observador);

    <E> void postar(E evento);
}
