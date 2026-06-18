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

public class ApuracaoAutomaticaPalpiteServicoAplicacao {

    private final PalpiteServico palpiteServico;
    private final PartidaRepositorio partidaRepositorio;
    private final ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServico;
    private final RankingServicoAplicacao rankingServico;

    public ApuracaoAutomaticaPalpiteServicoAplicacao(
            PalpiteServico palpiteServico,
            PartidaRepositorio partidaRepositorio,
            ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServico,
            RankingServicoAplicacao rankingServico) {
        this.palpiteServico = palpiteServico;
        this.partidaRepositorio = partidaRepositorio;
        this.resultadoCompeticaoServico = resultadoCompeticaoServico;
        this.rankingServico = rankingServico;
    }

    public List<Palpite> apurarVencedorPartida(long torneioId,
                                               long partidaId,
                                               int golsMandante,
                                               int golsVisitante) {
        if (golsMandante == golsVisitante) {
            return List.of();
        }
        var partida = partidaRepositorio.buscarPorId(new PartidaId(partidaId)).orElseThrow();
        long vencedorId = golsMandante > golsVisitante
                ? partida.getMandante().valor()
                : partida.getVisitante().valor();
        return palpiteServico.apurar(
                EventoAlvoPalpite.paraPartida(
                        new com.torneios.dominio.compartilhado.torneio.TorneioId(torneioId),
                        new PartidaId(partidaId)),
                vencedorId);
    }

    public void apurarRankingsDoTorneio(long torneioId) {
        resultadoCompeticaoServico.visualizarClassificacao(torneioId).stream()
                .findFirst()
                .ifPresent(campeao -> palpiteServico.apurar(
                        eventoTorneio(TipoPalpite.CAMPEAO_TORNEIO, torneioId),
                        campeao.timeId()));

        rankingServico.gerarRankingArtilharia(torneioId).stream()
                .findFirst()
                .ifPresent(artilheiro -> palpiteServico.apurar(
                        eventoTorneio(TipoPalpite.ARTILHEIRO_TORNEIO, torneioId),
                        artilheiro.jogadorId()));

        rankingServico.listarLideresAssistencias(torneioId).stream()
                .findFirst()
                .ifPresent(lider -> palpiteServico.apurar(
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
}
