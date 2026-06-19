package com.torneios.aplicacao.competicao.andamento;

import java.util.List;
import java.util.Optional;

public interface PartidaRepositorioAplicacao {
    List<PartidaResumo> pesquisarResumosPorTorneio(long torneioId);
    Optional<PartidaResumo> buscarResumoPorId(long partidaId);
}
