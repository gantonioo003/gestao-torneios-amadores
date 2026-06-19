package com.torneios.aplicacao.competicao.andamento;

public interface PartidaResumo {
    Long getId();
    Long getTorneioId();
    Long getMandanteId();
    Long getVisitanteId();
    String getEtapa();
    boolean isIniciada();
    boolean isEncerrada();
    Integer getGolsMandante();
    Integer getGolsVisitante();
}
