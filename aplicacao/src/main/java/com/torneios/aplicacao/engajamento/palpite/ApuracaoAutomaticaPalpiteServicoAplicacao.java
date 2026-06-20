package com.torneios.aplicacao.engajamento.palpite;

import java.util.List;
import java.util.Locale;
import java.util.OptionalLong;
import java.util.Set;

import com.torneios.aplicacao.competicao.resultado.ResultadoCompeticaoServicoAplicacao;
import com.torneios.aplicacao.estatisticas.ranking.RankingServicoAplicacao;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.engajamento.palpite.EventoAlvoPalpite;
import com.torneios.dominio.engajamento.palpite.Palpite;
import com.torneios.dominio.engajamento.palpite.PalpiteServico;
import com.torneios.dominio.engajamento.palpite.TipoPalpite;
import com.torneios.dominio.engajamento.palpite.OpcaoPalpite;
import com.torneios.dominio.engajamento.palpite.PalpiteRepositorio;
import com.torneios.dominio.engajamento.palpite.ProgressoPalpiteServico;
import com.torneios.dominio.torneio.torneio.Torneio;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;

public class ApuracaoAutomaticaPalpiteServicoAplicacao {

    private final PalpiteServico palpiteServico;
    private final PartidaRepositorio partidaRepositorio;
    private final ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServico;
    private final RankingServicoAplicacao rankingServico;
    private final PalpiteRepositorio palpiteRepositorio;
    private final ProgressoPalpiteServico progressoServico;
    private final TorneioRepositorio torneioRepositorio;

    public ApuracaoAutomaticaPalpiteServicoAplicacao(
            PalpiteServico palpiteServico,
            PartidaRepositorio partidaRepositorio,
            ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServico,
            RankingServicoAplicacao rankingServico) {
        this(palpiteServico, partidaRepositorio, resultadoCompeticaoServico, rankingServico, null, null, null);
    }

    public ApuracaoAutomaticaPalpiteServicoAplicacao(
            PalpiteServico palpiteServico,
            PartidaRepositorio partidaRepositorio,
            ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServico,
            RankingServicoAplicacao rankingServico,
            PalpiteRepositorio palpiteRepositorio,
            ProgressoPalpiteServico progressoServico,
            TorneioRepositorio torneioRepositorio) {
        this.palpiteServico = palpiteServico;
        this.partidaRepositorio = partidaRepositorio;
        this.resultadoCompeticaoServico = resultadoCompeticaoServico;
        this.rankingServico = rankingServico;
        this.palpiteRepositorio = palpiteRepositorio;
        this.progressoServico = progressoServico;
        this.torneioRepositorio = torneioRepositorio;
    }

    public List<Palpite> apurarVencedorPartida(long torneioId,
                                               long partidaId,
                                               int golsMandante,
                                               int golsVisitante) {
        var partida = partidaRepositorio.buscarPorId(new PartidaId(partidaId)).orElseThrow();
        long vencedorId = golsMandante == golsVisitante
                ? OpcaoPalpite.EMPATE
                : golsMandante > golsVisitante
                    ? partida.getMandante().valor()
                    : partida.getVisitante().valor();
        EventoAlvoPalpite evento = EventoAlvoPalpite.paraPartida(
                        new com.torneios.dominio.compartilhado.torneio.TorneioId(torneioId),
                        new PartidaId(partidaId));
        List<Palpite> apurados = apurarComPontuacao(evento, vencedorId);
        finalizarEApurarTorneioSeConcluido(torneioId);
        return apurados;
    }

    public void apurarRankingsDoTorneio(long torneioId) {
        identificarCampeao(torneioId).ifPresent(campeaoId -> apurarComPontuacao(
                eventoTorneio(TipoPalpite.CAMPEAO_TORNEIO, torneioId),
                campeaoId));

        rankingServico.gerarRankingArtilharia(torneioId).stream()
                .findFirst()
                .ifPresent(artilheiro -> apurarComPontuacao(
                        eventoTorneio(TipoPalpite.ARTILHEIRO_TORNEIO, torneioId),
                        artilheiro.jogadorId()));

        rankingServico.listarLideresAssistencias(torneioId).stream()
                .findFirst()
                .ifPresent(lider -> apurarComPontuacao(
                        eventoTorneio(TipoPalpite.LIDER_ASSISTENCIAS_TORNEIO, torneioId),
                        lider.jogadorId()));
    }

    private void finalizarEApurarTorneioSeConcluido(long torneioId) {
        if (torneioRepositorio == null) return;
        TorneioId id = new TorneioId(torneioId);
        torneioRepositorio.buscarPorId(id).ifPresent(torneio -> {
            List<com.torneios.dominio.competicao.partida.Partida> partidas =
                    partidaRepositorio.listarPorTorneio(id);
            boolean concluido = competicaoConcluida(torneio, partidas);
            if (!concluido) return;
            torneio.finalizarAutomaticamente(true);
            torneioRepositorio.salvar(torneio);
            apurarRankingsDoTorneio(torneioId);
        });
    }

