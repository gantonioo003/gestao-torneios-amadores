package com.torneios.dominio.compartilhado.usuario;

import java.util.Objects;

public class Usuario {

    private final UsuarioId id;
    private String nome;

    public Usuario(UsuarioId id, String nome) {
        this.id = Objects.requireNonNull(id, "O id do usuario e obrigatorio.");
        this.nome = validarNome(nome);
    }

    public UsuarioId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void renomear(String novoNome) {
        this.nome = validarNome(novoNome);
    }

    private String validarNome(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome do usuario e obrigatorio.");
        }
        return valor.trim();
    }
}
