package com.torneios.apresentacao.participacao.time;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.participacao.time.TimeResumo;
import com.torneios.aplicacao.participacao.time.TimeServicoAplicacao;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.time.Time;
import com.torneios.dominio.participacao.time.TimeServico;

@RestController
@RequestMapping("backend/time")
class TimeControlador {

    @Autowired TimeServico timeServico;
    @Autowired TimeServicoAplicacao timeServicoConsulta;

    @RequestMapping(method = GET, path = "pesquisa")
    List<? extends TimeResumo> pesquisar(@RequestParam long responsavelId) {
        return timeServicoConsulta.pesquisarResumosPorResponsavel(responsavelId);
    }

    @RequestMapping(method = GET, path = "criacao")
    TimeFormulario.TimeDto criacao() {
        return new TimeFormulario.TimeDto();
    }

    @RequestMapping(method = POST, path = "salvar")
    void salvar(@RequestBody TimeFormulario.TimeDto dto) {
        var time = new Time(new TimeId(gerarId()), dto.nome, new UsuarioId(dto.responsavelId));
        timeServico.salvar(time);
    }

    @RequestMapping(method = GET, path = "{id}/edicao")
    TimeFormulario edicao(@PathVariable long id) {
        var time = timeServico.obterTime(new TimeId(id));
        var dto = new TimeFormulario.TimeDto();
        dto.id = time.getId().valor();
        dto.nome = time.getNome();
        dto.responsavelId = time.getResponsavel().valor();
        dto.elenco = timeServicoConsulta.pesquisarResumoExpandido(id).getElenco();
        return new TimeFormulario(dto);
    }

    @RequestMapping(method = POST, path = "{id}/salvar")
    void atualizar(@PathVariable long id, @RequestParam long responsavelId,
            @RequestBody TimeFormulario.TimeDto dto) {
        timeServico.editarTime(new TimeId(id), new UsuarioId(responsavelId), dto.nome);
    }

    @RequestMapping(method = POST, path = "{id}/excluir")
    void excluir(@PathVariable long id, @RequestParam long responsavelId) {
        timeServico.excluirTime(new TimeId(id), new UsuarioId(responsavelId));
    }

    @RequestMapping(method = POST, path = "{id}/vincular-profissional")
    void vincularProfissional(@PathVariable long id, @RequestParam long responsavelId,
            @RequestBody TimeFormulario.VinculoDto dto) {
        timeServico.vincularProfissional(new TimeId(id), new UsuarioId(responsavelId),
            new ProfissionalEsportivoId(dto.profissionalId), dto.funcao,
            dto.dataInicio, dto.dataLimiteContrato);
    }

    @RequestMapping(method = POST, path = "{id}/editar-vinculo/{profissionalId}")
    void editarVinculo(@PathVariable long id, @PathVariable long profissionalId,
            @RequestParam long responsavelId, @RequestBody TimeFormulario.VinculoDto dto) {
        timeServico.editarVinculoProfissional(new TimeId(id), new UsuarioId(responsavelId),
            new ProfissionalEsportivoId(profissionalId), dto.funcao,
            dto.dataInicio, dto.dataLimiteContrato);
    }

    @RequestMapping(method = POST, path = "{id}/remover-profissional/{profissionalId}")
    void removerProfissional(@PathVariable long id, @PathVariable long profissionalId,
            @RequestParam long responsavelId) {
        timeServico.removerVinculoProfissional(new TimeId(id), new UsuarioId(responsavelId),
            new ProfissionalEsportivoId(profissionalId));
    }

    private long gerarId() {
        return System.currentTimeMillis();
    }
}
