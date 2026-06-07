package com.torneios.dominio.competicao.geracao;

import java.util.List;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.Partida;

public abstract class GeradorPartidas {

    public final List<Partida> gerar(TorneioId torneioId,
                                     int quantidadeJogadoresPorEquipe,
                                     List<TimeId> participantes) {
        validarParticipantes(participantes);
        return criarPartidas(torneioId, quantidadeJogadoresPorEquipe, participantes);
    }

    protected void validarParticipantes(List<TimeId> participantes) {
        if (participantes == null || participantes.size() < 2) {
            throw new IllegalArgumentException(
                    "E necessario ter ao menos dois participantes para gerar partidas.");
        }
    }

    protected abstract List<Partida> criarPartidas(TorneioId torneioId,
                                                    int quantidadeJogadoresPorEquipe,
                                                    List<TimeId> participantes);

    protected static PartidaId proximoId(long[] sequencia) {
        return new PartidaId(sequencia[0]++);
    }
}
