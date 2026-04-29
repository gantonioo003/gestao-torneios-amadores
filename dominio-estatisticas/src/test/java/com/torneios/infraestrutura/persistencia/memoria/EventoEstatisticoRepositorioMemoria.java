package com.torneios.infraestrutura.persistencia.memoria;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.evento.EventoEstatistico;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoRepositorio;

public class EventoEstatisticoRepositorioMemoria implements EventoEstatisticoRepositorio {

    private final Map<Long, EventoEstatistico> dados = new LinkedHashMap<>();

    @Override
    public void salvar(EventoEstatistico eventoEstatistico) {
        dados.put(eventoEstatistico.getId(), eventoEstatistico);
    }

    @Override
    public Optional<EventoEstatistico> buscarPorId(long eventoId) {
        return Optional.ofNullable(dados.get(eventoId));
    }

    @Override
    public void remover(long eventoId) {
        dados.remove(eventoId);
    }

    @Override
    public List<EventoEstatistico> listarTodos() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public List<EventoEstatistico> listarPorTorneio(TorneioId torneioId) {
        return dados.values().stream()
                .filter(evento -> evento.getTorneioId().equals(torneioId))
                .toList();
    }

    @Override
    public List<EventoEstatistico> listarPorPartida(PartidaId partidaId) {
        return dados.values().stream()
                .filter(evento -> evento.getPartidaId().equals(partidaId))
                .toList();
    }

    @Override
    public List<EventoEstatistico> listarPorJogadorNoTorneio(JogadorId jogadorId, TorneioId torneioId) {
        return dados.values().stream()
                .filter(evento -> evento.getJogadorId().equals(jogadorId) && evento.getTorneioId().equals(torneioId))
                .toList();
    }

    public void limpar() {
        dados.clear();
    }
}
