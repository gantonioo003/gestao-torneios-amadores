package com.torneios.dominio.compartilhado.time;

import java.util.Objects;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class Time {

    private final TimeId id;
    private String nome;
    private UsuarioId responsavel;

    public Time(TimeId id, String nome, UsuarioId responsavel) {
        this.id = Objects.requireNonNull(id, "O id do time e obrigatorio.");
        this.nome = validarNome(nome);
        this.responsavel = Objects.requireNonNull(responsavel, "O responsavel do time e obrigatorio.");
    }

    public TimeId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public UsuarioId getResponsavel() {
        return responsavel;
    }

    public void renomear(String novoNome) {
        this.nome = validarNome(novoNome);
    }

    public void alterarResponsavel(UsuarioId novoResponsavel) {
        this.responsavel = Objects.requireNonNull(novoResponsavel, "O novo responsavel e obrigatorio.");
    }

    private String validarNome(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome do time e obrigatorio.");
        }
        return valor.trim();
    }
}
