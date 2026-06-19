package com.torneios.dominio.estatisticas.comparacao;

import java.util.List;
import java.util.Optional;

public interface ComparativoDesempenhoRepositorio {

    void salvar(ComparativoDesempenho comparativoDesempenho);

    Optional<ComparativoDesempenho> buscarPorId(long comparativoId);

    List<ComparativoDesempenho> listarTodos();

    void remover(long comparativoId);
}
