package com.torneios.dominio.participacao.profissional;

import java.util.List;
import java.util.Optional;

public interface ProfissionalEsportivoRepositorio {

    void salvar(ProfissionalEsportivo profissional);

    Optional<ProfissionalEsportivo> buscarPorId(ProfissionalEsportivoId id);

    List<ProfissionalEsportivo> pesquisarPorNome(String nome);

    void remover(ProfissionalEsportivoId id);
}
