package com.torneios.dominio.torneio.torneio;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface TorneioRepositorio {

    void salvar(Torneio torneio);

    Optional<Torneio> buscarPorId(TorneioId id);

    List<Torneio> listarTodos();

    List<Torneio> listarPorOrganizador(UsuarioId organizadorId);
}
