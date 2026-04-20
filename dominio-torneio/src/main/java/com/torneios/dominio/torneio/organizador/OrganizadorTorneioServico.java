package com.torneios.dominio.torneio.organizador;

import com.torneios.dominio.torneio.torneio.Torneio;

public class OrganizadorTorneioServico {

    public boolean ehOrganizador(Torneio torneio, long usuarioId) {
        if (torneio == null) {
            throw new IllegalArgumentException("O torneio e obrigatorio.");
        }
        return torneio.getOrganizadorId() == usuarioId;
    }

    public void validarPermissao(Torneio torneio, long usuarioId) {
        if (!ehOrganizador(torneio, usuarioId)) {
            throw new IllegalStateException("Apenas o organizador do torneio pode executar esta operacao.");
        }
    }
}
