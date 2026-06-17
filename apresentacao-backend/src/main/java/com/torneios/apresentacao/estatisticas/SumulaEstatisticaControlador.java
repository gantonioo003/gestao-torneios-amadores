package com.torneios.apresentacao.estatisticas;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.estatisticas.sumula.SumulaEstatisticaServicoAplicacao;

@RestController
@RequestMapping("backend/sumula-estatistica")
class SumulaEstatisticaControlador {

    @Autowired
    SumulaEstatisticaServicoAplicacao sumulaEstatisticaServicoAplicacao;

    @RequestMapping(method = POST, path = "registrar-gol")
    SumulaEstatisticaServicoAplicacao.EventoResumo registrarGol(@RequestBody EventoJogadorDto dto) {
        return sumulaEstatisticaServicoAplicacao.registrarGol(
                System.currentTimeMillis(),
                dto.torneioId,
                dto.partidaId,
                dto.organizadorId,
                dto.jogadorId);
    }

    @RequestMapping(method = POST, path = "registrar-assistencia")
    SumulaEstatisticaServicoAplicacao.EventoResumo registrarAssistencia(@RequestBody EventoJogadorDto dto) {
        return sumulaEstatisticaServicoAplicacao.registrarAssistencia(
                System.currentTimeMillis(),
                dto.torneioId,
                dto.partidaId,
                dto.organizadorId,
                dto.jogadorId);
    }

    @RequestMapping(method = POST, path = "registrar-cartao-amarelo")
    SumulaEstatisticaServicoAplicacao.EventoResumo registrarCartaoAmarelo(@RequestBody EventoJogadorDto dto) {
        return sumulaEstatisticaServicoAplicacao.registrarCartaoAmarelo(
                System.currentTimeMillis(),
                dto.torneioId,
                dto.partidaId,
                dto.organizadorId,
                dto.jogadorId);
    }

    @RequestMapping(method = POST, path = "registrar-cartao-vermelho")
    SumulaEstatisticaServicoAplicacao.EventoResumo registrarCartaoVermelho(@RequestBody EventoJogadorDto dto) {
        return sumulaEstatisticaServicoAplicacao.registrarCartaoVermelho(
                System.currentTimeMillis(),
                dto.torneioId,
                dto.partidaId,
                dto.organizadorId,
                dto.jogadorId);
    }

    @RequestMapping(method = POST, path = "registrar-substituicao")
    SumulaEstatisticaServicoAplicacao.EventoResumo registrarSubstituicao(@RequestBody SubstituicaoDto dto) {
        return sumulaEstatisticaServicoAplicacao.registrarSubstituicaoComParadaJogo(
                System.currentTimeMillis(),
                dto.torneioId,
                dto.partidaId,
                dto.organizadorId,
                dto.jogadorSaiuId,
                dto.jogadorEntrouId,
                dto.ordemParadaJogo,
                dto.descricaoParadaJogo);
    }

    @RequestMapping(method = POST, path = "{id}/corrigir-evento")
    SumulaEstatisticaServicoAplicacao.EventoResumo corrigirEvento(@PathVariable long id,
                                                                  @RequestBody CorrecaoEventoDto dto) {
        return sumulaEstatisticaServicoAplicacao.corrigirEvento(
                id,
                dto.torneioId,
                dto.partidaId,
                dto.organizadorId,
                dto.jogadorId,
                dto.novoTipo);
    }

    @RequestMapping(method = POST, path = "{id}/corrigir-substituicao")
    SumulaEstatisticaServicoAplicacao.EventoResumo corrigirSubstituicao(@PathVariable long id,
                                                                        @RequestBody SubstituicaoDto dto) {
        return sumulaEstatisticaServicoAplicacao.corrigirSubstituicao(
                id,
                dto.torneioId,
                dto.partidaId,
                dto.organizadorId,
                dto.jogadorSaiuId,
                dto.jogadorEntrouId,
                dto.ordemParadaJogo,
                dto.descricaoParadaJogo);
    }

    @RequestMapping(method = POST, path = "{id}/remover")
    void remover(@PathVariable long id,
                 @RequestParam long torneioId,
                 @RequestParam long partidaId,
                 @RequestParam long organizadorId) {
        sumulaEstatisticaServicoAplicacao.removerEvento(id, torneioId, partidaId, organizadorId);
    }

    @RequestMapping(method = GET, path = "partida/{partidaId}")
    List<SumulaEstatisticaServicoAplicacao.EventoResumo> listarPorPartida(@PathVariable long partidaId) {
        return sumulaEstatisticaServicoAplicacao.listarPorPartida(partidaId);
    }

    @RequestMapping(method = GET, path = "torneio/{torneioId}")
    List<SumulaEstatisticaServicoAplicacao.EventoResumo> listarPorTorneio(@PathVariable long torneioId) {
        return sumulaEstatisticaServicoAplicacao.listarPorTorneio(torneioId);
    }

    @RequestMapping(method = GET, path = "controle-disciplinar")
    SumulaEstatisticaServicoAplicacao.ControleDisciplinarResumo consultarControleDisciplinar(
            @RequestParam long torneioId,
            @RequestParam long partidaId,
            @RequestParam long jogadorId) {
        return sumulaEstatisticaServicoAplicacao.consultarControleDisciplinar(torneioId, partidaId, jogadorId);
    }

    static class EventoJogadorDto {
        public long torneioId;
        public long partidaId;
        public long organizadorId;
        public long jogadorId;
    }

    static class CorrecaoEventoDto extends EventoJogadorDto {
        public String novoTipo;
    }

    static class SubstituicaoDto {
        public long torneioId;
        public long partidaId;
        public long organizadorId;
        public long jogadorSaiuId;
        public long jogadorEntrouId;
        public Integer ordemParadaJogo;
        public String descricaoParadaJogo;
    }
}
