package com.torneios.dominio.compartilhado.evento;

/**
 * @deprecated Use {@link EventoBarramento} em vez desta interface.
 */
@Deprecated
@FunctionalInterface
public interface PublicadorEvento {

    void publicar(EventoDominio evento);
}
