package com.torneios.dominio.competicao.geracao;

import java.util.ArrayList;
import java.util.List;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.Partida;

public class GeradorMataMata extends GeradorPartidas {

    @Override
    protected List<Partida> criarPartidas(TorneioId torneioId,
                                          int quantidadeJogadoresPorEquipe,
                                          List<TimeId> participantes) {
        List<Partida> partidas = new ArrayList<>();
        long[] sequencia = {1L};

        for (int i = 0; i + 1 < participantes.size(); i += 2) {
            partidas.add(new Partida(
                    proximoId(sequencia),
                    torneioId,
                    participantes.get(i),
                    participantes.get(i + 1),
                    "Chaveamento",
                    quantidadeJogadoresPorEquipe));
        }

        return partidas;
    }
}
