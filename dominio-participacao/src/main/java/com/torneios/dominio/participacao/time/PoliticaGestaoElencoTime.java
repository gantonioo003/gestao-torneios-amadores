package com.torneios.dominio.participacao.time;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

@FunctionalInterface
public interface PoliticaGestaoElencoTime {

    boolean podeGerenciar(Time time, UsuarioId usuarioId);
}
