package com.torneios.dominio.estatisticas.desempenho;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.evento.EventoEstatistico;

public class EstatisticaTorneio {

    private final TorneioId torneioId;
    private final Map<JogadorId, EstatisticaJogador> estatisticasPorJogador;

    public EstatisticaTorneio(TorneioId torneioId) {
        if (torneioId == null) {
            throw new IllegalArgumentException("O torneio da estatistica agregada e obrigatorio.");
        }
        this.torneioId = torneioId;
        this.estatisticasPorJogador = new LinkedHashMap<>();
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public void registrarEvento(EventoEstatistico evento) {
        if (evento == null) {
            throw new IllegalArgumentException("O evento estatistico e obrigatorio.");
        }

        EstatisticaJogador estatistica = estatisticasPorJogador.computeIfAbsent(
                evento.getJogadorId(),
                jogadorId -> new EstatisticaJogador(torneioId, jogadorId));

        estatistica.registrarEvento(evento.getTipo());
    }

    public Optional<EstatisticaJogador> obter(JogadorId jogadorId) {
        return Optional.ofNullable(estatisticasPorJogador.get(jogadorId));
    }

    public Collection<EstatisticaJogador> getEstatisticasJogadores() {
        return java.util.List.copyOf(estatisticasPorJogador.values());
    }
}
