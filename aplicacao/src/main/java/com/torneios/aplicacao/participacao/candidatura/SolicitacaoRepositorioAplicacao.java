package com.torneios.aplicacao.participacao.candidatura;

import java.util.List;

public interface SolicitacaoRepositorioAplicacao {
    List<SolicitacaoResumo> pesquisarPorSolicitante(long solicitanteId);
    List<SolicitacaoResumo> pesquisarPendentesPorTorneio(long torneioId);
}
