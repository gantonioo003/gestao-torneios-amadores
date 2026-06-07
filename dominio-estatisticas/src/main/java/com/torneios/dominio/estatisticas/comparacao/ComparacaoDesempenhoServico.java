package com.torneios.dominio.estatisticas.comparacao;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.evento.EventoEstatistico;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoRepositorio;

public class ComparacaoDesempenhoServico {

    private final EventoEstatisticoRepositorio eventoEstatisticoRepositorio;
    private final ComparativoDesempenhoRepositorio comparativoDesempenhoRepositorio;
    private final ConsultaComparacaoDesempenho consultaComparacaoDesempenho;

    public ComparacaoDesempenhoServico(EventoEstatisticoRepositorio eventoEstatisticoRepositorio,
                                       ComparativoDesempenhoRepositorio comparativoDesempenhoRepositorio,
                                       ConsultaComparacaoDesempenho consultaComparacaoDesempenho) {
        this.eventoEstatisticoRepositorio = Objects.requireNonNull(eventoEstatisticoRepositorio,
                "O repositorio de eventos estatisticos e obrigatorio.");
        this.comparativoDesempenhoRepositorio = Objects.requireNonNull(comparativoDesempenhoRepositorio,
                "O repositorio de comparativos de desempenho e obrigatorio.");
        this.consultaComparacaoDesempenho = Objects.requireNonNull(consultaComparacaoDesempenho,
                "A consulta de comparacao de desempenho e obrigatoria.");
    }

    public ComparativoDesempenho gerarComparativoJogadores(long comparativoId,
                                                           TorneioId torneioId,
                                                           JogadorId primeiroJogadorId,
                                                           JogadorId segundoJogadorId) {
        Objects.requireNonNull(torneioId, "O torneio da comparacao e obrigatorio.");
        Objects.requireNonNull(primeiroJogadorId, "O primeiro jogador da comparacao e obrigatorio.");
        Objects.requireNonNull(segundoJogadorId, "O segundo jogador da comparacao e obrigatorio.");
        if (primeiroJogadorId.equals(segundoJogadorId)) {
            throw new RegraDeNegocioException("A comparacao deve envolver jogadores diferentes.");
        }

        List<EventoEstatistico> eventos = eventosDoTorneio(torneioId);
        Map<JogadorId, DesempenhoAcumulado> ranking = acumularJogadores(eventos);

        DesempenhoComparado primeiro = montarDesempenho(
                consultaComparacaoDesempenho.nomeJogador(primeiroJogadorId),
                ranking.get(primeiroJogadorId),
                posicaoNoRanking(primeiroJogadorId, ranking));
        DesempenhoComparado segundo = montarDesempenho(
                consultaComparacaoDesempenho.nomeJogador(segundoJogadorId),
                ranking.get(segundoJogadorId),
                posicaoNoRanking(segundoJogadorId, ranking));

        exigirDadosComparaveis(primeiro, segundo);

        return new ComparativoDesempenho(
                comparativoId, torneioId, TipoComparativoDesempenho.JOGADORES, primeiro, segundo);
    }

    public ComparativoDesempenho gerarComparativoTimes(long comparativoId,
                                                       TorneioId torneioId,
                                                       TimeId primeiroTimeId,
                                                       TimeId segundoTimeId) {
        Objects.requireNonNull(torneioId, "O torneio da comparacao e obrigatorio.");
        Objects.requireNonNull(primeiroTimeId, "O primeiro time da comparacao e obrigatorio.");
        Objects.requireNonNull(segundoTimeId, "O segundo time da comparacao e obrigatorio.");
        if (primeiroTimeId.equals(segundoTimeId)) {
            throw new RegraDeNegocioException("A comparacao deve envolver times diferentes.");
        }

        List<EventoEstatistico> eventos = eventosDoTorneio(torneioId);
        Map<TimeId, DesempenhoAcumulado> ranking = acumularTimes(eventos);

        DesempenhoComparado primeiro = montarDesempenho(
                consultaComparacaoDesempenho.nomeTime(primeiroTimeId),
                ranking.get(primeiroTimeId),
                posicaoNoRanking(primeiroTimeId, ranking));
        DesempenhoComparado segundo = montarDesempenho(
                consultaComparacaoDesempenho.nomeTime(segundoTimeId),
                ranking.get(segundoTimeId),
                posicaoNoRanking(segundoTimeId, ranking));

        exigirDadosComparaveis(primeiro, segundo);

        return new ComparativoDesempenho(
                comparativoId, torneioId, TipoComparativoDesempenho.TIMES, primeiro, segundo);
    }

