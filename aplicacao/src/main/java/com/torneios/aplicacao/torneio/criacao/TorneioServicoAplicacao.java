package com.torneios.aplicacao.torneio.criacao;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

public class TorneioServicoAplicacao {

    private final TorneioRepositorioAplicacao repositorio;

    public TorneioServicoAplicacao(TorneioRepositorioAplicacao repositorio) {
        notNull(repositorio, "O repositorio nao pode ser nulo.");
        this.repositorio = repositorio;
    }

    public List<TorneioResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    public List<TorneioResumo> pesquisarResumosPorOrganizador(long organizadorId) {
        return repositorio.pesquisarResumosPorOrganizador(organizadorId);
    }
}
