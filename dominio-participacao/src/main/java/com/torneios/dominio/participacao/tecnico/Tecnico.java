package com.torneios.dominio.participacao.tecnico;

import java.util.Objects;

public class Tecnico {

    private final long id;
    private String nome;

    public Tecnico(long id, String nome) {
        if (id <= 0) {
            throw new IllegalArgumentException("O id do tecnico deve ser maior que zero.");
        }
        this.id = id;
        this.nome = validarNome(nome);
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void renomear(String novoNome) {
        this.nome = validarNome(novoNome);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tecnico tecnico)) {
            return false;
        }
        return id == tecnico.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private String validarNome(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome do tecnico e obrigatorio.");
        }
        return valor.trim();
    }
}
