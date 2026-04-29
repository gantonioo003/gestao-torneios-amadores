package com.torneios.dominio.estatisticas.desempenho;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Comparator;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.evento.EventoEstatistico;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoRepositorio;
import com.torneios.dominio.estatisticas.nota.CalculadoraNotaEstatistica;
import com.torneios.dominio.estatisticas.nota.NotaEstatistica;

public class EstatisticaServico {

    private final EventoEstatisticoRepositorio eventoEstatisticoRepositorio;
    private final CalculadoraNotaEstatistica calculadoraNotaEstatistica;

    public EstatisticaServico(EventoEstatisticoRepositorio eventoEstatisticoRepositorio,
                              CalculadoraNotaEstatistica calculadoraNotaEstatistica) {
        this.eventoEstatisticoRepositorio = Objects.requireNonNull(eventoEstatisticoRepositorio,
                "O repositorio de eventos estatisticos e obrigatorio.");
        this.calculadoraNotaEstatistica = Objects.requireNonNull(calculadoraNotaEstatistica,
                "A calculadora de nota estatistica e obrigatoria.");
    }

    public EstatisticaTorneio consolidarTorneio(TorneioId torneioId) {
        List<EventoEstatistico> eventos = eventoEstatisticoRepositorio.listarPorTorneio(torneioId);
        EstatisticaTorneio estatisticaTorneio = new EstatisticaTorneio(torneioId);
        eventos.forEach(estatisticaTorneio::registrarEvento);
        return estatisticaTorneio;
    }

    public Optional<NotaEstatistica> calcularNotaJogador(TorneioId torneioId,
                                                         PartidaId partidaId,
                                                         JogadorId jogadorId) {
        List<EventoEstatistico> eventosDoJogadorNaPartida = eventoEstatisticoRepositorio.listarPorPartida(partidaId)
                .stream()
                .filter(evento -> evento.getJogadorId().equals(jogadorId))
                .toList();
        if (eventosDoJogadorNaPartida.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(calculadoraNotaEstatistica.calcular(
                torneioId,
                partidaId,
                jogadorId,
                eventosDoJogadorNaPartida));
    }

    public Optional<EstatisticaJogador> obterEstatisticaJogador(TorneioId torneioId, JogadorId jogadorId) {
        EstatisticaTorneio estatisticaTorneio = consolidarTorneio(torneioId);
        return estatisticaTorneio.obter(jogadorId);
    }

    public List<EstatisticaJogador> listarEstatisticasJogadores(TorneioId torneioId) {
        return estatisticaTorneioComoLista(consolidarTorneio(torneioId));
    }

    public List<EstatisticaJogador> listarLideresAssistencias(TorneioId torneioId) {
        return estatisticaTorneioComoLista(consolidarTorneio(torneioId)).stream()
                .sorted(Comparator.comparingInt(EstatisticaJogador::getAssistencias).reversed()
                        .thenComparingLong(estatisticaJogador -> estatisticaJogador.getJogadorId().valor()))
                .toList();
    }

    public List<EventoEstatistico> obterHistoricoJogador(TorneioId torneioId, JogadorId jogadorId) {
        Objects.requireNonNull(torneioId, "O torneio do historico e obrigatorio.");
        Objects.requireNonNull(jogadorId, "O jogador do historico e obrigatorio.");
        return eventoEstatisticoRepositorio.listarPorJogadorNoTorneio(jogadorId, torneioId);
    }

    private List<EstatisticaJogador> estatisticaTorneioComoLista(EstatisticaTorneio estatisticaTorneio) {
        return estatisticaTorneio.getEstatisticasJogadores().stream().toList();
    }
}
