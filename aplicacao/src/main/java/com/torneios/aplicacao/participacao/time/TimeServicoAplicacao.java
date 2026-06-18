package com.torneios.aplicacao.participacao.time;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

public class TimeServicoAplicacao {

    private final TimeRepositorioAplicacao repositorio;

    public TimeServicoAplicacao(TimeRepositorioAplicacao repositorio) {
        notNull(repositorio, "O repositorio nao pode ser nulo.");
        this.repositorio = repositorio;
    }

    public List<TimeResumo> pesquisarResumosPorResponsavel(long responsavelId) {
        return repositorio.pesquisarResumosPorResponsavel(responsavelId);
    }

    public List<TimeResumo> pesquisarResumos(String nome) {
        return repositorio.pesquisarResumos(nome == null ? "" : nome.trim());
    }

    public TimeResumoExpandido pesquisarResumoExpandido(long timeId) {
        return repositorio.pesquisarResumoExpandido(timeId);
    }
}
