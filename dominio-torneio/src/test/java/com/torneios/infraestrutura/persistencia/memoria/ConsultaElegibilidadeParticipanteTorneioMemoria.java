package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.torneio.torneio.ConsultaElegibilidadeParticipanteTorneio;

public class ConsultaElegibilidadeParticipanteTorneioMemoria implements ConsultaElegibilidadeParticipanteTorneio {

    private final Set<TimeId> timesExistentes = new HashSet<>();
    private final Set<TimeId> timesComTecnico = new HashSet<>();
    private final Map<TimeId, Integer> quantidadeJogadoresPorTime = new HashMap<>();
    private final Map<TimeId, Set<TorneioId>> vinculosTimeTorneio = new HashMap<>();

    public void adicionarTime(TimeId timeId, boolean possuiTecnico, int quantidadeJogadores) {
        timesExistentes.add(timeId);
        if (possuiTecnico) {
            timesComTecnico.add(timeId);
        }
        quantidadeJogadoresPorTime.put(timeId, quantidadeJogadores);
    }

    @Override
    public boolean timeExiste(TimeId timeId) {
        return timesExistentes.contains(timeId);
    }

    @Override
    public boolean timePossuiTecnico(TimeId timeId) {
        return timesComTecnico.contains(timeId);
    }

    @Override
    public int quantidadeJogadores(TimeId timeId) {
        return quantidadeJogadoresPorTime.getOrDefault(timeId, 0);
    }

    @Override
    public void vincularTimeAoTorneio(TimeId timeId, TorneioId torneioId) {
        vinculosTimeTorneio.computeIfAbsent(timeId, k -> new HashSet<>()).add(torneioId);
    }

    @Override
    public void removerVinculoDoTimeAoTorneio(TimeId timeId, TorneioId torneioId) {
        Set<TorneioId> torneios = vinculosTimeTorneio.get(timeId);
        if (torneios != null) {
            torneios.remove(torneioId);
        }
    }

    public void limpar() {
        timesExistentes.clear();
        timesComTecnico.clear();
        quantidadeJogadoresPorTime.clear();
        vinculosTimeTorneio.clear();
    }
}
