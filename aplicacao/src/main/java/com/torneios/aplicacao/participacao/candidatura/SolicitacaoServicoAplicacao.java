package com.torneios.aplicacao.participacao.candidatura;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

public class SolicitacaoServicoAplicacao {

    private final SolicitacaoRepositorioAplicacao repositorio;

    public SolicitacaoServicoAplicacao(SolicitacaoRepositorioAplicacao repositorio) {
        notNull(repositorio, "O repositorio nao pode ser nulo.");
        this.repositorio = repositorio;
    }

    public List<SolicitacaoResumo> pesquisarPorSolicitante(long solicitanteId) {
        return repositorio.pesquisarPorSolicitante(solicitanteId);
    }

    public List<SolicitacaoResumo> pesquisarPendentesPorTorneio(long torneioId) {
        return repositorio.pesquisarPendentesPorTorneio(torneioId);
    }
}
