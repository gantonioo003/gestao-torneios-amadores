package com.torneios.aplicacao.participacao.profissional;

import java.util.List;

public interface ProfissionalResumoExpandido {
    ProfissionalResumo getProfissional();
    List<RegistroDeCarreiraResumo> getHistorico();
}
