package com.torneios.apresentacao.competicao;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.torneios.aplicacao.competicao.andamento.PartidaResumo;
import com.torneios.aplicacao.competicao.andamento.PartidaServicoAplicacao;
import com.torneios.aplicacao.competicao.resultado.ResultadoCompeticaoServicoAplicacao;
import com.torneios.aplicacao.competicao.escalacao.EscalacaoServicoAplicacao;
import com.torneios.aplicacao.engajamento.palpite.ApuracaoAutomaticaPalpiteServicoAplicacao;
import com.torneios.aplicacao.estatisticas.ranking.RankingServicoAplicacao;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.partida.PartidaServico;
import com.torneios.apresentacao.SessaoUsuario;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/partida")
class PartidaControlador {

    @Autowired PartidaServico partidaServico;
    @Autowired PartidaServicoAplicacao partidaServicoConsulta;
    @Autowired ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServico;
    @Autowired RankingServicoAplicacao rankingServico;
    @Autowired ApuracaoAutomaticaPalpiteServicoAplicacao apuracaoAutomaticaPalpiteServico;
    @Autowired EscalacaoServicoAplicacao escalacaoServicoAplicacao;

    @RequestMapping(method = GET, path = "pesquisa")
    List<? extends PartidaResumo> pesquisar(@RequestParam long torneioId) {
        return partidaServicoConsulta.pesquisarResumosPorTorneio(torneioId);
    }

    @RequestMapping(method = GET, path = "{id}")
    PartidaResumo buscar(@PathVariable long id) {
        return partidaServicoConsulta.buscarResumoPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida nao encontrada."));
    }

    @RequestMapping(method = POST, path = "{id}/iniciar")
    void iniciar(@PathVariable long id,
                 @RequestParam long torneioId,
                 @RequestParam long organizadorId) {
        partidaServico.iniciarPartida(
                new TorneioId(torneioId),
                new PartidaId(id),
                new UsuarioId(organizadorId));
        escalacaoServicoAplicacao.congelarEscalacoesDaPartida(id);
    }

    @RequestMapping(method = POST, path = "{id}/agendar")
    void agendar(@PathVariable long id,
                 @RequestBody AgendamentoDto dto,
                 HttpSession sessao) {
        partidaServico.agendarPartida(
                new TorneioId(dto.torneioId),
                new PartidaId(id),
                new UsuarioId(SessaoUsuario.exigirUsuarioId(sessao)),
                dto.dataHora,
                dto.local);
    }

    @RequestMapping(method = POST, path = "{id}/registrar-resultado")
    ResultadoPartidaCompleto registrarResultado(@PathVariable long id,
                                                @RequestParam long torneioId,
                                                @RequestParam long organizadorId,
                                                @RequestBody ResultadoDto dto) {
        var andamento = resultadoCompeticaoServico.registrarResultado(
                torneioId, id, organizadorId, dto.golsMandante, dto.golsVisitante);
        apuracaoAutomaticaPalpiteServico.apurarVencedorPartida(
                torneioId, id, dto.golsMandante, dto.golsVisitante);
        return new ResultadoPartidaCompleto(
                andamento,
                rankingServico.gerarRankingArtilharia(torneioId),
                rankingServico.listarLideresAssistencias(torneioId),
                rankingServico.listarMelhoresMediasDeNota(torneioId, 1));
    }

    public static class ResultadoDto {
        public int golsMandante;
        public int golsVisitante;
    }

    public static class AgendamentoDto {
        public long torneioId;
        public LocalDateTime dataHora;
        public String local;
    }

    public record ResultadoPartidaCompleto(
            ResultadoCompeticaoServicoAplicacao.AtualizacaoCompeticaoResumo andamento,
            List<RankingServicoAplicacao.EstatisticaJogadorResumo> artilharia,
            List<RankingServicoAplicacao.EstatisticaJogadorResumo> assistencias,
            List<RankingServicoAplicacao.NotaMediaResumo> melhoresNotas) {
    }
}


