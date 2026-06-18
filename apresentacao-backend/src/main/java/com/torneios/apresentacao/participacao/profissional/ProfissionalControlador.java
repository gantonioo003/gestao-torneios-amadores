package com.torneios.apresentacao.participacao.profissional;

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

import com.torneios.aplicacao.participacao.profissional.ProfissionalResumo;
import com.torneios.aplicacao.participacao.profissional.ProfissionalServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoServico;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;
import com.torneios.dominio.participacao.profissional.RegistroDeCarreira;
import com.torneios.dominio.participacao.profissional.RegistroDeCarreiraId;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/profissional")
class ProfissionalControlador {

    @Autowired ProfissionalEsportivoServico profissionalServico;
    @Autowired ProfissionalServicoAplicacao profissionalServicoConsulta;
    @Autowired ContaUsuarioServico contaUsuarioServico;

    @RequestMapping(method = GET, path = "pesquisa")
    List<? extends ProfissionalResumo> pesquisar(
            @RequestParam(required = false, defaultValue = "") String nome) {
        return profissionalServicoConsulta.pesquisarResumosPorNome(nome);
    }

    @RequestMapping(method = GET, path = "criacao")
    ProfissionalFormulario.ProfissionalDto criacao() {
        return new ProfissionalFormulario.ProfissionalDto();
    }

    @RequestMapping(method = POST, path = "salvar")
    void salvar(@RequestBody ProfissionalFormulario.ProfissionalDto dto, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeGerenciarTimes(new UsuarioId(usuarioId));
        profissionalServico.cadastrar(new ProfissionalEsportivoId(gerarId()),
            dto.nome, dto.tipo, new UsuarioId(usuarioId));
    }

    @RequestMapping(method = GET, path = "{id}/edicao")
    ProfissionalFormulario edicao(@PathVariable long id) {
        var expandido = profissionalServicoConsulta.pesquisarResumoExpandido(id);
        var dto = new ProfissionalFormulario.ProfissionalDto();
        dto.id = expandido.getProfissional().getId();
        dto.nome = expandido.getProfissional().getNome();
        dto.tipo = com.torneios.dominio.participacao.profissional.TipoProfissional
            .valueOf(expandido.getProfissional().getTipo());
        var formulario = new ProfissionalFormulario(dto);
        formulario.historico = expandido.getHistorico();
        return formulario;
    }

    @RequestMapping(method = POST, path = "{id}/salvar")
    void atualizar(@PathVariable long id, @RequestBody ProfissionalFormulario.ProfissionalDto dto,
            HttpSession sessao) {
        profissionalServico.editar(new ProfissionalEsportivoId(id),
            new UsuarioId(SessaoUsuario.exigirUsuarioId(sessao)), dto.nome);
    }

    @RequestMapping(method = POST, path = "{id}/excluir")
    void excluir(@PathVariable long id, HttpSession sessao) {
        profissionalServico.remover(new ProfissionalEsportivoId(id),
            new UsuarioId(SessaoUsuario.exigirUsuarioId(sessao)));
    }

    @RequestMapping(method = POST, path = "{id}/adicionar-carreira")
    void adicionarCarreira(@PathVariable long id, @RequestBody ProfissionalFormulario.RegistroDto dto,
            HttpSession sessao) {
        var registroId = new RegistroDeCarreiraId(gerarId());
        profissionalServico.adicionarRegistroDeCarreira(new ProfissionalEsportivoId(id),
            new UsuarioId(SessaoUsuario.exigirUsuarioId(sessao)), registroId, dto.nomeDoClube,
            dto.dataInicio, dto.dataFim, dto.motivoDeSaida, dto.descricao);
    }

    @RequestMapping(method = POST, path = "{id}/remover-carreira/{registroId}")
    void removerCarreira(@PathVariable long id, @PathVariable long registroId, HttpSession sessao) {
        profissionalServico.removerRegistroDeCarreira(new ProfissionalEsportivoId(id),
            new UsuarioId(SessaoUsuario.exigirUsuarioId(sessao)), new RegistroDeCarreiraId(registroId));
    }

    private long gerarId() {
        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return id == 0 ? 1L : id;
    }
}
