package com.torneios.dominio.estatisticas.artilharia;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaJogador;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaServico;
import com.torneios.dominio.estatisticas.nota.NotaMediaJogador;

public class ArtilhariaServico {

    private final EstatisticaServico estatisticaServico;

    public ArtilhariaServico(EstatisticaServico estatisticaServico) {
        this.estatisticaServico = estatisticaServico;
    }

    public List<EstatisticaJogador> gerarRanking(TorneioId torneioId) {
        Map<com.torneios.dominio.compartilhado.jogador.JogadorId, Double> medias = estatisticaServico
                .listarMelhoresMediasDeNota(torneioId, 1)
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        NotaMediaJogador::jogadorId,
                        NotaMediaJogador::media,
                        (primeiro, segundo) -> primeiro));
        return estatisticaServico.consolidarTorneio(torneioId)
                .getEstatisticasJogadores()
                .stream()
                .sorted(Comparator.comparingInt(EstatisticaJogador::getGols).reversed()
                        .thenComparingInt(EstatisticaJogador::getAssistencias).reversed()
                        .thenComparing((EstatisticaJogador estatisticaJogador) ->
                                medias.getOrDefault(estatisticaJogador.getJogadorId(), 0.0), Comparator.reverseOrder())
                        .thenComparingInt(EstatisticaJogador::getCartoesVermelhos)
                        .thenComparingInt(EstatisticaJogador::getCartoesAmarelos)
                        .thenComparingLong(estatisticaJogador -> estatisticaJogador.getJogadorId().valor()))
                .toList();
    }
}
