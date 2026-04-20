package com.torneios.dominio.compartilhado.evento;

import java.time.Instant;
import java.util.Objects;

public abstract class EventoDominio {

    private final String nome;
    private final Instant ocorridoEm;

    protected EventoDominio(String nome) {
        this.nome = Objects.requireNonNull(nome, "O nome do evento e obrigatorio.");
        this.ocorridoEm = Instant.now();
    }

    public String getNome() {
        return nome;
    }

    public Instant getOcorridoEm() {
        return ocorridoEm;
    }
}
