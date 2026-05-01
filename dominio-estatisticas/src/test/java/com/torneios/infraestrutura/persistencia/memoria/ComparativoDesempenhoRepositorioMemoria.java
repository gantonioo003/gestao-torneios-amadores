package com.torneios.infraestrutura.persistencia.memoria;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.comparacao.ComparativoDesempenho;
import com.torneios.dominio.estatisticas.comparacao.ComparativoDesempenhoRepositorio;

public class ComparativoDesempenhoRepositorioMemoria implements ComparativoDesempenhoRepositorio {

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

    public List<ComparativoDesempenho> listarTodos() {
        return new ArrayList<>(dados.values());
    }

    public void limpar() {
        dados.clear();
    }
}
