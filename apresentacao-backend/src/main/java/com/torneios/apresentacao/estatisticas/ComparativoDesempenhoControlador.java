package com.torneios.apresentacao.estatisticas;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.estatisticas.comparacao.ComparativoDesempenhoServicoAplicacao;

@RestController
@RequestMapping("backend/comparativo-desempenho")
class ComparativoDesempenhoControlador {

    @Autowired
    ComparativoDesempenhoServicoAplicacao comparativoDesempenhoServicoAplicacao;

    @RequestMapping(method = POST, path = "jogadores")
    ComparativoDesempenhoServicoAplicacao.ComparativoResumo compararJogadores(@RequestBody ComparativoJogadoresDto dto) {
        return comparativoDesempenhoServicoAplicacao.gerarComparativoJogadores(
                System.currentTimeMillis(),
                dto.primeiroJogadorId,
                dto.segundoJogadorId);
    }

    @RequestMapping(method = POST, path = "times")
    ComparativoDesempenhoServicoAplicacao.ComparativoResumo compararTimes(@RequestBody ComparativoTimesDto dto) {
        return comparativoDesempenhoServicoAplicacao.gerarComparativoTimes(
                System.currentTimeMillis(),
                dto.primeiroTimeId,
                dto.segundoTimeId);
    }

    @RequestMapping(method = POST, path = "salvar-jogadores")
    void salvarJogadores(@RequestBody ComparativoJogadoresDto dto) {
        comparativoDesempenhoServicoAplicacao.salvarComparativoJogadores(
                System.currentTimeMillis(),
                dto.primeiroJogadorId,
                dto.segundoJogadorId);
    }

    @RequestMapping(method = POST, path = "salvar-times")
    void salvarTimes(@RequestBody ComparativoTimesDto dto) {
        comparativoDesempenhoServicoAplicacao.salvarComparativoTimes(
                System.currentTimeMillis(),
                dto.primeiroTimeId,
                dto.segundoTimeId);
    }

    @RequestMapping(method = GET, path = "salvos")
    List<ComparativoDesempenhoServicoAplicacao.ComparativoResumo> listarSalvos() {
        return comparativoDesempenhoServicoAplicacao.consultarComparativosSalvos();
    }

    @RequestMapping(method = POST, path = "{id}/atualizar")
    ComparativoDesempenhoServicoAplicacao.ComparativoResumo atualizar(@PathVariable long id,
                                                                      @RequestBody ComparativoDesempenhoServicoAplicacao.ComparativoResumo dto) {
        if (dto.id() != id) {
            throw new IllegalArgumentException("O id do caminho deve ser igual ao id do comparativo.");
        }
        return comparativoDesempenhoServicoAplicacao.atualizarComparativoSalvo(dto);
    }

    @RequestMapping(method = POST, path = "{id}/excluir")
    void excluir(@PathVariable long id) {
        comparativoDesempenhoServicoAplicacao.excluirComparativoSalvo(id);
    }

    static class ComparativoJogadoresDto {
        public long primeiroJogadorId;
        public long segundoJogadorId;
    }

    static class ComparativoTimesDto {
        public long primeiroTimeId;
        public long segundoTimeId;
    }
}
