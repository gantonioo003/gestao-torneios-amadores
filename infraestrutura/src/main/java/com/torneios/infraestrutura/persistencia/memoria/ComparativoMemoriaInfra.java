package com.torneios.infraestrutura.persistencia.memoria;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.comparacao.ComparativoDesempenho;
import com.torneios.dominio.estatisticas.comparacao.ComparativoDesempenhoRepositorio;
import com.torneios.dominio.estatisticas.comparacao.ConsultaComparacaoDesempenho;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoRepositorio;
import com.torneios.dominio.participacao.profissional.TipoProfissional;
import com.torneios.dominio.participacao.time.TimeRepositorio;

@Component
class ComparativoDesempenhoRepositorioMemoriaInfra implements ComparativoDesempenhoRepositorio {

    private final Map<Long, ComparativoDesempenho> dados = new LinkedHashMap<>();

    @Override
    public void salvar(ComparativoDesempenho comparativoDesempenho) {
        dados.put(comparativoDesempenho.getId(), comparativoDesempenho);
    }

    @Override
    public Optional<ComparativoDesempenho> buscarPorId(long comparativoId) {
        return Optional.ofNullable(dados.get(comparativoId));
    }

    @Override
    public List<ComparativoDesempenho> listarPorTorneio(TorneioId torneioId) {
        return dados.values().stream()
                .filter(comparativo -> comparativo.getTorneioId().equals(torneioId))
                .toList();
    }

    @Override
    public void remover(long comparativoId) {
        dados.remove(comparativoId);
    }
}

@Component
class ConsultaComparacaoDesempenhoInfra implements ConsultaComparacaoDesempenho {

    @Autowired
    TimeRepositorio timeRepositorio;

    @Autowired
    ProfissionalEsportivoRepositorio profissionalEsportivoRepositorio;

    @Override
    public String nomeJogador(JogadorId jogadorId) {
        return profissionalEsportivoRepositorio.buscarPorId(new ProfissionalEsportivoId(jogadorId.valor()))
                .map(profissional -> profissional.getNome())
                .orElse("Jogador " + jogadorId.valor());
    }

    @Override
    public String nomeTime(TimeId timeId) {
        return timeRepositorio.buscarPorId(timeId)
                .map(time -> time.getNome())
                .orElse("Time " + timeId.valor());
    }

    @Override
    public Optional<TimeId> timeDoJogador(JogadorId jogadorId) {
        return timeRepositorio.listarTodos().stream()
                .filter(time -> time.getElenco().stream()
                        .anyMatch(vinculo -> TipoProfissional.JOGADOR.name().equalsIgnoreCase(vinculo.getFuncao())
                                && vinculo.getProfissionalId().valor() == jogadorId.valor()))
                .map(time -> time.getId())
                .findFirst();
    }
}
