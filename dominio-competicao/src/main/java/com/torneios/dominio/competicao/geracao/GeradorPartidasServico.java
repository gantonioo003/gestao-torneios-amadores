package com.torneios.dominio.competicao.geracao;

import java.util.ArrayList;
import java.util.List;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.rodada.Rodada;

public class GeradorPartidasServico {

    public List<Partida> gerarPontosCorridos(TorneioId torneioId, List<TimeId> participantes) {
        validarParticipantes(participantes);

        List<Partida> partidas = new ArrayList<>();
        long sequencia = 1L;

        for (int i = 0; i < participantes.size(); i++) {
            for (int j = i + 1; j < participantes.size(); j++) {
                partidas.add(new Partida(new PartidaId(sequencia++), torneioId, participantes.get(i), participantes.get(j)));
            }
        }

        return partidas;
    }

    public List<Partida> gerarMataMata(TorneioId torneioId, List<TimeId> participantes) {
        validarParticipantes(participantes);

        List<Partida> partidas = new ArrayList<>();
        long sequencia = 1L;

        for (int i = 0; i < participantes.size(); i += 2) {
            if (i + 1 >= participantes.size()) {
                break;
            }
            partidas.add(new Partida(new PartidaId(sequencia++), torneioId, participantes.get(i), participantes.get(i + 1)));
        }

        return partidas;
    }

    public List<Rodada> distribuirEmRodadas(TorneioId torneioId, List<Partida> partidas) {
        List<Rodada> rodadas = new ArrayList<>();
        int numeroRodada = 1;

        for (Partida partida : partidas) {
            Rodada rodada = new Rodada(torneioId, numeroRodada++);
            rodada.adicionarPartida(partida.getId());
            rodadas.add(rodada);
        }

        return rodadas;
    }

    private void validarParticipantes(List<TimeId> participantes) {
        if (participantes == null || participantes.size() < 2) {
            throw new IllegalArgumentException("E necessario ter ao menos dois participantes para gerar partidas.");
        }
    }
}
