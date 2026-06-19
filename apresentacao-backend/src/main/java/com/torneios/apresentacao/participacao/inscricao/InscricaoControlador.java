package com.torneios.apresentacao.participacao.inscricao;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.participacao.inscricao.InscricaoServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/inscricao")
@Transactional
class InscricaoControlador {

    @Autowired
    InscricaoServicoAplicacao inscricaoServicoAplicacao;

    @RequestMapping(method = POST, path = "solicitar")
    InscricaoServicoAplicacao.InscricaoResumo solicitar(@RequestParam long timeId,
                                                        @RequestParam long torneioId,
                                                        HttpSession sessao) {
        return inscricaoServicoAplicacao.solicitarParticipacao(
                System.currentTimeMillis(),
                SessaoUsuario.exigirUsuarioId(sessao),
                timeId,
                torneioId);
    }

    @RequestMapping(method = POST, path = "{id}/aprovar")
    InscricaoServicoAplicacao.InscricaoResumo aprovar(@PathVariable long id, HttpSession sessao) {
        return inscricaoServicoAplicacao.aprovarSolicitacao(id, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/rejeitar")
    InscricaoServicoAplicacao.InscricaoResumo rejeitar(@PathVariable long id, HttpSession sessao) {
        return inscricaoServicoAplicacao.rejeitarSolicitacao(id, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/cancelar")
    void cancelar(@PathVariable long id, HttpSession sessao) {
        inscricaoServicoAplicacao.cancelarCandidatura(id, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "remover-participante")
    void removerParticipante(@RequestParam long torneioId,
                             @RequestParam long timeId,
                             HttpSession sessao) {
        inscricaoServicoAplicacao.removerParticipanteAprovado(
                torneioId, timeId, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "candidaturas")
    List<InscricaoServicoAplicacao.InscricaoResumo> acompanhar(HttpSession sessao) {
        return inscricaoServicoAplicacao.acompanharCandidaturas(SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "pendentes")
    List<InscricaoServicoAplicacao.InscricaoResumo> pendentes(@RequestParam long torneioId,
                                                              HttpSession sessao) {
        return inscricaoServicoAplicacao.listarPendentesParaAvaliacao(
                torneioId, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "{id}")
    InscricaoServicoAplicacao.InscricaoResumo obter(@PathVariable long id) {
        return inscricaoServicoAplicacao.obterSolicitacao(id);
    }
}
