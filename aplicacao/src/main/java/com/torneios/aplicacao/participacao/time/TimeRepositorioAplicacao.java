package com.torneios.aplicacao.participacao.time;

import java.util.List;

public interface TimeRepositorioAplicacao {
    List<TimeResumo> pesquisarResumos(String nome);
    List<TimeResumo> pesquisarResumosPorResponsavel(long responsavelId);
    TimeResumoExpandido pesquisarResumoExpandido(long timeId);
}
