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

import com.torneios.aplicacao.competicao.escalacao.EscalacaoServicoAplicacao;

@RestController
@RequestMapping("backend/escalacao")
class EscalacaoControlador {

    @Autowired
    EscalacaoServicoAplicacao escalacaoServicoAplicacao;

    @RequestMapping(method = POST, path = "salvar-por-responsavel")
    EscalacaoServicoAplicacao.EscalacaoResumo salvarPorResponsavel(@RequestBody EscalacaoDto dto) {
        return escalacaoServicoAplicacao.definirEscalacaoPorResponsavel(
                System.currentTimeMillis(),
                dto.partidaId,
                dto.timeId,
                dto.usuarioId,
                dto.esquemaTatico,
                dto.titulares,
                dto.reservas);
    }

    @RequestMapping(method = POST, path = "salvar-por-tecnico")
    EscalacaoServicoAplicacao.EscalacaoResumo salvarPorTecnico(@RequestBody EscalacaoTecnicoDto dto) {
        return escalacaoServicoAplicacao.definirEscalacaoPorTecnico(
                System.currentTimeMillis(),
                dto.partidaId,
                dto.timeId,
                dto.tecnicoId,
                dto.esquemaTatico,
                dto.titulares,
                dto.reservas);
    }

    @RequestMapping(method = GET, path = "partida/{partidaId}/time/{timeId}")
    EscalacaoServicoAplicacao.EscalacaoResumo obter(@PathVariable long partidaId, @PathVariable long timeId) {
        return escalacaoServicoAplicacao.obterEscalacao(partidaId, timeId);
    }

    @RequestMapping(method = GET, path = "partida/{partidaId}")
    List<EscalacaoServicoAplicacao.EscalacaoResumo> listarPorPartida(@PathVariable long partidaId) {
        return escalacaoServicoAplicacao.listarPorPartida(partidaId);
    }

    @RequestMapping(method = GET, path = "mesa-tatica")
    EscalacaoServicoAplicacao.MesaTaticaResumo gerarMesaTatica(@RequestParam long partidaId,
                                                               @RequestParam long timeId) {
        return escalacaoServicoAplicacao.gerarMesaTatica(partidaId, timeId);
    }

    @RequestMapping(method = POST, path = "partida/{partidaId}/congelar")
    void congelar(@PathVariable long partidaId) {
        escalacaoServicoAplicacao.congelarEscalacoesDaPartida(partidaId);
    }

    static class EscalacaoDto {
        public long partidaId;
        public long timeId;
        public long usuarioId;
        public String esquemaTatico;
        public List<EscalacaoServicoAplicacao.JogadorEscaladoEntrada> titulares;
        public List<Long> reservas;
    }

    static class EscalacaoTecnicoDto {
        public long partidaId;
        public long timeId;
        public long tecnicoId;
        public String esquemaTatico;
        public List<EscalacaoServicoAplicacao.JogadorEscaladoEntrada> titulares;
        public List<Long> reservas;
    }
}
