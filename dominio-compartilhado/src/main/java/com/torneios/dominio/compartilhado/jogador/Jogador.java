package com.torneios.dominio.compartilhado.jogador;

import java.util.Objects;

import com.torneios.dominio.compartilhado.time.TimeId;

public class Jogador {

    private final JogadorId id;
    private String nome;
    private TimeId timeId;

    public Jogador(JogadorId id, String nome, TimeId timeId) {
        this.id = Objects.requireNonNull(id, "O id do jogador e obrigatorio.");
        this.nome = validarNome(nome);
        this.timeId = Objects.requireNonNull(timeId, "O time do jogador e obrigatorio.");
    }

    public JogadorId getId() {
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
        this.timeId = Objects.requireNonNull(novoTimeId, "O time do jogador e obrigatorio.");
    }

    private String validarNome(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome do jogador e obrigatorio.");
        }
        return valor.trim();
    }
}