    public void salvarComparativo(ComparativoDesempenho comparativoDesempenho) {
        comparativoDesempenhoRepositorio.salvar(Objects.requireNonNull(comparativoDesempenho,
                "O comparativo de desempenho e obrigatorio para salvar."));
    }

    public List<ComparativoDesempenho> consultarComparativosSalvos(TorneioId torneioId) {
        Objects.requireNonNull(torneioId, "O torneio da consulta de comparativos e obrigatorio.");
        return comparativoDesempenhoRepositorio.listarPorTorneio(torneioId);
    }

    public ComparativoDesempenho atualizarComparativoSalvo(ComparativoDesempenho comparativoDesempenho) {
        Objects.requireNonNull(comparativoDesempenho, "O comparativo atualizado e obrigatorio.");
        if (comparativoDesempenhoRepositorio.buscarPorId(comparativoDesempenho.getId()).isEmpty()) {
            throw new RegraDeNegocioException("O comparativo salvo nao foi encontrado.");
        }
        comparativoDesempenhoRepositorio.salvar(comparativoDesempenho);
        return comparativoDesempenho;
    }

    public void excluirComparativoSalvo(long comparativoId) {
        if (comparativoDesempenhoRepositorio.buscarPorId(comparativoId).isEmpty()) {
            throw new RegraDeNegocioException("O comparativo salvo nao foi encontrado.");
        }
        comparativoDesempenhoRepositorio.remover(comparativoId);
    }

    private List<EventoEstatistico> eventosDoTorneio(TorneioId torneioId) {
        return eventoEstatisticoRepositorio.listarPorTorneio(torneioId);
    }

    private Map<JogadorId, DesempenhoAcumulado> acumularJogadores(List<EventoEstatistico> eventos) {
        Map<JogadorId, DesempenhoAcumulado> ranking = new HashMap<>();
        eventos.forEach(evento -> ranking
                .computeIfAbsent(evento.getJogadorId(), id -> new DesempenhoAcumulado())
                .registrar(evento));
        return ranking;
    }

    private Map<TimeId, DesempenhoAcumulado> acumularTimes(List<EventoEstatistico> eventos) {
        Map<TimeId, DesempenhoAcumulado> ranking = new HashMap<>();
        for (EventoEstatistico evento : eventos) {
            consultaComparacaoDesempenho.timeDoJogador(evento.getJogadorId())
                    .ifPresent(timeId -> ranking.computeIfAbsent(timeId, id -> new DesempenhoAcumulado())
                            .registrar(evento));
        }
        return ranking;
    }

    private <T> int posicaoNoRanking(T chave, Map<T, DesempenhoAcumulado> ranking) {
        if (!ranking.containsKey(chave)) {
            return 0;
        }
        List<T> ordenado = ranking.entrySet().stream()
                .sorted(Comparator
                        .<Map.Entry<T, DesempenhoAcumulado>>comparingDouble(entry -> entry.getValue().pontuacao())
                        .reversed())
                .map(Map.Entry::getKey)
                .toList();
        return ordenado.indexOf(chave) + 1;
    }

    private DesempenhoComparado montarDesempenho(String rotulo,
                                                 DesempenhoAcumulado acumulado,
                                                 int posicaoRanking) {
        DesempenhoAcumulado dados = acumulado != null ? acumulado : new DesempenhoAcumulado();
        return new DesempenhoComparado(
                rotulo,
                dados.gols,
                dados.assistencias,
                dados.cartoesAmarelos,
                dados.cartoesVermelhos,
                dados.partidasComEventos(),
                posicaoRanking,
                dados.pontuacao());
    }

    private void exigirDadosComparaveis(DesempenhoComparado primeiro, DesempenhoComparado segundo) {
        if (!primeiro.possuiDados() && !segundo.possuiDados()) {
            throw new RegraDeNegocioException("Nao existem dados estatisticos para gerar o comparativo.");
        }
    }

    private static class DesempenhoAcumulado {

        private int gols;
        private int assistencias;
        private int cartoesAmarelos;
        private int cartoesVermelhos;
        private final Set<PartidaId> partidas = new HashSet<>();

        private void registrar(EventoEstatistico evento) {
            partidas.add(evento.getPartidaId());
            TipoEventoEstatistico tipo = evento.getTipo();
            switch (tipo) {
                case GOL -> gols++;
                case ASSISTENCIA -> assistencias++;
                case CARTAO_AMARELO -> cartoesAmarelos++;
                case CARTAO_VERMELHO -> cartoesVermelhos++;
                case SUBSTITUICAO -> { }
            }
        }

        private int partidasComEventos() {
            return partidas.size();
        }

        private double pontuacao() {
            return gols * 3.0
                    + assistencias * 2.0
                    - cartoesAmarelos * 0.5
                    - cartoesVermelhos;
        }
    }
}
