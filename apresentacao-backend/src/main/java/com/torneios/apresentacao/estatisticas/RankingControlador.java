package com.torneios.apresentacao.estatisticas;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.estatisticas.ranking.RankingServicoAplicacao;

@RestController
@RequestMapping("backend/ranking-estatistico")
class RankingControlador {

    @Autowired
    RankingServicoAplicacao rankingServicoAplicacao;

    @RequestMapping(method = GET, path = "{torneioId}/artilharia")
    List<RankingServicoAplicacao.EstatisticaJogadorResumo> artilharia(@PathVariable long torneioId) {
        return rankingServicoAplicacao.gerarRankingArtilharia(torneioId);
    }

    @RequestMapping(method = GET, path = "{torneioId}/jogadores")
    List<RankingServicoAplicacao.EstatisticaJogadorResumo> estatisticasJogadores(@PathVariable long torneioId) {
        return rankingServicoAplicacao.listarEstatisticasJogadores(torneioId);
    }

    @RequestMapping(method = GET, path = "{torneioId}/assistencias")
    List<RankingServicoAplicacao.EstatisticaJogadorResumo> lideresAssistencias(@PathVariable long torneioId) {
        return rankingServicoAplicacao.listarLideresAssistencias(torneioId);
    }

    @RequestMapping(method = GET, path = "{torneioId}/nota")
    RankingServicoAplicacao.NotaEstatisticaResumo nota(@PathVariable long torneioId,
                                                       @RequestParam long partidaId,
                                                       @RequestParam long jogadorId) {
        return rankingServicoAplicacao.calcularNotaJogador(torneioId, partidaId, jogadorId);
    }

    @RequestMapping(method = GET, path = "{torneioId}/jogador/{jogadorId}")
    RankingServicoAplicacao.EstatisticaJogadorResumo obterEstatisticaJogador(@PathVariable long torneioId,
                                                                              @PathVariable long jogadorId) {
        return rankingServicoAplicacao.obterEstatisticaJogador(torneioId, jogadorId);
    }

    @RequestMapping(method = GET, path = "{torneioId}/jogador/{jogadorId}/historico")
    List<RankingServicoAplicacao.EventoScoutResumo> historicoJogador(@PathVariable long torneioId,
                                                                     @PathVariable long jogadorId) {
        return rankingServicoAplicacao.obterHistoricoJogador(torneioId, jogadorId);
    }

    @RequestMapping(method = POST, path = "{torneioId}/arquivar-edicao")
    RankingServicoAplicacao.HistoricoEdicaoEstatisticaResumo arquivarEdicao(@PathVariable long torneioId,
                                                                             @RequestParam int numeroEdicao) {
        return rankingServicoAplicacao.arquivarEstatisticasDaEdicao(torneioId, numeroEdicao);
    }

    @RequestMapping(method = POST, path = "{torneioId}/recalcular-edicao")
    RankingServicoAplicacao.HistoricoEdicaoEstatisticaResumo recalcularEdicao(@PathVariable long torneioId,
                                                                               @RequestParam int numeroEdicao) {
        return rankingServicoAplicacao.recalcularEdicaoFechada(torneioId, numeroEdicao);
    }

    @RequestMapping(method = GET, path = "{torneioId}/melhores-medias")
    List<RankingServicoAplicacao.NotaMediaResumo> melhoresMedias(@PathVariable long torneioId,
                                                                 @RequestParam int minimoPartidas) {
        return rankingServicoAplicacao.listarMelhoresMediasDeNota(torneioId, minimoPartidas);
    }

    @RequestMapping(method = GET, path = "{torneioId}/melhor-media")
    RankingServicoAplicacao.NotaMediaResumo melhorMedia(@PathVariable long torneioId,
                                                        @RequestParam int minimoPartidas) {
        return rankingServicoAplicacao.obterMelhorNotaComElegibilidade(torneioId, minimoPartidas);
    }

    @RequestMapping(method = GET, path = "carreira/{jogadorId}")
    RankingServicoAplicacao.EstatisticaCarreiraResumo carreira(@PathVariable long jogadorId) {
        return rankingServicoAplicacao.obterEstatisticaCarreiraJogador(jogadorId);
    }
}
