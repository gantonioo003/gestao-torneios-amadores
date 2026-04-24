package com.torneios.dominio.participacao.acesso;

import java.util.Objects;

import com.torneios.dominio.compartilhado.torneio.TorneioId;

public record TorneioDisponivel(TorneioId id, String nome, boolean aceitaSolicitacoes) {

    public TorneioDisponivel {
        Objects.requireNonNull(id, "O id do torneio disponivel e obrigatorio.");
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do torneio disponivel e obrigatorio.");
        }
        nome = nome.trim();
    }
}
