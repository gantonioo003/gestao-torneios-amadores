package com.torneios.infraestrutura.persistencia.memoria;

import java.util.ArrayList;
import java.util.List;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.evento.EventoEstatistico;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoRepositorio;

public class EventoEstatisticoRepositorioMemoria implements EventoEstatisticoRepositorio {

    private final List<EventoEstatistico> dados = new ArrayList<>();

    @Override
    public void salvar(EventoEstatistico eventoEstatistico) {
        dados.add(eventoEstatistico);
    }

    @Override
    public List<EventoEstatistico> listarTodos() {
        return List.copyOf(dados);
    }

    @Override
    public List<EventoEstatistico> listarPorTorneio(TorneioId torneioId) {
        return dados.stream()
                .filter(evento -> evento.getTorneioId().equals(torneioId))
                .toList();
    }

    @Override
    public List<EventoEstatistico> listarPorPartida(PartidaId partidaId) {
        return dados.stream()
                .filter(evento -> evento.getPartidaId().equals(partidaId))
                .toList();
    }

    @Override
    public List<EventoEstatistico> listarPorJogadorNoTorneio(JogadorId jogadorId, TorneioId torneioId) {
        return dados.stream()
                .filter(evento -> evento.getJogadorId().equals(jogadorId) && evento.getTorneioId().equals(torneioId))
                .toList();
    }

    public void limpar() {
        dados.clear();
    }
}
