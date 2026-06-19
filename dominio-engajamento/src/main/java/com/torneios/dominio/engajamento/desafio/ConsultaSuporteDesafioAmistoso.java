package com.torneios.dominio.engajamento.desafio;

import java.util.Optional;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ConsultaSuporteDesafioAmistoso {

    boolean usuarioEstaAutenticado(UsuarioId usuarioId);

    boolean usuarioPodeGerenciarTimes(UsuarioId usuarioId);

    boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId);

    Optional<UsuarioId> buscarResponsavelDoTime(TimeId timeId);

    boolean timesPodemSeDesafiar(TimeId timeDesafianteId, TimeId timeDesafiadoId);
}
