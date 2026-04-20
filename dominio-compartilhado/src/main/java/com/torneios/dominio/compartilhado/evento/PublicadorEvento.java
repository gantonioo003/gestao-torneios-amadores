package com.torneios.dominio.compartilhado.evento;

@FunctionalInterface
public interface PublicadorEvento {

    void publicar(EventoDominio evento);
}
