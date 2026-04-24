package com.torneios.dominio.compartilhado.tecnico;

import java.util.Objects;

import com.torneios.dominio.compartilhado.time.TimeId;

public class Tecnico {

    private final TecnicoId id;
    private String nome;
    private TimeId timeId;

    public Tecnico(TecnicoId id, String nome, TimeId timeId) {
        this.id = Objects.requireNonNull(id, "O id do tecnico e obrigatorio.");
        this.nome = validarNome(nome);
        this.timeId = Objects.requireNonNull(timeId, "O time do tecnico e obrigatorio.");
    }

    public TecnicoId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public TimeId getTimeId() {
        return timeId;
    }

    public void renomear(String novoNome) {
        this.nome = validarNome(novoNome);
    }

    public void vincularAoTime(TimeId novoTimeId) {
        this.timeId = Objects.requireNonNull(novoTimeId, "O time do tecnico e obrigatorio.");
    }

    private String validarNome(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome do tecnico e obrigatorio.");
        }
        return valor.trim();
    }
}
