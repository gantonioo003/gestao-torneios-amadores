package com.torneios.dominio.engajamento.desafio;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ConsultaSuporteDesafioAmistoso {

    boolean usuarioEstaAutenticado(UsuarioId usuarioId);

    boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId);

    boolean timesPodemSeDesafiar(TimeId timeDesafianteId, TimeId timeDesafiadoId);
}
