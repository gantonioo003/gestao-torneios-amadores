package com.torneios.apresentacao.participacao.solicitacao;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.participacao.candidatura.SolicitacaoResumo;
import com.torneios.aplicacao.participacao.candidatura.SolicitacaoServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoServico;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/solicitacao-participacao")
class SolicitacaoControlador {

    @Autowired SolicitacaoParticipacaoServico solicitacaoServico;
    @Autowired SolicitacaoServicoAplicacao solicitacaoServicoConsulta;

    @RequestMapping(method = POST, path = "solicitar")
    void solicitar(@RequestParam long timeId,
                   @RequestParam long torneioId,
                   HttpSession sessao) {
        solicitacaoServico.solicitarParticipacao(
                new SolicitacaoParticipacaoId(gerarId()),
                new UsuarioId(SessaoUsuario.exigirUsuarioId(sessao)),
                new TimeId(timeId),
                new TorneioId(torneioId));
    }

    @RequestMapping(method = POST, path = "{id}/aprovar")
    void aprovar(@PathVariable long id, HttpSession sessao) {
        solicitacaoServico.aprovarSolicitacao(
                new SolicitacaoParticipacaoId(id), new UsuarioId(SessaoUsuario.exigirUsuarioId(sessao)));
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

    private long gerarId() {
        return System.currentTimeMillis();
    }
}
