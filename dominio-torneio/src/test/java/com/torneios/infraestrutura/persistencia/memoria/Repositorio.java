package com.torneios.infraestrutura.persistencia.memoria;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.torneio.torneio.Torneio;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;

public class Repositorio implements TorneioRepositorio {

    /*-----------------------------------------------------------------------*/
    private final Map<TorneioId, Torneio> torneios = new HashMap<>();

    @Override
    public void salvar(Torneio torneio) {
        notNull(torneio, "O torneio nao pode ser nulo.");
        torneios.put(torneio.getId(), torneio);
    }

    @Override
    public Optional<Torneio> buscarPorId(TorneioId id) {
        notNull(id, "O id do torneio nao pode ser nulo.");
        return Optional.ofNullable(torneios.get(id));
    }

    @Override
    public List<Torneio> listarTodos() {
        return List.copyOf(torneios.values());
    }

    @Override
    public List<Torneio> listarPorOrganizador(UsuarioId organizadorId) {
        notNull(organizadorId, "O id do organizador nao pode ser nulo.");
        return torneios.values().stream()
                .filter(t -> t.getOrganizadorId().equals(organizadorId))
                .toList();
    }
    /*-----------------------------------------------------------------------*/

    public void limpar() {
        torneios.clear();
    }
}
