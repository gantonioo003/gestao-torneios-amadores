package com.torneios.dominio.compartilhado.usuario;

public record UsuarioId(long valor) {

    public UsuarioId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id do usuario deve ser maior que zero.");
        }
    }
}
