package com.torneios.dominio.participacao.responsavel;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ConsultaUsuario {

    boolean existe(UsuarioId usuarioId);
}
