package com.torneios.dominio.estatisticas.evento;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public interface EventoEstatisticoRepositorio {

    void salvar(EventoEstatistico eventoEstatistico);

    Optional<EventoEstatistico> buscarPorId(long eventoId);

    void remover(long eventoId);

    List<EventoEstatistico> listarTodos();

    List<EventoEstatistico> listarPorTorneio(TorneioId torneioId);

    List<EventoEstatistico> listarPorPartida(PartidaId partidaId);

    List<EventoEstatistico> listarPorJogadorNoTorneio(JogadorId jogadorId, TorneioId torneioId);
}
