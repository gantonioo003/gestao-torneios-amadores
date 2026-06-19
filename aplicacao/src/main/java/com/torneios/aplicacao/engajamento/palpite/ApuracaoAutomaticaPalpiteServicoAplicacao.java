package com.torneios.aplicacao.engajamento.palpite;

import java.util.List;

import com.torneios.aplicacao.competicao.resultado.ResultadoCompeticaoServicoAplicacao;
import com.torneios.aplicacao.estatisticas.ranking.RankingServicoAplicacao;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.engajamento.palpite.EventoAlvoPalpite;
import com.torneios.dominio.engajamento.palpite.Palpite;
import com.torneios.dominio.engajamento.palpite.PalpiteServico;
import com.torneios.dominio.engajamento.palpite.TipoPalpite;
import com.torneios.dominio.engajamento.palpite.OpcaoPalpite;
import com.torneios.dominio.engajamento.palpite.PalpiteRepositorio;
import com.torneios.dominio.engajamento.palpite.ProgressoPalpiteServico;

public class ApuracaoAutomaticaPalpiteServicoAplicacao {

    private final PalpiteServico palpiteServico;
    private final PartidaRepositorio partidaRepositorio;
    private final ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServico;
    private final RankingServicoAplicacao rankingServico;
    private final PalpiteRepositorio palpiteRepositorio;
    private final ProgressoPalpiteServico progressoServico;

    public ApuracaoAutomaticaPalpiteServicoAplicacao(
            PalpiteServico palpiteServico,
            PartidaRepositorio partidaRepositorio,
            ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServico,
            RankingServicoAplicacao rankingServico) {
        this(palpiteServico, partidaRepositorio, resultadoCompeticaoServico, rankingServico, null, null);
    }

    public ApuracaoAutomaticaPalpiteServicoAplicacao(
            PalpiteServico palpiteServico,
            PartidaRepositorio partidaRepositorio,
            ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServico,
            RankingServicoAplicacao rankingServico,
            PalpiteRepositorio palpiteRepositorio,
            ProgressoPalpiteServico progressoServico) {
        this.palpiteServico = palpiteServico;
        this.partidaRepositorio = partidaRepositorio;
        this.resultadoCompeticaoServico = resultadoCompeticaoServico;
        this.rankingServico = rankingServico;
        this.palpiteRepositorio = palpiteRepositorio;
        this.progressoServico = progressoServico;
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
        return apurarComPontuacao(evento, vencedorId);
    }

    public void apurarRankingsDoTorneio(long torneioId) {
        resultadoCompeticaoServico.visualizarClassificacao(torneioId).stream()
                .findFirst()
                .ifPresent(campeao -> apurarComPontuacao(
                        eventoTorneio(TipoPalpite.CAMPEAO_TORNEIO, torneioId),
                        campeao.timeId()));

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
            pendentes.forEach(palpite -> progressoServico.registrarApuracao(
                    palpite.getUsuarioId(), palpite.acertou().orElse(false)));
        }
        return apurados;
    }
}
