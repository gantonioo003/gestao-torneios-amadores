package com.torneios.aplicacao.participacao.candidatura;

public interface SolicitacaoResumo {
    Long getId();
    Long getTimeId();
    Long getTorneioId();
    Long getSolicitanteId();
    String getStatus();
}
