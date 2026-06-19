package com.torneios.dominio.estatisticas.desempenho;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.evento.EventoEstatistico;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoRepositorio;
import com.torneios.dominio.estatisticas.nota.CalculadoraNotaEstatistica;
import com.torneios.dominio.estatisticas.nota.NotaMediaJogador;
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
        Map<JogadorId, Double> medias = mediasDeNotaPorJogador(torneioId);
        return estatisticaTorneioComoLista(consolidarTorneio(torneioId)).stream()
                .sorted(Comparator.comparingInt(EstatisticaJogador::getAssistencias).reversed()
                        .thenComparing(Comparator.comparingInt(EstatisticaJogador::getGols).reversed())
                        .thenComparing((EstatisticaJogador estatisticaJogador) ->
                                medias.getOrDefault(estatisticaJogador.getJogadorId(), 0.0), Comparator.reverseOrder())
                        .thenComparingInt(EstatisticaJogador::getCartoesVermelhos)
                        .thenComparingInt(EstatisticaJogador::getCartoesAmarelos)
                        .thenComparingLong(estatisticaJogador -> estatisticaJogador.getJogadorId().valor()))
                .toList();
    }

    public List<EventoEstatistico> obterHistoricoJogador(TorneioId torneioId, JogadorId jogadorId) {
        Objects.requireNonNull(torneioId, "O torneio do historico e obrigatorio.");
        Objects.requireNonNull(jogadorId, "O jogador do historico e obrigatorio.");
        return eventoEstatisticoRepositorio.listarPorJogadorNoTorneio(jogadorId, torneioId);
    }

    public HistoricoEstatisticoTorneio arquivarEstatisticasDaEdicao(TorneioId torneioId, int numeroEdicao) {
        return consolidarTorneio(torneioId).arquivarEdicao(numeroEdicao);
    }

    public HistoricoEstatisticoTorneio recalcularEdicaoFechada(TorneioId torneioId, int numeroEdicao) {
        Objects.requireNonNull(torneioId, "O torneio da recalculacao e obrigatorio.");
        if (numeroEdicao <= 0) {
            throw new IllegalArgumentException("O numero da edicao deve ser positivo.");
        }
        return consolidarTorneio(torneioId).arquivarEdicao(numeroEdicao);
    }

    public List<NotaMediaJogador> listarMelhoresMediasDeNota(TorneioId torneioId, int minimoPartidas) {
        if (minimoPartidas <= 0) {
            throw new IllegalArgumentException("A quantidade minima de partidas deve ser positiva.");
        }

        Map<JogadorId, List<NotaEstatistica>> notasPorJogador = new LinkedHashMap<>();
        for (var entrada : eventosPorPartida(torneioId).entrySet()) {
            PartidaId partidaId = entrada.getKey();
            List<EventoEstatistico> eventosDaPartida = entrada.getValue();
            Set<JogadorId> jogadores = eventosDaPartida.stream()
                    .map(EventoEstatistico::getJogadorId)
                    .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
            for (JogadorId jogadorId : jogadores) {
                calcularNotaJogador(torneioId, partidaId, jogadorId)
                        .ifPresent(nota -> notasPorJogador.computeIfAbsent(jogadorId, chave -> new java.util.ArrayList<>())
                                .add(nota));
            }
        }

        return notasPorJogador.entrySet().stream()
                .filter(entrada -> entrada.getValue().size() >= minimoPartidas)
                .map(entrada -> converterMedia(torneioId, entrada.getKey(), entrada.getValue()))
                .sorted(Comparator.comparingDouble(NotaMediaJogador::media).reversed()
                        .thenComparing(Comparator.comparingInt(NotaMediaJogador::partidasConsideradas).reversed())
                        .thenComparingLong(notaMediaJogador -> notaMediaJogador.jogadorId().valor()))
                .toList();
    }

    public Optional<NotaMediaJogador> obterMelhorNotaComElegibilidade(TorneioId torneioId, int minimoPartidas) {
        return listarMelhoresMediasDeNota(torneioId, minimoPartidas).stream().findFirst();
    }

    public EstatisticaCarreiraJogador obterEstatisticaCarreiraJogador(JogadorId jogadorId) {
        Objects.requireNonNull(jogadorId, "O jogador da estatistica de carreira e obrigatorio.");
        List<EventoEstatistico> eventos = eventoEstatisticoRepositorio.listarTodos().stream()
                .filter(evento -> evento.getJogadorId().equals(jogadorId))
                .toList();
        int gols = (int) eventos.stream().filter(evento -> evento.getTipo() == com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico.GOL).count();
        int assistencias = (int) eventos.stream().filter(evento -> evento.getTipo() == com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico.ASSISTENCIA).count();
        int cartoesAmarelos = (int) eventos.stream().filter(evento -> evento.getTipo() == com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico.CARTAO_AMARELO).count();
        int cartoesVermelhos = (int) eventos.stream().filter(evento -> evento.getTipo() == com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico.CARTAO_VERMELHO).count();
        long torneiosComEventos = eventos.stream()
                .map(EventoEstatistico::getTorneioId)
                .distinct()
                .count();
        return new EstatisticaCarreiraJogador(
                jogadorId,
                gols,
                assistencias,
                cartoesAmarelos,
                cartoesVermelhos,
                (int) torneiosComEventos);
    }

    private List<EstatisticaJogador> estatisticaTorneioComoLista(EstatisticaTorneio estatisticaTorneio) {
        return estatisticaTorneio.getEstatisticasJogadores().stream().toList();
    }

    private Map<PartidaId, List<EventoEstatistico>> eventosPorPartida(TorneioId torneioId) {
        return eventoEstatisticoRepositorio.listarPorTorneio(torneioId).stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        EventoEstatistico::getPartidaId,
                        LinkedHashMap::new,
                        java.util.stream.Collectors.toList()));
    }

    private Map<JogadorId, Double> mediasDeNotaPorJogador(TorneioId torneioId) {
        return listarMelhoresMediasDeNota(torneioId, 1).stream()
                .collect(java.util.stream.Collectors.toMap(
                        NotaMediaJogador::jogadorId,
                        NotaMediaJogador::media,
                        (primeiro, segundo) -> primeiro,
                        LinkedHashMap::new));
    }

    private NotaMediaJogador converterMedia(TorneioId torneioId,
                                            JogadorId jogadorId,
                                            List<NotaEstatistica> notas) {
        double media = notas.stream().mapToDouble(NotaEstatistica::valor).average().orElse(0.0);
        return new NotaMediaJogador(torneioId, jogadorId, media, notas.size());
    }
}
