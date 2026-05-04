package com.torneios.dominio.estatisticas.desempenho;

import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.torneio.TorneioId;

public class HistoricoEstatisticoTorneio {

    private final TorneioId torneioId;
    private final int numeroEdicao;
    private final List<EstatisticaJogador> estatisticasArquivadas;

    public HistoricoEstatisticoTorneio(TorneioId torneioId,
                                       int numeroEdicao,
                                       List<EstatisticaJogador> estatisticasArquivadas) {
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio do historico estatistico e obrigatorio.");
        if (numeroEdicao <= 0) {
            throw new IllegalArgumentException("O numero da edicao deve ser maior que zero.");
        }
        this.numeroEdicao = numeroEdicao;
        this.estatisticasArquivadas = List.copyOf(Objects.requireNonNull(estatisticasArquivadas,
                "As estatisticas arquivadas sao obrigatorias."));
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public int getNumeroEdicao() {
        return numeroEdicao;
    }

    public List<EstatisticaJogador> getEstatisticasArquivadas() {
        return estatisticasArquivadas;
    }
}
