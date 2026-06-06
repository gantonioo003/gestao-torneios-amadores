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

import com.torneios.aplicacao.torneio.criacao.TorneioResumo;
import com.torneios.aplicacao.torneio.criacao.TorneioServicoAplicacao;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.torneio.torneio.TorneioServico;

@RestController
@RequestMapping("backend/torneio")
class TorneioControlador {

    @Autowired TorneioServico torneioServico;
    @Autowired TorneioServicoAplicacao torneioServicoConsulta;

    @RequestMapping(method = GET, path = "pesquisa")
    List<? extends TorneioResumo> pesquisar(@RequestParam(required = false) Long organizadorId) {
        if (organizadorId != null) {
            return torneioServicoConsulta.pesquisarResumosPorOrganizador(organizadorId);
        }
        return torneioServicoConsulta.pesquisarResumos();
    }

    @RequestMapping(method = GET, path = "criacao")
    TorneioFormulario.TorneioDto criacao() {
        return new TorneioFormulario.TorneioDto();
    }

    @RequestMapping(method = POST, path = "salvar")
    void salvar(@RequestBody TorneioFormulario.TorneioDto dto) {
        torneioServico.criarTorneio(
                new TorneioId(gerarId()),
                dto.nome,
                dto.formato,
                dto.formatoEquipe,
                new UsuarioId(dto.organizadorId),
                dto.aceitaSolicitacoes);
    }

    @RequestMapping(method = POST, path = "{id}/iniciar")
    void iniciar(@PathVariable long id, @RequestParam long organizadorId) {
        torneioServico.iniciarTorneio(new TorneioId(id), new UsuarioId(organizadorId));
    }

    @RequestMapping(method = POST, path = "{id}/finalizar")
    void finalizar(@PathVariable long id, @RequestParam long organizadorId) {
        torneioServico.finalizarTorneio(new TorneioId(id), new UsuarioId(organizadorId));
    }

    @RequestMapping(method = POST, path = "{id}/abrir-solicitacoes")
    void abrirSolicitacoes(@PathVariable long id, @RequestParam long organizadorId) {
        torneioServico.abrirSolicitacoes(new TorneioId(id), new UsuarioId(organizadorId));
    }

    @RequestMapping(method = POST, path = "{id}/fechar-solicitacoes")
    void fecharSolicitacoes(@PathVariable long id, @RequestParam long organizadorId) {
        torneioServico.fecharSolicitacoes(new TorneioId(id), new UsuarioId(organizadorId));
    }

    @RequestMapping(method = POST, path = "{id}/gerar-estrutura")
    void gerarEstrutura(@PathVariable long id, @RequestParam long organizadorId) {
        torneioServico.gerarEstruturaCompeticao(new TorneioId(id), new UsuarioId(organizadorId));
    }

    private long gerarId() {
        return System.currentTimeMillis();
    }
}
