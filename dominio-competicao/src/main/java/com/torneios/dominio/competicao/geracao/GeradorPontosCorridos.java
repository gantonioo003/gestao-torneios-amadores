package com.torneios.dominio.competicao.geracao;

import java.util.ArrayList;
import java.util.List;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.Partida;

public class GeradorPontosCorridos extends GeradorPartidas {

    @Override
    protected List<Partida> criarPartidas(TorneioId torneioId,
                                          int quantidadeJogadoresPorEquipe,
                                          List<TimeId> participantes) {
        List<Partida> partidas = new ArrayList<>();
        long[] sequencia = {1L};

        for (int i = 0; i < participantes.size(); i++) {
            for (int j = i + 1; j < participantes.size(); j++) {
                partidas.add(new Partida(
                        proximoId(sequencia),
                        torneioId,
                        participantes.get(i),
                        participantes.get(j),
                        "Pontos corridos",
                        quantidadeJogadoresPorEquipe));
            }
        }

        return partidas;
    }
}
