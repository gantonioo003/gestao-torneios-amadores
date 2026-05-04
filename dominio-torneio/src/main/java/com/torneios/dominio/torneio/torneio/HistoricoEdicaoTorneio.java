package com.torneios.dominio.torneio.torneio;

import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public class HistoricoEdicaoTorneio {

    private final TorneioId torneioId;
    private final int numeroEdicao;
    private final String nomeTorneio;
    private final List<TimeId> participantes;

    public HistoricoEdicaoTorneio(TorneioId torneioId,
                                  int numeroEdicao,
                                  String nomeTorneio,
                                  List<TimeId> participantes) {
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio do historico e obrigatorio.");
        if (numeroEdicao <= 0) {
            throw new IllegalArgumentException("O numero da edicao deve ser maior que zero.");
        }
        this.numeroEdicao = numeroEdicao;
        this.nomeTorneio = Objects.requireNonNull(nomeTorneio, "O nome do torneio historico e obrigatorio.");
        this.participantes = List.copyOf(Objects.requireNonNull(participantes,
                "Os participantes da edicao historica sao obrigatorios."));
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public int getNumeroEdicao() {
        return numeroEdicao;
    }

    public String getNomeTorneio() {
        return nomeTorneio;
    }

    public List<TimeId> getParticipantes() {
        return participantes;
    }
}
