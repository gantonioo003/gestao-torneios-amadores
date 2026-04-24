package com.torneios.dominio.torneio.organizador;

import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.torneio.torneio.Torneio;

public class OrganizadorTorneioServico {

    public boolean ehOrganizador(Torneio torneio, UsuarioId usuarioId) {
        if (torneio == null) {
            throw new IllegalArgumentException("O torneio e obrigatorio.");
        }
        return usuarioId != null && torneio.getOrganizadorId().equals(usuarioId);
    }

    public void validarPermissao(Torneio torneio, UsuarioId usuarioId) {
        if (!ehOrganizador(torneio, usuarioId)) {
            throw new OperacaoNaoPermitidaException("Apenas o organizador do torneio pode executar esta operacao.");
        }
    }
}
