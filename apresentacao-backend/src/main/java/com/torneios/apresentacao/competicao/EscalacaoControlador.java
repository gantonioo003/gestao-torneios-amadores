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
import com.torneios.apresentacao.SessaoUsuario;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/escalacao")
class EscalacaoControlador {

    @Autowired
    EscalacaoServicoAplicacao escalacaoServicoAplicacao;

    @RequestMapping(method = POST, path = "salvar-por-responsavel")
    EscalacaoServicoAplicacao.EscalacaoResumo salvarPorResponsavel(
            @RequestBody EscalacaoDto dto,
            HttpSession sessao) {
        return escalacaoServicoAplicacao.definirEscalacao(
                System.currentTimeMillis(),
                dto.partidaId,
                dto.timeId,
                SessaoUsuario.exigirUsuarioId(sessao),
                dto.tipoVisualizacao,
                dto.esquemaTatico,
                dto.titulares,
                dto.reservas);
    }

    @RequestMapping(method = POST, path = "salvar-por-tecnico")
    EscalacaoServicoAplicacao.EscalacaoResumo salvarPorTecnico(
            @RequestBody EscalacaoTecnicoDto dto,
            HttpSession sessao) {
        return escalacaoServicoAplicacao.definirEscalacao(
                System.currentTimeMillis(),
                dto.partidaId,
                dto.timeId,
                SessaoUsuario.exigirUsuarioId(sessao),
                dto.tipoVisualizacao,
                dto.esquemaTatico,
                dto.titulares,
                dto.reservas);
    }

    @RequestMapping(method = GET, path = "partida/{partidaId}/time/{timeId}")
    EscalacaoServicoAplicacao.EscalacaoResumo obter(
            @PathVariable long partidaId,
            @PathVariable long timeId,
            HttpSession sessao) {
        return escalacaoServicoAplicacao.obterEscalacaoDoResponsavel(
                partidaId, timeId, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "partida/{partidaId}/publica")
    EscalacaoServicoAplicacao.VisualizacaoPublicaResumo visualizarPublicamente(@PathVariable long partidaId) {
        return escalacaoServicoAplicacao.visualizarPublicamente(partidaId);
    }

    @RequestMapping(method = GET, path = "mesa-tatica")
    EscalacaoServicoAplicacao.MesaTaticaResumo gerarMesaTatica(@RequestParam long partidaId,
                                                               @RequestParam long timeId) {
        return escalacaoServicoAplicacao.gerarMesaTaticaPublica(partidaId, timeId);
    }

    @RequestMapping(method = POST, path = "partida/{partidaId}/congelar")
    void congelar(@PathVariable long partidaId) {
        escalacaoServicoAplicacao.congelarEscalacoesDaPartida(partidaId);
    }

    static class EscalacaoDto {
        public long partidaId;
        public long timeId;
        public String tipoVisualizacao;
        public String esquemaTatico;
        public List<EscalacaoServicoAplicacao.JogadorEscaladoEntrada> titulares;
        public List<Long> reservas;
    }

    static class EscalacaoTecnicoDto {
        public long partidaId;
        public long timeId;
        public String tipoVisualizacao;
        public String esquemaTatico;
        public List<EscalacaoServicoAplicacao.JogadorEscaladoEntrada> titulares;
        public List<Long> reservas;
    }
}
