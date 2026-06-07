package com.torneios.dominio.compartilhado.evento;

@Deprecated
@FunctionalInterface
public interface PublicadorEvento {

    void publicar(EventoDominio evento);
}
