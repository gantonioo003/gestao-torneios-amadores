package com.torneios.dominio.torneio.estrutura;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.torneios.dominio.compartilhado.time.TimeId;

public class Grupo {

    private final String nome;
    private final Set<TimeId> participantes;

    public Grupo(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do grupo e obrigatorio.");
        }
        this.nome = nome.trim();
        this.participantes = new LinkedHashSet<>();
    }

    public String getNome() {
        return nome;
    }

    public Set<TimeId> getParticipantes() {
        return Set.copyOf(participantes);
    }

    public void adicionarParticipante(TimeId timeId) {
        participantes.add(Objects.requireNonNull(timeId, "O participante do grupo e obrigatorio."));
    }
}
