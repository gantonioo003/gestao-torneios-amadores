package com.torneios.aplicacao.competicao.andamento;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

public class PartidaServicoAplicacao {

    private final PartidaRepositorioAplicacao repositorio;

    public PartidaServicoAplicacao(PartidaRepositorioAplicacao repositorio) {
        notNull(repositorio, "O repositorio nao pode ser nulo.");
        this.repositorio = repositorio;
    }

    public List<PartidaResumo> pesquisarResumosPorTorneio(long torneioId) {
        return repositorio.pesquisarResumosPorTorneio(torneioId);
    }
}
