package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.torneio.torneio.Torneio;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;

public class TorneioRepositorioMemoria implements TorneioRepositorio {

    private final Map<TorneioId, Torneio> dados = new HashMap<>();

    @Override
    public void salvar(Torneio torneio) {
        dados.put(torneio.getId(), torneio);
    }

    @Override
    public Optional<Torneio> buscarPorId(TorneioId id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<Torneio> listarTodos() {
        return List.copyOf(dados.values());
    }

    @Override
    public List<Torneio> listarPorOrganizador(UsuarioId organizadorId) {
        return dados.values().stream()
                .filter(torneio -> torneio.getOrganizadorId().equals(organizadorId))
                .toList();
    }

    public void limpar() {
        dados.clear();
    }
}
