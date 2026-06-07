package com.torneios.dominio.torneio.torneio;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public abstract class TorneioRepositorioDecorador implements TorneioRepositorio {

    protected final TorneioRepositorio delegado;

    protected TorneioRepositorioDecorador(TorneioRepositorio delegado) {
        this.delegado = Objects.requireNonNull(delegado, "O repositorio delegado e obrigatorio.");
    }

    @Override
    public void salvar(Torneio torneio) {
        delegado.salvar(torneio);
    }

    @Override
    public Optional<Torneio> buscarPorId(TorneioId id) {
        return delegado.buscarPorId(id);
    }

    @Override
    public List<Torneio> listarTodos() {
        return delegado.listarTodos();
    }

    @Override
    public List<Torneio> listarPorOrganizador(UsuarioId organizadorId) {
        return delegado.listarPorOrganizador(organizadorId);
    }
}
