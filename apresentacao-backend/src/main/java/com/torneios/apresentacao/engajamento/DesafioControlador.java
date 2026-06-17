package com.torneios.apresentacao.engajamento;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.engajamento.desafio.DesafioServicoAplicacao;

@RestController
@RequestMapping("backend/desafio-amistoso")
class DesafioControlador {

    @Autowired
    DesafioServicoAplicacao desafioServicoAplicacao;

    @RequestMapping(method = POST, path = "propor")
    DesafioServicoAplicacao.DesafioResumo propor(@RequestBody PropostaDesafioDto dto) {
        return desafioServicoAplicacao.proporConfronto(
                System.currentTimeMillis(),
                dto.usuarioId,
                dto.timeDesafianteId,
                dto.timeDesafiadoId,
                dto.dataHora,
                dto.local);
    }

    @RequestMapping(method = POST, path = "{id}/aceitar")
    DesafioServicoAplicacao.DesafioResumo aceitar(@PathVariable long id, @RequestParam long usuarioId) {
        return desafioServicoAplicacao.aceitarConvite(id, usuarioId);
    }

    @RequestMapping(method = POST, path = "{id}/recusar")
    DesafioServicoAplicacao.DesafioResumo recusar(@PathVariable long id, @RequestParam long usuarioId) {
        return desafioServicoAplicacao.recusarConvite(id, usuarioId);
    }

    @RequestMapping(method = POST, path = "{id}/reagendar")
    DesafioServicoAplicacao.DesafioResumo reagendar(@PathVariable long id,
                                                    @RequestBody ReagendamentoDto dto) {
        return desafioServicoAplicacao.reagendarAmistoso(id, dto.usuarioId, dto.novaDataHora, dto.novoLocal);
    }

    @RequestMapping(method = POST, path = "{id}/registrar-resultado")
    DesafioServicoAplicacao.DesafioResumo registrarResultado(@PathVariable long id,
                                                             @RequestBody ResultadoDesafioDto dto) {
        return desafioServicoAplicacao.registrarResultado(id, dto.usuarioId, dto.golsDesafiante, dto.golsDesafiado);
    }

    @RequestMapping(method = GET, path = "historico")
    List<DesafioServicoAplicacao.DesafioResumo> historico(@RequestParam long timeId) {
        return desafioServicoAplicacao.listarHistoricoDoTime(timeId);
    }

    static class PropostaDesafioDto {
        public long usuarioId;
        public long timeDesafianteId;
        public long timeDesafiadoId;
        public LocalDateTime dataHora;
        public String local;
    }

    static class ReagendamentoDto {
        public long usuarioId;
        public LocalDateTime novaDataHora;
        public String novoLocal;
    }

    static class ResultadoDesafioDto {
        public long usuarioId;
        public int golsDesafiante;
        public int golsDesafiado;
    }
}
