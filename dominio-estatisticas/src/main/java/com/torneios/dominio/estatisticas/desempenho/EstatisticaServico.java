package com.torneios.dominio.estatisticas.desempenho;

import java.util.List;
import java.util.Objects;

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

    public NotaEstatistica calcularNotaJogador(TorneioId torneioId, PartidaId partidaId, long jogadorId) {
        List<EventoEstatistico> eventos = eventoEstatisticoRepositorio.listarPorPartida(partidaId);
        return calculadoraNotaEstatistica.calcular(torneioId, partidaId, jogadorId, eventos);
    }

    public EstatisticaJogador obterEstatisticaJogador(TorneioId torneioId, long jogadorId) {
        EstatisticaTorneio estatisticaTorneio = consolidarTorneio(torneioId);
        return estatisticaTorneio.obterOuCriar(jogadorId);
    }
}
