package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashSet;
import java.util.Set;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.responsavel.ConsultaUsuario;

public class ConsultaUsuarioMemoria implements ConsultaUsuario {

    private final Set<UsuarioId> usuarios = new HashSet<>();

    public void registrar(UsuarioId usuarioId) {
        usuarios.add(usuarioId);
    }

    public void limpar() {
        usuarios.clear();
    }

    @Override
    public boolean existe(UsuarioId usuarioId) {
        return usuarioId != null && usuarios.contains(usuarioId);
    }
}
