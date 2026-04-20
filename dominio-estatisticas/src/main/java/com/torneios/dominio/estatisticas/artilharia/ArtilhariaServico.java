package com.torneios.dominio.estatisticas.artilharia;

import java.util.Comparator;
import java.util.List;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaJogador;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaServico;

public class ArtilhariaServico {

    private final EstatisticaServico estatisticaServico;

    public ArtilhariaServico(EstatisticaServico estatisticaServico) {
        this.estatisticaServico = estatisticaServico;
    }

    public List<EstatisticaJogador> gerarRanking(TorneioId torneioId) {
        return estatisticaServico.consolidarTorneio(torneioId)
                .getEstatisticasJogadores()
                .stream()
                .sorted(Comparator.comparingInt(EstatisticaJogador::getGols).reversed()
                        .thenComparingLong(EstatisticaJogador::getJogadorId))
                .toList();
    }
}
