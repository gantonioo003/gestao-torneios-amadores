package com.torneios.aplicacao.competicao.andamento;

import java.util.List;

public interface PartidaRepositorioAplicacao {
    List<PartidaResumo> pesquisarResumosPorTorneio(long torneioId);
}
