package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.feed.ConsultaSuporteFeedTorneio;

public class ConsultaSuporteFeedTorneioMemoria implements ConsultaSuporteFeedTorneio {

    private final Set<UsuarioId> usuariosAutenticados = new HashSet<>();
    private final Map<TorneioId, UsuarioId> organizadoresPorTorneio = new HashMap<>();
    private final Map<TorneioId, Set<PartidaId>> partidasPorTorneio = new HashMap<>();

    public void autenticar(UsuarioId usuarioId) {
        usuariosAutenticados.add(usuarioId);
    }

    public void registrarTorneio(TorneioId torneioId, UsuarioId organizadorId) {
        organizadoresPorTorneio.put(torneioId, organizadorId);
    }

    public void registrarPartida(TorneioId torneioId, PartidaId partidaId) {
        partidasPorTorneio.computeIfAbsent(torneioId, ignored -> new HashSet<>()).add(partidaId);
    }

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return usuariosAutenticados.contains(usuarioId);
    }

    @Override
    public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
        return usuarioId.equals(organizadoresPorTorneio.get(torneioId));
    }

    @Override
    public boolean partidaPertenceAoTorneio(TorneioId torneioId, PartidaId partidaId) {
        return partidasPorTorneio.getOrDefault(torneioId, Set.of()).contains(partidaId);
    }
}
