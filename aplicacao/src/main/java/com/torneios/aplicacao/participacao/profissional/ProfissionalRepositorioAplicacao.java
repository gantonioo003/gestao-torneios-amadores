package com.torneios.aplicacao.participacao.profissional;

import java.util.List;

public interface ProfissionalRepositorioAplicacao {
    List<ProfissionalResumo> pesquisarResumosPorNome(String nome);
    ProfissionalResumoExpandido pesquisarResumoExpandido(long profissionalId);
}
