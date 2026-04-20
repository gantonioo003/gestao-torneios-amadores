package com.torneios.dominio.competicao.classificacao;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.Partida;

public class ClassificacaoServico {

    public List<Classificacao> gerar(TorneioId torneioId, Collection<TimeId> participantes, List<Partida> partidas) {
        if (torneioId == null) {
            throw new IllegalArgumentException("O torneio da classificacao e obrigatorio.");
        }

        Map<TimeId, Classificacao> classificacoes = new LinkedHashMap<>();
        participantes.forEach(timeId -> classificacoes.put(timeId, new Classificacao(torneioId, timeId)));

        for (Partida partida : partidas) {
            if (!partida.estaEncerrada()) {
                continue;
            }

            Classificacao classificacaoMandante = classificacoes.computeIfAbsent(
                    partida.getMandante(),
                    timeId -> new Classificacao(torneioId, timeId));
            Classificacao classificacaoVisitante = classificacoes.computeIfAbsent(
                    partida.getVisitante(),
                    timeId -> new Classificacao(torneioId, timeId));

            classificacaoMandante.registrarComoMandante(partida.getResultado());
            classificacaoVisitante.registrarComoVisitante(partida.getResultado());
        }

        return classificacoes.values().stream()
                .sorted(Comparator.comparingInt(Classificacao::getPontos).reversed()
                        .thenComparingInt(Classificacao::getSaldoGols).reversed()
                        .thenComparingInt(Classificacao::getGolsPro).reversed()
                        .thenComparingLong(classificacao -> classificacao.getTimeId().valor()))
                .toList();
    }
}
