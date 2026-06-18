package com.torneios.apresentacao.engajamento;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.engajamento.palpite.PalpiteServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/palpite")
class PalpiteControlador {

    @Autowired
    PalpiteServicoAplicacao palpiteServicoAplicacao;

    @RequestMapping(method = POST, path = "salvar")
    PalpiteServicoAplicacao.PalpiteResumo salvar(@RequestBody PalpiteDto dto, HttpSession sessao) {
        return palpiteServicoAplicacao.registrarOuAtualizar(
                System.currentTimeMillis(),
                SessaoUsuario.exigirUsuarioId(sessao),
                dto.tipo,
                dto.torneioId,
                dto.partidaId,
                dto.opcao);
    }

    @RequestMapping(method = POST, path = "salvar-visitante")
    PalpiteServicoAplicacao.PalpiteResumo salvarVisitante(@RequestBody PalpiteVisitanteDto dto) {
        return palpiteServicoAplicacao.registrarOuAtualizarComoVisitante(
                System.currentTimeMillis(),
                dto.visitanteId,
                dto.tipo,
                dto.torneioId,
                dto.partidaId,
                dto.opcao);
    }

    @RequestMapping(method = GET, path = "percentuais")
    PalpiteServicoAplicacao.PercentuaisResumo percentuais(@RequestParam String tipo,
                                                          @RequestParam long torneioId,
                                                          @RequestParam(required = false) Long partidaId) {
        return palpiteServicoAplicacao.obterPercentuais(tipo, torneioId, partidaId);
    }

    @RequestMapping(method = POST, path = "apurar")
    List<PalpiteServicoAplicacao.PalpiteResumo> apurar(@RequestParam String tipo,
                                                       @RequestParam long torneioId,
                                                       @RequestParam(required = false) Long partidaId,
                                                       @RequestParam long resultadoReal) {
        return palpiteServicoAplicacao.apurar(tipo, torneioId, partidaId, resultadoReal);
    }

    @RequestMapping(method = GET, path = "evento")
    List<PalpiteServicoAplicacao.PalpiteResumo> listarPorEvento(@RequestParam String tipo,
                                                                @RequestParam long torneioId,
                                                                @RequestParam(required = false) Long partidaId) {
        return palpiteServicoAplicacao.listarPorEvento(tipo, torneioId, partidaId);
    }

    static class PalpiteDto {
        public String tipo;
        public long torneioId;
        public Long partidaId;
        public long opcao;
    }

    static class PalpiteVisitanteDto {
        public String visitanteId;
        public String tipo;
        public long torneioId;
        public Long partidaId;
        public long opcao;
    }
}
