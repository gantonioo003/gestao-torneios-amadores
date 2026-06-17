package com.torneios.apresentacao.participacao.inscricao;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.participacao.inscricao.InscricaoServicoAplicacao;

@RestController
@RequestMapping("backend/inscricao")
class InscricaoControlador {

    @Autowired
    InscricaoServicoAplicacao inscricaoServicoAplicacao;

    @RequestMapping(method = POST, path = "solicitar")
    InscricaoServicoAplicacao.InscricaoResumo solicitar(@RequestParam long usuarioId,
                                                        @RequestParam long timeId,
                                                        @RequestParam long torneioId) {
        return inscricaoServicoAplicacao.solicitarParticipacao(
                System.currentTimeMillis(),
                usuarioId,
                timeId,
                torneioId);
    }

    @RequestMapping(method = POST, path = "{id}/aprovar")
    InscricaoServicoAplicacao.InscricaoResumo aprovar(@PathVariable long id, @RequestParam long organizadorId) {
        return inscricaoServicoAplicacao.aprovarSolicitacao(id, organizadorId);
    }

    @RequestMapping(method = POST, path = "{id}/rejeitar")
    InscricaoServicoAplicacao.InscricaoResumo rejeitar(@PathVariable long id, @RequestParam long organizadorId) {
        return inscricaoServicoAplicacao.rejeitarSolicitacao(id, organizadorId);
    }

    @RequestMapping(method = POST, path = "{id}/cancelar")
    void cancelar(@PathVariable long id, @RequestParam long usuarioId) {
        inscricaoServicoAplicacao.cancelarCandidatura(id, usuarioId);
    }

    @RequestMapping(method = POST, path = "remover-participante")
    void removerParticipante(@RequestParam long torneioId,
                             @RequestParam long timeId,
                             @RequestParam long organizadorId) {
        inscricaoServicoAplicacao.removerParticipanteAprovado(torneioId, timeId, organizadorId);
    }

    @RequestMapping(method = GET, path = "candidaturas")
    List<InscricaoServicoAplicacao.InscricaoResumo> acompanhar(@RequestParam long usuarioId) {
        return inscricaoServicoAplicacao.acompanharCandidaturas(usuarioId);
    }

    @RequestMapping(method = GET, path = "pendentes")
    List<InscricaoServicoAplicacao.InscricaoResumo> pendentes(@RequestParam long torneioId,
                                                              @RequestParam long organizadorId) {
        return inscricaoServicoAplicacao.listarPendentesParaAvaliacao(torneioId, organizadorId);
    }

    @RequestMapping(method = GET, path = "{id}")
    InscricaoServicoAplicacao.InscricaoResumo obter(@PathVariable long id) {
        return inscricaoServicoAplicacao.obterSolicitacao(id);
    }
}
