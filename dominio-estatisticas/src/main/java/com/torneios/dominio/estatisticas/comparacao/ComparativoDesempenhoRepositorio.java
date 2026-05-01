package com.torneios.dominio.estatisticas.comparacao;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.torneio.TorneioId;

public interface ComparativoDesempenhoRepositorio {

    void salvar(ComparativoDesempenho comparativoDesempenho);

    Optional<ComparativoDesempenho> buscarPorId(long comparativoId);

    List<ComparativoDesempenho> listarPorTorneio(TorneioId torneioId);

    void remover(long comparativoId);
}
