package com.torneios.apresentacao.torneio;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.torneio.criacao.TorneioResumo;
import com.torneios.aplicacao.torneio.criacao.TorneioServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.torneio.torneio.TorneioServico;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/torneio")
class TorneioControlador {

    @Autowired TorneioServico torneioServico;
    @Autowired TorneioServicoAplicacao torneioServicoConsulta;
    @Autowired ContaUsuarioServico contaUsuarioServico;

    @RequestMapping(method = GET, path = "pesquisa")
    List<? extends TorneioResumo> pesquisar(
            @RequestParam(required = false) Long organizadorId,
            @RequestParam(required = false, defaultValue = "") String nome) {
        if (organizadorId != null) {
            return torneioServicoConsulta.pesquisarResumosPorOrganizador(organizadorId);
        }
        return torneioServicoConsulta.pesquisarResumosPorNome(nome);
    }

    @RequestMapping(method = GET, path = "criacao")
    TorneioFormulario.TorneioDto criacao() {
        return new TorneioFormulario.TorneioDto();
    }

    @RequestMapping(method = POST, path = "salvar")
    void salvar(@RequestBody TorneioFormulario.TorneioDto dto, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeCriarTorneio(new UsuarioId(usuarioId));
        torneioServico.criarTorneio(
                new TorneioId(gerarId()),
                dto.nome,
                dto.formato,
                dto.formatoEquipe,
                new UsuarioId(usuarioId),
                dto.aceitaSolicitacoes);
    }

    @RequestMapping(method = POST, path = "{id}/iniciar")
    void iniciar(@PathVariable long id, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeCriarTorneio(new UsuarioId(usuarioId));
        torneioServico.iniciarTorneio(new TorneioId(id), new UsuarioId(usuarioId));
    }

    @RequestMapping(method = POST, path = "{id}/finalizar")
    void finalizar(@PathVariable long id, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeCriarTorneio(new UsuarioId(usuarioId));
        torneioServico.finalizarTorneio(new TorneioId(id), new UsuarioId(usuarioId));
    }

    @RequestMapping(method = POST, path = "{id}/abrir-solicitacoes")
    void abrirSolicitacoes(@PathVariable long id, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeCriarTorneio(new UsuarioId(usuarioId));
        torneioServico.abrirSolicitacoes(new TorneioId(id), new UsuarioId(usuarioId));
    }

    @RequestMapping(method = POST, path = "{id}/fechar-solicitacoes")
    void fecharSolicitacoes(@PathVariable long id, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeCriarTorneio(new UsuarioId(usuarioId));
        torneioServico.fecharSolicitacoes(new TorneioId(id), new UsuarioId(usuarioId));
    }

    @RequestMapping(method = POST, path = "{id}/gerar-estrutura")
    void gerarEstrutura(@PathVariable long id, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeCriarTorneio(new UsuarioId(usuarioId));
        torneioServico.gerarEstruturaCompeticao(new TorneioId(id), new UsuarioId(usuarioId));
    }

    private long gerarId() {
        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return id == 0 ? 1L : id;
    }
}
