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

import com.torneios.aplicacao.competicao.resultado.ResultadoCompeticaoServicoAplicacao;
import com.torneios.aplicacao.engajamento.palpite.ApuracaoAutomaticaPalpiteServicoAplicacao;

@RestController
@RequestMapping("backend/resultado-competicao")
class ResultadoCompeticaoControlador {

    @Autowired
    ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServicoAplicacao;

    @Autowired
    ApuracaoAutomaticaPalpiteServicoAplicacao apuracaoAutomaticaPalpiteServico;

    @RequestMapping(method = POST, path = "registrar-resultado")
    ResultadoCompeticaoServicoAplicacao.AtualizacaoCompeticaoResumo registrarResultado(
            @RequestBody ResultadoDto dto) {
        var atualizacao = resultadoCompeticaoServicoAplicacao.registrarResultado(
                dto.torneioId, dto.partidaId, dto.organizadorId, dto.golsMandante, dto.golsVisitante);
        apuracaoAutomaticaPalpiteServico.apurarVencedorPartida(
                dto.torneioId,
                dto.partidaId,
                dto.golsMandante,
                dto.golsVisitante);
        return atualizacao;
    }

    @RequestMapping(method = POST, path = "{torneioId}/gerenciar-andamento")
    ResultadoCompeticaoServicoAplicacao.AtualizacaoCompeticaoResumo gerenciarAndamento(@PathVariable long torneioId) {
        return resultadoCompeticaoServicoAplicacao.gerenciarAndamento(torneioId);
    }

    @RequestMapping(method = GET, path = "{torneioId}/classificacao")
    List<ResultadoCompeticaoServicoAplicacao.ClassificacaoResumo> visualizarClassificacao(@PathVariable long torneioId) {
        return resultadoCompeticaoServicoAplicacao.visualizarClassificacao(torneioId);
    }

    @RequestMapping(method = GET, path = "{torneioId}/chaveamento")
    ResultadoCompeticaoServicoAplicacao.ChaveamentoResumo visualizarChaveamento(@PathVariable long torneioId) {
        return resultadoCompeticaoServicoAplicacao.visualizarChaveamento(torneioId);
    }

    @RequestMapping(method = POST, path = "abrir-contestacao")
    ResultadoCompeticaoServicoAplicacao.ContestacaoResumo abrirContestacao(@RequestBody ContestacaoDto dto) {
        return resultadoCompeticaoServicoAplicacao.abrirContestacao(
                System.currentTimeMillis(),
                dto.partidaId,
                dto.timeSolicitanteId,
                dto.usuarioSolicitanteId,
                dto.motivo,
                dto.justificativa,
                dto.evidencias,
                dto.dataHoraAbertura == null ? LocalDateTime.now() : dto.dataHoraAbertura);
    }

    @RequestMapping(method = POST, path = "{id}/analisar-contestacao")
    ResultadoCompeticaoServicoAplicacao.ContestacaoResumo analisarContestacao(@PathVariable long id,
                                                                              @RequestBody AnaliseContestacaoDto dto) {
        return resultadoCompeticaoServicoAplicacao.analisarContestacao(
                id,
                dto.organizadorId,
                dto.decisao,
                dto.observacao,
                dto.golsMandanteCorrigido,
                dto.golsVisitanteCorrigido,
                dto.dataHoraDecisao == null ? LocalDateTime.now() : dto.dataHoraDecisao);
    }

    @RequestMapping(method = GET, path = "{torneioId}/contestacoes")
    List<ResultadoCompeticaoServicoAplicacao.ContestacaoResumo> listarContestacoes(@PathVariable long torneioId) {
        return resultadoCompeticaoServicoAplicacao.listarContestacoesDoTorneio(torneioId);
    }

    static class ResultadoDto {
        public long torneioId;
        public long partidaId;
        public long organizadorId;
        public int golsMandante;
        public int golsVisitante;
    }

    static class ContestacaoDto {
        public long partidaId;
        public long timeSolicitanteId;
        public long usuarioSolicitanteId;
        public String motivo;
        public String justificativa;
        public List<String> evidencias;
        public LocalDateTime dataHoraAbertura;
    }

    static class AnaliseContestacaoDto {
        public long organizadorId;
        public String decisao;
        public String observacao;
        public Integer golsMandanteCorrigido;
        public Integer golsVisitanteCorrigido;
        public LocalDateTime dataHoraDecisao;
    }
}


