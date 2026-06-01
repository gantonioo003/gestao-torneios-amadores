package com.torneios.aplicacao.participacao.profissional;

import java.time.LocalDate;

public interface RegistroDeCarreiraResumo {
    Long getId();
    String getNomeDoClube();
    LocalDate getDataInicio();
    LocalDate getDataFim();
    String getMotivoDeSaida();
}
