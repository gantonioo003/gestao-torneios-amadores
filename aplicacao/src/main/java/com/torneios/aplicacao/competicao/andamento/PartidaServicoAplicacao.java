package com.torneios.aplicacao.competicao.andamento;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;
import java.util.Optional;

public class PartidaServicoAplicacao {

    private final PartidaRepositorioAplicacao repositorio;

    public PartidaServicoAplicacao(PartidaRepositorioAplicacao repositorio) {
        notNull(repositorio, "O repositorio nao pode ser nulo.");
        this.repositorio = repositorio;
    }

    public List<PartidaResumo> pesquisarResumosPorTorneio(long torneioId) {
        return repositorio.pesquisarResumosPorTorneio(torneioId);
    }

    public Optional<PartidaResumo> buscarResumoPorId(long partidaId) {
        return repositorio.buscarResumoPorId(partidaId);
    }
}