    private boolean competicaoConcluida(
            Torneio torneio,
            List<com.torneios.dominio.competicao.partida.Partida> partidas) {
        if (partidas.isEmpty()) return false;
        boolean todasEncerradas = partidas.stream()
                .allMatch(com.torneios.dominio.competicao.partida.Partida::estaEncerrada);
        return switch (torneio.getFormato()) {
            case FINAL_UNICA, PONTOS_CORRIDOS -> todasEncerradas;
            case MATA_MATA -> (torneio.getParticipantesAprovados().size() == 2 && todasEncerradas)
                    || possuiFinalEncerrada(partidas);
            case FASE_DE_GRUPOS_COM_MATA_MATA -> possuiFinalEncerrada(partidas);
        };
    }

    private OptionalLong identificarCampeao(long torneioId) {
        if (torneioRepositorio == null) {
            return resultadoCompeticaoServico.visualizarClassificacao(torneioId).stream()
                    .findFirst()
                    .map(campeao -> OptionalLong.of(campeao.timeId()))
                    .orElseGet(OptionalLong::empty);
        }
        Torneio torneio = torneioRepositorio.buscarPorId(new TorneioId(torneioId)).orElse(null);
        if (torneio == null) return OptionalLong.empty();
        if (torneio.getFormato() == FormatoTorneio.PONTOS_CORRIDOS) {
            return resultadoCompeticaoServico.visualizarClassificacao(torneioId).stream()
                    .findFirst()
                    .map(campeao -> OptionalLong.of(campeao.timeId()))
                    .orElseGet(OptionalLong::empty);
        }
        List<com.torneios.dominio.competicao.partida.Partida> partidas =
                partidaRepositorio.listarPorTorneio(new TorneioId(torneioId));
        return partidas.stream()
                .filter(com.torneios.dominio.competicao.partida.Partida::estaEncerrada)
                .filter(partida -> ehPartidaFinal(torneio, partida, partidas.size()))
                .findFirst()
                .map(partida -> {
                    var resultado = partida.getResultado();
                    if (resultado == null || resultado.empate()) return OptionalLong.empty();
                    return OptionalLong.of(resultado.mandanteVenceu()
                            ? partida.getMandante().valor()
                            : partida.getVisitante().valor());
                })
                .orElseGet(OptionalLong::empty);
    }

    private boolean possuiFinalEncerrada(
            List<com.torneios.dominio.competicao.partida.Partida> partidas) {
        return partidas.stream()
                .anyMatch(partida -> partida.estaEncerrada() && etapaFinal(partida.getEtapa()));
    }

    private boolean ehPartidaFinal(
            Torneio torneio,
            com.torneios.dominio.competicao.partida.Partida partida,
            int totalPartidas) {
        if (etapaFinal(partida.getEtapa())) return true;
        return totalPartidas == 1
                && (torneio.getFormato() == FormatoTorneio.FINAL_UNICA
                    || torneio.getParticipantesAprovados().size() == 2);
    }

    private boolean etapaFinal(String etapa) {
        if (etapa == null) return false;
        String normalizada = java.text.Normalizer.normalize(etapa, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase(Locale.ROOT);
        return normalizada.equals("final")
                || normalizada.equals("final unica")
                || normalizada.equals("decisao");
    }

    private EventoAlvoPalpite eventoTorneio(TipoPalpite tipo, long torneioId) {
        var id = new com.torneios.dominio.compartilhado.torneio.TorneioId(torneioId);
        return switch (tipo) {
            case CAMPEAO_TORNEIO -> EventoAlvoPalpite.paraCampeao(id);
            case ARTILHEIRO_TORNEIO -> EventoAlvoPalpite.paraArtilheiro(id);
            case LIDER_ASSISTENCIAS_TORNEIO -> EventoAlvoPalpite.paraLiderAssistencias(id);
            case VENCEDOR_PARTIDA -> throw new IllegalArgumentException("Tipo de torneio esperado.");
        };
    }

    private List<Palpite> apurarComPontuacao(EventoAlvoPalpite evento, long resultadoReal) {
        List<Palpite> pendentes = palpiteRepositorio == null ? List.of() : palpiteRepositorio.listarPorEvento(evento)
                .stream()
                .filter(palpite -> !palpite.estaApurado() && palpite.getUsuarioId() != null)
                .toList();
        List<Palpite> apurados = palpiteServico.apurar(evento, resultadoReal);
        if (progressoServico != null) {
            Set<Long> idsPendentes = pendentes.stream()
                    .map(palpite -> palpite.getId().valor())
                    .collect(java.util.stream.Collectors.toSet());
            apurados.stream()
                    .filter(palpite -> idsPendentes.contains(palpite.getId().valor()))
                    .filter(palpite -> palpite.getUsuarioId() != null)
                    .forEach(palpite -> progressoServico.registrarApuracao(
                            palpite.getUsuarioId(),
                            palpite.acertou().orElse(false),
                            palpite.getEventoAlvo().getTipo()));
        }
        return apurados;
    }
}
