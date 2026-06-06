package com.torneios.aplicacao.torneio.criacao;

import java.util.List;

public interface TorneioRepositorioAplicacao {
    List<TorneioResumo> pesquisarResumos();
    List<TorneioResumo> pesquisarResumosPorOrganizador(long organizadorId);
}
