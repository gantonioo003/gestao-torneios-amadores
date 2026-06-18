package com.torneios.aplicacao.participacao.time;

import java.util.List;

public interface TimeRepositorioAplicacao {
    List<TimeResumo> pesquisarResumos(String nome);
    List<TimeResumo> pesquisarResumosPorResponsavel(long responsavelId);
    List<TimeResumo> pesquisarResumosGerenciaveis(long usuarioId);
    TimeResumoExpandido pesquisarResumoExpandido(long timeId);
}
