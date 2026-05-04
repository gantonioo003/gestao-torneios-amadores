package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashSet;
import java.util.Set;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.ConsultaSuporteChat;

public class ConsultaSuporteChatMemoria implements ConsultaSuporteChat {

    private final Set<UsuarioId> usuariosAutenticados = new HashSet<>();
    private final Set<UsuarioId> usuariosCadastrados = new HashSet<>();

    public void cadastrarUsuario(UsuarioId usuarioId) {
        usuariosCadastrados.add(usuarioId);
    }

    public void autenticar(UsuarioId usuarioId) {
        usuariosCadastrados.add(usuarioId);
        usuariosAutenticados.add(usuarioId);
    }

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return usuariosAutenticados.contains(usuarioId);
    }

    @Override
    public boolean usuarioExiste(UsuarioId usuarioId) {
        return usuariosCadastrados.contains(usuarioId);
    }
}
