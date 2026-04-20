package com.torneios.dominio.participacao.acesso;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class AutenticacaoServico {

    public boolean estaAutenticado(UsuarioId usuarioId) {
        return usuarioId != null;
    }

    public void exigirAutenticacao(UsuarioId usuarioId) {
        if (!estaAutenticado(usuarioId)) {
            throw new IllegalStateException("O usuario precisa estar autenticado para executar esta operacao.");
        }
    }
}
