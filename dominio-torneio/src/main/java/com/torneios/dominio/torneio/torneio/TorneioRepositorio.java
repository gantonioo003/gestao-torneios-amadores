package com.torneios.dominio.torneio.torneio;

import java.util.List;
import java.util.Optional;

public interface TorneioRepositorio {

    void salvar(Torneio torneio);

    Optional<Torneio> buscarPorId(TorneioId id);

    List<Torneio> listarTodos();
}
