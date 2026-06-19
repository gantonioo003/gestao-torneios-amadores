package com.torneios.aplicacao.competicao.andamento;

import java.time.LocalDateTime;

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
    LocalDateTime getDataHoraAgendada();
    String getLocalPartida();
}
