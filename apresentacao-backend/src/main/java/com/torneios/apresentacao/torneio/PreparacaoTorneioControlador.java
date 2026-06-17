package com.torneios.apresentacao.torneio;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.torneio.preparacao.PreparacaoTorneioServicoAplicacao;

@RestController
@RequestMapping("backend/preparacao-torneio")
class PreparacaoTorneioControlador {

    @Autowired
    PreparacaoTorneioServicoAplicacao preparacaoTorneioServicoAplicacao;

    @RequestMapping(method = POST, path = "salvar")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao salvar(@RequestBody CriacaoTorneioDto dto) {
        return preparacaoTorneioServicoAplicacao.criarTorneio(
                System.currentTimeMillis(),
                dto.nome,
                dto.formato,
                dto.formatoEquipe,
                dto.organizadorId,
                dto.aceitaSolicitacoes);
    }

    @RequestMapping(method = GET, path = "{id}")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao obter(@PathVariable long id) {
        return preparacaoTorneioServicoAplicacao.obterTorneio(id);
    }

    @RequestMapping(method = POST, path = "{id}/definir-participantes")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao definirParticipantes(@PathVariable long id,
                                                                                  @RequestParam long organizadorId,
                                                                                  @RequestBody List<Long> timesIds) {
        return preparacaoTorneioServicoAplicacao.definirParticipantesIniciais(id, organizadorId, timesIds);
    }

    @RequestMapping(method = POST, path = "{id}/aprovar-participante")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao aprovarParticipante(@PathVariable long id,
                                                                                 @RequestParam long organizadorId,
                                                                                 @RequestParam long timeId) {
        return preparacaoTorneioServicoAplicacao.aprovarParticipante(id, organizadorId, timeId);
    }

    @RequestMapping(method = POST, path = "{id}/remover-participante")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao removerParticipante(@PathVariable long id,
                                                                                 @RequestParam long organizadorId,
                                                                                 @RequestParam long timeId) {
        return preparacaoTorneioServicoAplicacao.removerParticipante(id, organizadorId, timeId);
    }

    @RequestMapping(method = POST, path = "{id}/abrir-solicitacoes")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao abrirSolicitacoes(@PathVariable long id,
                                                                               @RequestParam long organizadorId) {
        return preparacaoTorneioServicoAplicacao.abrirSolicitacoes(id, organizadorId);
    }

    @RequestMapping(method = POST, path = "{id}/fechar-solicitacoes")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao fecharSolicitacoes(@PathVariable long id,
                                                                                @RequestParam long organizadorId) {
        return preparacaoTorneioServicoAplicacao.fecharSolicitacoes(id, organizadorId);
    }

    @RequestMapping(method = POST, path = "{id}/gerar-estrutura-sorteio")
    PreparacaoTorneioServicoAplicacao.EstruturaCompeticaoResumo gerarEstruturaPorSorteio(@PathVariable long id,
                                                                                          @RequestParam long organizadorId) {
        return preparacaoTorneioServicoAplicacao.gerarEstruturaPorSorteio(id, organizadorId);
    }

    @RequestMapping(method = POST, path = "{id}/gerar-estrutura-manual")
    PreparacaoTorneioServicoAplicacao.EstruturaCompeticaoResumo gerarEstruturaManual(@PathVariable long id,
                                                                                      @RequestParam long organizadorId,
                                                                                      @RequestBody List<Long> ordemManualParticipantes) {
        return preparacaoTorneioServicoAplicacao.gerarEstruturaManual(id, organizadorId, ordemManualParticipantes);
    }

    @RequestMapping(method = POST, path = "{id}/preparar-competicao-sorteio")
    PreparacaoTorneioServicoAplicacao.PreparacaoCompeticaoResumo prepararCompeticaoPorSorteio(@PathVariable long id,
                                                                                               @RequestParam long organizadorId) {
        return preparacaoTorneioServicoAplicacao.prepararCompeticaoPorSorteio(id, organizadorId);
    }

    @RequestMapping(method = POST, path = "{id}/preparar-competicao-manual")
    PreparacaoTorneioServicoAplicacao.PreparacaoCompeticaoResumo prepararCompeticaoManual(@PathVariable long id,
                                                                                           @RequestParam long organizadorId,
                                                                                           @RequestBody List<Long> ordemManualParticipantes) {
        return preparacaoTorneioServicoAplicacao.prepararCompeticaoManual(id, organizadorId, ordemManualParticipantes);
    }

    @RequestMapping(method = POST, path = "{id}/iniciar")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao iniciar(@PathVariable long id,
                                                                     @RequestParam long organizadorId) {
        return preparacaoTorneioServicoAplicacao.iniciarTorneio(id, organizadorId);
    }

    @RequestMapping(method = POST, path = "{id}/finalizar")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao finalizar(@PathVariable long id,
                                                                       @RequestParam long organizadorId) {
        return preparacaoTorneioServicoAplicacao.finalizarTorneio(id, organizadorId);
    }

    @RequestMapping(method = POST, path = "{id}/repetir")
    PreparacaoTorneioServicoAplicacao.HistoricoEdicaoResumo repetir(@PathVariable long id,
                                                                    @RequestParam long organizadorId,
                                                                    @RequestParam boolean abrirSolicitacoes) {
        return preparacaoTorneioServicoAplicacao.repetirTorneio(id, organizadorId, abrirSolicitacoes);
    }

    static class CriacaoTorneioDto {
        public String nome;
        public String formato;
        public String formatoEquipe;
        public long organizadorId;
        public boolean aceitaSolicitacoes;
    }
}
