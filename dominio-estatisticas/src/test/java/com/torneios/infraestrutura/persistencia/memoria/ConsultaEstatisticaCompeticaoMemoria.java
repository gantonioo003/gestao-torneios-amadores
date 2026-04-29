package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.estatisticas.evento.ConsultaEstatisticaCompeticao;

public class ConsultaEstatisticaCompeticaoMemoria implements ConsultaEstatisticaCompeticao {

    private final Map<TorneioId, UsuarioId> organizadores = new HashMap<>();
    private final Map<PartidaId, TorneioId> partidaTorneio = new HashMap<>();
    private final Map<PartidaId, Set<JogadorId>> jogadoresPorPartida = new HashMap<>();

    public void registrarOrganizador(TorneioId torneioId, UsuarioId organizadorId) {
        organizadores.put(torneioId, organizadorId);
    }

    public void registrarPartida(PartidaId partidaId, TorneioId torneioId) {
        partidaTorneio.put(partidaId, torneioId);
    }

    public void registrarJogadorNaPartida(PartidaId partidaId, JogadorId jogadorId) {
        jogadoresPorPartida.computeIfAbsent(partidaId, k -> new HashSet<>()).add(jogadorId);
    }

    @Override
    public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
        UsuarioId organizador = organizadores.get(torneioId);
        return organizador != null && organizador.equals(usuarioId);
    }

    @Override
    public boolean partidaPertenceAoTorneio(PartidaId partidaId, TorneioId torneioId) {
        TorneioId torneio = partidaTorneio.get(partidaId);
        return torneio != null && torneio.equals(torneioId);
    }

    @Override
    public boolean jogadorPertenceAosTimesDaPartida(PartidaId partidaId, JogadorId jogadorId) {
        Set<JogadorId> jogadores = jogadoresPorPartida.get(partidaId);
        return jogadores != null && jogadores.contains(jogadorId);
    }

    public void limpar() {
        organizadores.clear();
        partidaTorneio.clear();
        jogadoresPorPartida.clear();
    }
}
