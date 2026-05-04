package com.torneios.dominio.engajamento.chat;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ConsultaSuporteChat {

    boolean usuarioEstaAutenticado(UsuarioId usuarioId);

    boolean usuarioExiste(UsuarioId usuarioId);
}
