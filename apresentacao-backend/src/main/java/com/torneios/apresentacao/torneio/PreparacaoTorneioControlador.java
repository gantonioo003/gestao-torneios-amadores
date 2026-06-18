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
import com.torneios.apresentacao.SessaoUsuario;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/preparacao-torneio")
class PreparacaoTorneioControlador {

    @Autowired
    PreparacaoTorneioServicoAplicacao preparacaoTorneioServicoAplicacao;

    @Autowired
    ContaUsuarioServico contaUsuarioServico;

    @RequestMapping(method = POST, path = "salvar")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao salvar(@RequestBody CriacaoTorneioDto dto,
                                                                    HttpSession sessao) {
        long organizadorId = exigirOrganizador(sessao);
        return preparacaoTorneioServicoAplicacao.criarTorneio(
                System.currentTimeMillis(),
                dto.nome,
                dto.formato,
                dto.formatoEquipe,
                organizadorId,
                dto.aceitaSolicitacoes);
    }

    @RequestMapping(method = GET, path = "{id}")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao obter(@PathVariable long id) {
        return preparacaoTorneioServicoAplicacao.obterTorneio(id);
    }

    @RequestMapping(method = POST, path = "{id}/definir-participantes")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao definirParticipantes(@PathVariable long id,
                                                                                  @RequestParam long organizadorId,
                                                                                  @RequestBody List<Long> timesIds,
                                                                                  HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.definirParticipantesIniciais(id, exigirOrganizador(sessao), timesIds);
    }

    @RequestMapping(method = POST, path = "{id}/aprovar-participante")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao aprovarParticipante(@PathVariable long id,
                                                                                 @RequestParam long organizadorId,
                                                                                 @RequestParam long timeId,
                                                                                 HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.aprovarParticipante(id, exigirOrganizador(sessao), timeId);
    }

    @RequestMapping(method = POST, path = "{id}/remover-participante")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao removerParticipante(@PathVariable long id,
                                                                                 @RequestParam long organizadorId,
                                                                                 @RequestParam long timeId,
                                                                                 HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.removerParticipante(id, exigirOrganizador(sessao), timeId);
    }

    @RequestMapping(method = POST, path = "{id}/abrir-solicitacoes")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao abrirSolicitacoes(@PathVariable long id,
                                                                               @RequestParam long organizadorId,
                                                                               HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.abrirSolicitacoes(id, exigirOrganizador(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/fechar-solicitacoes")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao fecharSolicitacoes(@PathVariable long id,
                                                                                @RequestParam long organizadorId,
                                                                                HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.fecharSolicitacoes(id, exigirOrganizador(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/renomear")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao renomear(@PathVariable long id,
                                                                      @RequestBody RenomearTorneioDto dto,
                                                                      HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.renomearTorneio(
                id, exigirOrganizador(sessao), dto.nome);
    }

    @RequestMapping(method = POST, path = "{id}/gerar-estrutura-sorteio")
    PreparacaoTorneioServicoAplicacao.EstruturaCompeticaoResumo gerarEstruturaPorSorteio(@PathVariable long id,
                                                                                          @RequestParam long organizadorId,
                                                                                          HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.gerarEstruturaPorSorteio(id, exigirOrganizador(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/gerar-estrutura-manual")
    PreparacaoTorneioServicoAplicacao.EstruturaCompeticaoResumo gerarEstruturaManual(@PathVariable long id,
                                                                                      @RequestParam long organizadorId,
                                                                                      @RequestBody List<Long> ordemManualParticipantes,
                                                                                      HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.gerarEstruturaManual(
                id, exigirOrganizador(sessao), ordemManualParticipantes);
    }

    @RequestMapping(method = POST, path = "{id}/preparar-competicao-sorteio")
    PreparacaoTorneioServicoAplicacao.PreparacaoCompeticaoResumo prepararCompeticaoPorSorteio(@PathVariable long id,
                                                                                               @RequestParam long organizadorId,
                                                                                               HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.prepararCompeticaoPorSorteio(id, exigirOrganizador(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/preparar-competicao-manual")
    PreparacaoTorneioServicoAplicacao.PreparacaoCompeticaoResumo prepararCompeticaoManual(@PathVariable long id,
                                                                                           @RequestParam long organizadorId,
                                                                                           @RequestBody List<Long> ordemManualParticipantes,
                                                                                           HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.prepararCompeticaoManual(
                id, exigirOrganizador(sessao), ordemManualParticipantes);
    }

    @RequestMapping(method = POST, path = "{id}/iniciar")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao iniciar(@PathVariable long id,
                                                                     @RequestParam long organizadorId,
                                                                     HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.iniciarTorneio(id, exigirOrganizador(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/finalizar")
    PreparacaoTorneioServicoAplicacao.TorneioResumoAplicacao finalizar(@PathVariable long id,
                                                                       @RequestParam long organizadorId,
                                                                       HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.finalizarTorneio(id, exigirOrganizador(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/repetir")
    PreparacaoTorneioServicoAplicacao.HistoricoEdicaoResumo repetir(@PathVariable long id,
                                                                    @RequestParam long organizadorId,
                                                                    @RequestParam boolean abrirSolicitacoes,
                                                                    HttpSession sessao) {
        return preparacaoTorneioServicoAplicacao.repetirTorneio(
                id, exigirOrganizador(sessao), abrirSolicitacoes);
    }

    private long exigirOrganizador(HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeCriarTorneio(new UsuarioId(usuarioId));
        return usuarioId;
    }

    static class CriacaoTorneioDto {
        public String nome;
        public String formato;
        public String formatoEquipe;
        public long organizadorId;
        public boolean aceitaSolicitacoes;
    }

    static class RenomearTorneioDto {
        public String nome;
    }
}
