package com.torneios.aplicacao.participacao.profissional;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

public class ProfissionalServicoAplicacao {

    private final ProfissionalRepositorioAplicacao repositorio;

    public ProfissionalServicoAplicacao(ProfissionalRepositorioAplicacao repositorio) {
        notNull(repositorio, "O repositorio nao pode ser nulo.");
        this.repositorio = repositorio;
    }

    public List<ProfissionalResumo> pesquisarResumosPorNome(String nome) {
        return repositorio.pesquisarResumosPorNome(nome == null ? "" : nome);
    }

    public ProfissionalResumoExpandido pesquisarResumoExpandido(long profissionalId) {
        return repositorio.pesquisarResumoExpandido(profissionalId);
    }
}
