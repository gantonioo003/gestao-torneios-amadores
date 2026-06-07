package com.torneios.dominio.torneio.torneio;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public class ConsultaElegibilidadeComCache implements ConsultaElegibilidadeParticipanteTorneio {

    private final ConsultaElegibilidadeParticipanteTorneio delegado;
    private final Map<TimeId, Boolean> cacheExistencia = new HashMap<>();
    private final Map<TimeId, Boolean> cacheTecnico = new HashMap<>();
    private final Map<TimeId, Integer> cacheJogadores = new HashMap<>();

    public ConsultaElegibilidadeComCache(ConsultaElegibilidadeParticipanteTorneio delegado) {
        this.delegado = Objects.requireNonNull(delegado,
                "O delegado de elegibilidade nao pode ser nulo.");
    }

    @Override
    public boolean timeExiste(TimeId timeId) {
        return cacheExistencia.computeIfAbsent(timeId, delegado::timeExiste);
    }

    @Override
    public boolean timePossuiTecnico(TimeId timeId) {
        return cacheTecnico.computeIfAbsent(timeId, delegado::timePossuiTecnico);
    }

    @Override
    public int quantidadeJogadores(TimeId timeId) {
        return cacheJogadores.computeIfAbsent(timeId, delegado::quantidadeJogadores);
    }

    @Override
    public void vincularTimeAoTorneio(TimeId timeId, TorneioId torneioId) {
        invalidarCache(timeId);
        delegado.vincularTimeAoTorneio(timeId, torneioId);
    }

    @Override
    public void removerVinculoDoTimeAoTorneio(TimeId timeId, TorneioId torneioId) {
        invalidarCache(timeId);
        delegado.removerVinculoDoTimeAoTorneio(timeId, torneioId);
    }

    public void invalidarCache(TimeId timeId) {
        cacheExistencia.remove(timeId);
        cacheTecnico.remove(timeId);
        cacheJogadores.remove(timeId);
    }

    public void limparCache() {
        cacheExistencia.clear();
        cacheTecnico.clear();
        cacheJogadores.clear();
    }
}
