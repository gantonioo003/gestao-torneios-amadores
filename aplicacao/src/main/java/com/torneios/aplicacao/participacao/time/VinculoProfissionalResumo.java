package com.torneios.aplicacao.participacao.time;

import java.time.LocalDate;

public interface VinculoProfissionalResumo {
    Long getProfissionalId();
    String getFuncao();
    LocalDate getDataInicio();
    LocalDate getDataLimiteContrato();
}
