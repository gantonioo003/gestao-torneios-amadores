package com.torneios.apresentacao.participacao.time;

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

import com.torneios.aplicacao.participacao.time.TimeResumo;
import com.torneios.aplicacao.participacao.time.TimeServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoRepositorio;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoServico;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;
import com.torneios.dominio.participacao.time.TimeServico;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/time")
class TimeControlador {

    @Autowired TimeServico timeServico;
    @Autowired TimeServicoAplicacao timeServicoConsulta;
    @Autowired ProfissionalEsportivoRepositorio profissionalRepositorio;
    @Autowired ProfissionalEsportivoServico profissionalServico;
    @Autowired ContaUsuarioServico contaUsuarioServico;

    @RequestMapping(method = GET, path = "pesquisa")
    List<? extends TimeResumo> pesquisar(
            @RequestParam(required = false, defaultValue = "") String nome,
            @RequestParam(required = false, defaultValue = "false") boolean meus,
            @RequestParam(required = false, defaultValue = "false") boolean gerenciaveis,
            HttpSession sessao) {
        if (gerenciaveis) {
            return timeServicoConsulta.pesquisarResumosGerenciaveis(
                    SessaoUsuario.exigirUsuarioId(sessao));
        }
        if (meus) {
            return timeServicoConsulta.pesquisarResumosPorResponsavel(SessaoUsuario.exigirUsuarioId(sessao));
        }
        return timeServicoConsulta.pesquisarResumos(nome);
    }

    @RequestMapping(method = GET, path = "criacao")
    TimeFormulario.TimeDto criacao() {
        return new TimeFormulario.TimeDto();
    }

    @RequestMapping(method = POST, path = "salvar")
    void salvar(@RequestBody TimeFormulario.TimeDto dto, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeGerenciarTimes(new UsuarioId(usuarioId));
        timeServico.criarTime(new TimeId(gerarId()), dto.nome, new UsuarioId(usuarioId));
    }

    @RequestMapping(method = GET, path = "{id}/edicao")
    TimeFormulario edicao(@PathVariable long id, HttpSession sessao) {
        var time = timeServico.obterTime(new TimeId(id));
        var dto = new TimeFormulario.TimeDto();
        dto.id = time.getId().valor();
        dto.nome = time.getNome();
        dto.responsavelId = time.getResponsavel().valor();
        dto.elenco = timeServicoConsulta.pesquisarResumoExpandido(id).getElenco().stream()
            .map(v -> {
                var profissional = profissionalRepositorio
                    .buscarPorId(new ProfissionalEsportivoId(v.getProfissionalId()));
                String nome = profissional.map(p -> p.getNome())
                        .orElse("Profissional #" + v.getProfissionalId());
                String tipo = profissional.map(p -> p.getTipo().name()).orElse(null);
                return new TimeFormulario.VinculoEnriquecidoDto(v, nome, tipo);
            }).toList();
        var formulario = new TimeFormulario(dto);
        Long usuarioId = SessaoUsuario.usuarioIdOuNulo(sessao);
        formulario.podeEditarTime = usuarioId != null && time.getResponsavel().valor() == usuarioId;
        formulario.podeGerenciarElenco = usuarioId != null
                && timeServico.podeGerenciarElenco(new TimeId(id), new UsuarioId(usuarioId));
        return formulario;
    }

    @RequestMapping(method = POST, path = "{id}/salvar")
    void atualizar(@PathVariable long id, @RequestBody TimeFormulario.TimeDto dto, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeGerenciarTimes(new UsuarioId(usuarioId));
        timeServico.editarTime(new TimeId(id), new UsuarioId(usuarioId), dto.nome);
    }

    @RequestMapping(method = POST, path = "{id}/excluir")
    void excluir(@PathVariable long id, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeGerenciarTimes(new UsuarioId(usuarioId));
        timeServico.excluirTime(new TimeId(id), new UsuarioId(usuarioId));
    }

    @RequestMapping(method = POST, path = "{id}/vincular-profissional")
    void vincularProfissional(@PathVariable long id, @RequestBody TimeFormulario.VinculoDto dto,
            HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeGerenciarTimes(new UsuarioId(usuarioId));
        timeServico.vincularProfissional(new TimeId(id), new UsuarioId(usuarioId),
            new ProfissionalEsportivoId(dto.profissionalId), dto.funcao,
            dto.dataInicio, dto.dataLimiteContrato);
    }

    @RequestMapping(method = POST, path = "{id}/cadastrar-profissional")
    void cadastrarProfissional(@PathVariable long id, @RequestBody TimeFormulario.NovoIntegranteDto dto,
            HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeGerenciarTimes(new UsuarioId(usuarioId));
        timeServico.validarCadastroProfissional(
                new TimeId(id), new UsuarioId(usuarioId), dto.tipo);
        var profissionalId = new ProfissionalEsportivoId(gerarId());
        profissionalServico.cadastrar(profissionalId, dto.nome, dto.tipo, new UsuarioId(usuarioId));
        timeServico.vincularProfissional(new TimeId(id), new UsuarioId(usuarioId),
                profissionalId, dto.funcao, dto.dataInicio, dto.dataLimiteContrato);
    }

    @RequestMapping(method = POST, path = "{id}/editar-vinculo/{profissionalId}")
    void editarVinculo(@PathVariable long id, @PathVariable long profissionalId,
            @RequestBody TimeFormulario.VinculoDto dto, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeGerenciarTimes(new UsuarioId(usuarioId));
        timeServico.editarVinculoProfissional(new TimeId(id), new UsuarioId(usuarioId),
            new ProfissionalEsportivoId(profissionalId), dto.funcao,
            dto.dataInicio, dto.dataLimiteContrato);
    }

    @RequestMapping(method = POST, path = "{id}/remover-profissional/{profissionalId}")
    void removerProfissional(@PathVariable long id, @PathVariable long profissionalId,
            HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeGerenciarTimes(new UsuarioId(usuarioId));
        timeServico.removerVinculoProfissional(new TimeId(id), new UsuarioId(usuarioId),
            new ProfissionalEsportivoId(profissionalId));
    }

    private long gerarId() {
        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return id == 0 ? 1L : id;
    }
}
