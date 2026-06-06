package com.torneios.dominio.torneio.torneio;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

/**
 * Padrão Decorator — implementação concreta com log.
 *
 * Registra cada operação de escrita antes de delegar ao repositório real,
 * sem modificar a lógica de persistência.
 */
public class TorneioRepositorioComLog extends TorneioRepositorioDecorador {

    private static final Logger LOG = Logger.getLogger(TorneioRepositorioComLog.class.getName());

    public TorneioRepositorioComLog(TorneioRepositorio delegado) {
        super(delegado);
    }

    @Override
    public void salvar(Torneio torneio) {
        LOG.info("[Torneio] salvar id=" + torneio.getId().valor()
                + " nome=" + torneio.getNome()
                + " status=" + torneio.getStatus());
        super.salvar(torneio);
    }

    @Override
    public Optional<Torneio> buscarPorId(TorneioId id) {
        LOG.fine("[Torneio] buscarPorId id=" + id.valor());
        return super.buscarPorId(id);
    }

    @Override
    public List<Torneio> listarTodos() {
        LOG.fine("[Torneio] listarTodos");
        return super.listarTodos();
    }

    @Override
    public List<Torneio> listarPorOrganizador(UsuarioId organizadorId) {
        LOG.fine("[Torneio] listarPorOrganizador organizadorId=" + organizadorId.valor());
        return super.listarPorOrganizador(organizadorId);
    }
}
