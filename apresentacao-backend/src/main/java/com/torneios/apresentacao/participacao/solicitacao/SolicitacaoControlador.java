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
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoServico;

@RestController
@RequestMapping("backend/solicitacao-participacao")
class SolicitacaoControlador {

    @Autowired SolicitacaoParticipacaoServico solicitacaoServico;
    @Autowired SolicitacaoServicoAplicacao solicitacaoServicoConsulta;

    @RequestMapping(method = POST, path = "solicitar")
    void solicitar(@RequestParam long usuarioId,
                   @RequestParam long timeId,
                   @RequestParam long torneioId) {
        solicitacaoServico.solicitarParticipacao(
                new SolicitacaoParticipacaoId(gerarId()),
                new UsuarioId(usuarioId),
                new TimeId(timeId),
                new TorneioId(torneioId));
    }

    @RequestMapping(method = POST, path = "{id}/aprovar")
    void aprovar(@PathVariable long id, @RequestParam long organizadorId) {
        solicitacaoServico.aprovarSolicitacao(
                new SolicitacaoParticipacaoId(id), new UsuarioId(organizadorId));
    }

    @RequestMapping(method = POST, path = "{id}/rejeitar")
    void rejeitar(@PathVariable long id, @RequestParam long organizadorId) {
        solicitacaoServico.rejeitarSolicitacao(
                new SolicitacaoParticipacaoId(id), new UsuarioId(organizadorId));
    }

    @RequestMapping(method = POST, path = "{id}/cancelar")
    void cancelar(@PathVariable long id, @RequestParam long usuarioId) {
        solicitacaoServico.cancelarCandidatura(
                new SolicitacaoParticipacaoId(id), new UsuarioId(usuarioId));
    }

    @RequestMapping(method = GET, path = "pesquisa-por-solicitante")
    List<? extends SolicitacaoResumo> pesquisarPorSolicitante(@RequestParam long solicitanteId) {
        return solicitacaoServicoConsulta.pesquisarPorSolicitante(solicitanteId);
    }

    @RequestMapping(method = GET, path = "pesquisa-por-torneio")
    List<? extends SolicitacaoResumo> pesquisarPendentesPorTorneio(@RequestParam long torneioId) {
        return solicitacaoServicoConsulta.pesquisarPendentesPorTorneio(torneioId);
    }

    private long gerarId() {
        return System.currentTimeMillis();
    }
}
