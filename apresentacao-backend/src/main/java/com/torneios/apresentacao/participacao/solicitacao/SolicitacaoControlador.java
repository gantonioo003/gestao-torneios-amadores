package com.torneios.apresentacao.participacao.solicitacao;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.participacao.candidatura.SolicitacaoResumo;
import com.torneios.aplicacao.participacao.candidatura.SolicitacaoServicoAplicacao;
import com.torneios.aplicacao.participacao.inscricao.InscricaoServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoServico;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/solicitacao-participacao")
@Transactional
class SolicitacaoControlador {

    @Autowired SolicitacaoParticipacaoServico solicitacaoServico;
    @Autowired SolicitacaoServicoAplicacao solicitacaoServicoConsulta;
    @Autowired InscricaoServicoAplicacao inscricaoServicoAplicacao;

    @RequestMapping(method = POST, path = "solicitar")
    InscricaoServicoAplicacao.InscricaoResumo solicitar(@RequestParam long timeId,
                   @RequestParam long torneioId,
                   HttpSession sessao) {
        return inscricaoServicoAplicacao.solicitarParticipacao(
                gerarId(), SessaoUsuario.exigirUsuarioId(sessao), timeId, torneioId);
    }

    @RequestMapping(method = POST, path = "convidar")
    InscricaoServicoAplicacao.InscricaoResumo convidar(@RequestParam long timeId,
                                                       @RequestParam long torneioId,
                                                       HttpSession sessao) {
        return inscricaoServicoAplicacao.convidarTime(
                gerarId(), SessaoUsuario.exigirUsuarioId(sessao), timeId, torneioId);
    }

    @RequestMapping(method = POST, path = "{id}/aprovar")
    InscricaoServicoAplicacao.InscricaoResumo aprovar(@PathVariable long id, HttpSession sessao) {
        return inscricaoServicoAplicacao.aprovarSolicitacao(id, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/rejeitar")
    void rejeitar(@PathVariable long id, HttpSession sessao) {
        solicitacaoServico.rejeitarSolicitacao(
                new SolicitacaoParticipacaoId(id), new UsuarioId(SessaoUsuario.exigirUsuarioId(sessao)));
    }

    @RequestMapping(method = POST, path = "{id}/cancelar")
    void cancelar(@PathVariable long id, HttpSession sessao) {
        solicitacaoServico.cancelarCandidatura(
                new SolicitacaoParticipacaoId(id), new UsuarioId(SessaoUsuario.exigirUsuarioId(sessao)));
    }

    @RequestMapping(method = GET, path = "pesquisa-por-solicitante")
    List<? extends SolicitacaoResumo> pesquisarPorSolicitante(HttpSession sessao) {
        return solicitacaoServicoConsulta.pesquisarPorSolicitante(SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "pesquisa-por-torneio")
    List<? extends SolicitacaoResumo> pesquisarPendentesPorTorneio(@RequestParam long torneioId) {
        return solicitacaoServicoConsulta.pesquisarPendentesPorTorneio(torneioId);
    }

    @RequestMapping(method = GET, path = "torneio")
    List<InscricaoServicoAplicacao.InscricaoResumo> acompanharTorneio(
            @RequestParam long torneioId, HttpSession sessao) {
        return inscricaoServicoAplicacao.acompanharTorneio(
                torneioId, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "time")
    List<InscricaoServicoAplicacao.InscricaoResumo> acompanharTime(
            @RequestParam long timeId, HttpSession sessao) {
        return inscricaoServicoAplicacao.acompanharTime(
                timeId, SessaoUsuario.exigirUsuarioId(sessao));
    }

    private long gerarId() {
        return System.currentTimeMillis();
    }
}
