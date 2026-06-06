package com.torneios.apresentacao.competicao;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.competicao.andamento.PartidaResumo;
import com.torneios.aplicacao.competicao.andamento.PartidaServicoAplicacao;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.partida.PartidaServico;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

@RestController
@RequestMapping("backend/partida")
class PartidaControlador {

    @Autowired PartidaServico partidaServico;
    @Autowired PartidaServicoAplicacao partidaServicoConsulta;

    @RequestMapping(method = GET, path = "pesquisa")
    List<? extends PartidaResumo> pesquisar(@RequestParam long torneioId) {
        return partidaServicoConsulta.pesquisarResumosPorTorneio(torneioId);
    }

    @RequestMapping(method = POST, path = "{id}/registrar-resultado")
    void registrarResultado(@PathVariable long id,
                            @RequestParam long torneioId,
                            @RequestParam long organizadorId,
                            @RequestBody ResultadoDto dto) {
        partidaServico.registrarResultado(
                new TorneioId(torneioId),
                new PartidaId(id),
                new UsuarioId(organizadorId),
                new ResultadoPartida(dto.golsMandante, dto.golsVisitante));
    }

    public static class ResultadoDto {
        public int golsMandante;
        public int golsVisitante;
    }
}
